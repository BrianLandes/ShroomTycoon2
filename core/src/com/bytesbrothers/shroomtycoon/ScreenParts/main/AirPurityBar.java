package com.bytesbrothers.shroomtycoon.ScreenParts.main;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.elements.Progressable;

import java.util.Random;

public class AirPurityBar extends Progressable {
	
	private float fluct_time = 0;// frames until fluctuates again
	private int fluct_degrees = 0;// between -3 and 1 randomly
	
	@Override
	public void act( float delta ) {
		fluct_time -= delta;
		if ( fluct_time<0 ) {
			Random random = new Random();
			fluct_time = random.nextFloat() * 1.0f + 0.1f;
			fluct_degrees = random.nextInt( 4 ) - 3;
		}
	}
	
	@Override
	public float getProgress() {
		if ( Assets.player!=null ) {
			float progress = Assets.player.getGbSterility() + ((float)fluct_degrees/100.0f) - 0.1f;
			if ( progress>1.0f )
				progress = 1.0f;
			return progress;
		}
		return 0.0f;
	}
	
	public String getCenterText() {
		return "Current Air Purity Rating";
		
	}
	
	public String getRightText() {
		float progress = getProgress();
		return "%" + ((int)(progress*100.0f));
//				+ "." + ((int)(progress*1000.0f%10) );
	}
}
