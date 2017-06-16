package com.bytesbrothers.shroomtycoon.elements;

/**
 * TERMS OF USE - EASING EQUATIONS

Open source under the BSD License. 

Copyright � 2001 Robert Penner
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of the author nor the names of contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
 LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class Ease {
	
	/*
	 * Easing Constants
	 */
	public static final int IN_SINE = 0;
	public static final int OUT_SINE = 1;
	public static final int IN_OUT_SINE = 2;
	public static final int IN_QUAD = 3;
	public static final int OUT_QUAD = 4;
	public static final int IN_OUT_QUAD = 5;
	public static final int IN_CUBIC = 6;
	public static final int OUT_CUBIC = 7;
	public static final int IN_OUT_CUBIC = 8;
	public static final int IN_QUINT = 9;
	public static final int OUT_QUINT = 10;
	public static final int IN_OUT_QUINT = 11;
	public static final int IN_EXPO = 12;
	public static final int OUT_EXPO = 13;
	public static final int IN_OUT_EXPO = 14;
	
	/**
	 * @param type one of the Ease Constants determining the type of ease
	 * @param t is the current time (or position) of the tween. This can be seconds or frames, steps, seconds, ms, whatever � as long as the unit is the same as is used for the total time
	 * @param b is the beginning value of the property.
	 * @param c is the change between the beginning and destination value of the property.
	 * @param d the total time of the tween.
	 * @return a tweened value
	 */
	public static float ease( int type, float t, float b, float c, float d ) {
		switch ( type ) {
		case IN_SINE:
			return -c * (float)Math.cos(t/d * (Math.PI/2)) + c + b;
		case OUT_SINE:
			return c * (float)Math.sin(t/d * (Math.PI/2)) + b;
		case IN_OUT_SINE:
			return -c/2 * ((float)Math.cos(Math.PI*t/d) - 1) + b;
		case IN_QUAD:
			return c*(t/=d)*t + b;
		case OUT_QUAD:
			return -c *(t/=d)*(t-2) + b;
		case IN_OUT_QUAD:
			if ((t/=d/2) < 1) return c/2*t*t + b;
			return -c/2 * ((--t)*(t-2) - 1) + b;
		case IN_CUBIC:
			return c*(t/=d)*t*t + b;
		case OUT_CUBIC:
			return c*((t=t/d-1)*t*t + 1) + b;
		case IN_OUT_CUBIC:
			if ((t/=d/2) < 1) return c/2*t*t*t + b;
			return c/2*((t-=2)*t*t + 2) + b;
		case IN_QUINT:
			return c*(t/=d)*t*t*t*t + b;
		case OUT_QUINT:
			return c*((t=t/d-1)*t*t*t*t + 1) + b;
		case IN_OUT_QUINT:
			if ((t/=d/2) < 1) return c/2*t*t*t + b;
			return c/2*((t-=2)*t*t + 2) + b;
		case IN_EXPO:
			return (t==0) ? b : c * (float)Math.pow(2, 10 * (t/d - 1)) + b;
		case OUT_EXPO:
			return (t==d) ? b+c : c * (-(float)Math.pow(2, -10 * t/d) + 1) + b;	
		case IN_OUT_EXPO:
			if (t==0) return b;
			if (t==d) return b+c;
			if ((t/=d/2) < 1) return c/2 * (float)Math.pow(2, 10 * (t - 1)) + b;
			return c/2 * (-(float)Math.pow(2, -10 * --t) + 2) + b;
		}
		return 0;
	}
}
