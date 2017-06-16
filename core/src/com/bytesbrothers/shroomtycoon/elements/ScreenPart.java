package com.bytesbrothers.shroomtycoon.elements;

import java.util.ArrayList;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.CD;

/**
 * Similar to ColumnTable or TableScreen but with the ability to check, tell, and alter it's position on the stage
 * as well as it's size.
 * @author Brian
 *
 */
public abstract class ScreenPart extends Table {

	//--------------------------------
	//		ScreenPart properties
	//--------------------------------
	/** Whether or not this ScreenPart will handle the 'back' button.*/
//	public boolean backable = true;
	public boolean renderable = false;
	
	public static final int MIN_BUTTON_WIDTH = 20;
	public static final int MIN_BUTTON_HEIGHT = 20;
	public static final float BUTTON_WIDTH_MOD = 0.95f;
	public static final float BUTTON_HEIGHT_MOD = 0.18f;
	
	protected Skin skin;
//	protected ShroomPlayer player;
	public ArrayList<ScreenButton> buttons = new ArrayList<ScreenButton>();
	public ArrayList<ScreenLabel> labels = new ArrayList<ScreenLabel>();

	/** The name of the table. Does NOT appear anywhere, mostly for comparing and finding purposes.*/
	public String header = "";
	public Vector2 actualCoords = new Vector2( 0, 0 );

	public boolean populated = false;
	public boolean needsResize = true;
	
	/** This ScreenPart's desired position, represented as a percentage of the screen's actual size. 
	 * Default is full screen.*/
	public float desiredX = 0.0f, desiredY = 0.0f;
	/** This ScreenPart's desired size, represented as a percentage of the screen's actual size. 
	 * Default is full screen. (I can't think of a reason for these values to be >1)*/
	public float desiredWidth = 1.0f, desiredHeight = 1.0f;
	
	// Notes: libgdx's stage has (0,0) in the bottom left and the table's x and y describing it's center
	
//	public float width, height;

	public ScreenPart( String header ) {
		this.header = header;
	}

	
	//----------------------------------
	//	Functions that handle the frame to frame and life-cycle of this ScreenPart
	//----------------------------------
	
	public void pause() {
		needsResize = true;
	}
	
//	/**
//	 * Attempts to resize this ScreenPart based on the dimensions of the screen
//	 * @param width the width of the screen
//	 * @param height the height of the screen
//	 */
//	public void tryResize( float width, float height ) {
//		player = Assets.getPlayer();
//		
//		if ( populated && player!=null ) {
//			resize( width, height );
//			needsResize = false;
//		}
//	}
	
	@Override
	public void act( float delta ) {
		super.act( delta );
		
		if ( !populated ) {
//			player = Assets.getPlayer();
			skin = Assets.getSkin();
			if ( Assets.player!=null && skin!=null ) {
				populate();
				populated = needsResize = true;
			}
		}
		
		if ( needsResize && populated ) {
//			System.out.println( "TryResize" );
//			player = Assets.getPlayer();
			resize( );
			needsResize = false;
		}
		if ( Assets.player!=null && skin!=null && populated ) {
			screenAct( delta );
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
		}
	}
	
	@Override
	public void draw( Batch batch, float parentAlpha ) {
		if ( CD.RectAndRect( 0, 0, Assets.width, Assets.height,
				actualCoords.x - Assets.width*desiredWidth*0.5f, 
				actualCoords.y - Assets.height*desiredHeight*0.5f,
				actualCoords.x + Assets.width*desiredWidth*0.5f, 
				actualCoords.y + Assets.height*desiredHeight*0.5f ) )
			super.draw( batch, parentAlpha );
	}
	
	//------------------------------------
	//	Functionality functions
	//------------------------------------
	public float getActualX() {
		return actualCoords.x;
	}
	
	public float getActualY() {
		return actualCoords.y;
	}
	
	public void setD( float x, float y ) {
		desiredX = x;
		desiredY = y;
		needsResize = true;
	}
	
	public void setDx( float x ) {
		desiredX = x;
		needsResize = true;
	}
	
	public void setDy( float y ) {
		desiredY = y;
		needsResize = true;
	}
	
	public void setDWidth( float width ) {
		if ( desiredWidth!=width ) {
			Tween.to( this, ScreenPartTween.WIDTH, Assets.TRANS_TIME )
	        .target( width )
	        .ease(TweenEquations.easeOutQuad)
	        .start( Assets.getTweenManager() ); //** start it
		}
	}
	
	public void setDHeight( float height ) {
		if ( desiredHeight!=height ) {
			Tween.to( this, ScreenPartTween.HEIGHT, Assets.TRANS_TIME )
	        .target( height )
	        .ease(TweenEquations.easeOutQuad)
	        .start( Assets.getTweenManager() ); //** start it
		}
	}
	
	/** pass-through for if the default ScreePart.act function is overwritten and skipped */
	public void superAct( float delta ) {
		super.act( delta );
	}
	
	/**
	 * Holds a FitButton and all the requirements for populating, resizing, and acting on it.
	 * @author Brian
	 *
	 */
	public class ScreenButton {
		public FitButton fitButton;
		public String styleName = "default";
		public String iconName = null;
		public String colorName = "white";
//		public boolean hasAct = false;
		public float scaleX = 1.0f;
		public float scaleY = 1.0f;
		public Table table = null;
		
		public ScreenButton( FitButton button ) {
			fitButton = button;
		}
		
		public ScreenButton( String buttonText, String iconName, String colorName, Skin skin ) {
			fitButton = new FitButton( buttonText, skin.getRegion( iconName ), skin );
			fitButton.setColor( skin.getColor( colorName ) );
			this.colorName = colorName;
			this.iconName = iconName;
		}
		
		/**
		 * Appears like but is not to be confused with Actor.act( delta ).
		 * @param delta
		 */
		public void act( float delta ) { }
		
		public void resize( int buttonWidth, int buttonHeight ) {
			fitButton.setStyle( skin.get( styleName, FitButton.FitButtonStyle.class ) );
			fitButton.setColor( skin.getColor( colorName ) );
			if ( fitButton.image!=null && iconName!=null )
				fitButton.image.setDrawable( skin.getDrawable( iconName ) );
			if ( table==null )
				getCell( fitButton )
					.width( buttonWidth * scaleX )
					.height( buttonHeight * scaleY );
			else
				table.getCell( fitButton )
				.width( buttonWidth * scaleX )
				.height( buttonHeight * scaleY );
		}
	}
	
	/**
	 * Holds a FitLabel and all the requirements for populating, resizing, and acting on it.
	 * @author Brian
	 *
	 */
	public class ScreenLabel {
		public FitLabel label;
		public String styleName = "default";
		public String colorName = "white";
//		public boolean hasAct = false;
		public float scaleX = 1.0f;
		public float scaleY = 1.0f;
		public Table table = null;
		
		public ScreenLabel( FitLabel label ) {
			this.label = label;
		}
		
		/**
		 * Appears like but is not to be confused with Actor.act( delta ).
		 * @param delta
		 */
		public void act( float delta ) { }
		
		public void resize( int buttonWidth, int buttonHeight ) {
			label.setStyle( skin.get( styleName, LabelStyle.class ) );
			label.setColor( skin.getColor( colorName ) );
			if ( table==null )
				getCell( label )
					.width( buttonWidth * scaleX )
					.height( buttonHeight * scaleY );
			else
				table.getCell( label )
				.width( buttonWidth * scaleX )
				.height( buttonHeight * scaleY );
		}
	}
	
	//------------------------------------
//		Functions that MUST be implemented in the subclass
	//------------------------------------
	
	/**
	 * Runs exactly once after: the table has been created, the table has been added to the stage, and all assets have been loaded. 
	 */
	protected abstract void populate();
	
	public abstract void dispose();
	
	/** Called when the player presses the 'back' button. Returns the ScreenPart that will become active and/or handle the next 'back' button. */
	public abstract void back();
	
	
	//-----------------------------------
	//	Functions that CAN/MAY be extended in the subclass
	//-----------------------------------
	
	public InputProcessor getInput() { return null; }

	public void resume() { }
	
	/**
	 * Runs before the stage is drawn IF {@link #renderable} is true.
	 */
	public void render() { }
	
	/**
	 * Resizes this ScreenPart based on the dimensions of the screen. Should only be called through {@link #tryResize}.
	 * Also handles the ScreenPart's position, tweening the ScreenPart if it is out of place.
	 * @param width the width of the screen
	 * @param height the height of the screen
	 */
	protected void resize( ) {
		skin = Assets.getSkin();
		if ( !buttons.isEmpty() || !labels.isEmpty() ) {
			int buttonHeight = (int)Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
			int buttonWidth = (int)Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
			
			for ( ScreenButton button: buttons ) {
				button.resize( buttonWidth, buttonHeight );
			}
			
			for ( ScreenLabel label: labels ) {
				label.resize( buttonWidth, buttonHeight );
			}
		}
		
		int x = (int)(Assets.width * desiredX);
		int y = (int)(Assets.height * desiredY);
		int tableWidth = (int)(Assets.width * desiredWidth);
		int tableHeight = (int)(Assets.height * desiredHeight);
		
		Tween.to( this, ScreenPartTween.Y, Assets.TRANS_TIME )
        .target( y + tableHeight*0.5f )
        .ease(TweenEquations.easeOutQuad)
        .start( Assets.getTweenManager() ); //** start it
		
		Tween.to( this, ScreenPartTween.X, Assets.TRANS_TIME )
        .target( x + tableWidth*0.5f )
        .ease(TweenEquations.easeOutQuad)
        .start( Assets.getTweenManager() ); //** start it
	}
	
	/**
	 * Allows game functions to pass this ScreenPart objects and letting the specific extended class handle it.
	 * @param object any object. The ScreenPart subclass will either know what to do with it or do nothing.
	 */
	public void passObject( Object object ) { }
	
	/**
	 * Allows game functions to pass this ScreenPart objects and letting the specific extended class handle it.
	 * @param object any object. The ScreenPart subclass will either know what to do with it or do nothing.
	 */
	public Object getObject() { return null; }
	
	public void screenAct( float delta ) { }
	
	public void unpopulate() { }
	
	@Override
	public void clear() {
		super.clear();
		buttons.clear();
		labels.clear();
	}
	
	/**
	 * 
	 */
	public static class ScreenPartTween implements TweenAccessor<ScreenPart> {
		public static final int X = 1;
		public static final int Y = 2;
		public static final int WIDTH = 3;
		public static final int HEIGHT = 4;

		@Override
		public int getValues( ScreenPart target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case X:
				returnValues[0] = target.getX();
				return 1;
			case Y:
				returnValues[0] = target.getY();
				return 1;
			case WIDTH:
				returnValues[0] = target.desiredWidth;
				return 1;
			case HEIGHT:
				returnValues[0] = target.desiredHeight;
				return 1;
			default:
				assert false;
				return 0;
			}
		}

		@Override
		public void setValues( ScreenPart target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case X:
				target.setX( newValues[0] );
				break;
			case Y:
				target.setY( newValues[0] );
				break;
			case WIDTH:
				target.desiredWidth = newValues[0];
				target.needsResize = true;
				break;
			case HEIGHT:
				target.desiredHeight = newValues[0];
				target.needsResize = true;
				break;
			default: assert false; break;
			}
		}
	}
}
