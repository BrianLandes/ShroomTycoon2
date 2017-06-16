package com.bytesbrothers.shroomtycoon.client;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.bytesbrothers.shroomtycoon.AppResolver;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ShroomPlayer;
import com.bytesbrothers.shroomtycoon.elements.FitButton;

import java.math.BigInteger;
import java.util.Random;

public class AppResolverHtml implements AppResolver {

	Application activity;
	
	@Override
	public Application getApp() {
		return activity;
	}

	@Override
	public void buySku(String SKU) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trackerSendEvent(String category, String action, String label,
			long value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trackerSendAppView(String screenName) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void unlockAchievement(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incrementAchievement(String id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAndroid() {
		return false;
	}

	@Override
	public void showAchievements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean loadFromCloud(int slot) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveToCloud(ShroomPlayer player, int slot) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void showSavedGamesUIforLoading() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showSavedGamesUIforSaving() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCurrentSaveName() {
		// TODO Auto-generated method stub
		return "snapshotTemp";
	}

	@Override
	public void setCurrentSaveName(String newSaveName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveToSnapShot(ShroomPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFromSnapShot(String uniqueName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSnapshotState() {
		return 3;
	}

	@Override
	public boolean isGoogleSignedIn() {
		return false;
	}

	@Override
	public void googleSignIn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void googleSignOut() {
		// TODO Auto-generated method stub
		
	}

	Dialog loadDialog;
	
	@Override
	public void startLoadScreen( String message ) {
//		ShroomClass game = Assets.getGame();
//		float w = game.width;
//		float h = game.height;
//		final Skin skin = Assets.getSkin();
//		loadDialog = new Dialog( "", skin.get( WindowStyle.class) );
//		Label myLabel = new Label( message, skin.get( "small", LabelStyle.class) );
//		myLabel.setColor( skin.getColor( "black" ) );
//		myLabel.setWrap( true );
//		myLabel.setAlignment( Align.center );
//		myLabel.setWidth( w * 0.5f );
//		loadDialog.getContentTable().add(myLabel).width( w * 0.5f ).height( myLabel.getPrefHeight() )
//		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
//		
//		loadDialog.getContentTable().row();
//
//		game.showDialog( loadDialog );
		
	}

	@Override
	public void endLoadScreen() {
//		if ( loadDialog!=null )
//			loadDialog.hide();
	}

	@Override
	public void toast(String message) {
//		ShroomClass game = Assets.game;
		float w = Assets.width;
		float h = Assets.height;
		final Skin skin = Assets.getSkin();
		loadDialog = new Dialog( "", skin.get( WindowStyle.class) );
		Label myLabel = new Label( message, skin );
		myLabel.setColor( skin.getColor( "black" ) );
		myLabel.setWrap( true );
		myLabel.setAlignment( Align.center );
		myLabel.setWidth( w * 0.5f );
		loadDialog.getContentTable().add(myLabel).width( w * 0.5f ).height( myLabel.getPrefHeight() )
		.padBottom( h*0.05f ).padTop( h*0.1f ).padLeft( w*0.05f ).padRight( w*0.05f );
		
		loadDialog.getContentTable().row();
		
		FitButton yesButton = new FitButton( "Okay", skin );
		loadDialog.getButtonTable().add( yesButton ).width( w * 0.5f ). height( h * 0.2f );
		
		yesButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				loadDialog.hide();
 			}

 		});

		Assets.game.showDialog( loadDialog );
	}
	
	@Override
	public String getEmail() {
		Preferences prefs = Gdx.app.getPreferences("myWebserviceId");
		
		String emailName = prefs.getString( "emailName", null );
		
		if ( emailName == null ) {
			// This should only happen the first time
			// Generate a name and save it for the next time
			
			String unique = new BigInteger(50, new Random()).toString(13);
			emailName = "pcUser-" + unique;
			
			prefs.putString( "emailName", emailName );
			prefs.flush();
		}
		
		return emailName;
	}

	@Override
	public void exitDialog() {
		// TODO Auto-generated method stub
		
	}
}
