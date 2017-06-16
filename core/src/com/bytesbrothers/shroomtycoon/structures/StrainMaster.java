package com.bytesbrothers.shroomtycoon.structures;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;

public class StrainMaster {
	private static ArrayList<Strain> strains = null;
	
	private static int strain_enum = 1;
	private static boolean strainEnumIncreases = true;
	
	public static ArrayList<Strain> getStrains() {
		if ( strains==null )
			loadStrains();
		return strains;
	}
	
	public static Strain getStrain( String name ) {
		if ( strains==null )
			loadStrains();
//		Iterator<Strain> iterator = strains.iterator();
//		while (iterator.hasNext() ) {
//			Strain thisStrain = iterator.next();
		for ( Strain thisStrain: strains ) {
			if ( thisStrain.name.equals( name ) ) {
				return thisStrain;
			}
		}
//		iterator = Assets.player.strains.iterator();
//		while (iterator.hasNext() ) {
//			Strain thisStrain = iterator.next();
		for ( Strain thisStrain: Assets.player.strains ) {
			if ( thisStrain.name.equals( name ) ) {
				return thisStrain;
			}
		}
		
//		iterator = Assets.player.serverStrains.iterator();
//		while (iterator.hasNext() ) {
//			Strain thisStrain = iterator.next();
		for ( Strain thisStrain: Assets.player.serverStrains ) {
			if ( thisStrain.name.equals( name ) ) {
				return thisStrain;
			}
		}
		
		System.out.println( "getStrain: " + name + " not found." );
		return null;
	}
	
	public static Strain combineStrains( Strain A, Strain B ) {
		Strain newStrain = new Strain();
		
		newStrain.description = "A hybrid of " + A.name + " and " + B.name + ".";
		
		Random random = new Random();
		/*
		 * Speed Qualities
		 */
		newStrain.jar_grow_time = random.nextBoolean()? A.jar_grow_time: B.jar_grow_time; // num of seconds for jar to grow while incubating
		newStrain.casing_grow_time = random.nextBoolean()? A.casing_grow_time: B.casing_grow_time; // num of seconds for casing to incubate
		newStrain.fruit_grow_time = random.nextBoolean()? A.fruit_grow_time: B.fruit_grow_time; // num of seconds for fruit to grow
		newStrain.pins_time = random.nextBoolean()? A.pins_time: B.pins_time; // seconds in between pinheads
		
		/*
		 * Gross Income Qualities
		 */
		newStrain.fruit_weight = random.nextBoolean()? A.fruit_weight: B.fruit_weight;// average fruit body weight
		newStrain.fruit_weight_Var = random.nextBoolean()? A.fruit_weight_Var: B.fruit_weight_Var;// variance in fruit body weight
		
		newStrain.potency = random.nextBoolean()? A.potency: B.potency;
		newStrain.potency_Var = random.nextBoolean()? A.potency_Var: B.potency_Var;
		newStrain.batch_weight = random.nextBoolean()? A.batch_weight: B.batch_weight;// total batch weight in g's
		newStrain.batch_weight_Var = random.nextBoolean()? A.batch_weight_Var: B.batch_weight_Var;// variance in batch weight
		
		/*
		 * Difficulty Qualities
		 */
		newStrain.temp_required = random.nextBoolean()? A.temp_required: B.temp_required;// temp quality need: low, medium, or high
		newStrain.humidity_cost = random.nextBoolean()? A.humidity_cost: B.humidity_cost;// units required of humidity for pinning
		newStrain.substrate = random.nextBoolean()? A.substrate: B.substrate;// this strains preferred substrate type
		
		if ( Assets.player.customColors == null ) {
			Assets.player.customColors = new ArrayList<FruitColor>();
		}
		
		/*
		 * Other
		 */
		switch( random.nextInt( 6 ) ) {
		case 0:
		case 1:
		case 2:
		case 3:
			for ( Integer color: A.colors )
				newStrain.colors.add( color );

			for ( Integer color: B.colors )
				newStrain.colors.add( color );
			break;

		case 4: {
			// 1 out of 6 chance that the two ( or more) colors are mixed to make a FruitColor with layers from each
			int indexA = random.nextInt( A.colors.size() );
			FruitColor colorA = getColor( A.colors.get( indexA ) );
			int indexB = random.nextInt( B.colors.size() );
			FruitColor colorB = getColor( B.colors.get( indexB ) );
			
			boolean useOver = random.nextBoolean()? colorA.usesTopOver: colorB.usesTopOver;
			boolean useSpots = random.nextBoolean()? colorA.usesSpots: colorB.usesSpots;
			FruitColor newColor = new FruitColor( useOver, useSpots,
					random.nextBoolean()? colorA.color[0]: colorB.color[0], // baseColor
					random.nextBoolean()? colorA.color[1]: colorB.color[1], // capColor
					useOver? random.nextBoolean()? colorA.color[2]: colorB.color[2]:null, // secondaryColor
					useSpots? random.nextBoolean()? colorA.color[3]: colorB.color[3]:null ); // spotColor
			
			newStrain.colors.add( new Integer( 
					Assets.player.
					customColors.
					size() + 
					ST.CUSTOM_COLOR_INDEX_OFFSET ) );
			Assets.player.customColors.add( newColor );
			
			// add any other colors
			for ( int i = 0; i < A.colors.size(); i ++ )
				if ( i!=indexA )
					newStrain.colors.add( A.colors.get( i ) );
			for ( int i = 0; i < B.colors.size(); i ++ )
				if ( i!=indexB )
					newStrain.colors.add( B.colors.get( i ) );
			break; }
		case 5:
		default:
			// 1 out of 6 chance that the two ( or more) colors are mixed to make a FruitColor with colors combined on each layer
			int indexA = random.nextInt( A.colors.size() );
			FruitColor colorA = getColor( A.colors.get( indexA ) );
			int indexB = random.nextInt( B.colors.size() );
			FruitColor colorB = getColor( B.colors.get( indexB ) );
			
			boolean useOver = random.nextBoolean()? colorA.usesTopOver: colorB.usesTopOver;
			boolean useSpots = random.nextBoolean()? colorA.usesSpots: colorB.usesSpots;
			
			Color[] color = colorA.color;
			if ( color[0] != null ) {
				if ( colorB.color[0]!=null ) {
					color[0].r = color[0].r *0.5f + colorB.color[0].r * 0.5f;
					color[0].g = color[0].g *0.5f + colorB.color[0].g * 0.5f;
					color[0].b = color[0].b *0.5f + colorB.color[0].b * 0.5f;
				}
			} else
				color[0] = colorB.color[0];
			if ( color[1] != null ) {
				if ( colorB.color[1]!=null ) {
					color[1].r = color[1].r *0.5f + colorB.color[1].r * 0.5f;
					color[1].g = color[1].g *0.5f + colorB.color[1].g * 0.5f;
					color[1].b = color[1].b *0.5f + colorB.color[1].b * 0.5f;
				}
			} else
				color[1] = colorB.color[1];
			
			
			if ( useOver ) {
				if ( color[2] != null ) {
					if ( colorB.color[3]!=null ) {
						color[2].r = color[2].r *0.5f + colorB.color[2].r * 0.5f;
						color[2].g = color[2].g *0.5f + colorB.color[2].g * 0.5f;
						color[2].b = color[2].b *0.5f + colorB.color[2].b * 0.5f;
					}
				} else
					color[2] = colorB.color[2];
			} else
				color[3] = null;
			if ( useSpots ) {
				if ( color[3] != null ) {
					if ( colorB.color[3]!=null ) {
						color[3].r = color[3].r *0.5f + colorB.color[3].r * 0.5f;
						color[3].g = color[3].g *0.5f + colorB.color[3].g * 0.5f;
						color[3].b = color[3].b *0.5f + colorB.color[3].b * 0.5f;
					}
				} else
					color[3] = colorB.color[3];
			} else
				color[3] = null;
			
			FruitColor newColor = new FruitColor( useOver, useSpots, color[0], // baseColor
					color[1], // capColor
					color[2], // secondaryColor
					color[3] ); // spotColor
			
			newStrain.colors.add( new Integer( Assets.player.customColors.size() + ST.CUSTOM_COLOR_INDEX_OFFSET ) );
			Assets.player.customColors.add( newColor );
			
			// add any other colors
			for ( int i = 0; i < A.colors.size(); i ++ )
				if ( i!=indexA )
					newStrain.colors.add( A.colors.get( i ) );
			for ( int i = 0; i < B.colors.size(); i ++ )
				if ( i!=indexB )
					newStrain.colors.add( B.colors.get( i ) );
			break;
		}
		
		
		newStrain.fruit_width_Var = random.nextBoolean()? A.fruit_width_Var: B.fruit_width_Var;
		newStrain.fruit_height_Var = random.nextBoolean()? A.fruit_height_Var: B.fruit_height_Var;
		newStrain.costsCoins = random.nextBoolean()? A.costsCoins: B.costsCoins;
		
		// randomly mutate one quality by (randomly) good or bad
		switch ( random.nextInt( 8 ) ) {
		case 0:
			newStrain.jar_grow_time += (float)newStrain.jar_grow_time*0.1f -
				((float)newStrain.jar_grow_time*0.2f * random.nextFloat()); // plus or minus up to %10 of current
			break;
		case 1:
			newStrain.casing_grow_time += (float)newStrain.casing_grow_time*0.1f -
				((float)newStrain.casing_grow_time*0.2f * random.nextFloat()); // plus or minus up to %10 of current
			break;
		case 2:
			newStrain.fruit_grow_time += (float)newStrain.fruit_grow_time*0.1f -
				((float)newStrain.fruit_grow_time*0.2f * random.nextFloat()); // plus or minus up to %10 of current
			break;
		case 3:
			newStrain.fruit_weight += (float)newStrain.fruit_weight*0.1f -
				((float)newStrain.fruit_weight*0.2f * random.nextFloat()); // plus or minus up to %10 of current
			// but don't let fruit_weight get below 0.4f;
			if ( newStrain.fruit_weight<0.4f ) newStrain.fruit_weight = 0.4f;
			break;
		case 4:
			newStrain.potency += (float)newStrain.potency*0.1f -
				((float)newStrain.potency*0.2f * random.nextFloat()); // plus or minus up to %10 of current
			break;
		case 5:
			newStrain.batch_weight += (float)newStrain.batch_weight*0.1f -
				((float)newStrain.batch_weight*0.2f * random.nextFloat()); // plus or minus up to %10 of current
			break;
		default:
			break;
		}
		
		// make sure the fruit_weight never varies below 0.0f or even 0.2f
		if ( newStrain.fruit_weight-newStrain.fruit_weight_Var < 0.2f )
			newStrain.fruit_weight_Var = newStrain.fruit_weight * 0.5f;
		
		return newStrain;
	}
	
	public static FruitColor getColor( int index ) {
		if ( index>= ST.CUSTOM_COLOR_INDEX_OFFSET )
			if ( (index - ST.CUSTOM_COLOR_INDEX_OFFSET ) >= Assets.player.customColors.size() )
				return Assets.player.customColors.get( Assets.player.customColors.size() -1 );
			else
				return Assets.player.customColors.get( index - ST.CUSTOM_COLOR_INDEX_OFFSET );
		return ST.fruitColors[ index ];
	}
	
	public static void loadStrains() {
		strains = new ArrayList<Strain>();
		Random random = new Random();
		strain_enum = random.nextInt(30) + 1;
		strainEnumIncreases = random.nextBoolean();
		
		Strain newStrain = new Strain( "Golden Teacher", "The Golden Teacher is a classic." );
		newStrain.jar_grow_time = 10800;
		newStrain.casing_grow_time = 7200;
		newStrain.pins_time = 65;
		newStrain.fruit_grow_time = 1800;
		newStrain.fruit_weight = 0.5f;
		newStrain.fruit_weight_Var = 0.1f;
		newStrain.potency = 9.4f;
		newStrain.potency_Var = 0.2f;
		newStrain.batch_weight = 25.0f;
		newStrain.batch_weight_Var = 1.0f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.A );
		
		newStrain.fruit_width_Var = 0.2f;
		newStrain.fruit_height_Var = 0.3f;
		strains.add( newStrain );
		
		newStrain = new Strain( "B+", "The B+ origins have become a thing of legend." );
		newStrain.jar_grow_time = 11400;
		newStrain.casing_grow_time = 6600;
		newStrain.pins_time = 75;
		newStrain.fruit_grow_time = 2100;
		newStrain.fruit_weight = 0.7f;
		newStrain.fruit_weight_Var = 0.1f;
		newStrain.potency = 8.2f;
		newStrain.potency_Var = 0.3f;
		newStrain.batch_weight = 26.0f;
		newStrain.batch_weight_Var = 1.0f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.D );
		newStrain.fruit_width_Var = 0.2f;
		newStrain.fruit_height_Var = 0.3f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Acadian Coast", "People who try it seem to enjoy it." );
		newStrain.jar_grow_time = 150*60;
		newStrain.casing_grow_time = 120*60;
		newStrain.pins_time = 55;
		newStrain.fruit_grow_time = 50*60;
		newStrain.fruit_weight = 0.57f;
		newStrain.fruit_weight_Var = 0.2f;
		newStrain.potency = 10.3f;
		newStrain.potency_Var = 0.1f;
		newStrain.batch_weight = 26.5f;
		newStrain.batch_weight_Var = 1.5f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.E );
		newStrain.fruit_width_Var = 0.5f;
		newStrain.fruit_height_Var = 0.3f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Costa Rica", "Generated from a Costa Rican sample that was labeled as an unknown landslide mushroom." );
		newStrain.jar_grow_time = 330*60;
		newStrain.casing_grow_time = 220*60;
		newStrain.pins_time = 45;
		newStrain.fruit_grow_time = 48*60;
		newStrain.fruit_weight = 0.83f;
		newStrain.fruit_weight_Var = 0.12f;
		newStrain.potency = 10.5f;
		newStrain.potency_Var = 0.5f;
		newStrain.batch_weight = 24.5f;
		newStrain.batch_weight_Var = 2.5f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.F );
		newStrain.fruit_width_Var = 0.1f;
		newStrain.fruit_height_Var = 0.3f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Lizard King", "Found growing on a mixture of wood and horse poo." );
		newStrain.jar_grow_time = 130*60;
		newStrain.casing_grow_time = 90*60;
		newStrain.pins_time = 50;
		newStrain.fruit_grow_time = 22*60;
		newStrain.fruit_weight = 0.43f;
		newStrain.fruit_weight_Var = 0.1f;
		newStrain.potency = 8.6f;
		newStrain.potency_Var = 0.2f;
		newStrain.batch_weight = 26.5f;
		newStrain.batch_weight_Var = 0.5f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.DUNG;
		newStrain.colors.add( ST.G );
		newStrain.fruit_width_Var = 0.2f;
		newStrain.fruit_height_Var = 0.2f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Ecuador", "While not the fastest growing cube, the EQ makes up for it with its impressive flushes." );
		newStrain.jar_grow_time = 165*60;
		newStrain.casing_grow_time = 135*60;
		newStrain.pins_time = 78;
		newStrain.fruit_grow_time = 30*60;
		newStrain.fruit_weight = 0.3f;
		newStrain.fruit_weight_Var = 0.05f;
		newStrain.potency = 9.8f;
		newStrain.potency_Var = 0.1f;
		newStrain.batch_weight = 29.2f;
		newStrain.batch_weight_Var = 0.7f;
		newStrain.temp_required = ST.MEDIUM;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.U );
		newStrain.fruit_width_Var = 0.3f;
		newStrain.fruit_height_Var = 0.3f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Philosopher's Stone", "Easily the most potent, these mushrooms give a super introspective and meditative experience." );
		newStrain.jar_grow_time = 190*60;
		newStrain.casing_grow_time = 140*60;
		newStrain.pins_time = 42;
		newStrain.fruit_grow_time = 42*60;
		newStrain.fruit_weight = 0.85f;
		newStrain.fruit_weight_Var = 0.05f;
		newStrain.potency = 12.4f;
		newStrain.potency_Var = 0.2f;
		newStrain.batch_weight = 31.2f;
		newStrain.batch_weight_Var = 0.8f;
		newStrain.temp_required = ST.MEDIUM;
		newStrain.humidity_cost = 11;
		newStrain.substrate = ST.STRAW;
		newStrain.colors.add( ST.B );
		newStrain.fruit_width_Var = 0.3f;
		newStrain.fruit_height_Var = 0.5f;
		strains.add( newStrain );
		
		newStrain = new Strain( "India Orissa", "Easily the most potent, these mushrooms give a super introspective and meditative experience." );
		newStrain.jar_grow_time = 120*60;
		newStrain.casing_grow_time = 130*60;
		newStrain.pins_time = 65;
		newStrain.fruit_grow_time = 80*60;
		newStrain.fruit_weight = 1.14f;
		newStrain.fruit_weight_Var = 0.3f;
		newStrain.potency = 10.6f;
		newStrain.potency_Var = 0.4f;
		newStrain.batch_weight = 30.2f;
		newStrain.batch_weight_Var = 1.8f;
		newStrain.temp_required = ST.MEDIUM;
		newStrain.humidity_cost = 11;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.C );
		newStrain.fruit_width_Var = 0.2f;
		newStrain.fruit_height_Var = 0.1f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Albino Envy", "A strain with a disturbing mutation." );
		newStrain.jar_grow_time = 7454;
		newStrain.casing_grow_time = 7419;
		newStrain.pins_time = 37;
		newStrain.fruit_grow_time = 1946;
		newStrain.fruit_weight = 0.39f;
		newStrain.fruit_weight_Var = 0.15f;
		newStrain.potency = 9.6f;
		newStrain.potency_Var = 0.5f;
		newStrain.batch_weight = 23.0f;
		newStrain.batch_weight_Var = 2.8f;
		newStrain.temp_required = ST.MEDIUM;
		newStrain.humidity_cost = 11;
		newStrain.substrate = ST.STRAW;
		newStrain.colors.add( ST.H );
		newStrain.fruit_width_Var = 0.1f;
		newStrain.fruit_height_Var = 0.5f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Ocean Body", "A difficult strain to grow, these mushrooms are a blue-ish hue." );
		newStrain.jar_grow_time = 14228;
		newStrain.casing_grow_time = 5158;
		newStrain.pins_time = 75;
		newStrain.fruit_grow_time = 4234;
		newStrain.fruit_weight = 0.81f;
		newStrain.fruit_weight_Var = 0.1f;
		newStrain.potency = 12.09f;
		newStrain.potency_Var = 0.6f;
		newStrain.batch_weight = 21.45f;
		newStrain.batch_weight_Var = 4.8f;
		newStrain.temp_required = ST.HIGH;
		newStrain.humidity_cost = 12;
		newStrain.substrate = ST.WOOD_CHIPS;
		newStrain.colors.add( ST.I );
		newStrain.fruit_width_Var = 0.1f;
		newStrain.fruit_height_Var = 0.1f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Quick Cubes", "A good strain for beginners and experts alike, this strain is know for its quick grow time." );
		newStrain.jar_grow_time = 6718;
		newStrain.casing_grow_time = 4583;
		newStrain.pins_time = 49;
		newStrain.fruit_grow_time = 3232;
		newStrain.fruit_weight = 0.43f;
		newStrain.fruit_weight_Var = 0.3f;
		newStrain.potency = 7.16f;
		newStrain.potency_Var = 0.76f;
		newStrain.batch_weight = 24.66f;
		newStrain.batch_weight_Var = 2.8f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.DUNG;
		newStrain.colors.add( ST.J );
		newStrain.fruit_width_Var = 0.1f;
		newStrain.fruit_height_Var = 0.6f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Cambodian", "This special strain comes from the ancient temple of Angkor Wat, located in South East Asia. It was used regularly by the early inhabitants there." );
		newStrain.jar_grow_time = 10353;
		newStrain.casing_grow_time = 7695;
		newStrain.pins_time = 52;
		newStrain.fruit_grow_time = 4050;
		newStrain.fruit_weight = 0.675f;
		newStrain.fruit_weight_Var = 0.5f;
		newStrain.potency = 12.33f;
		newStrain.potency_Var = 0.1f;
		newStrain.batch_weight = 25.34f;
		newStrain.batch_weight_Var = 3.1f;
		newStrain.temp_required = ST.HIGH;
		newStrain.humidity_cost = 11;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.K );
		newStrain.fruit_width_Var = 0.3f;
		newStrain.fruit_height_Var = 0.1f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Fun Guy", "Very potent, this strain is rumored to be infused with trace amounts of DMT." );
		newStrain.jar_grow_time = 11153;
		newStrain.casing_grow_time = 7513;
		newStrain.pins_time = 83;
		newStrain.fruit_grow_time = 2858;
		newStrain.fruit_weight = 0.617f;
		newStrain.fruit_weight_Var = 0.15f;
		newStrain.potency = 12.88f;
		newStrain.potency_Var = 1.1f;
		newStrain.batch_weight = 30.36f;
		newStrain.batch_weight_Var = 5.1f;
		newStrain.temp_required = ST.MEDIUM;
		newStrain.humidity_cost = 12;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.L );
		newStrain.fruit_width_Var = 0.36f;
		newStrain.fruit_height_Var = 0.7f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Treasure Coast", "Domesticated from wild samples found along the Southern Florida Gulf Coast." );
		newStrain.jar_grow_time = 4708;
		newStrain.casing_grow_time = 6915;
		newStrain.pins_time = 82;
		newStrain.fruit_grow_time = 4103;
		newStrain.fruit_weight = 0.859f;
		newStrain.fruit_weight_Var = 0.05f;
		newStrain.potency = 7.04f;
		newStrain.potency_Var = 0.01f;
		newStrain.batch_weight = 28.2f;
		newStrain.batch_weight_Var = 0.1f;
		newStrain.temp_required = ST.HIGH;
		newStrain.humidity_cost = 12;
		newStrain.substrate = ST.WOOD_CHIPS;
		newStrain.colors.add( ST.M );
		newStrain.fruit_width_Var = 0.01f;
		newStrain.fruit_height_Var = 0.01f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Super M", "Named after a popular video game character, these mushrooms will make you feel huge." );
		newStrain.jar_grow_time = 10092;
		newStrain.casing_grow_time = 9117;
		newStrain.pins_time = 44;
		newStrain.fruit_grow_time = 3014;
		newStrain.fruit_weight = 0.97f;
		newStrain.fruit_weight_Var = 0.55f;
		newStrain.potency = 12.4f;
		newStrain.potency_Var = 0.01f;
		newStrain.batch_weight = 24.65f;
		newStrain.batch_weight_Var = 3.14f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 12;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.N );
		newStrain.colors.add( ST.N );
		newStrain.colors.add( ST.N );
		newStrain.colors.add( ST.O );
		newStrain.fruit_width_Var = 0.4f;
		newStrain.fruit_height_Var = 0.4f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Easter Egg", "A strange purple mushroom, this strain can only be found during April." );
		newStrain.jar_grow_time = 9739;
		newStrain.casing_grow_time = 7897;
		newStrain.pins_time = 40;
		newStrain.fruit_grow_time = 3457;
		newStrain.fruit_weight = 0.429f;
		newStrain.fruit_weight_Var = 0.12f;
		newStrain.potency = 11.9f;
		newStrain.potency_Var = 1.1f;
		newStrain.batch_weight = 34.46f;
		newStrain.batch_weight_Var = 1.5f;
		newStrain.temp_required = ST.HIGH;
		newStrain.humidity_cost = 11;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.P );
		newStrain.fruit_width_Var = 0.1f;
		newStrain.fruit_height_Var = 0.2f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Jeepers Creepers", "Known for being scary potent, this strain is harder to grow but well worth it." );
		newStrain.jar_grow_time = 7159;
		newStrain.casing_grow_time = 6123;
		newStrain.pins_time = 86;
		newStrain.fruit_grow_time = 3924;
		newStrain.fruit_weight = 0.9f;
		newStrain.fruit_weight_Var = 0.25f;
		newStrain.potency = 11.66f;
		newStrain.potency_Var = 2.0f;
		newStrain.batch_weight = 32.15f;
		newStrain.batch_weight_Var = 2.5f;
		newStrain.temp_required = ST.HIGH;
		newStrain.humidity_cost = 12;
		newStrain.substrate = ST.DUNG;
		newStrain.colors.add( ST.Q );
		newStrain.colors.add( ST.R );
		newStrain.fruit_width_Var = 0.3f;
		newStrain.fruit_height_Var = 0.2f;
		strains.add( newStrain );

		newStrain = new Strain( "Business Man", "Breed for efficiency, this strain is all about the Benjamins." );
		newStrain.jar_grow_time = 10902;
		newStrain.casing_grow_time = 4076;
		newStrain.pins_time = 33;
		newStrain.fruit_grow_time = 1763;
		newStrain.fruit_weight = 0.95f;
		newStrain.fruit_weight_Var = 0.01f;
		newStrain.potency = 8.75f;
		newStrain.potency_Var = 0.01f;
		newStrain.batch_weight = 32.73f;
		newStrain.batch_weight_Var = 0.01f;
		newStrain.temp_required = ST.MEDIUM;
		newStrain.humidity_cost = 10;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.S );
		newStrain.fruit_width_Var = 0.01f;
		newStrain.fruit_height_Var = 0.01f;
		strains.add( newStrain );
		

		newStrain = new Strain( "Z Strain", "A very aggressive colonizer, Z-Strain is a cousin to the A and B+." );
		newStrain.jar_grow_time = 4094;
		newStrain.casing_grow_time = 5685;
		newStrain.pins_time = 75;
		newStrain.fruit_grow_time = 2602;
		newStrain.fruit_weight = 0.4088f;
		newStrain.fruit_weight_Var = 0.1f;
		newStrain.potency = 7.9f;
		newStrain.potency_Var = 0.9f;
		newStrain.batch_weight = 26.8f;
		newStrain.batch_weight_Var = 2.2f;
		newStrain.temp_required = ST.LOW;
		newStrain.humidity_cost = 11;
		newStrain.substrate = ST.STRAW;
		newStrain.colors.add( ST.T );
		newStrain.fruit_width_Var = 0.01f;
		newStrain.fruit_height_Var = 0.4f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Stamets", "Named for the famous mycologist, this strain takes an extremely long time but is worth the wait." );
		newStrain.jar_grow_time = 14635;
		newStrain.casing_grow_time = 12970;
		newStrain.pins_time = 53;
		newStrain.fruit_grow_time = 4632;
		newStrain.fruit_weight = 1.1979914f;
		newStrain.fruit_weight_Var = 0.25f;
		newStrain.potency = 15.567175f;
		newStrain.potency_Var = 0.6f;
		newStrain.batch_weight = 55.685776f;
		newStrain.batch_weight_Var = 8.2f;
		newStrain.temp_required = ST.HIGH;
		newStrain.humidity_cost = 13;
		newStrain.substrate = ST.WOOD_CHIPS;
		newStrain.colors.add( ST.V );
		newStrain.fruit_width_Var = 0.1f;
		newStrain.fruit_height_Var = 0.15f;
		strains.add( newStrain );
		
		newStrain = new Strain( "Midnight", "One of the most robust strains, the caps appear a dark blue and the caps are black as charcoal." );
		newStrain.costsCoins = true;
		newStrain.jar_grow_time = 6470;
		newStrain.casing_grow_time = 3091;
		newStrain.pins_time = 35;
		newStrain.fruit_grow_time = 4632;
		newStrain.fruit_weight = 0.5160111f;
		newStrain.fruit_weight_Var = 0.13f;
		newStrain.potency = 7.162711f;
		newStrain.potency_Var = 0.6f;
		newStrain.batch_weight = 25.967907f;
		newStrain.batch_weight_Var = 3.4f;
		newStrain.temp_required = ST.MEDIUM;
		newStrain.humidity_cost = 11;
		newStrain.substrate = ST.COCO_COIR;
		newStrain.colors.add( ST.W );
		newStrain.fruit_width_Var = 0.4f;
		newStrain.fruit_height_Var = 0.05f;
		strains.add( newStrain );

	}
	
	/** returns a short string for naming new custom strains */
	public static String getNewStrainName() {
		String string = "";
		Random random = new Random();
		switch( strain_enum ) {
		case 1: string = "AAA"; break;
		case 2: string = "DD"; break;
		case 3: string = "SS"; break;
		case 4: string = string.equals( "" )? "Jay": ""; break;
		case 5: string = "G"; break;
		case 6: string = "H"; break;
		case 7: string = "I"; break;
		case 8: string = "J"; break;
		case 9: string = "K"; break;
		case 10: string = "L"; break;
		case 11: string = "M"; break;
		case 12: string = "N"; break;
		case 13: string = "O"; break;
		case 14: string = "P"; break;
		case 15: string = "Q"; break;
		case 16: string = "R"; break;
		case 17: string = "S"; break;
		case 18: string = "T"; break;
		case 19: string = "U"; break;
		case 20: string = "V"; break;
		case 21: string = "W"; break;
		case 22: string = "X"; break;
		case 23: string = "Y"; break;
		case 24: string = "A"; break;
		case 25: string = "B"; break;
		case 26: string = "C"; break;
		case 27: string = "D"; break;
		case 28: string = "E"; break;
		case 29: string = "F"; break;
		case 30: string = "SG"; break;
		case 31: string = "GT"; break;
		}
		if ( strainEnumIncreases )
			strain_enum++;
		else
			strain_enum++;
		
		if ( strain_enum>31 ) {
			strain_enum = random.nextInt(31) + 1;
			strainEnumIncreases = random.nextBoolean();
		}
		if ( strain_enum<=0 ) {
			strain_enum = random.nextInt(31) + 1;
			strainEnumIncreases = random.nextBoolean();
		}
		
		switch ( random.nextInt(55) ) {
		case 0: case 21: string += "+"; break;
		case 1: string += "++"; break;
		case 2: string += "/1"; break;
		case 3: string = "Big " + string; break;
		case 4: string += "-"; break;
		case 5: string = string+string; break;
		case 6: case 20: case 29:
			strain_enum = random.nextInt(30) + 1;
			string = string + " " + getNewStrainName();
			break;
		case 7: string += " Envy"; break;
		case 8: string = "Ultra " + string; break;
		case 9: string = "Super " + string; break;
		case 10: string = "lil " + string; break;
		case 11: string = "Sweet " + string; break;
		case 12: string = "Grade " + string; break;
		case 13: string = "Strain " + string; break;
		case 14: string = "Easy " + string; break;
		case 15: string = "Sexy " + string; break;
		case 16: string = "Golden " + string; break;
		case 17: string = "New " + string; break;
		case 18: string = "Deep " + string; break;
		case 19: string = "Dr. " + string; break;
//		case 22: string += "="; break;
		case 23: string += "*"; break;
		case 24: string += "#"; break;
		case 25: string = "Double " + string; break;
		case 26: string = "Mr. " + string; break;
		case 27: string = "Ms. " + string; break;
		case 28: string = "Mrs. " + string; break;
		case 30: string = "Triple " + string; break;
		case 31: string += " " + random.nextInt( 10 ); break;
		case 32: string = "Heavy " + string; break;
		case 33: string = "Psycho " + string; break;
		case 34: string = "Quick " + string; break;
		case 35: string = string + " Paranoid"; break;
		case 36: string = string + " Body"; break;
		case 37: string = string + " King"; break;
		case 38: string = string + " X" + (random.nextInt(3)+2); break;
		case 39: string += " " + (random.nextInt( 900 ) + 100); break;
		case 40: string = "Magic " + string; break;
		case 41: string = random.nextBoolean()? "West " + string : string + " West"; break;
		case 42: string = "Giant " + string; break;
		case 43: string = "Blue " + string; break;
		case 44: string = "Sub " + string; break;
		case 45: string = "Teemo " + string; break;
		case 46: string = string.replaceAll( " ", "" ); break;
		default: break;
		}

		return string.trim();
	}
}
