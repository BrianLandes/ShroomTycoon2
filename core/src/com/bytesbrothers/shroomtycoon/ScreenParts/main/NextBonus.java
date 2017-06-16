package com.bytesbrothers.shroomtycoon.ScreenParts.main;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.elements.Progressable;

public class NextBonus extends Progressable {
	@Override
	public float getProgress() {
		return Math.min( 1.0f - (float) Assets.player.samBonusTimer/(float) ST.SAMS_BONUS_TIME, 1.0f);
	}
	
	@Override
	public String getCenterText() {
		return "Next Bonus";
	}
	
	@Override
	public String getRightText() {
		int timeLeft = Assets.player.samBonusTimer;
		if ( timeLeft>0 ) {
			int minutes = (int) ((float)timeLeft/60.0f);
			int hours = (int) ((float)minutes/60.0f);
			minutes = minutes%60;
//			int seconds = timeLeft - hours*60*60 - minutes * 60;
			return "" + ( hours>0? (hours + "h "): "" ) + ( (minutes==0 && hours<1)? "<1m": (minutes + "m") );
		}
		return "is Ready!";
	}
}