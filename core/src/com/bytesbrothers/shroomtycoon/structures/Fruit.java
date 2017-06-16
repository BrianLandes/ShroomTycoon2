package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.ST;

import java.util.Random;

public class Fruit {
	public int growth = 0;
	public int max_growth = 0;
	public float weight = 0.5f;
	
	public float heightvar = 1.0f;
	public float widthvar = 1.0f;
	
	public int x = 0;
	public float y = 0;
	public int color = 1;
	public int type = 1;
	public boolean flipped = false;
	
//	public boolean customColor = false;
	public FruitColor fruitColor = null;

	public Fruit() {
		
	}
	public Fruit( Strain strain ) {
		this( new StrainInstance( strain ) );
	}
	
	public Fruit( StrainInstance strain ) {
		Random random = new Random();
		
		max_growth = strain.fruit_grow_time;
		weight = strain.fruit_weight + ((random.nextFloat() * 2.0f) - 1.0f ) * strain.fruit_weight_Var;
		widthvar = 1.0f + ((random.nextFloat() * 2.0f) - 1.0f ) * strain.fruit_width_Var;
		heightvar = 1.0f + ((random.nextFloat() * 2.0f) - 1.0f ) * strain.fruit_height_Var;
		
		type = random.nextInt( ST.FRUIT_NAMES.length );
		flipped = random.nextBoolean();
		x = random.nextInt( 99 ) + 1;
		y = random.nextInt( 99 ) + 1;
		
//		if ( !strain.customColors.isEmpty() ) {
//			// possibly use a custom color
//			int chance = random.nextInt( strain.colors.size() + strain.customColors.size() );
//			if ( chance<strain.colors.size() ) {
//				color = strain.colors.get( chance );
//			} else {
//				fruitColor = strain.customColors.get( chance-strain.colors.size() );
//				customColor = true;
//			}
//			
//		} else {
			color = strain.colors.get( random.nextInt( strain.colors.size() ) );
//			customColor = false;
//		}
		
		
		type = random.nextInt( 4 );
	}
	
	/**
	 * returns between 1 and 16 (inclusive) depending on growth factor
	 */
	public int getFrame() {
		return (int) (((float)growth)/((float)max_growth) * 15.0f) + 1;
	}
	
	public void updateOneSec() {
		growth += 1;
		if ( growth>=max_growth )
			growth = max_growth;
	}
	
	public void updateBy( int secs ) {
		growth += secs;
		if ( growth>=max_growth )
			growth = max_growth;
	}
	
	/**
	 * returns between 0.0f and 1.0f
	 */
	public float getProgress() {
		return (float)growth/(float)max_growth;
	}
}
