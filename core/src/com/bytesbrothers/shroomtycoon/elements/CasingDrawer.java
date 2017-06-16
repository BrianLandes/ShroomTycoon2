package com.bytesbrothers.shroomtycoon.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ColumnTables.CasingShader;
import com.bytesbrothers.shroomtycoon.ColumnTables.CasingShader.CasingTextureAttribute;
import com.bytesbrothers.shroomtycoon.ColumnTables.FruitShader;
import com.bytesbrothers.shroomtycoon.ColumnTables.FruitShader.FruitColorAttribute;
import com.bytesbrothers.shroomtycoon.ColumnTables.FruitShader.FruitTextureAttribute;
import com.bytesbrothers.shroomtycoon.ColumnTables.TrayShader;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.CasingPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.CasingPart.FruitData;
import com.bytesbrothers.shroomtycoon.pools.ModelPool;
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Fruit;
import com.bytesbrothers.shroomtycoon.structures.FruitColor;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class CasingDrawer {

	private Casing theCasing;
	public boolean populated = false;
	
	public Shader fruitShader;
	public Shader trayShader;
	public Shader casingShader;
	
	public PerspectiveCamera cam;
	
	public ArrayList<ModelInstance> fruitInstances = new ArrayList<ModelInstance>();
	public HashMap<FruitTCF, Model> fruitModels = new HashMap<FruitTCF, Model>();
	public ModelPool modelPool;
	public ModelInstance trayInstance;
	public Model trayModel;
	public ModelInstance casingInstance;
	public Model casingModel;
	public ModelBatch modelBatch;
//	private ModelBuilder modelBuilder;
	
	TextureAtlas casingAtlas;
	private int lastSubFrame = -1;
	public boolean boxesMade = false;

	private Iterator<FruitTCF> colorIt;
	private FruitTCF thisCaF;
	private Model thisModel;
	private Mesh thisMesh;
	private Material thisMaterial;
	private FruitTextureAttribute thisTexAtt;
	private FruitColorAttribute thisColAtt;

	private FruitColor fColor;
	
	public CasingDrawer() {
		fruitShader = new FruitShader();
	    fruitShader.init();
	    
	    trayShader = new TrayShader();
	    trayShader.init();
	    
	    casingShader = new CasingShader();
	    casingShader.init();
	    
	    cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    
//	    modelBatch = new ModelBatch( new MyRenderableSorter() );
	    modelBatch = new ModelBatch( );
	    
	}

	public Casing getCasing() {
		return theCasing;
	}

	public void setCasing(Casing theCasing) {
		if ( theCasing!=null && !theCasing.equals( this.theCasing ) ) {
			this.theCasing = theCasing;
			populated = false;
			if ( modelPool==null )
				modelPool = new ModelPool();
			dispose();
		}
	}
	
	public void render() {
		if ( theCasing!=null ) {
			if ( !populated )
				populate();
		}
		
		// update the substrate
		int newSubFrame = theCasing.getFrame();
		if ( lastSubFrame!=newSubFrame ) {
			lastSubFrame = newSubFrame;
			
		    TextureRegion moldARegion = casingAtlas.findRegion( ST.MOLD_NAMES[ theCasing.mold_typeA ], lastSubFrame );
		    TextureRegion moldBRegion = null;
		    boolean useMoldB = true;
		    if ( theCasing.mold_contaminated ) {
		    	moldBRegion = casingAtlas.findRegion( ST.CONT_NAMES[ theCasing.mold_typeB ], lastSubFrame );
			} else if ( theCasing.mold_typeB!= -1 ) {
				moldBRegion = casingAtlas.findRegion( ST.MOLD_NAMES[ theCasing.mold_typeB ], lastSubFrame );
//										moldBRegion = casingAtlas.findRegion( ST.MOLD_NAMES[ 1 ], lastSubFrame );
			} else {
				useMoldB = false;
			}

			Mesh mesh = casingModel.meshes.get( 0 );
			
			updateTexCoord( mesh, 4, 1, moldARegion );
			casingInstance.materials.get(0).set( new CasingTextureAttribute( CasingTextureAttribute.MoldATexture, moldARegion.getTexture() ) );

			if ( useMoldB ) {
				updateTexCoord( mesh, 4, 2, moldBRegion );

				casingInstance.materials.get(0).set( new CasingTextureAttribute( CasingTextureAttribute.MoldBTexture, moldBRegion.getTexture() ) );
			} else {
				casingInstance.materials.get(0).remove( CasingTextureAttribute.MoldBTexture );
			}
			
			if ( theCasing.isSoaking() ) {
				TextureRegion waterRegion = casingAtlas.findRegion( "Soaking" );
				updateTexCoord( mesh, 4, 3, waterRegion );

				casingModel.materials.get(0).set( new CasingTextureAttribute( CasingTextureAttribute.WaterTexture, waterRegion.getTexture() ) );
			} else {
				casingInstance.materials.get(0).remove( CasingTextureAttribute.WaterTexture );
			}

		}
		
		ArrayList<Fruit> newFruits = new ArrayList<Fruit>();
		Iterator<ModelInstance> instanceIt = fruitInstances.iterator();
		while ( instanceIt.hasNext() ) {
			ModelInstance fruitInstance = instanceIt.next();
			FruitData thisData = ((FruitData)fruitInstance.userData);
			
			if ( thisData.fruit.getFrame() != thisData.lastFrame ) {
				instanceIt.remove();
//				fruitInstances.remove( fruitInstance );
				
				newFruits.add( thisData.fruit );
//				addFruit( thisData.fruit );
			}
		}
		
		for ( Fruit newFruit : newFruits ) {
			addFruit( newFruit );
		}
//		// update fruits
//		for( ModelInstance fruitInstance: fruitInstances ) {
//			FruitData thisData = ((FruitData)fruitInstance.userData);
//			if ( thisData.fruit.getFrame() != thisData.lastFrame ) {
//				fruitInstances.remove( fruitInstance );
//				
//				addFruit( thisData.fruit );
//			}
//		}
		
		// we also need to add fruits if new ones have sprouted up
		if ( fruitInstances.size()<theCasing.fruits.size() ) {
			ListIterator<Fruit> fruitIt = theCasing.fruits.listIterator( theCasing.fruits.size() );
			
			while( fruitIt.hasPrevious() && fruitInstances.size()<theCasing.fruits.size() ) {
				Fruit fruit = fruitIt.previous();
				
				boolean inInstances = false;
				for( ModelInstance fruitInstance: fruitInstances ) {
					if ( ((FruitData)fruitInstance.userData).fruit.equals( fruit ) ) {
						inInstances = true;
						break;
					}
				}
				
				if ( !inInstances ) {
					System.out.println( "A fruit sprouted and we're making a ModelInstance" );
					addFruit( fruit );
				}
			}
		}
		
		modelBatch.begin( cam );
		
		modelBatch.render( trayInstance, trayShader );
		
		modelBatch.render( casingInstance, casingShader );
		
		for (ModelInstance instance : fruitInstances) {
			modelBatch.render( instance, fruitShader );
		}
	    
		modelBatch.end();
	}
	
	public void populate() {
		casingAtlas = Assets.getCasingsAtlas();
		
//		if ( modelBuilder==null )
//			modelBuilder = new ModelBuilder();

		TextureRegion trayRegion = casingAtlas.findRegion( "CasingSide" );
		
		Assets.modelBuilder.begin();
		Assets.modelBuilder.part("1", createCube( trayRegion ),   GL20.GL_TRIANGLES,
	    		new Material("1", new TextureAttribute( TextureAttribute.createNormal( trayRegion.getTexture() ) ) ) );
		
		trayModel = Assets.modelBuilder.end();

	    trayInstance = new ModelInstance( trayModel, 0, 0, 0 );
	    
	    lastSubFrame = theCasing.getFrame();

	    TextureRegion casingRegion = casingAtlas.findRegion( ST.SUB_NAMES[ theCasing.substrate ] );
	    TextureRegion moldARegion = casingAtlas.findRegion( ST.MOLD_NAMES[ theCasing.mold_typeA ], lastSubFrame );
	    TextureRegion moldBRegion = null;
	    boolean useMoldB = true;
	    if ( theCasing.mold_contaminated ) {
	    	moldBRegion = casingAtlas.findRegion( ST.CONT_NAMES[ theCasing.mold_typeB ], lastSubFrame );
		} else if ( theCasing.mold_typeB!= -1 ) {
			moldBRegion = casingAtlas.findRegion( ST.MOLD_NAMES[ theCasing.mold_typeB ], lastSubFrame );
		} else {
			useMoldB = false;
		}
	    TextureRegion waterRegion = casingAtlas.findRegion( "Soaking" );
	    boolean useWater = theCasing.isSoaking();
		
	    Assets.modelBuilder.begin();
	    Assets.modelBuilder.part("1", ST.create3dSquare( casingRegion, moldARegion, moldBRegion, waterRegion ),   GL20.GL_TRIANGLES,
	    		new Material("1", new CasingTextureAttribute( CasingTextureAttribute.SubstrateTexture, casingRegion.getTexture() ),
	    				new CasingTextureAttribute( CasingTextureAttribute.MoldATexture, moldARegion.getTexture() ) ) );

		casingModel = Assets.modelBuilder.end();

		if ( useMoldB ) {
			CasingTextureAttribute casingAttribute = new CasingTextureAttribute( CasingTextureAttribute.MoldBTexture, moldBRegion.getTexture() );
			
			casingModel.materials.get(0).set( casingAttribute );
		}
		
		if ( useWater ) {
			CasingTextureAttribute casingAttribute = new CasingTextureAttribute( CasingTextureAttribute.WaterTexture, waterRegion.getTexture() );
			
			casingModel.materials.get(0).set( casingAttribute );
		}

	    casingInstance = new ModelInstance( casingModel, 0, 0, 0 );
	    
	    casingInstance.transform.translate( 0 , .25f, 0 );
	    casingInstance.transform.scale( 2.0f, 0.25f, 3.0f );
	    casingInstance.transform.rotate( Vector3.X, -90 );
	    
	    trayInstance.transform.scale( 2.0f, 0.25f, 3.0f );

		for ( Fruit fruit: theCasing.fruits ) {
			addFruit( fruit );
		}
		
		populated = true;
	}
	
	public void resize() {
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0f, 4f, -6f);
		cam.normalizeUp ();
		cam.lookAt(0,0,0);
		cam.near = .1f;
		cam.far = 300f;
		
		cam.update();
		
		// TODO: re-apply textures instead of dispose and rebuild
		populated = false;
		dispose();
	}
	
	public void dispose() {

		colorIt = fruitModels.keySet().iterator();
		while(colorIt.hasNext()) {
			thisModel = fruitModels.get( colorIt.next() );
			modelPool.returnObject( thisModel );
//			thisModel.dispose();
		}
		fruitModels.clear();
		
		fruitInstances.clear();

		if ( trayModel!=null ) {
			trayModel.dispose();
			trayModel = null;
		}
		
		if ( casingModel!=null ) {
			casingModel.dispose();
			casingModel = null;
		}
	}
	
	public void addFruit( Fruit fruit ) {
		int frame = fruit.getFrame();
		fColor = StrainMaster.getColor( fruit.color );
		thisCaF = new FruitTCF( fColor, fruit.type, frame );
		
		thisModel = fruitModels.get( thisCaF );
		if ( thisModel == null ) {
			TextureRegion region = Assets.getFruitPart( ST.FruitLayer.BASE, fruit.type, frame );
			TextureRegion topRegion = Assets.getFruitPart( ST.FruitLayer.TOP, fruit.type, frame );
			TextureRegion topOverRegion = Assets.getFruitPart( ST.FruitLayer.TOP_OVER, fruit.type, frame );
			TextureRegion spotsRegion = Assets.getFruitPart( ST.FruitLayer.SPOTS, fruit.type, frame );

			thisModel = modelPool.borrowObject();
			thisMesh = thisModel.meshes.get( 0 );
			thisMaterial = thisModel.materials.get( 0 );
			
			updateTexCoord( thisMesh, 4, 0, region );
			thisTexAtt = new FruitTextureAttribute( FruitTextureAttribute.FruitTexture, region.getTexture() );
    	    thisMaterial.set( thisTexAtt );
    	    
    	    if ( fColor.color[0]!=null ) {
		    	thisColAtt = new FruitColorAttribute( FruitColorAttribute.BaseColor,
	    	    		fColor.color[0].r, fColor.color[0].g, fColor.color[0].b, fColor.color[0].a );
	    	    
		    } else
		    	thisColAtt = new FruitColorAttribute( FruitColorAttribute.BaseColor, 1, 1, 1, 1 );
		    thisMaterial.set( thisColAtt );
		    
		    
    	    
			updateTexCoord( thisMesh, 4, 1, topRegion );
			thisTexAtt = new FruitTextureAttribute( FruitTextureAttribute.TopTexture, topRegion.getTexture() );
    	    thisMaterial.set( thisTexAtt );
			
			thisColAtt = new FruitColorAttribute( FruitColorAttribute.TopColor,
		    		fColor.color[1].r, fColor.color[1].g, fColor.color[1].b, fColor.color[1].a );
			thisMaterial.set( thisColAtt );
		    
		   
		    
		    
		    if ( fColor.color[2]!=null ) {
		    	updateTexCoord( thisMesh, 4, 2, topOverRegion );
		    	thisColAtt = new FruitColorAttribute( FruitColorAttribute.TopOverColor,
	    	    		fColor.color[2].r, fColor.color[2].g, fColor.color[2].b, fColor.color[2].a );
	    	    thisMaterial.set( thisColAtt );
	    	    
	    	    thisTexAtt = new FruitTextureAttribute( FruitTextureAttribute.TopOverTexture, topOverRegion.getTexture() );
	    	    thisMaterial.set( thisTexAtt );
		    } else {
		    	thisMaterial.remove( FruitTextureAttribute.TopOverTexture );
		    }
		    
		    
		    if ( fColor.color[3]!=null ) {
		    	updateTexCoord( thisMesh, 4, 3, spotsRegion );
		    	thisColAtt = new FruitColorAttribute( FruitColorAttribute.SpotsColor,
	    	    		fColor.color[3].r, fColor.color[3].g, fColor.color[3].b, fColor.color[3].a );
	    	    thisMaterial.set( thisColAtt );
		    	
	    	    thisTexAtt = new FruitTextureAttribute( FruitTextureAttribute.SpotsTexture, spotsRegion.getTexture() );
		    	thisMaterial.set( thisTexAtt );
		    } else {
		    	thisMaterial.remove( FruitTextureAttribute.SpotsTexture );
		    }

		    fruitModels.put( thisCaF, thisModel );
		}

	    
	    float height = 1f * fruit.weight * fruit.heightvar; // scale
	    
		float fruitx = 1.9f - fruit.x*0.01f*3.8f;
		float fruity = 2.9f - fruit.y*0.01f*5.8f;
		float z = 0.15f + height;
		
	    ModelInstance instance = new ModelInstance( thisModel, fruitx, z, fruity );
	    instance.transform.scale( 0.5f * fruit.weight * fruit.widthvar, height, 1f );
	    
	    
	    fruitInstances.add(instance);
	    
	    
	    instance.userData = new FruitData( fruit );

	    boxesMade = false;
	}
	
	public void removeFruit(Fruit fruit) {
		Iterator<ModelInstance> fruitIt = fruitInstances.iterator();
		while ( fruitIt.hasNext() ) {
			ModelInstance fruitInstance = fruitIt.next();
			
			if ( ((FruitData)fruitInstance.userData).fruit.equals( fruit ) ) {
				fruitIt.remove();
				break;
			}
			
		}
	}
	
	public void makeFruitBoxes() {
		for( ModelInstance fruitInstance: fruitInstances ) {
			if ( ((FruitData)fruitInstance.userData).bBox == null ) {
				((FruitData)fruitInstance.userData).bBox = fruitInstance.model.meshes.get(0).calculateBoundingBox()
						.mul(fruitInstance.transform);
			}
		}
		boxesMade = true;
	}

	/**
	 * A cube this no top
	 * @param region
	 * @return
	 */
	private Mesh createCube( TextureRegion region ) {
		Mesh quad = new Mesh(true, 20, 30, new VertexAttribute(Usage.Position, 3,   "a_position"), 
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

		float u = region.getU();
		float u2 = region.getU2();
		float v = region.getV();
		float v2 = region.getV2();
		
		quad.setVertices(new float[] { 
				//Vertices according to faces
				-1.0f, -1.0f, 1.0f,			u, v2, //Vertex 0
				1.0f, -1.0f, 1.0f,			u2, v2, //v1
				-1.0f, 1.0f, 1.0f,			u, v,  //v2
				1.0f, 1.0f, 1.0f,			u2, v,  //v3
				
				1.0f, -1.0f, 1.0f,			u, v2,	//4
				1.0f, -1.0f, -1.0f,			u2, v2,//5
				1.0f, 1.0f, 1.0f,			u, v, //6
				1.0f, 1.0f, -1.0f,			u2, v, //7
				
				1.0f, -1.0f, -1.0f,			u, v2,//8
				-1.0f, -1.0f, -1.0f,		u2, v2, //9
				1.0f, 1.0f, -1.0f,			u, v,//10
				-1.0f, 1.0f, -1.0f,			u2, v,//11
				
				-1.0f, -1.0f, -1.0f,		u, v2,//12
				-1.0f, -1.0f, 1.0f,			u2, v2,//13
				-1.0f, 1.0f, -1.0f,			u, v,//14
				-1.0f, 1.0f, 1.0f,			u2, v,//15
				
				-1.0f, -1.0f, -1.0f,		u, v2,//16
				1.0f, -1.0f, -1.0f,			u2, v2,//17
				-1.0f, -1.0f, 1.0f,			u, v,//18
				1.0f, -1.0f, 1.0f,			u2, v,//19
				
//				-1.0f, 1.0f, 1.0f,			0.5f, 1.0f,
//				1.0f, 1.0f, 1.0f,			1.0f, 1.0f,
//				-1.0f, 1.0f, -1.0f,			0.5f, 0.0f,
//				1.0f, 1.0f, -1.0f,			1.0f, 0.0f
		} );
		quad.setIndices(new short[] { 
				0,1,3, 0,3,2, 			//Face front
				4,5,7, 4,7,6, 			//Face right
				8,9,11, 8,11,10, 		//... 
				12,13,15, 12,15,14, 	
				16,17,19, 16,19,18
//				20,21,23, 20,23,22
				});
		
		return quad;
	}
	
	private Mesh updateTexCoord( Mesh quad, int textures, int index, TextureRegion region ) {
		int vertSize = quad.getNumVertices();
		float[] vertices = new float[ vertSize * (3 + textures*2) ];
		quad.getVertices( vertices );

		vertices[ 3 + index*2 ] = region.getU();						vertices[ 4 + index*2 ] = region.getV2();
		vertices[ 3 + (3+textures*2) + index*2 ] = region.getU2();		vertices[ 4 + (3+textures*2) + index*2 ] = region.getV2();
		vertices[ 3 + (3+textures*2)*2 + index*2 ] = region.getU2();	vertices[ 4 + (3+textures*2)*2 + index*2 ] = region.getV();
		vertices[ 3 + (3+textures*2)*3 + index*2 ] = region.getU();		vertices[ 4 + (3+textures*2)*3 + index*2 ] = region.getV();
		
		quad.setVertices( vertices );
		return quad;
	}
}
