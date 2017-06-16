package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
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
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Jar;
import com.bytesbrothers.shroomtycoon.structures.Project;

public class MakeCasingPart extends ScreenPart {

	private Jar theJar = null;
	private int substrate = ST.COCO_COIR;
	private ScrollPane pane;
	private Table scrollTable;
	private Table nameTable;
	private TextField nameField;
	private Table jarTable;
	private Table substrateTable;
	private Label hintLabel;
	
	public MakeCasingPart( Jar jar  ) {
		super( "Make Casing" );
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
		
		ScreenLabel label = new ScreenLabel( new FitLabel( "Make Casing", skin ) );
		label.scaleY = 0.9f;
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		nameTable = new Table();
		
		label = new ScreenLabel( new FitLabel( "New Name:", skin ) );
		label.scaleX = 0.35f;
		label.label.setAlignment( Align.right );
		label.table = nameTable;
		label.table.add( label.label );
		labels.add( label );
		
		nameField = new TextField( nameField==null? (theJar==null?"New Casing":theJar.name ): nameField.getText() , skin );
		nameTable.add( nameField );
  		Assets.game.stage.setKeyboardFocus( nameField );
  		nameField.selectAll();
  		nameField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
  		scrollTable.add( nameTable );
  		scrollTable.row();

  		jarTable = new Table();
  		
  		label = new ScreenLabel( new FitLabel( "Jar:", skin ) );
		label.scaleX = 0.2f;
		label.label.setAlignment( Align.right );
		label.table = jarTable;
		label.table.add( label.label );
		labels.add( label );
  		
		button = new ScreenButton( "Jar:", "JarIcon", "laboratory", skin ) {
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
		button.scaleX = 0.8f;
		button.table = jarTable;
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
						myButton.setColor( skin.getColor( "laboratory" ) );
		 				myButton.left();
		 				myButton.setDisabled( thisJar.getProgress()!=1.0f || thisJar.contaminated );
		 				myButton.addListener( new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				theJar = thisJar;
		 		 				if ( nameField!=null )
		 		 					nameField.setText( theJar==null?"New Casing":theJar.name );
		 		 				if ( hintLabel!=null ) {
		 		 					hintLabel.setText( getHint() );
		 		 					scrollTable.getCell( hintLabel ).height( hintLabel.getPrefHeight() );
		 		 				}
		 		 			}
		 		 		});
		 				buttons.add( myButton );
	 				}
	  				
	 				Dialogs.scrollTableDialog( "Please choose a fully colonized jar:", buttons );
  				}
 			}
 		});
		scrollTable.add( jarTable );
		scrollTable.row();
		
		hintLabel = new Label( getHint(), skin.get( LabelStyle.class ) );
		hintLabel.setAlignment( Align.center );
		hintLabel.setWrap( true );
		scrollTable.add( hintLabel );
		scrollTable.row();
		
		substrateTable = new Table();
  		
  		label = new ScreenLabel( new FitLabel( "Substrate:", skin ) );
		label.scaleX = 0.5f;
		label.label.setAlignment( Align.right );
		label.table = substrateTable;
		label.table.add( label.label );
		labels.add( label );
  		
		button = new ScreenButton( "Substrate:", "SubJarIcon", "resource", skin ) {
			int last = -1;
			@Override
			public void act( float delta ) {
				if ( last!=substrate ) {
					last = substrate;
					fitButton.setText( ST.substrates[substrate] );
					fitButton.resizeText();
				}
			}
		};
		button.styleName = "SelectBox";
		button.scaleX = 0.5f;
		button.table = substrateTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				ArrayList<Button> buttons = new ArrayList<Button>();
  				for ( int i = 0; i <4; i++ ) {
 					final int j = i;
 					FitButton myButton = new FitButton( ST.substrates[i] + " x" + Assets.player.sub_jars[i], skin );
 					myButton.setColor( skin.getColor( "resource" ) );
	 				myButton.left();
	 				myButton.setDisabled( Assets.player.sub_jars[i]==0 ) ;
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				substrate = j;
	 		 			}
	 		 		});
	 				buttons.add( myButton );
 				}
  				
  				Dialogs.scrollTableDialog( "Please select a substrate to use:", buttons );
 			}
 		});
		scrollTable.add( substrateTable );
		scrollTable.row();
		
		final int space = Assets.player.incubatorSize() - (Assets.player.numJars( ST.INCUBATOR ) + Assets.player.numCasings( ST.INCUBATOR )) + 
				((theJar!=null && theJar.location==ST.INCUBATOR)? 1: 0);
		label = new ScreenLabel( new FitLabel( "Inc. Free Space: " + space, skin ) );
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();

		label = new ScreenLabel( new FitLabel( "Time to complete: " + ST.PROJECT_TIME + " seconds", skin ) );
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		button = new ScreenButton( "Make Casing", "Jar2CasingIcon", "project", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Gdx.input.setOnscreenKeyboardVisible( false );
 				
 				if ( space<1 ) {
 					Dialogs.acceptDialog( "There is not enough space in the incubator" );
 				} else if ( theJar!=null ) {
					Project newProject = new Project( "Make Casing", "Jar2CasingIcon" );
					newProject.message = "" + theJar.name + " was made into a casing! " + nameField.getText() +
							" was put into the incubator.";
					final Casing newCasing = new Casing( nameField.getText(),
							theJar.strain, Assets.game );
					newCasing.substrate = substrate;
					newProject.nextPart = Assets.getScreenHandler().getScreenPart( CasingInfoPart.class );
					newProject.nextPartObject = newCasing;
					newProject.addEvent( new Project.ProjectEvent() {
						public void run() {
							theJar.useAlert();
							Assets.player.jars.remove( theJar );
							Assets.player.casings.add( newCasing );
							Assets.player.sub_jars[ substrate ] -= 1;
							Assets.player.totalCasingsMade += 1;
							Assets.player.increaseXP( 20 );
							Assets.getResolver().trackerSendEvent( "Strain", theJar.strain.name, "Make Casing", (long)0 );
							Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQAw" );
						}
					});
					Assets.game.startProjectPart( newProject );
 				} else {
 					Dialogs.acceptDialog( "Please choose a fully colonized jar" );
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

		nameField.setStyle( skin.get( TextFieldStyle.class ) );
		nameTable.getCell( nameField )
			.width( buttonWidth * 0.65f )
			.height( buttonHeight );
		Stage stage = Assets.game.stage;
		if ( stage!=null && nameField!=null )
			stage.setKeyboardFocus( nameField );
		
  		nameField.selectAll();
  		
  		hintLabel.setStyle( skin.get( LabelStyle.class ) );
  		hintLabel.setText( getHint() );
  		hintLabel.setWidth( buttonWidth );
		scrollTable.getCell( hintLabel )
			.width( buttonWidth ).height( hintLabel.getPrefHeight() );
		
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
		if ( nameField!=null )
			nameField.setText( theJar==null?"New Casing":theJar.name );
	}
	
	public String getHint() {
		if ( theJar!=null ) {
			return "hint: " + theJar.strain.name + " prefers " + ST.substrates[theJar.strain.substrate];
		}
		return "";
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
	
	public static class MakeCasingBuilder implements ScreenPartBuilder<MakeCasingPart> {
		@Override
		public MakeCasingPart build() {
			MakeCasingPart part = new MakeCasingPart( null );
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
