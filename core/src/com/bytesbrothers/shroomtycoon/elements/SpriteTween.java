package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteTween implements TweenAccessor<Sprite> {
	public static final int ALPHA = 1;
	public static final int ROTATION = 2;
	public static final int X = 3;
	public static final int Y = 4;

	@Override
	public int getValues( Sprite target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		case ROTATION:
			returnValues[0] = target.getRotation();
			return 1;
		case X:
			returnValues[0] = target.getX();
			return 1;
		case Y:
			returnValues[0] = target.getY();
			return 1;
		default:
			assert false;
			return 0;
		}
	}

	@Override
	public void setValues( Sprite target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case ALPHA:
			Color color = target.getColor();
			target.setColor( color.r, color.g, color.b, newValues[0] );
			break;
		case ROTATION:
			target.setRotation( newValues[0] );
			break;
		case X:
			target.setX( newValues[0] );
			break;
		case Y:
			target.setY( newValues[0] );
			break;

		default: assert false; break;
		}
	}

}
