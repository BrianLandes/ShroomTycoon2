package com.bytesbrothers.shroomtycoon;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bytesbrothers.shroomtycoon.ScreenParts.ActionBarPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenHandler;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.FloatingText;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.elements.SpriteTween;
import com.bytesbrothers.shroomtycoon.pools.AlertPool;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.PressureCooker;
import com.bytesbrothers.shroomtycoon.structures.Project;

import java.util.ArrayList;

public class Assets {

	private static AssetManager manager;
	private static Skin skin;
	public static boolean assetsLoaded = false;
	public static boolean paused = true;
	public static boolean loaded = false;
	public static boolean isMonday = false;
	
	public static AlertPool alertPool = new AlertPool();
	public static ArrayList<Alert> alerts = new ArrayList<Alert>();
	
	private static AppResolver appResolver;
	public static ShroomPlayer player;
	public static ShroomClass game;
	private static TweenManager tweenManager;
	private static TextureAtlas fruitAtlas = null;
	public static ModelBuilder modelBuilder;
	
	public static int width = 0;
	public static int height = 0;
	
	//------------------------------
	//		Constants
	//------------------------------
	/** Screen transition time in seconds.*/
	public static final float TRANS_TIME = 0.5f;
	
	public static void loadAssets() {
//		if ( _instance==null )
//			_instance = new Game();
		
//		System.out.println( "loadAssets" );
//		Resolution _320x480 = new Resolution(320, 480, "320x480" );
//		Resolution _480x800 = new Resolution(480, 800, "480x800" );
//		Resolution _720x1280 = new Resolution(720, 1280, "720x1280" );
//		Resolution _1080x1920 = new Resolution(1080, 1920, "1080x1920" );
//		ResolutionFileResolver resolver = new ResolutionFileResolver( new InternalFileHandleResolver()
//		,
//				_320x480, _480x800,
//				_720x1280,
//				_1080x1920
//				);
		
		// Tweening
		setTweenManager( new TweenManager() );
		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor( Sprite.class, new SpriteTween());
		Tween.registerAccessor( ScreenPart.class, new ScreenPart.ScreenPartTween());
		Tween.registerAccessor( Batch.class, new Batch.BatchTween());
		Tween.registerAccessor( PressureCooker.class, new PressureCooker.PressureCookerTween());
		Tween.registerAccessor( FloatingText.class, new FloatingText.FloatingTextTween());
		Tween.registerAccessor( ActionBarPart.ActionBar.class, new ActionBarPart.ActionBarTween());
//		Tween.registerAccessor( Table.class, new TableTween());
		Tween.registerAccessor( Project.class, new Project.ProjectTween());
		
		// initialize AssetManager
		manager = new AssetManager();
//		manager.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
		
		// Initialize skin
		manager.load( "data/style.pack", TextureAtlas.class);
				
		// load all the fruits and casings
		manager.load( "data/fruits.pack", TextureAtlas.class);
		manager.load( "data/casings.pack", TextureAtlas.class);
		
		// load all the jars
		manager.load( "data/jars.pack", TextureAtlas.class);
		
		if ( modelBuilder==null )
			modelBuilder = new ModelBuilder();
		
		
//		// load all the minigame graphics
//		manager.load( "data/minigames.pack", TextureAtlas.class);
//		
//		// load all the backgrounds
//		manager.load( "data/backgrounds.pack", TextureAtlas.class);
		
		fruitAtlas = null;
		
		assetsLoaded = false;
		loaded = true;
	}

	public static void unloadAssets() {
//		if ( _instance==null )
//			_instance = new Game();

		loaded = false;
		if ( manager!=null )
			manager.clear();
		assetsLoaded = false;
		skin = null;
		fruitAtlas = null;
	}
	
	public static void updateAssetManager() {
		updateAssetManager( Gdx.graphics.getDeltaTime() );
	}
	public static void updateAssetManager( float delta ) {
		if ( !loaded || manager==null )
			loadAssets();
		
		//update the tween manager
		tweenManager.update( delta );
		
		assetsLoaded = manager.update();
		
		// we can still make a skin from style
		if ( manager.isLoaded( "data/style.pack" ) ) {
			if ( skin==null )
				skin = new Skin( Gdx.files.internal("data/style.json"), manager.get( "data/style.pack", TextureAtlas.class ) );
			
		} else
			skin = null;

	}
	
	public static AssetManager getAssetManager() {
		if ( manager==null )
			loadAssets();
		
		return manager;
	}
	
	public static Skin getSkin() {
//		if ( _instance==null )
//			_instance = new Game();
		// we can still make a skin from style
		if ( !loaded )
			loadAssets();

		while ( !manager.isLoaded( "data/style.pack" ) ) {
			manager.update();
			skin = null;
		}
		if ( skin==null )
			skin = new Skin( Gdx.files.internal("data/style.json"), manager.get( "data/style.pack", TextureAtlas.class ) );
		return skin;
	}
	
	public static TextureAtlas getAtlas() {
		if ( !loaded )
			loadAssets();

		while ( !manager.isLoaded( "data/style.pack" ) ) {
			manager.update();
		}
		return manager.get( "data/style.pack", TextureAtlas.class );
	}
	
	public static TextureAtlas getFruitsAtlas() {
		if ( !loaded )
			loadAssets();

		if ( fruitAtlas!=null )
			return fruitAtlas;
		
		while ( !manager.isLoaded( "data/fruits.pack" ) ) {
			manager.update();
		}
		fruitAtlas = manager.get( "data/fruits.pack", TextureAtlas.class );
		return fruitAtlas;
	}
	
	public static TextureRegion getFruitPart(ST.FruitLayer layer, int type, int frame ) {
		if ( fruitAtlas == null ) {
			getFruitsAtlas();
		}
		
		switch( layer ) {
		case BASE:
			return fruitAtlas.findRegion( ST.FRUIT_NAMES[ type ] + "-" + frame );
		case SPOTS:
			return fruitAtlas.findRegion( ST.FRUIT_NAMES[ type ] + "Spots-" + frame );
		case TOP:
			return fruitAtlas.findRegion( ST.FRUIT_NAMES[ type ] + "Top-" + frame );
		case TOP_OVER:
			return fruitAtlas.findRegion( ST.FRUIT_NAMES[ type ] + "TopOver-" + frame );
		default:
			return null;
		}
	}
	
	public static TextureAtlas getJarsAtlas() {
		if ( !loaded )
			loadAssets();

		while ( !manager.isLoaded( "data/jars.pack" ) ) {
			manager.update();
		}
		return manager.get( "data/jars.pack", TextureAtlas.class );
	}
	
	public static TextureAtlas getCasingsAtlas() {
		if ( !loaded )
			loadAssets();

		while ( !manager.isLoaded( "data/casings.pack" ) ) {
			manager.update();
		}
		return manager.get( "data/casings.pack", TextureAtlas.class );
	}
	
//	public static TextureAtlas getBackgrounds() {
////		if ( _instance==null )
////			_instance = new Game();
//		if ( paused )
//			loadAssets();
//
//		while ( !manager.isLoaded( "data/backgrounds.pack" ) ) {
//			manager.update();
//		}
//		return manager.get( "data/backgrounds.pack", TextureAtlas.class );
//	}
	
	
//	public static boolean isAssetsLoaded() {
////		if ( _instance==null )
////			_instance = new Game();
//		
//		return assetsLoaded;
//	}
//	
//	public static boolean isPaused() {
//		if ( _instance==null )
//			_instance = new Game();
//		
//		return _instance.paused;
//	}
	
	public static void dispose() {
//		if ( _instance==null )
//			_instance = new Game();

		if ( manager!=null ) {
			manager.dispose();
			manager = null;
		}
		if ( skin!=null ) {
			skin.dispose();
			skin = null;
		}
		
		alertPool.shutdown();
	}

//	public static boolean isMonday() {
////		if ( _instance==null )
////			_instance = new Game();
//		return isMonday;
//	}

//	public static void setMonday(boolean isMonday) {
////		if ( _instance==null )
////			_instance = new Game();
//		_instance.isMonday = isMonday;
//	}
//	
//	public static void addTable( ColumnTable table ) {
//		game.addTable( table );
//	}
	
	public static void clearAlerts() {
		alerts.clear();
		if ( player!=null )
			player.useAlerts();
	}
	
//	public static ArrayList<Alert> getAlerts() {
//		return alerts;
//	}
	
	public static boolean removeAlert( Alert alert ) {
		alertPool.returnObject( alert );
		return alerts.remove( alert );
	}
	
	public static void addAlert( Alert alert ) {
		alerts.add( alert );
	}
	
	public static Alert hasAlertWithOrigin( Object origin ) {
		for ( Alert alert: alerts )
			if ( alert.origin!=null && alert.origin.equals( origin ) )
				return alert;

		return null;
	}
	
	public static Alert hasAlertWithID( int id ) {
		for ( Alert alert: alerts )
			if ( alert.uniqueID == id )
				return alert;

		return null;
	}

	public static AppResolver getResolver() {
		return appResolver;
	}

	public static void setResolver( AppResolver newAppResolver ) {
		appResolver = newAppResolver;
	}

//	public static ShroomPlayer getPlayer() {
//		return player;
//	}

	public static void setPlayer( ShroomPlayer newPlayer ) {
//		if ( newPlayer!=null
////				&& newPlayer.getGame()==null 
//				)
//			newPlayer.setGame( game );
		game.needsUnpopulate = true;
//		game.unpopulateAll();
		player = newPlayer;
	}

//	public static ShroomClass getGame() {
//		return game;
//	}
	
	public static ScreenHandler getScreenHandler() {
		return game.screenHandler;
	}

	public static void setGameAndResolver( ShroomClass newGame, AppResolver newAppResolver ) {
		game = newGame;
		appResolver = newAppResolver;
	}
	
	public static void setGame( ShroomClass newGame ) {
//		if ( _instance==null )
//			_instance = new Game();
		game = newGame ;
	}
	
	/**
	 * Fetches the default scrollPaneStyle and returns the backgrounds left and right widths plus the scroll bar's width
	 * @return
	 */
	public static float getScrollPaneWidth() {
		ScrollPaneStyle style = getSkin().get( ScrollPaneStyle.class );
		float width = 0.0f;
		if ( style.background!=null )
			width += style.background.getLeftWidth() + style.background.getRightWidth();
		if ( style.vScrollKnob!=null )
			width += style.vScrollKnob.getMinWidth();
		return width;
	}
	
//	public static boolean isTwoColumns() {
//		if ( game.width>game.height )
//			return true;
//		return false;
//	}

	public static TweenManager getTweenManager() {
		if ( !loaded )
			loadAssets();
		return tweenManager;
	}

	public static void setTweenManager(TweenManager tweenManager) {
		Assets.tweenManager = tweenManager;
	}
	
	public static InputListener stopTouchDown() {
		return new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				event.stop();
				return false;
			}
		};
	};
	
	
}
