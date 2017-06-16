package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.CasingInfoPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Progressable;
import com.bytesbrothers.shroomtycoon.elements.TextAndSeconds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Casing extends Progressable {
	public String name = "";
	
	public int growth = 0;
	/** Replaced the temp requirement. Instead, maintenance increases along with growth (faster or slower depending on
	 * strain.temp and inc/fc.temp) and halts growth if it reaches ST.Casing_MAINTENANCE. Results in player having to apply
	 * maintenance on jars and casings every so often.*/
	public int maintenance = 0;
	public int maintenanceDelay = 0;
	public int pin_occur_delay = 0;
	public float batch_weight = -1;
	public float total_batch_weight = -1;
	
	public StrainInstance strain = null;
//	public transient ShroomClass game = null;
//	transient Alert alert = null;
//	
//	transient static final int MAINTENANCE_ID = 901202;
//	transient static final int INCUBATED_ID = 903217;
//	transient static final int SOAKED_ID = 201;
//	transient static final int HUMIDITY_ID = 34567;
//	transient static final int FRUITED_ID = 721;
	
	public boolean alertedOnIncubated = false;
	public boolean alertedOnSoaked = false;
//	public boolean alertedOnPinned = false;
	
	public int location = ST.INCUBATOR;

	public int substrate = ST.COCO_COIR;
	
	public int mold_typeA;
	public boolean mold_contaminated = false;
	public int mold_typeB = -1;
	
	public ArrayList<Fruit> fruits = new ArrayList<Fruit>();
	
	public boolean beenPrinted = false;
//	public boolean beenSoaked = false;
	/** -1 if not soaking, 0-SOAK_TIME if soaking, >SOAK_TIME if soaked */
	public int soaking = -1;
	
	public boolean hasTemp = true;
	public boolean hasHumidity = true;

//	transient private Tree<Fruit> fruitTree = null;
	
	public Casing() {
//		makeFruitTree();
	}
	
	public Casing( String newname, StrainInstance strain, ShroomClass game ) {
		this();
		name = newname;
		this.strain = strain;

		Random random = new Random();
		
		mold_typeA = random.nextInt( ST.MOLD_NAMES.length );
		
		if ( random.nextFloat() > Assets.player.getGbSterility() ) {
			// then make this one contaminated
			mold_contaminated = true;
			mold_typeB = random.nextInt( ST.CONT_NAMES.length );
		} else if ( random.nextBoolean() ) {
			// if not then maybe use moldB
			mold_typeB = random.nextInt( ST.MOLD_NAMES.length );
		}
	}
	public Casing( String newname, Strain strain, ShroomClass game ) {
		this( newname, new StrainInstance( strain ), game );
	}
	
	public void updateOneSec() {
		if ( location== ST.INCUBATOR ) {
			// Incubator
			if ( !isSoaking() ) {
				if ( batch_weight==-1 ) {
					if ( growth<strain.casing_grow_time ) {
						maintenance += 1;
						maintenanceDelay += 1;
						if ( maintenance < ST.getCasingMaintenance( strain.temp_required, Assets.player.inc_temp ) || maintenanceDelay>=3 ) {
							growth += 1;
							maintenanceDelay = 0;
						} else if ( shouldMaintenanceAlert() ) {
							Alert alert = Assets.alertPool.borrowObject();
							alert.reset();
							alert.origin = this;
							alert.icon = "CasingIcon";
							alert.title = "Casing Trouble";
							alert.message = name + " requires some maintenance";
							alert.uniqueID = ST.ALERT_MAINTENANCE_ID;
							alert.type = ST.TABLE;
							alert.nextPart = CasingInfoPart.class;
//							alert.partName = "CasingInfo";
//							alert.screenPart = game.getScreenPart( "CasingInfo" );
							alert.partObject = this;
//							alert.table = new CasingInfoColumn( this );
							Assets.addAlert( alert );
						}
					} else {
						growth = strain.casing_grow_time;
						if ( shouldIncubatedAlert() ) {
							Alert alert = Assets.hasAlertWithOrigin( this );
							if ( alert!=null && alert.uniqueID==ST.ALERT_MAINTENANCE_ID )
								useAlert();
							alert = Assets.alertPool.borrowObject();
							alert.reset();
							alert.origin = this;
//							alert.timer = 10*60; // 10 minutes
							alert.icon = "CasingIcon";
							alert.title = "Casing Complete";
							alert.message = name + " has incubated for enough time";
							alert.uniqueID = ST.ALERT_INCUBATED_ID;
							alert.type = ST.TABLE;
//							alert.partName = "CasingInfo";
							alert.nextPart = CasingInfoPart.class;
							alert.partObject = this;
							Assets.addAlert( alert );
							alertedOnIncubated = true;
						}
					}
				}
			} else {
				// it's soaking
				if ( soaking<ST.SOAK_TIME ) {
					soaking ++;
				} else if ( shouldSoakedAlert() ) {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.timer = 10*60; // 10 minutes
					alert.icon = "CasingIcon";
					alert.title = "Casing Complete";
					alert.message = name + " has finished soaking";
					alert.uniqueID = ST.ALERT_SOAKED_ID;
					alert.type = ST.TABLE;
//					alert.partName = "CasingInfo";
					alert.nextPart = CasingInfoPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
					alertedOnSoaked = true;
				}
			}
		} else if ( location== ST.FC ) {
			// Fruiting Chamber
			if ( batch_weight>0.0f ) {
				maintenance += 1;
				maintenanceDelay += 1;
				if ( maintenance < ST.getCasingMaintenance( strain.temp_required, Assets.player.fc_temp ) || maintenanceDelay>=3  ) {
					pin_occur_delay += 1;
					maintenanceDelay = 0;
				} else if ( shouldMaintenanceAlert() ) {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.icon = "CasingIcon";
					alert.title = "Casing Trouble";
					alert.message = name + " requires some maintenance";
					alert.uniqueID = ST.ALERT_MAINTENANCE_ID;
					alert.type = ST.TABLE;
//					alert.partName = "CasingInfo";
					alert.nextPart = CasingInfoPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
				}
				if ( pin_occur_delay>= strain.pins_time ) {
					pin_occur_delay = 0;
					
					float weight = addOneFruit();
					batch_weight -= weight;
				}
			} else
				batch_weight = 0.0f;
			
			boolean finished = true;
			// update each fruit
			Iterator<Fruit> iterator = fruits.iterator();
			while ( iterator.hasNext() ) {
				Fruit currentFruit = iterator.next();
				currentFruit.updateOneSec();
				if ( currentFruit.getProgress()!= 1.0f )
					finished = false;
			}
			if ( finished && batch_weight<=0.0f && shouldFruitedAlert() ) {
				Alert alert = Assets.hasAlertWithOrigin( this );
				if ( !(alert!=null && alert.uniqueID==ST.ALERT_MAINTENANCE_ID) ) {
//					useAlert();
					alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert = new Alert();
					alert.timer = 10*60; // 10 minutes
					alert.icon = "CasingIcon";
					alert.title = "Casing Complete";
					alert.message = name + " is finished fruiting";
					alert.uniqueID = ST.ALERT_FRUITED_ID;
					alert.type = ST.TABLE;
	//				alert.partName = "CasingInfo";
					alert.nextPart = CasingInfoPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
				}
//				alertedOnPinned = true;
			}
		}
	}
	
	
	
	public void updateBy( int secs ) {
		if ( location== ST.INCUBATOR ) {
			// Incubator
			if ( !isSoaking() ) {
				if ( batch_weight==-1 ) {
					int requiredMain = ST.getCasingMaintenance( strain.temp_required, Assets.player.inc_temp );
					if ( growth<strain.casing_grow_time ) {
							// if the growth will not be complete after updating
							
							if ( maintenance >= requiredMain ) {
								// if maintenance is already too high
								growth += (int) ( (float)secs * 0.33f );
								
							} else if ( maintenance + secs < requiredMain ) {
								// if maintenance will NOT be reached after updating
								// update growth as usual
								
								growth += secs;
								maintenance += secs;
								maintenanceDelay = 0;
							} else {
								// if maintenance will be reached after updating
								int difference = requiredMain - maintenance;
								
								growth += difference;
								
								// update the remaining time by 1/3
								growth += (int) ( (float)(secs-difference) * 0.33f );
								maintenance = requiredMain;
								
							}
							
						if ( maintenance>=requiredMain && shouldMaintenanceAlert() ) {			
							Alert alert = Assets.alertPool.borrowObject();
							alert.reset();
							alert.origin = this;
							alert.icon = "CasingIcon";
							alert.title = "Casing Trouble";
							alert.message = name + " requires some maintenance";
							alert.uniqueID = ST.ALERT_MAINTENANCE_ID;
							alert.type = ST.TABLE;
//							alert.partName = "CasingInfo";
							alert.nextPart = CasingInfoPart.class;
							alert.partObject = this;
							Assets.addAlert( alert );
						}
					} else {
						growth = strain.casing_grow_time;
						if ( shouldIncubatedAlert() ) {
							Alert alert = Assets.hasAlertWithOrigin( this );
							if ( alert!=null && alert.uniqueID==ST.ALERT_MAINTENANCE_ID )
								useAlert();
							alert = Assets.alertPool.borrowObject();
							alert.reset();
							alert.origin = this;
//							alert.timer = 10*60; // 10 minutes
							alert.icon = "CasingIcon";
							alert.title = "Casing Complete";
							alert.message = name + " has incubated for enough time";
							alert.uniqueID = ST.ALERT_INCUBATED_ID;
							alert.type = ST.TABLE;
//							alert.partName = "CasingInfo";
							alert.nextPart = CasingInfoPart.class;
							alert.partObject = this;
							Assets.addAlert( alert );
							alertedOnIncubated = true;
						}
					}
				}
			} else {
				// it's soaking
				if ( soaking<ST.SOAK_TIME ) {
					soaking += secs;
					if ( soaking>ST.SOAK_TIME )
						soaking = ST.SOAK_TIME;
				}
				if ( soaking>=ST.SOAK_TIME && shouldSoakedAlert() ) {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.timer = 10*60; // 10 minutes
					alert.icon = "CasingIcon";
					alert.title = "Casing Complete";
					alert.message = name + " has finished soaking";
					alert.uniqueID = ST.ALERT_SOAKED_ID;
					alert.type = ST.TABLE;
//					alert.partName = "CasingInfo";
					alert.nextPart = CasingInfoPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
					alertedOnSoaked = true;
				}
			}
		} else if ( location== ST.FC ) {
			// Fruiting Chamber
			if ( batch_weight>0.0f ) {
				int requiredMain = ST.getCasingMaintenance( strain.temp_required, Assets.player.fc_temp );
				if ( maintenance>= requiredMain ) {
					// if maintenance is already too high
					pin_occur_delay += (int) ( ( float) secs * 0.33f );
					
				} else if ( maintenance + secs >= requiredMain ) {
					// if maintenance will become too high while we're updating
					int difference = requiredMain - maintenance;
					
					pin_occur_delay += difference;
					
					pin_occur_delay += (int) ( (float)(secs - difference)*0.33f );
					maintenance = requiredMain;
				} else {
					// if maintenance has a ways to go
					maintenance += secs;
					maintenanceDelay = 0;
					pin_occur_delay += secs;
				} 
				
				if ( maintenance>= requiredMain && shouldMaintenanceAlert() ) {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.icon = "CasingIcon";
					alert.title = "Casing Trouble";
					alert.message = name + " requires some maintenance";
					alert.uniqueID = ST.ALERT_MAINTENANCE_ID;
					alert.type = ST.TABLE;
//					alert.partName = "CasingInfo";
					alert.nextPart = CasingInfoPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
				}
				while ( pin_occur_delay>= strain.pins_time && batch_weight>0 ) {
					pin_occur_delay -= strain.pins_time;
					
					float weight = addOneFruit();
					batch_weight -= weight;
				}
			} else
				batch_weight = 0.0f;
			
			boolean finished = true;
			// update each fruit
//			Iterator<Fruit> iterator = fruits.iterator();
//			while ( iterator.hasNext() ) {
//				Fruit currentFruit = iterator.next();
			for ( Fruit currentFruit: fruits ) {
//				currentFruit.updateBy( secs );
				currentFruit.growth += secs;
				if ( currentFruit.growth>=currentFruit.max_growth )
					currentFruit.growth = currentFruit.max_growth;
	//			currentFruit.updateOneSec();
				if ( currentFruit.getProgress()!= 1.0f )
					finished = false;
			}
			if ( finished && batch_weight<=0.0f && shouldFruitedAlert() ) {
				Alert alert = Assets.hasAlertWithOrigin( this );
				if ( !(alert!=null && alert.uniqueID==ST.ALERT_MAINTENANCE_ID) ) {
//					useAlert();
					alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.timer = 10*60; // 10 minutes
					alert.icon = "CasingIcon";
					alert.title = "Casing Complete";
					alert.message = name + " is finished fruiting";
					alert.uniqueID = ST.ALERT_FRUITED_ID;
					alert.type = ST.TABLE;
	//				alert.partName = "CasingInfo";
					alert.nextPart = CasingInfoPart.class;
					alert.partObject = this;
					Assets.addAlert( alert );
				}
//				alertedOnPinned = true;
			}
		}
	}
	
	public TextAndSeconds getAlarm() {
		if ( location== ST.INCUBATOR ) {
			if ( !isSoaking() ) {
				if ( batch_weight == -1 && growth<strain.casing_grow_time ) {
					// it is still in incubation phase
					int requiredMain = ST.getCasingMaintenance( strain.temp_required, Assets.player.inc_temp );
					// represents the amount of time that this jar will grow before needing maintenance
					int unmainSeconds = requiredMain - maintenance;
					if ( unmainSeconds<0 )
						unmainSeconds = 0;
					
					int requiredSeconds = strain.jar_grow_time - growth;

					int seconds = (requiredSeconds - unmainSeconds)*3 + unmainSeconds;
					return new TextAndSeconds( seconds, "Your casing is fully incubated!" );
				}
			} else if ( soaking<ST.SOAK_TIME ) {
				// the casing is in soaking phase
				return new TextAndSeconds( ST.SOAK_TIME - soaking, "Your casing is done soaking!" );
			}
		} else if ( location == ST.FC ) {
			// fruiting phase
			int time_left = -1;

			// if there is at least one fruit that needs to pop out: add that much time
			if ( batch_weight>0.0f ) {
				time_left = strain.fruit_grow_time;
				// add time until last fruit pops out
				float current_weight = batch_weight;
				while ( current_weight>0.0f ) {
					current_weight -= strain.fruit_weight;
					time_left += strain.pins_time;
				}
			} else
				// find youngest fruit
				for ( Fruit fruit: fruits )
					time_left = Math.max( time_left, fruit.max_growth - fruit.growth );
			
			float percentageMain = ((float)(maintenance+1)/(float)ST.getCasingMaintenance( strain.temp_required, Assets.player.fc_temp ) );
			
			int seconds = (int) (((float)time_left * (1.0f - percentageMain)) + ((float)time_left * percentageMain * 3));
			return new TextAndSeconds( seconds, "Your casing is ready for harvest!" );
		}
		
		return null;
	}
	
	public void useAlert() {
		Alert alert = Assets.hasAlertWithOrigin( this );
		while ( alert!=null ) {
			Assets.removeAlert( alert );
			alert = Assets.hasAlertWithOrigin( this );
		}
	}
	
	public void moveIntoFc() {
		// if this is the first time
		// determine total batch weight
		if ( batch_weight == -1 ) {
			batch_weight = strain.batch_weight;
			
			// if we used a special substrate add %20
			if ( strain.substrate!=ST.COCO_COIR && substrate==strain.substrate )
				batch_weight += strain.batch_weight * .2f;
			
			// if casing isn't fully grown penalize:
			if ( growth<strain.casing_grow_time ) {
				float percentMissing = 1.0f - ((float)growth)/((float)strain.casing_grow_time);
				batch_weight -= strain.batch_weight * 0.6f * percentMissing;
			}
			// save this number for later use
			total_batch_weight = batch_weight;
			
			maintenance = 0;
		}
		// if we've been soaking
		if ( soaking != -1 && soaking <=ST.SOAK_TIME ) {
			
			// add to batch_weight
			float percent = 0.5f * ( (float)soaking/(float)ST.SOAK_TIME ); // up to %50 more depending on if it soaked the whole time
			batch_weight += total_batch_weight * percent;
			
			soaking = ST.SOAK_TIME + 2; // 2 because why the fuck not
			
			maintenance = 0;
			
			
		}
		location = ST.FC;
	}
	
	/**
	 * returns between 1 and 8 (inclusive) depending on growth factor
	 */
	public int getFrame() {
		return (int) (((float)growth)/((float)strain.casing_grow_time) * 7.0f) + 1;
	}
	/**
	 * returns between 0.0f and 1.0f
	 * 	if incubating: growth out of casing_grow_time
	 * 	if fruiting: total fruits growth out of total fruit_grow_time (estimates fruits not yet pinned)
	 *  if fruiting: and fruits have all been picked then 1.0f;
	 */
	@Override
	public float getProgress() {
		if ( location==ST.INCUBATOR ) {
			if ( soaking!= -1 )
				if ( soaking<=ST.SOAK_TIME )
					return (float)soaking/(float)ST.SOAK_TIME;
				else 
					return 1.0f;
			
			return (float)growth/(float)strain.casing_grow_time;
		} else if ( location == ST.FC ) {
			float fruit_growth = 0.01f;
			float fruit_max_growth = 0.01f;
			// find youngest fruit
			Iterator<Fruit> iterator = fruits.iterator();
			while ( iterator.hasNext() ) {
				Fruit currentFruit = iterator.next();
				fruit_growth += currentFruit.growth;
				fruit_max_growth += currentFruit.max_growth;
			}
			
			float current_weight = batch_weight;
			while ( current_weight>0.0f ) {
				current_weight -= strain.fruit_weight;
				fruit_growth += 0;
				fruit_max_growth += strain.fruit_grow_time;
			}

			return fruit_growth/fruit_max_growth;
		}
		// may be in Fridge
		return 0.0f;
	}
	
	/**
	 * returns (in seconds) the amount of time until the next stage:
	 * 	if incubating: time left until time for fruiting
	 * 	if fruiting: time left until last fruit is fully grown
	 * 
	 * 	-1 means infinity (like in the fridge)
	 */
	public int getTimeLeft() {
		if ( location==ST.INCUBATOR ) {
			if ( soaking!= -1 )
				if ( soaking<=ST.SOAK_TIME)
					return ST.SOAK_TIME - soaking;
				else // if soaking >ST.SOAK_TIME
					return -1;
			
			if ( maintenance >= ST.getCasingMaintenance( strain.temp_required, Assets.player.inc_temp ) )
				return (strain.casing_grow_time - growth) * 3;
			
			return strain.casing_grow_time - growth;
		} else if ( location == ST.FC ) {
			int time_left = -1;

			// if there is at least one fruit that needs to pop out: add that much time
			if ( batch_weight>0.0f ) {
				time_left = strain.fruit_grow_time;
				// add time until last fruit pops out
				float current_weight = batch_weight;
				while ( current_weight>0.0f ) {
					current_weight -= strain.fruit_weight;
					time_left += strain.pins_time;
				}
			} else {
				// find youngest fruit
				Iterator<Fruit> iterator = fruits.iterator();
				while ( iterator.hasNext() ) {
					Fruit currentFruit = iterator.next();
					time_left = Math.max( time_left, currentFruit.max_growth - currentFruit.growth );
				}
			}
			if ( maintenance >= ST.getCasingMaintenance( strain.temp_required, Assets.player.fc_temp ) )
				return time_left * 3;
			
			return time_left;
		}
		// may be in Fridge
		return -1;
	}
	
	public float addOneFruit() {
		Fruit newFruit = new Fruit( strain );
		// only add a fruit if FC humidity is >= all casings requirement
		if ( Assets.player.hasHumidity() ) {
			fruits.add( newFruit );
//			makeFruitTree();
		} else if ( shouldHumidityAlert() ) {
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.origin = this;
			alert.icon = "CasingIcon";
			alert.title = "Casing Trouble";
			alert.message = name + " is not fruiting";
			alert.uniqueID = ST.ALERT_HUMIDITY_ID;
			alert.type = ST.TABLE;
//			alert.partName = "CasingInfo";
			alert.nextPart = CasingInfoPart.class;
			alert.partObject = this;
			Assets.addAlert( alert );
		}
		
		return newFruit.weight;
	}
	
	public boolean shouldMaintenanceAlert() {
		if( !getsMaintenance() )
			return false;
		// if the player is looking at this jar then NO
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "CasingInfo" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		// if the player is looking at this jar then NO
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Casing" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		// if there is already a maintenance alert then NO
		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null && alert.uniqueID==ST.ALERT_MAINTENANCE_ID )
			return false;
		
		return true;
	}
	
	public boolean shouldIncubatedAlert() {
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "CasingInfo" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Casing" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		// if there is already a maintenance alert then NO
		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null && alert.uniqueID==ST.ALERT_INCUBATED_ID )
			return false;
		
		return true;
	}
	
	public boolean shouldSoakedAlert() {
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "CasingInfo" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Casing" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null && alert.uniqueID==ST.ALERT_SOAKED_ID )
			return false;
		
		return true;
	}
	
	public boolean shouldHumidityAlert() {
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "CasingInfo" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;

		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Casing" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;

		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null && alert.uniqueID==ST.ALERT_HUMIDITY_ID )
			return false;
		
		return true;
	}
	
	public boolean shouldFruitedAlert() {
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "CasingInfo" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Casing" ) &&
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;

		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null && alert.uniqueID==ST.ALERT_FRUITED_ID )
			return false;
		
		return true;
	}

	
	/** 
	 * For checking whether or not there's at least one mature fruit
	 * @return True if at least one fruit is mature
	 */
	public boolean isHarvestable() {
		Iterator<Fruit> iterator = fruits.iterator();
		while ( iterator.hasNext() ) {
			Fruit thisFruit = iterator.next();
			if ( thisFruit.getFrame()>13 )
				return true;
		}
		
		return false;
	}
	
	/** checks if we're soaking */
	public boolean isSoaking() {
		return (soaking>=0 && soaking<=ST.SOAK_TIME );
	}
	
	/** provides a message with what is next in this casing's process */
	public String whatsNext() {
		if ( location==ST.INCUBATOR ) {
			// if its incubating
			if ( getProgress()<1.0f )
				if ( maintenance >= ST.getCasingMaintenance( strain.temp_required, Assets.player.inc_temp ) )
					return "This casing requires maintenance before it will continue growing.";
				else if ( isSoaking() )
					if ( soaking==60 )
						return "It's time to move this casing back into the Fruiting Chamber.";
					else
						return "This casing needs time to soak.";
				else
					return "This casing needs time to incubate.";
			// if its in the incubator and fully incubated
			else
				if ( mold_contaminated )
					return "This casing is CONTAMINATED. There's nothing to do with it except toss it.";
				else
					return "It is time to move this casing into the Fruiting Chamber.";
		} else if ( location==ST.FC ) {
			if ( !isHarvestable() ) {
				if ( getProgress()<1.0f ) {
					// if temp is insufficient
					if (  maintenance >= ST.getCasingMaintenance( strain.temp_required, Assets.player.fc_temp ) )
						return "This casing requires maintenance before it will continue growing.";
					// if humidity is insufficient
					if ( !Assets.player.hasHumidity() )
						return "There is not enough humidity in the fruiting chamber and no pinheads are forming.";
					// if its been moved into the fruiting chamber and needs to grow
					return "This casing is trying to grow mushrooms, it just needs more time.";
				} else if ( soaking==-1 )
					// if it hasn't been soaked yet
					return "This casing is exhausted but can be soaked, giving one last flush.";
				else 
					// after being soaked and fully grown and having been harvested
					return "This casing has been exhausted to the full extent.";
			} else {
				if ( mold_contaminated )
					return "This casing is CONTAMINATED. There's nothing to do with it except toss it.";
				else
					if ( !beenPrinted )
						// if it hasn't been printed
						return "This casing has fruits for harvesting! Be sure to take a spore syringe first.";
					else
						// if there are fruits to harvest
						return "This casing has fruits for harvesting!";
			}
		} else {
			// must be in fridge
			return "This casing is too cold to grow anything.";
		}
	}
	
	@Override
	public boolean equals( Object object ) {
		if ( object==null )
			return false;
		if ( object.getClass().equals( Casing.class ) ) {
			Casing thatCasing = (Casing)object;
			if ( !name.equals( thatCasing.name ) )
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
		if ( location==ST.INCUBATOR && growth >= strain.casing_grow_time )
			return false;
		if ( location==ST.FC && batch_weight<=0.0f )
			return false;
		if ( soaking!= -1 || location==ST.FRIDGE )
			return false;
		return true;
	}
	
	public Progressable getMaintenanceBar() {
		return new Progressable() {
			@Override
			public float getProgress() {
				if ( !getsMaintenance() || Assets.player==null )
					return 0.0f;
				
				switch( location ) {
				case ST.INCUBATOR:
					return Math.min( (float)maintenance/(float)ST.getCasingMaintenance( strain.temp_required, Assets.player.inc_temp ), 1.0f);
				case ST.FC:
					return Math.min( (float)maintenance/(float)ST.getCasingMaintenance( strain.temp_required, Assets.player.fc_temp ), 1.0f);
				}
				
				return 0.0f;
				
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
				int timeLeft = ST.getCasingMaintenance( strain.temp_required, location==ST.INCUBATOR?Assets.player.inc_temp:Assets.player.fc_temp ) - maintenance;
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
