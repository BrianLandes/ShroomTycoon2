package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class SyringeStorePart extends ScreenPart {

	private ScrollPane pane;
	private Table scrollTable;
	
	int lastTotal = 0;
	
	public SyringeStorePart( ) {
		super( "Syringe Store" );
	}

	@Override
	public void populate() {
		clear();

		scrollTable = new Table();
		
		lastTotal = Assets.player.strains.size() + Assets.player.serverStrains.size();
		
		ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				back();
 			}
 		});
		
		ScreenLabel label = new ScreenLabel( new FitLabel( "Buyable Syringes", skin ) );
		label.table = scrollTable;
		label.table.add( label.label ).padTop( 14 );
		labels.add( label );
		label.table.row();

		ArrayList<Strain> strains = StrainMaster.getStrains();
		ArrayList<String> buyable = Assets.player.can_buy_strains;

		int index = 0;
		int[] list = Assets.player.getItemizedSyringes();
		
		for ( final Strain strain: strains ) {
			boolean show = false;
			Iterator<String> canBuyIt = buyable.iterator();
			while( canBuyIt.hasNext() && !show ) {
				String string= canBuyIt.next();
				if ( string.equals( strain.name ) )
					show = true;
			}
			if ( show ) {
				button = new ScreenButton( "[" + list[index] + "] " + strain.name, "SyringeIcon", "newColumn", skin );
				if ( Assets.player.reg_discount.equals( strain.name ) )
					button.iconName = "DiscountIcon";
				button.table = scrollTable;
				button.table.add( button.fitButton );
				button.table.row();
				buttons.add( button );

				button.fitButton.addListener( new ChangeListener() {
		 			public void changed (ChangeEvent event, Actor actor) {
		 				StrainPart screen = Assets.getScreenHandler().getScreenPart( StrainPart.class );
 		 				screen.setStrain( StrainMaster.getStrain( strain.name ) );
 		 				screen.setDx( 0 );
 		 				setDx( -1f );
 		 				
 		 				Assets.getScreenHandler().setActivePart( screen );
		 			}
		 		});
				
			}
			
			index ++;
		}
		
		ArrayList<Strain> customStrains = Assets.player.strains;
		for ( final Strain strain: customStrains ) {

			button = new ScreenButton( "[" + list[index] + "] " + strain.name, "SyringeIcon", "newColumn", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			button.table.row();
			buttons.add( button );

			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				StrainPart screen = Assets.getScreenHandler().getScreenPart( StrainPart.class );
		 				screen.setStrain( StrainMaster.getStrain( strain.name ) );
		 				screen.setDx( 0 );
		 				setDx( -1f );
		 				
		 				Assets.getScreenHandler().setActivePart( screen );
	 			}
	 		});
			
			index ++;
		}
		
		ArrayList<Strain> serverStrains = Assets.player.serverStrains;
		for ( final Strain strain: serverStrains ) {

			button = new ScreenButton( "[" + list[index] + "] " + strain.name, "SyringeIcon", "newColumn", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			button.table.row();
			buttons.add( button );

			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				StrainPart screen = Assets.getScreenHandler().getScreenPart( StrainPart.class );
		 				screen.setStrain( StrainMaster.getStrain( strain.name ) );
		 				screen.setDx( 0 );
		 				setDx( -1f );
		 				
		 				Assets.getScreenHandler().setActivePart( screen );
	 			}
	 		});
			
			index ++;
		}
		
		pane = new ScrollPane( scrollTable, skin);
		add( pane );
	}
	
	@Override
	public void screenAct( float delta ) {
		int newTotal = Assets.player.strains.size() + Assets.player.serverStrains.size();
		if ( newTotal!=lastTotal )
			populated = false;
	}

	@Override
	protected void resize( ) {
		
		super.resize( );

		pane.setStyle( skin.get( ScrollPaneStyle.class ) );
		getCell( pane )
			.width( Assets.width*desiredWidth )
			.height( Assets.height*desiredHeight );
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void back() {
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );
//		ScreenPart screen = Assets.game.getScreenPart( "Main" );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public static class SyringeStoreBuilder implements ScreenPartBuilder<SyringeStorePart> {
		@Override
		public SyringeStorePart build() {
			SyringeStorePart part = new SyringeStorePart( );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2.0f );
			part.setY( Assets.height * (0.5f+(Assets.game.actionBarHeight*0.5f)) );
			part.desiredX = 1f;
			part.desiredY = Assets.game.actionBarHeight;
			part.desiredWidth = 1f;
			part.desiredHeight = 1f - Assets.game.actionBarHeight;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
}
