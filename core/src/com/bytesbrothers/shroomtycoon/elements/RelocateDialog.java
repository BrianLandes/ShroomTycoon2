package com.bytesbrothers.shroomtycoon.elements;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.bytesbrothers.shroomtycoon.Assets;

public class RelocateDialog extends Dialog {

	Skin skin;

	Sprite jarOrCasingSprite;
	
	float timeLeft = 2.3f;
	
	public RelocateDialog( boolean isJar, int from, int to  ) {
		super( "", Assets.getSkin().get( "Help", WindowStyle.class) );
		
		skin = Assets.getSkin();
//		ShroomClass game = Assets.game;
		
		float w = Assets.width;
		float h = Assets.height;
		
		float blockWidth = (w * 0.9f)/3.0f;
		if ( blockWidth > h*0.8f )
			blockWidth = h*0.8f;
		
		final Sprite[] backgrounds = { skin.getSprite( "Incubator" ), skin.getSprite( "FruitingChamber" ), skin.getSprite( "Fridge" )};

		Image fromImage = new Image( backgrounds[from] );
		fromImage.setScaling( Scaling.fit );
		getContentTable().add( fromImage ).width( blockWidth ).height( blockWidth );
		
		Image relocateImage = new Image( skin.getSprite( "RelocateArrow" ) );
		relocateImage.setScaling( Scaling.fit );
		getContentTable().add( relocateImage ).width( blockWidth ).height( blockWidth );
		
		Image toImage = new Image( backgrounds[to] );
		toImage.setScaling( Scaling.fit );
		getContentTable().add( toImage ).width( blockWidth ).height( blockWidth );
		
		// cool animation
		jarOrCasingSprite = isJar? skin.getSprite( "RelocateJar" ): skin.getSprite( "RelocateCasing" ) ;
		float spriteWidth = blockWidth;
		float spriteHeight = blockWidth * ( jarOrCasingSprite.getHeight()/jarOrCasingSprite.getWidth() );
		
		jarOrCasingSprite.setBounds( w * 0.3f - spriteWidth*0.5f, h * 0.5f - spriteHeight*0.5f, spriteWidth, spriteHeight );
		jarOrCasingSprite.setAlpha( 0.0f );
		
		Tween.to( jarOrCasingSprite, SpriteTween.ALPHA, 0.6f )
        .target( 1 )
        .ease(TweenEquations.easeOutQuad)
        .delay( 0.2f )
//        .target( 0 )
//        .ease(TweenEquations.easeOutQuad)
        .start( Assets.getTweenManager() ); //** start it
		
		Tween.to( jarOrCasingSprite, SpriteTween.ALPHA, 0.5f )
        .target( 0 )
        .ease(TweenEquations.easeOutQuad)
        .delay( 2.2f )
//        .target( 0 )
//        .ease(TweenEquations.easeOutQuad)
        .start( Assets.getTweenManager() ); //** start it
		
		Tween.to( jarOrCasingSprite, SpriteTween.X, 1.6f )
        .target( w * 0.7f - spriteWidth*0.5f )
        .ease(TweenEquations.easeOutQuad)
        .delay( 0.2f )
//        .target( 0 )
//        .ease(TweenEquations.easeOutQuad)
        .start( Assets.getTweenManager() ); //** start it
	}
	
	@Override
	public void act ( float delta ) {
		super.act( delta );
		if ( timeLeft!= -80.085f ) {
			timeLeft -= delta;
			if ( timeLeft<=0 ) {
				hide();
				timeLeft = -80.085f;
			}
		}
	}

	@Override
	public void draw( Batch batch, float parentAlpha ) {
		super.draw( batch, parentAlpha);
		
		jarOrCasingSprite.draw( batch, parentAlpha );
	}
}
