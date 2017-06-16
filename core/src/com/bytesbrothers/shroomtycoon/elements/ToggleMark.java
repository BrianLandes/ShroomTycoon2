package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ToggleMark extends Actor {

	public boolean toggle = false;
	ToggleMarkStyle style;
	
	public ToggleMark ( Skin skin) {
		this( skin.get( ToggleMarkStyle.class));
	}

	public ToggleMark ( Skin skin, String styleName) {
		this( skin.get( styleName, ToggleMarkStyle.class));
	}

	public ToggleMark ( ToggleMarkStyle style) {
		super();
		setStyle(style);
		this.style = style;
		
		setWidth( Math.max( style.up.getMinWidth(), style.down.getMinWidth() ) );
		setHeight( Math.max( style.up.getMinHeight(), style.down.getMinHeight() ) );
	}

	public void setStyle ( ToggleMarkStyle style ) {
		if (!(style instanceof ToggleMarkStyle)) throw new IllegalArgumentException("style must be a ToggleMarkStyle.");
		this.style = (ToggleMarkStyle)style;

	}
	
	@Override
	public void draw( Batch batch, float parentAlpha) {
		super.draw( batch, parentAlpha);
		Drawable background = null;

		if ( toggle ) {
			background = style.up;
		} else {
			background = style.down;
		}

		if (background != null) {
			Color color = getColor();
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			background.draw(batch, getX(), getY(), getWidth(), getHeight());
		}
	}

	static public class ToggleMarkStyle {
		Drawable up;
		Drawable down;
		
		public ToggleMarkStyle () {
		}

		public ToggleMarkStyle ( Drawable up, Drawable down ) {
			this.up = up;
			this.down = down;
		}

		public ToggleMarkStyle ( ToggleMarkStyle style) {
			this.up = style.up;
			this.down = style.down;
		}
	}
}
