package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar.ProgressBarStyle;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Jar;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class JarPart extends ScreenPart {

	public Jar theJar;
	private TextureAtlas jarAtlas;
	private int lastFrame;
	public boolean fastForwarding = false;
	public float fSpeed = 1.0f;
	public float fDelay = 0.0f;
	private Vector2 someShit;

	private ScrollPane pane;
	private Table scrollTable;
	private Image icon;
	private Sprite grainSprite;
	private Sprite jarSprite;
	private Sprite moldASprite;
	private Sprite moldBSprite;
	private float w2hRatio = 1.0f;
	private ProgressBar progressBar;
	private ProgressBar maintenanceBar;
	private Label nextLabel;
	private Label locationLabel;
	
	public JarPart( Jar jar ) {
		super( "Jar" );
		setJar( jar );
	}

	@Override
	public void populate() {
		clear();
		
		if ( theJar!=null ) {
			jarAtlas = Assets.getJarsAtlas();
			
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
			
			ScreenLabel label = new ScreenLabel( new FitLabel( theJar.name, skin ) );
			label.scaleY = 0.9f;
			label.table = scrollTable;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();
			
			lastFrame = theJar.getFrame();
			grainSprite = jarAtlas.createSprite( "JarBack" );
			jarSprite = jarAtlas.createSprite( "JarFront" );
			moldASprite = jarAtlas.createSprite( ST.JAR_MOLD_NAMES[ theJar.mold_typeA ], lastFrame );
			if ( theJar.contaminated ) {
				moldBSprite = jarAtlas.createSprite( ST.JAR_CONT_NAMES[ theJar.mold_typeB ], lastFrame );
	    	} else if ( theJar.mold_typeB!= -1 ) {
	    		moldBSprite = jarAtlas.createSprite( ST.JAR_MOLD_NAMES[theJar. mold_typeB ], lastFrame );
	    	} else {
	    		moldBSprite = null;
	    	}
			w2hRatio = jarSprite.getHeight()/jarSprite.getWidth();
			
			icon = new Image();
			scrollTable.add( icon );
			scrollTable.row();
			
			progressBar = new ProgressBar( theJar, skin );
			scrollTable.add( progressBar );
			scrollTable.row();
			
			maintenanceBar = new ProgressBar( theJar.getMaintenanceBar(), skin.get( "orange", ProgressBarStyle.class ) );
			scrollTable.add( maintenanceBar );
			scrollTable.row();
			
			nextLabel = new Label( theJar.whatsNext(), skin.get( LabelStyle.class ) );
			nextLabel.setAlignment( Align.center );
			nextLabel.setWrap( true );
			scrollTable.add( nextLabel );
			scrollTable.row();
			
			locationLabel = new Label( "Location: " + ST.locations[theJar.location], skin.get( LabelStyle.class ) );
			locationLabel.setAlignment( Align.center );
			locationLabel.setWrap( true );
			scrollTable.add( locationLabel );
			scrollTable.row();
			
			button = new ScreenButton( "Strain: " + theJar.strain.name, "SyringeIcon", "resource", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			button.table.row();
			buttons.add( button );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				StrainPart screen = Assets.getScreenHandler().getScreenPart( StrainPart.class );
//	 				StrainPart screen = (StrainPart)Assets.game.getScreenPart( "Strain" );
	 				screen.setStrain( StrainMaster.getStrain( theJar.strain.name ) );
	 				screen.setDx( 0 );
	 				setDx( -1f );
	 				
	 				Assets.getScreenHandler().setActivePart( screen );
	 			}
	 		});
			
			button = new ScreenButton( "Fast Forward", "FastForwardIcon", "useCoin", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( theJar.location!=ST.INCUBATOR || theJar.getProgress()==1.0f );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				final int coin_cost = 1 + theJar.getTimeLeft()/(60*60);
	 				if ( Assets.player.coins >= coin_cost ) {
	 					Dialogs.choiceDialog( "Would you like to fast forward this jar to fully grown for " + coin_cost + " Blue Coin(s)?",
	 						new ChangeListener() {
	 		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 					fSpeed = (float)theJar.getTimeLeft() * 1.5f;
		 		 					fDelay = 0.0f;
		 		 					Assets.player.changeCoins( -coin_cost );
		 		 					fastForwarding = true;
		 		 					pane.scrollTo( 0, icon.getY(), 0, icon.getHeight() );
		 		 					Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Jar FF", (long)0 );
	 		 		 			}
		 					}
	 					);
	 				} else
	 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + coin_cost + "." );
	 			}
	 		});
			
			button = new ScreenButton( "Maintenance", "MaintainIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					float percent = (float)theJar.maintenance/(float)ST.getJarMaintenance( theJar.strain.temp_required, Assets.player.inc_temp );
					fitButton.setDisabled( percent<=0.3f || !theJar.getsMaintenance() );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				// give the Assets.player a little xp
	 				float percent = (float)theJar.maintenance/(float)ST.getJarMaintenance( theJar.strain.temp_required, Assets.player.inc_temp );
	 				Assets.player.increaseXP( (int)(ST.MAINTENANCE_XP*ST.logisticSpread( percent ) ) );
	 				
	 				ST.focus( pane, maintenanceBar );
	 				
	 				// reduce the jar's maintenance to 0
	 				theJar.maintenance = 0;
	 			}
	 		});
			
			button = new ScreenButton( "Make Casing", "Jar2CasingIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( theJar.getProgress()!=1.0f );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				if ( theJar.contaminated ) {
	 					Dialogs.acceptDialog( "Contaminated jars cannot/should not be cased." );
	 				} else if ( Assets.player.getSubstrateJars()>0 ) {
	 					MakeCasingPart screen = Assets.getScreenHandler().getScreenPart( MakeCasingPart.class );
	 	 				screen.setJar( theJar );

	 	 				screen.setDx( 0 );
	 	 				setDx( -1f );
	 	 				
	 	 				Assets.getScreenHandler().setActivePart( screen );
	 				} else {
	 					Dialogs.acceptDialog( "You must have at least one jar of substrate." );
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "G2G Transfer", "G2gIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( !Assets.player.canG2g3() || theJar.getProgress()!=1.0f );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				if ( theJar.contaminated ) {
	 					Dialogs.acceptDialog( "Contaminated jars cannot/should not be used to innoculate others." );
	 				} else if ( Assets.player.grain_jars>0 ) {
	 					G2gPart screen = Assets.getScreenHandler().getScreenPart( G2gPart.class );
//	 					G2gPart screen = (G2gPart)Assets.game.getScreenPart( "G2g" );
	 	 				screen.setJar( theJar );

	 	 				screen.setDx( 0 );
	 	 				setDx( -1f );
	 	 				
	 	 				Assets.getScreenHandler().setActivePart( screen );
	 				} else {
	 					Dialogs.acceptDialog( "You must have at least one jar of grain." );
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
	 				myButton.setDisabled( theJar.location==ST.INCUBATOR ) ;
	 				myButton.setColor( !myButton.isDisabled()? skin.getColor( "laboratory" ) : skin.getColor( "disabled" ) );
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				int spaced = Assets.player.numJars(ST.INCUBATOR) + Assets.player.numCasings(ST.INCUBATOR);
	 		 				if ( spaced>=Assets.player.inc_capacity ) {
	 		 					Dialogs.acceptDialog( "There is not enough room in the incubator" );
	 		 				} else {
	 		 					Dialogs.relocateDialog( true, theJar.location, ST.INCUBATOR );
	 		 					theJar.location = ST.INCUBATOR;
	 		 					locationLabel.setText( "Location: " + ST.locations[theJar.location] );
	 		 				}
	 		 			}
	 		 		});

	 				myButton = new FitButton( "To Fridge", skin.getRegion( "FridgeIcon" ), skin );
	 				myButton.left();
	 				myButton.setDisabled( theJar.location==ST.FRIDGE || !Assets.player.canFridge() ) ;
	 				myButton.setColor( !myButton.isDisabled()? skin.getColor( "laboratory" ) : skin.getColor( "disabled" ) );
	 				buttons.add( myButton );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				int spaced = Assets.player.numJars(ST.FRIDGE) + Assets.player.numCasings(ST.FRIDGE);
	 		 				if ( spaced>= Assets.player.fridge_capacity ) {
	 		 					Dialogs.acceptDialog( "There is not enough room in the fridge" );
	 		 				} else {
	 		 					Dialogs.relocateDialog( true, theJar.location, ST.FRIDGE );
	 		 					theJar.location = ST.FRIDGE;
	 		 					locationLabel.setText( "Location: " + ST.locations[theJar.location] );
	 		 				}
	 		 			}
	 		 		});
	 				Dialogs.scrollTableDialog( "Move this jar?", buttons );
	 			}
	 		});
			
			button = new ScreenButton( "Throw Away", "TrashIcon", "action", skin );
			button.table = scrollTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			scrollTable.row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Dialogs.choiceDialog( "Throw this jar away?",
 						new ChangeListener() {
 		 		 			public void changed (ChangeEvent event, Actor actor) {
 	 		 		 			Assets.player.jars.remove( theJar );
 	 		 		 			if ( theJar.contaminated ) {
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
			add( pane );
		}
	}
	
	@Override
	public Object getObject() {
		return theJar;
	}
	
	@Override
	public void unpopulate() {
		theJar = null;
	}
	
//	@Override
//	public void clear() {
//		super.clear();
//		theJar = null;
//	}
	
	@Override
	public void passObject( Object object ) {
		if ( object!=null && object.getClass().equals( Jar.class ) )
			setJar( (Jar)object );
	}
	
	public void setJar( Jar jar ) {
		theJar = jar;
		populated = false;
	}
	
	@Override
	protected void resize( ) {
		
		super.resize( );
		
		if ( theJar!=null ) {
			jarAtlas = Assets.getJarsAtlas();
			
			float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
			
			lastFrame = theJar.getFrame();
			grainSprite = jarAtlas.createSprite( "JarBack" );
			jarSprite = jarAtlas.createSprite( "JarFront" );
			moldASprite = jarAtlas.createSprite( ST.JAR_MOLD_NAMES[ theJar.mold_typeA ], lastFrame );
			if ( theJar.contaminated ) {
				moldBSprite = jarAtlas.createSprite( ST.JAR_CONT_NAMES[ theJar.mold_typeB ], lastFrame );
	    	} else if ( theJar.mold_typeB!= -1 ) {
	    		moldBSprite = jarAtlas.createSprite( ST.JAR_MOLD_NAMES[theJar. mold_typeB ], lastFrame );
	    	} else {
	    		moldBSprite = null;
	    	}

			scrollTable.getCell( icon )
				.width( buttonWidth * 0.25f )
				.height( (buttonWidth * 0.25f ) * w2hRatio )
				.padBottom( buttonHeight * 0.25f ).padTop( buttonHeight * 0.25f );
			
			progressBar.setStyle( skin.get( ProgressBarStyle.class ) );
			scrollTable.getCell( progressBar )
				.width( buttonWidth ).height( buttonHeight * 0.5f );
			invalidateHierarchy();
			
			maintenanceBar.setStyle( skin.get( "orange", ProgressBarStyle.class ) );
			scrollTable.getCell( maintenanceBar )
				.width( buttonWidth ).height( buttonHeight * 0.5f );
			invalidateHierarchy();
			
			nextLabel.setStyle( skin.get( LabelStyle.class ) );
			nextLabel.setText( theJar.whatsNext() );
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
	public void screenAct( float delta ) {
		if ( theJar!=null ) {
			theJar.useAlert();
			
			if ( fastForwarding ) {
				fDelay += delta * fSpeed;
				while ( fDelay>= 1.0f ) {
					theJar.updateOneSec();
					fDelay -= 1.0f;
				}
				
				if ( theJar.getProgress()==1.0f ) {
					fastForwarding = false;
				}
			}
			
			int newFrame = theJar.getFrame();
			if ( lastFrame!=newFrame ) {
				lastFrame = newFrame;
				moldASprite = jarAtlas.createSprite( ST.JAR_MOLD_NAMES[ theJar.mold_typeA ], lastFrame );
				if ( theJar.contaminated ) {
					moldBSprite = jarAtlas.createSprite( ST.JAR_CONT_NAMES[ theJar.mold_typeB ], lastFrame );
		    	} else if ( theJar.mold_typeB!= -1 ) {
		    		moldBSprite = jarAtlas.createSprite( ST.JAR_MOLD_NAMES[theJar. mold_typeB ], lastFrame );
		    	} else {
		    		moldBSprite = null;
		    	}
			}
		}
	}
	
	@Override
	public void draw( Batch batch, float parentAlpha ) {
		super.draw( batch, parentAlpha );

		if ( theJar!=null ) {
			someShit = new Vector2( 0, 0 );
			icon.localToStageCoordinates( someShit );
	
			grainSprite.setBounds( someShit.x, someShit.y,
					icon.getWidth(), icon.getHeight() );
			grainSprite.setOrigin( icon.getWidth() * 0.5f, icon.getHeight() * 0.5f );
			grainSprite.draw( batch, parentAlpha );
			
			moldASprite.setBounds( someShit.x, someShit.y,
					icon.getWidth(), icon.getHeight() );
			moldASprite.setOrigin( icon.getWidth() * 0.5f, icon.getHeight() * 0.5f );
			moldASprite.draw( batch, parentAlpha );
			
			if ( moldBSprite!=null ) {
				moldBSprite.setBounds( someShit.x, someShit.y,
						icon.getWidth(), icon.getHeight() );
				moldBSprite.setOrigin( icon.getWidth() * 0.5f, icon.getHeight() * 0.5f );
				moldBSprite.draw( batch, parentAlpha );
			}
			
			jarSprite.setBounds( someShit.x, someShit.y,
					icon.getWidth(), icon.getHeight() );
			jarSprite.setOrigin( icon.getWidth() * 0.5f, icon.getHeight() * 0.5f );
			jarSprite.draw( batch, parentAlpha );
		}
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
	
	public static class JarBuilder implements ScreenPartBuilder<JarPart> {
		@Override
		public JarPart build() {
			JarPart part = new JarPart( null );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2f );
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
