package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.elements.SpriteTween;
import com.bytesbrothers.shroomtycoon.structures.Project;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

public class ProjectPart extends ScreenPart {

	public Project theProject;
	public boolean fastForwarding = false;
	private boolean complete = false;
	private Vector2 someShit;

	private Image icon;
	private Sprite iconSprite;
	private ProgressBar progressBar;
	
	public ProjectPart( Project project ) {
		super( "Project" );
		setProject( project );
	}

	@Override
	public void populate() {
		clear();

		if ( theProject!=null ) {
			ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
			add( button.fitButton );
			row();
			buttons.add( button );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				back();
	 			}
	 		});
			
			ScreenLabel label = new ScreenLabel( new FitLabel( theProject.name, skin ) );
			label.label.setAlignment( Align.center );
			label.scaleY = 0.9f;
			add( label.label );
			labels.add( label );
			row();
			
			iconSprite = skin.getSprite( theProject.icon );
			icon = new Image();
			add( icon );
			row();
			
			progressBar = new ProgressBar( theProject, skin );
			add( progressBar );
			row();
			
			button = new ScreenButton( "Fast Forward", "FastForwardIcon", "useCoin", skin ) {
				@Override
				public void act ( float delta ) {
					if ( fastForwarding )
						fitButton.setDisabled( true );
				}
			};
			add( button.fitButton );
			buttons.add( button );
			row();
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				final int coin_cost = 1 + theProject.completionTime/ ST.PROJECT_TIME;
		 			if ( Assets.player.coins >= coin_cost ) {
		 				Dialogs.choiceDialog( "Would you like to fast forward this project for " + coin_cost + " Blue Coins?",
							new ChangeListener() {
			 		 			public void changed (ChangeEvent event, Actor actor) {
			 		 				Assets.player.changeCoins( -coin_cost );
			 		 				fastForwarding = true;
		 		 					Tween.to( theProject, Project.ProjectTween.PROGRESS, 1f )
		 		 			        .target( theProject.completionTime )
		 		 			        .ease(TweenEquations.easeInQuad)
		 		 			        .start( Assets.getTweenManager() ); //** start 
			 		 				Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Project FF", (long)0 );
			 		 			}
		 					}
						);
		 			} else {
		 				Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + coin_cost + "." );
	 				}
	 			}
	 		});
		}
	}

	@Override
	public void unpopulate() {
		theProject = null;
	}
	
	public void setProject( Project project ) {
		theProject = project;
		populated = false;
		complete = false;
		fastForwarding = false;
		if ( theProject!=null ) {
			iconSprite = Assets.getSkin().getSprite( theProject.icon );
			iconSprite.setRotation( 10 );
			Tween.to( iconSprite, SpriteTween.ALPHA, 2 )
	        .target( 0.1f )
	        .ease(TweenEquations.easeInOutQuad)
	        .repeatYoyo( -1, 0.5f)      // yoyo repetitions too (one play forward, the other backward, etc)
	        .start( Assets.getTweenManager() );    // and finally start it!
			Tween.to( iconSprite, SpriteTween.ROTATION, 4 )
	        .target( -10f )
	        .ease(TweenEquations.easeInOutQuad)
	        .repeatYoyo( -1, 0.75f)      // yoyo repetitions too (one play forward, the other backward, etc)
	        .start( Assets.getTweenManager() );    // and finally start it!
			System.out.println( "Setting Project" );
		} else
			System.out.println( "Setting Project to null" );
	}

	@Override
	protected void resize() {

		super.resize( );

		if ( theProject!=null ) {
			System.out.println( "Project resizing" );
			float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
			
			progressBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
			getCell( progressBar )
				.width( buttonWidth ).height( buttonHeight * 0.5f );
			invalidateHierarchy();
			
			float iconHeight = Assets.height - buttonHeight*3.5f;
			getCell( icon )
				.width( iconHeight )
				.height( iconHeight )
//				.padBottom( buttonHeight )
				;
			
			iconSprite = Assets.getAtlas().createSprite( theProject.icon );
			iconSprite.setRotation( 10 );
			Tween.to( iconSprite, SpriteTween.ALPHA, 2 )
	        .target( 0.1f )
	        .ease(TweenEquations.easeInOutQuad)
	        .repeatYoyo( -1, 0.5f)      // yoyo repetitions too (one play forward, the other backward, etc)
	        .start( Assets.getTweenManager() );    // and finally start it!
			Tween.to( iconSprite, SpriteTween.ROTATION, 4 )
	        .target( -10f )
	        .ease(TweenEquations.easeInOutQuad)
	        .repeatYoyo( -1, 0.75f)      // yoyo repetitions too (one play forward, the other backward, etc)
	        .start( Assets.getTweenManager() );    // and finally start it!
		} else
			System.out.println( "Project tried resizing but project was null" );
	}
	
	@Override
	public void screenAct( float delta ) {
		if ( theProject!=null ) {
			if ( !fastForwarding )
				theProject.update( delta );
			
			if ( !complete && theProject.getProgress()>=1.0f ) {
				completeProject();
				
//				Dialogs.sweetDialog( theProject.message, new ChangeListener() {
//		 			public void changed (ChangeEvent event, Actor actor) {
//	
//		 				if ( theProject.mainColumn!=MainPart.NONE ) {
//		 					back( theProject.mainColumn );
//		 				} else if ( theProject.nextPart!=null ) {
//		 					theProject.nextPart.populated = false;
//		 					theProject.nextPart.passObject( theProject.nextPartObject );
//		 					theProject.nextPart.setDx( 0 );
//		 					setDx( 1f );
//		 					
//		 					ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
//		 					actionBarPart.setDy( 0 );
//		 				
//		 					Assets.getScreenHandler().setActivePart( theProject.nextPart );
//		 					
//		 					setProject( null );
//						} else
//							back();
//		 			}
//		 		} );
				
			}
		}
	}
	
	@Override
	public void draw( Batch batch, float parentAlpha ) {
		super.draw( batch, parentAlpha );

		if ( theProject!=null ) {
			someShit = new Vector2( 0, 0 );
			icon.localToStageCoordinates( someShit );
	
			iconSprite.setBounds( someShit.x, someShit.y,
					icon.getWidth(), icon.getHeight() );
			iconSprite.setOrigin( icon.getWidth() * 0.5f, icon.getHeight() * 0.5f );
			iconSprite.draw( batch, parentAlpha );
		}
	}
	
	@Override
	public void pause() {
		super.pause();
		if ( theProject!=null && !complete && fastForwarding ) {
			theProject.complete();
			complete = true;
			fastForwarding = false;
		}
	}

	
//	@Override
//	public void resume() {
//		if ( complete && theProject!=null ) {
//			complete = true;
//			System.out.println( "Project completed" );
//			Dialogs.sweetDialog( theProject.message, new ChangeListener() {
//	 			public void changed (ChangeEvent event, Actor actor) {
//
//	 				if ( theProject.mainColumn!=MainPart.NONE ) {
//	 					back( theProject.mainColumn );
//	 				} else if ( theProject.nextPart!=null ) {
//	 					theProject.nextPart.populated = false;
//	 					theProject.nextPart.passObject( theProject.nextPartObject );
//	 					theProject.nextPart.setDx( 0 );
//	 					setDx( 1f );
//	 					
//	 					ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
//	 					actionBarPart.setDy( 0 );
//	 				
//	 					Assets.getScreenHandler().setActivePart( theProject.nextPart );
//	 					
//	 					setProject( null );
//					} else
//						back();
//	 			}
//	 		} );
//		}
//	}
	
	public void completeProject() {
		complete = true;
		System.out.println( "Project completed" );
		theProject.complete();
		ProjectCompletedPart completePart = Assets.getScreenHandler().getScreenPart( ProjectCompletedPart.class );
		completePart.passObject( theProject );
		completePart.setDx( 0 );
		Assets.getScreenHandler().setActivePart( completePart );
		setProject( null );
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void back() {
		back( MainPart.NONE );
	}
	
	public void back( int quickOpen ) {
		
		if ( theProject!=null && !complete && fastForwarding ) {
			theProject.complete();
			complete = true;
			fastForwarding = false;
		}
		
		setProject( null );
		
//		ShroomClass game = Assets.game;
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );

		screen.setDx( 0 );
		setDx( 1f );
		
		ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
		actionBarPart.setDy( 0 );
	
		screen.quickOpen( quickOpen );
		Assets.getScreenHandler().setActivePart( screen );
		
	}
	
	public static class ProjectBuilder implements ScreenPartBuilder<ProjectPart> {
		@Override
		public ProjectPart build() {
			ProjectPart part = new ProjectPart( null );
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
