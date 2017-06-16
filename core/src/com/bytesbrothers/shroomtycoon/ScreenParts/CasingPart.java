package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.elements.CasingDrawer;
import com.bytesbrothers.shroomtycoon.elements.IconButton;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Fruit;

public class CasingPart extends ScreenPart {

	public Casing theCasing;
	public CasingDrawer drawer;
	
	public static final int LOOKING = 0;
	public static final int HARVEST = 1;
	public static final int FAST_FORWARD = 2;
	public int doing = LOOKING;
	public int cameraType = HARVEST;
	public float fSpeed = 1.0f;
	public float fDelay = 0.0f;
	
	public CasingPartInput harvestInput;
	public CameraInputController camController;
	public Batch newBatch = null;
	
	private Image icon;
	private IconButton cameraButton;
	
	public CasingPart( Casing casing, int doing ) {
		super( "Casing" );
		drawer = Assets.game.casingDrawer;
		setCasing( casing, doing );
		renderable = true;
		
		

	    harvestInput = new CasingPartInput( this );
	    camController = new CameraInputController( drawer.cam );
	}
	
	public static class FruitData {
		public Fruit fruit;
		public int lastFrame;
		public BoundingBox bBox = null;
		
		public FruitData( Fruit fruit ) {
			this.fruit = fruit;
			this.lastFrame = fruit.getFrame();
		}
	}

	@Override
	public void populate() {
		clear();
		
		if ( theCasing!=null ) {

			ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
			add( button.fitButton );
			row();
			buttons.add( button );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				back();
	 			}
	 		});
			
			icon = new Image();
			add( icon );
			row();
			
			cameraButton = new IconButton( "", skin.getSprite( "MoveCameraIcon" ), skin );
			cameraButton.setColor( skin.getColor( "action" ) );
			add( cameraButton ).right();
			cameraButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
			 		if ( cameraType==HARVEST ) {
			 			cameraButton.icon.setDrawable( skin.getDrawable( "MoveCameraIcon" ) );
			 			cameraType = LOOKING;
			 			
			 		} else {
			 			cameraButton.icon.setDrawable( skin.getDrawable( "HarvestIcon" ) );
			 			cameraType = HARVEST;
			 		}
			 		
			 		Assets.game.updateInputs();
	 			}
	 		});
			cameraButton.setVisible( doing==HARVEST );
		}
	}
	
	@Override
	public Object getObject( ) {
		return theCasing;
	}
	
	@Override
	public void passObject( Object object ) {
		if ( object!=null && object.getClass().equals( Casing.class ) )
			setCasing( (Casing)object, LOOKING );
	}
	
	public void setCasing( Casing casing, int doing ) {
		theCasing = casing;
		this.doing = doing;
		populated = false;
		drawer.setCasing( theCasing );
//		System.out.println( "CasingPart.setCasing, doing: " + doing );
		Assets.game.updateInputs();
	}
	
	@Override
	protected void resize( ) {

		super.resize( );
		
		if ( theCasing!=null ) {
			drawer.resize();
			
			camController.camera = drawer.cam;

			float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
//			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, width * desiredWidth * BUTTON_WIDTH_MOD );
			
			getCell( icon )
				.width( Assets.width*desiredWidth )
				.height( Assets.height*desiredHeight - buttonHeight*2.0f );
			
			cameraButton.setStyle( skin.get( IconButton.IconButtonStyle.class ) );
			if ( cameraType==HARVEST ) {
				cameraButton.icon.setDrawable( skin.getDrawable( "HarvestIcon" ) );
	 		} else 
	 			cameraButton.icon.setDrawable( skin.getDrawable( "MoveCameraIcon" ) );

			getCell( cameraButton )
				.width( buttonHeight )
				.height( buttonHeight );
		}
	}
	
	@Override
	public void screenAct( float delta ) {
		switch( doing ) {
		case LOOKING:
			break;
		case HARVEST:
			break;
		case FAST_FORWARD:
			fDelay += delta * fSpeed;
			while ( fDelay>= 1.0f ) {
				theCasing.updateOneSec();
				fDelay -= 1.0f;
			}
			
			if ( theCasing.getProgress()==1.0f ) {
				doing = LOOKING;
//				game.back();
			}
			break;
		}
	}
	
	@Override
    public void render( ) {
		if ( populated && theCasing!=null ) {
			camController.update();
			
			drawer.render();
		}
    }
	
	@Override
	public void dispose() {
		drawer.dispose();
	}

	@Override
	public void back() {
		
		drawer.dispose();
		
		switch( doing ) {
		case LOOKING:
			break;
		case HARVEST:
			if ( newBatch!=null ) {
				Assets.player.addBatch( newBatch );
				Assets.player.totalHarvests += 1;
				newBatch = null;
				Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQBA" );
			}
			break;
		case FAST_FORWARD:
			while ( theCasing.getProgress()<1.0f ) {
				theCasing.updateOneSec();
			}
			break;
		}
		
//		ShroomClass game = Assets.game;
		CasingInfoPart screen = Assets.getScreenHandler().getScreenPart( CasingInfoPart.class );
//		ScreenPart screen = game.getScreenPart( "CasingInfo" );
		ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
//		ActionBarPart actionBarPart = (ActionBarPart) game.getScreenPart( "Action Bar" );
		actionBarPart.setDy( 0 );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	@Override
	public InputProcessor getInput() {
		if ( doing==HARVEST && cameraType==HARVEST )
			return new GestureDetector( harvestInput );
		return camController;
	}
	
	public static class CasingBuilder implements ScreenPartBuilder<CasingPart> {
		@Override
		public CasingPart build() {
			CasingPart part = new CasingPart( null, 0 );
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
