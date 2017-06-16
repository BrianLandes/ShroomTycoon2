package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.Align;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.Profiles;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.ShroomPlayer;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.pools.DialogPool;

public class ManageProfilesPart extends ScreenPart {

	private ShroomPlayer player;

	private ScrollPane pane;
	private Table scrollTable;
	
	private boolean signedIn = false;
	protected Profiles.PlayerInfo deleteInfo;
	protected Dialog anotherDialog;
	protected Slider slider;
	
	ArrayList<Button> deleteButtons = new ArrayList<Button>();
	
	public ManageProfilesPart( ) {
		super( "Manage Profiles" );
	}

	@Override
	public void populate() {
		clear();
		deleteButtons.clear();
		
		player = Assets.player;
		skin = Assets.getSkin();
			
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
		
		if ( Assets.getResolver().isAndroid() ) {
			signedIn = Assets.getResolver().isGoogleSignedIn();
			
			button = new ScreenButton( signedIn? "Sign out of Google Play": "Sign in to Google Play", "games_controller", "white", skin ) {
				@Override
				public void act( float delta ) {
					boolean newSignedIn = Assets.getResolver().isGoogleSignedIn();
					if ( newSignedIn!=signedIn ) {
						signedIn = newSignedIn;
						fitButton.setText( signedIn? "Sign out of Google Play": "Sign in to Google Play" );
					}
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton ).colspan( 2 );
			button.table.row();
			buttons.add( button );
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				if ( signedIn ) {
	 					Assets.getResolver().googleSignOut();
	 				} else {
	 					Assets.getResolver().googleSignIn();
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Load From Server", "games_savedgames_green", "white", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( !Assets.getResolver().isGoogleSignedIn() );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton ).colspan( 2 );
			button.table.row();
			buttons.add( button );
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				ShroomClass game = Assets.game;
//	 				for ( ScreenPart screenPart: game.screenHandler.screenParts )
//	 					screenPart.dispose();
	 				game.needsUnpopulate = true;
//	 				game.unpopulateAll();
//	 				game.stage.clear();
//	 				game.screenHandler.screenParts.clear();
//	 				game.populated = false;
	 				Assets.getResolver().showSavedGamesUIforLoading();
	 			}
	 		});
		}
		
		
		ArrayList<Profiles.PlayerInfo> infos = Profiles.getPlayerInfo();
		
		if ( infos.isEmpty() ) {
		
			ScreenLabel label = new ScreenLabel( new FitLabel( "No saved profiles", skin ) );
			label.label.setAlignment( Align.center );
			label.table = scrollTable;
			label.table.add( label.label ).colspan( 2 );
			labels.add( label );
			label.table.row();
		} else {
			ScreenLabel label = new ScreenLabel( new FitLabel( "Load Profile", skin ) );
			label.label.setAlignment( Align.center );
			label.table = scrollTable;
			label.table.add( label.label ).colspan( 2 ).padTop( 15 );
			labels.add( label );
			label.table.row();
			
			for ( final Profiles.PlayerInfo info: infos ) {
				button = new ScreenButton( info.name, "SettingsUp", "white", skin ) {
					@Override
					public void resize( int width, int height) {
						width -= height;
						super.resize( width, height );
					}
					@Override
					public void act( float delta ) {
						if ( player!=null ) {
							fitButton.setDisabled( info.name.equals( player.name ) );
						}
					}
				};
				button.table = scrollTable;
				button.table.add( button.fitButton );
//					button.table.row();
				buttons.add( button );
		
				button.fitButton.addListener( new ChangeListener() {
		 			public void changed (ChangeEvent event, Actor actor) {
		 				Profiles.save();
						
						switch ( Profiles.load( info ) ) {
						case Profiles.CLOUD_WAIT:
						case Profiles.SNAPSHOT_WAIT:
							break;
						case Profiles.LOADED:
							Profiles.timeTravel();
							back();
							break;
						case Profiles.FAILED:
							Dialogs.acceptDialog( "There was a problem loading " + info.name );
							break;
						}
		 			}
		 		});
				
				Button deleteButton = new Button( skin.get( "Delete", ButtonStyle.class ) );
				scrollTable.add( deleteButton );
				deleteButtons.add( deleteButton );
				scrollTable.row();
				deleteButton.addListener( new ChangeListener() {
					public void changed (ChangeEvent event, Actor actor) {
//						ShroomClass game = Assets.game;
						deleteInfo = info;
//		 				anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		 				if ( Dialogs.dialogPool == null )
		 					Dialogs.dialogPool = new DialogPool();
		 				anotherDialog = Dialogs.dialogPool.borrowObject();
		 				anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		 				anotherDialog.clear();
	 					Label myLabel = new Label( "Would you like to delete this profile? This cannot be undone.", skin.get( "SmallBasic", LabelStyle.class) );
	 					myLabel.setColor( skin.getColor( "black" ) );
	 					myLabel.setWrap( true );
	 					myLabel.setAlignment( Align.center );
	 					myLabel.setWidth( Assets.width * 0.8f );
	 					anotherDialog.getContentTable().add(myLabel).width( Assets.width * 0.8f ).height( myLabel.getPrefHeight() )
	 					.padBottom( Assets.height*0.05f ).padTop( Assets.height*0.1f ).padLeft( Assets.width*0.05f ).padRight( Assets.width*0.05f );
	 					
	 					anotherDialog.getContentTable().row();
	 					
	 					myLabel = new Label( "slide to delete", skin.get( "SmallBasic", LabelStyle.class) );
	 					myLabel.setColor( skin.getColor( "black" ) );
	 					myLabel.setAlignment( Align.right );
	 					myLabel.setWidth( Assets.width * 0.8f );
	 					anotherDialog.getContentTable().add(myLabel).width( Assets.width - Assets.width * 0.125f ).height( myLabel.getPrefHeight() );
	 					
	 					slider = new Slider( 0, 100, 1, false, skin );
	 					slider.setAnimateDuration( 0.35f );
	 					anotherDialog.getButtonTable().add( slider ).width( Assets.width - Assets.width * 0.125f ).height( Assets.height * 0.15f ).padBottom( Assets.height*0.025f ).padLeft( Assets.width*0.05f ).padRight( Assets.width*0.05f );
	 					anotherDialog.getButtonTable().row();
	 					final FitButton cancelbutton = new FitButton( "Cancel", skin );
	 					anotherDialog.getButtonTable().add( cancelbutton ).width( Assets.width - Assets.width*0.125f ).height( Assets.height * 0.1f ).padBottom( Assets.height*0.025f );

	 					cancelbutton.addListener( new ChangeListener() {
	 			 			public void changed (ChangeEvent event, Actor actor) {
	 			 				anotherDialog.hide();
	 			 			}
	 			 		});
	 					
	 					Assets.game.showDialog( anotherDialog );
		 			}
		 		});
			}
		}
		
		button = new ScreenButton( "New Game", "SideJobIcon", "white", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 ).padTop( 15 );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( Assets.getResolver().isAndroid() ) {
 					String unique = new BigInteger(281, new Random()).toString(13);
 					Assets.getResolver().setCurrentSaveName( "snapshotTemp-" + unique );
 				}
// 				ShroomPlayer player = new ShroomPlayer( Assets.game );
//				Assets.setPlayer( player );
// 				player.newPlayer();
 				
 				Dialogs.renameDialog( "Create a new Profile:", null );
// 				game.addTable( new NewPlayerColumn() );
 			}
 		});
		
		pane = new ScrollPane( scrollTable, skin);
		add( pane );

	}

	@Override
	public void act( float delta ) {
		superAct( delta );
		if ( !populated ) {
			player = Assets.player;
//			if ( player!=null )
				populate();
			populated = needsResize = true;
		}
		
		if ( needsResize && populated ) {
			System.out.println( "TryResize" );
			player = Assets.player;
			resize( );
			needsResize = false;
		}
		
		actualCoords = new Vector2( 0, 0 );
		localToStageCoordinates( actualCoords );
		
		if ( !buttons.isEmpty() ) {
			for ( ScreenButton button: buttons ) {
				button.act( delta );
				if ( button.fitButton.isDisabled() )
					button.fitButton.setColor( skin.getColor( "disabled" ) );
				else
					button.fitButton.setColor( skin.getColor( button.colorName ) );
			}
		}
		
		if ( !labels.isEmpty() )
			for ( ScreenLabel label: labels )
				label.act( delta );
		
		if ( slider!=null ) {
			if ( !slider.isDragging() )
				slider.setValue( 0.0f );
			
			if ( slider.getVisualValue()==100.0f && !slider.isDisabled() ) {
				slider.setDisabled( true );
				anotherDialog.hide();
				
				Profiles.delete( deleteInfo );
			}
		}
	}
	
//	/**
//	 * Attempts to resize this ScreenPart based on the dimensions of the screen
//	 * @param width the width of the screen
//	 * @param height the height of the screen
//	 */
//	public void tryresize( int width, int height ) {
////		player = Assets.getPlayer();
//		if ( populated ) {
//			resize( width, height );
//			needsResize = false;
//		}
//	}
	
	@Override
	protected void resize( ) {

		super.resize( );

		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
//			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, width * desiredWidth * BUTTON_WIDTH_MOD );

		for ( Button myButton: deleteButtons ) {
			myButton.setStyle( skin.get( "Delete", ButtonStyle.class ) );
			scrollTable.getCell( myButton )
				.width( buttonHeight )
				.height( buttonHeight);
		}
		
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
		if ( Assets.player==null ) {
			Dialogs.acceptDialog( "Please start or load a profile." );
		} else {
//			ShroomClass game = Assets.game;
			MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );
	
			screen.setDx( 0 );
			setDx( 1f );
			
			ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
//			ActionBarPart actionBarPart = (ActionBarPart) game.getScreenPart( "Action Bar" );
			actionBarPart.setDy( 0 );
	
			Assets.getScreenHandler().setActivePart( screen );
		}
	}
	
	public static class ManageProfilesBuilder implements ScreenPartBuilder<ManageProfilesPart> {
		@Override
		public ManageProfilesPart build() {
			ManageProfilesPart part = new ManageProfilesPart( );
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
