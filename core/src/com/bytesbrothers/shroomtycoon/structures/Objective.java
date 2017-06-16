package com.bytesbrothers.shroomtycoon.structures;

public class Objective {
	public final int STRAIN = 0;
	public final int UPGRADE = 1;
	public final int HIRE = 2;
	public final int FIRE = 3;
	public final int HARVEST = 4;
	public int type;
	public String text; // additional info for objective
	public String definition;
	
	public Objective() {
		
	}
}
