package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.PressureCookersPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Progressable;
import com.bytesbrothers.shroomtycoon.elements.TextAndSeconds;

import aurelienribon.tweenengine.TweenAccessor;

public class PressureCooker extends Progressable {
	/** capacity level, for num of jars use ST.PC_CAPACITIES[] */
	public int capacity = 0;
	
	/** 0 to PC_TIME */
	public int progress = 0;
	
	/** doing: -1 nothing/empty, 0-3 substrate, 4 grain */
	public int doing = -1;
	
//	public transient ShroomClass game = null;
	
//	public PressureCooker() {
//		
//	}
	public PressureCooker( ShroomClass game ) {
//		this.game = game;
	}
	
	/** updates this shit by one second. What did you expect?
	 * this one returns true if it completes */
	public boolean updateOneSec() {
		if ( doing!= -1 ) {
			progress += 1;
			if ( progress>= ST.PC_TIME ) {
				progress = ST.PC_TIME;
				String text = doing==4? "Grain": ST.substrates[ doing ];
				
				if ( !Assets.game.screenHandler.activePart.getClass().equals( PressureCookersPart.class ) ) {
						Alert alert = Assets.alertPool.borrowObject();
						alert.reset();
						alert.uniqueID = ST.ALERT_PRESSURE_COOKER;
						alert.icon = "CookIcon";
						alert.title = "Cook Complete";
						alert.message = "Your " + text + " has finished cooking";
						alert.type = ST.TABLE;
						alert.nextPart = PressureCookersPart.class;
						Assets.addAlert( alert );
				}
				
				Assets.player.totalCooks += 1;
				if ( doing==4 ) {
					Assets.player.grain_jars += ST.PC_CAPACITIES[capacity];
				} else {
					Assets.player.sub_jars[ doing ] += ST.PC_CAPACITIES[capacity];
				}
				doing = -1;
				progress = 0;
				return true;
			}
		}
		return false;
	}
	
	/** updates this shit by secs. */
	public void updateBy( int secs ) {
		if ( doing!= -1 ) {
			progress += secs;
			if ( progress>=ST.PC_TIME ) {
				progress = ST.PC_TIME;
				String text = doing==4? "Grain": ST.substrates[ doing ];
				
//				if ( !Assets.game.screenHandler.getActivePartHeader().equals( "Pressure Cookers" ) ) {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.uniqueID = ST.ALERT_PRESSURE_COOKER;
					alert.icon = "CookIcon";
					alert.title = "Cook Complete";
					alert.message = "Your " + text + " has finished cooking";
					alert.type = ST.TABLE;
					alert.nextPart = PressureCookersPart.class;
					Assets.addAlert( alert );
//				}
				
				Assets.player.totalCooks += 1;
				if ( doing==4 ) {
					Assets.player.grain_jars += ST.PC_CAPACITIES[capacity];
				} else {
					Assets.player.sub_jars[ doing ] += ST.PC_CAPACITIES[capacity];
				}
				doing = -1;
				progress = 0;
			}
		}
	}
	
	public TextAndSeconds getAlarm() {
		if ( doing!= -1 )
			return new TextAndSeconds( ST.PC_TIME - progress, "Your pressure cooker has finished!" );
		return null;
	}
	
	@Override
	public float getProgress() {
		return (float)progress/(float)ST.PC_TIME;
	}
	
	/**
	 * returns (in seconds) the amount of time until done
	 */
	public int getTimeLeft() {
		if ( doing==-1 )
			return -1;
		return ST.PC_TIME - progress;
	}
	
	static public class PressureCookerTween implements TweenAccessor<PressureCooker> {
		public static final int PROGRESS = 1;

		@Override
		public int getValues(PressureCooker target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case PROGRESS:
				returnValues[0] = target.progress;
				return 1;
			default:
				assert false;
				return 0;
			}
		}

		@Override
		public void setValues( PressureCooker target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case PROGRESS: target.progress = (int) newValues[0]; break;

			default: assert false; break;
			}
		}

	}
	
	@Override
	public String getRightText() {
		int timeLeft = getTimeLeft();
		if ( timeLeft>0 ) {
			int minutes = (int) ((float)timeLeft/60.0f);
			int hours = (int) ((float)minutes/60.0f);
			minutes = minutes%60;
//			int seconds = timeLeft - hours*60*60 - minutes * 60;
			return "" + ( hours>0? (hours + "h "): "" ) + ( (minutes==0 && hours<1)? "<1m": (minutes + "m") );
		}
		return "";
	}
	
	public String getCenterText() {
		
		switch( doing ) {
		case -1:
			return "Ready";
		case 4:
			return "Cooking: Grain";
		case 0:
		case 1:
		case 2:
		case 3:
			return "Cooking: " + ST.substrates[doing];
		}
		return "";
	}
}
