package com.bytesbrothers.shroomtycoon.structures;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.WeighPart;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Progressable;

import java.util.ArrayList;
import java.util.Iterator;

import aurelienribon.tweenengine.TweenAccessor;

public class Batch extends Progressable {
	public StrainInstance strain;
	public ArrayList<Fruit> fruits = new ArrayList<Fruit>();
	
	/** the index of the dehydrator this batch is in if any */
	public int dehydrator = -1;
	public float dehydrator_rate = 0.0f;
	
	/** num of seconds required to dry this batch. About 62 for each body */
	public int max_dry_time;
	/** num of seconds we've been drying */
	public float dry_time = 0;
	
	/** progress of selling */
	public float sold_weight = 0.0f;
	
	public boolean weighed = false;
	public float dried_weight = -1;
	transient public boolean fastForward = false;
	
//	transient public  Alert alert = null;
	transient public boolean needsResize = true;
	
	public Batch() {
		updateMaxDryTime();
	}
	public Batch ( Strain strain ) {
		this( new StrainInstance( strain ) );
	}
	public Batch ( StrainInstance strain ) {
		this();
		this.strain = strain;
		
	}
	public float getWeight() {
		if ( dried_weight == -1 ) {
			float result = 0.0f;
			Iterator<Fruit> iterator = fruits.iterator();
			while ( iterator.hasNext() ) {
				Fruit thisFruit = iterator.next();
				result += thisFruit.weight* ((float)thisFruit.growth)/((float)thisFruit.max_growth);
			}
			dried_weight = result;
		}
		return dried_weight;
	}
	public int getNumFruits() {
		return fruits.size();
	}

	/** updates this shit by one second. What did you expect? */
	public void updateOneSec( ) {
		if ( dehydrator != -1 ) {
			dry_time += dehydrator_rate;
			if ( dry_time>max_dry_time )
				dry_time = max_dry_time;
		}
		if ( fastForward && dry_time>=max_dry_time ) {
			dry_time = max_dry_time;
			fastForward = false;
			needsResize = true;
		}
		
		if ( shouldAlert() ) {
			// alert them that there is a batch
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.origin = this;
			alert.icon = "BatchIcon";
			alert.title = "Batch Complete";
			alert.message = strain.name + " is ready for weighing";
			alert.type = ST.TABLE;
//			alert.screenPart = Assets.getGame().getScreenPart( "Weigh" );
//			alert.partName = "Weigh";
			alert.nextPart = WeighPart.class;
			alert.partObject = this;
//			alert.table = new WeighColumn( this );
			Assets.addAlert( alert );
		}
	}
	
	/** updates this shit by secs. */
	public void updateBy( int secs ) {
		int difference = 0;
		if ( dehydrator != -1 ) {
			dry_time += dehydrator_rate * secs;
			if ( dry_time>max_dry_time ) {
				difference = (int) ((dry_time - max_dry_time)/dehydrator_rate);
				dry_time = max_dry_time;
			}
		}
		if ( fastForward && dry_time>=max_dry_time ) {
			dry_time = max_dry_time;
			fastForward = false;
			needsResize = true;
		}
		
		if ( dehydrator!= -1 && getProgress()==1.0f ) {
			// just completed drying
			Dehydrator dehyd = Assets.player.dehydrators.get( dehydrator );
			int index = dehydrator;
			// if there is a batch that needs to dry put it in this dehydrator
			Iterator<Batch> batchIt2 = Assets.player.batches.iterator();
			while ( batchIt2.hasNext() ) {
				Batch thisBatch = batchIt2.next();
				if ( thisBatch.dehydrator == -1 && !thisBatch.weighed && thisBatch.getProgress()!=1.0f ) {
					thisBatch.changeDehydrator( dehyd, index );
					thisBatch.updateBy( difference );
					break;
				}
			}
			
			changeDehydrator( null, -1 );
		}
		
		if ( shouldAlert() ) {
			// alert them that there is a batch
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.origin = this;
			alert.icon = "BatchIcon";
			alert.title = "Batch Complete";
			alert.message = strain.name + " is ready for weighing";
			alert.type = ST.TABLE;
			alert.nextPart = WeighPart.class;
//			alert.partName = "Weigh";
//			alert.screenPart = Assets.getGame().getScreenPart( "Weigh" );
			alert.partObject = this;
			Assets.addAlert( alert );
		}
	}
	
	public boolean shouldAlert() {

		if ( weighed )
			return false;
		
		if ( getProgress()!=1.0f )
			return false;
		
		// if there is already a maintenance alert then NO
		Alert alert = Assets.hasAlertWithOrigin( this );
		if ( alert!=null )
			return false;
		
//		ShroomClass game = Assets.game;

		// if the player is looking at this jar then NO
//		if ( game.getColumnName().equals( "Weigh" ) && ((WeighColumn)game.currentTable).theBatch.equals(this ) )
		if ( 
//				game.screenHandler.getActivePartHeader().equals( "Weigh" ) && 
				Assets.game.screenHandler.isActivePartObjectEquals( this ) )
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
	
	/** returns time in seconds left of drying if any */
	public int getTimeLeft() {
		if ( dehydrator==-1 || dehydrator_rate==0.0f )
			return -1;
		
		return (int)((float)(max_dry_time - dry_time)/dehydrator_rate);
	}
	
	/** returns between 0.0f and 1.0f depending on dryness */
	@Override
	public float getProgress() {
		return dry_time/(float)max_dry_time;
	}
	
	public void changeDehydrator( Dehydrator dehydrator, int index ) {
		if ( !fastForward )
			needsResize = true;
		if( dehydrator!=null) {
			dehydrator_rate = dehydrator.rate;
			this.dehydrator = index;
		} else {
			dehydrator_rate = 0.0f;
			this.dehydrator = -1;
		}
	}
	
	public void updateMaxDryTime() {
		max_dry_time = fruits.size() * 62;
	}
	public void addFruit( Fruit newFruit ) {
		fruits.add( newFruit );
		updateMaxDryTime();
	}
	
	static public class BatchTween implements TweenAccessor<Batch> {
		public static final int DRY_TIME = 1;
		public static final int SOLD_WEIGHT = 2;

		@Override
		public int getValues(Batch target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case DRY_TIME:
				returnValues[0] = target.dry_time;
				return 1;
			case SOLD_WEIGHT:
				returnValues[0] = target.sold_weight;
				return 1;

			default:
				assert false;
				return 0;
			}
		}

		@Override
		public void setValues( Batch target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case DRY_TIME: target.dry_time = newValues[0]; break;
			case SOLD_WEIGHT: target.sold_weight = newValues[0]; break;

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

}
