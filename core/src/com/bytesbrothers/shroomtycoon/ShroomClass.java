package com.bytesbrothers.shroomtycoon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bytesbrothers.shroomtycoon.ScreenParts.ActionBarPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.ManageProfilesPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.PlayerPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.ProjectPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenHandler;
import com.bytesbrothers.shroomtycoon.ScreenParts.StrainPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.CasingDrawer;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FloatingText;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.pools.FloatingTextPool;
import com.bytesbrothers.shroomtycoon.structures.Objective;
import com.bytesbrothers.shroomtycoon.structures.Project;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

public class ShroomClass implements ApplicationListener {
	//----------------
	//		Screen properties and entities
	//----------------
	public Stage stage;
	//	public int width;
//	public int height;
	public InputMultiplexer multiplexer;

	public ScreenHandler screenHandler;

	public ArrayList<Dialog> dialogs = new ArrayList<Dialog>();
	public ArrayList<FloatingText> fTexts = new ArrayList<FloatingText>();
	public FloatingTextPool fTextPool = null;
	public boolean backed = false; // to manage the back key

	private double dUpdate = 0;

	public boolean cloudLoad = false;

	public Alert sideJob = null;
	public Alert samAlert = null;

	public CasingDrawer casingDrawer;
//	public ModelBatch modelBatch;

	/**
	 * random event generator variables
	 */
	public int reg_timer = 0;
	public int reg_next_event = 0; // num for timer to reach

	//	public boolean gotoNewGameScreen = false;
	public boolean needsUnpopulate = false;

	/** an interruption free way to process Skus */
	public ArrayList<String> skus = new ArrayList<String>();

	// ---------------------------------

	/*
	 * Background
	 */
	private Sprite background = null; // only appears if sam background is unlocked and being used

	private boolean needsTimeTravel = false;
	public float actionBarHeight = 0.14f;

	public ShroomClass ( AppResolver appResolver ) {
		Assets.setGameAndResolver( this, appResolver );
	}

	@Override
	public void create() {

		Assets.getResolver().startLoadScreen( "Loading" );

		Assets.loadAssets();
		Assets.paused = false;

//		loadingTexture = new Sprite( new Texture(Gdx.files.internal("data/loading.png") ) );
//		loadingSprite = new Sprite( new Texture(Gdx.files.internal("data/loadingSprite.png") ) );

		background = new Sprite( new Texture(Gdx.files.internal("data/Sam.png") ) );

//		modelBatch = new ModelBatch( new MyRenderableSorter() );
		casingDrawer = new CasingDrawer();


		Assets.width = Gdx.graphics.getWidth();
		Assets.height = Gdx.graphics.getHeight();

		stage = new Stage( new ScreenViewport( ) );
		Gdx.input.setInputProcessor(stage);

		Gdx.input.setCatchBackKey(true);

		Random random = new Random();
		// random event generator
		reg_next_event = 30 + random.nextInt( 270 ); // between 30 seconds and 300 seconds

		Assets.setPlayer( null );

		screenHandler = new ScreenHandler();

		screenHandler.setActivePart( MainPart.class );
		ActionBarPart actionPart = screenHandler.getScreenPart( ActionBarPart.class );
		screenHandler.actionBar = actionPart;

//		populate();

		if ( Profiles.isAutoLoad() ) {
			switch( Profiles.load( Profiles.getAutoLoadInfo() ) ) {
				case Profiles.LOADED:
					Profiles.timeTravel();
					break;
				case Profiles.CLOUD_WAIT:
				case Profiles.SNAPSHOT_WAIT:
					cloudLoad = true;
					break;
				case Profiles.FAILED:
					Assets.setPlayer( null );
					break;
			}
		}

		if ( Assets.player==null ) {
			System.out.println( "Manage Profiles needs to be pulled up" );
			ShroomClass game = Assets.game;
			screenHandler.activePart.setDx( -1f );

			ScreenPart screen = screenHandler.getScreenPart( ManageProfilesPart.class );
			screen.setDx( 0 );
			screenHandler.setActivePart( screen );

			ActionBarPart actionBarPart = screenHandler.getScreenPart( ActionBarPart.class );
			actionBarPart.setDy( -game.actionBarHeight );
		}

	}

	public void putCloudLoadPlayer( ShroomPlayer cloudPlayer ) {
//		if ( cloudPlayer!=null )
//			cloudPlayer.setGame( this );
//		else
		if ( cloudPlayer==null )
			System.out.println( "putCloudLoadPlayer was null" );
		Assets.setPlayer( cloudPlayer );
		needsTimeTravel = true;

//		ShroomClass game = Assets.game;
		screenHandler.setActivePartDx( -1f );
		ScreenPart screen = screenHandler.getScreenPart( MainPart.class );
		screen.setDx( 0 );

		ActionBarPart actionBarPart = screenHandler.getScreenPart( ActionBarPart.class );
		actionBarPart.setDy( 0 );

		screenHandler.setActivePart( screen );

		cloudLoad = false;
	}

	@Override
	public void dispose() {
		System.out.println( "disposing" );

		if (stage!=null) {
			try {
				stage.dispose();
				stage = null;
			} catch ( IllegalArgumentException ex ){
				ex.printStackTrace();
			} finally{}
		}

		if ( casingDrawer.modelBatch!=null ) {
			casingDrawer.modelBatch.dispose();
			casingDrawer.modelBatch = null;
		}

		if ( fTextPool!=null )
			fTextPool.shutdown();

		if ( Dialogs.dialogPool != null )
			Dialogs.dialogPool.shutdown();

		if ( screenHandler!=null )
			screenHandler.dispose();

		if ( casingDrawer.modelPool!=null )
			casingDrawer.modelPool.shutdown();

		Assets.dispose();

		if ( background!=null )
			background.getTexture().dispose();

	}

	@Override
	public void render() {

		float delta = Gdx.graphics.getDeltaTime();

		// update the AssetManager and find whether its done or not
		Assets.updateAssetManager( delta );

		com.badlogic.gdx.graphics.g2d.Batch batch = stage.getBatch();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor( .95f, .95f, .96f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if ( Assets.player!=null && Assets.player.useSamsBackground ) {
			batch.begin();
			float backHeight = Assets.height;
			float backWidth = Assets.height * background.getWidth()/background.getHeight();

			background.setBounds( Assets.width*0.5f - backWidth*0.5f,
					Assets.height*0.5f - backHeight*0.5f,
					backWidth, backHeight );
			background.draw( batch );
			batch.end();
		}

		if ( needsUnpopulate ) {
			for ( ScreenPart screenPart: screenHandler.screenParts ) {
				screenPart.clear();
				screenPart.unpopulate();
//				screenPart.needsResize = true;
				screenPart.populated = false;
			}
			needsUnpopulate = false;
//			System.out.println( "unpopulateAll" );
		}

		if ( checkLoaded() ) {
			if ( Assets.player!=null && needsTimeTravel ) {
				Profiles.timeTravel();
				needsTimeTravel = false;
			}

			stage.act( delta );

			if ( screenHandler.activePart.renderable )
				screenHandler.activePart.render();

			stage.draw();

			// remove the floating texts as they finish
			Iterator<FloatingText> floatIt = fTexts.iterator();
			while( floatIt.hasNext() ) {
				FloatingText firstText = floatIt.next();
				if ( firstText.getAlpha()<=0.0f || firstText.failSafe<=0 ){
					floatIt.remove(); // remove it from our list
					firstText.remove(); // remove it from the stage
					fTextPool.returnObject( firstText );
				}
			}

			Iterator<String> skuIt = skus.iterator();
			while ( skuIt.hasNext() ) {
				String thisSku = skuIt.next();
				processSku( thisSku );
				skuIt.remove();
			}

			Iterator<Dialog> dialogIt = dialogs.iterator();
			while ( dialogIt.hasNext() ) {
				Dialog dialog = dialogIt.next();
				if ( dialog.getParent()==null ) {
//				if ( dialog.getColor().a<=0 ) {
//					System.out.println( "Removing a dialog" );
					dialogIt.remove();
					Dialogs.dialogPool.returnObject( dialog );
//					System.out.println( "Dialog size: " + dialogs.size() );
				}
			}

			Assets.getResolver().endLoadScreen();
		}

		// we're still rendering and need to update time
		dUpdate += delta; // add elapsed seconds

		while ( dUpdate>=1.0 && Assets.player!=null ) {
			Assets.player.updateOneSec();

			// random event generator
			if ( Assets.assetsLoaded ) {
				reg_timer ++;

				if ( reg_timer>= reg_next_event && Assets.player.can_buy_strains.size()>0 )
					runRandomEventGenerator();
			}

			// update alerts
			for ( Alert thisAlert: Assets.alerts ) {
				thisAlert.updateOneSec();
			}

			dUpdate -= 1.0;
		}

		if ( Gdx.input.isKeyPressed(Input.Keys.BACK) ){
			if ( !backed  ) {
				backed = true;
				if ( !dialogs.isEmpty() ) {
					dialogs.get( dialogs.size() - 1 ).hide();
				} else if ( screenHandler.activePart!=null )
					screenHandler.activePart.back();
//        		else {
////        			System.out.println( "Trying to do exit Dialog" );
//        			Assets.getResolver().exitDialog();
//        		}
			}
		} else
			backed = false;

//		 System.out.println( "fps: " + Gdx.graphics.getFramesPerSecond() );

	}

	@Override
	public void resize( int width, int height ) {
		Assets.width = width;
		Assets.height = height;
//		System.out.println( "Resizing" );
		if ( !Assets.loaded )
			Assets.loadAssets();

		if ( stage == null )
			stage = new Stage( new ScreenViewport() );

		stage.getViewport().update( width, height, true );

		for ( Dialog dialog: dialogs ) {
			dialog.hide();
		}

		for ( ScreenPart screenPart: screenHandler.screenParts ) {
			screenPart.needsResize = true;
//			System.out.println( screenPart.header + "needsResize" );
//				screenPart.width = width;
//				screenPart.height = height;
		}

		updateInputs();


	}

	public void updateInputs() {
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor( stage );

//		for ( ScreenPart screenPart: screenHandler.screenParts ) {
////			screenPart.tryResize( width, height );
//
		InputProcessor myInput = screenHandler.activePart.getInput();
		if ( myInput!=null )
			multiplexer.addProcessor( myInput );
//		}

		Gdx.input.setInputProcessor( multiplexer );
	}

	@Override
	public void pause() {
		System.out.println( "pausing" );

		Assets.paused = true;
		Assets.unloadAssets();

//		unpopulateAll();
		for ( ScreenPart screenPart: screenHandler.screenParts )
//			screenPart.dispose();
			screenPart.pause();
//		System.out.println( "Dialogs: " +dialogs.size() );
		for ( Dialog dialog: dialogs ) {
//			System.out.println( "removing a dialog" );
			dialog.remove();
			Dialogs.dialogPool.returnObject( dialog );
		}
		dialogs.clear();
//		stage.clear();
//		screenParts.clear();
//		activePart = null;
//		populated = false;

		Profiles.save();
	}

	@Override
	public void resume() {
		System.out.println( "resuming" );

		Assets.getResolver().startLoadScreen( "Loading" );

		if ( !Assets.loaded )
			Assets.loadAssets();
		Profiles.timeTravel();

//		System.out.println( "Dialogs: " +dialogs.size() );
		for ( Dialog dialog: dialogs ) {
			dialog.remove();
//			System.out.println( "removing a dialog" );
			Dialogs.dialogPool.returnObject( dialog );
		}
		dialogs.clear();

		for ( FloatingText firstText: fTexts ) {
//    		fTexts.remove( firstText );
			firstText.remove(); // remove it from the stage
			fTextPool.returnObject( firstText );
		}
		fTexts.clear();

		Assets.paused = false;

//		if ( !populated )
//			populate();

		for ( ScreenPart screenPart: screenHandler.screenParts )
			screenPart.resume();
	}

	/** checks whether the atlases needed for this screen have been loaded
	 * @return true if necessary assets have been loaded
	 */
	public boolean checkLoaded() {
		if ( Assets.paused )
			return false;
		if ( !Assets.loaded )
			return false;
		if ( Assets.assetsLoaded )
			return true;
		AssetManager manager = Assets.getAssetManager();

		if ( !manager.isLoaded( "data/style.pack" ) )
			return false;

		if ( cloudLoad )
			return false;

		return true;
	}

	/** shows this dialog on the stage and adds it to the SimpleScreen's ArrayList<>() */
	public void showDialog( Dialog dialog ) {
//    	System.out.println( "Adding a dialog" );
		dialogs.add( dialog );
		dialog.show( stage );

//    	System.out.println( "Dialog size: " + dialogs.size() );
	}

	public void addFloatingText( FloatingText fText ) {
		while ( fTexts.size()>ST.MAX_FLOATING_TEXTS ) {
			// remove the first fText until there is room
			fTexts.remove( 0 );
		}
		Color color = fText.getColor();
		fText.setColor( color.r, color.g, color.b, 1.0f );
		fText.setWidth( Assets.width * 0.25f );
		fText.setHeight( Assets.height * 0.1f );
		fTexts.add( fText );
		stage.addActor( fText );
		Tween.to( fText, FloatingText.FloatingTextTween.Y, 1.0f )
				.target( Assets.height/9.0f * 2.0f )
				.ease(TweenEquations.easeOutQuad)
				.start( Assets.getTweenManager() ); //** start it

		Tween.to( fText, FloatingText.FloatingTextTween.ALPHA, 1.0f )
				.target( 0.0f )
				.delay( 0.7f )
				.ease(TweenEquations.easeInQuad)
				.start( Assets.getTweenManager() ); //** start it

		fText.failSafe = 2.0f;
	}

	/** takes a purchase SKU and applies it */
	public void applySku( String SKU ) {
		if ( SKU.equals( ST.SKU_FIVE ) )
			skus.add( SKU );
		else if ( SKU.equals( ST.SKU_TWENTY ) )
			skus.add( SKU );
		else if ( SKU.equals( ST.SKU_THIRTY_NINE ) )
			skus.add( SKU );
		else if ( SKU.equals( ST.SKU_FIFTY ) )
			skus.add( SKU );
		else if ( SKU.equals( ST.SKU_HUNDRED ) )
			skus.add( SKU );
		else if ( SKU.equals( ST.SKU_MONDAY ) )
			skus.add( SKU );
	}
	private void processSku( String SKU ) {
		// should be good because this wouldn't be called except from render->check_style_loaded
		int num = -1;
		if ( SKU.equals( ST.SKU_FIVE ) )
			num = 5;
		else if ( SKU.equals( ST.SKU_TWENTY ) )
			num = 20;
		else if ( SKU.equals( ST.SKU_THIRTY_NINE ) )
			num = 39;
		else if ( SKU.equals( ST.SKU_FIFTY ) )
			num = 50;
		else if ( SKU.equals( ST.SKU_HUNDRED ) )
			num = 100;
		else if ( SKU.equals( ST.SKU_MONDAY ) )
			num = 100;

		if ( num!=-1 ) {
			System.out.println( "Successfully purchased " + num + " Blue Coins!");
			Dialogs.blueCoinDialog( "You purchased " + num + " Blue Coins!", num );
			Assets.getResolver().trackerSendEvent( "In-App Purchase", "Purchase", "" + num + " BC" + (SKU.equals(ST.SKU_MONDAY)?" MM":""), (long)0 );
		}
	}



	public void runRandomEventGenerator() {
		reg_timer = 0;
		Random random = new Random();
		reg_next_event = 20 + random.nextInt( 100 ); // between 20 seconds and 120 seconds
//		final ShroomPlayer Assets.player = bb.junk.shroomtycoon2.Assets.getPlayer();
		if ( !Assets.player.givenNewPlayerBlueCoins ) {
			Assets.player.givenNewPlayerBlueCoins = true;
			samAlert = Assets.alertPool.borrowObject();
			samAlert.reset();
			samAlert.icon = "SpeechIcon";
			samAlert.title = "Sam Sincaid";
			samAlert.message = "Sam sent you a text";
			samAlert.type = ST.SPEECH_DIALOG;
			samAlert.dialog_title = "Sam Sincaid";
			samAlert.dialog_text = "Hey, you seem like you could use some help so here: " +
					"these are 'Blue Coins,' they do all kinds of things like speed mushrooms up. You can thank me later!";
			Assets.addAlert( samAlert );
			Assets.player.changeCoins( 10 );

		} else if ( Assets.player.reg_last_batch!= -1 && random.nextFloat()>0.5f ) {
			// if there's a last batch and %50 chance

			// feedback on a sale
			String message = "";
			switch( Assets.player.reg_last_batch ) {
				case 0:
					switch( random.nextInt( 5 ) ) {
						case 0: message = "Those mushrooms were bunk."; break;
						case 1: message = "Can you get better mushrooms?"; break;
						case 2: message = "Were those even psychedelic mushrooms?"; break;
						case 3: message = "I had a lot of people complaining about those last ones."; break;
						case 4: message = "Nobody tripped off of the mushrooms you gave me."; break;
					}
					break;
				case 1:
					switch( random.nextInt( 6 ) ) {
						case 0: message = "Those mushrooms were alright."; break;
						case 1: message = "I've had better, but I've also had worse."; break;
						case 2: message = "People seem to like the mushrooms you sell."; break;
						case 3: message = "Keep up the average work."; break;
						case 4: message = "When it comes to shrooms, you really know how to leave the bar exactly where it is."; break;
						case 5: message = "Did you test these before you gave them to me? They weren't bad."; break;
					}
					break;
				case 2:
					switch( random.nextInt( 6 ) ) {
						case 0: message = "Those mushrooms were great!"; break;
						case 1: message = "I've never sold anything like those! Thank you!"; break;
						case 2: message = "People seem to REALLY like the mushrooms you sell!"; break;
						case 3: message = "Those shrooms were so potent that my dog started talking to me."; break;
						case 4: message = "I had one peron complaining that those shrooms were TOO strong. I don't talk to them anymore."; break;
						case 5: message = "Was there anything extra in that batch? Because wow!"; break;
					}
					break;
			}
			Alert alert = new Alert();
			alert.icon = "SpeechIcon";
			alert.title = "Dealer Feedback";
			alert.message = Assets.player.reg_dealer_name + " has something to say about the shrooms";
			alert.type = ST.SPEECH_DIALOG;
			alert.dialog_title = Assets.player.reg_dealer_name;
			alert.dialog_text = message;
			Assets.addAlert( alert );
			Assets.player.reg_last_batch = -1;
		} else if ( random.nextFloat()>=0.9997f && !Assets.player.has_reg_ocean_body ) {
			// %.03 chance of unlocking Ocean Body
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.icon = "SyringeIcon";
			alert.title = "Strain: Ocean Body";
			alert.message = "A new strain has been unlocked: Ocean Body";
			Assets.player.has_reg_ocean_body = true;
			Assets.player.can_buy_strains.add( "Ocean Body" );
			alert.type = ST.TABLE;
//			alert.partName = "Strain";
			alert.nextPart = StrainPart.class;
//			alert.screenPart = Assets.game.getScreenPart( "Strain" );
			alert.partObject = StrainMaster.getStrain( "Ocean Body" );
//			alert.table = new StrainColumn( StrainMaster.getStrain( "Ocean Body" ) );
//			alert.table = new StrainScreenTable( getStrain("Ocean Body") );
//			alert.screen = new HUDScreen( new StrainScreenTable( getStrain("Ocean Body") ) );
			Assets.addAlert( alert );
		} else if ( random.nextFloat()>=0.9998f && !Assets.player.has_reg_fun_guy ) {
			// %.02 chance of unlocking Fun Guy
			Assets.player.has_reg_fun_guy = true;
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.icon = "SyringeIcon";
			alert.title = "Strain: Fun Guy";
			alert.message = "A new strain has been unlocked: Fun Guy";
			Assets.player.can_buy_strains.add( "Fun Guy" );
			alert.type = ST.TABLE;
//			alert.screenPart = Assets.game.getScreenPart( "Strain" );
//			alert.partName = "Strain";
			alert.nextPart = StrainPart.class;
			alert.partObject = StrainMaster.getStrain( "Fun Guy" );
//			alert.table = new StrainColumn( StrainMaster.getStrain( "Fun Guy" ) );
//			alert.table = new StrainScreenTable( getStrain("Fun Guy") );
//			alert.screen = new HUDScreen( new StrainScreenTable( getStrain("Fun Guy") ) );
			Assets.addAlert( alert );
		} else if ( Assets.player.reg_discount.equals("") && random.nextFloat()>=0.8f ) {
			// if there is not already a discount %20 chance of discount
			Assets.player.reg_discount = Assets.player.can_buy_strains.get( random.nextInt( Assets.player.can_buy_strains.size() ) );
			switch( random.nextInt( 15 ) ) {
				case 0: case 1: case 2: case 3: case 4:
					Assets.player.reg_discount_percent = 0.1f;
					Assets.player.reg_discount_timer = 60*10 + random.nextInt( 90*60 ); // between 10 mins and 100 mins
					break;
				case 5: case 6: case 7: case 8:
					Assets.player.reg_discount_percent = 0.2f;
					Assets.player.reg_discount_timer = 60*8 + random.nextInt( 72*60 ); // between 8 mins and 80 mins
					break;
				case 9: case 10: case 11:
					Assets.player.reg_discount_percent = 0.3f;
					Assets.player.reg_discount_timer = 60*6 + random.nextInt( 54*60 ); // between 6 mins and 60 mins
					break;
				case 12: case 13:
					Assets.player.reg_discount_percent = 0.4f;
					Assets.player.reg_discount_timer = 60*2 + random.nextInt( 18*60 ); // between 2 mins and 20 mins
					break;
				case 14:
					Assets.player.reg_discount_percent = 0.5f;
					Assets.player.reg_discount_timer = 60*1 + random.nextInt( 9*60 ); // between 1 mins and 10 mins
					break;
			}
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.timer = Assets.player.reg_discount_timer; // exactly as long as the sale is
			alert.icon = "DiscountIcon";
			alert.title = "Limited Time Offer";
			alert.message = Assets.player.reg_discount + " is on sale";
			alert.type = ST.TABLE;
//			alert.screenPart = Assets.game.getScreenPart( "Strain" );
//			alert.partName = "Strain";
			alert.nextPart = StrainPart.class;
			alert.partObject = StrainMaster.getStrain( Assets.player.reg_discount );
//			alert.table = new StrainColumn( StrainMaster.getStrain( Assets.player.reg_discount ) );
//			alert.table = new StrainScreenTable( getStrain( Assets.player.reg_discount ) );
//			alert.screen = new HUDScreen( new StrainScreenTable( getStrain( Assets.player.reg_discount ) ) );
			Assets.addAlert( alert );
		} else if ( Assets.player.objective==null && random.nextFloat()>0.5f ) {
			// if Assets.player doesn't have an objective: %50 of new objective
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.icon = "ObjectiveIcon";
			alert.title = "New Objective";
			alert.nextPart = PlayerPart.class;
//			alert.partName = "Player";
//			alert.type = ST.MAIN_COLUMN;
//			alert.mainColumn = MainPart.PLAYER;
			alert.type = ST.TABLE;
//			alert.table = new PlayerScreenTable();
//			alert.screen = new HUDScreen( new PlayerScreenTable() );

			Assets.player.objective = new Objective();
			switch( random.nextInt( 3 + ( Assets.player.canUpgradeIncOrFc() || Assets.player.canUpgradePc() ? 1: 0 ) ) ) {
				case 0:
					// sell a specific strain
					Assets.player.objective.type = Assets.player.objective.STRAIN;
					// pick a strain from can_buy or custom strains
					if ( random.nextBoolean() || Assets.player.strains.size()==0 ) {
						Assets.player.objective.text = Assets.player.can_buy_strains.get( random.nextInt( Assets.player.can_buy_strains.size() ) );
					} else {
						Assets.player.objective.text = Assets.player.strains.get( random.nextInt( Assets.player.strains.size() ) ).name;
					}
					Assets.player.objective.definition = "Sell any amount of " + Assets.player.objective.text;
					break;
				case 1:
					// hire or fire dealer
					// if dealer capacity is full -> fire a dealer
					if ( Assets.player.dealers.size()>= Assets.player.getMaxDealers() ) {
						Assets.player.objective.type = Assets.player.objective.FIRE;

						if ( random.nextBoolean() ) {
							// text is "" and firing any dealer will complete objective
							Assets.player.objective.text = "";
							Assets.player.objective.definition = "Fire one of your dealers.";
						} else {
							// text includes name of particular dealer
							Assets.player.objective.text = Assets.player.dealers.get( random.nextInt( Assets.player.dealers.size() ) ).name;
							Assets.player.objective.definition = "Fire " + Assets.player.objective.text + " Nobody likes them.";
						}
					} else {
						// if dealer capacity is not full -> hire a dealer
						Assets.player.objective.type = Assets.player.objective.HIRE;
						Assets.player.objective.definition = "Hire on a new dealer.";
					}
//				SimpleScreen currentScreen = (SimpleScreen)getScreen();
//				if ( currentScreen.isHUDScreen ) {
//					((HUDScreen)currentScreen).changeTable( new DealersScreenTable( (HUDScreen)currentScreen ));
//				} else {
//
//				}
					alert.type = ST.MAIN_COLUMN;
					alert.mainColumn = MainPart.DEALERS;
//				alert.table = new DealersScreenTable( );
//				alert.screen = new HUDScreen( new DealersScreenTable( ) );
					break;
				case 2:
					// (harvest and) weigh batch of X or more
					Assets.player.objective.type = Assets.player.objective.HARVEST;
					Assets.player.objective.text = "" + ( 20 + random.nextInt(8) );
					Assets.player.objective.definition = "Weigh a batch of at least " + Assets.player.objective.text + "g.";
//				alert.table = new DehydScreenTable();
					alert.type = ST.MAIN_COLUMN;
					alert.mainColumn = MainPart.DEHYDRATORS;
//				alert.screen = new DehydScreen();
					break;
				case 3:
					// upgrade inc of fc or upgrade pc of dehydrator
					Assets.player.objective.type = Assets.player.objective.UPGRADE;
					// check that there is at least 1 upgrade available
					if ( Assets.player.canUpgradeIncOrFc() || Assets.player.canUpgradePc() ) {
						boolean found_one = false;
						while ( !found_one ) {
							switch ( random.nextInt( 4 ) ) {
								case 0:
									// inc temp upgrade
									if ( Assets.player.inc_temp!=ST.HIGH ) {
										found_one = true;
										Assets.player.objective.text = "Inc Temp";
										Assets.player.objective.definition = "Upgrade the thermostat in the incubator";
										alert.type = ST.MAIN_COLUMN;
										alert.mainColumn = MainPart.INC_SETTINGS;
//								alert.type = ST.TABLE;
//								alert.table = new IncSettingsScreenTable();
//								alert.screen = new IncSettingsScreen();
									}
									break;
								case 1:
									// fc temp upgrade
									if ( Assets.player.fc_temp!=ST.HIGH ) {
										found_one = true;
										Assets.player.objective.text = "Fc Temp";
										Assets.player.objective.definition = "Upgrade the thermostat in the fruiting chamber";
//								alert.type = ST.TABLE;
//								alert.table = new FcSettingsScreenTable();
										alert.type = ST.MAIN_COLUMN;
										alert.mainColumn = MainPart.FC_SETTINGS;
//								alert.screen = new FcSettingsScreen();
									}
									break;
								case 2:
									// fc humidity upgrade
									if ( Assets.player.fc_humidity<4 ) {
										found_one = true;
										Assets.player.objective.text = "Fc Humidity";
										Assets.player.objective.definition = "Upgrade the humidifier in the fruiting chamber";
										alert.type = ST.MAIN_COLUMN;
										alert.mainColumn = MainPart.FC_SETTINGS;
//								alert.screen = new FcSettingsScreen();
//								alert.type = ST.TABLE;
//								alert.table = new FcSettingsScreenTable();
									}
									break;
								case 3:
									// pc capacity upgrade
									if ( Assets.player.canUpgradePc() ) {
										found_one = true;
										Assets.player.objective.text = "Pc capacity";
										Assets.player.objective.definition = "Upgrade a pressure cooker's capacity";
										alert.type = ST.MAIN_COLUMN;
										alert.mainColumn = MainPart.PRESSURE_COOKERS;
//								alert.type = ST.TABLE;
//								alert.table = new PcSettingsScreenTable();
//								alert.screen = new PcSettingsScreen();
									}
									break;
							}
						}
					}

					break;
			}

			alert.message = Assets.player.objective.definition;
			Assets.player.objective_alert = alert;
			Assets.addAlert( alert );
			Assets.getResolver().trackerSendEvent("Objective", "Given", Assets.player.objective.text, (long)0 );
//			appResolver.trackerSend( MapBuilder.createEvent("Objective", "Given", Assets.player.objective.text, (long)0 ).build() );

		} else if (random.nextFloat()>0.7f && (samAlert==null || !samAlert.show) ) {
			// %30 chance of a small message from Sam
			String message = "";
			switch( random.nextInt( 7 ) ) {
				case 0: message = "Hey you, just wanted to";
					switch( random.nextInt( 5 ) ) {
						case 0: message += " say what's up."; break;
						case 1: message += " see how you were doing."; break;
						case 2: message += " tell you that you are one of a kind. :)"; break;
						case 3: message += " let you know that I was going to the bar tonight. You should come!"; break;
						case 4: message += " see how your day was."; break;
					}
					break;
				case 1: message = "You're not going to believe this: ";
					switch( random.nextInt( 6 ) ) {
						case 0: message += "I locked myself out of my house!"; break;
						case 1: message += "I've started seeing someone!"; break;
						case 2: message += "remember when I told you I started seeing someone? Yeah, that didn't last very long..."; break;
						case 3: message += "I managed to ollie up a curb!"; break;
						case 4: message += "I did mushrooms last night and I think they were some from you!"; break;
						case 5: message += "some assholes wearing masks broke into my house last night! They stole my hash and my bong!"; break;
					}
					break;
				case 2: message = "Remember that time ";
					switch( random.nextInt( 6 ) ) {
						case 0: message += "I kicked your ass at Monopoly?"; break;
						case 1: message += "we went to the bar and got in a fight with some rednecks?"; break;
						case 2: message += "you got busted and went to jail?"; break;
						case 3: message += "I tricked you into admiting you had sex with that fat chick?"; break;
						case 4: message += "we did acid in a hotel room after a rave with a bunch of strangers?"; break;
						case 5: message += "I fronted Tod a half-pound and he never paid me back?"; break;
					}
					message += " Yeah, good times...";
					break;
				case 3: message = "Did you know ";
					switch( random.nextInt( 6 ) ) {
						case 0: message += "your left tail-light was out?"; break;
						case 1: message += "Nick R. was looking for you?"; break;
						case 2: message += "another dealer has much cheaper mushrooms for sale?"; break;
						case 3: message += "about this 'Dubstep' thing?"; break;
						case 4: message += "that cops aren't allowed to lie to you about anything? A cop told me that."; break;
						case 5: message += "that mushrooms grow from spores?"; break;
					}
					break;
				case 4: message = "Help! ";
					switch( random.nextInt( 6 ) ) {
						case 1: message += "I can't find the remote!"; break;
						case 2: message += "I think I smoked too much!"; break;
						case 3: message += "I can't find my cat!"; break;
						case 4: message += "My ex keeps texting me!"; break;
						default: message += "I need some mushrooms!"; break;
					}
					break;
				case 5: message = "Hey, ";
					switch( random.nextInt( 6 ) ) {
						case 0: message += "if I start sending you nonsense messages it's because I'm about to start tripping."; break;
						case 1: message += "did you get that picture of a hamburger I sent you?"; break;
						case 2: message += "I think I left my phone at your house. Lol, no wait..."; break;
						case 3: message += "forward this message to five more people or at 1:13 the turkey on your sandwich will become slightly dry."; break;
						case 4: message += "this is my new number, save it bitches!"; break;
						case 5: message += "have you heard about the strain called ";
							if ( random.nextBoolean() || Assets.player.strains.size()==0 ) {
								message += Assets.player.can_buy_strains.get( random.nextInt( Assets.player.can_buy_strains.size() ) );
							} else
								message += Assets.player.strains.get( random.nextInt( Assets.player.strains.size() ) ).name;
							message += "? They're supposedly pretty dank.";
							break;
					}
					break;
				case 6: message = "So, ";
					switch( random.nextInt( 5 ) ) {
						case 0: message += "I couldn't find my cat all yesterday. Turns out he was playing a one person game of 'Trapped in the Pantry.'"; break;
						case 1: message += "you wanna play some vidja games later?"; break;
						case 2: message += "how about that money you owe me?"; break;
						case 3: message += "my dad offered me a job, which I want/need, but they do random drug tests. fml"; break;
						case 4: message += "I literally watched tv all day today. You wanna chill? Maybe watch some tv?"; break;
					}
					break;
			}
			samAlert = new Alert();
			samAlert.icon = "SpeechIcon";
			samAlert.title = "Sam Sincaid";
			samAlert.message = "Sam sent you a text";
			samAlert.type = ST.SPEECH_DIALOG;
			samAlert.dialog_title = "Sam Sincaid";
			samAlert.dialog_text = message;
			Assets.addAlert( samAlert );
		}

//		Profiles.autoSave();
		// something with a dealer
	}


	public void startProjectPart( Project project ) {
//		ProjectPart projectPart = (ProjectPart)screenHandler.getScreenPart( "Project" );
		ProjectPart projectPart = screenHandler.getScreenPart( ProjectPart.class );
		projectPart.setProject( project );
		projectPart.setDx( 0 );

		screenHandler.activePart.setDx( -1f );

		screenHandler.setActivePart( projectPart );

//		ActionBarPart actionBarPart = (ActionBarPart) getScreenPart( "Action Bar");
		ActionBarPart actionBarPart = screenHandler.getScreenPart( ActionBarPart.class );
		actionBarPart.setDy( -actionBarHeight );
	}
}
