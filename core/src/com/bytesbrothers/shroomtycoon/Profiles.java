package com.bytesbrothers.shroomtycoon;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.bytesbrothers.shroomtycoon.ScreenParts.StrainPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Dealer;
import com.bytesbrothers.shroomtycoon.structures.Dehydrator;
import com.bytesbrothers.shroomtycoon.structures.Jar;
import com.bytesbrothers.shroomtycoon.structures.Objective;
import com.bytesbrothers.shroomtycoon.structures.PressureCooker;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Since;
import com.google.gson.reflect.TypeToken;

public class Profiles {
	
//	transient private static Profiles _instance = null;
	transient static private Preferences prefs = null;
	
	transient static final String no_name = "$null$";
	public static final int FAILED = 0;
	public static final int CLOUD_WAIT = 1;
	public static final int LOADED = 2;
	public static final int SNAPSHOT_WAIT = 3;
	
	public static final int IN_USE = 0;
	public static final int TOO_SHORT = 1;
	public static final int TOO_LONG = 2;
	public static final int GOOD_NAME = 3;
	
	public static final int SS_READY = 0;
	public static final int SS_LOADING = 1;
	public static final int SS_SAVING = 2;
	public static final int SS_FAILED = 3;
	public static final int SS_SUCCESS = 4;
	
	transient static Gson gson;
	
	public static class OldStyleProfile {
		@Since(1.1) public ArrayList<String> profile_names = new ArrayList<String>();
		@Since(1.1) public boolean auto_load = false;
		@Since(1.1) public String auto_load_profile = "";
	
		@Since(1.1) public boolean auto_save = true;
		
		@Since(1.1) private boolean oldStyle = true;
	}
	
	/**  */
	public static class OccupiedSlot {
		public String name = null;
		public boolean oldStyle = false;
		public boolean fileStyle = false;
		public boolean cloudStyle = false;
		public boolean snapShotStyle = false;
		public String snapShotUniqueName = "snapshotTemp";
	}
	
	/** */
	public static class PlayerInfo {
		public String name = null;
		public int slot = -1;
		public boolean oldStyle = false;
		public boolean fileStyle = false;
		public boolean cloudStyle = false;
//		public boolean snapShotStyle = false;
		public String snapShotUniqueName = "snapshotTemp";
	}
	
	public static PlayerInfo createInfo( String name ) {
		PlayerInfo newInfo = new PlayerInfo();
		newInfo.name = name;
		newInfo.oldStyle = true;
		newInfo.fileStyle = true;
		newInfo.cloudStyle = true;
//		newInfo.snapShotStyle = true;
		return newInfo;
	}
	
	public static OccupiedSlot[] slots = { new OccupiedSlot(), new OccupiedSlot(),
			new OccupiedSlot(), new OccupiedSlot() };
	
//	public static boolean auto_load = false;
	public static PlayerInfo autoLoadInfo = null;
//	public String auto_load_profile = "";
	
	/**
	 * In fileStyle, both oldStyle and !oldStyle are loaded then saved into a .st2 file.
	 * This only occurs once then fileStyle is set to true.
	 * auto_load and auto_load_profile(string) are used to auto load, they are saved in preferences.
	 * auto_save is also saved in preferences.
	 * Player names are now checked on created for duplicity and extracurricular characters.
	 */

//	transient private static final float playerVersion = 1.3f;
	transient private static final float gameVersion = 2f;

	public static void build() {
		prefs = Gdx.app.getPreferences("myShroomProfiles");
		gson = new GsonBuilder()
//		.setVersion( playerVersion )
		.create();
		
		convertIfNeeded();
	}
	
	public Profiles() {
		
	}

	public static void convertIfNeeded() {
		if ( prefs==null )
			build();
		
		if ( !prefs.getBoolean( "fileStyle", false ) ) {
			if ( prefs.getBoolean( "oldStyle", true ) ) {
				String profiles_json = prefs.getString( "profiles", no_name );
				if ( !profiles_json.equals( no_name ) ) {
					OldStyleProfile profiles = gson.fromJson( profiles_json, OldStyleProfile.class );

					// right here load all the profiles, convert them to fileStyle and save them
					Iterator<String> nameIt = profiles.profile_names.iterator();
//					 int size = profiles.profile_names.size();
//					for ( int i = 0 ; i < size; i ++ ) {
					while ( nameIt.hasNext() ) {
//						String playerJson = prefs.getString( "profile_json_" + profiles.profile_names.get( i ), no_name );
						String playerJson = prefs.getString( "profile_json_" + nameIt.next(), no_name );
						
						if ( playerJson.equals( no_name ) ) {
							// there was a problem loading it from the preferences
							Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Convert: OldStyle, Name in profiles not found in Prefs", "", (long)0 );
							System.out.println( "Profiles " + gameVersion + " Error, " + "Convert: OldStyle, Name in profiles not found in Prefs" );
						} else {
							ShroomPlayer player;

							player =  gson.fromJson( playerJson, ShroomPlayer.class );
				
							if ( player==null ) {
//								Game.getResolver().trackerSendEvent("Error", "Load", "player fromGson was null", (long)0 );
								Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Convert: OldStyle, Player from Gson was null", "", (long)0 );
								System.out.println( "Profiles " + gameVersion + " Error, " + "Convert: OldStyle, Player from Gson was null" );
							}
							
							String newName = player.name;
							int n = 1;
							
							boolean goodName = true;

							Iterator<String> stringIt = profiles.profile_names.iterator();
							while( stringIt.hasNext() && goodName ) {
								String thisName = stringIt.next();
								if ( newName.equals( thisName ) )
									goodName = false;
							}
							
//							while( !checkThisName( newName ) ) {
							while ( !goodName ) {
								newName = player.name + n;
								n ++;
								
								goodName = true;

								stringIt = profiles.profile_names.iterator();
								while( stringIt.hasNext() && goodName ) {
									String thisName = stringIt.next();
									if ( newName.equals( thisName ) )
										goodName = false;
								}
							}
							
							player.name = newName;
							
							String profileAsCode = no_name;
							
							try {
								// convert the given profile to text
						        String profileAsText = gson.toJson( player, ShroomPlayer.class );
						
						        // encode the text
						        profileAsCode = Base64Coder.encodeString( profileAsText );
							} catch ( Exception e ) {
								
								profileAsCode = no_name;
								
								Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Convert: OldStyle, FileStyle, Unable to parse existing profile data file", e.getMessage(), (long)0 );
								System.out.println( "Profiles " + gameVersion + " Error, " + "Convert: OldStyle, FileStyle, Unable to parse existing profile data file" + e.getMessage() );
							}

							if ( !profileAsCode.equals( no_name ) ) {
								// FileStyle Save
								try {
							        // create the handle for the profile data file
							        FileHandle profileDataFile = Gdx.files.external( "/shroomtycoon2/" + player.name + ".st2" );
							        if ( !profileDataFile.exists() ) {
							        	Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Convert: OldStyle, FileStyle profileDataFile would not exist", "", (long)0 );
										System.out.println( "Profiles " + gameVersion + " Error, " + "Convert: OldStyle, FileStyle profileDataFile would not exist" );
							        } else {
	
								        // write the profile data file
								        profileDataFile.writeString( profileAsCode, false ); //ErrnoException
							        }
						        
								} catch ( Exception e ) {
									Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Convert: OldStyle, FileStyle write error", e.getMessage(), (long)0 );
									System.out.println( "Profiles " + gameVersion + " Error, " + "Convert: OldStyle, FileStyle write error" + e.getMessage() );
								}
							}
						}
						
					}
					Assets.getResolver().trackerSendEvent("FileStyle", "Converted From OldStyle", "", (long)0 );
				} else {
					Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Convert: OldStyle, Profile_names was null", "", (long)0 );
					System.out.println( "Profiles " + gameVersion + " Error, " + "Convert: OldStyle, Profile_names was null" );
					
//					OldStyleProfile profiles = new OldStyleProfile();
					prefs.putString( "profiles", gson.toJson( new OldStyleProfile() ) );
					prefs.flush();
				}
				prefs.putBoolean( "oldStyle", false );
			} else {
				// right here load all the profiles, convert them to fileStyle and save them
				for( int i = 0; i<3; i ++ ) {
					if ( prefs.getBoolean( "slot" + i + "isused" ) ) {
//						load( i );
						ShroomPlayer player = new ShroomPlayer( );
						
						player.name = prefs.getString( "slot" + i + "name", "nu$%ll" );
						player.last_save = gson.fromJson( prefs.getString( "slot" + i + "last_save" ), Calendar.class );
						player.jars = gson.fromJson( prefs.getString( "slot" + i + "jars" ), new TypeToken<ArrayList<Jar>>() {}.getType() );
						player.casings = gson.fromJson( prefs.getString( "slot" + i + "casings" ), new TypeToken<ArrayList<Casing>>() {}.getType() );
						player.dealers = gson.fromJson( prefs.getString( "slot" + i + "dealers" ), new TypeToken<ArrayList<Dealer>>() {}.getType() );
						player.dehydrators = gson.fromJson( prefs.getString( "slot" + i + "dehydrators" ), new TypeToken<ArrayList<Dehydrator>>() {}.getType() );
						String myString = prefs.getString( "slot" + i + "cookers" );
						player.cookers = gson.fromJson( myString, new TypeToken<ArrayList<PressureCooker>>() {}.getType() );
						
						// resources
						myString = prefs.getString( "slot" + i + "cash", "nu$%ll" );
						player.cash = new BigInteger( myString );
						player.batches = gson.fromJson( prefs.getString( "slot" + i + "batches" ), new TypeToken<ArrayList<Batch>>() {}.getType() );
						player.grain_jars = prefs.getInteger( "slot" + i + "grain_jars", -978621 );
						player.sub_jars[0] = prefs.getInteger( "slot" + i + "sub_jars0", -978621 );
						player.sub_jars[1] = prefs.getInteger( "slot" + i + "sub_jars1", -978621 );
						player.sub_jars[2] = prefs.getInteger( "slot" + i + "sub_jars2", -978621 );
						player.sub_jars[3] = prefs.getInteger( "slot" + i + "sub_jars3", -978621 );
						player.coins = prefs.getInteger( "slot" + i + "coins", -978621 );
						player.syringes = gson.fromJson( prefs.getString( "slot" + i + "syringes" ), new TypeToken<ArrayList<Strain>>() {}.getType() );
						player.can_buy_strains = gson.fromJson( prefs.getString( "slot" + i + "can_buy_strains" ), new TypeToken<ArrayList<String>>() {}.getType() );

						/** custom strains*/
						player.strains = gson.fromJson( prefs.getString( "slot" + i + "strains" ), new TypeToken<ArrayList<Strain>>() {}.getType() );

						// naming numbers
						player.jar_enum = prefs.getInteger( "slot" + i + "jar_enum", -978621 );
						player.strain_enum = prefs.getInteger( "slot" + i + "strain_enum", -978621 );

						// laboratory settings
						player.inc_temp = prefs.getInteger( "slot" + i + "inc_temp", -978621 );
						player.fc_temp = prefs.getInteger( "slot" + i + "fc_temp", -978621 );
						player.fc_humidity = prefs.getInteger( "slot" + i + "fc_humidity", -978621 );
						player.last_gb_sterile_secs = prefs.getInteger( "slot" + i + "last_gb_sterile_secs", -978621 );
						player.permanent_sterile = prefs.getBoolean( "slot" + i + "permanent_sterile" );

						player.gb_sterile_secs = prefs.getInteger( "slot" + i + "gb_sterile_secs", -978621 );
						player.inc_capacity = prefs.getInteger( "slot" + i + "inc_capacity", -978621 );
						player.fc_capacity = prefs.getInteger( "slot" + i + "fc_capacity", -978621 );
						player.fridge_capacity = prefs.getInteger( "slot" + i + "fridge_capacity", -978621 );
						
						// statistics
						player.totalJarsMade = prefs.getInteger( "slot" + i + "totalJarsMade", -978621 );
						player.totalCasingsMade = prefs.getInteger( "slot" + i + "totalCasingsMade", -978621 );
						myString = prefs.getString( "slot" + i + "totalCashMade", "nu$%ll" );
						player.totalCashMade = new BigInteger( myString );
						player.totalStashWeighed = prefs.getFloat( "slot" + i + "totalStashWeighed", -978621 );
						myString = prefs.getString( "slot" + i + "totalCashSpent", "nu$%ll" );
						player.totalCashSpent = new BigInteger( myString );
						player.totalStashSold = prefs.getFloat( "slot" + i + "totalStashSold", -978621 );
						player.totalBlueCoinsEarned = prefs.getInteger( "slot" + i + "totalBlueCoinsEarned", -978621 );
						player.totalBlueCoinsUsed = prefs.getInteger( "slot" + i + "totalBlueCoinsUsed", -978621 );
						player.totalSideJobsDone = prefs.getInteger( "slot" + i + "totalSideJobsDone", -978621 );
						player.totalObjectivesDone = prefs.getInteger( "slot" + i + "totalObjectivesDone", -978621 );
						player.totalContaminations = prefs.getInteger( "slot" + i + "totalContaminations", -978621 );
						player.totalCooks = prefs.getInteger( "slot" + i + "totalCooks", -978621 );
						player.totalDealersHired = prefs.getInteger( "slot" + i + "totalDealersHired", -978621 );
						player.totalHarvests = prefs.getInteger( "slot" + i + "totalHarvests", -978621 );
						player.totalSyringesMade = prefs.getInteger( "slot" + i + "totalSyringesMade", -978621 );

						// profile options
						player.useTransitions = prefs.getBoolean( "slot" + i + "useTransitions" );
						player.fc_cam_distance = prefs.getFloat( "slot" + i + "fc_cam_distance", -978621 );
						
						// help hints
						player.useHelpHints = prefs.getBoolean( "slot" + i + "useHelpHints" );
						player.hintFirstFc = prefs.getBoolean( "slot" + i + "hintFirstFc" );
						player.hintFirstSam = prefs.getBoolean( "slot" + i + "hintFirstSam" );
						player.hintFirstJar = prefs.getBoolean( "slot" + i + "hintFirstJar" );
						player.hintFirstDealers = prefs.getBoolean( "slot" + i + "hintFirstDealers" );
						player.hintFirstGloveBox = prefs.getBoolean( "slot" + i + "hintFirstGloveBox" );
						player.hintFirstGloveBoxSettings = prefs.getBoolean( "slot" + i + "hintFirstGloveBoxSettings" );
						player.hintFirstHarvest = prefs.getBoolean( "slot" + i + "hintFirstHarvest" );
						
						player.givenNewPlayerBlueCoins = prefs.getBoolean( "slot" + i + "givenNewPlayerBlueCoins" );
						
						// xp values
						player.current_xp = prefs.getInteger( "slot" + i + "current_xp", -978621 );
						player.last_level = prefs.getInteger( "slot" + i + "last_level", -978621 );
						player.next_level = prefs.getInteger( "slot" + i + "next_level", -978621 );
						player.level = prefs.getInteger( "slot" + i + "level", -978621 );

						// time values
						player.days = prefs.getInteger( "slot" + i + "days", -978621 );
						player.hours = prefs.getInteger( "slot" + i + "hours", -978621 );
						player.mins = prefs.getInteger( "slot" + i + "mins", -978621 );
						player.seconds = prefs.getInteger( "slot" + i + "seconds", -978621 );

						// sam and his/her loan
						player.debt = prefs.getInteger( "slot" + i + "debt", -978621 );
						player.half_paid = prefs.getBoolean( "slot" + i + "half_paid" );
						player.useSamsBackground = prefs.getBoolean( "slot" + i + "useSamsBackground" );
						
						// cheats
						player.usedFreeHundred = prefs.getBoolean( "slot" + i + "usedFreeHundred" );
						
						//random event variables
						player.reg_last_batch = prefs.getInteger( "slot" + i + "reg_last_batch", -978621 );
						player.reg_dealer_name = prefs.getString( "slot" + i + "reg_dealer_name", "nu$%ll" );
						player.reg_discount = prefs.getString( "slot" + i + "reg_discount", "nu$%ll" );
						player.reg_discount_percent = prefs.getFloat( "slot" + i + "reg_discount_percent", -978621 );
						player.reg_discount_timer = prefs.getInteger( "slot" + i + "reg_discount_timer", -978621 );
						
						if ( prefs.getBoolean( "slot" + i + "objective" ) ) {
							player.objective = new Objective();
							player.objective.type = prefs.getInteger( "slot" + i + "objectivetype", -978621 );
							player.objective.text = prefs.getString( "slot" + i + "objectivetext", "nu$%ll" );
							player.objective.definition = prefs.getString( "slot" + i + "objectivedefinition", "nu$%ll" );
						}
						
						
						
						player.has_reg_ocean_body = prefs.getBoolean( "slot" + i + "has_reg_ocean_body" );
						player.has_reg_fun_guy = prefs.getBoolean( "slot" + i + "has_reg_fun_guy" );

						
						// FileStyle Save
						try {
					        // create the handle for the profile data file
					        FileHandle profileDataFile = Gdx.files.external( "/shroomtycoon2/" + player.name + ".st2" );
					
					        // convert the given profile to text
					        String profileAsText = gson.toJson( player, ShroomPlayer.class );
					
					        // encode the text
					        String profileAsCode = Base64Coder.encodeString( profileAsText );
					
					        // write the profile data file
					        profileDataFile.writeString( profileAsCode, false ); //ErrnoException
				        
						} catch ( Exception e ) {
							Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Convert: NotOldStyle > FileStyle write error", e.getMessage(), (long)0 );
							System.out.println( "Profiles " + gameVersion + " Error, " + "Convert: NotOldStyle > FileStyle write error" + e.getMessage() );
						} finally {

						}
					}
				}
				Assets.getResolver().trackerSendEvent("FileStyle", "Converted From NotOldStyle", "", (long)0 );
			}
			
			// right here set it to file style and save
			prefs.putBoolean( "fileStyle", true );
			
		}

		prefs.flush();
	}
	
	/** Performs the 'update time passing while we were away' on the current Game.getShroomPlayer */
	public static void timeTravel() {
		ShroomPlayer player = Assets.player;
		if ( player!=null ) {
			Assets.clearAlerts();
			System.out.println( "Time Traveling" );
			
			// i fucked up on the NotOldStyle to FileStyle and didn't load the cookers so do a check and if there are none
			// then give them two that have one upgrade each
			if ( player.cookers.isEmpty() ) {
				player.cookers = new ArrayList<PressureCooker> ();
				PressureCooker newCooker = new PressureCooker( Assets.game );
				newCooker.capacity += 2;
				player.cookers.add( newCooker );
				newCooker = new PressureCooker( Assets.game );
				newCooker.capacity += 2;
				player.cookers.add( newCooker );
			}
			
			// update as if we never closed
			Calendar cal = Calendar.getInstance();
	
	    	Calendar lastsave = player.last_save;
	
	    	long difference = cal.getTimeInMillis() - lastsave.getTimeInMillis();
	    	int seconds = (int)((float)difference/1000.0f);
	
			player.updateBy( seconds );

	    	player.can_buy_strains.remove( "Easter Egg" );
	    	if ( cal.get(Calendar.MONTH)==Calendar.APRIL )
	    		player.can_buy_strains.add( "Easter Egg" );
	    	
	    	if ( player.level>=6 && !player.canBuyStrain( "Midnight", false ) ) {
	    		Alert alert = Assets.alertPool.borrowObject();
				alert.reset();
				alert.icon = "SyringeIcon";
				alert.title = "Strain: Midnight";
				alert.uniqueID = ST.ALERT_MIDNIGHT;
				alert.message = "A new strain has been unlocked: Midnight";
				player.can_buy_strains.add( "Midnight" );
				alert.type = ST.TABLE;
				alert.nextPart = StrainPart.class;
//				alert.partName = "Strain";
//				alert.screenPart = Assets.game.getScreenPart( "Strain" );
				alert.partObject = StrainMaster.getStrain( "Midnight" );
//				alert.table = new StrainColumn( StrainMaster.getStrain( "Midnight" ) );
				Assets.addAlert( alert );
	    	}
	    	
//	    	if ( player.level>=12 && !player.canBuyStrain( "Golden Master", false ) ) {
//	    		Alert alert = new Alert();
//				alert.icon = "SyringeIcon";
//				alert.title = "Strain: Golden Master";
//				alert.message = "A new strain has been unlocked: Golden Master";
//				player.can_buy_strains.add( "Golden Master" );
//				alert.type = ST.TABLE;
//				alert.screenPart = Assets.game.getScreenPart( "Strain" );
//				alert.partObject = new StrainColumn( StrainMaster.getStrain( "Midnight" ) );
////				alert.table = new StrainColumn( StrainMaster.getStrain( "Golden Master" ) );
//				Assets.addAlert( alert );
//	    	}
	
	    	Assets.isMonday = ( cal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY );
	    	if ( Assets.isMonday ) {
	    		Alert alert = Assets.alertPool.borrowObject();
				alert.reset();
				alert.icon = "CoinSaleIcon";
				alert.title = "Mushroom Monday";
				alert.message = "Get 100 BC %20 off!";
				alert.type = ST.COINS_DIALOG;
//				alert.mainColumn = MainColumn.COINS;

				Assets.addAlert( alert );
	    	}
	    	
	    	if ( player.samBonusTimer<=0 && player.debt>0 ) {
	    		Alert alert = Assets.alertPool.borrowObject();
				alert.reset();
				alert.icon = "SpeechIcon";
				alert.title = "Sam's Bonus";
				alert.message = "Sam has a gift for you!";
				alert.uniqueID = ST.ALERT_SAM_BONUS;
				alert.type = ST.MAIN_COLUMN;
				alert.mainColumn = MainPart.SAM;

				Assets.addAlert( alert );
	    	}
	    	
	    	// since we added achievements after releasing the game we can check here and hand out achievements that this player has already earned
	    	// earned $1,000
	    	if ( player.totalCashMade.intValue() >=1000 )
	    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQDQ" );
	    	
	    	// earned $10,000
	    	if ( player.totalCashMade.intValue() >=10000 )
	    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQDg" );
	    	
	    	// earned $100,000
	    	if ( player.totalCashMade.intValue() >=100000 )
	    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQDw" );
	    	
	    	// grew a pound 
	    	if ( player.totalStashWeighed >=448 )
	    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQEA" );
	    	
	    	// made one payment
	    	if ( player.debt < ST.SAMS_LOAN )
	    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQEg" );
	    	
	    	// paid sam back in full
	    	if ( player.debt <= 0 )
	    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQEw" );
	    	
	    	// created an eight star strain
	    	Iterator<Strain> strainIt = player.strains.iterator();
	    	while ( strainIt.hasNext() ) {
	    		Strain thisStrain = strainIt.next();
	    		if ( thisStrain.getGrossRating()>=8 )
	    			Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQFA" );
	    	}
		}
	}
	
	/** Profiles 1.8 load the profile with info. Attempts to load it from cloud, file, then internal. Performs corrupted data recovery automatically.*/
	public static int load( PlayerInfo playerInfo ) {
		System.out.println( "Profiles " + gameVersion + ", " + "Load: Info, trying to load.."  );
		if ( playerInfo!=null ) {
			setAutoLoadInfo( playerInfo );
			
			if ( playerInfo.snapShotUniqueName!=null && !playerInfo.snapShotUniqueName.equals( "snapshotTemp" ) ) {
				Assets.getResolver().loadFromSnapShot( playerInfo.snapShotUniqueName );
				
				while( Assets.getResolver().getSnapshotState() == SS_LOADING ) {
					
				}
				
				switch( Assets.getResolver().getSnapshotState() ) {
				case SS_SUCCESS:
					System.out.println( "Load from snapshot success" );
					return SNAPSHOT_WAIT;

				case SS_FAILED:
					System.out.println( "Load from snapshot failed" );
					break;
				}
			}
			
			if ( playerInfo.oldStyle && loadOldStyle( playerInfo.name ) )
				return LOADED;
			
			if ( playerInfo.fileStyle && loadFileStyle( playerInfo.name ) )
				return LOADED;
			
			if ( playerInfo.cloudStyle && Assets.getResolver().loadFromCloud( playerInfo.slot ) )
				return CLOUD_WAIT;
			
			// and if we're really desperate we can see if it's floating around the NotOldStyle files
			for ( int i = 0; i < 4; i ++ ) {
				String name = getPlayerName( i );
				if ( name.equals( playerInfo.name ) && loadNotOldStyle( i ) ) {
					return LOADED;
				}
			}
		} else {
			Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Load: Info, PlayerInfo was null", "", (long)0 );
			System.out.println( "Profiles " + gameVersion + " Error, " + "Load: Info, PlayerInfo was null" );
			return FAILED;
		}
		
		Assets.setPlayer( null );
		Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Load: Info, Total Load Failure", "", (long)0 );
		System.out.println( "Profiles " + gameVersion + " Error, " + "Load: Info, Total Load Failure" );
		
		return FAILED;
	}

	private static boolean loadFileStyle( String name ) {
		if ( name.equals( no_name ) )
			return false;
		
		ShroomPlayer player = new ShroomPlayer( );
		
		boolean successful = true;
		
		// create the handle for the profile data file
        FileHandle profileDataFile = Gdx.files.external( "/shroomtycoon2/" + name + ".st2" );
        
        // check if the profile data file exists
        if( profileDataFile.exists() ) {
            // load the profile from the data file
            try {
                // read the file as text
                String profileAsCode = profileDataFile.readString();

                // decode the contents
                String profileAsText = Base64Coder.decodeString( profileAsCode );

                // restore the state
                player = gson.fromJson( profileAsText, ShroomPlayer.class );
                
            } catch( Exception e ) {

                // log the exception
            	Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Load: FileStyle, Unable to parse existing profile data file", e.getMessage(), (long)0 );
				System.out.println( "Profiles " + gameVersion + " Error, " + "Load: FileStyle, Unable to parse existing profile data file" + e.getMessage() );
            	successful =  false;
            }
        } else {
        	Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Load: FileStyle, Filename did not exist", "", (long)0 );
			System.out.println( "Profiles " + gameVersion + " Error, " + "Load: FileStyle, Filename did not exist" );
        	successful =  false;
        }
        
        if ( player==null ) {
        	Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Load: FileStyle, Player was null", "", (long)0 );
			System.out.println( "Profiles " + gameVersion + " Error, " + "Load: FileStyle, Player was null" );
        	successful = false;
        }

        Assets.setPlayer( player );
        System.out.println( "Profiles " + gameVersion + " Load Filestyle returned: " + successful );
        return successful;
	}
	
	public static ArrayList<PlayerInfo> getPlayerInfo() {
		if ( prefs==null )
			build();

		ArrayList<PlayerInfo> info = new ArrayList<PlayerInfo>();
		
		// include the first 4 that make up the slots and hopefully cloud saves
		for ( int i = 0; i < 4; i ++ ) {
			loadSlot( i );
			if ( slots[i].name!=null ) {
				PlayerInfo newInfo = new PlayerInfo();
				newInfo.name = slots[i].name;
				newInfo.snapShotUniqueName = slots[i].snapShotUniqueName;
				newInfo.slot = i;
				newInfo.oldStyle = slots[i].oldStyle;
				newInfo.fileStyle = slots[i].fileStyle;
				newInfo.cloudStyle = slots[i].cloudStyle;
//				newInfo.snapShotStyle = slots[i].snapShotStyle;
				info.add( newInfo );
			}
		}
		
		// add names found in FileStyle
		FileHandle dirFile = Gdx.files.external( "/shroomtycoon2" );

		if ( dirFile.isDirectory() ) {
			for ( FileHandle fileFile: dirFile.list() ) {
				
				String newName = fileFile.nameWithoutExtension();
				// skip player names that are already in the cloudsave
				boolean duplicates = false;
				Iterator<PlayerInfo> infoIt = info.iterator();
				while( infoIt.hasNext() && !duplicates ) {
					PlayerInfo thisInfo = infoIt.next();
					if ( thisInfo.name.equals( newName ) )
						duplicates = true;
				}
				if ( !duplicates ) {
					PlayerInfo newInfo = new PlayerInfo();
					newInfo.name = newName;
					newInfo.slot = -1;
					newInfo.oldStyle = false;
					newInfo.fileStyle = true;
					newInfo.cloudStyle = false;
//					newInfo.snapShotStyle = false;
					info.add( newInfo );
				}
			}
		}
		
		// add names found in OldStyle
		String profiles_json = prefs.getString( "profiles", no_name );
		if ( !profiles_json.equals( no_name ) ) {
			OldStyleProfile profiles = gson.fromJson( profiles_json, OldStyleProfile.class );
			
			Iterator<String> nameIt = profiles.profile_names.iterator();
			while ( nameIt.hasNext() ) {
				String thisName = nameIt.next();

				// skip player names that are already in the cloudsave and update the fileStyle ones to include OldStyle
				boolean duplicates = false;
				Iterator<PlayerInfo> infoIt = info.iterator();
				while( infoIt.hasNext() && !duplicates ) {
					PlayerInfo thisInfo = infoIt.next();
					if ( thisInfo.name.equals( thisName ) ) {
						duplicates = true;
						thisInfo.oldStyle = true;
					}
				}
				if ( !duplicates ) {
					PlayerInfo newInfo = new PlayerInfo();
					newInfo.name = thisName;
					newInfo.slot = -1;
					newInfo.oldStyle = true;
					newInfo.fileStyle = false;
					newInfo.cloudStyle = false;
//					newInfo.snapShotStyle = false;
					info.add( newInfo );
				}
			}
		}

		return info;
	}
	
	/**
	 * 
	 * @param name: the name they are trying to use for their profile
	 * @return true if -the name is not already in use and it consists of only letters, numbers and spaces. 
	 * false otherwise
	 */
	public static int checkThisName( String name ) {
		
		if ( name.equals( no_name ) )
			return TOO_SHORT;
		
		if ( name.equals( "" ) )
			return TOO_SHORT;
		
		if ( name.length()>15 )
			return TOO_LONG;
		
		Iterator<PlayerInfo> infoIt = getPlayerInfo().iterator();
		while( infoIt.hasNext() ) {
			PlayerInfo thisInfo = infoIt.next();
			if ( thisInfo.name.equals( name ) )
				return IN_USE;
		}

		return GOOD_NAME;
	}
	
	public static boolean delete( PlayerInfo info ) {
		if ( info.fileStyle )
			deleteFileStyle( info.name );
		
		if ( info.oldStyle )
			deleteOldStyle( info.name );

		if ( info.slot>=0 && info.slot<=3 ) {
			if ( info.cloudStyle )
				Assets.getResolver().saveToCloud( null, info.slot );
			
			slots[info.slot].name = null;
			slots[info.slot].cloudStyle = false;
			slots[info.slot].fileStyle = false;
			slots[info.slot].oldStyle = false;
			saveSlot( info.slot );
		}
		
		PlayerInfo autoLoadInfo = getAutoLoadInfo();
		if ( autoLoadInfo!=null && autoLoadInfo.name.equals( info.name ) ) {
			setAutoLoad( false );
		}
		
		Assets.setPlayer( null );
		Assets.clearAlerts();
		
		return true;
	}
	
	private static boolean deleteFileStyle( String name ) {
		// create the handle for the profile data file
        FileHandle profileDataFile = Gdx.files.external( "/shroomtycoon2/" + name + ".st2" );
        
        // check if the profile data file exists
        if( profileDataFile.exists() ) {
        	return profileDataFile.delete();
        } else
        	return false;
       
	}
	
	private static void deleteOldStyle( String name ) {
		if ( prefs==null )
			build();

		OldStyleProfile profiles = new OldStyleProfile();
		
		String profiles_json = prefs.getString( "profiles", no_name );
		if ( !profiles_json.equals( no_name ) ) {
			profiles = gson.fromJson( profiles_json, OldStyleProfile.class );

			boolean containsProfile = false;
			
			Iterator<String> stringIt = profiles.profile_names.iterator();
			while( stringIt.hasNext() && !containsProfile ) {
				String thisName = stringIt.next();
				if ( thisName.equals( name ) )
					containsProfile = true;
			}
			
			if ( containsProfile )
				profiles.profile_names.remove( name );
			
		} else {
			Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Delete: OldStyle, Profiles was null", "", (long)0 );
			System.out.println( "Profiles " + gameVersion + " Error, " + "Delete: OldStyle, Profiles was null" );
		}
			
		prefs.putString( "profiles", gson.toJson( profiles ) );
		
		prefs.putString( "profile_json_" + name, no_name );
		prefs.flush();

	}
	
	/** loads the indicated profile*/
	private static boolean loadOldStyle( String name ) {
		if ( prefs==null )
			build();

		String playerJson = prefs.getString( "profile_json_" + name, no_name );
		
		if ( playerJson.equals( no_name ) ) {
			// there was a problem loading it from the preferences
			Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Load: OldStyle, Player String from Prefs was null", "", (long)0 );
			System.out.println( "Profiles " + gameVersion + " Error, " + "Load: OldStyle, Player String from Prefs was null" );
			return false;
		} else {
			ShroomPlayer player;

			player =  gson.fromJson( playerJson, ShroomPlayer.class );

			if ( player==null ) {
				Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Load: OldStyle, Player fromGson was null", "", (long)0 );
				System.out.println( "Profiles " + gameVersion + " Error, " + "Load: OldStyle, Player fromGson was null" );
				return false;
			}
			
			Assets.setPlayer( player );

			return true;
		}
	}

	private static String getPlayerName( int slot ) {
		if ( prefs==null )
			build();

		String name = prefs.getString( "slot" + slot + "name", no_name );

		return name;
	}

	private static boolean loadNotOldStyle( int slot ) {
		if ( prefs==null )
			build();

		ShroomPlayer player = new ShroomPlayer( );
		
		player.name = prefs.getString( "slot" + slot + "name", no_name );

		player.last_save = gson.fromJson( prefs.getString( "slot" + slot + "last_save" ), Calendar.class );
		player.jars = gson.fromJson( prefs.getString( "slot" + slot + "jars" ), new TypeToken<ArrayList<Jar>>() {}.getType() );
		player.casings = gson.fromJson( prefs.getString( "slot" + slot + "casings" ), new TypeToken<ArrayList<Casing>>() {}.getType() );
		player.dealers = gson.fromJson( prefs.getString( "slot" + slot + "dealers" ), new TypeToken<ArrayList<Dealer>>() {}.getType() );
		player.dehydrators = gson.fromJson( prefs.getString( "slot" + slot + "dehydrators" ), new TypeToken<ArrayList<Dehydrator>>() {}.getType() );
		String myString = prefs.getString( "slot" + slot + "cookers" );
		player.cookers = gson.fromJson( myString, new TypeToken<ArrayList<PressureCooker>>() {}.getType() );

		// resources
		myString = prefs.getString( "slot" + slot + "cash", "1000" );
		player.cash = new BigInteger( myString );
		player.batches = gson.fromJson( prefs.getString( "slot" + slot + "batches" ), new TypeToken<ArrayList<Batch>>() {}.getType() );

		player.grain_jars = prefs.getInteger( "slot" + slot + "grain_jars", -978621 );
		player.sub_jars[0] = prefs.getInteger( "slot" + slot + "sub_jars0", -978621 );
		player.sub_jars[1] = prefs.getInteger( "slot" + slot + "sub_jars1", -978621 );
		player.sub_jars[2] = prefs.getInteger( "slot" + slot + "sub_jars2", -978621 );
		player.sub_jars[3] = prefs.getInteger( "slot" + slot + "sub_jars3", -978621 );
		player.coins = prefs.getInteger( "slot" + slot + "coins", -978621 );

		player.syringes = gson.fromJson( prefs.getString( "slot" + slot + "syringes" ), new TypeToken<ArrayList<Strain>>() {}.getType() );
		player.can_buy_strains = gson.fromJson( prefs.getString( "slot" + slot + "can_buy_strains" ), new TypeToken<ArrayList<String>>() {}.getType() );

		/** custom strains*/
		player.strains = gson.fromJson( prefs.getString( "slot" + slot + "strains" ), new TypeToken<ArrayList<Strain>>() {}.getType() );
		
		// naming numbers
		player.jar_enum = prefs.getInteger( "slot" + slot + "jar_enum", -978621 );
		player.strain_enum = prefs.getInteger( "slot" + slot + "strain_enum", -978621 );
		
		// laboratory settings
		player.inc_temp = prefs.getInteger( "slot" + slot + "inc_temp", -978621 );
		player.fc_temp = prefs.getInteger( "slot" + slot + "fc_temp", -978621 );
		player.fc_humidity = prefs.getInteger( "slot" + slot + "fc_humidity", -978621 );
		player.last_gb_sterile_secs = prefs.getInteger( "slot" + slot + "last_gb_sterile_secs", -978621 );
		player.permanent_sterile = prefs.getBoolean( "slot" + slot + "permanent_sterile" );

		player.gb_sterile_secs = prefs.getInteger( "slot" + slot + "gb_sterile_secs", -978621 );
		player.inc_capacity = prefs.getInteger( "slot" + slot + "inc_capacity", -978621 );
		player.fc_capacity = prefs.getInteger( "slot" + slot + "fc_capacity", -978621 );
		player.fridge_capacity = prefs.getInteger( "slot" + slot + "fridge_capacity", -978621 );
		
		// statistics
		player.totalJarsMade = prefs.getInteger( "slot" + slot + "totalJarsMade", -978621 );
		player.totalCasingsMade = prefs.getInteger( "slot" + slot + "totalCasingsMade", -978621 );
		myString = prefs.getString( "slot" + slot + "totalCashMade", "nu$%ll" );
		player.totalCashMade = new BigInteger( myString );
		player.totalStashWeighed = prefs.getFloat( "slot" + slot + "totalStashWeighed", -978621 );
		myString = prefs.getString( "slot" + slot + "totalCashSpent", "nu$%ll" );
		player.totalCashSpent = new BigInteger( myString );
		player.totalStashSold = prefs.getFloat( "slot" + slot + "totalStashSold", -978621 );
		player.totalBlueCoinsEarned = prefs.getInteger( "slot" + slot + "totalBlueCoinsEarned", -978621 );
		player.totalBlueCoinsUsed = prefs.getInteger( "slot" + slot + "totalBlueCoinsUsed", -978621 );
		player.totalSideJobsDone = prefs.getInteger( "slot" + slot + "totalSideJobsDone", -978621 );
		player.totalObjectivesDone = prefs.getInteger( "slot" + slot + "totalObjectivesDone", -978621 );
		player.totalContaminations = prefs.getInteger( "slot" + slot + "totalContaminations", -978621 );
		player.totalCooks = prefs.getInteger( "slot" + slot + "totalCooks", -978621 );
		player.totalDealersHired = prefs.getInteger( "slot" + slot + "totalDealersHired", -978621 );
		player.totalHarvests = prefs.getInteger( "slot" + slot + "totalHarvests", -978621 );
		player.totalSyringesMade = prefs.getInteger( "slot" + slot + "totalSyringesMade", -978621 );
		
		// profile options
		player.useTransitions = prefs.getBoolean( "slot" + slot + "useTransitions" );
		player.fc_cam_distance = prefs.getFloat( "slot" + slot + "fc_cam_distance", -978621 );
		
		// help hints
		player.useHelpHints = prefs.getBoolean( "slot" + slot + "useHelpHints" );
		player.hintFirstFc = prefs.getBoolean( "slot" + slot + "hintFirstFc" );
		player.hintFirstSam = prefs.getBoolean( "slot" + slot + "hintFirstSam" );
		player.hintFirstJar = prefs.getBoolean( "slot" + slot + "hintFirstJar" );
		player.hintFirstDealers = prefs.getBoolean( "slot" + slot + "hintFirstDealers" );
		player.hintFirstGloveBox = prefs.getBoolean( "slot" + slot + "hintFirstGloveBox" );
		player.hintFirstGloveBoxSettings = prefs.getBoolean( "slot" + slot + "hintFirstGloveBoxSettings" );
		player.hintFirstHarvest = prefs.getBoolean( "slot" + slot + "hintFirstHarvest" );
		
		player.givenNewPlayerBlueCoins = prefs.getBoolean( "slot" + slot + "givenNewPlayerBlueCoins" );
		
		// xp values
		player.current_xp = prefs.getInteger( "slot" + slot + "current_xp", -978621 );
		player.last_level = prefs.getInteger( "slot" + slot + "last_level", -978621 );
		player.next_level = prefs.getInteger( "slot" + slot + "next_level", -978621 );
		player.level = prefs.getInteger( "slot" + slot + "level", -978621 );
		
		// time values
		player.days = prefs.getInteger( "slot" + slot + "days", -978621 );
		player.hours = prefs.getInteger( "slot" + slot + "hours", -978621 );
		player.mins = prefs.getInteger( "slot" + slot + "mins", -978621 );
		player.seconds = prefs.getInteger( "slot" + slot + "seconds", -978621 );
		
		// sam and his/her loan
		player.debt = prefs.getInteger( "slot" + slot + "debt", -978621 );
		player.half_paid = prefs.getBoolean( "slot" + slot + "half_paid" );
		player.useSamsBackground = prefs.getBoolean( "slot" + slot + "useSamsBackground" );
		
		// cheats
		player.usedFreeHundred = prefs.getBoolean( "slot" + slot + "usedFreeHundred" );
		
		//random event variables
		player.reg_last_batch = prefs.getInteger( "slot" + slot + "reg_last_batch", -978621 );
		player.reg_dealer_name = prefs.getString( "slot" + slot + "reg_dealer_name", "nu$%ll" );
		player.reg_discount = prefs.getString( "slot" + slot + "reg_discount", "nu$%ll" );
		player.reg_discount_percent = prefs.getFloat( "slot" + slot + "reg_discount_percent", -978621 );
		player.reg_discount_timer = prefs.getInteger( "slot" + slot + "reg_discount_timer", -978621 );
		
		if ( prefs.getBoolean( "slot" + slot + "objective" ) ) {
			player.objective = new Objective();
			player.objective.type = prefs.getInteger( "slot" + slot + "objectivetype", -978621 );
			player.objective.text = prefs.getString( "slot" + slot + "objectivetext", "nu$%ll" );
			player.objective.definition = prefs.getString( "slot" + slot + "objectivedefinition", "nu$%ll" );
		}

		player.has_reg_ocean_body = prefs.getBoolean( "slot" + slot + "has_reg_ocean_body" );
		player.has_reg_fun_guy = prefs.getBoolean( "slot" + slot + "has_reg_fun_guy" );
		
		if ( !player.name.equals( no_name ) && player.level!=-978621 ) {
			Assets.setPlayer( player );

			return true;
		} else {
			return false;
		}
	}
	
	/** For reseting the array of four holding data on the save slots */
	public static void resetSlots() {
		if ( prefs==null )
			build();

		for ( int i = 0; i < 4; i ++ ) {
			slots[i].name = null;
			slots[i].cloudStyle = false;
			slots[i].fileStyle = false;
			slots[i].oldStyle = false;
		}
	}
	
	/** saves the specified slot */
	public static void saveSlot( int slot ) {
		if ( prefs==null )
			build();

		if ( slot>=0 && slot<=3 ) {
			String asJson = gson.toJson( slots[ slot ] );
			System.out.println( "Saving slot " + slot + ": " + asJson );
			prefs.putString( "occupiedSlot" + slot, asJson );
			prefs.flush();
		}
	}
	
	/** loads the specified slot */
	public static void loadSlot( int slot ) {
		if ( prefs==null )
			build();

		if ( slot>=0 && slot<=3 ) {
			String slotGson = prefs.getString( "occupiedSlot" + slot, no_name );
			if ( !slotGson.equals( no_name ) ) {
				System.out.println( "Loading slot " + slot );
				slots[ slot ] = gson.fromJson( slotGson, OccupiedSlot.class );
			} else {
				System.out.println( "No slot " + slot + " so making one..." );
				slots[ slot ] = new OccupiedSlot();
				saveSlot( slot );
			}
		}
	}
	
	/** saves current player profile */
	public static void save() {
		if ( prefs==null )
			build();

		ShroomPlayer player = Assets.player;
		if ( player!=null ) {
			
			System.out.println( "Saving" );
			ShroomPlayerCompressor.compress( player );
			
			player.last_save = Calendar.getInstance();
			
			// try and find a slot for this player if they do not already have one
			for ( int i = 0; i < 4 && (player.slot<=-1 || player.slot>3); i ++ ) {
				loadSlot( i );
				if ( slots[i].name == null ) {
					player.slot = i;
					System.out.println( "Save: slot found for player: " + i );
				}
			}
	
			if ( player.slot>=0 && player.slot<=3 ) {
				slots[player.slot].name = player.name;
				slots[player.slot].snapShotUniqueName = player.uniqueSaveName;
				slots[player.slot].cloudStyle = false;
				slots[player.slot].fileStyle = false;
				slots[player.slot].oldStyle = false;
				slots[player.slot].snapShotStyle = true;
			}
			
			if ( player!=null ) {
				if ( player.uniqueSaveName==null || player.uniqueSaveName.equals( "snapshotTemp" ) ) {
					String unique = new BigInteger(281, new Random()).toString(13);
					player.uniqueSaveName = "snapshot-" + unique;
				}
				
				
				if ( player.uniqueSaveName!=null && !player.uniqueSaveName.equals( "snapshotTemp" ) )
					// Snapshot save
					Assets.getResolver().saveToSnapShot( player );
				else
					Assets.getResolver().showSavedGamesUIforSaving();
			}
			
//			// Cloud save
//			if ( Assets.getResolver().saveToCloud( player, player.slot ) && player.slot>=0 && player.slot<=3 ) {
//				slots[player.slot].cloudStyle = true;
//			}
	
			String profileAsCode = no_name;
			
			try {
				// convert the given profile to text
		        String profileAsText = gson.toJson( player, ShroomPlayer.class );
		
		        // encode the text
		        profileAsCode = Base64Coder.encodeString( profileAsText );
			} catch ( Exception e ) {
				profileAsCode = no_name;
				Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Save: FileStyle could not convert player into gson", e.getMessage(), (long)0 );
				System.out.println( "Profiles " + gameVersion + " Error, " + "Save: FileStyle could not convert player into gson" + e.getMessage() );
			} finally {
				// FileStyle Save
				if ( Gdx.files.isExternalStorageAvailable() ) {
					try {
				        // create the handle for the profile data file
				        FileHandle profileDataFile = Gdx.files.external( "/shroomtycoon2/" + player.name + ".st2" );

				        // write the profile data file
				        profileDataFile.writeString( profileAsCode, false ); //ErrnoException
			        
					} catch ( Exception e ) {
						Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Save: FileStyle write error", e.getMessage(), (long)0 );
						System.out.println( "Profiles " + gameVersion + " Error, " + "Save: FileStyle write error" + e.getMessage() );
					} finally {
						if ( player.slot>=0 && player.slot<=3 )
							slots[player.slot].fileStyle = true;
					}
				} else {
					Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Save: FileStyle external storage was not available","", (long)0 );
					System.out.println( "Profiles " + gameVersion + " Error, " + "Save: FileStyle external storage was not available" );
				}
			}

			// oldStyle
			try {
				OldStyleProfile profiles = new OldStyleProfile();
				
				String profiles_json = prefs.getString( "profiles", no_name );
				if ( !profiles_json.equals( no_name ) ) {
					profiles = gson.fromJson( profiles_json, OldStyleProfile.class );

					boolean containsProfile = false;
					
					Iterator<String> stringIt = profiles.profile_names.iterator();
					while( stringIt.hasNext() && !containsProfile ) {
						String thisName = stringIt.next();
						if ( thisName.equals( player.name ) )
							containsProfile = true;
					}
					
					if ( !containsProfile )
						profiles.profile_names.add( player.name );
					
				} else {
					Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Save: OldStyle Profiles was null", "", (long)0 );
					System.out.println( "Profiles " + gameVersion + " Error, " + "Save: OldStyle Profiles was null" );
					profiles.profile_names.add( player.name );
				}
					
				prefs.putString( "profiles", gson.toJson( profiles ) );
				
				prefs.putString( "profile_json_" + player.name, gson.toJson( player ) );
				prefs.flush();

			} catch ( Exception e ) {
				Assets.getResolver().trackerSendEvent("Profiles " + gameVersion + " Error", "Save: OldStyle write error", e.getMessage(), (long)0 );
				System.out.println( "Profiles " + gameVersion + " Error, " + "Save: OldStyle write error" + e.getMessage() );
			} finally {
				if ( player.slot>=0 && player.slot<=3 )
					slots[player.slot].oldStyle = true;
			}
			
			
			if ( player.slot>=0 && player.slot<=3 )
				saveSlot( player.slot );
		}
		
		
	}

	/** alter the profiles auto-load */
	public static boolean isAutoLoad() {
		if ( prefs==null )
			build();

		boolean auto_load = prefs.getBoolean( "auto_load", false );
		return auto_load;
	}
	
	public static void setAutoLoad( boolean autoload ) {
		if ( prefs==null )
			build();

		prefs.putBoolean( "auto_load", autoload );
		prefs.flush();
	}
	
	/** alter the profiles auto-load */
	public static PlayerInfo getAutoLoadInfo() {
		if ( prefs==null )
			build();

		String loadJson = prefs.getString( "autoLoadInfo", no_name );
		if ( !loadJson.equals( no_name ) ) {
			autoLoadInfo = gson.fromJson( loadJson, PlayerInfo.class );
		} else {
			autoLoadInfo = null;
		}
		
		return autoLoadInfo;
	}

	public static void setAutoLoadInfo( PlayerInfo info ) {
		if ( prefs==null )
			build();
		
		autoLoadInfo = info;
		
		String loadJson = gson.toJson( autoLoadInfo, PlayerInfo.class );
		prefs.putString( "autoLoadInfo", loadJson );

		prefs.flush();
		
		setAutoLoad( true );
	}
	
}
