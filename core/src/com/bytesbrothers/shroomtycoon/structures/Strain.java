package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;

import java.util.ArrayList;

public class Strain {
	public String name = "";
	public String description = "";
	public int id = -1;

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
	public float potency_Var;
	public float batch_weight; // total batch weight in g's
	public float batch_weight_Var; // variance in batch weight
	
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
	public boolean costsCoins = false;
	
	public Strain() {
		
	}
	public Strain( String newname ) {
		name = newname;
	}
	public Strain( String newname, String newDesc ) {
		name = newname;
		description = newDesc;
	}
	
	public Strain( StrainSQL strain ) {
		name = strain.name;
		description = strain.d;
		
		jar_grow_time = strain.j; // num of seconds for jar to grow while incubating
		casing_grow_time = strain.c; // num of seconds for casing to incubate
		fruit_grow_time = strain.fG; // num of seconds for fruit to grow
		pins_time = strain.p; // seconds in between pinheads
		
		fruit_weight = strain.fW; // average fruit body weight
		fruit_weight_Var = strain.fWv; // variance in fruit body weight
		potency = strain.pot;
		potency_Var = strain.potV;
		batch_weight = strain.b; // total batch weight in g's
		batch_weight_Var = strain.bV; // variance in batch weight
		
		temp_required = strain.t; // temp quality need: low, medium, or high
		humidity_cost = strain.h; // units required of humidity for pinning
		substrate = strain.s; // this strains preferred substrate type
		
		fruit_width_Var = strain.wV; // variance of fruit width
		fruit_height_Var = strain.hV; // variance of fruit height
		costsCoins = strain.cC;
		
		colors = strain.clr;
		
		for ( FruitColor color: strain.cClr ) {
			colors.add( new Integer( Assets.player.customColors.size() +
					ST.CUSTOM_COLOR_INDEX_OFFSET ) );
			Assets.player.customColors.add( color );
		}
	}
	
	public float getTotalTime() {
		return batch_weight/fruit_weight * pins_time
				+ jar_grow_time + casing_grow_time + fruit_grow_time;
	}
	public float getTotalGross() {
		return batch_weight * potency;
	}
	public float getDph() {
		return getTotalGross() / ( getTotalTime()/ 60.0f/ 60.0f );
	}
	
	public int getSyringePrice() {
		float dph = getDph();
		if ( dph<40 ) {
			return 100;
		} else if ( dph<50 ) {
			return 350;
		} else if ( dph<60 ) {
			return 600;
		} else if ( dph<70 ) {
			return 1400;
		}
		return 3200;
	}
	
	public int getCoinPrice() {
		float dph = getDph();
		if ( dph<50 ) {
			return 5;
		} else if ( dph<60 ) {
			return 10;
		} else if ( dph<70 ) {
			return 15;
		} else if ( dph<80 ) {
			return 20;
		}
		return 30;
	}
	
	public int getSpeedRating() {
		return (int) ((15.0f - ( getTotalTime()/ 60.0f/ 60.0f ) )/15.0f*10.0f );
	}
	
	public int getGrossRating() {
		return (int) (getTotalGross()/500.0f * 10.0f);
	}
	
//	public float getDifficultyRating() {
//		// stars for temp_required (up to 3 )
//		float rating = (float)temp_required;
//		
//		// stars for substrate ( up to 2)
//		rating += (float)(substrate + 1) * 0.5f;
//		
//		// stars for humidity cost ( up to 2.5 )
//		rating += (float)(humidity_cost) * 0.5f;
//		
//		return rating;
//	}
}
