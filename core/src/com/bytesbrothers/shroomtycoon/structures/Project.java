package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.elements.Progressable;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;

import java.util.ArrayList;
import java.util.Iterator;

import aurelienribon.tweenengine.TweenAccessor;

public class Project extends Progressable {
	public String name = "";
	public String message = ""; // displayed when project is completed
	public String icon;
	public ArrayList<ProjectEvent> events = new ArrayList<ProjectEvent>();
	public ScreenPart nextPart = null;
	public Object nextPartObject = null;
	public int mainColumn = -1;
	
	public float timer = 0.0f;
	public int completionTime = ST.PROJECT_TIME;

	public Project( String name, String icon ) {
		this.name = name;
		this.icon = icon;
	}
	
	/** returns between 0.0f and 1.0f depending on completion out of 100 */
	@Override
	public float getProgress() {
		return Math.min( timer/(float)completionTime, 1.0f );
	}
	
	public String getCenterText() {
		return "%" + ((int)(getProgress()*100.0f)) + "." + ((int)(getProgress()*1000.0f%10) );
	}
	
	public String getRightText() {
		int timeLeft = completionTime - (int)timer;
		if ( timeLeft>0 ) {
			int minutes = (int) ((float)timeLeft/60.0f);
			int hours = (int) ((float)minutes/60.0f);
			minutes = minutes%60;
			int seconds = timeLeft - hours*60*60 - minutes * 60;
			return "" + ( hours>0? (hours + "h "): "" ) + ( minutes>0? (minutes + "m"): "" ) +  seconds + "s";
		}
		return "";
	}
	
	public void update( float delta ) {
		timer += delta;
	}

	public void complete() {
		timer = completionTime;
		Iterator<ProjectEvent> iterator = events.iterator();
		while ( iterator.hasNext() ) {
			ProjectEvent event = iterator.next();
			event.run();
		}
	}
	public void addEvent( ProjectEvent event ) {
		events.add( event );
	}
	
	public interface ProjectEvent {

		abstract public void run();
	}
	
	static public class ProjectTween implements TweenAccessor<Project> {
		public static final int PROGRESS = 1;

		@Override
		public int getValues( Project target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case PROGRESS:
				returnValues[0] = target.timer;
				return 1;

			default:
				assert false;
				return 0;
			}
		}

		@Override
		public void setValues( Project target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case PROGRESS:
				target.timer = newValues[0];
				break;

			default: assert false; break;
			}
		}

	}

}
