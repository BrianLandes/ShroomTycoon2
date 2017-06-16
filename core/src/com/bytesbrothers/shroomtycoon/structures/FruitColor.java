package com.bytesbrothers.shroomtycoon.structures;

import com.badlogic.gdx.graphics.Color;

public class FruitColor {
	
	public boolean usesTopOver = false;
	public boolean usesSpots = false;
	public Color[] color = {null, null, null, null};
	
	public FruitColor( boolean over, boolean spots, Color baseColor, Color capColor, Color secondaryColor, Color spotColor ) {
		usesTopOver = over;
		usesSpots = spots;
		color[0] = baseColor;
		color[1] = capColor;
		color[2] = secondaryColor;
		color[3] = spotColor;
	}
}
