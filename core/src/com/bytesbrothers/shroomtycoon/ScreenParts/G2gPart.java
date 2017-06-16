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

public class G2gPart extends ScreenPart {

	private Jar theJar = null;
	private ScrollPane pane;
	private Table scrollTable;
	private Table syringeTable;
	private Table numTable;
	private int numJars = 1;
	
	public G2gPart( Jar jar  ) {
		super( "G2g" );
		setJar( jar );
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
		
		ScreenLabel label = new ScreenLabel( new FitLabel( "G2G Transfer", skin ) );
		label.scaleY = 0.9f;
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		syringeTable = new Table();
		
		label = new ScreenLabel( new FitLabel( "Jar:", skin ) );
		label.label.setAlignment( Align.right );
		label.scaleX = 0.35f;
		label.table = syringeTable;
		label.table.add( label.label );
		labels.add( label );
		
		button = new ScreenButton( "Back", "JarIcon", "laboratory", skin ) {
			String last = "";
			@Override
			public void act( float delta ) {
				if ( theJar==null && !last.equals( "(none)" ) ) {
					last = "(none)";
					fitButton.setText( "(none)");
					fitButton.resizeText();
				}
				if ( theJar!=null && !last.equals( theJar.name ) ) {
					last = theJar.name;
					fitButton.setText( theJar.name );
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
 				if ( Assets.player.jars.size()==0 ) {
  					Dialogs.acceptDialog( "You have no jars" );
  				} else {
	 				ArrayList<Button> buttons = new ArrayList<Button>();
	 				Sprite icon = Assets.getAtlas().createSprite( "JarIcon" );
	 				Iterator<Jar> jarIt = Assets.player.jars.iterator();
	 				while ( jarIt.hasNext() ) {
	 					final Jar thisJar = jarIt.next();
						FitButton myButton = new FitButton( thisJar.name, icon, skin );
						myButton.setColor( skin.getColor( "resource" ) );
		 				myButton.left();
		 				myButton.setDisabled( thisJar.getProgress()!=1.0f || thisJar.contaminated );
		 				myButton.addListener( new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				theJar = thisJar;
		 		 			}
		 		 		});
		 				buttons.add( myButton );
	 				}
	  				
	 				Dialogs.scrollTableDialog( "Please choose a fully colonized jar:", buttons );
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
  				if ( Assets.player.canG2g5() )
  					num = 5;
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
		
		button = new ScreenButton( "Commence Transfer", "G2gIcon", "project", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( space<numJars ) {
 					Dialogs.acceptDialog( "There is not enough space in the incubator" );
 				} else if ( theJar==null ) {
 					Dialogs.acceptDialog( "Please choose a fully colonized jar" );
 				} else if ( numJars> Assets.player.grain_jars ) {
 					Dialogs.acceptDialog( "You do not have enough jars of grain" );
 				} else {
	 				Project newProject = new Project( "G2G Transfer", "G2gIcon" );
	 				if ( numJars==1 )
	 					newProject.message = "" + theJar.name + " was transfered into another jar!" +
	 							" There was no point for it, but it was put into the incubator.";
	 				else
	 					newProject.message = "" + theJar.name + " was transfered into " + numJars +
							" more jars! The new jars were put into the incubator.";
	 				newProject.addEvent( new Project.ProjectEvent() {
	 					public void run() {
	 						theJar.useAlert();
	 						Assets.player.jars.remove( theJar );
	 						for ( int i = 0; i < numJars; i ++ ) {
	 							String newName = theJar.strain.name;
	 							newName += " " + ( Assets.player.jar_enum<10?"0":"") + Assets.player.jar_enum;
	 							Jar newJar = new Jar( newName, theJar.strain, Assets.game );
		 						
		 						Assets.player.jars.add( newJar );
		 						Assets.player.jar_enum ++;
		 						if ( Assets.player.jar_enum>99 )
		 							Assets.player.jar_enum = 1;
		 						Assets.player.totalJarsMade += 1;
		 						Assets.player.grain_jars -= 1;
	 						}
	 						Assets.player.increaseXP( 6 * numJars );
	 						Assets.getResolver().trackerSendEvent( "Strain", theJar.strain.name, "G2G", (long)0 );
	 					}
	 				});
	 				newProject.mainColumn = MainPart.INC;
	 				Assets.game.startProjectPart( newProject );
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
		if ( object!=null && object.getClass().equals( Jar.class ) )
			setJar( (Jar)object );
	}
	
	public void setJar( Jar jar ) {
		theJar = jar;
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
	
	public static class G2gBuilder implements ScreenPartBuilder<G2gPart> {
		@Override
		public G2gPart build() {
			G2gPart part = new G2gPart( null );
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
