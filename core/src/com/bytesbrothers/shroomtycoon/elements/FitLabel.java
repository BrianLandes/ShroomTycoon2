package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FitLabel extends Label {

	public GlyphLayout layout = new GlyphLayout();

	public FitLabel(CharSequence text, Skin skin) {
		super(text, skin);
	}
	public FitLabel(CharSequence text, LabelStyle style) {
		super(text, style);
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		float leftWidth = getWidth() * 0.9f;
		float leftHeight = getHeight() * 0.9f;
		layout.setText(getStyle().font,getText());
		float ratio = Math.min( leftHeight/layout.height, leftWidth/layout.width );
		setFontScale( Math.max( ratio, 0.001f ) );
//		setFontScale(.1f, .1f);
		super.draw(batch,parentAlpha);

	}
}
