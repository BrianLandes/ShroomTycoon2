package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.StashPart;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;

import java.util.ArrayList;
import java.util.Hashtable;

public class ScreenHandler {
	
	private Hashtable<Class<? extends ScreenPart>, ScreenPartBuilder<? extends ScreenPart>> screenMakers =
			new Hashtable<Class<? extends ScreenPart>, ScreenPartBuilder<? extends ScreenPart>>();
	
	public ScreenPart activePart;
	
	public ArrayList<ScreenPart> screenParts = new ArrayList<ScreenPart>();
	public ActionBarPart actionBar;

	public ScreenHandler() {
		screenMakers.put( MainPart.class, new MainPart.MainBuilder() );
		screenMakers.put( JarPart.class, new JarPart.JarBuilder() );
		screenMakers.put( CasingInfoPart.class, new CasingInfoPart.CasingInfoBuilder() );
		screenMakers.put( CasingPart.class, new CasingPart.CasingBuilder() );
		screenMakers.put( ProjectPart.class, new ProjectPart.ProjectBuilder() );
		screenMakers.put( ProjectCompletedPart.class, new ProjectCompletedPart.ProjectBuilder() );
		screenMakers.put( PressureCookersPart.class, new PressureCookersPart.PressureCookersBuilder() );
		screenMakers.put( DehydratorsPart.class, new DehydratorsPart.DehydratorsBuilder() );
		screenMakers.put( StrainPart.class, new StrainPart.StrainBuilder() );
		screenMakers.put( WeighPart.class, new WeighPart.WeighBuilder() );
		screenMakers.put( DealerPart.class, new DealerPart.DealerBuilder() );
		screenMakers.put( StashPart.class, new StashPart.StashBuilder() );
		screenMakers.put( SyringesOwnedPart.class, new SyringesOwnedPart.SyringesOwnedBuilder() );
		screenMakers.put( SyringeStorePart.class, new SyringeStorePart.SyringeStoreBuilder() );
//		screenMakers.put( SyringesServerPart.class, new SyringesServerPart.SyringesServerBuilder() );
//		screenMakers.put( StrainInfoPart.class, new StrainInfoPart.StrainInfoBuilder() );
		screenMakers.put( EnterCodePart.class, new EnterCodePart.EnterCodeBuilder() );
		screenMakers.put( CheatPart.class, new CheatPart.CheatBuilder() );
		screenMakers.put( StrainSimPart.class, new StrainSimPart.StrainSimBuilder() );
		screenMakers.put( InoculationPart.class, new InoculationPart.InoculationBuilder() );
		screenMakers.put( NewDealersPart.class, new NewDealersPart.NewDealersBuilder() );
		screenMakers.put( MakeCasingPart.class, new MakeCasingPart.MakeCasingBuilder() );
		screenMakers.put( G2gPart.class, new G2gPart.G2gBuilder() );
		screenMakers.put( CombineStrainsPart.class, new CombineStrainsPart.CombineStrainsBuilder() );
		screenMakers.put( PlayerPart.class, new PlayerPart.PlayerBuilder() );
		screenMakers.put( ManageProfilesPart.class, new ManageProfilesPart.ManageProfilesBuilder() );
		screenMakers.put( MakeCasingPart.class, new MakeCasingPart.MakeCasingBuilder() );
		screenMakers.put( ActionBarPart.class, new ActionBarPart.ActionBarBuilder() );
		screenMakers.put( OptionsPart.class, new OptionsPart.OptionsBuilder() );
	}
	
//	public ScreenPart getScreenPart( String header ) {
//		for ( ScreenPart screenPart: screenParts ) {
//			if ( screenPart.header.equals( header ) )
//				return screenPart;
//		}
//		return null;
//	}
//	
	public <T> T getScreenPart( Class<T> screenClass ) {
		for ( ScreenPart screenPart: screenParts ) {
			if ( screenPart.getClass().equals( screenClass ) )
				return screenClass.cast( screenPart );
		}
		
		ScreenPart newScreen = screenMakers.get( screenClass ).build();
		screenParts.add( newScreen );
		if ( actionBar!=null )
			actionBar.setZIndex(100);
		return screenClass.cast( newScreen );
	}
	
	public void setActivePart( ScreenPart screenPart ) {
//		if ( screenPart!=null ) {
			activePart = screenPart;
//		} else
//			activePart = null;
			Assets.game.updateInputs();
	}
	
	public void setActivePart( Class<? extends ScreenPart> screenClass ) {
		
		ScreenPart part = getScreenPart( screenClass );
		setActivePart( part );
	}
	
//	public String getActivePartHeader() {
//		if ( activePart!=null )
//			return activePart.header;
//		return "none";
//	}
//	
//	public boolean isActivePartAndObject( Class<? extends ScreenPart> screenClass, Object object ) {
//		if ( activePart==null || object==null )
//			return false;
//		if ( activePart.)
//		Object partObject = activePart.getObject();
//		if ( partObject == null ) 
//			return false;
//	}

	public boolean isActivePartObjectEquals( Object object ) {
		if ( activePart==null || object==null )
			return false;
		Object partObject = activePart.getObject();
		if ( partObject == null ) 
			return false;
		if ( object.equals( partObject ) )
			return true;
		return false;
	}
	
	public void setActivePartDx( float x ) {
		if ( activePart!=null )
			activePart.setDx( x );
	}

	public void dispose() {
		for ( ScreenPart screenPart: screenParts ) {
			screenPart.dispose();
		}
	}
}
