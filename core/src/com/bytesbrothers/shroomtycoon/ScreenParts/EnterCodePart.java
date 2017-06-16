package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Strain;

public class EnterCodePart extends ScreenPart {

	private TextField nameField;
	
	public EnterCodePart( ) {
		super( "Enter Code" );
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
			
		ScreenLabel label = new ScreenLabel( new FitLabel( "You know what to do", skin ) );
		label.scaleY = 0.9f;
		add( label.label );
		labels.add( label );
		row();
		
		String newName = nameField==null? "" : nameField.getText();
		nameField = new TextField( newName, skin );
		add( nameField );
		row();
  		Assets.game.stage.setKeyboardFocus( nameField );
  		nameField.selectAll();
			
		button = new ScreenButton( "Validate-izzle", "SideJobIcon", "action", skin );
		add( button.fitButton );
		buttons.add( button );
		row();
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Gdx.input.setOnscreenKeyboardVisible( false );
 				
 				String message = "Sorry, that is not a valid code.";
 				String code = nameField.getText();
 				nameField.setText( "" );
 				if ( code.equals( "WildeFriendship54") && !Assets.player.usedFreeHundred) {
 					message = "You have recieved 100 Blue Coins!";
 					Assets.player.changeCoins( 100 );
 					Assets.player.usedFreeHundred = true;
 					Assets.getResolver().trackerSendEvent( "Cheat", message, "WildeFriendship54", (long)0 );
 				} else if ( code.equals( "whatRUwaiting4" ) ) {
 					message = "You have recieved $1,000!";
 					Assets.player.changeCash( 1000 );
 					Assets.getResolver().trackerSendEvent( "Cheat", message, "whatRUwaiting4", (long)0 );
 				} else if ( code.equals( "MacaroniAndTunaFish" ) ) {
 					message = "You have recieved 100 Blue Coins!";
 					Assets.player.changeCoins( 100 );
 					Assets.getResolver().trackerSendEvent( "Cheat", message, "MacaroniAndTunaFish", (long)0 );
 				} else if ( code.equals( "iOnceLovedHeatherPorter" ) ) {
 					message = "You're over her now!";
 					Assets.player.changeCoins( 10000 );
 					Assets.player.changeCash( 100000 );
 					Assets.getResolver().trackerSendEvent( "Cheat", message, "iOnceLovedHeatherPorter", (long)0 );
 				} else if ( code.equals( "alliwannadoisgetteemo" ) ) {
 					message = "Cheat Screen";
 					CheatPart screen = Assets.getScreenHandler().getScreenPart( CheatPart.class );
// 					ScreenPart screen = Assets.game.getScreenPart( "Cheat" );

 	 				screen.setDx( 0 );
 	 				setDx( -1f );
 	 				
 	 				Assets.getScreenHandler().setActivePart( screen );
 	 				
 					Assets.getResolver().trackerSendEvent( "Cheat", message, "alliwannadoisgetteemo", (long)0 );
 				} else if ( code.equals( "I am always awake" ) ) {
 					message = "Strain Sim Screen";
 					StrainSimPart screen = Assets.getScreenHandler().getScreenPart( StrainSimPart.class );
// 					ScreenPart screen = Assets.game.getScreenPart( "Strain Sim" );

 	 				screen.setDx( 0 );
 	 				setDx( -1f );
 	 				
 	 				Assets.getScreenHandler().setActivePart( screen );

 					Assets.getResolver().trackerSendEvent( "Cheat", message, "I am always awake", (long)0 );
 				} else if ( code.equals( "Any price for Selena Gomez" ) ) {
 					message = "All Strains are now available";
// 					can_buy_strains = new ArrayList<String>();
// 					can_buy_strains.add( "Golden Teacher" );
// 					can_buy_strains.add( "B+" );
// 					can_buy_strains.add( "Acadian Coast" );
// 					can_buy_strains.add( "Costa Rica" );
// 					can_buy_strains.add( "Lizard King" );
// 					can_buy_strains.add( "Ecuador" );
// 					can_buy_strains.add( "Philosopher's Stone" );
// 					can_buy_strains.add( "India Orissa" );
 					Assets.player.can_buy_strains.add( "Super M" );
 					Assets.player.can_buy_strains.add( "Albino Envy" );
 					Assets.player.can_buy_strains.add( "Z Strain" );
 					Assets.player.can_buy_strains.add( "Ocean Body" );
 					Assets.player.can_buy_strains.add( "Business Man" );
 					Assets.player.can_buy_strains.add( "Fun Guy" );
 					Assets.player.can_buy_strains.add( "Jeepers Creepers" );
 					Assets.player.can_buy_strains.add( "Quick Cubes" );
 					Assets.player.can_buy_strains.add( "Cambodian" );
 					Assets.player.can_buy_strains.add( "Treasure Coast" );
 					Assets.player.can_buy_strains.add( "Stamets" );
 					Assets.player.can_buy_strains.add( "Midnight" );
 					Assets.player.can_buy_strains.add( "Easter Egg" );
 					Assets.getResolver().trackerSendEvent( "Cheat", message, "I am always awake", (long)0 );
 				} else if ( code.equals( "do you believe in lucid dreaming" ) ) {
 					message = "I had a Golden Master";
 					Strain newStrain = new Strain( "Golden Master", "" );
 					newStrain.costsCoins = true;
 					newStrain.jar_grow_time = 9917;
 					newStrain.casing_grow_time = 4185;
 					newStrain.pins_time = 36;
 					newStrain.fruit_grow_time = 2848;
 					newStrain.fruit_weight = 0.5121434f;
 					newStrain.fruit_weight_Var = 0.23f;
 					newStrain.potency = 8.827902f;
 					newStrain.potency_Var = 0.65f;
 					newStrain.batch_weight = 22.745853f;
 					newStrain.batch_weight_Var = 6.4f;
 					newStrain.temp_required = ST.MEDIUM;
 					newStrain.humidity_cost = 10;
 					newStrain.substrate = ST.COCO_COIR;
 					newStrain.colors.add( ST.X );
 					newStrain.fruit_width_Var = 0.4f;
 					newStrain.fruit_height_Var = 0.05f;
 					Assets.player.strains.add( newStrain );

 					Assets.getResolver().trackerSendEvent( "Cheat", message, "do you believe in lucid dreaming", (long)0 );
 				}
 				
 				Dialogs.acceptDialog( message );
 			}
 		});
	}
	
	@Override
	protected void resize( ) {

		super.resize( );
		
		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
		
		nameField.setStyle( skin.get( TextFieldStyle.class ) );
		getCell( nameField )
			.width( buttonWidth )
			.height( buttonHeight )
			.padTop( ( Assets.height * desiredHeight - buttonHeight*4.0f )* 0.4f )
			.padBottom( ( Assets.height * desiredHeight - buttonHeight*4.0f )* 0.6f )
			;
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void back() {
//		ShroomClass game = Assets.game;
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );

		screen.setDx( 0 );
		setDx( 1f );
		
		ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
		actionBarPart.setDy( 0 );

		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public static class EnterCodeBuilder implements ScreenPartBuilder<EnterCodePart> {
		@Override
		public EnterCodePart build() {
			EnterCodePart part = new EnterCodePart();
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
