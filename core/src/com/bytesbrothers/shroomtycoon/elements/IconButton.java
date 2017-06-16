package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class IconButton extends Button {
	public static GlyphLayout layout = new GlyphLayout();

	private Label label;
	private IconButtonStyle style;
//	private Sprite icon = null;
	public Image icon;
	public Table iconTable;

	public IconButton (String text, Sprite icon, Skin skin) {
		this(text, icon, skin.get( IconButtonStyle.class));
		setSkin(skin);
	}

	public IconButton (String text, Sprite icon, Skin skin, String styleName) {
		this(text, icon, skin.get(styleName, IconButtonStyle.class));
		setSkin(skin);
	}

	public IconButton (String text, Sprite icon, IconButtonStyle style) {
		super();
		setStyle(style);
		this.style = style;
		label = new Label(text, new LabelStyle( style.font, style.fontColor) );
		label.setAlignment( Align.center, Align.bottom );
		iconTable = new Table();
		iconTable.setFillParent( true );
		this.icon = new Image( icon );
		this.icon.setScaling( Scaling.fit );
		iconTable.add( this.icon ).fill();

		addActor( iconTable );
		add( label );

//		add( new Image( icon ) ).width( icon.getWidth() ).height( icon.getHeight() );

		setWidth( getPrefWidth() );
		setHeight( getPrefHeight() );

//		this.icon = icon;
	}

	@Override
	public void setStyle (ButtonStyle style) {
		if (!(style instanceof IconButtonStyle)) throw new IllegalArgumentException("style must be a IconButtonStyle.");
		super.setStyle(style);
		this.style = (IconButtonStyle)style;
		if (label != null) {
			IconButtonStyle textButtonStyle = (IconButtonStyle)style;
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
		float leftHeight = getHeight()*0.9f;

		if ( style.up!=null ) {
			leftWidth -= style.up.getLeftWidth();
			leftWidth -= style.up.getRightWidth();
			leftHeight -= style.up.getTopHeight();
			leftHeight -= style.up.getBottomHeight();
		}
		
//				if ( icon!=null ) {
//					getCell( image ).padRight( spacing ).width( leftHeight ).height( leftHeight );
//					leftWidth -= leftHeight;
//					leftWidth -= spacing;
//				}
		
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
		
		if ( style.buttonColor!=null && !isDisabled() )
			setColor( style.buttonColor );
		
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
	
	
	static public class IconButtonStyle extends ButtonStyle {
		public BitmapFont font;
		/** Optional. */
		public Color fontColor, downFontColor, overFontColor, checkedFontColor, checkedOverFontColor, disabledFontColor,
			buttonColor;

		public IconButtonStyle () {
			buttonColor = null;
		}

		public IconButtonStyle (Drawable up, Drawable down, Drawable checked, BitmapFont font) {
			super(up, down, checked);
			this.font = font;
		}

		public IconButtonStyle ( IconButtonStyle style) {
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
