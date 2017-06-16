package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.StashPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.IconButton;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;

public class ActionBarPart extends ScreenPart {

	public IconButton cashbutton;
	public IconButton stashbutton;
	public IconButton coinbutton;
	public IconButton alertbutton;

	private ActionBar actionBar = new ActionBar();
//	private boolean alerted;

	public ActionBarPart() {
		super( "Action Bar" );
//		backable = false;
	}

	@Override
	public void populate() {
		clear();

		cashbutton = new IconButton( "",  skin.getSprite( "CashIconAb" ), skin.get( "Clear", IconButton.IconButtonStyle.class ) );
		cashbutton.iconTable.left();
		cashbutton.setColor( skin.getColor( "golden" ) );
		add(cashbutton);

  		cashbutton.addListener( new ChangeListener() {
  			public void changed (ChangeEvent event, Actor actor) {
  				ShroomClass game = Assets.game;
  				game.screenHandler.setActivePartDx( -1f );
  				
  				MainPart screen = (MainPart)game.screenHandler.getScreenPart( MainPart.class );
  				screen.setDx( 0 );
  				game.screenHandler.setActivePart( screen );
  				
  				
  				ActionBarPart actionBarPart = (ActionBarPart) game.screenHandler.getScreenPart( ActionBarPart.class );
  				actionBarPart.setDy( 0 );
  			
  				screen.quickOpen( MainPart.CASH );
  				
  			}
  		});

		stashbutton = new IconButton( "",  skin.getSprite( "StashIconAb" ), skin.get( "Clear", IconButton.IconButtonStyle.class ) );
		stashbutton.iconTable.left();
		stashbutton.setColor( skin.getColor( "stashGold" ) );
		add(stashbutton);

  		stashbutton.addListener( new ChangeListener() {
  			public void changed (ChangeEvent event, Actor actor) {
  				ShroomClass game = Assets.game;
  				game.screenHandler.setActivePartDx( -1f );
  				
  				ActionBarPart actionBarPart = (ActionBarPart) game.screenHandler.getScreenPart( ActionBarPart.class );
  				actionBarPart.setDy( 0 );
  				
  				ScreenPart screen = game.screenHandler.getScreenPart( StashPart.class );
 				screen.setDx( 0 );
 				game.screenHandler.setActivePart( screen );

  			}
  		});

		coinbutton = new IconButton( "",  skin.getSprite( "CoinIconAb" ), skin.get( "Clear", IconButton.IconButtonStyle.class ) );
		coinbutton.iconTable.left();
		coinbutton.setColor( skin.getColor( "coinBlue" ) );
		add(coinbutton);

  		coinbutton.addListener( new ChangeListener() {
  			public void changed (ChangeEvent event, Actor actor) {
  				Dialogs.buyCoinsDialog();
  			}
  		});
  		
  		alertbutton = new IconButton( "alerts", skin.getSprite( "AlertNoneIcon" ), skin.get( "Clear", IconButton.IconButtonStyle.class ) );
  		alertbutton.iconTable.right();
  		alertbutton.setDisabled( true );
  		add(alertbutton);
  		alertbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.alertsDialog();
 			}
 		} );
//  		alerted = false;

	}
	
	@Override
	public void screenAct( float delta ) {
		// update the actionbar
		int newCash = Assets.player.getCash();
		if ( newCash!=actionBar.lastCash ) {
			actionBar.lastCash = newCash;
			if( newCash >=100000000 ) {
				cashbutton.setText( ">100M" );
				cashbutton.resizeText();
			} else {
				if ( actionBar.cashTween!=null && !actionBar.cashTween.isFinished() )
					actionBar.cashTween.free();
				
				actionBar.cashTween = Tween.to( actionBar, ActionBarTween.CASH, 1.0f )
		        .target( newCash )
		        .ease(TweenEquations.easeOutQuad)
		        .start( Assets.getTweenManager() ); //** start it
			}
		}
		if ( actionBar.cashTween!=null && actionBar.cashTween.isFinished() && newCash!=actionBar.displayedCash ) {
			System.out.println( "ActionBar: cashTween failed, restarting" );
			actionBar.cashTween.free();
			
			actionBar.cashTween = Tween.to( actionBar, ActionBarTween.CASH, 1.0f )
	        .target( newCash )
	        .ease(TweenEquations.easeOutQuad)
	        .start( Assets.getTweenManager() ); //** start it
		}
		if ( actionBar.lastDisplayedCash!=actionBar.displayedCash ) {
			actionBar.lastDisplayedCash = actionBar.displayedCash;
			if ( actionBar.displayedCash >=100000000 ) {
				cashbutton.setText( ">100M" );
				cashbutton.resizeText();
			} else {
				cashbutton.setText( ST.withComas( actionBar.displayedCash ) );
				cashbutton.resizeText();
			}
		}

		if ( !stashbutton.getText().equals( ST.withComas( Assets.player.getStash() ) + "g" ) ) {
			stashbutton.setText( ST.withComas( Assets.player.getStash() ) + "g" );
			stashbutton.resizeText();
		}
		if ( !coinbutton.getText().equals( ST.withComas( Assets.player.coins ) )  ) {
			coinbutton.setText( ST.withComas( Assets.player.coins ) );
			coinbutton.resizeText();
		}
//		if ( !alertbutton.isDisabled() ) {
			if ( Assets.alerts.isEmpty() ) {
//				alerted = false;
		  		alertbutton.icon.setDrawable( skin.getDrawable( "AlertNoneIcon" ) );
		  		alertbutton.setDisabled( true );
			}
//		} else {
			else if ( !Assets.alerts.isEmpty() ) {
//				alerted = true;
		  		alertbutton.icon.setDrawable( skin.getDrawable( "AlertIcon" ) );
		  		alertbutton.setDisabled( false );
			}
//		}
	}
	
	@Override
	protected void resize( ) {

		super.resize( );
		
		float buttonHeight = Assets.height * desiredHeight;
		float buttonWidth = Assets.width * desiredWidth * 0.25f ;
		
		cashbutton.setStyle( skin.get( "Clear", IconButton.IconButtonStyle.class ) );
		cashbutton.setColor( skin.getColor( "golden" ) );
		cashbutton.icon.setDrawable( skin.getDrawable( "CashIconAb" ) );
		getCell( cashbutton )
			.width( buttonWidth )
			.height( buttonHeight );
		
		stashbutton.setStyle( skin.get( "Clear", IconButton.IconButtonStyle.class ) );
		stashbutton.setColor( skin.getColor( "stashGold" ) );
		stashbutton.icon.setDrawable( skin.getDrawable( "StashIconAb" ) );
		getCell( stashbutton )
			.width( buttonWidth )
			.height( buttonHeight );
		
		coinbutton.setStyle( skin.get( "Clear", IconButton.IconButtonStyle.class ) );
		coinbutton.setColor( skin.getColor( "coinBlue" ) );
		coinbutton.icon.setDrawable( skin.getDrawable( "CoinIconAb" ) );
		getCell( coinbutton )
			.width( buttonWidth )
			.height( buttonHeight );
		
		alertbutton.setStyle( skin.get( "Clear", IconButton.IconButtonStyle.class ) );
		alertbutton.icon.setDrawable( skin.getDrawable( "AlertNoneIcon" ) );
		alertbutton.setDisabled( true );
		getCell( alertbutton )
			.width( buttonWidth )
			.height( buttonHeight );
//		alerted = false;
		
		float x = Assets.width * desiredX;
		float y = Assets.height * desiredY;
		float tableWidth = Assets.width * desiredWidth;
		float tableHeight = Assets.height * desiredHeight;
		
		Tween.to( this, ScreenPartTween.Y, Assets.TRANS_TIME )
        .target( y + tableHeight*0.5f )
        .ease(TweenEquations.easeOutQuad)
        .start( Assets.getTweenManager() ); //** start it
		
		Tween.to( this, ScreenPartTween.X, Assets.TRANS_TIME )
        .target( x + tableWidth*0.5f )
        .ease(TweenEquations.easeOutQuad)
        .start( Assets.getTweenManager() ); //** start it
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void back() { }
	
	public class ActionBar {
		int lastCash = 0;
		int lastDisplayedCash = 0;
		int displayedCash = 0;
		Tween cashTween = null;
		
		
		int lastStash = 0;
		int lastDisplayedStash = 0;
		int displayedStash = 0;
		Tween stashTween = null;
	}
	
	/**
	 * 
	 */
	public static class ActionBarTween implements TweenAccessor<ActionBar> {
		public static final int CASH = 1;
		public static final int STASH = 2;
		public static final int COIN = 3;

		@Override
		public int getValues( ActionBar target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case CASH:
				returnValues[0] = target.displayedCash;
				return 1;
			case STASH:
				returnValues[0] = target.displayedStash;
				return 1;
//			case COIN:
//				returnValues[0] = target.desiredWidth;
//				return 1;
			default:
				assert false;
				return 0;
			}
		}

		@Override
		public void setValues( ActionBar target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case CASH:
				target.displayedCash = (int) newValues[0];
				break;
			case STASH:
				target.displayedStash = (int) newValues[0];
				break;
//			case COIN:
//				target.desiredWidth = newValues[0];
//				target.needsResize = true;
//				break;
			default: assert false; break;
			}
		}
	}
	
	
	public static class ActionBarBuilder implements ScreenPartBuilder<ActionBarPart> {
		@Override
		public ActionBarPart build() {
			ActionBarPart part = new ActionBarPart( );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 0.5f );
			part.setY( -Assets.height*Assets.game.actionBarHeight );
			part.desiredX = 0;
			part.desiredY = 0;
			part.desiredWidth = 1f;
			part.desiredHeight = Assets.game.actionBarHeight;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
}
