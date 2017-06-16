package com.bytesbrothers.shroomtycoon.structures;

/** a class for Dehydrators. Has a maximum batch capacity and a drying rate */
public class Dehydrator {
	/** batch capacity. max num of batches this dehydrator can be drying at a time */
	public int capacity = 3;
	/** rate per second at which batches dry while in this dehydrator */
	public float rate = 1.0f;
	
	public Dehydrator() { }
	
}
