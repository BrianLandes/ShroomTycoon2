package com.bytesbrothers.shroomtycoon.elements;

import com.bytesbrothers.shroomtycoon.ST;

public class Alert {
	public String title = "";
	public int uniqueID = 1001;
	public String message = ""; // displayed when projects is completed
	public String icon = "";
	public int type = ST.TABLE;
//	public String partName = "";
	public Class<? extends ScreenPart> nextPart;
	public Object partObject = null;
	public int mainColumn = -1;
	public String dialog_title = ""; // character name if speech dialog
	public String dialog_text = "";
	public int timer = -1;
	public boolean show = true;
//	public Project side_job = null;
	public Object origin = null;
	
	public Alert () {

	}
	
	public void reset() {
		title = "";
		uniqueID = 1001;
		message = "";
		icon = "";
		type = ST.TABLE;
		nextPart = null;
		partObject = null;
		mainColumn = -1;
		dialog_title = "";
		dialog_text = "";
		timer = -1;
//		show = true;
//		side_job = null;
		origin = null;
	}
	
	/** updated to stay relevant */
	public void updateOneSec() {
		if ( timer!=-1  ) {
			timer --;
			if ( timer==0 )
				show = false;
		}
	}
}
