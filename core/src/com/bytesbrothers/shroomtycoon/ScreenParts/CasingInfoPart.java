package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.Align;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.CasingDrawer;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Project;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;


public class CasingInfoPart extends ScreenPart {

	public Casing theCasing;
	public CasingDrawer drawer;
	
	private Table scrollTable;
	private Image icon;
	private ProgressBar progressBar;
	private ProgressBar maintenanceBar;
	private Label nextLabel;
	private Label locationLabel;
	private ScrollPane pane;
	
	public CasingInfoPart( Casing casing ) {
		super( "CasingInfo" );
		drawer = Assets.game.casingDrawer;
		setCasing( casing );
		
		renderable = true;
	}
	
	@Override
	public void populate() {
		clear();
		
		if ( theCasing!=null ) {
			
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
			
			ScreenLabel label = new ScreenLabel( new FitLabel( theCasing.name, skin ) );
			label.scaleY = 0.9f;
			label.table = scrollTable;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();

			icon = new Image();
			scrollTable.add( icon );
			scrollTable.row();
			
			progressBar = new ProgressBar( theCasing, skin );
			scrollTable.add( progressBar );
			scrollTable.row();
			
			maintenanceBar = new ProgressBar( theCasing.getMaintenanceBar(), skin.get( "orange", ProgressBar.ProgressBarStyle.class ) );
			scrollTable.add( maintenanceBar );
			scrollTable.row();
			
			nextLabel = new Label( theCasing.whatsNext(), skin.get( LabelStyle.class ) );
			nextLabel.setAlignment( Align.center );
			nextLabel.setWrap( true );
			scrollTable.add( nextLabel );
			scrollTable.row();
			
			locationLabel = new Label( "Location: " + ST.locations[theCasing.location], skin.get( LabelStyle.class ) );
			locationLabel.setAlignment( Align.center );
			locationLabel.setWrap( true );
			scrollTable.add( locationLabel );
			scrollTable.row();
			
			button = new ScreenButton( "Strain: " + theCasing.strain.name, "SyringeIcon", "resource", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			button.table.row();
			buttons.add( button );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				StrainPart screen = Assets.getScreenHandler().getScreenPart( StrainPart.class );
//	 				StrainPart screen = (StrainPart)Assets.game.getScreenPart( "Strain" );
	 				screen.setStrain( StrainMaster.getStrain( theCasing.strain.name ) );
	 				screen.setDx( 0 );
	 				setDx( -1f );
	 				
	 				Assets.getScreenHandler().setActivePart( screen );
	 			}
	 		});
			

			button = new ScreenButton( "View", "MoveCameraIcon", "newColumn", skin ) {
//				@Override
//				public void act( float delta ) {
//					fitButton.setDisabled( theJar.location!=ST.INCUBATOR || theJar.getProgress()==1.0f );
//				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				ShroomClass game = Assets.game;
	 				CasingPart casingPart = Assets.getScreenHandler().getScreenPart( CasingPart.class );
//	 				CasingPart casingPart = (CasingPart) game.getScreenPart( "Casing" );
//	 				ActionBarPart actionBarPart = (ActionBarPart) game.getScreenPart( "Action Bar" );
	 				ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
	 				casingPart.setCasing( theCasing, CasingPart.LOOKING );
	 				casingPart.setDx( 0 );
	 				actionBarPart.setDy( -game.actionBarHeight );
	 				setDx( -1f );
	 				Assets.getScreenHandler().setActivePart( casingPart );
	 			}
	 		});
			
			button = new ScreenButton( "Fast Forward", "FastForwardIcon", "useCoin", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( theCasing.location==ST.FRIDGE || theCasing.getProgress()==1.0f  );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				final int coin_cost = 1 + theCasing.getTimeLeft()/(60*60);
	 				if ( Assets.player.coins >= coin_cost ) {
	 					Dialogs.choiceDialog( "Would you like to fast forward this casing for " + coin_cost + " Blue Coin(s)?",
	 						new ChangeListener() {
	 		 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 		 				ShroomClass game = Assets.game;
	 		 		 				CasingPart casingPart = Assets.getScreenHandler().getScreenPart( CasingPart.class );
//	 		 		 				CasingPart casingPart = (CasingPart) game.getScreenPart( "Casing" );
//	 		 		 				ActionBarPart actionBarPart = (ActionBarPart) game.getScreenPart( "Action Bar" );
	 		 		 				ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
	 		 		 				casingPart.setCasing( theCasing, CasingPart.FAST_FORWARD );
	 		 		 				casingPart.fSpeed = (float)theCasing.getTimeLeft() * 1f;
	 		 		 				casingPart.fDelay = 0.0f;
	 		 		 				Assets.player.changeCoins( -coin_cost );
	 		 		 				casingPart.setDx( 0 );
	 		 		 				actionBarPart.setDy( -game.actionBarHeight );
	 		 		 				setDx( -1f );
	 		 		 				Assets.getScreenHandler().setActivePart( casingPart );
		 		 					Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Casing FF", (long)0 );
	 		 		 			}
		 					}
	 					);
	 				} else {
	 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + coin_cost + "." );
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Maintenance", "MaintainIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					float percent = (float)theCasing.maintenance/(float)ST.getCasingMaintenance( theCasing.strain.temp_required, Assets.player.inc_temp );
					if ( theCasing.location==ST.FC )
						 percent = (float)theCasing.maintenance/(float)ST.getCasingMaintenance( theCasing.strain.temp_required, Assets.player.fc_temp );
					fitButton.setDisabled( percent<=0.3f || !theCasing.getsMaintenance() );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				// give the player a little xp
	 				float percent = (float)theCasing.maintenance/(float)ST.getJarMaintenance( theCasing.strain.temp_required, Assets.player.inc_temp );
	 				if ( theCasing.location==ST.FC )
	 					 percent = (float)theCasing.maintenance/(float)ST.getJarMaintenance( theCasing.strain.temp_required, Assets.player.fc_temp );
	 				Assets.player.increaseXP( (int)(ST.MAINTENANCE_XP*ST.logisticSpread( percent ) ) );
	 				
	 				ST.focus( pane, maintenanceBar );
	 				
	 				// reduce the jar's maintenance to 0
	 				theCasing.maintenance = 0;
	 			}
	 		});
			
			button = new ScreenButton( "Harvest", "HarvestIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( !theCasing.isHarvestable() ) ;
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				if ( theCasing.mold_contaminated ) {
	 					Dialogs.acceptDialog( "You can't harvest those mushrooms. This casing is contaminated." );
	 				} else {
	 					ShroomClass game = Assets.game;
	 					CasingPart casingPart = Assets.getScreenHandler().getScreenPart( CasingPart.class );
//		 				CasingPart casingPart = (CasingPart) game.getScreenPart( "Casing" );
//		 				ActionBarPart actionBarPart = (ActionBarPart) game.getScreenPart( "Action Bar" );
		 				ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
 		 				casingPart.setCasing( theCasing, CasingPart.HARVEST );
 		 				casingPart.setDx( 0 );
 		 				actionBarPart.setDy( -game.actionBarHeight );
 		 				setDx( -1f );
 		 				Assets.getScreenHandler().setActivePart( casingPart );

	 					if ( !theCasing.beenPrinted ) {
	 						Dialogs.choiceDialog( "Would you like to take a spore print of this casing before you harvest?",
		 						new ChangeListener() {
		 		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 		 				Project newProject = new Project( "Spore Print", "PrintIcon" );
		 		 						newProject.message = "A spore print was collected from " + theCasing.name + " and made into a syringe!";
		 		 						newProject.addEvent( new Project.ProjectEvent() {
		 		 							public void run() {
		 		 								theCasing.beenPrinted = true;
		 		 								
		 		 								Strain thisStrain = StrainMaster.getStrain( theCasing.strain.name );
		 		 								if ( thisStrain!=null )
		 		 									Assets.player.syringes.add( thisStrain );
		 		 								Assets.player.totalSyringesMade += 1;
		 		 								Assets.player.increaseXP( 20 );
		 		 								Assets.getResolver().trackerSendEvent( "Strain", thisStrain.name, "Spore Print", (long)0 );
		 		 							}
		 		 						});
		 		 						newProject.nextPart = CasingInfoPart.this;
		 								Assets.game.startProjectPart( newProject );
		 		 		 			}
			 					}
		 					);
	 					}
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Spore Print", "PrintIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( !theCasing.isHarvestable() || theCasing.beenPrinted );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				if ( theCasing.mold_contaminated ) {
	 					Dialogs.acceptDialog( "You can't/don't want to take a syringe. This casing is contaminated." );
	 				} else {
	 					Project newProject = new Project( "Spore Print", "PrintIcon" );
						newProject.message = "A spore print was collected from " + theCasing.name + " and made into a syringe!";
						newProject.addEvent( new Project.ProjectEvent() {
							public void run() {
								System.out.println( "Spore Print completed" );
								theCasing.beenPrinted = true;
								
								Strain thisStrain = StrainMaster.getStrain( theCasing.strain.name );
								if ( thisStrain!=null )
									Assets.player.syringes.add( thisStrain );
								Assets.player.totalSyringesMade += 1;
								Assets.player.increaseXP( 20 );
								Assets.getResolver().trackerSendEvent( "Strain", thisStrain.name, "Spore Print", (long)0 );
							}
						});
						newProject.nextPart = CasingInfoPart.this;
						Assets.game.startProjectPart( newProject );
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Relocate", "MoveIcon", "action", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				ArrayList<Button> buttons = new ArrayList<Button>();

	 				FitButton myButton = new FitButton( "To Incubator", skin.getRegion( "IncIcon" ), skin );
	 				myButton.left();
	 				myButton.setDisabled( theCasing.location==ST.INCUBATOR ) ;
	 				myButton.setColor( !myButton.isDisabled()? skin.getColor( "laboratory" ) : skin.getColor( "disabled" ) );
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				int spaced = Assets.player.numJars(ST.INCUBATOR) + Assets.player.numCasings(ST.INCUBATOR);
	 		 				if ( spaced>=Assets.player.inc_capacity ) {
	 		 					Dialogs.acceptDialog( "There is not enough room in the incubator" );
	 		 				} else {
	 		 					Dialogs.relocateDialog( false, theCasing.location, ST.INCUBATOR );
	 		 					theCasing.location = ST.INCUBATOR;
	 		 					locationLabel.setText( "Location: " + ST.locations[theCasing.location] );
	 		 				}
	 		 			}
	 		 		});
	 				
	 				myButton = new FitButton( "To Fruiting Chamber", skin.getRegion( "FcIcon" ), skin );
	 				myButton.setColor( skin.getColor( "laboratory" ) );
	 				myButton.left();
	 				myButton.setDisabled( theCasing.location==ST.FC ) ;
	 				myButton.setColor( !myButton.isDisabled()? skin.getColor( "laboratory" ) : skin.getColor( "disabled" ) );
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				int spaced = Assets.player.numJars(ST.FC) + Assets.player.numCasings(ST.FC);
	 		 				if ( spaced>=Assets.player.fc_capacity ) {
	 		 					Dialogs.acceptDialog( "There is not enough room in the fruiting chamber" );
	 		 				} else {
	 		 					Dialogs.relocateDialog( false, theCasing.location, ST.FC );
	 		 					theCasing.moveIntoFc();
	 		 					populated = false;
	 		 					locationLabel.setText( "Location: " + ST.locations[theCasing.location] );
	 		 				}
	 		 			}
	 		 		});

	 				myButton = new FitButton( "To Fridge", skin.getRegion( "FridgeIcon" ), skin );
	 				myButton.setColor( skin.getColor( "laboratory" ) );
	 				myButton.left();
	 				myButton.setDisabled( theCasing.location==ST.FRIDGE || !Assets.player.canFridge() ) ;
	 				myButton.setColor( !myButton.isDisabled()? skin.getColor( "laboratory" ) : skin.getColor( "disabled" ) );
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				int spaced = Assets.player.numJars(ST.FRIDGE) + Assets.player.numCasings(ST.FRIDGE);
	 		 				if ( spaced>= Assets.player.fridge_capacity ) {
	 		 					Dialogs.acceptDialog( "There is not enough room in the fridge" );
	 		 				} else {
	 		 					Dialogs.relocateDialog( false, theCasing.location, ST.FRIDGE );
	 		 					theCasing.location = ST.FRIDGE;
	 		 					locationLabel.setText( "Location: " + ST.locations[theCasing.location] );
	 		 				}
	 		 			}
	 		 		});
	 				Dialogs.scrollTableDialog( "Move this casing?", buttons );
	 			}
	 		});
			
			button = new ScreenButton( "Soak", "SoakIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( !Assets.player.canSoak() || theCasing.location!=ST.FC || theCasing.getProgress()!= 1.0f || 
							theCasing.mold_contaminated
							|| theCasing.soaking!=-1 || theCasing.isHarvestable() );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Dialogs.choiceDialog( "Would you like to soak this casing and get another flush?",
 						new ChangeListener() {
 		 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 		 			Project newProject = new Project( "Soak Casing", "SoakIcon" );
	 		 	 				newProject.message = "" + theCasing.name + " was submerged in distilled water." +
	 		 	 						" Leave it in the incubator for some time then drain it and put it in the" +
	 		 	 						" Fruiting Chamber.";
	 		 	 				newProject.completionTime = ST.PROJECT_TIME;
	 		 	 				newProject.addEvent( new Project.ProjectEvent() {
	 		 	 					public void run() {
	 		 	 						theCasing.soaking = 0;
	 		 	 						theCasing.location = ST.INCUBATOR;
	 		 	 						Assets.player.increaseXP( 30 );
	 		 	 						Assets.getResolver().trackerSendEvent( "Strain", theCasing.strain.name, "Soak", (long)0 );
	 		 	 					}
	 		 	 				});
	 		 	 				newProject.nextPart = CasingInfoPart.this;
	 		 	 				Assets.game.startProjectPart( newProject );
 		 		 			}
 	 					}
 					);
	 			}
	 		});
			
			button = new ScreenButton( "Throw Away", "TrashIcon", "action", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Dialogs.choiceDialog( "Throw this casing away?",
 						new ChangeListener() {
 		 		 			public void changed (ChangeEvent event, Actor actor) {
 		 		 				Assets.player.casings.remove( theCasing );
 	 		 		 			if ( theCasing.mold_contaminated ) {
 	 		 		 				Assets.player.totalContaminations += 1;
 	 		 		 				Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQCQ" );
 	 		 		 			}
 		 		 				back();
 		 		 			}
 	 					}
 					);
	 			}
	 		});
			
			pane = new ScrollPane( scrollTable, skin);
			pane.setFadeScrollBars( false );
			add( pane );
			
		}
	}
	
	@Override
	public Object getObject( ) {
		return theCasing;
	}
	
	@Override
	public void passObject( Object object ) {
		if ( object!=null && object.getClass().equals( Casing.class ) )
			setCasing( (Casing)object );
	}
	
	public void setCasing( Casing casing ) {
		theCasing = casing;
		if ( theCasing!=null )
			theCasing.useAlert();
		drawer.setCasing( theCasing );
		populated = false;
	}
	
	@Override
	protected void resize( ) {
		
		super.resize( );
		
		if ( theCasing!=null ) {
			drawer.resize();
			
			float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
			
			scrollTable.getCell( icon )
				.width( buttonWidth * 0.25f )
				.height( buttonHeight*2.0f )
				.padBottom( buttonHeight * 0.25f ).padTop( buttonHeight * 0.25f );
			
			progressBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
			scrollTable.getCell( progressBar )
				.width( buttonWidth ).height( buttonHeight * 0.5f );
			invalidateHierarchy();
			
			maintenanceBar.setStyle( skin.get( "orange", ProgressBar.ProgressBarStyle.class ) );
			scrollTable.getCell( maintenanceBar )
				.width( buttonWidth ).height( buttonHeight * 0.5f );
			invalidateHierarchy();
			
			nextLabel.setStyle( skin.get( LabelStyle.class ) );
			nextLabel.setText( theCasing.whatsNext() );
			nextLabel.setWidth( buttonWidth );
			scrollTable.getCell( nextLabel )
				.width( buttonWidth ).height( nextLabel.getPrefHeight() );
			
			locationLabel.setStyle( skin.get( LabelStyle.class ) );
			locationLabel.setWidth( buttonWidth );
			scrollTable.getCell( locationLabel )
				.width( buttonWidth ).height( locationLabel.getPrefHeight() );
			
			pane.setStyle( skin.get( ScrollPaneStyle.class ) );
			getCell( pane )
				.width( Assets.width*desiredWidth )
				.height( Assets.height*desiredHeight );
		}
	}
	
	@Override
    public void render( ) {
		if ( populated && theCasing!=null ) {
			drawer.render();
		}
    }
	
	@Override
	public void dispose() {
		drawer.dispose();
	}

	@Override
	public void back() {
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );
//		ScreenPart screen = Assets.game.getScreenPart( "Main" );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public static class CasingInfoBuilder implements ScreenPartBuilder<CasingInfoPart> {
		@Override
		public CasingInfoPart build() {
			CasingInfoPart part = new CasingInfoPart( null );
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
