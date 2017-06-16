package com.bytesbrothers.shroomtycoon.structures;

import java.util.ArrayList;
import java.util.Random;


// class for specific jar/casing strain use
public class StrainInstance {
	public String name = "";
//	String description = "";

	/*
	 * Speed Qualities
	 */
	public int jar_grow_time; // num of seconds for jar to grow while incubating
	public int casing_grow_time; // num of seconds for casing to incubate
	public int fruit_grow_time; // num of seconds for fruit to grow
	public int pins_time; // seconds in between pinheads
	
	/*
	 * Gross Income Qualities
	 */
	public float fruit_weight; // average fruit body weight
		public float fruit_weight_Var; // variance in fruit body weight
	public float potency;
//		float potency_Var;
	public float batch_weight; // total batch weight in g's
//		float batch_weight_Var; // variance in batch weight
	
	/*
	 * Difficulty Qualities
	 */
	public int temp_required; // temp quality need: low, medium, or high
	public int humidity_cost; // units required of humidity for pinning
	public int substrate; // this strains preferred substrate type
	
	/*
	 * Other
	 */
	public ArrayList<Integer> colors = new ArrayList<Integer>();
	public float fruit_width_Var; // variance of fruit width
	public float fruit_height_Var; // variance of fruit height
	
//	public ArrayList<FruitColor> customColors = new ArrayList<FruitColor>();
	
	public StrainInstance( Strain strain ) {
		Random random = new Random();
		
		name = strain.name;
		jar_grow_time = strain.jar_grow_time;
		casing_grow_time = strain.casing_grow_time;
		fruit_grow_time = strain.fruit_grow_time;
		pins_time = strain.pins_time;
		fruit_weight = strain.fruit_weight;
		fruit_weight_Var = strain.fruit_weight_Var;
		potency = strain.potency + ((random.nextFloat() * 2.0f) - 1.0f ) * strain.potency_Var;
		batch_weight = strain.batch_weight + ((random.nextFloat() * 2.0f) - 1.0f ) * strain.batch_weight_Var;
		temp_required = strain.temp_required;
		humidity_cost = strain.humidity_cost;
		substrate = strain.substrate;
		colors = strain.colors;
		fruit_width_Var = strain.fruit_width_Var;
		fruit_height_Var = strain.fruit_height_Var;
	}
	
//	public StrainInstance( StrainSQL strain ) {
//		Random random = new Random();
//		
//		name = strain.name;
//		jar_grow_time = strain.jar_grow_time;
//		casing_grow_time = strain.casing_grow_time;
//		fruit_grow_time = strain.fruit_grow_time;
//		pins_time = strain.pins_time;
//		fruit_weight = strain.fruit_weight;
//		fruit_weight_Var = strain.fruit_weight_Var;
//		potency = strain.potency + ((random.nextFloat() * 2.0f) - 1.0f ) * strain.potency_Var;
//		batch_weight = strain.batch_weight + ((random.nextFloat() * 2.0f) - 1.0f ) * strain.batch_weight_Var;
//		temp_required = strain.temp_required;
//		humidity_cost = strain.humidity_cost;
//		substrate = strain.substrate;
//		colors = strain.colors;
//		customColors = strain.customColors;
//		fruit_width_Var = strain.fruit_width_Var;
//		fruit_height_Var = strain.fruit_height_Var;
//	}

	public float getTotalTime() {
		return batch_weight/fruit_weight * pins_time
				+ jar_grow_time + casing_grow_time + fruit_grow_time;
	}
	public float getTotalGross() {
		return batch_weight * potency / 100.0f;
	}
	public float getDph() {
		return getTotalGross() / ( getTotalTime()/ 60.0f/ 60.0f );
	}
}
