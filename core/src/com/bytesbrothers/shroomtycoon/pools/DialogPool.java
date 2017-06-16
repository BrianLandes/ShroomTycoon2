package com.bytesbrothers.shroomtycoon.pools;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.bytesbrothers.shroomtycoon.Assets;

import easypool.ObjectPool;

public class DialogPool extends ObjectPool<Dialog>{

	public DialogPool() {
		super( 2 );
	}

	@Override
	protected Dialog createObject() {
		return new Dialog( "", Assets.getSkin() );
	}

}
