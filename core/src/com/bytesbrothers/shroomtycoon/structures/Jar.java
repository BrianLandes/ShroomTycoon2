package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.JarPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Progressable;
import com.bytesbrothers.shroomtycoon.elements.TextAndSeconds;

import java.util.Random;

public class Jar extends Progressable {
	public String name = "";

	int growth = 0;
	/** Replaced the temp requirement. Instead, maintenance increases along with growth (faster or slower depending on
	 * strain.temp and inc/fc.temp) and halts growth if it reaches ST.JAR_MAINTENANCE. Results in player having to apply
	 * maintenance on jars and casings every so often.*/
	public int maintenance = 0;
	public int maintenanceDelay = 0;
	
	public StrainInstance strain = null;
//	public transient ShroomClass game = null;
//	transient Alert alert = null;
//	transient static final int MAINTENANCE_ID = 9002;
//	transient static final int INCUBATED_ID = 25617;
	public boolean alertedOnIncubated = false;
	
	public int location = ST.INCUBATOR;
	public int mold_typeA;
	public int mold_typeB = -1;
	public boolean contaminated = false;

	public Jar() {
		
	}
	
	public Jar( String newName, StrainInstance newStrain, ShroomClass game ) {
		this();
		name = newName;
		this.strain = newStrain;
		
		// randomly choose molds
		Random random = new Random();
		
		mold_typeA = random.nextInt( ST.JAR_MOLD_NAMES.length );
		
		if ( random.nextFloat() > Assets.player.getGbSterility() ) {
			// then make this one contaminated
			contaminated = true;
			
			mold_typeB = random.nextInt( ST.JAR_CONT_NAMES.length );
		} else if ( random.nextBoolean() ) {
			// if not then maybe use moldB
			mold_typeB = random.nextInt( ST.JAR_MOLD_NAMES.length );
		}
	}
	public Jar( String newname, Strain strain, ShroomClass game ) {
		this( newname, new StrainInstance( strain ), game );
	}
	
	/** updates this shit by one second. What did you expect? */
	public void updateOneSec() {
		if ( location == ST.INCUBATOR ) {
			// Incubator
			if ( growth<strain.jar_grow_time ) {
				maintenance += 1;
				maintenanceDelay += 1;
				if ( maintenance < ST.getJarMaintenance( strain.temp_required, Assets.player.inc_temp ) || maintenanceDelay>=3 ) {
					growth += 1;
					maintenanceDelay = 0;
				} else if ( shouldMaintenanceAlert() ) {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.uniqueID = ST.ALERT_JAR_MAINTENANCE_ID;
					alert.icon = "JarIcon";
					alert.title = "Jar Trouble";
					alert.message = name + " requires maintenance";
					alert.type = ST.TABLE;
					alert.nextPart = JarPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
				}
			} else {
				growth = strain.jar_grow_time;
				if ( shouldIncubatedAlert() ) {
					// remove the alert if it was a maintenance alert
					Alert alert = Assets.hasAlertWithOrigin( this );
					if ( alert!=null && alert.uniqueID==ST.ALERT_JAR_MAINTENANCE_ID )
						useAlert();
					alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.icon = "JarIcon";
					alert.title = "Jar Complete";
					alert.message = name + " has become fully colonized";
					alert.uniqueID = ST.ALERT_JAR_INCUBATED_ID;
					alert.type = ST.TABLE;
//					alert.screenPart = game.getScreenPart( "Jar" );
//					alert.partName = "Jar";
					alert.nextPart = JarPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
//					alertedOnIncubated = true;
					
				}
			}
				
		}
	}
	
	
	/** updates this shit by secs */
	public void updateBy( int secs ) {
		if ( location == ST.INCUBATOR ) {
			// Incubator
			if ( growth<strain.jar_grow_time  ) {
				// check the maintenance
				int requiredMain = ST.getJarMaintenance( strain.temp_required, Assets.player.inc_temp );
				if ( maintenance>requiredMain ) {
					// maintenance is already too high
					
					// add growth at a rate of 1/3
					growth += (int)((float)secs*0.33f);
					
				} else if ( maintenance+secs>requiredMain ) {
					// maintenance will become too high while updating
					int regularDif = requiredMain - maintenance;
					// add growth regular until maintenace does become too high
					growth += regularDif;
					
					int remainderDif = secs - regularDif;
					// then add at a rate of 1/3 for the remainder
					growth += (int)((float)remainderDif*0.33f);
					
					maintenance = requiredMain;
				} else {
					// maintenance is still a long enough ways off
					 growth += secs;
					 maintenance += secs;
					 maintenanceDelay = 0;
				}
				
				if ( maintenance>=requiredMain && shouldMaintenanceAlert() ) {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.uniqueID = ST.ALERT_JAR_MAINTENANCE_ID;
					alert.icon = "JarIcon";
					alert.title = "Jar Trouble";
					alert.message = name + " requires maintenance";
					alert.type = ST.TABLE;
					alert.nextPart = JarPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
				}
				
				// if growth has wound up being greater than jar_grow_time set growth equal to jar_grow_time
				if ( growth>strain.jar_grow_time)
					growth = strain.jar_grow_time;
			} else {
				// growth is already greater than or equal to jar_grow_time
			
				growth = strain.jar_grow_time;
				if ( shouldIncubatedAlert() ) {
					// remove the alert if it was a maintenance alert
					Alert alert = Assets.hasAlertWithOrigin( this );
					if ( alert!=null && alert.uniqueID==ST.ALERT_JAR_MAINTENANCE_ID )
						useAlert();
					alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.icon = "JarIcon";
					alert.title = "Jar Complete";
					alert.message = name + " has become fully colonized";
					alert.uniqueID = ST.ALERT_JAR_INCUBATED_ID;
					alert.type = ST.TABLE;
//					alert.screenPart = game.getScreenPart( "Jar" );
//					alert.partName = "Jar";
					alert.nextPart = JarPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
				}
			}
				
		}
	}
	
	public TextAndSeconds getAlarm() {
		if ( location == ST.INCUBATOR && growth<strain.jar_grow_time ) {
			int requiredMain = ST.getJarMaintenance( strain.temp_required, Assets.player.inc_temp );
			// represents the amount of time that this jar will grow before needing maintenance
			int unmainSeconds = requiredMain - maintenance;
			if ( unmainSeconds<0 )
				unmainSeconds = 0;
			
			int requiredSeconds = strain.jar_grow_time - growth;

			int seconds = (requiredSeconds - unmainSeconds)*3 + unmainSeconds;
			return new TextAndSeconds( seconds, "Your jar is fully incubated!" );
		}
		return null;
	}
	
	public boolean shouldMaintenanceAlert() {
		if( !getsMaintenance() )
			return false;
		// if the player is looking at this jar then NO
//		if ( game.activePart.header.equals( "Jar" ) && ((JarPart)game.activePart).theJar.equals(this ) )
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Jar" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		
		
		// if there is already a maintenance alert then NO
		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null && alert.uniqueID==ST.ALERT_JAR_MAINTENANCE_ID )
			return false;
		
		return true;
	}
	
	public boolean shouldIncubatedAlert() {
		// if the player is looking at this jar then NO
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Jar" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
//		if ( game.
//				activePart.
//				header.equals( "Jar" ) && ((JarPart)game.
//						activePart).
//						theJar.equals(this ) )
			return false;
		
//		// if the player is looking at this jar then NO
//		if ( game.getColumnName().equals( "MakeCasing" ) && ((MakeCasingColumn)game.currentTable).theJar.equals( this ) )
//			return false;
//		
//		// if the player is looking at this jar then NO
//		if ( game.getColumnName().equals( "Project" ) && ((ProjectColumn)game.currentTable).theProject.name.equals( "Make Casing" ) )
//			return false;
		
		// if there is already a maintenance alert then NO
		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null && alert.uniqueID==ST.ALERT_JAR_INCUBATED_ID )
			return false;
		
		return true;
	}
	
	/**
	 * returns between 1 and 8 (inclusive) depending on growth factor
	 */
	public int getFrame() {
		return (int) (((float)growth)/((float)strain.jar_grow_time) * 7.0f) + 1;
	}
	/**
	 * returns between 0.0f and 1.0f
	 * 	whether incubating, in FC or fridge: growth out of jar_grow_time
	 */
	@Override
	public float getProgress() {
		return (float)growth/(float)strain.jar_grow_time;
	}
	
	/**
	 * returns (in seconds) the amount of time until done growing:
	 * 	-1 means infinity (like in the fridge)
	 */
	public int getTimeLeft() {
		if ( location==ST.INCUBATOR ) {
			if ( maintenance >= ST.getJarMaintenance( strain.temp_required, Assets.player.inc_temp ) )
				return (strain.jar_grow_time - growth) * 3;
			return strain.jar_grow_time - growth;
		}
		// may be in Fridge
		return -1;
	}
	
	/** provides a message with what is next in this casing's process */
	public String whatsNext() {
		switch( location ) {
		case ST.INCUBATOR:
			if ( getProgress()<1.0f )
				if ( maintenance < ST.getJarMaintenance( strain.temp_required, Assets.player.inc_temp ) )
					return "This jar needs time to grow.";
				else
					return "This jar requires some attention before it'll grow again.";
			else
				if ( contaminated )
					return "This jar is CONTAMINATED. It is worthless and needs to be thrown away.";
				else
					return "This jar is ready to be made into a casing or used in G2G.";
		case ST.FC:
			return "How did this jar get in the FC?";
		case ST.FRIDGE:
			return "This jar is too cold to grow.";
		}
		return "Idk wtf.";
	}
	
//	/** 
//	 * Draws the jar in 3D to the screen
//	 * @param a bunch of shit
//	 */
//	public void draw( GL20 gl, float x, float y, TexturedSquare2d grainGraphic, Graphic2d jarGraphic,
//			TextureAtlas jars ) {
////		int frame = getFrame();
////		
////		gl.glPushMatrix();
////    	
////    	gl.glTranslatef( x, y, 0.0f );
////    	
////    	gl.glRotatef( -25.0f, 1.0f, 0.0f, 0.0f );
////    	
////    	gl.glScalef( 0.55f, 1.0f, 1.0f );
////    	grainGraphic.giveTextureOne( jars.findRegion( ST.JAR_MOLD_NAMES[ mold_typeA ], frame ) );
////    	if ( contaminated ) {
////    		grainGraphic.giveTextureTwo( jars.findRegion( ST.JAR_CONT_NAMES[ mold_typeB ], frame ) );
////    	} else if ( mold_typeB!= -1 ) {
////    		grainGraphic.giveTextureTwo( jars.findRegion( ST.JAR_MOLD_NAMES[ mold_typeB ], frame ) );
////    	} else {
////    		grainGraphic.useTexB = false;
////    	}
////		grainGraphic.draw( gl );
////
////		gl.glPopMatrix();
////		
////		
////		
////		gl.glPushMatrix();
////    	
////    	gl.glTranslatef( x, y, 0.25f );
////    	
////    	gl.glRotatef( -25.0f, 1.0f, 0.0f, 0.0f );
////    	
////    	gl.glScalef( 0.55f, 1.0f, 1.0f );
////    	
////    	jarGraphic.enable( gl );
////		jarGraphic.draw( gl );
////		jarGraphic.disable( gl );
////
////		gl.glPopMatrix();
//	}
	
	public void useAlert() {
		Alert alert = Assets.hasAlertWithOrigin( this );
		while ( alert!=null ) {
			Assets.removeAlert( alert );
			alert = Assets.hasAlertWithOrigin( this );
		}
	}

	@Override
	public boolean equals( Object object ) {
		if ( object==null )
			return false;
		if ( object.getClass().equals( Jar.class ) ) {
			Jar thatJar = (Jar)object;
			if ( !name.equals( thatJar.name ) )
				return false;
			if ( contaminated!=thatJar.contaminated )
				return false;
			if ( mold_typeA!=thatJar.mold_typeA )
				return false;
			if ( mold_typeB!=thatJar.mold_typeB )
				return false;
			return true;
		} else
			return super.equals( object );
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
	
	public boolean getsMaintenance() {
		if ( growth>=strain.jar_grow_time || location==ST.FRIDGE )
			return false;
		return true;
	}
	
	public Progressable getMaintenanceBar() {
		return new Progressable() {
			@Override
			public float getProgress() {
				if ( !getsMaintenance() || Assets.player==null )
					return 0.0f;
				return Math.min( (float)maintenance/(float)ST.getJarMaintenance( strain.temp_required, Assets.player.inc_temp ), 1.0f);
			}
			
			@Override
			public String getCenterText() {
				if ( !getsMaintenance() )
					return "";
				return "Maintenance";
			}
			
			@Override
			public String getRightText() {
				if ( !getsMaintenance() || Assets.player==null )
					return "";
				int timeLeft = ST.getJarMaintenance( strain.temp_required, Assets.player.inc_temp ) - maintenance;
				if ( timeLeft>0 ) {
					int minutes = (int) ((float)timeLeft/60.0f);
					int hours = (int) ((float)minutes/60.0f);
					minutes = minutes%60;
//					int seconds = timeLeft - hours*60*60 - minutes * 60;
					return "" + ( hours>0? (hours + "h "): "" ) + ( (minutes==0 && hours<1)? "<1m": (minutes + "m") );
				}
				return "Now";
			}
		};
	}
}
