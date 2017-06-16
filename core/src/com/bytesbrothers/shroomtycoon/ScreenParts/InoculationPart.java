package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Jar;
import com.bytesbrothers.shroomtycoon.structures.Project;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class InoculationPart extends ScreenPart {

	private Strain theSyringe = null;
	private ScrollPane pane;
	private Table scrollTable;
	private Table syringeTable;
	private Table numTable;
	private int numJars = 1;
	
	public InoculationPart( Strain syringe  ) {
		super( "Inoculation" );
		setSyringe( syringe );
	}

	@Override
	public void populate() {
		clear();
		
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
		
		ScreenLabel label = new ScreenLabel( new FitLabel( "Inoculation", skin ) );
		label.scaleY = 0.9f;
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		syringeTable = new Table();
		
		label = new ScreenLabel( new FitLabel( "Syringe:", skin ) );
		label.label.setAlignment( Align.right );
		label.scaleX = 0.35f;
		label.table = syringeTable;
		label.table.add( label.label );
		labels.add( label );
		
		button = new ScreenButton( "Back", "SyringeIcon", "resource", skin ) {
			String last = "";
			@Override
			public void act( float delta ) {
				if ( theSyringe==null && !last.equals( "(none)" ) ) {
					last = "(none)";
					fitButton.setText( "(none)");
					fitButton.resizeText();
				}
				if ( theSyringe!=null && !last.equals( theSyringe.name ) ) {
					last = theSyringe.name;
					fitButton.setText( theSyringe.name );
					fitButton.resizeText();
				}
			}
		};
		button.styleName = "SelectBox";
		button.scaleX = 0.65f;
		button.table = syringeTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( Assets.player.getNumSyringes()==0 ) {
  					Dialogs.acceptDialog( "You have no syringes" );
  				} else {
	 				ArrayList<Button> buttons = new ArrayList<Button>();
	 				ArrayList<Strain> strains = StrainMaster.getStrains();
	 				Sprite icon = skin.getSprite( "SyringeIcon" );
					int[] list = Assets.player.getItemizedSyringes();
					int size = strains.size();
					for ( int index = 0; index<size; index ++ ) {
						final Strain currentStrain = strains.get( index );
						if ( list[index]>0 ) {
							boolean show = false;
							Iterator<String> canBuyIt = Assets.player.can_buy_strains.iterator();
							while( canBuyIt.hasNext() && !show ) {
								String string= canBuyIt.next();
								if ( string.equals( currentStrain.name ) )
									show = true;
							}
							if ( show ) {
								FitButton myButton = new FitButton( currentStrain.name + " x" + list[index], icon, skin );
								myButton.setColor( skin.getColor( "resource" ) );
				 				myButton.left();
				 				myButton.addListener( new ChangeListener() {
				 		 			public void changed (ChangeEvent event, Actor actor) {
				 		 				theSyringe = currentStrain;
				 		 			}
				 		 		});
				 				buttons.add( myButton );
							}
						}
					}
					
					int customSize = Assets.player.strains.size();
					
					for ( int i = 0; i< customSize; i ++ ) {
						final Strain currentStrain = Assets.player.strains.get( i );
						if ( list[ i + size ]>0 ) {
							FitButton myButton = new FitButton( currentStrain.name + " x" + list[i + size], icon, skin );
							myButton.setColor( skin.getColor( "resource" ) );
			 				myButton.left();
			 				myButton.addListener( new ChangeListener() {
			 		 			public void changed (ChangeEvent event, Actor actor) {
			 		 				theSyringe = currentStrain;
			 		 			}
			 		 		});
			 				buttons.add( myButton );
						}
					}
					
					int serverSize = Assets.player.serverStrains.size();
					
					for ( int i = 0; i< serverSize; i ++ ) {
						final Strain currentStrain = Assets.player.serverStrains.get( i );
						if ( list[ i + size + customSize ]>0 ) {
							FitButton myButton = new FitButton( currentStrain.name + " x" + list[i + size + customSize], icon, skin );
							myButton.setColor( skin.getColor( "resource" ) );
			 				myButton.left();
			 				myButton.addListener( new ChangeListener() {
			 		 			public void changed (ChangeEvent event, Actor actor) {
			 		 				theSyringe = currentStrain;
			 		 			}
			 		 		});
			 				buttons.add( myButton );
						}
					}

					Dialogs.scrollTableDialog( "Please choose a syringe:", buttons );
  				}
 			}
 		});
		
		scrollTable.add( syringeTable );
		scrollTable.row();
		
		final int space = Assets.player.incubatorSize() - (Assets.player.numJars( ST.INCUBATOR ) + Assets.player.numCasings( ST.INCUBATOR ));
		label = new ScreenLabel( new FitLabel( "Inc. Free Space: " + space, skin ) );
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		label = new ScreenLabel( new FitLabel( "Jars of grain: " + Assets.player.grain_jars, skin ) );
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		numTable = new Table();
		
		label = new ScreenLabel( new FitLabel( "Jars to make:", skin ) );
		label.label.setAlignment( Align.right );
		label.scaleX = 0.6f;
		label.table = numTable;
		label.table.add( label.label );
		labels.add( label );
		
		button = new ScreenButton( "" + numJars, "GrainJarIcon", "resource", skin ) {
			int last = 1;
			@Override
			public void act( float delta ) {
				if ( last!=numJars ) {
					last = numJars;
					fitButton.setText( "" + numJars );
					fitButton.resizeText();
				}
			}
		};
		button.styleName = "SelectBox";
		button.scaleX = 0.4f;
		button.table = numTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				ArrayList<Button> buttons = new ArrayList<Button>();
  				
  				int num = 3;
 				for ( int i = 1; i <=num; i++ ) {
 					final int j = i;
 					FitButton myButton = new FitButton( "" + j + " Jars", skin );
 					myButton.setColor( skin.getColor( "resource" ) );
	 				myButton.left();
	 				myButton.setDisabled( Assets.player.grain_jars<i );
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				numJars = j;
	 		 			}
	 		 		});
	 				buttons.add( myButton );
 				}
  				
 				Dialogs.scrollTableDialog( "Please select a number of jars to make:", buttons );
 			}
 		});
		
		scrollTable.add( numTable );
		scrollTable.row();

		label = new ScreenLabel( new FitLabel( "Time to complete: " + (ST.PROJECT_TIME* numJars) + " seconds", skin ) ) {
			int last = ST.PROJECT_TIME* numJars;
			@Override
			public void act( float delta ) {
				if ( last!=ST.PROJECT_TIME* numJars ) {
					last = ST.PROJECT_TIME* numJars;
					label.setText( "Time to complete: " + last + " seconds" );
				}
			}
		};
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		button = new ScreenButton( "Inoculate", "InnoculationIcon", "project", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( space<numJars ) {
 					Dialogs.acceptDialog( "There is not enough space in the incubator" );
 				} else if ( numJars> Assets.player.grain_jars ) {
 					Dialogs.acceptDialog( "You do not have enough jars of grain" );
 				} else if ( theSyringe==null )  {
 					Dialogs.acceptDialog( "Please choose a syringe" );
  				} else {
	 				Project newProject = new Project( "Inoculation", "InnoculationIcon" );
	 				if ( numJars==1 )
	 					newProject.message = "A jar was inoculated with " + theSyringe.name + 
	 					"! It was placed in the incubator.";
	 				else
	 					newProject.message = "" + numJars + " jars were inoculated with "
	 							+ theSyringe.name + "! They were placed in the incubator.";
	 				newProject.completionTime = ST.PROJECT_TIME* numJars;
	 				newProject.mainColumn = MainPart.INC;
	 				newProject.addEvent( new Project.ProjectEvent() {
	 					public void run() {
	 						Assets.player.removeSyringe( theSyringe.name );
//		 						game.Assets.player.syringes.remove( theSyringe );
	 						for ( int i = 0; i < numJars; i ++ ) {
	 							String newName = theSyringe.name;
	 							newName += " " + ( Assets.player.jar_enum<10?"0":"") + Assets.player.jar_enum;
	 							Jar newJar = new Jar( newName, theSyringe, Assets.game );
		 						
		 						Assets.player.jars.add( newJar );
		 						Assets.player.jar_enum ++;
		 						if ( Assets.player.jar_enum>99 )
		 							Assets.player.jar_enum = 1;
		 						Assets.player.totalJarsMade += 1;
		 						Assets.player.grain_jars -= 1;
	 						}
	 						Assets.player.increaseXP( 10 * numJars );
//		 						Profiles.autoSave();
	 						Assets.getResolver().trackerSendEvent( "Strain", theSyringe.name, "Inoculation", (long)0 );

	 						Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQAg" );
	 					}
	 				});
					Assets.game.startProjectPart( newProject );
//		 				game.addTable( new ProjectColumn( newProject ) );
 				}
 			}
 		});
		
		pane = new ScrollPane( scrollTable, skin);
		add( pane );
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
	public void passObject( Object object ) {
		if ( object!=null && object.getClass().equals( Strain.class ) )
			setSyringe( (Strain)object );
	}
	
	public void setSyringe( Strain syringe ) {
		theSyringe = syringe;
		populated = false;
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
	
	public static class InoculationBuilder implements ScreenPartBuilder<InoculationPart> {
		@Override
		public InoculationPart build() {
			InoculationPart part = new InoculationPart( null );
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
