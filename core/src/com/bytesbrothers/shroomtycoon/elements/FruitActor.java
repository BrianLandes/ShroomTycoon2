package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.structures.FruitColor;

public class FruitActor extends Actor {
	
	public FruitColor fruitColor;
	
	public TextureAtlas fruits;
	
	public int type;
	public int frame;
	public boolean flipped;
	
	public Sprite[] layer = { null, null, null, null };
	
	public FruitActor( int type, int frame, boolean flipped, FruitColor fruitColor, TextureAtlas fruits ) {
		this.fruitColor = fruitColor;
		this.fruits = fruits;
		this.type = type;
		this.frame = frame;
		this.flipped = flipped;
		
		layer[0] = new Sprite( fruits.findRegion( ST.FRUIT_NAMES[ type ] + "-" + frame ) );
		layer[0].setFlip( flipped, false );
		if ( fruitColor.color[0]!=null )
			layer[0].setColor( fruitColor.color[0] );
		layer[1] = new Sprite( fruits.findRegion( ST.FRUIT_NAMES[ type ] + "Top-" + frame ) );
		layer[1].setFlip( flipped, false );
		if ( fruitColor.color[1]!=null ) {
			layer[1].setColor( fruitColor.color[1] );
		} else
			layer[1].setColor( 1.0f, 1.0f, 1.0f, 1.0f );
		
		if ( fruitColor.usesTopOver && fruitColor.color[2]!=null ) {
			layer[2] = new Sprite( fruits.findRegion( ST.FRUIT_NAMES[ type ] + "TopOver-" + frame ) );
			layer[2].setFlip( flipped, false );
			layer[2].setColor( fruitColor.color[2] );
		} 
//		else
//			layer[2].setColor( 1.0f, 1.0f, 1.0f, 1.0f );
		
		
		if ( fruitColor.usesSpots && fruitColor.color[3]!=null ) {
			layer[3] = new Sprite( fruits.findRegion( ST.FRUIT_NAMES[ type ] + "Spots-" + frame ) );
			layer[3].setFlip( flipped, false );
			layer[3].setColor( fruitColor.color[3] );
		} 
//		else
//			layer[3].setColor( 1.0f, 1.0f, 1.0f, 1.0f );
		
		setTouchable( Touchable.disabled );
	}
	
	
	@Override
	public void draw( Batch batch, float parentAlpha ) {
		super.draw( batch, parentAlpha );
		
		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();
		float rotation = getRotation();
		
		for ( int i = 0; i < 4; i ++ ) {
			if ( layer[i]!=null ) {
				layer[i].setBounds( x, y, width, height );
				layer[i].setOrigin( width*0.5f, height*0.5f );
				layer[i].setRotation( rotation );
				layer[i].draw( batch );
			}
		}
		
	}
}
