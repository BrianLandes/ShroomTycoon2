package com.bytesbrothers.shroomtycoon.elements;

import aurelienribon.tweenengine.TweenAccessor;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

public class FloatingText extends FitLabel {

	public FloatingText (CharSequence text, Skin skin) {
		this(text, skin.get(LabelStyle.class));
	}

	public FloatingText (CharSequence text, Skin skin, String styleName) {
		this(text, skin.get(styleName, LabelStyle.class));
	}

	/** Creates a label, using a {@link LabelStyle} that has a BitmapFont with the specified name from the skin and the specified
	 * color. */
	public FloatingText (CharSequence text, Skin skin, String fontName, Color color) {
		this(text, new LabelStyle(skin.getFont(fontName), color));
	}

	/** Creates a label, using a {@link LabelStyle} that has a BitmapFont with the specified name and the specified color from the
	 * skin. */
	public FloatingText (CharSequence text, Skin skin, String fontName, String colorName) {
		this(text, new LabelStyle(skin.getFont(fontName), skin.getColor(colorName)));
	}

	public FloatingText (CharSequence text, LabelStyle style) {
		super( text, style );
		setTouchable( Touchable.disabled );
		setAlignment( Align.center );
	}

	public float failSafe = 5;
	
	@Override
	public void act( float delta ) {
		super.act( delta );
		failSafe -= delta;
//		if ( getColor().a<=0.0f || failSafe<=0 )
//			remove();
	}
	
	public float getAlpha() {
		return getColor().a;
	}
	
	static public class FloatingTextTween implements TweenAccessor<FloatingText> {
		public static final int X = 1;
		public static final int Y = 2;
		public static final int ALPHA = 3;

		@Override
		public int getValues( FloatingText target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case X:
				returnValues[0] = target.getX();
				return 1;
			case Y:
				returnValues[0] = target.getY();
				return 1;
			case ALPHA:
				returnValues[0] = target.getColor().a;
				return 1;
			default:
				assert false;
				return 0;
			}
		}

		@Override
		public void setValues( FloatingText target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case X:
				target.setX(newValues[0]);
				break;
			case Y:
				target.setY(newValues[0]);
				break;
			case ALPHA:
				Color color = target.getColor();
				target.setColor( color.r, color.g, color.b, newValues[0] );
				break;

			default: assert false; break;
			}
		}

	}
}
