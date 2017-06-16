package com.bytesbrothers.shroomtycoon;

/**
 * Created by Brian on 6/15/2017.
 */

import com.badlogic.gdx.Application;

public interface AppResolver {
    public Application getApp();

    public void buySku( String SKU );
    public boolean isAndroid();

    //	public void trackerSend( Map<String, String> params );
    public void trackerSendEvent( String category, String action, String label, long value );
    public void trackerSendAppView( String screenName );

    public boolean saveToCloud( ShroomPlayer player, int slot );
    public boolean loadFromCloud( int slot );

    public void showSavedGamesUIforLoading();
    public void showSavedGamesUIforSaving();

    public void unlockAchievement( String id );
    public void incrementAchievement( String id, int value );
    public void showAchievements();

    public String getCurrentSaveName();
    public void setCurrentSaveName( String newSaveName );

    public void saveToSnapShot( ShroomPlayer player );
    public void loadFromSnapShot( String uniqueName );

    public int getSnapshotState();

    public boolean isGoogleSignedIn();
    public void googleSignIn();
    public void googleSignOut();

    public void startLoadScreen( String message );
    public void endLoadScreen();
    public void toast( String message );

    public void exitDialog();
    public String getEmail();
}
