uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec2 a_texCoord1;
attribute vec2 a_texCoord2;
attribute vec2 a_texCoord3;

varying vec2 v_texCoord0;
varying vec2 v_texCoord1;
varying vec2 v_texCoord2;
varying vec2 v_texCoord3;

void main() {
	v_texCoord0 = a_texCoord0;
	v_texCoord1 = a_texCoord1;
	v_texCoord2 = a_texCoord2;
	v_texCoord3 = a_texCoord3;
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}