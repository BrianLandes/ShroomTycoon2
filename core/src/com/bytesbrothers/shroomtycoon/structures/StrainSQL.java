package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.ST;

import java.util.ArrayList;

public class StrainSQL {
	public String name = "";
	public String d = "";
//	public int id = -9;

	/*
	 * Speed Qualities
	 */
	public int j; // num of seconds for jar to grow while incubating
	public int c; // num of seconds for casing to incubate
	public int fG; // num of seconds for fruit to grow
	public int p; // seconds in between pinheads
	
	/*
	 * Gross Income Qualities
	 */
	public float fW; // average fruit body weight
	public float fWv; // variance in fruit body weight
	public float pot;
	public float potV;
	public float b; // total batch weight in g's
	public float bV; // variance in batch weight
	
	/*
	 * Difficulty Qualities
	 */
	public int t; // temp quality need: low, medium, or high
	public int h; // units required of humidity for pinning
	public int s; // this strains preferred substrate type
	
	/*
	 * Other
	 */
	public ArrayList<Integer> clr = new ArrayList<Integer>();
	public float wV; // variance of fruit width
	public float hV; // variance of fruit height
	public boolean cC = false;
	
	public ArrayList<FruitColor> cClr = new ArrayList<FruitColor>();
	
	public StrainSQL() {
		
	}
	public StrainSQL( Strain strain ) {
		name = strain.name;
		d = strain.description;
		
		j = strain.jar_grow_time; // num of seconds for jar to grow while incubating
		c = strain.casing_grow_time; // num of seconds for casing to incubate
		fG = strain.fruit_grow_time; // num of seconds for fruit to grow
		p = strain.pins_time; // seconds in between pinheads
		
		fW = strain.fruit_weight; // average fruit body weight
		fWv = strain.fruit_weight_Var; // variance in fruit body weight
		pot = strain.potency;
		potV = strain.potency_Var;
		b = strain.batch_weight; // total batch weight in g's
		bV = strain.batch_weight_Var; // variance in batch weight
		
		t = strain.temp_required; // temp quality need: low, medium, or high
		h = strain.humidity_cost; // units required of humidity for pinning
		s = strain.substrate; // this strains preferred substrate type
		
		wV = strain.fruit_width_Var; // variance of fruit width
		hV = strain.fruit_height_Var; // variance of fruit height
		cC = strain.costsCoins;
		
		for ( Integer color: strain.colors ) {
			if ( color>= ST.CUSTOM_COLOR_INDEX_OFFSET ) {
				cClr.add( StrainMaster.getColor( color ) );
			} else {
				clr.add( color );
			}
		}
	}

}
