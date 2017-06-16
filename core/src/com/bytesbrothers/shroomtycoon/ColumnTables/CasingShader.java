package com.bytesbrothers.shroomtycoon.ColumnTables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class CasingShader implements Shader {
	ShaderProgram program;
	Camera camera;
    RenderContext context;
    int u_projViewTrans;
    int u_worldTrans;

    int u_texture0;
    int u_texture1;
    int u_texture2;
    int u_texture3;

    int u_used2;
    int u_used3;
	
    public static class CasingTextureAttribute extends TextureAttribute {
        public final static String SubstrateTextureAlias = "substrateTexture";
        public final static long SubstrateTexture = register(SubstrateTextureAlias);
 
        public final static String MoldATextureAlias = "moldATexture";
        public final static long MoldATexture = register(MoldATextureAlias);
        
        public final static String MoldBTextureAlias = "moldBTexture";
        public final static long MoldBTexture = register(MoldBTextureAlias);
        
        public final static String WaterTextureAlias = "waterTexture";
        public final static long WaterTexture = register(WaterTextureAlias);

        static {
            Mask = Mask | SubstrateTexture | MoldATexture | MoldBTexture | WaterTexture;
        }
         
        public CasingTextureAttribute (final long type, final Texture texture) {
            super( type, texture );
        }

    }
    
	@Override
	public void init () {
        String vert = Gdx.files.internal("data/casing.vertex.glsl").readString();
        String frag = Gdx.files.internal("data/casing.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_projViewTrans = program.getUniformLocation("u_projViewTrans");

        u_texture0 = program.getUniformLocation("u_texture0");
        u_texture1 = program.getUniformLocation("u_texture1");
        u_texture2 = program.getUniformLocation("u_texture2");
        u_texture3 = program.getUniformLocation("u_texture3");
        
        u_used2 = program.getUniformLocation("u_used2");
        u_used3 = program.getUniformLocation("u_used3");
        
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
        context.setDepthTest( GL20.GL_LEQUAL);
        context.setCullFace(GL20.GL_BACK);
        
        program.setUniformi( u_texture0, 0 );
        program.setUniformi( u_texture1, 1 );
        program.setUniformi( u_texture2, 2 );
        program.setUniformi( u_texture3, 3 );
        
        context.setBlending( true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );
    }
    
    @Override
    public void render (Renderable renderable) {
    	program.setUniformMatrix(u_worldTrans, renderable.worldTransform);

    	Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    	TextureAttribute attribute = (TextureAttribute)renderable.material.get(CasingTextureAttribute.SubstrateTexture);
//    	context.textureBinder.bind( attribute.textureDescription.texture );
    	attribute.textureDescription.texture.bind();
    	
    	
    	Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
    	attribute = (TextureAttribute)renderable.material.get(CasingTextureAttribute.MoldATexture);
//    	context.textureBinder.bind( attribute.textureDescription.texture );
    	attribute.textureDescription.texture.bind();
    	
    	if ( renderable.material.has( CasingTextureAttribute.MoldBTexture ) ) {
    		program.setUniformi( u_used2, 1 );
    		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE2);
    		attribute = (TextureAttribute)renderable.material.get(CasingTextureAttribute.MoldBTexture);
    		attribute.textureDescription.texture.bind();
    	} else {
    		program.setUniformi( u_used2, 0 );
    	}
    	
    	if ( renderable.material.has( CasingTextureAttribute.WaterTexture ) ) {
    		program.setUniformi( u_used3, 1 );
    		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE3);
    		attribute = (TextureAttribute)renderable.material.get(CasingTextureAttribute.WaterTexture);
    		attribute.textureDescription.texture.bind();
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
    	return renderable.material.has(CasingTextureAttribute.SubstrateTexture | CasingTextureAttribute.MoldATexture
////    			| FruitTextureAttribute.TopOverTexture | FruitTextureAttribute.SpotsTexture
    			);
//    	return true;
    }
}
