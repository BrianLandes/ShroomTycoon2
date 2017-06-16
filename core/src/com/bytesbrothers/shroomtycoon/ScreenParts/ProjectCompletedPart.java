package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.Align;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Project;

public class ProjectCompletedPart extends ScreenPart {

//	public Project theProject;
//	public boolean fastForwarding = false;
//	private boolean complete = false;
//	private Vector2 someShit;

//	private Image icon;
//	private Sprite iconSprite;
//	private ProgressBar progressBar;
	
	private Label messageLabel;
	
	public NameAndMessage nAndM = new NameAndMessage();
	
	public class NameAndMessage {
		public String message = "";
		public String name = "Project";
		public int mainColumn = -1;
		public ScreenPart nextPart = null;
		public Object nextPartObject = null;
	}
	
	public ProjectCompletedPart( ) {
		super( "Project Completed" );
	}
	
	public ProjectCompletedPart( NameAndMessage nAndM ) {
		super( "Project Completed" );
		this.nAndM = nAndM;
	}

	@Override
	public void populate() {
		clear();

		ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
		add( button.fitButton );
		row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				back();
 			}
 		});
		
		ScreenLabel label = new ScreenLabel( new FitLabel( nAndM.name, skin ) );
		label.label.setAlignment( Align.center );
		label.scaleY = 0.9f;
		add( label.label );
		labels.add( label );
		row();
		
		messageLabel = new Label( nAndM.message, skin );
		messageLabel.setAlignment( Align.center );
		messageLabel.setWrap( true );
		messageLabel.setWidth( Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD ) );
		add( messageLabel )
			.height( messageLabel.getPrefHeight() * 1.4f )
			.padTop( 10 );
		
	}
	
	@Override
	public void passObject( Object object ) {
		if ( object.getClass().equals( Project.class ) ) {
			Project project = (Project)object;
			nAndM.name = project.name;
			nAndM.message = project.message;
			nAndM.mainColumn = project.mainColumn;
			nAndM.nextPart = project.nextPart;
			nAndM.nextPartObject = project.nextPartObject;
			
		} else if ( object.getClass().equals( NameAndMessage.class ) ){
			NameAndMessage newNameAndMessage = (NameAndMessage)object;
			nAndM.name = newNameAndMessage.name;
			nAndM.message = newNameAndMessage.message;
			nAndM.mainColumn = -1;
			nAndM.nextPart = null;
			nAndM.nextPartObject = null;
		}
		
		populated = false;
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void resize() {
		super.resize();
		
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
		
		messageLabel.setStyle( Assets.getSkin().get( LabelStyle.class ) );
		getCell( messageLabel ).width( buttonWidth )
			.height( messageLabel.getPrefHeight() * 1.4f );
	}
	
	@Override
	public void back() {
//		back( MainPart.NONE );
//	}
//	
//	public void back( int quickOpen ) {
		if ( nAndM.mainColumn!= MainPart.NONE ) {
			MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );

			screen.setDx( 0 );
			setDx( 1f );
			
			ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
			actionBarPart.setDy( 0 );
		
			screen.quickOpen( nAndM.mainColumn );
			Assets.getScreenHandler().setActivePart( screen );
		} else if ( nAndM.nextPart!=null ) {
			nAndM.nextPart.populated = false;
			nAndM.nextPart.passObject( nAndM.nextPartObject );
			nAndM.nextPart.setDx( 0 );
			setDx( 1f );
			
			ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
			actionBarPart.setDy( 0 );
		
			Assets.getScreenHandler().setActivePart( nAndM.nextPart );

		} else {

			MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );
	
			screen.setDx( 0 );
			setDx( 1f );
			
			ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
			actionBarPart.setDy( 0 );
		
//			screen.quickOpen( quickOpen );
			Assets.getScreenHandler().setActivePart( screen );
		}
		
	}
	
	public static class ProjectBuilder implements ScreenPartBuilder<ProjectCompletedPart> {
		@Override
		public ProjectCompletedPart build() {
			ProjectCompletedPart part = new ProjectCompletedPart( );
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
