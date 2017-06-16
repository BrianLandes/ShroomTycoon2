package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Ray;
import com.bytesbrothers.shroomtycoon.ScreenParts.CasingPart.FruitData;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Fruit;

public class CasingPartInput implements GestureListener {

	CasingPart screen;
	
	float first_distance = 0;
	float last_distance = 0;

	public CasingPartInput ( CasingPart screen ) {
		this.screen = screen;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean tap(float x, float y, int count, int button) {
//		System.out.println( "CasingColumnInput: tapped" );
		if ( screen.doing==CasingPart.HARVEST ) {
			if ( !screen.drawer.boxesMade )
				screen.drawer.makeFruitBoxes();
			return pickFruit( x, y );
		}
		
		return false;
	}


	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
//		float height = velocityY/(float)screen.h;
//		screen.scrollEase = -height * 0.5f;
		
		return false;
	}


	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if ( screen.doing==CasingPart.HARVEST ) {
			if ( !screen.drawer.boxesMade )
				screen.drawer.makeFruitBoxes();
			return pickFruit( x, y );
		}
		
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean zoom(float initialDistance, float distance) {

		return false;
	}


	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {

	}


	public boolean pickFruit( float x, float y ) {
		Fruit pickFruit = null;

		for ( ModelInstance fruitInstance: screen.drawer.fruitInstances ) {
			Fruit thisFruit = ((FruitData)fruitInstance.userData).fruit;
			if ( thisFruit.getFrame()>13 ) {
				if ( ((FruitData)fruitInstance.userData).bBox==null ) {
					((FruitData)fruitInstance.userData).bBox = fruitInstance.model.meshes.get(0).calculateBoundingBox()
							.mul(fruitInstance.transform);
				}
				Ray ray = screen.drawer.cam.getPickRay( x, y );

				if ( ray!= null && Intersector.intersectRayBoundsFast( ray, ((FruitData)fruitInstance.userData).bBox ) ) {
					if ( pickFruit==null || pickFruit.y<thisFruit.y )
						pickFruit = thisFruit;
				}
			}
		}
		
		if ( pickFruit!=null ) {
			if ( screen.newBatch==null ) {
				screen.newBatch = new Batch();
				screen.newBatch.strain = screen.theCasing.strain;
			}
			screen.newBatch.addFruit( pickFruit );
			screen.theCasing.fruits.remove( pickFruit );
			screen.drawer.removeFruit( pickFruit );
//			System.out.println( "CasingColumnInput: removed fruit " );
			
			return true;
		}
		return false;
	}
}