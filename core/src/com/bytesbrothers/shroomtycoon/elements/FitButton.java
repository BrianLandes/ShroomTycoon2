package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class FitButton extends Button {

	public static GlyphLayout layout = new GlyphLayout();

	public Label label;
	private FitButtonStyle style;
	public Image image;
//	public Cell<Actor> imageCell;
	public TextureRegion icon;
	public static final int SPACING = 20;
	public static final float MIN_LABEL_WIDTH_AS_PERCENTAGE = 0.80f;

	public FitButton (String text, TextureRegion icon, Skin skin) {
		this(text, icon, skin.get( FitButtonStyle.class));
		setSkin(skin);
	}

	public FitButton (String text, TextureRegion icon, Skin skin, String styleName) {
		this(text, icon, skin.get(styleName, FitButtonStyle.class));
		setSkin(skin);
	}
	
	public FitButton (String text, FitButtonStyle style) {
		this( text, null, style );
	}
	
	public FitButton (String text, Skin skin) {
		this(text, null, skin.get( FitButtonStyle.class));
		setSkin(skin);
	}

	public FitButton (String text, TextureRegion icon, FitButtonStyle style) {
		super();
		setStyle(style);
		this.style = style;
		label = new Label(text, new LabelStyle( style.font, style.fontColor) );
		label.setAlignment( Align.left, Align.bottom );
		this.icon = icon;
		if ( icon!=null ) {
			image = new Image( icon );
			image.setScaling( Scaling.fillY );
			add( image ).padRight( 100 );
		}
		add( label );

		setWidth( getPrefWidth() );
		setHeight( getPrefHeight() );
	}

	public void setStyle (ButtonStyle style) {
		if (!(style instanceof FitButtonStyle)) throw new IllegalArgumentException("style must be a FitButtonStyle.");
		super.setStyle(style);
		this.style = (FitButtonStyle)style;
		if (label != null) {
			FitButtonStyle textButtonStyle = (FitButtonStyle)style;
			LabelStyle labelStyle = label.getStyle();
			labelStyle.font = textButtonStyle.font;
			labelStyle.fontColor = textButtonStyle.fontColor;
			label.setStyle(labelStyle);
		}
	}
	
	@Override
	protected void sizeChanged() {
		resizeText();
		super.sizeChanged();
	}
	
	public void resizeText() {
		// this is the size the label wants to be
//		BitmapFont.TextBounds bounds = style.font.getBounds( label.getText() );
		layout.setText(style.font,label.getText() );
  		
		// whats left over of the button from the icon
		float leftWidth = getWidth()*0.9f;
		float leftHeight = getHeight()*0.95f;

		if ( style.up!=null ) {
			leftWidth -= style.up.getLeftWidth();
			leftWidth -= style.up.getRightWidth();
			leftHeight -= style.up.getTopHeight();
			leftHeight -= style.up.getBottomHeight();
		}
		
		if ( icon!=null ) {
			if ( leftHeight/getWidth()<=1.0f - MIN_LABEL_WIDTH_AS_PERCENTAGE ) {
				getCell( image ).padRight( SPACING ).width( leftHeight ).height( leftHeight );
				leftWidth -= leftHeight;
				leftWidth -= SPACING;
			} else {
				float maxIconWidth = getWidth() * (1.0f - MIN_LABEL_WIDTH_AS_PERCENTAGE);
				getCell( image ).padRight( SPACING ).width( maxIconWidth ).height( maxIconWidth );
				leftWidth -= maxIconWidth;
				leftWidth -= SPACING;
			}
		}
		
		getCell( label ).width( leftWidth ).height( leftHeight );

  		float ratio = Math.min( leftHeight/layout.height, leftWidth/layout.width );
  		label.setFontScale( Math.max( ratio, 0.0001f ) );
	}
	
	@Override
	public void draw (Batch batch, float parentAlpha) {
		

		Color fontColor;
		if (isDisabled() && style.disabledFontColor != null)
			fontColor = style.disabledFontColor;
		else if (isPressed() && style.downFontColor != null)
			fontColor = style.downFontColor;
		else if (isChecked() && style.checkedFontColor != null)
			fontColor = (isOver() && style.checkedOverFontColor != null) ? style.checkedOverFontColor : style.checkedFontColor;
		else if (isOver() && style.overFontColor != null)
			fontColor = style.overFontColor;
		else
			fontColor = style.fontColor;
		if (fontColor != null) label.getStyle().fontColor = fontColor;
		
//		if ( style.buttonColor!=null && !isDisabled() )
//			setColor( style.buttonColor );
		
		super.draw(batch, parentAlpha);
	}

	public Label getLabel () {
		return label;
	}

//	public Cell getLabelCell () {
//		return getCell(label);
//	}

	public void setText (String text) {
		label.setText(text);
	}

	public CharSequence getText () {
		return label.getText();
	}
	
	
	static public class FitButtonStyle extends ButtonStyle {
		public BitmapFont font;
		/** Optional. */
		public Color fontColor, downFontColor, overFontColor, checkedFontColor, checkedOverFontColor, disabledFontColor;
//			buttonColor;

		public FitButtonStyle () {
//			buttonColor = null;
		}

		public FitButtonStyle (Drawable up, Drawable down, Drawable checked, BitmapFont font) {
			super(up, down, checked);
			this.font = font;
		}

		public FitButtonStyle ( FitButtonStyle style) {
			super(style);
			this.font = style.font;
			if (style.fontColor != null) this.fontColor = new Color(style.fontColor);
			if (style.downFontColor != null) this.downFontColor = new Color(style.downFontColor);
			if (style.overFontColor != null) this.overFontColor = new Color(style.overFontColor);
			if (style.checkedFontColor != null) this.checkedFontColor = new Color(style.checkedFontColor);
			if (style.checkedOverFontColor != null) this.checkedFontColor = new Color(style.checkedOverFontColor);
			if (style.disabledFontColor != null) this.disabledFontColor = new Color(style.disabledFontColor);
		}
	}
}
