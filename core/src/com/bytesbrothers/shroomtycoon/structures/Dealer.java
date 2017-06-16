package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.DealerPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Progressable;
import com.bytesbrothers.shroomtycoon.elements.TextAndSeconds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/** Person who deals */
public class Dealer extends Progressable {

//	public transient ShroomClass game = null;
	
	public String name = "";
	
	/** 1 - 9, rating of this dealer */
	public int rating;
	/** speed in grams per second they'll sell */
	public float speed;
	/** max number of batches they'll take */
	public int capacity;
	/** 0.0 - 1.0f, percent of this dealer's cut */
	public float cut;
	
	/** levels */
	public int speed_level = 0;
	public int capacity_level = 0;
	public int cut_level = 0;
	
	/** time player's known this dealer */
	public int days = 0;
	public int hours = 0;
	public int mins = 0;
	public int seconds = 0;
	
	/** xp. if this num reaches 10,000 they (may) go up one level */
	public int xp = 0;
	
	/** total num of g's this dealer has sold */
	public float total_sold = 0.0f;
	
	/** total amount of cash this dealer has made for player */
	public int total_cash_made = 0;
	
	public ArrayList<Batch> batches = new ArrayList<Batch>();

//	transient private Alert alert;
//	public boolean alertedOnSale = false;

	public Dealer() {
		
	}
	public Dealer( int rating, ShroomClass game ) {
		this.rating = rating;
//		this.game = game;
		// randomly generate dealer's stats from rating
		while ( rating>0 ) {
			oneLevelUp();
			
			rating --;
		}

		speed = getSpeedLevel( speed_level );
		capacity = getCapacityLevel( capacity_level );
		cut = getOverheadLevel( cut_level );
		
		name = ST.getName();
	}
	
	/** increase one stat by one. The actual stat will need to be set afterwards */
	public void oneLevelUp() {
		Random random = new Random();
		
		boolean needsSpeed = speed_level<3;
		boolean needsCapacity = capacity_level<3;
		boolean needsCut = cut_level<3;
		if ( needsSpeed ) {
			// speed is less than 3
			if ( needsCapacity ) {
				// capacity is less than 3
				if ( needsCut ) {
					// cut is less than 3
					switch ( random.nextInt( 3 ) ) {
					case 0:
						speed_level ++;
						break;
					case 1:
						capacity_level ++;
						break;
					case 2:
						cut_level ++;
						break;
					}
				} else {
					// cut is full
					switch ( random.nextInt( 2 ) ) {
					case 0:
						speed_level ++;
						break;
					case 1:
						capacity_level ++;
						break;
					}
				}
			} else {
				// capacity is full
				if ( needsCut ) {
					// cut is less than 3
					switch ( random.nextInt( 2 ) ) {
					case 0:
						speed_level ++;
						break;
					case 1:
						cut_level ++;
						break;
					}
				} else {
					// cut is full
					// speed is the only one open
					speed_level ++;
				}
			}
		} else {
			// speed is full
			if ( needsCapacity ) {
				// capacity is less than 3
				if ( needsCut ) {
					// cut is less than 3
					switch ( random.nextInt( 2 ) ) {
					case 0:
						capacity_level ++;
						break;
					case 1:
						cut_level ++;
						break;
					}
				} else {
					// cut is full
					// capacity is the only one open
					capacity_level ++;
				}
			} else {
				// capacity is full
				if ( needsCut ) {
					// cut is less than 3
					// cut is the only one open
					cut_level ++;
				} else {
					// cut is full
					// none of them are open (somehow)
					rating = 0;
				}
			}
		}
	}
	
	public float getSpeedLevel( int level ) {
		switch ( level ) {
		case 0:
			return 1.0f/60.0f; // 1 g per 60 seconds
		case 1:
			return 1.5f/60.0f;
		case 2:
			return 2.0f/60.0f;
		case 3:
			return 2.5f/60.0f;
		}
		return 1.0f/60.0f;
	}
	
	public int getCapacityLevel( int level ) {
		switch ( level ) {
		case 0:
			return 1; // 1 batch
		case 1:
			return 2;
		case 2:
			return 3;
		case 3:
			return 4;
		}
		return 1;
	}
	
	public float getOverheadLevel( int level ) {
		switch ( level ) {
		case 0:
			return 0.25f; // %25
		case 1:
			return 0.20f;
		case 2:
			return 0.15f;
		case 3:
			return 0.10f;
		}
		return 0.25f;
	}
	
	/** updates this dealer by one second (drank)*/
	public void updateOneSec() {
		// if they're selling anything then sell it
		if ( batches.size()>0 ) {
			Iterator<Batch> batchIt = batches.iterator();
			while ( batchIt.hasNext() ) {
				Batch currentBatch = batchIt.next();
				if ( currentBatch.sold_weight<currentBatch.getWeight() ) {
					currentBatch.sold_weight += speed;
					if ( currentBatch.sold_weight>currentBatch.getWeight() )
						currentBatch.sold_weight = currentBatch.getWeight();
					break;
				} else {
					currentBatch.sold_weight = currentBatch.getWeight();
				}
			}
//			if ( getProgress()==1.0f && alert==null && !alertedOnSale ) {
			if ( shouldAlert() ) {
//				if ( game.getColumnName().equals( name ) ) {
//					game.resize( Gdx.graphics.getWidth(), Gdx.graphics.getHeight() );
//				} else {
					Alert alert = Assets.alertPool.borrowObject();
					alert.reset();
					alert.origin = this;
					alert.icon = "DealerIcon";
					alert.title = "Dealer Dealt";
					alert.message = name + " has money for you";
					alert.type = ST.TABLE;
					alert.nextPart = DealerPart.class;
//					alert.partName = "Dealer";
//					alert.screenPart = game.getScreenPart( "Dealer" );
					alert.partObject = this;
//					alert.table = new DealerColumn( this );
					Assets.addAlert( alert );
//				}
//				alertedOnSale = true;
			}
		}
		
		// update the time the player's known this dealer
		seconds += 1;
		if ( seconds>=60 ) { seconds = 0; mins += 1; }
		if ( mins>=60 ) { 
			mins = 0; hours += 1;
			// increase xp by 1
//			increaseXP( 1 );
		}
		if ( hours>=24 ) { hours = 0; days += 1; }
	}
	
	/** updates this dealer by secs */
	public void updateBy( int secs ) {
		// if they're selling anything then sell it
		if ( batches.size()>0 ) {

			int leftSecs = secs;
			for ( Batch currentBatch: batches ) {
				if ( currentBatch.sold_weight<currentBatch.getWeight() ) {
					if ( currentBatch.sold_weight + speed*leftSecs<currentBatch.getWeight() ) {
						currentBatch.sold_weight += speed*leftSecs;
						if ( currentBatch.sold_weight>currentBatch.getWeight() )
							currentBatch.sold_weight = currentBatch.getWeight();
						leftSecs = 0;
						break;
					} else {
						float difference = currentBatch.getWeight() - currentBatch.sold_weight;
						int difSecs = (int)(difference/speed);
						currentBatch.sold_weight = currentBatch.getWeight();
						leftSecs -= difSecs;
					}
				} else
					currentBatch.sold_weight = currentBatch.getWeight();
			}
			if ( shouldAlert() ) {
				Alert alert = Assets.alertPool.borrowObject();
				alert.reset();
				alert.origin = this;
				alert.icon = "DealerIcon";
				alert.title = "Dealer Dealt";
				alert.message = name + " has money for you";
				alert.type = ST.TABLE;
//				alert.screenPart = game.getScreenPart( "Dealer" );
//				alert.partName = "Dealer";
				alert.nextPart = DealerPart.class;
				alert.partObject = this;
//				alert.table = new DealerColumn( this );
				Assets.addAlert( alert );
			}
		}
		
		// update the time the player's known this dealer
		seconds += secs;
		while ( seconds>=60 ) { seconds -= 60; mins += 1; }
		while ( mins>=60 ) { mins -= 60; hours += 1; }
		while ( hours>=24 ) { hours -= 24; days += 1; }
	}
	
	public TextAndSeconds getAlarm() {
		if ( !batches.isEmpty() ) {
			float time_left = 0.0f;

			for ( Batch batch: batches )
				time_left += (batch.getWeight() - batch.sold_weight)/speed;
			
			return new TextAndSeconds( (int)time_left, "Your dealer has money for you!" );

		}
		return null;
	}
	
	public boolean shouldAlert() {
		if ( getProgress()!=1.0f )
			return false;
		
		// if the player is looking at this jar then NO
//		if ( game.activePart.header.equals( "Dealer" ) && ((DealerPart)game.activePart).theDealer.equals(this ) )
		if ( 
//				Assets.game.screenHandler.getActivePartHeader().equals( "Dealer" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
			return false;
		
		// if there is already a maintenance alert then NO
		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null )
			return false;
		
		return true;
	}

	public void useAlert() {
		Alert alert = Assets.hasAlertWithOrigin( this );
		while ( alert!=null ) {
			Assets.removeAlert( alert );
			alert = Assets.hasAlertWithOrigin( this );
		}
	}
	
	/** returns a brief String describing the action they are currently taking */
	public String getDoing() {
		if ( batches.size()>0 )
			if ( getProgress()!= 1.0f ) {
				return "Selling";
			} else
				return "Got your money";
		return "Chilling";
	}
	
	/**
	 * returns between 0.0f and 1.0f
	 * 	
	 */
	@Override
	public float getProgress() {
		if ( batches.size()>0 ) {
			float sold = 0.0f;
			float total = 0.0f;
			Iterator<Batch> batchIt = batches.iterator();
			while ( batchIt.hasNext() ) {
				Batch currentBatch = batchIt.next();
				sold += currentBatch.sold_weight;
				total += currentBatch.getWeight();
			}
			return sold/total;
		}
		return 0.0f;
	}
	
	/**
	 * returns (in seconds) the amount of time until done selling:
	 */
	public int getTimeLeft() {
		if ( batches.size()>0 ) {
			float time_left = 0.0f;
			Iterator<Batch> batchIt = batches.iterator();
			while ( batchIt.hasNext() ) {
				Batch currentBatch = batchIt.next();
				time_left += (currentBatch.getWeight() - currentBatch.sold_weight)/speed;
			}
			return (int) time_left;
		}
		return -1;
	}
	@Override
	public String getRightText() {
		int timeLeft = getTimeLeft();
		if ( timeLeft>0 && batches.size()>0 ) {
			int minutes = (int) ((float)timeLeft/60.0f);
			int hours = (int) ((float)minutes/60.0f);
			minutes = minutes%60;
//			int seconds = timeLeft - hours*60*60 - minutes * 60;
			return "" + ( hours>0? (hours + "h "): "" ) + ( (minutes==0 && hours<1)? "<1m": (minutes + "m") );
		}
		return "";
	}
	
	@Override
	public String getCenterText() {
		float progress = getProgress();
		if ( batches.size()>0 && progress!=1.0f ) {
			
			return "%" + ((int)(progress*100.0f)) + "." + ((int)(progress*1000.0f%10) );
		}
		return getDoing();
	}
}
