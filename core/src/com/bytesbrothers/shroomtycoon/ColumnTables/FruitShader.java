package com.bytesbrothers.shroomtycoon.ColumnTables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class FruitShader implements Shader {
	ShaderProgram program;
	Camera camera;
    RenderContext context;
    int u_projViewTrans;
    int u_worldTrans;
//    int u_color;
    int u_texture0;
    int u_texture1;
    int u_texture2;
    int u_texture3;
    int u_color0;
    int u_color1;
    int u_color2;
    int u_color3;
    
//    int u_used0;
    int u_used1;
    int u_used2;
    int u_used3;
	
    public static class FruitTextureAttribute extends TextureAttribute {
        public final static String FruitTextureAlias = "fruitTexture";
        public final static long FruitTexture = register(FruitTextureAlias);
 
        public final static String TopTextureAlias = "topTexture";
        public final static long TopTexture = register(TopTextureAlias);
        
        public final static String TopOverTextureAlias = "topOverTexture";
        public final static long TopOverTexture = register(TopOverTextureAlias);
        
        public final static String SpotsTextureAlias = "spotsTexture";
        public final static long SpotsTexture = register(SpotsTextureAlias);

        static {
            Mask = Mask | FruitTexture | TopTexture | TopOverTexture | SpotsTexture;
        }
         
        public FruitTextureAttribute (final long type, final Texture texture) {
            super( type, texture );
        }
        
        public FruitTextureAttribute (final long type) {
    		super(type);
    		if (!is(type)) throw new GdxRuntimeException("Invalid type specified");
    	}
    }
    
    public static class FruitColorAttribute extends ColorAttribute {
    	public final static String BaseColorAlias = "baseColor";
        public final static long BaseColor = register(BaseColorAlias);
        
        public final static String TopColorAlias = "topColor";
        public final static long TopColor = register(TopColorAlias);
        
        public final static String TopOverColorAlias = "topOverColor";
        public final static long TopOverColor = register(TopOverColorAlias);
        
        public final static String SpotsColorAlias = "spotsColor";
        public final static long SpotsColor = register(SpotsColorAlias);

        static {
            Mask = Mask | BaseColor | TopColor | TopOverColor | SpotsColor;
        }

		public FruitColorAttribute( long type, float r, float g, float b, float a ) {
			super(type, r, g, b, a);
		}

    }
    
	@Override
	public void init () {
        String vert = Gdx.files.internal("data/fruit.vertex.glsl").readString();
        String frag = Gdx.files.internal("data/fruit.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        
//        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_projViewTrans = program.getUniformLocation("u_projViewTrans");

        u_texture0 = program.getUniformLocation("u_texture0");
        u_texture1 = program.getUniformLocation("u_texture1");
        u_texture2 = program.getUniformLocation("u_texture2");
        u_texture3 = program.getUniformLocation("u_texture3");
        
        u_color0 = program.getUniformLocation("u_color0");
        u_color1 = program.getUniformLocation("u_color1");
        u_color2 = program.getUniformLocation("u_color2");
        u_color3 = program.getUniformLocation("u_color3");
        
//        u_used0 = program.getUniformLocation("u_used0");
        u_used1 = program.getUniformLocation("u_used1");
        u_used2 = program.getUniformLocation("u_used2");
        u_used3 = program.getUniformLocation("u_used3");
//
//        String[] names = program.getAttributes();
//        for ( String name : names) {
//        	System.out.println( name );
//        }
    }
	
    @Override
    public void dispose () {
        program.dispose();
    }

    
    @Override
    public void begin (Camera camera, RenderContext context) {
    	this.camera = camera;
        this.context = context;
        program.begin();
        program.setUniformMatrix(u_projViewTrans, camera.combined);
//        context.setDepthTest( GL20.GL_LEQUAL);
        context.setDepthMask( false );
        context.setCullFace(GL20.GL_NONE);
        
        program.setUniformi( u_texture0, 0 );
        program.setUniformi( u_texture1, 1 );
        program.setUniformi( u_texture2, 2 );
        program.setUniformi( u_texture3, 3 );
        
        context.setBlending( true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );

//        program.setAttributef( "Color", 1.0f, 1.0f, 1.0f, 1.0f );
    }
    
    @Override
    public void render (Renderable renderable) {
    	program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
    	
    	Color color0 = ((ColorAttribute)renderable.material.get(FruitColorAttribute.BaseColor)).color;
//        Color color1 = ((ColorAttribute)renderable.material.get(FruitColorAttribute.TopColor)).color;
        
        program.setUniformf(u_color0, color0.r, color0.g, color0.b, color0.a );
//        program.setUniformf(u_color1, color1.r, color1.g, color1.b, color1.a );
        
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    	TextureAttribute attribute = (TextureAttribute)renderable.material.get(FruitTextureAttribute.FruitTexture);
    	attribute.textureDescription.texture.bind();
        
    	if ( renderable.material.has( FruitTextureAttribute.TopTexture ) ) {
     		program.setUniformi( u_used1, 1 );
     		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
     		attribute = (TextureAttribute)renderable.material.get(FruitTextureAttribute.TopTexture);
     		attribute.textureDescription.texture.bind();
     		
     		Color color1 = ((ColorAttribute)renderable.material.get(FruitColorAttribute.TopColor)).color;
     		program.setUniformf(u_color1, color1.r, color1.g, color1.b, color1.a );
     	} else {
     		program.setUniformi( u_used1, 0 );
     	}
//    	
//    	Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
//    	attribute = (TextureAttribute)renderable.material.get(FruitTextureAttribute.TopTexture);
//    	attribute.textureDescription.texture.bind();
    	
    	
    	if ( renderable.material.has( FruitTextureAttribute.TopOverTexture ) ) {
     		program.setUniformi( u_used2, 1 );
     		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE2);
     		attribute = (TextureAttribute)renderable.material.get(FruitTextureAttribute.TopOverTexture);
     		attribute.textureDescription.texture.bind();
     		
     		Color color2 = ((ColorAttribute)renderable.material.get(FruitColorAttribute.TopOverColor)).color;
     		program.setUniformf(u_color2, color2.r, color2.g, color2.b, color2.a );
     	} else {
     		program.setUniformi( u_used2, 0 );
     	}
    	
    	if ( renderable.material.has( FruitTextureAttribute.SpotsTexture ) ) {
     		program.setUniformi( u_used3, 1 );
     		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE3);
     		attribute = (TextureAttribute)renderable.material.get(FruitTextureAttribute.SpotsTexture);
     		attribute.textureDescription.texture.bind();
     		
     		Color color3 = ((ColorAttribute)renderable.material.get(FruitColorAttribute.SpotsColor)).color;
     		program.setUniformf(u_color3, color3.r, color3.g, color3.b, color3.a );
     	} else {
     		program.setUniformi( u_used3, 0 );
     	}

        renderable.meshPart.mesh.render(program,
                renderable.meshPart.primitiveType,
                renderable.meshPart.offset,
                renderable.meshPart.size);
    }
    @Override
    public void end () {
        program.end();
    }
    @Override
    public int compareTo (Shader other) {
        return 0;
    }
    @Override
    public boolean canRender (Renderable renderable) {
    	return renderable.material.has(FruitTextureAttribute.FruitTexture
//    			| FruitTextureAttribute.TopTexture
//    			| FruitTextureAttribute.TopOverTexture | FruitTextureAttribute.SpotsTexture
    			);
//    	return true;
    }
}
