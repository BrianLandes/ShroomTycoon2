package com.bytesbrothers.shroomtycoon.ScreenParts.main;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.elements.Progressable;

public class FilterLeftBar extends Progressable {
	@Override
	public float getProgress() {
		if ( Assets.player.permanent_sterile )
			return 1.0f;
		return (float)Assets.player.gb_sterile_secs/(float)Assets.player.last_gb_sterile_secs;
	}

	@Override
	public String getCenterText() {
		if ( Assets.player.permanent_sterile )
			return "Permanently Sterile";
		if ( Assets.player.gb_sterile_secs <= 0 )
			return "Replace Air Filter";
		return "Filter Good for";
	}
	
	@Override
	public String getRightText() {
		int timeLeft = Assets.player.gb_sterile_secs;
		if ( timeLeft>0 ) {
			int minutes = (int) ((float)timeLeft/60.0f);
			int hours = (int) ((float)minutes/60.0f);
			minutes = minutes%60;
			return "" + ( hours>0? (hours + "h "): "" ) + ( (minutes==0 && hours<1)? "<1m": (minutes + "m") );
		}
		return "";
	}

}
