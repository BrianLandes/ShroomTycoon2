package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.badlogic.gdx.utils.Align;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.FitCheckButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;

public class OptionsPart extends ScreenPart {

	private ScrollPane pane;
	private Table table;
	private FitCheckButton useNotesButton;

	public OptionsPart() {
		super( "Options" );
	}

	@Override
	public void populate() {
		clear();

		table = new Table();

		ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
		button.table = table;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				back();
 			}
 		});

		ScreenLabel label = new ScreenLabel( new FitLabel( "Options", skin.get( "BasicWhite", Label.LabelStyle.class ) ) );
		label.styleName = "BasicWhite";
		label.colorName = "black";
		label.label.setAlignment( Align.center );
		label.table = table;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		useNotesButton = new FitCheckButton( "Enable Notifications", skin );
		useNotesButton.setChecked( Assets.player.useNotifications );
		table.add( useNotesButton );
		useNotesButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Assets.player.useNotifications = !Assets.player.useNotifications;
 				useNotesButton.setChecked( Assets.player.useNotifications );
 				if ( Assets.player.useNotifications )
 					Assets.getResolver().trackerSendEvent( "Notifications", "Use", "Use", (long)0 );
 				else
 					Assets.getResolver().trackerSendEvent( "Notifications", "Do Not Use", "Don't Use", (long)0 );
 			}
 		});
		
		pane = new ScrollPane( table, skin);
		add( pane );
		pane.addListener( Assets.stopTouchDown() );
	}

	@Override
	protected void resize( ) {
		super.resize( );
		
		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );

		useNotesButton.setStyle( skin.get( FitCheckButton.FitCheckButtonStyle.class ) );
		table.getCell( useNotesButton )
			.width( buttonWidth ).height( buttonHeight );
		
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
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public static class OptionsBuilder implements ScreenPartBuilder<OptionsPart> {
		@Override
		public OptionsPart build() {
			OptionsPart part = new OptionsPart( );
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
