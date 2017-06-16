package com.bytesbrothers.shroomtycoon.ScreenParts.main;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.FloatingText;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.pools.FloatingTextPool;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Dealer;

public class StashPart extends ScreenPart {
	
	private ScrollPane pane;
	private Table scrollTable;
	
	float lastTotal = 0;

	private float lastScrollY;
	
	public StashPart( ) {
		super( "Stash" );
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
		
		ScreenLabel label = new ScreenLabel( new FitLabel( "Dried Shrooms", skin ) );
		label.table = scrollTable;
		label.table.add( label.label ).padTop( 14 );
		labels.add( label );
		label.table.row();
		
		lastTotal = Assets.player.getStash();
		
		ArrayList<Batch> batches = Assets.player.batches;
		int i = 0;
		for( final Batch batch: batches ) {
			if ( batch.weighed ) {
				button = new ScreenButton( "" + batch.strain.name + " " + ST.withComasAndTwoDecimals( batch.getWeight() ) + " g",
						"BatchIcon", "resource", skin );
				button.table = scrollTable;
				button.table.add( button.fitButton );
				button.table.row();
				buttons.add( button );

				button.fitButton.addListener( new ChangeListener() {
		 			public void changed (ChangeEvent event, Actor actor) {
		 				ArrayList<Button> buttons = new ArrayList<Button>();

		 				ArrayList<Dealer> dealers = Assets.player.dealers;
		 				
		 				for ( final Dealer dealer: dealers ) {
		 					FitButton myButton = new FitButton( dealer.name, skin.getRegion( "DealerIcon" ), skin );
		 					myButton.setDisabled( dealer.batches.size() >=dealer.capacity );
			 				myButton.setColor( myButton.isDisabled()? skin.getColor( "disabled" ) : skin.getColor( "action" ) );
			 				buttons.add( myButton );
			 				myButton.addListener( new ChangeListener() {
			 		 			public void changed (ChangeEvent event, Actor actor) {
			 		 				ShroomClass game = Assets.game;
			 		 				Assets.player.batches.remove( batch );
			 		 				dealer.batches.add( batch );
//	 		 		 				theDealer.alertedOnSale = false;
			 		 				if ( game.fTextPool==null )
			 		 					game.fTextPool = new FloatingTextPool();
			 		 				FloatingText fText = game.fTextPool.borrowObject();
			 		 				fText.setText( "-" + (int)batch.getWeight() + "g" );
			 		 				fText.setStyle( skin.get( "Stash",  Label.LabelStyle.class ) );
// 		 		 		 			FloatingText fText = new FloatingText( "-" + (int)batch.getWeight() + "g", skin.get( "Stash", FitLabelStyle.class ) );
// 		 		 					fText.setWidth( game.width * 0.25f );
// 		 		 					fText.setHeight( game.height * 0.1f );
 		 		 					fText.setX( Assets.width * 0.25f );
 		 		 					fText.setY( Assets.height/9.0f );
 		 		 					game.addFloatingText( fText );
// 		 		 					Tween.to( fText, FloatingTextTween.Y, 1.0f )
// 		 		 			        .target(  game.height/9.0f * 2.0f )
// 		 		 			        .ease(TweenEquations.easeOutQuad)
// 		 		 			        .start( Assets.getTweenManager() ); //** start it
// 		 		 					
// 		 		 					Tween.to( fText, FloatingTextTween.ALPHA, 1.0f )
// 		 		 			        .target( 0.0f )
// 		 		 			        .delay( 0.7f )
// 		 		 			        .ease(TweenEquations.easeInQuad)
// 		 		 			        .start( Assets.getTweenManager() ); //** start it
// 		 		 					
// 		 		 					fText.failSafe = 2.0f;
			 		 			}
			 		 		});
		 				}

		 				Dialogs.scrollTableDialog( "Front this batch?", buttons );
		 			}
		 		});

				i ++;
			}
		}
		
		if ( i==0 ) {
			label = new ScreenLabel( new FitLabel( "(none)", skin ) );
			label.table = scrollTable;
			label.table.add( label.label );
			labels.add( label );
		}
		
		if ( pane!=null )
			lastScrollY = pane.getScrollY();
		pane = new ScrollPane( scrollTable, skin);
		add( pane );
		pane.validate();
		pane.setScrollY( lastScrollY );
		pane.updateVisualScroll();
	}

	@Override
	public void screenAct( float delta ) {
		float newTotal = Assets.player.getStash();
		if ( newTotal!=lastTotal )
			populated = false;
		
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
	public void dispose() {
		
	}

	@Override
	public void back() {
		ScreenPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public static class StashBuilder implements ScreenPartBuilder<StashPart> {
		@Override
		public StashPart build() {
			StashPart part = new StashPart( );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2.0f );
			part.setY( Assets.height * 0.5f );
			part.desiredX = 1f;
			part.desiredY = Assets.game.actionBarHeight;
			part.desiredWidth = 1f;
			part.desiredHeight = 1f - Assets.game.actionBarHeight;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
}
