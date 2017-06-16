package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.PressureCooker;

public class PressureCookersPart extends ScreenPart {

	private ScrollPane pane;
	private Table scrollTable;
	ArrayList<PressureCookerTable> cookerTables = new ArrayList<PressureCookerTable>();
	
	public PressureCookersPart( ) {
		super( "Pressure Cookers" );
	}

	@Override
	public void populate() {
		clear();
		cookerTables.clear();
		
		Alert alert = Assets.hasAlertWithID( ST.ALERT_PRESSURE_COOKER );
		while ( alert != null ) {
			Assets.removeAlert( alert );
			alert = Assets.hasAlertWithID( ST.ALERT_PRESSURE_COOKER );
		}
		
		scrollTable = new Table();

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
		
		ArrayList<PressureCooker> cookers = Assets.player.cookers;
		int index = 1;
		for ( final PressureCooker cooker: cookers ) {
			PressureCookerTable cookerTable = new PressureCookerTable( cooker, skin, index );
			
			scrollTable.add( cookerTable );

			scrollTable.row();
	 		cookerTables.add( cookerTable );
	 		
	 		index += 1;
		}
		
		button = new ScreenButton( "Buy Another Pressure Cooker", "CoinIcon", "useCoin", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to purchase a whole other pressure cooker for " + Assets.player.getPcIncreaseCost() +
 						" Blue Coins?",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.coins>= Assets.player.getPcIncreaseCost()  ) {
	 		 					Assets.player.changeCoins( -Assets.player.getPcIncreaseCost() );
	 		 					Assets.player.cookers.add( new PressureCooker( Assets.game ) );
	 		 					Dialogs.sweetDialog( "You bought another pressure cooker!", new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) {
			 				 				populated = false;
			 				 				needsResize = true;
			 				 			}
		 				 			}
		 		 				);
		 		 				Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "New Pressure Cooker", (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + Assets.player.getPcIncreaseCost() + "." );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		pane = new ScrollPane( scrollTable, skin);
		add( pane );
	}

	@Override
	protected void resize( ) {
		
		super.resize( );
		
		if ( cookerTables.size()!=Assets.player.cookers.size() )
			populate();
		
		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
		
		for ( PressureCookerTable cookerTable: cookerTables )
			cookerTable.resize( buttonWidth, buttonHeight, skin );
			
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

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public class PressureCookerTable extends Table {
		PressureCooker cooker;
		Image image;
		ProgressBar progressBar;
		float backgroundBorderWidth;
		
		public PressureCookerTable ( PressureCooker myCooker, Skin skin, int index ) {
			this.cooker = myCooker;
			
			Drawable background = skin.getDrawable( "ButtonDown");
			setBackground( background );
			setColor( skin.getColor("resource" ) );
			backgroundBorderWidth = background.getLeftWidth() + background.getRightWidth();
			
			image = new Image( skin.getDrawable( "PressureCooker") );
			image.setScaling( Scaling.fillY );
			add( image );
			
			ScreenLabel label = new ScreenLabel ( new FitLabel( "Pressure Cooker#" + index, skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= height + backgroundBorderWidth;
					super.resize( width, height );
				}
			};
			label.label.setColor( skin.getColor( "black" ) );
			label.label.setAlignment( Align.center );
			label.table = this;
			label.table.add( label.label);
			label.table.row();
			labels.add( label );

			progressBar = new ProgressBar( cooker, skin );
			add( progressBar ).colspan( 2 );
			row();
			
			ScreenButton button = new ScreenButton( "Start Cooking", "CookIcon", "action", skin ) {
				@Override
				public void resize( int width, int height ) {
					width -= backgroundBorderWidth;
					super.resize( width, height );
				}
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( cooker.doing!=-1 );
				}
			};
			button.table = this;
			button.table.add( button.fitButton ).colspan( 2 );
			button.table.row();
			buttons.add( button );
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Skin skin = Assets.getSkin();
	 				
	 				ArrayList<Button> buttons = new ArrayList<Button>();
	  				
	 				FitButton myButton = new FitButton( "Grain Jars ($60)", skin.getRegion( "GrainJarIcon" ), skin );
	 				myButton.left();
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.getCash()>=60 ) {
	 		 					Assets.player.changeCash( -60 );
	 		 					cooker.doing = 4;
	 		 					cooker.progress = 0;
//	 		 					parentScreen.resize( w, h );
	 		 					Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQDA" );
	 		 				} else {
	 		 					Dialogs.acceptDialog( "You don't have enough cash for that." );
	 		 				}
	 		 			}
	 		 		});
	 				buttons.add( myButton );
	 				
	 				for ( int i = 0; i < 4; i ++ ) {
	 					final int cost = 100 + 100 * i;
		 				myButton = new FitButton( ST.substrates[i] + " Jars ($" + cost + ")", skin.getRegion( "SubJarIcon" ), skin );
		 				myButton.setDisabled( !Assets.player.canSubstrate( i ) );
		 				myButton.left();
		 				final int j = i;
		 				myButton.addListener( new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				if ( Assets.player.getCash()>=cost ) {
		 		 					Assets.player.changeCash( -cost );
		 		 					cooker.doing = j;
		 		 					cooker.progress = 0;
//		 		 					parentScreen.resize( w, h );
		 		 				} else {
		 		 					Dialogs.acceptDialog( "You don't have enough cash for that." );
		 		 				}
		 		 			}
		 		 		});
		 				buttons.add( myButton );
	 				}

	 				Dialogs.scrollTableDialog( "Please select a type of substrate to cook:", buttons );
	 			}
	 		});
			
			button = new ScreenButton( "Fast Forward", "CoinIcon", "useCoin", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( cooker.doing==-1 );
				}
				@Override
				public void resize( int width, int height ) {
					width -= backgroundBorderWidth;
					super.resize( width, height );
				}
			};
			button.table = this;
			button.table.add( button.fitButton ).colspan( 2 );
			button.table.row();
			buttons.add( button );
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				final int coin_cost = 1 + cooker.getTimeLeft()/(60*60);
	 				if ( Assets.player.coins >= coin_cost ) {
	 					Dialogs.choiceDialog( "Would you like to fast forward this pressure cooker to fully cooked for " + coin_cost + " Blue Coin(s)?",
	 						new ChangeListener() {
	 		 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 		 				Assets.player.changeCoins( - coin_cost );
 	 		 		 			Tween.to( cooker, PressureCooker.PressureCookerTween.PROGRESS, 2.0f )
 	 		 			        .target( ST.PC_TIME )
 	 		 			        .ease(TweenEquations.easeInQuad)
 	 		 			        .start( Assets.getTweenManager() ); //** start it
 	 		 		 			Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Pressure Cooker FF", (long)0 );
	 		 		 			}
		 					});
	 				} else {
	 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + coin_cost + "." );
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Upgrade Capacity [" + ST.PC_CAPACITIES[cooker.capacity] + "]", "CashIcon", "action", skin ) {
				int oldCap = -1;
				@Override
				public void act( float delta ) {
					if ( oldCap!=cooker.capacity ) {
						oldCap = cooker.capacity;
						fitButton.setText( "Upgrade Capacity [" + ST.PC_CAPACITIES[cooker.capacity] + "]" );
					}
					fitButton.setDisabled( cooker.capacity>=3 || cooker.capacity>= Assets.player.getMaxPcUpgrade() );
				}
				@Override
				public void resize( int width, int height ) {
					width -= backgroundBorderWidth;
					super.resize( width, height );
				}
			};
			button.table = this;
			button.table.add( button.fitButton ).colspan( 2 );
			button.table.row();
			buttons.add( button );
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Dialogs.choiceDialog( "Would you like to upgrade the capacity of this pressure cooker for $" + ST.withComas(ST.PC_CAP_PRICES[cooker.capacity + 1]) + "?",
						new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				if ( Assets.player.getCash()>= ST.PC_CAP_PRICES[cooker.capacity + 1] ) {
		 		 					Assets.player.changeCash( -ST.PC_CAP_PRICES[cooker.capacity + 1] );
		 		 					cooker.capacity += 1;
		 		 					Dialogs.sweetDialog( "You increased the capacity to " + ST.PC_CAPACITIES[cooker.capacity] + "!", new ChangeListener() {
				 				 			public void changed (ChangeEvent event, Actor actor) { }
			 				 			}
			 		 				);
			 		 				// if the player has an objective
			 		 				if ( Assets.player.objective!=null && 
			 		 						Assets.player.objective.type== Assets.player.objective.UPGRADE &&
			 		 						Assets.player.objective.text.equals("Pc capacity") ) {
			 		 					Dialogs.objectiveDialog( "You successfully upgraded the capacity in a pressure cooker!" );
			 		 					Assets.player.objective = null;
			 							if ( Assets.player.objective_alert != null ) {
			 								Assets.removeAlert( Assets.player.objective_alert );
			 								Assets.player.objective_alert = null;
			 							}
			 							Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Upgrade Pc Capacity", (long)0 );
			 		 				}
			 		 				Assets.getResolver().trackerSendEvent( "Upgrade", "Pressure Cooker", "Capacity: " + ST.PC_CAPACITIES[cooker.capacity], (long)0 );
		 		 				} else {
		 		 					Dialogs.acceptDialog( "You do not have enough cash for this purchase" );
		 		 				}
		 		 				
		 		 			}
						}
					);
	 			}
	 		});
			
			
		}
		
		public void resize( float width, float height, Skin skin ) {
			Drawable background = skin.getDrawable( "ButtonDown");
			setBackground( background );

			backgroundBorderWidth = background.getLeftWidth() + background.getRightWidth();

			image.setDrawable( skin.getDrawable( "PressureCooker") );
			getCell( image ).width( height ).height( height );

			progressBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
			getCell( progressBar ).width( width - backgroundBorderWidth ).height( height );
		}
		
	}
	
	
	public static class PressureCookersBuilder implements ScreenPartBuilder<PressureCookersPart> {
		@Override
		public PressureCookersPart build() {
			PressureCookersPart part = new PressureCookersPart( );
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
