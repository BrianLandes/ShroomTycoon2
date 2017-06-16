package com.bytesbrothers.shroomtycoon.elements;

import java.util.Comparator;

public class TextAndSeconds {
	public int seconds = 0;
	public String text = "";
	
	public TextAndSeconds ( int seconds, String text ) {
		this.seconds = seconds;
		this.text = text;
	}
	
	public static class TextAndSecondsComparator implements Comparator<TextAndSeconds> {
		@Override
		public int compare( TextAndSeconds ts1, TextAndSeconds ts2 ) {
			if ( ts1.seconds>ts1.seconds ) {
				return 1;
			} else if ( ts1.seconds<ts1.seconds ) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
