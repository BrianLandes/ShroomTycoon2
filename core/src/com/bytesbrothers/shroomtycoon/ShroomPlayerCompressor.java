package com.bytesbrothers.shroomtycoon;

import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Strain;

public class ShroomPlayerCompressor {

	public static void compress( ShroomPlayer player ) {
		
		// turn list of syringes into a name -> number hashmap instead of multiple instances of strain classes
		for ( Strain strain: player.syringes ) {
			Integer thisNumber = player.syringesCompressed.get( strain.name );
			if ( thisNumber==null )
				thisNumber = new Integer( 0 );
			thisNumber ++;
			player.syringesCompressed.put( strain.name, thisNumber );
		}
		player.syringes.clear();
		
		// clear the fruits of the batches that have been weighed
		for ( Batch thisBatch: player.batches ) {
			if ( thisBatch.weighed && thisBatch.dried_weight!= -1 ) {
				thisBatch.fruits.clear();
			}
		}
	}
}
