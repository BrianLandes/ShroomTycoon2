package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ProgressBar extends Actor {

	public GlyphLayout layout = new GlyphLayout();

	public Progressable item = null;

	ProgressBarStyle style;
	private BitmapFontCache cache;
	private BitmapFontCache rightCache;
	
	public Color barColor = null;
	public boolean useEasing = true;
	public Ease ease = new Ease();
	public float progress = 0.0f;
	public float easeTime = 1.0f;
	public float startProgress = 0.0f;
	public float lastProgress = 0.0f;
	public float easedProgress = 0.0f;
	public float deltaProgress = 0.0f;
	public long startTime = 0;
	
	public Table table = null;
	
	public final static float CENTER_PERCENT = 0.4f;
	public final static float RIGHT_PERCENT = 0.3f;
	
	/** usually for progress */
	public String centerText = "";
	/** usually for time left */
	public String rightText = "";
	
	public ProgressBar ( Progressable progressable, Skin skin) {
		this( progressable, skin.get( ProgressBarStyle.class));
	}
	public ProgressBar ( Progressable progressable, ProgressBarStyle style) {
		super();
		setStyle(style);

		item = progressable;
		
		setWidth( Math.max( style.background.getMinWidth(), style.bar.getMinWidth() ) );
		setHeight( Math.max( style.background.getMinHeight(), style.bar.getMinHeight() ) );
	}

	public void setStyle (ProgressBarStyle style) {
		if (!(style instanceof ProgressBarStyle)) throw new IllegalArgumentException("style must be a ProgressBarStyle.");
		this.style = (ProgressBarStyle)style;
		cache = new BitmapFontCache(style.font, style.font.usesIntegerPositions());
		rightCache = new BitmapFontCache( style.rightFont!=null? style.rightFont:style.font,
				(style.rightFont!=null? style.rightFont:style.font).usesIntegerPositions());
	}
	
	@Override
	public void act( float delta ) {
		super.act( delta );
		
		if ( item!=null ) {
			item.act( delta );
			progress = item.getProgress();
			if ( useEasing ) {
				// start a new tween
				if ( lastProgress!=progress ) {
					long time = TimeUtils.millis();
					if ( time - startTime < 100 )
						easedProgress = progress;
					startTime = time;
//					if ( startProgress==easedProgress )
//						easedProgress = Ease.easeInOutSine( TimeUtils.millis() - startTime, startProgress, deltaProgress, easeTime * 1000 );
					startProgress = easedProgress;
					lastProgress = progress;
					deltaProgress = progress - startProgress;
				}
				long time = TimeUtils.millis() - startTime;
				if ( time<easeTime*1000 )
					easedProgress = Ease.ease( Ease.OUT_EXPO, time, startProgress, deltaProgress, easeTime * 1000 );
				else
					easedProgress = progress;
			} else {
				easedProgress = item.getProgress();
			}
		
			centerText = item.getCenterText();
			rightText = item.getRightText();
		}
	}
	
	public Table table() {
		table = new Table();
		table.add( this );
		return table;
	}
	
	@Override
	public void setSize( float width, float height ) {
		super.setSize( width, height );
		if ( table!=null ) {
			table.getCell( this ).width( width ).height( height );
			table.invalidate();
		} else
			System.out.println( "ProgressBar: setSize table was null" );
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void draw( Batch batch, float parentAlpha) {
		super.draw( batch, parentAlpha);
		Drawable background = style.background;

		Color color = getColor();
		
		if (background != null) {
			batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			background.draw(batch, getX(), getY(), getWidth(), getHeight());
		}
		
		Drawable bar = style.bar;

		if ( bar != null && easedProgress>0.0f ) {
			color = getColor();
			if ( barColor!=null )
				batch.setColor( barColor.r,  barColor.g,  barColor.b,  barColor.a * parentAlpha);
			else
				batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
			
				
			bar.draw(batch, getX() + background.getLeftWidth(), getY() + background.getBottomHeight(),
					(getWidth() - (background.getLeftWidth() + background.getRightWidth())) * easedProgress,
					getHeight() - (background.getTopHeight() + background.getBottomHeight()) );
		}

  		if ( centerText.length()>0 ) {
  			// this is the size the label wants to be
//  			BitmapFont.TextBounds centerBounds = style.font.getBounds( centerText );
  			
  			float leftWidth = getWidth()*CENTER_PERCENT ;
  			float leftHeight = getHeight()*0.5f;

			layout.setText(cache.getFont(),centerText);

  	  		float ratio = Math.max( Math.min( leftHeight/layout.height, leftWidth/layout.width ), 0.001f );
  			
//	  		style.font.getData().setScale( ratio );
	  		
//	  		cache.setMultiLineText( centerText, 0, 0, centerBounds.width, HAlignment.LEFT );


			cache.setColor(style.fontColor == null ? color : color.mul(style.fontColor));
			cache.setPosition( getX() + getWidth()*0.5f - layout.width*ratio*0.5f, getY() + getHeight() *0.5f + layout.height*ratio*0.5f );
			cache.draw(batch, color.a * parentAlpha);
		}
		
//		style.font.setScale( 1.0f );
		
		if ( rightText.length()>0 ) {
			// this is the size the label wants to be
//			BitmapFont.TextBounds rightBounds = style.font.getBounds( rightText );

			BitmapFont font = style.font;
			if ( style.rightFont!=null )
				font = style.rightFont;
			layout.setText(font,rightText);
			// whats left over of the button from the icon
			float leftWidth = getWidth()*0.25f;
			float leftHeight = getHeight()*0.5f;
	  		
			float ratio = Math.max( Math.min( leftHeight/layout.height, leftWidth/layout.width ), 0.001f );
  			
//			font.getData().setScale( ratio );
	  		
//			rightCache.setMultiLineText( rightText, 0, 0, rightBounds.width, HAlignment.LEFT );
			Color rightColor = new Color( color );
			if ( style.rightFontColor!=null )
				rightColor.mul(style.rightFontColor );
			else if ( style.fontColor!=null )
				rightColor.mul(style.fontColor );
			rightCache.setColor( rightColor );
			rightCache.setPosition( getX() + getWidth() - layout.width*ratio - background.getRightWidth(),
					getY() + getHeight() *0.5f + layout.height*ratio*0.5f );
			rightCache.draw(batch, color.a * parentAlpha);
			
//			font.getData().setScale( 1.0f );
		}

	}

	static public class ProgressBarStyle {
		public Drawable background;
		public Drawable bar;
		public BitmapFont font;
		/** Optional. */
		public Color fontColor;
		public BitmapFont rightFont;
		public Color rightFontColor;

		public ProgressBarStyle () {
		}

		public ProgressBarStyle (Drawable up, Drawable down, Drawable checked, BitmapFont font) {
			this.font = font;
		}

		public ProgressBarStyle ( ProgressBarStyle style) {
			this.font = style.font;
			if (style.fontColor != null) this.fontColor = new Color(style.fontColor);
		}
	}
}
