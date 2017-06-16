package com.bytesbrothers.shroomtycoon.ColumnTables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class TrayShader implements Shader {
	ShaderProgram program;
	Camera camera;
    RenderContext context;
    int u_projViewTrans;
    int u_worldTrans;
    int u_texture0;

	@Override
	public void init () {
        String vert = Gdx.files.internal("data/tray.vertex.glsl").readString();
        String frag = Gdx.files.internal("data/tray.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());

        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_projViewTrans = program.getUniformLocation("u_projViewTrans");

        u_texture0 = program.getUniformLocation("u_texture0");

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
        context.setBlending( true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );

    }
    
    @Override
    public void render (Renderable renderable) {
    	program.setUniformMatrix(u_worldTrans, renderable.worldTransform);

    	Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
    	TextureAttribute attribute = (TextureAttribute)renderable.material.get(TextureAttribute.Normal);
//    	context.textureBinder.bind( attribute.textureDescription.texture );
    	attribute.textureDescription.texture.bind();

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
    	return true;
    }
}
