package com.bytesbrothers.shroomtycoon.pools;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;

import easypool.ObjectPool;

public class ModelPool extends ObjectPool<Model> {

	public ModelPool( ) {
		super( 4 );
	}

	@Override
	protected Model createObject() {
		Assets.modelBuilder.begin();
		Assets.modelBuilder.part("1", ST.create3dSquare( null, null, null, null ),   GL20.GL_TRIANGLES,
	    		new Material("1", new BlendingAttribute( ) ) );

	    return Assets.modelBuilder.end();
	}

	/**
     * Shutdown this pool.
     */
	@Override
    public void shutdown() {
		super.shutdown();
		for ( Model model: getPool() ) {
			model.dispose();
		}
    }
}
