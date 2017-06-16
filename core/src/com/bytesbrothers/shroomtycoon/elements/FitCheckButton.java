package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class FitCheckButton extends TextButton {

	public static GlyphLayout layout = new GlyphLayout();

	private Image image;
	private FitCheckButtonStyle style;

	public FitCheckButton (String text, Skin skin) {
		this(text, skin.get(FitCheckButtonStyle.class));
	}

	public FitCheckButton (String text, Skin skin, String styleName) {
		this(text, skin.get(styleName, FitCheckButtonStyle.class));
	}

	public FitCheckButton (String text, FitCheckButtonStyle style) {
		super(text, style);
		clearChildren();
		add(image = new Image(style.checkboxOff));
		Label label = getLabel();
		add(label);
		label.setAlignment(Align.left);
		setSize(getPrefWidth(), getPrefHeight());
	}

	public void setStyle (ButtonStyle style) {
		if (!(style instanceof FitCheckButtonStyle)) throw new IllegalArgumentException("style must be a FitCheckButtonStyle.");
		super.setStyle(style);
		this.style = (FitCheckButtonStyle)style;
	}

	/** Returns the checkbox's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
	 * called. */
	public FitCheckButtonStyle getStyle () {
		return style;
	}
	
	@Override
	public void sizeChanged() {
		super.sizeChanged();
		
		float buttonHeight = getHeight();
		float buttonWidth = getWidth() - buttonHeight;
		
		if ( style.up!=null ) {
			buttonWidth -= style.up.getLeftWidth();
			buttonWidth -= style.up.getRightWidth();
			buttonHeight -= style.up.getTopHeight();
			buttonHeight -= style.up.getBottomHeight();
		}
		
		// this is the size the label wants to be
//		BitmapFont.TextBounds bounds = getStyle().font.getBounds( getText() );
		layout.setText( getStyle().font, getText() );
  		
		// whats left over of the button from the icon
		float leftWidth = buttonWidth*0.9f;
		float leftHeight = buttonHeight*0.95f;
  		float ratio = Math.min( leftHeight/layout.height, leftWidth/layout.width );
  		getLabel().setFontScale( Math.max( ratio, 0.0001f ) );
	}

	public void draw (Batch batch, float parentAlpha) {
		Drawable checkbox = null;
		if (isDisabled()) {
			if (isChecked() && style.checkboxOnDisabled != null)
				checkbox = style.checkboxOnDisabled;
			else
				checkbox = style.checkboxOffDisabled;
		}
		if (checkbox == null) {
			if (isChecked() && style.checkboxOn != null)
				checkbox = style.checkboxOn;
			else if (isOver() && style.checkboxOver != null && !isDisabled())
				checkbox = style.checkboxOver;
			else
				checkbox = style.checkboxOff;
		}
		image.setDrawable(checkbox);
		
//		if ( style.buttonColor!=null && !isDisabled() )
//			setColor( style.buttonColor );
		
		super.draw(batch, parentAlpha);
	}

	public Image getImage () {
		return image;
	}
	
	/** The style for a select box, see {@link CheckBox}.
	 * @author Nathan Sweet */
	static public class FitCheckButtonStyle extends TextButtonStyle {
		public Drawable checkboxOn, checkboxOff;
		/** Optional. */
		public Drawable checkboxOver, checkboxOnDisabled, checkboxOffDisabled;
		
//		public Color buttonColor = null;

		public FitCheckButtonStyle () {
		}

		public FitCheckButtonStyle (Drawable checkboxOff, Drawable checkboxOn, BitmapFont font, Color fontColor) {
			this.checkboxOff = checkboxOff;
			this.checkboxOn = checkboxOn;
			this.font = font;
			this.fontColor = fontColor;
		}

		public FitCheckButtonStyle (CheckBoxStyle style) {
			this.checkboxOff = style.checkboxOff;
			this.checkboxOn = style.checkboxOn;
			this.checkboxOver = style.checkboxOver;
			this.checkboxOffDisabled = style.checkboxOffDisabled;
			this.checkboxOnDisabled = style.checkboxOnDisabled;
			this.font = style.font;
			this.fontColor = new Color(style.fontColor);
		}
	}
}
