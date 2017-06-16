package com.bytesbrothers.shroomtycoon.elements;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.Profiles;
import com.bytesbrothers.shroomtycoon.Profiles.PlayerInfo;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ActionBarPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.StrainPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.ShroomPlayer;
import com.bytesbrothers.shroomtycoon.pools.DialogPool;
import com.bytesbrothers.shroomtycoon.structures.Strain;

public class Dialogs {
	
	public static DialogPool dialogPool = null;
	
	/** provides a dialog with a "no" button and a "yes" button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog choiceDialog( String message, ChangeListener yesEffect ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.7f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.7f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f );
		
		final FitButton nobutton = new FitButton( "Nah", skin );
		nobutton.setColor( skin.getColor( "red" ) );
		nobutton.setName( "NoButton" );
		anotherDialog.getButtonTable().add( nobutton ).width( w * 0.35f ).height( h * 0.15f ).padBottom( h*0.05f );

		nobutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});
		
		final FitButton yesbutton = new FitButton( "Yeah", skin );
		yesbutton.setColor( skin.getColor( "green" ) );
		nobutton.setName( "YesButton" );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.35f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.05f );

		yesbutton.addListener( yesEffect );
		
		// adds one to hide the dialog
		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});
		
		// adds it to screen as well
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a dialog with a white "ok" button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog acceptDialog( String message ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.8f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
		
		final FitButton yesbutton = new FitButton( "Accept It", skin );
		yesbutton.setColor( skin.getColor( "white" ) );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.8f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.05f ).padRight( w*0.05f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a dialog with a white "ok" button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog relocateDialog( boolean isJar, int from, int to ) {
		ShroomClass game = Assets.game;
//		float w = game.width;
//		float h = game.height;
//		final Skin skin = Assets.getSkin();
		final Dialog anotherDialog = new RelocateDialog( isJar, from, to );
		game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a dialog with all the alerts
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog alertsDialog( ) {
//		final ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Alerts", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Alerts", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		
		float height = (h>w)? w * 0.25f: h * 0.25f;

		Table scrollTable = new Table();
			
		Iterator<Alert> alertIt = Assets.alerts.iterator();
		ArrayList<Alert> removeAlerts = new ArrayList<Alert>();
		
		while ( alertIt.hasNext() ) {
			final Alert thisAlert = alertIt.next();
			if ( thisAlert.show ) {
				final Button myButton = new Button( skin.get( "AlertInfo", ButtonStyle.class ) );
				Image image = new Image( skin.getRegion( thisAlert.icon ) );

				image.setScaling( Scaling.fillY );
				myButton.add( image ).width( height*0.9f ).height( height*0.9f );
				
				Table infoTable = new Table();
				
				FitLabel myLabel = new FitLabel( thisAlert.title, skin );
//				.get( "BasicWhite", FitLabelStyle.class ) );
//				myLabel.setColor( skin.getColor( "black" ) );
				infoTable.add( myLabel).width( w - w * 0.2f - height*0.9f ).height( height*0.45f );
				
				infoTable.row();
				myLabel = new FitLabel( thisAlert.message, skin.get( "BasicWhite", LabelStyle.class ) );
				myLabel.setColor( skin.getColor( "black" ) );
				infoTable.add( myLabel).width( w - w * 0.2f - height*0.9f ).height( height*0.45f );
				
				myButton.add( infoTable );
				
				scrollTable.add( myButton ).width( w - w * 0.16f ).height( height );
				
				myButton.addListener( new ChangeListener() {
 		 			public void changed (ChangeEvent event, Actor actor) {
 		 				anotherDialog.hide();
// 		 				Assets.alerts.remove( thisAlert );
 		 				Assets.removeAlert( thisAlert );
 		 				thisAlert.show = false;
 		 				switch ( thisAlert.type ) {
// 		 				case ST.SCREEN:
// 		 					break;
// 		 				case ST.SIDE_JOB_DIALOG:
// 		 					Dialogs.sideJobDialog( thisAlert.dialog_text, thisAlert.side_job );
// 		 					break;
 		 				case ST.SPEECH_DIALOG:
 		 					Dialogs.speechDialog( thisAlert.dialog_title, thisAlert.dialog_text );
 		 					break;
 		 				case ST.TABLE: {
 		 					Assets.game.screenHandler.setActivePartDx( 1f );
// 		 					ActionBarPart actionBarPart = (ActionBarPart) game.screenHandler.getScreenPart( "Action Bar" );
 		 					ActionBarPart actionBarPart = Assets.game.screenHandler.getScreenPart( ActionBarPart.class );
 		 					actionBarPart.setDy( 0 );
 		 					
 		 					ScreenPart screen = Assets.game.screenHandler.getScreenPart( thisAlert.nextPart );
 		 					screen.passObject( thisAlert.partObject );
 		 					screen.setDx( 0 );
 		 					
 		 					
 		 					
 		 				
 		 					Assets.game.screenHandler.setActivePart( screen );
// 		 					game.addTable( thisAlert.table );
 		 					break; }
 		 				case ST.MAIN_COLUMN:
 		 					Assets.game.screenHandler.setActivePartDx( 1f );
 		 					
 		 					MainPart screen = Assets.game.screenHandler.getScreenPart( MainPart.class );
 		 					screen.setDx( 0 );
 		 					
 		 					ActionBarPart actionBarPart = Assets.game.screenHandler.getScreenPart( ActionBarPart.class );
 		 					actionBarPart.setDy( 0 );
 		 				
 		 					screen.quickOpen( thisAlert.mainColumn );
 		 					Assets.game.screenHandler.setActivePart( screen );
 		 					break;
 		 				case ST.COINS_DIALOG:
 		 					Dialogs.buyCoinsDialog();
 		 					break;
 		 				}
 		 				
 		 			}
 		 		});
				
				scrollTable.row();
			} else {
				removeAlerts.add( thisAlert );
			}
		}
		
		for ( Alert alert: removeAlerts )
			Assets.removeAlert( alert );
		
		ScrollPane pane = new ScrollPane( scrollTable, skin.get( "NoBack", ScrollPaneStyle.class ) );
		anotherDialog.getContentTable().add(pane).width( w - w * 0.15f ).height( h * 0.7f ).padLeft( w*0.04f).padRight( w*0.04f).padBottom( h*0.025f );
		
		final FitButton yesbutton = new FitButton( "Close Alerts", skin );
//		yesbutton.setColor( "black" );
		anotherDialog.getButtonTable().add( yesbutton ).width( w - w * 0.15f ).height( h * 0.1f ).padBottom( h*0.05f ).padLeft( w*0.04f ).padRight( w*0.04f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});
			
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a dialog with a white "ok" button and a yellow "Get more coins" button
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog acceptDialogGetMoreCoins( String message ) {
//		final ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.8f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
		
		final IconButton buyCoinsButton = new IconButton("Get More Coins", skin.getSprite( "CoinIcon" ), skin );
		buyCoinsButton.setColor( skin.getColor( "buyCoin" ) );
		buyCoinsButton.iconTable.left();
		buyCoinsButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 				Dialogs.buyCoinsDialog();
// 				game.backToMain( MainColumn.COINS );
 			}
		} );
		anotherDialog.getButtonTable().add( buyCoinsButton ).width( w * 0.4f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.05f );
		
		final FitButton yesbutton = new FitButton( "Accept It", skin );
		yesbutton.setColor( skin.getColor( "white" ) );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.4f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.05f ).padRight( w*0.05f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});
		
		
			
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
//	/** provides a dialog with a white "ok" button.
//	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
//	public static Dialog hintDialog( String message ) {
//		ShroomClass game = Assets.game;
//		float w = game.width;
//		float h = game.height;
//		final Skin skin = Assets.getSkin();
////		final Dialog anotherDialog = new Dialog( "", skin.get( "default", WindowStyle.class) );
//		if ( dialogPool == null )
//			dialogPool = new DialogPool();
//		final Dialog anotherDialog = dialogPool.borrowObject();
//		anotherDialog.setStyle( skin.get( "default", WindowStyle.class) );
//		anotherDialog.clear();
//		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
//		myLabel.setColor( skin.getColor( "black" ) );
//		myLabel.setWrap( true );
//		myLabel.setAlignment( Align.center );
//		myLabel.setWidth( w * 0.7f );
//		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() )
//		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
//		
//		final FitButton yesbutton = new FitButton( "Got it", skin );
//		yesbutton.setColor( skin.getColor( "white" ) );
//		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.6f ).height( h * 0.15f ).padBottom( h*0.05f );
//
//		yesbutton.addListener( new ChangeListener() {
// 			public void changed (ChangeEvent event, Actor actor) {
// 				anotherDialog.hide();
// 			}
// 		});
//		game.showDialog( anotherDialog );
//		
//		return anotherDialog;
//	}
	
	/** provides a dialog with a blue "sweet" button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog sweetDialog( String message, ChangeListener sweetEffect  ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.8f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f );
		
		final FitButton yesbutton = new FitButton( "Sweet", skin );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.8f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.025f ).padRight( w*0.025f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}

 		});
		yesbutton.addListener( sweetEffect );
		
//		System.out.println( "Dialogs.sweetDialog" );
		
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a dialog with a textField and an apply button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog renameDialog( String message, ShroomPlayer player ) {
//		final ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.8f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f );
		anotherDialog.getContentTable().row();
		
		final TextField nameField = new TextField( player==null? ST.getName(): player.name , skin );
		anotherDialog.getContentTable().add( nameField ).width( w * 0.8f ).height( h * 0.15f );
		Assets.game.stage.setKeyboardFocus( nameField );
  		nameField.selectAll();
  		nameField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		
		final FitButton yesbutton = new FitButton( "Apply", skin );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.8f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.025f ).padRight( w*0.025f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Gdx.input.setOnscreenKeyboardVisible( false );
 				anotherDialog.hide();
 				String newName = nameField.getText();
 				
 				switch ( Profiles.checkThisName( newName ) ) {
 				case Profiles.GOOD_NAME:
 					Assets.clearAlerts();
 					ShroomPlayer player = new ShroomPlayer( );
 					Assets.setPlayer( player );
 	 				player.newPlayer();
 	 				player.name = nameField.getText();
 	 				player.uniqueSaveName = Assets.getResolver().getCurrentSaveName();
 	 				player.newGame = true;
 	 				Profiles.save();
 	 				
 	 				// Set it to Auto-Load this profile
 	 				ArrayList<PlayerInfo> infos = Profiles.getPlayerInfo();
 	 				for ( final PlayerInfo info: infos ) {
 	 					if ( info.name.equals( player.name ) ) {
 	 						Profiles.setAutoLoadInfo( info );
 	 						break;
 	 					}
 	 				}

 	 				Assets.game.screenHandler.setActivePartDx( 1f );
 	 				ScreenPart screen = Assets.game.screenHandler.getScreenPart( MainPart.class );

 	 				screen.setDx( 0 );
 	 				screen.populated = false;
 	 				
 	 				ActionBarPart actionBarPart = Assets.game.screenHandler.getScreenPart( ActionBarPart.class );
 	 				actionBarPart.setDy( 0 );

 	 				Assets.game.screenHandler.setActivePart( screen );
 	 				Assets.game.needsUnpopulate = true;
// 	 				Assets.game.unpopulateAll();
 					break;
 				case Profiles.IN_USE:
 					Dialogs.acceptDialog( "You already have a profile with this name." );
 					break;
 				case Profiles.TOO_LONG:
 					Dialogs.acceptDialog( "This name is too long." );
 					break;
 				case Profiles.TOO_SHORT:
 					Dialogs.acceptDialog( "Your name has to be at least one character long. I mean, come on." );
 					break;
 				}
 			}
 		});
		anotherDialog.getButtonTable().row();
		
		final FitButton cancelbutton = new FitButton( "Cancel", skin );
		cancelbutton.setColor( skin.getColor( "red" ) );
		anotherDialog.getButtonTable().add( cancelbutton ).width( w * 0.8f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.025f ).padRight( w*0.025f );

		cancelbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});

		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a dialog with a textField and an apply button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog editDescDialog( String message, final Strain theStrain ) {
		if ( theStrain==null )
			return null;
//		final ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.8f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f );
		anotherDialog.getContentTable().row();
		
		final TextField nameField = new TextField( theStrain.description , skin );
		anotherDialog.getContentTable().add( nameField ).width( w * 0.8f ).height( h * 0.15f );
		Assets.game.stage.setKeyboardFocus( nameField );
		nameField.setMaxLength( 130 );
  		nameField.selectAll();
  		nameField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		
		final FitButton yesbutton = new FitButton( "Apply", skin );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.8f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.025f ).padRight( w*0.025f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Gdx.input.setOnscreenKeyboardVisible( false );
 				anotherDialog.hide();
 				String newName = nameField.getText();
 				
 				theStrain.description = newName;
 				Assets.getScreenHandler().getScreenPart( StrainPart.class ).populated = false;
 			}
 		});
		anotherDialog.getButtonTable().row();
		
		final FitButton cancelbutton = new FitButton( "Cancel", skin );
		cancelbutton.setColor( skin.getColor( "red" ) );
		anotherDialog.getButtonTable().add( cancelbutton ).width( w * 0.8f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.025f ).padRight( w*0.025f );

		cancelbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});

		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
//	/** provides a help dialog
//	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
//	public static Dialog helpDialog( String message ) {
//		ShroomClass game = Assets.game;
//		float w = game.width;
//		float h = game.height;
//		final Skin skin = Assets.getSkin();
////		final Dialog anotherDialog = new Dialog( "", skin.get( "Info", WindowStyle.class) );
//		if ( dialogPool == null )
//			dialogPool = new DialogPool();
//		final Dialog anotherDialog = dialogPool.borrowObject();
//		anotherDialog.setStyle( skin.get( "Info", WindowStyle.class) );
//		anotherDialog.clear();
//		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
//		myLabel.setColor( skin.getColor( "black" ) );
//		myLabel.setWrap( true );
//		myLabel.setAlignment( Align.center );
//		myLabel.setWidth( w * 0.8f );
//		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() )
//		.padBottom( h*0.05f ).padTop( h*0.1f );
//		
//		final FitButton yesbutton = new FitButton( "Okay", skin );
//		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.8f ).height( h * 0.15f ).padBottom( h*0.05f ).padLeft( w*0.025f ).padRight( w*0.025f );
//
//		yesbutton.addListener( new ChangeListener() {
// 			public void changed (ChangeEvent event, Actor actor) {
// 				anotherDialog.hide();
// 			}
// 		});
//		
//		game.showDialog( anotherDialog );
//		
//		return anotherDialog;
//	}
	
	/** provides a dialog with a title label across the top, a scroll table with buttons, and a cancel button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog scrollTableDialog( String message, ArrayList<Button> buttons ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setWidth( w * 0.8f );
		myLabel.setAlignment( Align.center );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() ).padBottom( h*0.05f );
		anotherDialog.getContentTable().row();
		
		float buttonWidth = w * 0.7f;
		float buttonHeight = buttonWidth * 0.25f;
		
		float maxScrollTableSize = h * 0.8f - (myLabel.getPrefHeight() + h * 0.125f) ;
		
		Table scrollTable = new Table();
		
		Iterator<Button> buttIt = buttons.iterator();
		while ( buttIt.hasNext() ) {
			Button thisButton = buttIt.next();
			scrollTable.add( thisButton ).width( buttonWidth ).height( buttonHeight ).padBottom( h*0.025f );
			thisButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				anotherDialog.hide();
//	 				resize( w, h );
	 			}
	 		});
			scrollTable.row();
		}

		if ( buttons.size() *(buttonHeight + h*0.025f ) > maxScrollTableSize ) {
			ScrollPane pane = new ScrollPane( scrollTable, skin.get( "NoBack", ScrollPaneStyle.class ) );
			anotherDialog.getContentTable().add(pane).width( w - w * 0.15f ).height( maxScrollTableSize ).padLeft( w*0.04f).padRight( w*0.04f).padBottom( h*0.025f );
		} else {
			anotherDialog.getContentTable().add(scrollTable).width( w - w * 0.15f ).height( buttons.size() *(buttonHeight + h*0.025f ) ).padLeft( w*0.04f).padRight( w*0.04f).padBottom( h*0.025f );
		}
		
		
		final FitButton cancelbutton = new FitButton( "Cancel", skin );
		cancelbutton.setColor( skin.getColor( "white" ) );
		anotherDialog.getButtonTable().add( cancelbutton ).width( w * 0.8f ).height( h * 0.1f ).padBottom( h*0.025f );

		cancelbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});
		
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a dialog with a title label across the top, a scroll table with buttons, and a cancel button.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog buyCoinsDialog( ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Coins", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Coins", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Label myLabel = new Label( "Purchase more Blue Coins", skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setWidth( w * 0.8f );
		myLabel.setAlignment( Align.center );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.8f ).height( myLabel.getPrefHeight() ).padBottom( h*0.05f );
		anotherDialog.getContentTable().row();
		
		float buttonWidth = w * 0.7f;
		float buttonHeight = buttonWidth * 0.25f;
		
		float maxScrollTableSize = h * 0.8f - (myLabel.getPrefHeight() + h * 0.125f) ;
		
		Table scrollTable = new Table();
		
		ArrayList<Button> buttons = new ArrayList<Button>();
		
		final String[] coinCosts = new String[] { "5 for $1.00 USD", "20 for $3.00 USD",
				"39 for $5.00 USD", "50 for $6.00 USD", "100 for $10.00 USD" };
		final String[] coinSkus = new String[] { ST.SKU_FIVE, ST.SKU_TWENTY,
				ST.SKU_THIRTY_NINE, ST.SKU_FIFTY, ST.SKU_HUNDRED };
		
		TextureRegion coinIcon = skin.getRegion( "CoinIcon" );
		TextureRegion coinSaleIcon = skin.getRegion( "CoinSaleIcon" );
		
		if ( Assets.isMonday ) {
			final FitButton buyCoinsButton = new FitButton("100 for $8.00 USD", coinSaleIcon, skin );
			buyCoinsButton.setColor( skin.getColor( "buyCoin" ) );
			buyCoinsButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Assets.getResolver().buySku( ST.SKU_MONDAY );
	 			}
			} );
			buttons.add( buyCoinsButton );
		}
		
		for ( int i = 0; i < 5; i ++ ) {
			final int j = i;
			final FitButton buyCoinsButton = new FitButton( coinCosts[i], coinIcon, skin );
			buyCoinsButton.setColor( skin.getColor( "buyCoin" ) );
			buyCoinsButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Assets.getResolver().buySku( coinSkus[j] );
	 			}
			} );
			buttons.add( buyCoinsButton );
		}
		
		Iterator<Button> buttIt = buttons.iterator();
		while ( buttIt.hasNext() ) {
			Button thisButton = buttIt.next();
			scrollTable.add( thisButton ).width( buttonWidth ).height( buttonHeight ).padBottom( h*0.025f );
			thisButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				anotherDialog.hide();
//	 				resize( w, h );
	 			}
	 		});
			scrollTable.row();
		}

		if ( buttons.size() *(buttonHeight + h*0.025f ) > maxScrollTableSize ) {
			ScrollPane pane = new ScrollPane( scrollTable, skin.get( "NoBack", ScrollPaneStyle.class ) );
			anotherDialog.getContentTable().add(pane).width( w - w * 0.15f ).height( maxScrollTableSize ).padLeft( w*0.04f).padRight( w*0.04f).padBottom( h*0.025f );
		} else {
			anotherDialog.getContentTable().add(scrollTable).width( w - w * 0.15f ).height( buttons.size() *(buttonHeight + h*0.025f ) ).padLeft( w*0.04f).padRight( w*0.04f).padBottom( h*0.025f );
		}
		
		
		final FitButton cancelbutton = new FitButton( "Cancel", skin );
		cancelbutton.setColor( skin.getColor( "white" ) );
		anotherDialog.getButtonTable().add( cancelbutton ).width( w * 0.8f ).height( h * 0.1f ).padBottom( h*0.025f );

		cancelbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 			}
 		});
		
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
	/** provides a speech bubble dialog with a character name title across the top and a label message.
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog speechDialog( String name, String message ) {
//		ShroomClass game = Assets.game;
//		float w = game.width;
//		float h = game.height;
		final Skin skin = Assets.getSkin();
//		final Dialog dialog = new Dialog( "", skin.get( "Speech", WindowStyle.class ) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog dialog = dialogPool.borrowObject();
		dialog.setStyle( skin.get( "Speech", WindowStyle.class) );
		dialog.getContentTable().clear();
		dialog.getButtonTable().clear();
		dialog.clearListeners();

		float iconHeight = Math.min( Gdx.graphics.getWidth()*0.2f, Gdx.graphics.getHeight()*0.3f );
		
		Image image = new Image( Assets.getSkin().getDrawable( "SpeechIcon" ) );
		image.setScaling( Scaling.fit );
		dialog.getContentTable().add( image ).width( iconHeight ).height( iconHeight );
		
		FitLabel fitLabel = new FitLabel( name, skin.get( "BasicWhite", LabelStyle.class ) );
		fitLabel.setColor( skin.getColor( "black" ) );
		dialog.getContentTable().add( fitLabel ).width( Gdx.graphics.getWidth() * 0.6f - iconHeight )
			.height( iconHeight );
		
		dialog.getContentTable().row();
		
		Label label = new Label( message, skin.get( "SmallBasic", LabelStyle.class ) );
		label.setColor( skin.getColor( "black" ) );
		label.setAlignment( Align.center );
		label.setWidth( Gdx.graphics.getWidth() * 0.6f );
		label.setWrap( true );
		dialog.getContentTable().add( label ).width( Gdx.graphics.getWidth() * 0.6f )
			.height( label.getPrefHeight() ).colspan( 2 )
			.pad( iconHeight * 0.2f );
		
		dialog.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            return true;
	        }
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	dialog.hide();
	        }
		});
		
		Assets.game.showDialog( dialog );
		
		return dialog;
	}
	
	/** provides a level up dialog
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog levelDialog( String message ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "XP", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "XP", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		
		Image image  = new Image( Assets.getAtlas().createSprite( "LevelUpIcon" ) );
		image.setScaling( Scaling.fillY );
		anotherDialog.getContentTable().add(image).width( w * 0.25f ).height( w * 0.25f );
		anotherDialog.getContentTable().row();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "darkPink" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.75f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.75f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
		
		final FitButton yesbutton = new FitButton( "Sweet", skin );
		yesbutton.setColor( skin.getColor( "white" ) );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.6f ).height( h * 0.15f ).padBottom( h*0.05f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.game.needsUnpopulate = true;
 				anotherDialog.hide();
 			}

 		});
//		yesbutton.addListener( sweetEffect );
		
		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	/** provides an "objective completed!" dialog
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog objectiveDialog( String message ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
		final int xpReward = 90 + 10 * Assets.player.level;
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Objective", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Objective", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		Image image  = new Image( Assets.getAtlas().createSprite( "ObjectiveCompleteIcon" ) );
		image.setScaling( Scaling.fillY );
		anotherDialog.getContentTable().add(image).width( w * 0.25f ).height( w * 0.25f );
		anotherDialog.getContentTable().row();
		Label myLabel = new Label( message + " Reward: 2 Blue Coins and " + ST.withComas( xpReward ) + "xp", skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setWrap( true );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.75f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.75f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
		
		final FitButton yesbutton = new FitButton( "Darn Right", skin );
		yesbutton.setColor( skin.getColor( "white" ) );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.6f ).height( h * 0.15f ).padBottom( h*0.05f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				anotherDialog.hide();
 				Assets.player.changeCoins( 2 );
 				Assets.player.increaseXP( xpReward );
 				Assets.player.totalObjectivesDone += 1;
//	 			Profiles.autoSave();
 			}
 		});

		Assets.game.showDialog( anotherDialog );
		
		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQBw" );
		
		return anotherDialog;
	}
//	/** provides a side job dialog
//	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
//	public static Dialog sideJobDialog( String message, final Project project ) {
////		ShroomClass game = Assets.game;
//		float w = Assets.width;
//		float h = Assets.height;
//		final Skin skin = Assets.getSkin();
////		final Dialog anotherDialog = new Dialog( "", skin.get( "Alerts", WindowStyle.class) );
//		if ( dialogPool == null )
//			dialogPool = new DialogPool();
//		final Dialog anotherDialog = dialogPool.borrowObject();
//		anotherDialog.setStyle( skin.get( "Alerts", WindowStyle.class) );
//		anotherDialog.getContentTable().clear();
//		anotherDialog.getButtonTable().clear();
//		anotherDialog.clearListeners();
//		Image image  = new Image( Assets.getAtlas().createSprite( "SideJobIcon" ) );
//		image.setScaling( Scaling.fillY );
//		anotherDialog.getContentTable().add(image).width( w * 0.25f ).height( w * 0.25f );
//		anotherDialog.getContentTable().row();
//		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
//		myLabel.setColor( skin.getColor( "black" ) );
//		myLabel.setWrap( true );
//		myLabel.setAlignment( Align.center );
//		myLabel.setWidth( w * 0.75f );
//		anotherDialog.getContentTable().add(myLabel).width( w * 0.75f ).height( myLabel.getPrefHeight() )
//		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
//		
//		final FitButton nobutton = new FitButton( "Nah", skin );
//		nobutton.setColor( skin.getColor( "red" ) );
//		anotherDialog.getButtonTable().add( nobutton ).width( w * 0.3f ).height( h * 0.15f ).padBottom( h*0.05f );
//
//		nobutton.addListener( new ChangeListener() {
// 			public void changed (ChangeEvent event, Actor actor) {
// 				anotherDialog.hide();
// 			}
// 		});
//		
//		final FitButton yesbutton = new FitButton( "Sure", skin );
//		yesbutton.setColor( skin.getColor( "green" ) );
//		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.4f ).height( h * 0.15f ).padBottom( h*0.05f );
//
//		yesbutton.addListener( new ChangeListener() {
// 			public void changed (ChangeEvent event, Actor actor) {
// 				anotherDialog.hide();
// 				Assets.getResolver().trackerSendEvent( "Side Jobs", "Accepted", project.name, (long)0 );
//// 				game.appResolver.trackerSend( MapBuilder.createEvent("Side Jobs", "Accepted", project.name, (long)0 ).build() );
// 				// TODO: add a link to project
//// 				changeScreen( game.getMinigame( project ), true );
// 			}
// 		});
//
//		Assets.game.showDialog( anotherDialog );
//		
//		return anotherDialog;
//	}
	
	/** provides a "Thanks for purchasing Blue Coins!" dialog, adds it to persistent
	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
	public static Dialog blueCoinDialog( String message, final int num ) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
//		final Dialog anotherDialog = new Dialog( "", skin.get( "Coins", WindowStyle.class) );
		if ( dialogPool == null )
			dialogPool = new DialogPool();
		final Dialog anotherDialog = dialogPool.borrowObject();
		anotherDialog.setStyle( skin.get( "Coins", WindowStyle.class) );
		anotherDialog.getContentTable().clear();
		anotherDialog.getButtonTable().clear();
		anotherDialog.clearListeners();
		
		Image image  = new Image( Assets.getAtlas().createSprite( "CoinIconAb" ) );
		image.setScaling( Scaling.fillY );
		anotherDialog.getContentTable().add(image).width( w * 0.25f ).height( w * 0.25f );
		anotherDialog.getContentTable().row();
		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.75f );
		anotherDialog.getContentTable().add(myLabel).width( w * 0.75f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
		
		final FitButton yesbutton = new FitButton( "Sweet", skin );
		yesbutton.setColor( skin.getColor( "white" ) );
		anotherDialog.getButtonTable().add( yesbutton ).width( w * 0.6f ).height( h * 0.15f ).padBottom( h*0.05f );

		yesbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.changeCoins( num );
 				Profiles.save();
 				anotherDialog.hide();
 			}
 		});

		Assets.game.showDialog( anotherDialog );
		
		return anotherDialog;
	}
	
//	/** provides a dialog with a white "ok" button and a yellow "Get more coins" button
//	 * given here to provide uniformality. Returns the dialog for whatever reason, also adds it right to the screen */
//	public static Dialog loadingDialog( String message ) {
//		ShroomClass game = Assets.game;
//		float w = game.width;
//		float h = game.height;
//		final Skin skin = Assets.getSkin();
////		final Dialog anotherDialog = new Dialog( "", skin.get( "Help", WindowStyle.class) );
//		if ( dialogPool == null )
//			dialogPool = new DialogPool();
//		final Dialog anotherDialog = dialogPool.borrowObject();
//		anotherDialog.setStyle( skin.get( "Help", WindowStyle.class) );
//		anotherDialog.clear();
//		Label myLabel = new Label( message, skin.get( "SmallBasic", LabelStyle.class) );
//		myLabel.setColor( skin.getColor( "black" ) );
//		myLabel.setWrap( true );
//		myLabel.setAlignment( Align.center );
//		myLabel.setWidth( w * 0.5f );
//		anotherDialog.getContentTable().add(myLabel).width( w * 0.5f ).height( myLabel.getPrefHeight() )
//		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
//		
//		anotherDialog.getContentTable().row();
//		
//		LoadingSpinner spinner = new LoadingSpinner( Assets.getAtlas().findRegion( "Loading" ) );
//		anotherDialog.getContentTable().add( spinner ).width( w * 0.25f ).height( w * 0.25f )
//		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
//
//		game.showDialog( anotherDialog );
//		
//		return anotherDialog;
//	}
}
