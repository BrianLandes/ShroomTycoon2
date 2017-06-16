package com.bytesbrothers.shroomtycoon;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.utils.Array;
import com.bytesbrothers.shroomtycoon.structures.FruitColor;

public class ST {
	
	/** Time (in seconds) it takes to complete a project part. Some projects contain multiple parts. */
	public static final int PROJECT_TIME = 20;
	/** Max xp for maintenance */
	public static final int MAINTENANCE_XP = 20;
	
	/*
	 * Menu constants
	 */
	public static final int MENU_NEW_GAME = 0;
	public static final int MENU_SAVE = 1;
	public static final int MENU_CLOSE = 2;
	
	/*
     * Substrate constants
     */
	public static final int COCO_COIR = 0;
	public static final int STRAW = 1;
	public static final int DUNG = 2;
	public static final int WOOD_CHIPS = 3;
	public static final String[] substrates = { "Coco Coir", "Straw", "Dung", "Wood Chips" };
	public static final String[] SUB_NAMES = { "CocoCoir", "Straw", "Dung", "WoodChips" };
	
	/*
	 * Arrow constants
	 */
	public static final int NONE = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	public static final int DOWN = 4;
	
	/*
	 * Casing Mold & Fruit Constants
	 */
//	public static final int NUM_MOLDS = 7;
//	public static final int NUM_CONTS = 5;
//	public static final int NUM_FRUITS = 20;
	
	public static final int NO_MOLD = -1;
	public static final int A = 0;
	public static final int B = 1;
	public static final int C = 2;
	public static final int D = 3;
	public static final int E = 4;
	public static final int F = 5;
	public static final int G = 6;
	public static final int H = 7;
	public static final int I = 8;
	public static final int J = 9;
	public static final int K = 10;
	public static final int L = 11;
	public static final int M = 12;
	public static final int N = 13;
	public static final int O = 14;
	public static final int P = 15;
	public static final int Q = 16;
	public static final int R = 17;
	public static final int S = 18;
	public static final int T = 19;
	public static final int U = 20;
	public static final int V = 21;
	public static final int W = 22;
	public static final int X = 23;
	public static final int Y = 24;
	public static final int Z = 25;
	public static final int AA = 26;
	public static final int AB = 27;
	public static final int AC = 28;
	public static final int AD = 29;
	public static final int AE = 30;
	public static final int AF = 31;
	public static final int AG = 32;
	public static final int AH = 33;
	public static final int AI = 34;
	public static final int AJ = 35;
	
	public static final int CUSTOM_COLOR_INDEX_OFFSET = 1001;
	
	public static final String[] MOLD_NAMES = {"MoldA", "MoldB", "MoldC", "MoldD", "MoldE", "MoldF", "MoldG", "MoldH", "MoldI" };
	public static final String[] CONT_NAMES = {"ContA", "ContB", "ContC", "ContD", "ContE" };
	public static final String[] FRUIT_NAMES = {"FruitA", "FruitB", "FruitC", "FruitD", "FruitE", "FruitF", "FruitG"
		, "FruitH", "FruitI", "FruitJ", "FruitK", "FruitL", "FruitM", "FruitN", "FruitO", "FruitP"
		, "FruitQ", "FruitR", "FruitS", "FruitT", "FruitU" };
	
	public static final String[] JAR_MOLD_NAMES = {"MoldA", "MoldB", "MoldC", "MoldD", "MoldE", "MoldF", "MoldG", "MoldH", "MoldI", "MoldJ", "MoldK" };
	public static final String[] JAR_CONT_NAMES = {"ContA", "ContB", "ContC", "ContD" };

	
	/*
	 * Fruit Colors
	 */
	public static final Color goldenTeacher = new Color( 0.827f, 0.5647f, 0.14117f, 1.0f );
	public static final Color philosophersStone = new Color( 0.655f, 0.27f, 0.098f, 1.0f );
	public static final Color indiaOrissa = new Color( 0.482f, 0.30588f, 0.1529f, 1.0f );
	public static final Color indiaOrissaSecond = new Color( 0.9529f, 0.8078f, 0.54117f, 0.6f );
	public static final Color acadianCoast = new Color( 1.0f, 0.6078f, 0.0f, 1.0f );
	public static final Color costaRica = new Color( 0.8549019607843137f, 0.803921568627451f, 0.1529411764705882f, 1.0f );
	public static final Color lizardKing = new Color( 0.8470588235294118f, 0.392156862745098f, 0.2f, 1.0f );
	public static final Color lizardKingSecond = new Color( 0.9294117647058824f, 0.6705882352941176f, 0.3647058823529412f, 1.0f );
	public static final Color albinoEnvy = new Color( 0.8431372549019608f, 0.8549019607843137f, 0.8941176470588235f, 1.0f );
	public static final Color albinoEnvyStem = new Color( 0.9294117647058824f, 0.9176470588235294f, 0.9098039215686275f, 1.0f );
	public static final Color oceanBody = new Color( 0.192156862745098f, 0.3058823529411765f, 0.6431372549019608f, 1.0f );
	public static final Color quickCubes = new Color( 0.8705882352941176f, 0.6705882352941176f, 0.5764705882352941f, 1.0f );
	public static final Color quickCubesSecond = new Color( 0.9647058823529412f, 0.8274509803921569f, 0.5019607843137255f, 1.0f );
	public static final Color cambodian = new Color( 0.3803921568627451f, 0.1294117647058824f, 0.1176470588235294f, 1.0f );
	public static final Color funGuy = new Color( 0.3137254901960784f, 0.2f, 0.407843137254902f, 1.0f );
	public static final Color funGuySecond = new Color( 0.3411764705882353f, 0.1372549019607843f, 0.1882352941176471f, 1.0f );
	public static final Color treasureCoastSpots = new Color( 0.6313725490196078f, 0.1803921568627451f, 0.0f, 1.0f );
	public static final Color superM = new Color( 0.9176470588235294f, 0.0f, 0.0313725490196078f, 1.0f );
	public static final Color superMSecond = new Color( 0.4352941176470588f, 0.0f, 0.003921568627451f, 1.0f );
	public static final Color superMSpots = new Color( 1f, 1.0f, 1f, 1.0f );
	public static final Color superMGreen = new Color( 0.1294117647058824f, 0.6666666666666667f, 0.3215686274509804f, 1.0f );
	public static final Color superMGreenSecond = new Color( 0.0117647058823529f, 0.3215686274509804f, 0.1490196078431373f, 1.0f );
	public static final Color easterEgg = new Color( 0.8470588235294118f, 0.2980392156862745f, 0.7686274509803922f, 1.0f );
	public static final Color easterEggSecond = new Color( 0.5019607843137255f, 0.2666666666666667f, 0.9411764705882353f, 1.0f );
	public static final Color easterEggSpots = new Color( 0.996078431372549f, 0.9294117647058824f, 0.2705882352941176f, 1.0f );
	public static final Color jeepersCreepersA = new Color( 0.8705882352941176f, 0.6705882352941176f, 0.5764705882352941f, 1.0f );
	public static final Color jeepersCreepersSpotsA = new Color( 0.9647058823529412f, 0.8235294117647059f, 0.4470588235294118f, 1.0f );
	public static final Color jeepersCreepersB = new Color( 0.8470588235294118f, 0.392156862745098f, 0.2f, 1.0f );
	public static final Color jeepersCreepersSpotsB = new Color( 0.8313725490196078f, 0.5803921568627451f, 0.1725490196078431f, 1.0f );
	public static final Color businessMan = new Color( 0.396078431372549f, 0.1254901960784314f, 0.0980392156862745f, 1.0f );
	public static final Color businessManSpots = new Color( 0.9215686274509804f, 0.6509803921568627f, 0.3529411764705882f, 1.0f );
	public static final Color zStrain = new Color( 0.8274509803921569f, 0.5803921568627451f, 0.1843137254901961f, 1.0f );
	public static final Color zStrainSpots = new Color( 0.4392156862745098f, 0.1215686274509804f, 0.0392156862745098f, 1.0f );
	public static final Color ecuador = new Color( 0.5215686274509804f, 0.2784313725490196f, 0.0784313725490196f, 1.0f );
	public static final Color ecuadorSecond = new Color( 0.5607843137254902f, 0.3058823529411765f, 0.2156862745098039f, 1.0f );
	public static final Color stamets = new Color( 0.9215686274509804f, 0.4313725490196078f, 0.2666666666666667f, 1.0f );
	public static final Color stametsSecond = new Color( 1f, 0.9607843137254902f, 0.7647058823529412f, 1.0f );
	public static final Color midnight = new Color( 0.1294117647058824f, 0.7450980392156863f, 0.8705882352941176f, 1.0f );
	public static final Color midnightStem = new Color( 0.2f, 0.2f, 0.2f, 1.0f );
	public static final Color midnightSpots = new Color( 0.1f, 0.1f, 0.1f, 1.0f );
	
	
	/*
	 * Fruit Colors in a usable array
	 */
	public static final FruitColor[] fruitColors = { new FruitColor( false, false, null, goldenTeacher, null, null ), // goldenTeacher
		new FruitColor( false, false, null, philosophersStone, null, null ),
		new FruitColor( true, false, null, indiaOrissa, indiaOrissaSecond, null ),
		new FruitColor( true, false, null, goldenTeacher, goldenTeacher, null ), // B+
		new FruitColor( false, false, null, acadianCoast, null, null ),
		new FruitColor( false, false, null, costaRica, null, null ), 
		new FruitColor( true, false, null, lizardKing, lizardKingSecond, null ),
		new FruitColor( false, false, albinoEnvyStem, albinoEnvy, null, null ),
		new FruitColor( false, false, null, oceanBody, null, null ), 
		new FruitColor( true, false, null, quickCubes, quickCubesSecond, null ),
		new FruitColor( true, false, null, cambodian, cambodian, null ), 
		new FruitColor( true, false, null, funGuy, funGuySecond, null ), 
		new FruitColor( false, true, null, goldenTeacher, null, treasureCoastSpots ), // treasure coast
		new FruitColor( true, true, null, superM, superMSecond, superMSpots ),
		new FruitColor( true, true, null, superMGreen, superMGreenSecond, superMSpots ),
		new FruitColor( true, true, null, easterEgg, easterEggSecond, easterEggSpots ),
		new FruitColor( false, true, null, jeepersCreepersA, null, jeepersCreepersSpotsA ),
		new FruitColor( false, true, null, jeepersCreepersB, null, jeepersCreepersSpotsB ),
		new FruitColor( false, true, null, businessMan, null, businessManSpots ),
		new FruitColor( false, true, null, zStrain, null, zStrainSpots ),
		new FruitColor( true, false, null, ecuador, ecuadorSecond, null ),
		new FruitColor( true, true, null, stamets, stametsSecond, stamets ),
		new FruitColor( false, true, midnightStem, midnight, null, midnightSpots ),
		new FruitColor( false, true, philosophersStone, goldenTeacher, null, jeepersCreepersSpotsA ) };
	
	/*
	 * Lab constants
	 */
	public static final int LOW = 0;
	public static final int MEDIUM = 1;
	public static final int HIGH = 2;
	public static final String[] temps = { "Good", "Better", "Best" };
	public static final int[] INC_UPGRADE_PRICES = { 0, 800, 4500 };
	public static final int[] FC_UPGRADE_PRICES = { 0, 1200, 6400 };
	public static final int[] humidities = { 50, 65, 75, 85, 100 };
	public static final int[] FC_HUMIDITY_PRICES = { 0, 540, 820, 1150, 1520 };
	public static final int[] PC_CAPACITIES = { 3, 5, 8, 10 };
	public static final int[] PC_CAP_PRICES = { 0, 510, 1020, 1530 };
	public static final int PC_TIME = 10 * 60; // 10 minutes
	
//	public static final int JAR_MAINTENANCE = 45;
	
	/*
	 * Location constants
	 */
	public static final int INCUBATOR = 0;
	public static final int FC = 1;
	public static final int FRIDGE = 2;
	
	/*
	 * Soaking
	 */
	public static final int SOAK_TIME = 60 * 60; // (in seconds) 60 minutes
	
	public static final String[] locations = { "Incubator", "Fruiting Chamber", "Fridge" };
	
	/*
	 * Alert Types
	 */
//	public static final int SCREEN = 0;
	public static final int SWEET_DIALOG = 1;
	public static final int SPEECH_DIALOG = 2;
//	public static final int SIDE_JOB_DIALOG = 3;
	public static final int TABLE = 4;
	public static final int MAIN_COLUMN = 5;
	public static final int COINS_DIALOG = 6;
	
	/*
	 * Sam's loan
	 */
	public static final int SAMS_LOAN = 15000;
	public static final int SAMS_BONUS_TIME = 24*60*60; // time in seconds that player can get sams bonus
	public static final int SAMS_BONUS_PRICE = 200;
	
	/*
	 * 
	 */
	public static final int MAX_FLOATING_TEXTS = 20;
	public static final int MAX_BACK_SCREENS = 20;
	
	/*
	 * In-App constants
	 */
	public static final String SKU_FIVE = "five_blue_coins";
	public static final String SKU_TWENTY = "twenty_blue_coins";
	public static final String SKU_THIRTY_NINE = "thirty_nine_blue_coins";
	public static final String SKU_FIFTY = "fifty_blue_coins";
	public static final String SKU_HUNDRED = "hundred_blue_coins";
	public static final String SKU_MONDAY = "hundred_blue_coins_monday";
	
	public static final String[] coinSkus = new String[] { SKU_FIVE, SKU_TWENTY,
		SKU_THIRTY_NINE, SKU_FIFTY, SKU_HUNDRED, SKU_MONDAY };
	
	/** some variables for4 your hip_hop consideration eminem */
	static final String eminem = "z/kBCFqD2l1gskT1myweW4Z+VPgvbG1EvBzYOILwfWd609L4kmbs2iGEXfxD+Du62itvXdAnkeZKVC0fQIDAQAB";
	static final String hip_hop = "nys7px1iq5Tb1AZv3Q1YTbxV1N5niQx8bX9qI/j8QIO3u1XDJ7Ej6QGCTqh0";
	static final String consideration = "tRk4kCEaeyn4dTYlU55MEKh3iZrhcRlrt9FtkdjKuI2AqqOCKBfi3SnolzkkMvahTOmItmgVUTERByq";
	static final String variables = "CAQ8AMIIBCgKCAQEAi5n1LBn4CBarw720BYd";
	static final String for4 = "i7iybCSMYPlNnzbzAIT3xQCRvWJERRhzGHVxRRuvKwUjWojhi2JWygRR9uk";
	static final String some = "MIIBIjANBgkqhkiG9w0BAQEFAAO";
	static final String your = "JY0mOjyeDS2pUBfL8lSER9z2wBU2XRp0A941bXloO7vJ";
	
	
	/* 
	 * Dealer names
	 */
	public static final String[] NAMES = { "AJ", "Adam", "Alan", "Alex", "Alissa", "Amy", "Andrea", "Andrew", "Austin", "Ben",
		"Blake", "Brady", "Brandon", "Brendan", "Brandy", "Brenda", "Brian", "Brooke", "Candy", "Cacee", "Camran", "Carl",
		"Carlos", "Cassidy", "Chris", "Christine", "Cody", "Colby", "Colton", "Corey", "Craig",  "Curtis",
		"Dakota", "Dan", "Dane", "Daniel", "Darin", "David", "Dion", "Don", "Drew", "Emily", "Ethan", "Eric", "Gary", "Greyson", "Gerald", "Heather", 
		"Jackson", "Jake", "Jamie", "Jared", "Jenn", "Jennifer", "Jessica", "Joe", "Jonathan", "Josh", "John", "Justin", "Kaci", 
		"Kasen", "Katherine", "Kevin", "Lauren", "Leslie", "Lisa", "Mary", "Matthew", "McKenzie", "Melody", "Miranda",
		"Nate", "Nichole", "Nick", "Paul", "Preston", "Rachael", "Rhett", "Ricky", "Ryan", "Samantha", "Sara",
		"Scott", "Sean", "Skylar", "Spencer", "Taylor", "Tod", "Travis", "Trevor", "Trisha", "Weston", "Zach", "Lee", "Kerry", "Toby", "Gordon", "Donna", "Cameron", "Roger",
		"Joan", "Betty", "Sally", "Elizabeth", "Andy", "Stephanie", "Terry", "Melissa", "Chelsea", "Charles", "Rick", "Andrew", "Sarah"
		, "Chandler", "Laurie", "Norman", "Daryl", "Andrea", "Lori", "Thomas", "Kate", "Ron", "Mark", "Tommy", "Kim", "Bobby", "Clarence", "Red" };
	public static final String[] INITIALS = { "A", "B", "B", "B", "B", "C", "C", "D", "D", "D", "E", "F", "F", "F", "G", "H", "J", "K", "L", "L", "M", "M", "M",
		"N", "N", "O", "P", "P", "P", "R", "R", "S", "S", "S", "T", "U", "V", "W", "W", "Y", "Z" };
	
//	public static final int DEALER_NEXT_LEVEL = 3500;
	
	public enum FruitLayer {
		BASE(),
		TOP(),
		TOP_OVER(),
		SPOTS();
	}
	
	public static String getName() {
		Random random = new Random();
		String result = NAMES[ random.nextInt( NAMES.length ) ];
		result += " " + INITIALS[ random.nextInt( INITIALS.length ) ] + ".";
		return result;
	}
	
	public static String withComas( float num ) {
		if ( num>=-0.001f && num<=0.001f || num==0 ) 
			return "0";
		num = Math.round( num );
		String result = "";
		boolean negative = false;
		if ( num<0 ) {
			negative = true;
			num *= -1;
		}
 		while ( num > 0 ) {
 			int threeDigits = (int) (num%1000);
 			num = (num - threeDigits)/1000;
 			String text = "" + threeDigits;
 			if ( num>0 ) {
 				if ( threeDigits<100 )
 					text = "0" + text;
 				if ( threeDigits<10 )
 					text = "0" + text;
 			}
 			if ( result.length()>0 )
 				result = "" + text +"," + result;
 			else
 				result = "" + text;
 			
 		}
 		if ( negative )
 			result = "-" + result;
 		return result;
	}
	public static String withComasAndTwoDecimals( float num ) {
		if ( num==0 ) 
			return "0";
		int whole = (int) num;
		String result = "";
 		while ( whole > 0 ) {
 			int threeDigits = (int) (whole%1000);
 			whole = (whole - threeDigits)/1000;
 			String text = "" + threeDigits;
 			if ( whole>0 ) {
 				if ( threeDigits<100 )
 					text = "0" + text;
 				if ( threeDigits<10 )
 					text = "0" + text;
 			}
 			if ( result.length()>0 )
 				result = "" + text +"," + result;
 			else
 				result = "" + text;
 			
 		}
 		result += ".";
 		int decimal = (int)((num%1.0f) * 100.0f);
 		if ( decimal<10 )
 			result += "0";
 		result += decimal;
 		return result;
	}
	
	public static int getJarMaintenance( int strainTemp, int incTemp ) {
		// Strain Temp		Inc Temp 		Maintenance every mins
		// LOW				LOW				60
		// LOW				MEDIUM			70
		// LOW				HIGH			80
		// MEDIUM			LOW				50
		// MEDIUM			MEDIUM			60
		// MEDIUM			HIGH			70
		// HIGH				LOW				40
		// HIGH				MEDIUM			50
		// HIGH				HIGH			60
		return ( (60 + incTemp*10) - strainTemp*10 ) * 60;
	}
	
	public static int getCasingMaintenance( int strainTemp, int incTemp ) {
		// Strain Temp		Inc Temp 		Maintenance every mins
		// LOW				LOW				45
		// LOW				MEDIUM			60
		// LOW				HIGH			75
		// MEDIUM			LOW				30
		// MEDIUM			MEDIUM			45
		// MEDIUM			HIGH			60
		// HIGH				LOW				15
		// HIGH				MEDIUM			30
		// HIGH				HIGH			45
		return ( (45 + incTemp*15) - strainTemp*15 ) * 60;
	}
	
	public static void focusNode( ScrollPane pane, Node node ) {
		if ( node.isExpanded() ) {
			boolean firstTime = true;
//			float left = 0;
			float bottom = 0;
//			float right = 0;
			float top = 0;
			pane.validate();
			Array<Node> children = node.getChildren();
			for ( Node child: children ) {
				Actor actor = child.getActor();
				if ( actor!=null ) {
//					float x = actor.getX();
					float y = actor.getY();
//					Vector2 firstShit = actor.localToStageCoordinates( new Vector2( 0, 0 ));
//					Vector2 someShit = pane.stageToLocalCoordinates( firstShit );
//					System.out.println( "FocusNode: " + y );
//					float width = actor.getWidth();
					float height = actor.getHeight();
					if ( firstTime ) {
//						left = x;
						bottom = y;
//						right = x+width;
						top = y+height;
						firstTime = false;
					} else {
//						if ( x<left )
//							left = x;
						if ( y<bottom )
							bottom = y;
//						if ( x+width>right )
//							right = x +width;
						if ( y+height>top )
							top = y + height;
					}
				}
			}
			
			pane.scrollTo( 0, bottom, 0, top-bottom );
//			pane.scrollToCenter( left, bottom, right-left, top-bottom );
		}

	}
	
	public static void focus( ScrollPane pane, Actor actor ) {
		float bottom = actor.getY();
		float top = bottom + actor.getHeight();
		pane.validate();

		pane.scrollTo( 0, bottom, 0, top-bottom );

	}
	
	public static final int ALERT_MAINTENANCE_ID = 901202;
	public static final int ALERT_INCUBATED_ID = 903217;
	public static final int ALERT_SOAKED_ID = 201;
	public static final int ALERT_HUMIDITY_ID = 34567;
	public static final int ALERT_FRUITED_ID = 721;
	public static final int ALERT_MIDNIGHT = 2004;
	public static final int ALERT_SAM_BONUS = 2005;
	public static final int ALERT_BUSINESS_MAN = 2006;
	public static final int ALERT_GLOVE_BOX = 2007;
	public static final int ALERT_JAR_MAINTENANCE_ID = 9002;
	public static final int ALERT_JAR_INCUBATED_ID = 25617;
	public static final int ALERT_PRESSURE_COOKER = 3008;
	
	/**
	 * y(x) = (1 / ( 1 + e^(-(x-0.5) * 10) ))
	 * @param x
	 * @return y
	 */
	public static float logisticSpread( float x ) {
		return (float) ( 1.0f/ (1.0f + Math.pow( Math.E, -(x - 0.5f) * 10.0f  ) ) );
	}
	
	public static Mesh create3dSquare( TextureRegion region,  TextureRegion topRegion,  TextureRegion topOverRegion,  TextureRegion spotsRegion ) {
		Mesh quad = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 3,   "a_position"), 
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"), 
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord1", 1), 
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord2", 2), 
				new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord3", 3)
		);
		float regionU = region==null? 0: region.getU();
		float regionU2 = region==null? 0: region.getU2();
		float regionV = region==null? 0: region.getV();
		float regionV2 = region==null? 0: region.getV2();
		
		float topU = topRegion==null? 0: topRegion.getU();
		float topU2 = topRegion==null? 0: topRegion.getU2();
		float topV = topRegion==null? 0: topRegion.getV();
		float topV2 = topRegion==null? 0: topRegion.getV2();
		
		float overU = topOverRegion==null? 0: topOverRegion.getU();
		float overU2 = topOverRegion==null? 0: topOverRegion.getU2();
		float overV = topOverRegion==null? 0: topOverRegion.getV();
		float overV2 = topOverRegion==null? 0: topOverRegion.getV2();
		
		float spotsU = spotsRegion==null? 0: spotsRegion.getU();
		float spotsU2 = spotsRegion==null? 0: spotsRegion.getU2();
		float spotsV = spotsRegion==null? 0: spotsRegion.getV();
		float spotsV2 = spotsRegion==null? 0: spotsRegion.getV2();

		quad.setVertices(new float[] { 
				-1f, -1f, 0, 	regionU, regionV2, 		topU, topV2,	overU, overV2, 		spotsU, spotsV2, // bottom left
				1f, -1f, 0, 	regionU2, regionV2, 	topU2, topV2,	overU2, overV2, 	spotsU2, spotsV2, // bottom right
				1f, 1f, 0, 		regionU2, regionV, 		topU2, topV,	overU2, overV, 		spotsU2, spotsV, // top right
				-1f, 1f, 0, 	regionU, regionV, 		topU, topV,		overU, overV, 		spotsU, spotsV }); // top left
		
		quad.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });
		return quad;
	}
}
