package com.bytesbrothers.shroomtycoon;

import com.badlogic.gdx.math.Vector3;

public class CD {
	// Collision Detection functions
	
	public static float EPSILON = 0.000001f;
	
	/** a method for making float arrays representing AABBs */
	public static float[] AABB(float left, float back, float right, float front, float top, float bottom ) {
    	// returns the box transformed by the rates
		float[] result = { left, back,
				right, front, top, bottom
		};
    	
    	return result;
    }
	
	public static boolean LineAndSquare(float[] P0, float[] P1, float[] V1, float[] V2, float[] V3, Vector3 outpoint) {
    	
    	/* find vectors for two edges sharing ver0 */
    	float u[] = new float[3];
    	float v[] = new float[3];
    	u[0] = V2[0]-V1[0];
    	u[1] = V2[1]-V1[1];
    	u[2] = V2[2]-V1[2];
    	
    	v[0] = V3[0]-V1[0];
    	v[1] = V3[1]-V1[1];
    	v[2] = V3[2]-V1[2];
    	
    	/* find a normal for the plane */
    	float norm[] = new float[3];
    	norm[0] = u[1]*v[2]-u[2]*v[1];
    	norm[1] = u[2]*v[0]-u[0]*v[2];
    	norm[2] = u[0]*v[1]-u[1]*v[0];
    	
    	/* Find V0 - P0 */
    	float VP[] = new float[3];
    	VP[0] = V1[0]-P1[0];
    	VP[1] = V1[1]-P1[1];
    	VP[2] = V1[2]-P1[2];
    	
    	/* Find P1 - P0 */
    	float PP[] = new float[3];
    	PP[0] = P1[0]-P0[0];
    	PP[1] = P1[1]-P0[1];
    	PP[2] = P1[2]-P0[2];
    	
    	/* Find the point on the line where intersection occurs */
    	float numerator = norm[0]*VP[0]+norm[1]*VP[1]+norm[2]*VP[2];
    	float denominator = norm[0]*PP[0]+norm[1]*PP[1]+norm[2]*PP[2];
    	float r = numerator/denominator;
    	
    	/* Find the actual coordinates */
    	outpoint.x = P1[0]+r*PP[0];
    	outpoint.y = P1[1]+r*PP[1];
    	outpoint.z = P1[2]+r*PP[2];
    	
    	/* Find w */
    	float w[] = new float[3];
    	w[0] = outpoint.x-V1[0];
    	w[1] = outpoint.y-V1[1];
    	w[2] = outpoint.z-V1[2];
    	
    	/* Find the denominator */
    	float UV = u[0]*v[0]+u[1]*v[1]+u[2]*v[2];
    	float UU = u[0]*u[0]+u[1]*u[1]+u[2]*u[2];
    	float VV = v[0]*v[0]+v[1]*v[1]+v[2]*v[2];
    	
    	float denominator2 = UV*UV-UU*VV;
    	
    	/* Find s */
    	float WV = w[0]*v[0]+w[1]*v[1]+w[2]*v[2];
    	float WU = w[0]*u[0]+w[1]*u[1]+w[2]*u[2];
    	
    	float s = (UV*WV-VV*WU)/denominator2;
    	
    	/* Find t */
    	float t = (UV*WU-UU*WV)/denominator2;
    	
//    	Log.i("BS","s: " + s + " t: " + t);
    	
    	if (s>=0 && t>=0 && s<=1 && t<=1)
    		return true;
    	
    	return false;
    	
    }
	
	/** Intersect ray R(t) = p + t*d against AABB a. When intersecting,
    * @return intersection distance tmin and point q of intersection */
    public static boolean LineAndAABB( Vector3 p0, Vector3 p1, float[] b ) {
    	Vector3 c = new Vector3( (b[0] + b[2]) * 0.5f, // box center-point
    			(b[5] + b[4]) * 0.5f,
    			(b[1] + b[3]) * 0.5f );
    	Vector3 e = new Vector3( b[2] - c.x, b[4] - c.y, b[3] - c.z ); // box half length extents
    	Vector3 m = p0.add(p1).scl(0.5f); // segment midpoint
    	Vector3 d = new Vector3( p1.x - m.x, p1.y - m.y, p1.z - m.z ); // segment halflength vector
    	m = m.sub(c); // translate box and segment to origin
    	// try world coordinate axes as separating ases
    	float adx = Math.abs( d.x );
    	if ( Math.abs( m.x) > e.x + adx) return false;
    	float ady = Math.abs( d.y );
    	if ( Math.abs( m.y) > e.y + ady) return false;
    	float adz = Math.abs( d.z );
    	if ( Math.abs( m.z) > e.z + adz) return false;
    	
    	// Add in an epsilon term to counteract arithmetic arros when segment is
    	// (near) parallel to a coordinate axis (see text for detail)
    	adx += EPSILON; ady += EPSILON; adz += EPSILON;
    	// Try cross products of segment direction vector with coordinate axes
    	if ( Math.abs( m.y*d.z - m.z*d.y) > e.y*adz + e.z*ady ) return false;
    	if ( Math.abs( m.z*d.x - m.x*d.z) > e.x*adz + e.z*adx ) return false;
    	if ( Math.abs( m.x*d.y - m.y*d.x) > e.x*ady + e.y*adx ) return false;
    	// No separating axis found; segment must be overlapping AABB
    	return true;
    }
    
    public static boolean SegmentAndSegment( float min1, float max1, float min2, float max2 ) {
    	if ( max1 < min2 )
    		return false;
    	if ( max2 < min1 )
    		return false;
    	
    	return true;
    }

    public static boolean RectAndRect( float minx1, float miny1, float maxx1, float maxy1,
    		float minx2, float miny2, float maxx2, float maxy2) {
    	if ( !SegmentAndSegment(minx1, maxx1, minx2, maxx2 ) )
    		return false;
    	if ( !SegmentAndSegment(miny1, maxy1, miny2, maxy2 ) )
    		return false;
    	return true;
    }
}
