package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.PulleyJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.Ease;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.FloatingText;
import com.bytesbrothers.shroomtycoon.elements.FruitActor;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.pools.FloatingTextPool;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Fruit;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;


public class WeighPart extends ScreenPart {

	private static final float WORLD_TO_BOX = 0.01f;
	private static final float BOX_TO_WORLD = 100f;
	
	private ShroomClass game;

//	public Camera cam;
	TextureAtlas fruitAtlas;
	public Batch theBatch = null;
	
	float risingWeight = 0.0f;
	float totalWeight = 0.0f;
	float totalTime = 0.0f;
	float currentTime = 0.0f;
	float updateDelta = 0.0f;
	Image image = new Image();
	
	World world;
	private Body levelBody;
	float weight = 0.0f;
	private Body weightBody;
	
	private Sprite levelSprite;
	private Array<Body> bodyArray;
	private SpriteBatch spriteBatch;

	public WeighPart( Batch batch ) {
		super( "Weigh" );
		setBatch( batch );
		renderable = true;
//	    cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    spriteBatch = new SpriteBatch();
	}
	
	@Override
	public void populate() {
		clear();
		updateDelta = 0;
		
		if ( theBatch!=null ) {
			theBatch.useAlert();
			
			risingWeight = 0.0f;
			totalWeight = theBatch.getWeight();
			totalTime = totalWeight * 0.6f;
			currentTime = 0.0f;
			
			fruitAtlas = Assets.getFruitsAtlas();

			ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
			add( button.fitButton );
			row();
			buttons.add( button );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				back();
	 			}
	 		});
			
			add( image );
			row();
			
			ScreenLabel label = new ScreenLabel( new FitLabel( "Weight", skin ) ) {
				@Override
				public void act( float delta ) {
					currentTime += delta;
					if ( currentTime>=totalTime ) currentTime = totalTime;
					risingWeight = Ease.ease( Ease.OUT_CUBIC, currentTime, 0.0f, totalWeight, totalTime );
					
					if ( label!=null )
						label.setText( "" + ST.withComasAndTwoDecimals(risingWeight) + "g" );
				}
				@Override
				public void resize( int width, int height ) {
					width *= 0.5f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.center );
			label.scaleY = 0.9f;
			add( label.label );
			labels.add( label );
			
		}
	}
	
	public void createWorld( float w, float h ) {
		if ( world!=null ) {
			world.dispose();
			world = null;
		}
		
		world = new World( new Vector2(0, -9.8f), true );

		// static base A
		BodyDef scaleDef = new BodyDef();
		scaleDef.position.set(new Vector2( 0.0f, -h * 0.55f * WORLD_TO_BOX ) );
		Body scaleBody = world.createBody(scaleDef);
		PolygonShape scaleShape = new PolygonShape();
		scaleShape.setAsBox( w * 0.5f * WORLD_TO_BOX, h * 0.0625f * WORLD_TO_BOX );
		scaleBody.createFixture( scaleShape, 0f );
		scaleShape.dispose();
		
		// static base B
		BodyDef armDef = new BodyDef();
		armDef.position.set(new Vector2( w * 5.0f * WORLD_TO_BOX, -h * 0.7f * WORLD_TO_BOX ) );
		Body armBody = world.createBody(armDef);
		PolygonShape armShape = new PolygonShape();
		armShape.setAsBox( w * 0.5f * WORLD_TO_BOX, h * 0.0625f * WORLD_TO_BOX );
		armBody.createFixture( armShape, 0f );
		armShape.dispose();
		
		// moving level
		BodyDef levelDef = new BodyDef();
		levelDef.type = BodyType.DynamicBody;
		levelDef.position.set(new Vector2( 0, -(h*0.25f) * WORLD_TO_BOX ) );
		levelBody = world.createBody(levelDef);
		scaleShape = new PolygonShape();
		scaleShape.setAsBox( ( w*0.45f) * WORLD_TO_BOX, (h*0.0625f) * WORLD_TO_BOX );
		levelBody.createFixture( scaleShape, 0.25f );
		scaleShape.dispose();
		
		levelSprite = skin.getSprite( "ScaleBottom" );
		levelSprite.setSize( w*0.9f , h*0.125f );
		levelSprite.setOrigin( levelSprite.getWidth()*0.5f, levelSprite.getHeight()*0.5f );
		
//				levelBody.setUserData( levelSprite );
		
		// right side
		scaleShape = new PolygonShape();
		scaleShape.setAsBox( ( w*0.03f) * WORLD_TO_BOX, (h*0.25f) * WORLD_TO_BOX, 
				new Vector2( ( w*0.45f) * WORLD_TO_BOX, (-h*0.25f + h*0.5f) * WORLD_TO_BOX ), 0.0f );
		levelBody.createFixture( scaleShape, 0.05f );
		scaleShape.dispose();
		
//		rightSprite = skin.getSprite( "ScaleRightSide" );
//		rightSprite.setSize( w*0.06f , h*0.5f );
//		rightSprite.setOrigin( rightSprite.getWidth()*0.5f, rightSprite.getHeight()*0.5f );
		
		// left side
		scaleShape = new PolygonShape();
		scaleShape.setAsBox( ( w*0.03f) * WORLD_TO_BOX, (h*0.25f) * WORLD_TO_BOX, 
				new Vector2( -( w*0.45f) * WORLD_TO_BOX, (-h*0.25f + h*0.5f) * WORLD_TO_BOX ), 0.0f );
		levelBody.createFixture( scaleShape, 0.05f );
		scaleShape.dispose();
		
//		leftSprite = skin.getSprite( "ScaleLeftSide" );
//		leftSprite.setSize( w*0.06f , h*0.5f );
//		leftSprite.setOrigin( leftSprite.getWidth()*0.5f, leftSprite.getHeight()*0.5f );
		
		// drop fruits from the sky
		Random random = new Random();
		float y = 0.0f;
//				weight = levelBody.getMass();
		weight = 0.0f;
		Iterator<Fruit> fruitIt = theBatch.fruits.iterator();
		
		// Create a fixture definition to apply our shape to
		FixtureDef fruitFixtureDef = new FixtureDef();
		
		
		
		
		
		fruitFixtureDef.density = 0.6f;
		fruitFixtureDef.friction = 0.9f;
		fruitFixtureDef.restitution = 0.1f; // Make it bounce a little bit
		
//				BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("data/FruitABody.json"));
//				fruitABodyOrigin = loader.getOrigin("FruitA", 0.75f ).cpy();
		
		while ( fruitIt.hasNext() ) {
			Fruit curFruit = fruitIt.next();
//					System.out.println( "Making a fruit" );
			// First we create a body definition
			BodyDef bodyDef = new BodyDef();
			// We set our body to dynamic, for something like ground which doesnt move we would set it to StaticBody
			bodyDef.type = BodyType.DynamicBody;
			// Set our body's starting position in the world
			int x = random.nextInt( (int) (w * 0.7f) );
			int rotate = random.nextInt( 360 );
			bodyDef.position.set( -w *0.35f * WORLD_TO_BOX + x * WORLD_TO_BOX,
					h * WORLD_TO_BOX + y * WORLD_TO_BOX);
			y += h * 0.1f;
		
			// Create our body in the world using our body definition
			
			float half_width = curFruit.weight * curFruit.widthvar * 1.2f;
			float half_height = curFruit.weight * curFruit.heightvar * 1.2f;

			PolygonShape polygon = new PolygonShape();
			
			float sides = (float)Math.max( 0.15f * half_width, 0.1);
			
			Vector2 vertices[] = new Vector2[4];
			vertices[0] = new Vector2(	0.0f, 	0.5f*half_height); 				// top
			vertices[1] = new Vector2( sides, 	0.3f*half_height);				// right
			vertices[2] = new Vector2(	0.0f, 	-0.5f*half_height);				// bottom
			vertices[3] = new Vector2( -sides, 	0.3f*half_height);				// left
			
			polygon.set(vertices);
		
			Body body = world.createBody(bodyDef);
//					System.out.println( "created a new body" );
		
			// Create a fixture definition to apply our shape to
			fruitFixtureDef.shape = polygon;
//					System.out.println( "set a polygon's shape" );
//					loader.attachFixture( body, ST.FRUIT_NAMES[ curFruit.type ], fruitFixtureDef,
//							Math.max( 0.5f, half_width*0.5f ) * (curFruit.flipped?-1:1), Math.max( 0.5f, half_height*0.5f ) );
			
			
		
			// Create our fixture and attach it to the body
			body.createFixture(fruitFixtureDef);
			
//					System.out.println( "Made a FixtureDef" );
			
			body.setTransform( body.getPosition(), MathUtils.degreesToRadians * (float)rotate  );
		//
			// make a FruitActor and setUserData
			
//		    	    instance.userData = fruit;
			
			FruitActor newActor = new FruitActor( curFruit.type, curFruit.getFrame(), curFruit.flipped, StrainMaster.getColor( curFruit.color ), fruitAtlas );
			newActor.setWidth( half_width * BOX_TO_WORLD * 0.5f );
			newActor.setHeight( half_height * BOX_TO_WORLD * 1.0f );
//					game.stage.addActor( newActor );
			body.setUserData( newActor );
//					newActor.toBack();

			// add to the counter weight
			weight += body.getMass();
			
			// Remember to dispose of any shapes after you're done with them!
			// BodyDef and FixtureDef don't need disposing, but shapes do.
//					polygon.dispose();
		}
		
		MassData massData = levelBody.getMassData();
		massData.mass = weight;
		levelBody.setMassData( massData );
		
		// counter weight
		BodyDef weightDef = new BodyDef();
		weightDef.type = BodyType.DynamicBody;
		weightDef.position.set(new Vector2( w * 5.0f * WORLD_TO_BOX, 0.0f * WORLD_TO_BOX ) );
		weightBody = world.createBody(weightDef);
		PolygonShape weightShape = new PolygonShape();
		weightShape.setAsBox( w * 0.25f * WORLD_TO_BOX, h * 0.25f * WORLD_TO_BOX );
		weightBody.createFixture( weightShape, 0.02f );
		massData = weightBody.getMassData();
		massData.mass = weight * 1.99999f;
		weightBody.setMassData( massData );
		weightShape.dispose();
		
		// pulley joint
		PulleyJointDef pulleyDef = new PulleyJointDef();
		pulleyDef.initialize( levelBody, weightBody,
				new Vector2( 0.0f, h * 5.0f * WORLD_TO_BOX ),
				new Vector2( w * 5.0f * WORLD_TO_BOX, h * 5.0f * WORLD_TO_BOX ),
				new Vector2( 0.0f * WORLD_TO_BOX, h*0.5f * WORLD_TO_BOX ),
				weightBody.getWorldCenter(), 1.0f
		);
		world.createJoint( pulleyDef );
		
		bodyArray = new Array<Body>();
		world.getBodies( bodyArray );
	}
	
	@Override
	public Object getObject( ) {
		return theBatch;
	}
	
	@Override
	public void passObject( Object object ) {
		if ( object!=null && object.getClass().equals( Batch.class ) )
			setBatch( (Batch)object );
	}
	
	public void setBatch( Batch batch ) {
		theBatch = batch;
		populated = false;
		
		if ( theBatch!=null ) {
			ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
			actionBarPart.setDy( -Assets.game.actionBarHeight );
		}
	}
	
	@Override
	protected void resize( ) {
		
		fruitAtlas = Assets.getFruitsAtlas();
		game = Assets.game;
		super.resize( );
		
//		cam = new PerspectiveCamera(67, width, height);
//		cam.position.set(0f, 0, 6f);
//		cam.normalizeUp ();
//		cam.lookAt(0, 0 ,0);
//		cam.near = .1f;
//		cam.far = 300f;
//		
//		cam.update();
		
		if ( theBatch!=null ) {
			getCell( image ).width( Assets.width * desiredWidth )
			.height( Assets.height * desiredHeight - Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD )*2.0f );
			
			createWorld( Assets.width, Assets.height);
		}
		
	}
	
	@Override
	public void screenAct( float delta ) {

		updateDelta += delta;
		
		if ( world!=null )
			while ( updateDelta>0.015f ) {
				world.step( ( 1/60f ) , 2, 1);
				updateDelta -= 0.015f;
			}
	}
	
	@Override
    public void render( ) {
		spriteBatch.begin();
		
		Iterator<Body> bi = bodyArray.iterator();
        
        while (bi.hasNext()){
            Body b = bi.next();
            
            FruitActor thisActor = (FruitActor) b.getUserData();
            if ( thisActor!=null ) {
            	
            	Vector2 bottlePos = b.getPosition();
            	 
            	thisActor.setPosition(Assets.width*0.5f + bottlePos.x * BOX_TO_WORLD - thisActor.getWidth()*0.5f,
            			Assets.height*0.5f + bottlePos.y * BOX_TO_WORLD - thisActor.getHeight()*0.5f );
//            	thisActor.setOrigin( thisActor.getWidth()*0.5f, thisActor.getHeight()*0.5f);
            	thisActor.setRotation(b.getAngle() * MathUtils.radiansToDegrees);

            	thisActor.draw( spriteBatch, 1.0f );
            }
        }
        
        // Update the entities/sprites position and angle
 		float x = levelBody.getPosition().x * BOX_TO_WORLD + Assets.width*0.5f ;
 		float y = levelBody.getPosition().y * BOX_TO_WORLD + Assets.height*0.5f ;

 		levelSprite.setPosition( x - levelSprite.getWidth()*0.5f , y - levelSprite.getHeight()*0.5f  );
 		// We need to convert our angle from radians to degrees
 		
 		levelSprite.setRotation( MathUtils.radiansToDegrees * levelBody.getAngle() );
 		levelSprite.draw( spriteBatch );

 		spriteBatch.end();
    }
	
	@Override
	public void dispose() {
		if ( world!=null ) {
			world.dispose();
			world = null;
		}
		
	}

	@Override
	public void back() {
//		ShroomClass game = Assets.game;
		
		if ( Assets.player.objective!=null && 
				Assets.player.objective.type== Assets.player.objective.HARVEST &&
				theBatch.getWeight()>=Float.parseFloat( Assets.player.objective.text ) ) {
			Dialogs.objectiveDialog( "You successfully harvested and dried a " + Assets.player.objective.text + "g batch!" );
			Assets.player.objective = null;
			if ( Assets.player.objective_alert != null ) {
				Assets.removeAlert( Assets.player.objective_alert );
				Assets.player.objective_alert = null;
			}
			Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Weigh Batch", (long)0 );
		}
		if ( !theBatch.weighed ) {
			theBatch.weighed = true;
			theBatch.needsResize = true;
//			final Skin skin = Assets.getSkin();
//			FloatingText fText = new FloatingText( "+" + (int)theBatch.getWeight() + "g", skin.get( "Stash", FitLabelStyle.class ) );
			if ( game.fTextPool==null )
				game.fTextPool = new FloatingTextPool();
			FloatingText fText = game.fTextPool.borrowObject();
			fText.setText( "+" + (int)theBatch.getWeight() + "g" );
			fText.setStyle( Assets.getSkin().get( "Stash",Label.LabelStyle.class ) );
//			fText.setWidth( game.width * 0.25f );
//			fText.setHeight( game.height * 0.1f );
			fText.setX( Assets.width * 0.25f );
			fText.setY( Assets.height/9.0f );
			game.addFloatingText( fText );
//			Tween.to( fText, FloatingTextTween.Y, 1.0f )
//	        .target(  game.height/9.0f * 2.0f )
//	        .ease(TweenEquations.easeOutQuad)
//	        .start( Assets.getTweenManager() ); //** start it
//			
//			Tween.to( fText, FloatingTextTween.ALPHA, 1.0f )
//	        .target( 0.0f )
//	        .delay( 0.7f )
//	        .ease(TweenEquations.easeInQuad)
//	        .start( Assets.getTweenManager() ); //** start it
//			
//			fText.failSafe = 2.0f;
			
			Assets.player.totalStashWeighed += theBatch.getWeight();
			Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQCA" );
			
			// grew a pound 
	    	if ( Assets.player.totalStashWeighed >=448 )
	    		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQEA" );
		}
		
		
		DehydratorsPart screen = Assets.getScreenHandler().getScreenPart( DehydratorsPart.class );
		ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
		actionBarPart.setDy( 0 );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public static class WeighBuilder implements ScreenPartBuilder<WeighPart> {
		@Override
		public WeighPart build() {
			WeighPart part = new WeighPart( null );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2.0f );
			part.setY( Assets.height * (0.5f+(Assets.game.actionBarHeight*0.5f)) );
			part.desiredX = 1;
			part.desiredY = 0;
			part.desiredWidth = 1f;
			part.desiredHeight = 1f;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
}
