#ifdef GL_ES 
precision mediump float;
#endif
 
//SpriteBatch will use texture unit 0
uniform sampler2D u_texture0;
//uniform sampler2D u_texture1;
//uniform sampler2D u_texture2;
//uniform sampler2D u_texture3;
//uniform vec4 u_color0;
//uniform vec4 u_color1;
//uniform vec4 u_color2;
//uniform vec4 u_color3;

varying vec2 v_texCoord0;
//varying vec2 v_texCoord1;
//varying vec2 v_texCoord2;
//varying vec2 v_texCoord3;

void main() {
    //sample the texture
    vec4 texColor0 = texture2D(u_texture0, v_texCoord0);
//	vec4 texColor1 = texture2D(u_texture1, v_texCoord1) * u_color1;
//	vec4 texColor2 = texture2D(u_texture2, v_texCoord2) * u_color2;
//	vec4 texColor3 = texture2D(u_texture3, v_texCoord3) * u_color3;

//	float finalRed = (texColor1.r * texColor1.a) + (texColor0.r * ( 1.0 - texColor1.a) );
//	float finalGreen = (texColor1.g * texColor1.a) + (texColor0.g * ( 1.0 - texColor1.a) );
//	float finalBlue = (texColor1.b * texColor1.a) + (texColor0.b * ( 1.0 - texColor1.a) );
	
    //final color
//    gl_FragColor = vec4( finalRed, finalGreen, finalBlue, texColor1.a+texColor0.a) ;
    
    gl_FragColor = texColor0;
}
