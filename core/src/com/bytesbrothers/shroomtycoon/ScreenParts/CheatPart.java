package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Dealer;
import com.bytesbrothers.shroomtycoon.structures.Fruit;
import com.bytesbrothers.shroomtycoon.structures.Jar;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class CheatPart extends ScreenPart {

	private ScrollPane pane;
	private Table scrollTable;
	
	private Strain theStrain;

	private TextField samsDebtField;
	
	public CheatPart( ) {
		super( "Cheat" );
	}

	@Override
	public void populate() {
		clear();

		scrollTable = new Table();

		ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				back();
 			}
 		});
		
		button = new ScreenButton( "Add $1,000", "CashIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.changeCash( 1000 );
 			}
 		});
		
		button = new ScreenButton( "Add $1,000,000,000", "CashIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.changeCash( 1000000000 );
 			}
 		});
		
		button = new ScreenButton( "Add 10 BC", "CoinIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.changeCoins( 10 );
 			}
 		});
		
		button = new ScreenButton( "Add 1 Grain Jar", "GrainJarIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.grain_jars ++;
 			}
 		});
		
		button = new ScreenButton( "Add 1 Each Substrate Jar", "SubJarIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				for ( int i = 0; i < 4; i ++ )
 					Assets.player.sub_jars[i] ++;
 			}
 		});
		
		button = new ScreenButton( "Add Dealer (lvl 9)", "DealerIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.dealers.add( new Dealer( 9, Assets.game ) );
 			}
 		});
		
		button = new ScreenButton( "Add 100 XP", "FloatingXP", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.increaseXP( 100 );
 			}
 		});
		
		button = new ScreenButton( "Add 1 Level", "FloatingXP", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.increaseXP( Assets.player.next_level - Assets.player.current_xp );
 			}
 		});
		
		button = new ScreenButton( "Add XP->Unlock G2G", "FloatingXP", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( Assets.player.level<19)
 					while ( Assets.player.level<19 )
 						Assets.player.increaseXP( Assets.player.next_level - Assets.player.current_xp );
 				else
 					while ( Assets.player.level<27 )
 						Assets.player.increaseXP( Assets.player.next_level - Assets.player.current_xp );
 			}
 		});
		
		button = new ScreenButton( "Set Sam's Debt", "SamIcon", "action", skin );
		button.table = scrollTable;
		button.scaleX = 0.5f;
		button.table.add( button.fitButton );
//		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.debt = Integer.parseInt( samsDebtField.getText() );
 				if ( Assets.player.debt>0 && Assets.player.full_paid )
 					Assets.player.full_paid = false;
 				if ( Assets.player.debt> ST.SAMS_LOAN*0.5f && Assets.player.half_paid )
 					Assets.player.half_paid = false;
 			}
 		});
		samsDebtField = new TextField( samsDebtField==null?"" + Assets.player.debt:samsDebtField.getText(), skin );
		samsDebtField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(samsDebtField);
		scrollTable.row();
		
		button = new ScreenButton( theStrain==null?"(none)":theStrain.name, "SyringeIcon", "action", skin ) {
			String strain = "";
			@Override
			public void act ( float delta ) {
				if ( theStrain!=null && !strain.equals( theStrain.name ) ) {
					fitButton.setText( theStrain.name );
					strain = theStrain.name;
				}
			}
		};
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				ArrayList<Button> buttons = new ArrayList<Button>();
 				ArrayList<Strain> strains = StrainMaster.getStrains();
				int size = strains.size();
				for ( int index = 0; index<size; index ++ ) {
					final Strain currentStrain = strains.get( index );
					FitButton myButton = new FitButton( currentStrain.name, skin.getRegion( "SyringeIcon" ), skin );
	 				myButton.left();
	 				myButton.addListener( new ChangeListener() {
						public void changed (ChangeEvent event, Actor actor) {
	 		 				theStrain = currentStrain;
	 		 			}
	 		 		});
	 				buttons.add( myButton );
				}
				
				for ( int i = 0; i<Assets.player.strains.size(); i ++ ) {
					final Strain currentStrain = Assets.player.strains.get( i );
					FitButton myButton = new FitButton( currentStrain.name, skin.getRegion( "SyringeIcon" ), skin );
	 				myButton.left();
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				theStrain = currentStrain;
	 		 			}
	 		 		});
	 				buttons.add( myButton );
				}
  				
  				Dialogs.scrollTableDialog( "Please choose a strain:", buttons );
 			}
 		});
		
		button = new ScreenButton( "Add 1 Syringe", "SyringeIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( theStrain!=null )
 					Assets.player.syringes.add( theStrain );
 			}
 		});
		
		button = new ScreenButton( "Add 1 Jar", "JarIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( theStrain!=null ) {
 					String newName = theStrain.name;
 					newName += " " + (Assets.player.jar_enum<10?"0":"") + Assets.player.jar_enum;
 					Jar newJar = new Jar( newName, theStrain, Assets.game );
 					
 					Assets.player.jars.add( newJar );
 					Assets.player.jar_enum ++;
 					if ( Assets.player.jar_enum>99 )
 						Assets.player.jar_enum = 1;
 				}
 			}
 		});
		
		button = new ScreenButton( "Add 1 Casing", "CasingIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( theStrain!=null ) {
 					String newName = theStrain.name;
 					newName += " " + (Assets.player.jar_enum<10?"0":"") + Assets.player.jar_enum;

 					Assets.player.jar_enum ++;
 					if ( Assets.player.jar_enum>99 )
 						Assets.player.jar_enum = 1;
 	 				
 	 				Casing newCasing = new Casing( newName, theStrain, Assets.game );
 	 				newCasing.substrate = theStrain.substrate;
 	 				Assets.player.casings.add( newCasing );
 				}
 			}
 		});

		button = new ScreenButton( "Add 1 Batch", "BatchIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( theStrain!=null ) {
 					Batch newBatch = new Batch( theStrain );
 	 				int num_fruits = (int) (theStrain.batch_weight / theStrain.fruit_weight);
 	 				for ( int j = 0; j < num_fruits; j ++ ) {
 	 					Fruit newFruit = new Fruit( theStrain );
 	 					newFruit.growth = theStrain.fruit_grow_time;
 	 					newBatch.addFruit( newFruit );
 	 				}
 	 				
 	 				Assets.player.addBatch( newBatch );
 				}
 			}
 		});
		
		pane = new ScrollPane( scrollTable, skin);
		add( pane );
	}

	@Override
	protected void resize( ) {
		
		super.resize( );
		
		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
		
		samsDebtField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( samsDebtField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
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
		
		ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
//		ActionBarPart actionBarPart = (ActionBarPart) Assets.game.getScreenPart( "Action Bar" );
		actionBarPart.setDy( 0 );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public static class CheatBuilder implements ScreenPartBuilder<CheatPart> {
		@Override
		public CheatPart build() {
			CheatPart part = new CheatPart();
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2.0f );
			part.setY( Assets.height * 0.5f );
			part.desiredX = 1f;
			part.desiredY = 0;
			part.desiredWidth = 1f;
			part.desiredHeight = 1f;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
	
}
