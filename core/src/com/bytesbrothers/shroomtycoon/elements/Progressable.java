package com.bytesbrothers.shroomtycoon.elements;

public abstract class Progressable {
	
//	public boolean useProgress();
	public abstract float getProgress();
	
	public void act( float delta ) { };
//	public boolean useTimeLeft();
//	public int getTimeLeft();
	
	public String getCenterText() {
		float progress = getProgress();
		return "%" + ((int)(progress*100.0f)) + "." + ((int)(progress*1000.0f%10) );
	}
	
	public String getRightText() {
		return "";
	}
}
