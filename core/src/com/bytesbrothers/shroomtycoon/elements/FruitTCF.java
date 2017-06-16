package com.bytesbrothers.shroomtycoon.elements;

import com.bytesbrothers.shroomtycoon.structures.FruitColor;

public class FruitTCF {
	public FruitColor color;
	public int type;
	public int frame;
	
	public int fHashCode = 0;
	
	public FruitTCF( FruitColor color, int type, int frame ) {
		this.color = color;
		this.type = type;
		this.frame = frame;
	}
	
	@Override
	public int hashCode() {
		if ( fHashCode== 0  ) {
			int result = HashCodeUtil.SEED;
			result = HashCodeUtil.hash( result, type );
			result = HashCodeUtil.hash( result, frame );
			result = HashCodeUtil.hash( result, color.usesSpots );
			result = HashCodeUtil.hash( result, color.usesTopOver );
			for ( int i = 0 ; i < 4; i ++ ) {
				if ( color.color[i]!=null ) {
					result = HashCodeUtil.hash( result, color.color[i].r );
					result = HashCodeUtil.hash( result, color.color[i].g );
					result = HashCodeUtil.hash( result, color.color[i].b );
					result = HashCodeUtil.hash( result, color.color[i].a );
				}
			}
			fHashCode = result;
		}
		return fHashCode;
	}
	
	@Override
	public boolean equals( Object object ) {
		if ( object.getClass().equals( FruitTCF.class ) ) {
			FruitTCF that = (FruitTCF)object;
			if ( this.type!=that.type )
				return false;
			if ( this.frame!=that.frame )
				return false;
			if ( this.color.usesSpots!=that.color.usesSpots )
				return false;
			if ( this.color.usesTopOver!=that.color.usesTopOver )
				return false;
			for ( int i = 0; i < 4; i ++ )
				if ( this.color.color[i]!=null ) {
					if ( that.color.color[i]==null )
						return false;
					if ( !this.color.color[i].equals( that.color.color[i] ) )
						return false;
				} else if ( that.color.color[i]!=null )
					return false;
			
			return true;
		} else
			return super.equals( object );
	}
}
