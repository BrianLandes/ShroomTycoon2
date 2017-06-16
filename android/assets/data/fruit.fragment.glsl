#ifdef GL_ES 
precision mediump float;
#endif
 
//SpriteBatch will use texture unit 0
uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform sampler2D u_texture2;
uniform sampler2D u_texture3;
uniform vec4 u_color0;
uniform vec4 u_color1;
uniform vec4 u_color2;
uniform vec4 u_color3;

varying vec2 v_texCoord0;
varying vec2 v_texCoord1;
varying vec2 v_texCoord2;
varying vec2 v_texCoord3;

//uniform int u_used0;
uniform int u_used1;
uniform int u_used2;
uniform int u_used3;

void main() {
    //sample the texture
    vec4 texColor0 = texture2D(u_texture0, v_texCoord0) * u_color0;
	
	
	float finalRed = texColor0.r;
	float finalGreen = texColor0.g;
	float finalBlue = texColor0.b;
	
	float finalAlpha = texColor0.a;
	
	if ( u_used1==1 ) {
		vec4 texColor1 = texture2D(u_texture1, v_texCoord1) * u_color1;
	
		finalRed = (texColor1.r * texColor1.a) + (finalRed * ( 1.0 - texColor1.a) );
		finalGreen = (texColor1.g * texColor1.a) + (finalGreen * ( 1.0 - texColor1.a) );
		finalBlue = (texColor1.b * texColor1.a) + (finalBlue * ( 1.0 - texColor1.a) );
	
		finalAlpha = finalAlpha+texColor1.a;
	}
	
	if ( u_used2==1 ) {
		vec4 texColor2 = texture2D(u_texture2, v_texCoord2) * u_color2;
		
		finalRed = (texColor2.r * texColor2.a) + (finalRed * ( 1.0 - texColor2.a) );
		finalGreen = (texColor2.g * texColor2.a) + (finalGreen * ( 1.0 - texColor2.a) );
		finalBlue = (texColor2.b * texColor2.a) + (finalBlue * ( 1.0 - texColor2.a) );
		
		finalAlpha = finalAlpha + texColor2.a;
	}
	
	if ( u_used3==1 ) {
		vec4 texColor3 = texture2D(u_texture3, v_texCoord3) * u_color3;
	
		finalRed = (texColor3.r * texColor3.a) + (finalRed * ( 1.0 - texColor3.a) );
		finalGreen = (texColor3.g * texColor3.a) + (finalGreen * ( 1.0 - texColor3.a) );
		finalBlue = (texColor3.b * texColor3.a) + (finalBlue * ( 1.0 - texColor3.a) );
		
		finalAlpha = finalAlpha + texColor3.a;
	}
	
    //final color
    gl_FragColor = vec4( finalRed, finalGreen, finalBlue, finalAlpha) ;

}
