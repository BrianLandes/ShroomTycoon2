package com.bytesbrothers.shroomtycoon.ScreenParts.main;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.elements.Progressable;

public class RemainingDebt extends Progressable {
	
	int lastDebt = -1;
	String lastRight =  "";
	
	@Override
	public float getProgress() {
		return Math.min( 1.0f, 1.0f - (float) Assets.player.debt/(float) ST.SAMS_LOAN );
	}
	
	@Override
	public String getCenterText() {
		return "Debt";
	}
	
	@Override
	public String getRightText() {
		int newDebt = Assets.player.debt;
		if ( lastDebt==newDebt) 
			return lastRight;
		lastRight = "Left: $" + ST.withComasAndTwoDecimals( newDebt );
		lastDebt = newDebt;
		return lastRight;
	}
}
