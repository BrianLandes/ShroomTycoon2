package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class SyringesOwnedPart extends ScreenPart {

	private ScrollPane pane;
	private Table scrollTable;
	
	int lastTotal = 0;
	
	public SyringesOwnedPart( ) {
		super( "Syringes Owned" );
	}

	@Override
	public void populate() {
		clear();

		scrollTable = new Table();
		lastTotal = Assets.player.getNumSyringes();
		
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
		
		ScreenLabel label = new ScreenLabel( new FitLabel( "Owned Syringes", skin ) );
		label.table = scrollTable;
		label.table.add( label.label ).padTop( 14 );
		labels.add( label );
		label.table.row();
		
		boolean hasNone = true;
		
		Set<String> syringeNames = Assets.player.syringesCompressed.keySet();
		for ( final String thisString: syringeNames ) {
			Integer thisSyringeNum = Assets.player.syringesCompressed.get( thisString );

			if ( thisSyringeNum>0 ) {
				hasNone = false;
				button = new ScreenButton( thisString + " x" + thisSyringeNum, "SyringeIcon", "resource", skin );
				button.table = scrollTable;
				button.table.add( button.fitButton );
				button.table.row();
				buttons.add( button );
				
				button.fitButton.addListener( new ChangeListener() {
		 			public void changed (ChangeEvent event, Actor actor) {
		 				ArrayList<Button> buttons = new ArrayList<Button>();

		 				FitButton myButton = new FitButton( "Inoculate", skin.getRegion( "InnoculationIcon" ), skin );
		 				myButton.setColor( skin.getColor( "action" ));
		 				buttons.add( myButton );
		 				myButton.addListener( new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				InoculationPart screen = Assets.getScreenHandler().getScreenPart( InoculationPart.class );
		 		 				screen.setSyringe( StrainMaster.getStrain( thisString ) );
		 		 				screen.setDx( 0 );
		 		 				setDx( -1f );
		 		 				
		 		 				Assets.getScreenHandler().setActivePart( screen );
		 		 			}
		 		 		});
		 				
		 				myButton = new FitButton( "Combine...", skin.getRegion( "CustomStrainIcon" ), skin );
		 				myButton.setColor( skin.getColor( "action" ));
		 				buttons.add( myButton );
		 				myButton.addListener( new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				CombineStrainsPart screen = Assets.getScreenHandler().getScreenPart( CombineStrainsPart.class );
		 		 				screen.setSyringe( StrainMaster.getStrain( thisString ) );
		 		 				screen.setDx( 0 );
		 		 				setDx( -1f );
		 		 				
		 		 				Assets.getScreenHandler().setActivePart( screen );
		 		 			}
		 		 		});

		 				myButton = new FitButton( "View Details", skin.getRegion( "SyringeIcon" ), skin );
		 				myButton.setColor( skin.getColor( "newColumn" ));
		 				buttons.add( myButton );
		 				myButton.addListener( new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				StrainPart screen = Assets.getScreenHandler().getScreenPart( StrainPart.class );
		 		 				screen.setStrain( StrainMaster.getStrain( thisString ) );
		 		 				screen.setDx( 0 );
		 		 				setDx( -1f );
		 		 				
		 		 				Assets.getScreenHandler().setActivePart( screen );
		 		 			}
		 		 		});
		 				Dialogs.scrollTableDialog( thisString + "?", buttons );
		 				
		 			}
		 		});
			}
		}
		
		ArrayList<Strain> syringes = Assets.player.syringes;
		int i = 0;
		for( final Strain strain: syringes ) {
			button = new ScreenButton( strain.name, "SyringeIcon", "resource", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			button.table.row();
			buttons.add( button );

			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				ArrayList<Button> buttons = new ArrayList<Button>();

	 				FitButton myButton = new FitButton( "Inoculate", skin.getRegion( "InnoculationIcon" ), skin );
	 				myButton.setColor( skin.getColor( "action" ));
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				InoculationPart screen = Assets.getScreenHandler().getScreenPart( InoculationPart.class );
	 		 				screen.setSyringe( strain );
	 		 				screen.setDx( 0 );
	 		 				setDx( -1f );
	 		 				
	 		 				Assets.getScreenHandler().setActivePart( screen );
	 		 			}
	 		 		});
	 				
	 				myButton = new FitButton( "Combine...", skin.getRegion( "CustomStrainIcon" ), skin );
	 				myButton.setColor( skin.getColor( "action" ));
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				CombineStrainsPart screen = Assets.getScreenHandler().getScreenPart( CombineStrainsPart.class );
	 		 				screen.setSyringe( strain );
	 		 				screen.setDx( 0 );
	 		 				setDx( -1f );
	 		 				
	 		 				Assets.getScreenHandler().setActivePart( screen );
	 		 			}
	 		 		});

	 				myButton = new FitButton( "View Details", skin.getRegion( "SyringeIcon" ), skin );
	 				myButton.setColor( skin.getColor( "newColumn" ));
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				StrainPart screen = Assets.getScreenHandler().getScreenPart( StrainPart.class );
	 		 				screen.setStrain( StrainMaster.getStrain( strain.name ) );
	 		 				screen.setDx( 0 );
	 		 				setDx( -1f );
	 		 				
	 		 				Assets.getScreenHandler().setActivePart( screen );
	 		 			}
	 		 		});
	 				Dialogs.scrollTableDialog( strain.name + "?", buttons );
	 				
	 			}
	 		});

			i ++;
		}
		
		if ( i==0 && hasNone ) {
			label = new ScreenLabel( new FitLabel( "(none)", skin ) );
			label.table = scrollTable;
			label.table.add( label.label );
			labels.add( label );
		}
		
		pane = new ScrollPane( scrollTable, skin);
		pane.addListener( Assets.stopTouchDown() );
		add( pane );
	}
	
	@Override
	public void screenAct( float delta ) {
		int newTotal = Assets.player.getNumSyringes();
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

	public static class SyringesOwnedBuilder implements ScreenPartBuilder<SyringesOwnedPart> {
		@Override
		public SyringesOwnedPart build() {
			SyringesOwnedPart part = new SyringesOwnedPart( );
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
