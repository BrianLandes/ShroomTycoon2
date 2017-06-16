package com.bytesbrothers.shroomtycoon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.FloatingText;
import com.bytesbrothers.shroomtycoon.elements.Progressable;
import com.bytesbrothers.shroomtycoon.elements.TextAndSeconds;
import com.bytesbrothers.shroomtycoon.pools.FloatingTextPool;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Dealer;
import com.bytesbrothers.shroomtycoon.structures.Dehydrator;
import com.bytesbrothers.shroomtycoon.structures.FruitColor;
import com.bytesbrothers.shroomtycoon.structures.Jar;
import com.bytesbrothers.shroomtycoon.structures.Objective;
import com.bytesbrothers.shroomtycoon.structures.PressureCooker;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;
import com.google.gson.annotations.Since;

public class ShroomPlayer extends Progressable {
	
	@Since(1.2) public int slot = -1; // for cloud saving, occupies slots 1-4
	@Since(1.3) public String uniqueSaveName = "snapshotTemp"; // for snapshot saving
	
	@Since(1.1) public String name = "";
	@Since(1.1) public Calendar last_save = null;
//	@Since(1.1) public float playerVersion = 1.1f;
	
//	private transient ShroomClass game = null;
	
	@Since(1.1) public ArrayList<Jar> jars = new ArrayList<Jar>();
	@Since(1.1) public ArrayList<Casing> casings = new ArrayList<Casing>();
	@Since(1.1) public ArrayList<Dealer> dealers = new ArrayList<Dealer>();
	@Since(1.1) public ArrayList<Dehydrator> dehydrators = new ArrayList<Dehydrator> ();
	@Since(1.1) public ArrayList<PressureCooker> cookers = new ArrayList<PressureCooker> ();
	
	// resources
	@Since(1.1) public BigInteger cash = BigInteger.valueOf( 0 );
	@Since(1.1) public ArrayList<Batch> batches = new ArrayList<Batch> ();
	@Since(1.1) public int grain_jars = 10;
	@Since(1.1) public int[] sub_jars = { 0, 0, 1, 0};
	@Since(1.1) public int coins = 10;
	@Since(1.1) public ArrayList<Strain> syringes = new ArrayList<Strain>();
	@Since(1.1) public HashMap< String, Integer > syringesCompressed = new HashMap< String, Integer >();
	@Since(1.1) public ArrayList<String> can_buy_strains = new ArrayList<String>();
	/** custom strains*/
	@Since(1.1) public ArrayList<Strain> strains = new ArrayList<Strain>();
	@Since(1.3) public ArrayList<FruitColor> customColors = new ArrayList<FruitColor>();
	/** A list of all downloaded strains.*/
	@Since(1.4) public ArrayList<Strain> serverStrains = new ArrayList<Strain>();
	@Since(1.4) public ArrayList<Integer> serverStrainsDownloaded = new ArrayList<Integer>();
	@Since(1.4) public ArrayList<Integer> serverStrainsThumbed = new ArrayList<Integer>();
//	/** A list of owned and consumable syringes of server strains.*/
//	@Since(1.4) public ArrayList<StrainSQL> serverSyringes = new ArrayList<StrainSQL>();
	
	// naming numbers
	@Since(1.1) public int jar_enum = 1;
	@Since(1.1) public int strain_enum = 1;
	
	// laboratory settings
	@Since(1.1) public int inc_temp = ST.LOW;
	@Since(1.1) public int fc_temp = ST.LOW;
	@Since(1.1) public int fc_humidity = 0;
	@Since(1.1) public int last_gb_sterile_secs = 0;
	@Since(1.1) public boolean permanent_sterile = false;
	@Since(1.1) public int gb_sterile_secs = 0;
	@Since(1.1) public int inc_capacity = 6;
	@Since(1.1) public int fc_capacity = 4;
	@Since(1.1) public int fridge_capacity = 4;
	
	// statistics
	@Since(1.1) public int totalJarsMade = 0;
	@Since(1.1) public int totalCasingsMade = 0;
	@Since(1.1) public BigInteger totalCashMade = BigInteger.valueOf( 0 );
	@Since(1.1) public float totalStashWeighed = 0.0f;
	@Since(1.1) public BigInteger totalCashSpent = BigInteger.valueOf( 0 );
	@Since(1.1) public float totalStashSold = 0.0f;
	@Since(1.1) public int totalBlueCoinsEarned = 0;
	@Since(1.1) public int totalBlueCoinsUsed = 0;
	@Since(1.1) public int totalSideJobsDone = 0;
	@Since(1.1) public int totalObjectivesDone = 0;
	@Since(1.1) public int totalContaminations = 0;
	@Since(1.1) public int totalCooks = 0;
	@Since(1.1) public int totalDealersHired = 0;
	@Since(1.1) public int totalHarvests = 0;
	@Since(1.1) public int totalSyringesMade = 0;
	
	
	// profile options
	@Since(1.1) public boolean useTransitions = true;
	@Since(1.1) public float fc_cam_distance = 3.0f;
	
	// help hints
	@Since(1.1) public boolean useHelpHints = true;
	@Since(1.1) public boolean hintFirstFc = true;
	@Since(1.1) public boolean hintFirstSam = true;
	@Since(1.1) public boolean hintFirstJar = true;
	@Since(1.1) public boolean hintFirstDealers = true;
	@Since(1.1) public boolean hintFirstGloveBox = true;
	@Since(1.1) public boolean hintFirstGloveBoxSettings = true;
	@Since(1.1) public boolean hintFirstHarvest = true;
	
	@Since(1.1) public boolean givenNewPlayerBlueCoins = false;
	
	// xp values
	@Since(1.1) public int current_xp = 0;
	@Since(1.1) public int last_level = 0;
	@Since(1.1) public int next_level = 1;
	@Since(1.1) public int level = 1;
	
	// time values
	@Since(1.1) public int days = 0;
	@Since(1.1) public int hours = 0;
	@Since(1.1) public int mins = 0;
	@Since(1.1) public int seconds = 0;
	
	// sam and his/her loan
	@Since(1.1) public int debt = ST.SAMS_LOAN;
	@Since(1.1) public boolean half_paid = false;
	@Since(1.1) public boolean useSamsBackground = false;
	@Since(1.3) public int samBonusTimer = 0; // time left until next bonus
//	transient public Alert samAlert = null;
	@Since(1.3) public boolean full_paid = false;
	@Since(1.3) public boolean firstBonus = true;
	@Since(1.3) public boolean newGame = false;
	
	// cheats
	@Since(1.1) public boolean usedFreeHundred = false;
	
	// use notifications
	public boolean useNotifications = true;
	
	/**
	 * random event variables
	 * 
	 */
	@Since(1.1) public int reg_last_batch = -1; // -1 None, 0 is Bad, 1 is okay, 2 is great
	@Since(1.1) public String reg_dealer_name = "";
	@Since(1.1) public String reg_discount = ""; //"" if none, name of strain if discount
	@Since(1.1) public float reg_discount_percent = 0.0f;
	@Since(1.1) public int reg_discount_timer = 0;
	@Since(1.1) public transient Alert objective_alert = null; // for when the objective is given, not when the objective is completed
	@Since(1.1) public Objective objective = null;
	@Since(1.1) public boolean has_reg_ocean_body = false;
	@Since(1.1) public boolean has_reg_fun_guy = false;
	
	
	@Since(1.3) public boolean hasStamets = false;
	
	public ShroomPlayer() {
		
	}
	
//	public ShroomPlayer( ShroomClass game ) {
//		setGame( game );
//	}
	
	/** use this to ensure that the batch is put into a dehydrator */
	public void addBatch( Batch newBatch ) {
		batches.add( newBatch );
		newBatch.changeDehydrator( null, -1 );
		if ( newBatch.getProgress()!=-1 ) {
			Iterator<Dehydrator> dehydIt = dehydrators.iterator();
			int index = 0;
			while ( dehydIt.hasNext() ) {
				Dehydrator dehydrator = dehydIt.next();
				// count the batches inside this dehydrator
				int num_inside = 0;
				Iterator<Batch> batchIt = batches.iterator();
				while ( batchIt.hasNext() ) {
					Batch thisBatch = batchIt.next();
					if ( thisBatch.dehydrator == index ) {
						num_inside ++;
					}
				}
				// if there is still room in this dehydrator, put it in there
				if ( num_inside<dehydrator.capacity ) {
					newBatch.changeDehydrator( dehydrator, index );
					// and we're done
					return;
				}
				index ++;
			}
		}
	}
	
	/** creates and adds one new dehydrator and fills it with any batches that need to dry */
	public void addDehydrator() {
		Dehydrator dehydrator = new Dehydrator();
		dehydrators.add( dehydrator );
		int num = 0;
		int index = dehydrators.size() - 1;
		Iterator<Batch> batchIt = batches.iterator();
		while ( batchIt.hasNext() && num<dehydrator.capacity ) {
			Batch thisBatch = batchIt.next();
			if ( thisBatch.dehydrator == -1 && thisBatch.getProgress()!=1.0f ) {
				thisBatch.changeDehydrator( dehydrator, index );
			}
		}
	}
	
	public boolean canBuyStrain( String strainName, boolean checkCustoms ) {
		
		for( String strain: can_buy_strains ) {
			if ( strainName.equals( strain ) )
				return true;
		}
		
		if ( checkCustoms ) {
			for( Strain strain: strains ) {
				if ( strainName.equals( strain.name ) )
					return true;
			}
		}
		
		return false;
	}
	
	/** checks and returns whether or not this player can use the fridge yet */
	public boolean canFridge() {
		if ( level>=5 )
			return true;
		return false;
	}
	
	/** checks and returns whether or not this player can use the G2G(3) yet */
	public boolean canG2g3() {
		if ( level>=19 )
			return true;
		return false;
	}
	
	/** checks and returns whether or not this player can use the G2G(5) yet */
	public boolean canG2g5() {
		if ( level>=27 )
			return true;
		return false;
	}
	
	/** checks and returns whether or not this player can soak yet */
	public boolean canSoak() {
		if ( level>=13 )
			return true;
		return false;
	}
	
	/** checks and returns whether or not this player can use the substrate */
	public boolean canSubstrate( int substrate ) {
		switch( substrate ) {
		case ST.COCO_COIR:
			return true;
		case ST.STRAW:
			if ( level>=9 )
				return true;
			return false;
		case ST.DUNG:
			if ( level>=22 )
				return true;
			return false;
		case ST.WOOD_CHIPS:
			if ( level>=17 )
				return true;
			return false;
		}
		return false;
	}
	/** returns true if there is at least 1 upgrade to do in the incubator or fc*/
	public boolean canUpgradeIncOrFc() {
		if ( inc_temp!=ST.HIGH )
			return true;
		if ( fc_temp!=ST.HIGH )
			return true;
		if ( fc_humidity!=4 )
			return true;
		return false;
	}
	
	
	/** returns true if there is at least 1 upgrade to do in a pressure cooker*/
	public boolean canUpgradePc() {
		Iterator<PressureCooker> pcIt = cookers.iterator();
		while ( pcIt.hasNext() ) {
			PressureCooker cooker = pcIt.next();
			if ( cooker.capacity< getMaxPcUpgrade() && cooker.capacity<3 )
				return true;
		}
		
		return false;
	}
	/** change cash by amount and displays floating text*;
	 */
	public void changeCash( int amount ) {
		cash = cash.add( BigInteger.valueOf(amount) );
		final Skin skin = Assets.getSkin();
		if ( Assets.game==null)
			System.out.println( "changeCash: game == null " );

		if ( Assets.game.fTextPool==null )
			Assets.game.fTextPool = new FloatingTextPool();
		FloatingText fText = Assets.game.fTextPool.borrowObject();
		fText.setText( (amount>0?"+":"-") + "$" + ST.withComas(Math.abs(amount)) );
		fText.setStyle( skin.get( "Cash", Label.LabelStyle.class ) );
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		fText.setWidth( w * 0.25f );
//		fText.setHeight( h * 0.1f );
		fText.setX( 20 );
		fText.setY( Assets.height/9.0f );
		
		Assets.game.addFloatingText( fText );
		
//		Tween.to( fText, FloatingTextTween.Y, 1.0f )
//        .target( h/9.0f * 2.0f )
//        .ease(TweenEquations.easeOutQuad)
//        .start( Assets.getTweenManager() ); //** start it
//		
//		Tween.to( fText, FloatingTextTween.ALPHA, 1.0f )
//        .target( 0.0f )
//        .delay( 0.7f )
//        .ease(TweenEquations.easeInQuad)
//        .start( Assets.getTweenManager() ); //** start it
//		
//		fText.failSafe = 2.0f;
		
		if ( amount>0 ) {
			totalCashMade = totalCashMade.add( BigInteger.valueOf( amount ) );
		} else {
			totalCashSpent = totalCashSpent.subtract( BigInteger.valueOf( amount ) );
		}
		
		// earned $1,000
    	if ( totalCashMade.intValue() >=1000 )
    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQDQ" );
    	
    	// earned $10,000
    	if ( totalCashMade.intValue() >=10000 )
    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQDg" );
    	
    	// earned $100,000
    	if ( totalCashMade.intValue() >=100000 )
    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQDw" );
	}
	
	/** change cash by amount and displays floating text*;
	 */
	public void changeCoins( int amount ) {
		coins += amount;
		final Skin skin = Assets.getSkin();
		if ( Assets.game.fTextPool==null )
			Assets.game.fTextPool = new FloatingTextPool();
		FloatingText fText = Assets.game.fTextPool.borrowObject();
		fText.setText( "" + amount + "BC" );
		fText.setStyle( skin.get( "Coins", Label.LabelStyle.class ) );
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		fText.setWidth( w *0.25f );
//		fText.setHeight( h * 0.1f );
		fText.setX( Assets.width * 0.5f );
		fText.setY( Assets.height/9.0f );
		Assets.game.addFloatingText( fText );
//		Tween.to( fText, FloatingTextTween.Y, 1.0f )
//        .target(  h/9.0f * 2.0f )
//        .ease(TweenEquations.easeOutQuad)
//        .start( Assets.getTweenManager() ); //** start it
//		
//		Tween.to( fText, FloatingTextTween.ALPHA, 1.0f )
//        .target( 0.0f )
//        .delay( 0.7f )
//        .ease(TweenEquations.easeInQuad)
//        .start( Assets.getTweenManager() ); //** start it
//		
//		fText.failSafe = 2.0f;
		
		if ( amount>0 ) {
			totalBlueCoinsEarned += amount;
		} else {
			totalBlueCoinsUsed -= amount;
		}
		
		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQEQ" );
		
//		System.out.println( "Player Change coins: " + amount );
	}
	
	/** returns total capacity of Fruiting Chamber */
	public int fcSize() {
		return fc_capacity;
	}
	
	public BigInteger getBigIntegerCash() {
		return cash;
	}
	
	/** returns either the int value of cash or 2 billion, whichever is lower */
	public int getCash() {
		if ( cash.compareTo( BigInteger.valueOf( 2000000000 ) ) > 0 )
			return 2000000000;
		return cash.intValue();
	}
	
	/** returns a formated version of cash */
	public String getCashString() {
		if ( cash.compareTo( BigInteger.valueOf( 2000000000 ) ) > 0 )
			return cash.toString();
		return ST.withComas( cash.intValue() );
	}
	
	int lastLevel = -1;
	String lastLevelCenterText = "";
	
	@Override
	public String getCenterText() {
		if ( lastLevel == level )
			return lastLevelCenterText;
		lastLevelCenterText = "Level: " + level;
		lastLevel = level;
		return lastLevelCenterText;
	}
	
	/** returns the cost in Blue Coins of buying an additional dehydrator */
	public int getDehydIncreaseCost() {
		return (dehydrators.size()-1)*3 + 6;
	}
	
	/** returns the cost in Blue Coins of increasing the Fruiting Chamber capacity */
	public int getFcCapacityIncreaseCost() {
		return (fc_capacity-4)*2 + 4;
	}
	
	/** returns the cost in Blue Coins of increasing the fridge capacity */
	public int getFridgeCapacityIncreaseCost() {
		return (fridge_capacity-4)*2 + 3;
	}
	
	/** returns a percentage ( 0.0 - 1.0f ) of sterility for new jars and casings */
	public float getGbSterility() {
		if ( gb_sterile_secs>0 || permanent_sterile )
			return 1.1f;
		return 0.85f;
	}
	
	public Progressable getHumidity() {
		return new Progressable() {

			public String getCenterText() {
				return "Humidity in use";
			}
			
			@Override
			public float getProgress() {
				return Math.min( (float)getNeededHumidity()/(float)ST.humidities[ fc_humidity ], 1.0f );
			}
			
			public String getRightText() {
				return "";
			}
		};
	}
	
	/** returns the cost in Blue Coins of increasing the Incubator capacity */
	public int getIncCapacityIncreaseCost() {
		return (inc_capacity-6)*2 + 3;
	}
	
	/** returns a list of amounts of syringes in order by strain */
	public int[] getItemizedSyringes() {
		int[] list = new int[ this.strains.size() + StrainMaster.getStrains().size() + serverStrains.size() ];
		
		// go through common strains first
		int size = StrainMaster.getStrains().size();
		for ( int i = 0; i < size; i++ ) {
			String currentStrain = StrainMaster.getStrains().get( i ).name;
			int total = 0;
			Iterator<Strain> syringeIt = syringes.iterator();
			while ( syringeIt.hasNext() ) {
				Strain syringe = syringeIt.next();
				if ( currentStrain.equals( syringe.name ) ) {
					total ++;
				}
			}
			
			// add compressed number
			Integer number = syringesCompressed.get( currentStrain );
			if ( number!=null )
				total += number;
			
			list[i] = total;
		}
		// go through custom strains
		int custom_size = this.strains.size();
		for ( int i = 0; i < custom_size; i++ ) {
			String currentStrain = this.strains.get( i ).name;
			int total = 0;
			Iterator<Strain> syringeIt = syringes.iterator();
			while ( syringeIt.hasNext() ) {
				Strain syringe = syringeIt.next();
				if ( currentStrain.equals( syringe.name ) ) {
					total ++;
				}
			}
			
			// add compressed number
			Integer number = syringesCompressed.get( currentStrain );
			if ( number!=null )
				total += number;
			
			list[ i + size ] = total;
		}
		
//		// go through server strains
		int serverSize = this.serverStrains.size();
		for ( int i = 0; i < serverSize; i++ ) {
			String currentStrain = this.serverStrains.get( i ).name;
			int total = 0;
			Iterator<Strain> syringeIt = syringes.iterator();
			while ( syringeIt.hasNext() ) {
				Strain syringe = syringeIt.next();
				if ( currentStrain.equals( syringe.name ) ) {
					total ++;
				}
			}
			
			// add compressed number
			Integer number = syringesCompressed.get( currentStrain );
			if ( number!=null )
				total += number;
			
			list[ i + size + custom_size ] = total;
		}
		return list;
	}
	
	/** returns max rating of dealers player can get */
	public int getMaxDealerRating() {
		if ( level<2 )
			return 1;
		else if ( level<4 )
			return 2;
		else if ( level<10 )
			return 3;
		else if ( level<12 )
			return 4;
		else if ( level<18 )
			return 5;
		else if ( level<20 )
			return 6;
		else if ( level<23 )
			return 7;
		else if ( level<28 )
			return 8;
		return 9;
	}
	
	/** returns max number of dealers player can have */
	public int getMaxDealers() {
		if ( level<7 )
			return 1;
		else if ( level<15 )
			return 2;
		else if ( level<26 )
			return 3;
		return 4;
	}
	
	/** returns max rating of dealers player can get */
	public int getMaxPcUpgrade() {
		if ( level<6 )
			return 0;
		else if ( level<14 )
			return 1;
		else if ( level<24 )
			return 2;
		
		return 3;
	}
	
	/**
	 * returns the int of humidity required by the casings currently
	 */
	public int getNeededHumidity() {
		Iterator<Casing> iterator = casings.iterator();
		int total = 0;
		while ( iterator.hasNext() ) {
			Casing currentCasing = iterator.next();
			if ( currentCasing.location==ST.FC )
				total += currentCasing.strain.humidity_cost;
		}
		
		return total;
	}
	
	/** returns the total amount of syringes
	 * 
	 */
	public int getNumSyringes() {
		int num = 0;
		Set<String> syringeNames = Assets.player.syringesCompressed.keySet();
		for ( String thisString: syringeNames ) {
			Integer thisSyringeNum = Assets.player.syringesCompressed.get( thisString );
			num += thisSyringeNum;
		}
		return num + syringes.size();
	}
	
	
	
	/** returns the amount of syringes of this strain
	 * 
	 */
	public int getNumSyringes( String strainName ) {
		int num = 0;
		Iterator<Strain> strainIt = syringes.iterator();
		while ( strainIt.hasNext() ) {
			Strain thisSyringe = strainIt.next();
			if ( thisSyringe.name.equals( strainName ) )
				num ++;
		}
		
		// add compressed number
		Integer number = syringesCompressed.get( strainName );
		if ( number!=null )
			num += number;
		
		return num;
	}
	
	/** returns the cost in Blue Coins of buying an additional pressure cookers */
	public int getPcIncreaseCost() {
		return (cookers.size()-1)*3 + 5;
	}
	
	@Override
	public float getProgress() {
		return (float)(current_xp-last_level)/(float)(next_level-last_level);
	}
	
	/** returns a random casing in the given location */
	public Casing getRandomCasing( int location ) {
		if ( numCasings( location)<=0 )
			return null;
		Casing theCasing = null;
		Random random = new Random();
		boolean found_one = false;
		while ( !found_one ) {
			theCasing = casings.get( random.nextInt( casings.size() ) );
			if ( theCasing.location == location || location== -1 )
				found_one = true;
		}
		return theCasing;
	}
	
	/** returns a random jar in the given location */
	public Jar getRandomJar( int location ) {
		if ( numJars( location)<=0 )
			return null;
		Jar theJar = null;
		Random random = new Random();
		boolean found_one = false;
		while ( !found_one ) {
			theJar = jars.get( random.nextInt( jars.size() ) );
			if ( theJar.location == location || location== -1 )
				found_one = true;
		}
		return theJar;
	}
	
	@Override
	public String getRightText() {
		return "" + current_xp + "/" + next_level+ " xp";
	}
	
	/** returns total amount of stash
	 *   only counts batches that are dried
	 */
	public float getStash() {
		float stash = 0.0f;
		
		Iterator<Batch> iterator = batches.iterator();
		while ( iterator.hasNext() ){
			Batch currentBatch = iterator.next();
			
			if ( currentBatch.weighed ) {
				stash += currentBatch.getWeight();
			}
		}
		
		return stash;
	}
	
	public int getSubstrateJars() {
		return sub_jars[0] + sub_jars[1] + sub_jars[2] + sub_jars[3];
	}
	
	/** returns a formated version of totalCashMade */
	public String getTotalCashMadeString() {
		if ( totalCashMade.compareTo( BigInteger.valueOf( 2000000000 ) ) > 0 )
			return totalCashMade.toString();
		return ST.withComas( totalCashMade.intValue() );
	}
	
	/** returns a formated version of totalCashSpent */
	public String getTotalCashSpentString() {
		if ( totalCashSpent.compareTo( BigInteger.valueOf( 2000000000 ) ) > 0 )
			return totalCashSpent.toString();
		return ST.withComas( totalCashSpent.intValue() );
	}
	/** returns a brief string about the unlock at the indicated level */
	public String getUnlockAtLevel( int lvl ) {
		switch ( lvl ) {
		case 1: return "";
		case 2: return "Two star dealers are now available.";
		case 3: return "Unlocked new strain: Quick Cubes.";
		case 4: return "Three star dealers are now available.";
		case 5: return "You have unlocked the Fridge.";
		case 6: return "Upgrade 1 for the Pressure Cooker is now available.";
		case 7: return "You can now have up to 2 dealers.";
		case 8: return "Unlocked new strain: Treasure Coast.";
		case 9: return "You have unlocked a new substrate: Straw.";
		case 10: return "Four star dealers are now available.";
		case 11: return "Unlocked new strain: Albino Envy.";
		case 12: return "Five star dealers are now available.";
		case 13: return "You have unlocked an ability: Soaking.";
		case 14: return "Upgrade 2 for the Pressure Cooker is now available.";
		case 15: return "You can now have up to 3 dealers.";
		case 16: return "Unlocked new strain: Z Strain.";
		case 17: return "You have unlocked a new substrate: Wood Chips.";
		case 18: return "Six star dealers are now available.";
		case 19: return "You have unlocked an ability: G2G (x3).";
		case 20: return "Seven star dealers are now available.";
		case 21: return "Unlocked new strain: Cambodian.";
		case 22: return "You have unlocked a new substrate: Dung.";
		case 23: return "Eight star dealers are now available.";
		case 24: return "Upgrade 3 for the Pressure Cooker is now available.";
		case 25: return "Unlocked new strain: Super M.";
		case 26: return "You can now have up to 4 dealers.";
		case 27: return "You have unlocked an ability: G2G (x5).";
		case 28: return "Nine star Dealers are now available.";
		case 29: return "Unlocked new strain: Jeepers Creepers.";
		default: return "You have been awarded 1 Blue Coin.";
		
		}
	}
	/**
	 * returns true if FC has enough humidity to support all the casings inside
	 */
	public boolean hasHumidity() {
		Iterator<Casing> iterator = casings.iterator();
		int total = 0;
		while ( iterator.hasNext() ) {
			Casing currentCasing = iterator.next();
			if ( currentCasing.location==ST.FC ) {
				total += currentCasing.strain.humidity_cost;
				if ( total>ST.humidities[fc_humidity] )
					return false;
			}
		}
		
		return true;
	}
	
	/** returns true if the player has at least one syringe of this strain */
	public boolean hasSyringe( Strain strain ) {
		return hasSyringe( strain.name );
	}
	
	/** returns true if the player has at least one syringe of this strain */
	public boolean hasSyringe( String strain ) {
		Integer number = syringesCompressed.get( strain );
		if ( number!=null && number>0 )
			return true;
		
		Iterator<Strain> strainIt = syringes.iterator();
		while ( strainIt.hasNext() ) {
			Strain thisSyringe = strainIt.next();
			if ( thisSyringe.name.equals( strain ) )
				return true;
		}
		return false;
	}
	
	/** increases XP by amount, increases level by one if reached next level, sets next level XP *;
	 */
	public void increaseXP( int xp ) {
		current_xp += xp;
		final Skin skin = Assets.getSkin();
		if ( Assets.game.fTextPool==null )
			Assets.game.fTextPool = new FloatingTextPool();
		FloatingText fText = Assets.game.fTextPool.borrowObject();
		fText.setText( "+" + xp + "xp" );
		fText.setStyle( skin.get( "XP", Label.LabelStyle.class ) );
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		fText.setWidth( w * 0.25f );
//		fText.setHeight( h * 0.1f );
		fText.setX( Assets.width * 0.72f );
		fText.setY( Assets.height/9.0f );
		Assets.game.addFloatingText( fText );
//		Tween.to( fText, FloatingTextTween.Y, 1.0f )
//        .target(  h/9.0f * 2.0f )
//        .ease(TweenEquations.easeOutQuad)
//        .start( Assets.getTweenManager() ); //** start it
//		
//		Tween.to( fText, FloatingTextTween.ALPHA, 1.0f )
//        .target( 0.0f )
//        .delay( 0.7f )
//        .ease(TweenEquations.easeInQuad)
//        .start( Assets.getTweenManager() ); //** start it
//		
//		fText.failSafe = 2.0f;
		
		while ( current_xp >= next_level ) {
			level += 1;
			last_level = next_level;
			next_level += 100 + next_level * 0.1f;
			
			Dialogs.levelDialog( "You have reached level " + level + "! " +
				getUnlockAtLevel( level ) );
//			Alert alert = new Alert( game );
//			alert.timer = 10*60; // 10 minutes
//			alert.icon = "LevelUpIcon";
//			alert.title = "Level Up";
//			alert.message = "You have reached level " + level + "!";
//			alert.screen = new PlayerScreen( game );
//			game.addAlert( alert );
			
			switch( level ) {
			default:
				break;
			case 3: can_buy_strains.add( "Quick Cubes" ); break;
			case 8: can_buy_strains.add( "Treasure Coast" ); break;
			case 11: can_buy_strains.add( "Albino Envy" ); break;
			case 16: can_buy_strains.add( "Z Strain" ); break;
			case 21: can_buy_strains.add( "Cambodian" ); break;
			case 25: can_buy_strains.add( "Super M" ); break;
			case 29: can_buy_strains.add( "Jeepers Creepers" ); break;
//				can_buy_strains.add( "Ocean Body" );
//				can_buy_strains.add( "Business Man" );
//				can_buy_strains.add( "Fun Guy" );
			}
			if ( level>30 ) {
				changeCoins( 1 );
			}
		}
	}
	
	/** returns total capacity of Incubator */
	public int incubatorSize() {
		return inc_capacity;
	}
	
	public void newPlayer() {
		name = "";
		
		jars = new ArrayList<Jar>();
		casings = new ArrayList<Casing>();
		
		// resources
		cash = BigInteger.valueOf( 1000 );
		batches = new ArrayList<Batch> ();
		grain_jars = 5;
		sub_jars[0] = 5; sub_jars[1] = 0; sub_jars[2] = 0; sub_jars[3] = 0;
		coins = 0;
		syringes = new ArrayList<Strain>();
		syringesCompressed.clear();
		
		// naming numbers
		jar_enum = 1;
		Random random = new Random();
		strain_enum = random.nextInt(28) + 1;
		
		// laboratory settings
		inc_temp = ST.LOW;
		fc_temp = ST.LOW;
		fc_humidity = 0;
		last_gb_sterile_secs = 60*60; // we'll give new players 60 minutes before they have to deal with cont
		gb_sterile_secs = 60*60; // time in seconds that player will be %100 sterile
		inc_capacity = 6;
		fc_capacity = 4;
		fridge_capacity = 4;
		
		// time
		days = 0;
		hours = 0;
		mins = 0;
		seconds = 0;
		
		// level
		level = 1;
		current_xp = 0;
		last_level = 0;
		next_level = 100;
		
		useHelpHints = true;
		hintFirstFc = true;
		hintFirstSam = true;
		hintFirstJar = true;
		hintFirstDealers = true;
		hintFirstGloveBox = true;
		hintFirstGloveBoxSettings = true;
		hintFirstHarvest = true;
		
		givenNewPlayerBlueCoins = false;
		
		// statistics
		totalJarsMade = 0;
		totalCasingsMade = 0;
		totalCashMade = BigInteger.valueOf( 0 );
		totalStashWeighed = 0.0f;
		totalCashSpent = BigInteger.valueOf( 0 );
		totalStashSold = 0.0f;
		totalBlueCoinsEarned = 0;
		totalBlueCoinsUsed = 0;
		totalSideJobsDone = 0;
		totalObjectivesDone = 0;
		totalContaminations = 0;
		totalCooks = 0;
		totalDealersHired = 0;
		totalHarvests = 0;
		totalSyringesMade = 0;
		
		// sam
		debt = ST.SAMS_LOAN;
		half_paid = false;
		useSamsBackground = false;
		
		dehydrators = new ArrayList<Dehydrator> ();
		dehydrators.add( new Dehydrator() );
		
		cookers = new ArrayList<PressureCooker> ();
		cookers.add( new PressureCooker( Assets.game ) );
		
		dealers = new ArrayList<Dealer> ();
		
		// give them one fully grown jar of either Golden Teacher, B+, or Ecuador
		Strain newStrain = StrainMaster.getStrain( "Golden Teacher" );
		switch( random.nextInt( 4 ) ) {
		case 0:
			break;
		case 1:
			newStrain = StrainMaster.getStrain( "B+" );
			break;
		case 2:
			newStrain = StrainMaster.getStrain( "Acadian Coast" );
			break;
		case 3:
			newStrain = StrainMaster.getStrain( "Costa Rica" );
			break;
		}
		Jar newJar = new Jar( newStrain.name + " 00", newStrain, Assets.game );
		while ( newJar.getTimeLeft()>0 )
			newJar.updateOneSec();
		
		jars.add( newJar );
		
		can_buy_strains = new ArrayList<String>();
		can_buy_strains.add( "Golden Teacher" );
		can_buy_strains.add( "B+" );
		can_buy_strains.add( "Acadian Coast" );
		can_buy_strains.add( "Costa Rica" );
		can_buy_strains.add( "Lizard King" );
		can_buy_strains.add( "Ecuador" );
		can_buy_strains.add( "Philosopher's Stone" );
		can_buy_strains.add( "India Orissa" );
//		can_buy_strains.add( "Super M" );
//		can_buy_strains.add( "Albino Envy" );
//		can_buy_strains.add( "Z Strain" );
//		can_buy_strains.add( "Ocean Body" );
//		can_buy_strains.add( "Business Man" );
//		can_buy_strains.add( "Fun Guy" );
//		can_buy_strains.add( "Jeepers Creepers" );
//		can_buy_strains.add( "Quick Cubes" );
//		can_buy_strains.add( "Cambodian" );
//		can_buy_strains.add( "Treasure Coast" );

		last_save = Calendar.getInstance();
	}
	
	/** returns total casings or, if given a location, total casings in location */
	public int numCasings( int location ) {
		if ( location == -1 )
			return casings.size();
		// count jars in Inc
  		Iterator<Casing> casingIt = casings.iterator();
  		int num_casings = 0;
        while ( casingIt.hasNext() ) {
        	Casing thisCasing = casingIt.next();
        	
        	if ( thisCasing.location == location ) {
        		num_casings ++;
        	}
        }
        return num_casings;
	}
	
	/** returns total jars or, if given a location, total jars in location */
	public int numJars( int location ) {
		if ( location == -1 )
			return jars.size();
		// count jars in Inc
  		Iterator<Jar> jarIt = jars.iterator();
  		int num_jars = 0;
        while ( jarIt.hasNext() ) {
        	Jar thisJar = jarIt.next();
        	
        	if ( thisJar.location == location ) {
        		num_jars ++;
        	}
        }
        return num_jars;
	}

	/** removes 1 syringe with the matching strain name */
	public boolean removeSyringe( String strainName ) {
		Iterator<Strain> strainIt = syringes.iterator();
		while ( strainIt.hasNext() ) {
			Strain thisSyringe = strainIt.next();
			if ( thisSyringe.name.equals( strainName ) ) {
				strainIt.remove();
				return true;
			}
		}
		
		Integer number = syringesCompressed.get( strainName );
		if ( number==null || number<=0 )
			return false;
		number --;
		syringesCompressed.put( strainName, number );
		
		return false;
	}

//	public void setGame( ShroomClass game ) {
//		this.game = game;
		// jars
//		Iterator<Jar> jarIt = jars.iterator();
//		while ( jarIt.hasNext() ) {
//			Jar jar = jarIt.next();
//			jar.game = game;
//		}
//		// casings
//		Iterator<Casing> casingIt = casings.iterator();
//		while ( casingIt.hasNext() ) {
//			Casing casing = casingIt.next();
//			casing.game = game;
//		}
//		
//		// dealers
//		Iterator<Dealer> dealIt = dealers.iterator();
//		while ( dealIt.hasNext() ) {
//			Dealer dealer = dealIt.next();
//			dealer.game = game;
//		}
//		
//		// pressure cookers
//		Iterator<PressureCooker> pcIt = cookers.iterator();
//		while ( pcIt.hasNext() ) {
//			PressureCooker cooker = pcIt.next();
//			cooker.game = game;
//		}
//		
		// dehydrators/batches
//		Iterator<Batch> batchIt = batches.iterator();
//		while ( batchIt.hasNext() ) {
//			Batch batch = batchIt.next();
//			batch.game = game;
//		}
//	}

	/** updates everything owned by the player by 'secs'
	 * */
	public void updateBy( int secs ) {
		// time
		seconds += secs;
		while ( seconds>=60 ) { seconds -= 60; mins += 1; }
		while ( mins>=60 ) { mins -= 60; hours += 1; }
		while ( hours>=24 ) { hours -= 24; days += 1; }
		
		// Sam
		samBonusTimer -= secs;
		
		// if they purchased sterility, reduce it by secs
		if ( gb_sterile_secs>0 ) {
		
			gb_sterile_secs -= secs;
			
			if ( gb_sterile_secs<=0 ) {
				gb_sterile_secs = 0;
				// alert them that there may be contamination
				Alert alert = Assets.alertPool.borrowObject();
				alert.reset();
				alert.uniqueID = ST.ALERT_GLOVE_BOX;
				alert.icon = "GloveBoxIcon";
				alert.title = "Need Air Filter";
				alert.message = "Sterility at %78";
				alert.type = ST.MAIN_COLUMN;
				alert.mainColumn = MainPart.GLOVE_BOX_SETTINGS;

				Assets.addAlert( alert );
			}
		}
		
		// jars
//		Iterator<Jar> jarIt = jars.iterator();
//		while ( jarIt.hasNext() ) {
//			Jar jar = jarIt.next();
		for ( Jar jar: jars ) {
			jar.updateBy( secs );
			
		}
		// casings
//		Iterator<Casing> casingIt = casings.iterator();
//		while ( casingIt.hasNext() ) {
//			Casing casing = casingIt.next();
		for ( Casing casing: casings ) {
			casing.updateBy(secs);
			
			//casing.updateOneSec();
		}
		
		// dealers
//		Iterator<Dealer> dealIt = dealers.iterator();
//		while ( dealIt.hasNext() ) {
//			Dealer dealer = dealIt.next();
		for ( Dealer dealer: dealers ) {
			dealer.updateBy( secs );
//			dealer.updateOneSec();
		}
		
		// pressure cookers
//		Iterator<PressureCooker> pcIt = cookers.iterator();
//		while ( pcIt.hasNext() ) {
//			PressureCooker cooker = pcIt.next();
		for ( PressureCooker cooker: cookers ) {
			cooker.updateBy( secs );
//			cooker.updateOneSec();
		}
		
		// dehydrators/batches
//		Iterator<Batch> batchIt = batches.iterator();
//		while ( batchIt.hasNext() ) {
//			Batch batch = batchIt.next();
		for ( Batch batch: batches ) {
			batch.updateBy( secs );
//			batch.updateOneSec( );
//			if ( batch.dehydrator!= -1 && batch.getProgress()==1.0f ) {
//				// just completed drying
//				Dehydrator dehydrator = dehydrators.get( batch.dehydrator );
//				int index = batch.dehydrator;
//				// if there is a batch that needs to dry put it in this dehydrator
//				Iterator<Batch> batchIt2 = batches.iterator();
//				while ( batchIt2.hasNext() ) {
//					Batch thisBatch = batchIt2.next();
//					if ( thisBatch.dehydrator == -1 && !thisBatch.weighed && thisBatch.getProgress()!=1.0f ) {
//						thisBatch.changeDehydrator( dehydrator, index );
//						break;
//					}
//				}
//				
//				batch.changeDehydrator( null, -1 );
//			}
		}

		return;
	}
	
	public ArrayList<TextAndSeconds> getAlarms() {
//		TreeSet<TextAndSeconds> alarms = new TreeSet<TextAndSeconds>( new TextAndSeconds.TextAndSecondsComparator() );
		ArrayList<TextAndSeconds> alarms = new ArrayList<TextAndSeconds>( );
		
		TextAndSeconds ts = null;
		
		 // sam
		 if ( samBonusTimer>0 && debt>0 )
			 alarms.add( new TextAndSeconds( samBonusTimer, "Sam has a gift for you!" ) );
		 
		 // sterility?
		 
		 // jars
		 for ( Jar jar: jars ) {
			 ts = jar.getAlarm();
			 if ( ts!=null )
				 alarms.add( ts );
		 }
		 
		 // casings
		 for ( Casing casing: casings ) {
			 ts = casing.getAlarm();
			 if ( ts!=null )
				 alarms.add( ts );
		 }
		 
		 // dealers
		 for ( Dealer dealer: dealers ) {
			 ts = dealer.getAlarm();
			 if ( ts!=null )
				 alarms.add( ts );
		 }
		 
		 // pressure cookers
		 for ( PressureCooker cooker: cookers ) {
			 ts = cooker.getAlarm();
			 if ( ts!=null )
				 alarms.add( ts );
		 }
		 
		 // dehydrators/batches
		 for ( Batch batch: batches ) {
			 if ( batch.dehydrator!= -1 && batch.dehydrator_rate>0.0f ) {
				 int seconds = (int)((float)(batch.max_dry_time - batch.dry_time)/batch.dehydrator_rate);
				 alarms.add( new TextAndSeconds( seconds, "Your batch is ready for weighing!" ) );
			 }
		 }
		 
//		 alarms.add( new TextAndSeconds( 5, "You're pretty cool Brian!" ) );
		 
		 System.out.println( "Alarms size: " + alarms.size() );
		 
		 return alarms;
	}
	
	/** updates everything owned by the player by one second.
	 * returns a screen if that type/class of screen should be updated */
	public void updateOneSec( ) {
//		String refresh = null;
		// time
		seconds += 1;
		if ( seconds>=60 ) { seconds = 0; mins += 1; }
		if ( mins>=60 ) { mins = 0; hours += 1; }
		if ( hours>=24 ) { hours = 0; days += 1; }
		
		if ( newGame ) {
			Alert alert = new Alert();
			alert.icon = "SpeechIcon";
			alert.title = "Sam Sincaid";
			alert.message = "Sam sent you a text";
			alert.type = ST.SPEECH_DIALOG;
			alert.dialog_title = "Sam Sincaid";
			alert.dialog_text = "Hey, how was jail? I know how it feels, that's why I " +
					"got the homies together and bailed you out. Sorry I couldn't be there sooner, but I left some cash" +
					" and some equipment at your house so that you can get back on your feet. You can just pay me back later.";

			Assets.addAlert( alert );

			newGame = false;
		}
		
		// Sam
		samBonusTimer --;
		if ( samBonusTimer<=0 && debt>0 ) {
			Alert alert = Assets.hasAlertWithID( ST.ALERT_SAM_BONUS );
			if ( alert==null ) {
				alert = Assets.alertPool.borrowObject();
				alert.reset();
				alert.icon = "SpeechIcon";
				alert.title = "Sam's Bonus";
				alert.message = "Sam has a gift for you!";
				alert.uniqueID = ST.ALERT_SAM_BONUS;
				alert.type = ST.MAIN_COLUMN;
				alert.mainColumn = MainPart.SAM;
	
				Assets.addAlert( alert );
			}
		}
		
		// if they purchased sterility, reduce it by one second
		if ( gb_sterile_secs>0 ) {
			if ( gb_sterile_secs==1 ) {
				// alert them that there may be contamination
				Alert alert = Assets.alertPool.borrowObject();
				alert.reset();
				alert.uniqueID = ST.ALERT_GLOVE_BOX;
				alert.icon = "GloveBoxIcon";
				alert.title = "Need Air Filter";
				alert.message = "Sterility at %78";
				alert.type = ST.MAIN_COLUMN;
				alert.mainColumn = MainPart.GLOVE_BOX_SETTINGS;

				Assets.addAlert( alert );
			}
			gb_sterile_secs --;
		}
		
		// jars
		Iterator<Jar> jarIt = jars.iterator();
		while ( jarIt.hasNext() ) {
			Jar jar = jarIt.next();
			jar.updateOneSec();
		}
		// casings
		Iterator<Casing> casingIt = casings.iterator();
		while ( casingIt.hasNext() ) {
			Casing casing = casingIt.next();
			casing.updateOneSec();
		}
		
		// dealers
		Iterator<Dealer> dealIt = dealers.iterator();
		while ( dealIt.hasNext() ) {
			Dealer dealer = dealIt.next();
			dealer.updateOneSec();
		}
		
		// pressure cookers
		Iterator<PressureCooker> pcIt = cookers.iterator();
		while ( pcIt.hasNext() ) {
			PressureCooker cooker = pcIt.next();
			cooker.updateOneSec();
//			if (  )
//				refresh = "Pressure Cooker";
		}
		
		// dehydrators/batches
		Iterator<Batch> batchIt = batches.iterator();
		while ( batchIt.hasNext() ) {
			Batch batch = batchIt.next();
			batch.updateOneSec( );
			if ( batch.dehydrator!= -1 && batch.getProgress()==1.0f ) {
				// just completed drying
				Dehydrator dehydrator = dehydrators.get( batch.dehydrator );
				int index = batch.dehydrator;
				// if there is a batch that needs to dry put it in this dehydrator
				Iterator<Batch> batchIt2 = batches.iterator();
				while ( batchIt2.hasNext() ) {
					Batch thisBatch = batchIt2.next();
					if ( thisBatch.dehydrator == -1 && !thisBatch.weighed && thisBatch.getProgress()!=1.0f ) {
						thisBatch.changeDehydrator( dehydrator, index );
						break;
					}
				}
				
				batch.changeDehydrator( null, -1 );
			}
		}
		
		// random event generator
		
		if ( !reg_discount.equals("") ) {
			if ( reg_discount_timer>0)
				reg_discount_timer --;
			else
				reg_discount = "";
		}
		
		return;
	}

	public void useAlerts() {
		// jars
		for( Jar jar: jars ) {
			jar.useAlert();
		}
		// casings
		for ( Casing casing: casings ) {
			casing.useAlert();
		}

		// dealers
		for ( Dealer dealer: dealers ) {
			dealer.useAlert();
		}
		
//		// pressure cookers
//		for ( PressureCooker cooker: cookers ) {
//			cooker.use
//		}

		// dehydrators/batches
		for ( Batch batch: batches ) {
			batch.useAlert();
		}
	}
}
