package com.bytesbrothers.shroomtycoon.pools;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.elements.FloatingText;

import easypool.ObjectPool;

public class FloatingTextPool extends ObjectPool<FloatingText>{

	public FloatingTextPool() {
		super( 2 );
	}

	@Override
	protected FloatingText createObject() {
		return new FloatingText( "", Assets.getSkin() );
	}

}
