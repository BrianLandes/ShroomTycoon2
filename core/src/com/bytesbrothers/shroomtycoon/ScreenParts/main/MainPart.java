package com.bytesbrothers.shroomtycoon.ScreenParts.main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ActionBarPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.CasingInfoPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.CombineStrainsPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.DehydratorsPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.EnterCodePart;
import com.bytesbrothers.shroomtycoon.ScreenParts.G2gPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.InoculationPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.JarPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.MakeCasingPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.OptionsPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.PlayerPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.PressureCookersPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.StrainPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.SyringeStorePart;
import com.bytesbrothers.shroomtycoon.ScreenParts.SyringesOwnedPart;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.CasingButton;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitCheckButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.JarButton;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Casing;
import com.bytesbrothers.shroomtycoon.structures.Fruit;
import com.bytesbrothers.shroomtycoon.structures.Jar;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class MainPart extends ScreenPart {

	private ScrollPane pane;
	private Tree tree;
	private float treeWidth = 1;

	private ArrayList<JarButton> jarButtons = new ArrayList<JarButton>();
	private ArrayList<CasingButton> casingButtons = new ArrayList<CasingButton>();
	private Node labNode;
	private Node incNode;
	private Node incSettingsNode;
	private Node incThermNode;
	private Node incCapNode;
	private Table insideIncTable;
	private Table insideFcTable;
	private Table insideFridgeTable;
	private Node insideIncNode;
	private Node fcNode;
	private Node fcSettingsNode;
	private Node fcThermNode;
	private Node fcCapNode;
	private ProgressBar humidityBar;
//	private Table fcHumidBarTable;
	private Node fcHumidBarNode;
	private Node fcHumidNode;
	private Node insideFcNode;
	private Node fridgeNode;
	private Node fridgeSettingsNode;
	private Node fridgeCapNode;
	private Node insideFridgeNode;
	private Node gloveBoxNode;
	private Node gloveBoxSettingsNode;
	private ProgressBar airPurity;
//	private Table airPurityTable;
	private ProgressBar filterLeft;
	private Table filterLeftTable;
	private Node resourcesNode;
	private Node cashNode;
	private Node syringesNode;
	protected int cheatScreenTaps;
	protected float cheatScreenDelay;
	private Node substrateNode;
	private Node dealersNode;
	private DealersTable dealersListTable;
	private Node samNode;
	private ProgressBar samRemainingBar;
	private ProgressBar samBonusBar;
//	private Table samRemainingTable;
//	private Table samBonusTable;
	private FitCheckButton samBackground;
	private Table samBackgroundTable;

	private float lastScrollY;

	private Node pcNode;

	private Node dehydNode;

	
	/*
	 * Main Column Quick-Open constants
	 */
	public static final int NONE = -1;
	public static final int MAIN = 0;
	public static final int INC = 1;
	public static final int INC_SETTINGS = 2;
	public static final int FC = 3;
	public static final int FC_SETTINGS = 4;
	public static final int FRIDGE = 5;
	public static final int FRIDGE_SETTINGS = 6;
	public static final int GLOVE_BOX = 7;
	public static final int GLOVE_BOX_SETTINGS = 8;
	public static final int PRESSURE_COOKERS = 9;
	public static final int DEHYDRATORS = 10;
	public static final int RESOURCES = 11;
	public static final int CASH = 12;
//	public static final int STASH = 13;
//	public static final int COINS = 14;
	public static final int SYRINGES = 15;
//	public static final int GRAIN_JARS = 16;
	public static final int SUB_JARS = 17;
	public static final int DEALERS = 18;
	public static final int SAM = 19;
//	public static final int Assets.player = 20;
	
	public MainPart() {
		super( "Main" );
	}

	@Override
	public void populate() {
		clear();

		tree = new Tree( skin, "small" );
		
		tree.setIconSpacing( 0, 0 );
		treeWidth = tree.getStyle().plus.getMinWidth();
		
		ScreenButton button = new ScreenButton( "Laboratory", "LabIcon", "topLevel", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		labNode = new Node( button.table );
		buttons.add( button );
		tree.add( labNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				labNode.setExpanded( !labNode.isExpanded() );
 				focusNode( labNode );
 			}
 		});
		
		button = new ScreenButton( "Incubator", "IncIcon", "laboratory", skin ) {
			int maxCapacity = 0;
			int capacity = 0 ;
			@Override
			public void act( float delta ) {
				int num = (Assets.player.numCasings( ST.INCUBATOR ) + Assets.player.numJars( ST.INCUBATOR ));
				if ( maxCapacity!= Assets.player.inc_capacity || capacity!= num ) {
					maxCapacity = Assets.player.inc_capacity;
					capacity = num;
					fitButton.setText( "Incubator (" + capacity + "/" + maxCapacity + ")" );
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		incNode = new Node( button.table );
		buttons.add( button );
		labNode.add( incNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				incNode.setExpanded( !incNode.isExpanded() );
 				focusNode( incNode );
 			}
 		});
		
		button = new ScreenButton( "Settings", "OptionsIcon", "topLevel", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		incSettingsNode = new Node( button.table );
		buttons.add( button );
		incNode.add( incSettingsNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				incSettingsNode.setExpanded( !incSettingsNode.isExpanded() );
 				focusNode( incSettingsNode );
 			}
 		});
		
		button = new ScreenButton( "Upgrade Thermostat [" + ST.temps[ Assets.player.inc_temp ] + "]", "CashIcon", "action", skin ) {
			int oldTemp = Assets.player.inc_temp;
			@Override
			public void act( float delta ) {
				fitButton.setDisabled( Assets.player.inc_temp==ST.HIGH );
				if ( oldTemp!= Assets.player.inc_temp) {
					oldTemp = Assets.player.inc_temp;
					fitButton.setText( "Upgrade Thermostat [" + ST.temps[ Assets.player.inc_temp ] + "]" );
				}
				
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		incThermNode = new Node( button.table );
		buttons.add( button );
		incSettingsNode.add( incThermNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to upgrade to the " + ST.temps[ Assets.player.inc_temp + 1 ] + 
 						" thermostat for $" + ST.withComas(ST.INC_UPGRADE_PRICES[ Assets.player.inc_temp + 1 ]) + "?",
 						new ChangeListener() {
 		 		 			public void changed (ChangeEvent event, Actor actor) {
 		 		 				if ( Assets.player.getCash()>= ST.INC_UPGRADE_PRICES[ Assets.player.inc_temp + 1 ]  ) {
 		 		 					Assets.player.changeCash( -ST.INC_UPGRADE_PRICES[ Assets.player.inc_temp + 1 ] );
 		 		 					Assets.player.inc_temp += 1;
 		 		 					Dialogs.sweetDialog( "You purchased the " + ST.temps[ Assets.player.inc_temp ] + 
 			 		 						" thermostat!", new ChangeListener() {
 				 				 			public void changed (ChangeEvent event, Actor actor) {
// 				 				 				button.fitButton.setText( "Upgrade Thermostat [" + ST.temps[ Assets.player.inc_temp ] + "]" );
// 				 				 				button.fitButton.setDisabled( Assets.player.inc_temp==ST.HIGH );
 				 				 			}
 			 				 			}
 			 		 				);
 			 		 				// if the Assets.player has an objective
 			 		 				if ( Assets.player.objective!=null && 
 			 		 						Assets.player.objective.type==Assets.player.objective.UPGRADE &&
 			 		 						Assets.player.objective.text.equals("Inc Temp") ) {
 			 		 					Dialogs.objectiveDialog( "You successfully upgraded the thermostat in the incubator!" );
 			 							Assets.player.objective = null;
 			 							if ( Assets.player.objective_alert != null ) {
 			 								Assets.removeAlert( Assets.player.objective_alert );
 			 								Assets.player.objective_alert = null;
 			 							}
 			 							Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Upgrade Inc Temp", (long)0 );
 			 		 				}
 			 		 				Assets.getResolver().trackerSendEvent( "Upgrade", "Incubator", "Temp: " + ST.temps[ Assets.player.inc_temp ], (long)0 );
 		 		 				} else {
 		 		 					Dialogs.acceptDialog( "You do not have enough cash for this purchase" );
 		 		 				}
 		 		 				
 		 		 			}
 						}
 					);
 			}
 		});
		
		button = new ScreenButton( "Upgrade Capacity", "CoinIcon", "useCoin", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		incCapNode = new Node( button.table );
		buttons.add( button );
		incSettingsNode.add( incCapNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to increase the incubator capacity by 1 for " + Assets.player.getIncCapacityIncreaseCost() +
 						" Blue Coins?",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.coins>= Assets.player.getIncCapacityIncreaseCost()  ) {
	 		 					Assets.player.changeCoins( -Assets.player.getIncCapacityIncreaseCost() );
	 		 					Assets.player.inc_capacity += 1;
	 		 					Dialogs.sweetDialog( "You increased the incubator capacity to " + Assets.player.inc_capacity, new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) { }
		 				 			}
		 		 				);
		 		 				Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Incubator Capacity", (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + Assets.player.getIncCapacityIncreaseCost() + "." );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		insideIncTable = new Table();
		insideIncNode = new Node( insideIncTable );
		incNode.add( insideIncNode );
		
		button = new ScreenButton( "Fruiting Chamber", "FcIcon", "laboratory", skin ) {
			int maxCapacity = 0;
			int capacity = 0 ;
			@Override
			public void act( float delta ) {
				int num = (Assets.player.numCasings( ST.FC ) + Assets.player.numJars( ST.FC ));
				if ( maxCapacity!= Assets.player.fc_capacity || capacity!= num ) {
					maxCapacity = Assets.player.fc_capacity;
					capacity = num;
					fitButton.setText( "Fruiting Chamber (" + capacity + "/" + maxCapacity + ")" );
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fcNode = new Node( button.table );
		buttons.add( button );
		labNode.add( fcNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				fcNode.setExpanded( !fcNode.isExpanded() );
 				focusNode( fcNode );
 			}
 		});
		
		button = new ScreenButton( "Settings", "OptionsIcon", "topLevel", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fcSettingsNode = new Node( button.table );
		buttons.add( button );
		fcNode.add( fcSettingsNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				fcSettingsNode.setExpanded( !fcSettingsNode.isExpanded() );
 				focusNode( fcSettingsNode );
 			}
 		});
		
		button = new ScreenButton( "Upgrade Thermostat [" + ST.temps[ Assets.player.fc_temp ] + "]", "CashIcon", "action", skin ) {
			int oldTemp = Assets.player.fc_temp;
			@Override
			public void act( float delta ) {
				fitButton.setDisabled( Assets.player.fc_temp==ST.HIGH );
				if ( oldTemp!= Assets.player.fc_temp) {
					oldTemp = Assets.player.fc_temp;
					fitButton.setText( "Upgrade Thermostat [" + ST.temps[ Assets.player.fc_temp ] + "]" );
				}
				
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fcThermNode = new Node( button.table );
		buttons.add( button );
		fcSettingsNode.add( fcThermNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to upgrade to the " + ST.temps[ Assets.player.fc_temp + 1 ] + 
 						" thermostat for $" + ST.withComas(ST.FC_UPGRADE_PRICES[ Assets.player.fc_temp + 1 ]) + "?",
 						new ChangeListener() {
 		 		 			public void changed (ChangeEvent event, Actor actor) {
 		 		 				if ( Assets.player.getCash()>= ST.FC_UPGRADE_PRICES[ Assets.player.fc_temp + 1 ]  ) {
 		 		 					Assets.player.changeCash( -ST.FC_UPGRADE_PRICES[ Assets.player.fc_temp + 1 ] );
 		 		 					Assets.player.fc_temp += 1;
 		 		 					Dialogs.sweetDialog( "You purchased the " + ST.temps[ Assets.player.fc_temp ] + 
 			 		 						" thermostat!", new ChangeListener() {
 				 				 			public void changed (ChangeEvent event, Actor actor) { }
 			 				 			}
 			 		 				);
 			 		 				// if the Assets.player has an objective
 			 		 				if ( Assets.player.objective!=null && 
 			 		 						Assets.player.objective.type==Assets.player.objective.UPGRADE &&
 			 		 						Assets.player.objective.text.equals("Fc Temp") ) {
 			 		 					Dialogs.objectiveDialog( "You successfully upgraded the thermostat in the fruiting chamber!" );
 			 							Assets.player.objective = null;
 			 							if ( Assets.player.objective_alert != null ) {
 			 								Assets.removeAlert( Assets.player.objective_alert );
 			 								Assets.player.objective_alert = null;
 			 							}
 			 							Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Upgrade Inc Temp", (long)0 );
 			 		 				}
 			 		 				Assets.getResolver().trackerSendEvent( "Upgrade", "Fruiting Chamber", "Temp: " + ST.temps[ Assets.player.fc_temp ], (long)0 );
 		 		 				} else {
 		 		 					Dialogs.acceptDialog( "You do not have enough cash for this purchase" );
 		 		 				}
 		 		 				
 		 		 			}
 						}
 					);
 			}
 		});
		
		button = new ScreenButton( "Upgrade Capacity", "CoinIcon", "useCoin", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fcCapNode = new Node( button.table );
		buttons.add( button );
		fcSettingsNode.add( fcCapNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to increase the fruiting chamber capacity by 1 for " + Assets.player.getFcCapacityIncreaseCost() + 
 						" Blue Coins?",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.coins>= Assets.player.getFcCapacityIncreaseCost()  ) {
	 		 					Assets.player.changeCoins( -Assets.player.getFcCapacityIncreaseCost() );
	 		 					Assets.player.fc_capacity += 1;
	 		 					Dialogs.sweetDialog( "You increased the fruiting chamber capacity to " + Assets.player.fc_capacity, new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) { }
		 				 			}
		 		 				);
		 		 				Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "FC Capacity", (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + Assets.player.getFcCapacityIncreaseCost() + "." );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		humidityBar = new ProgressBar( Assets.player.getHumidity(), skin );
		humidityBar.barColor = skin.getColor( "white" );
		fcHumidBarNode = new Node( humidityBar.table() );
		fcSettingsNode.add( fcHumidBarNode );
		
		button = new ScreenButton( "Upgrade Humidifier", "CashIcon", "action", skin ) {
			@Override
			public void act( float delta ) {
				fitButton.setDisabled( Assets.player.fc_humidity>=4 );
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fcHumidNode = new Node( button.table );
		buttons.add( button );
		fcSettingsNode.add( fcHumidNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to upgrade the humidifier for $" + ST.withComas(ST.FC_HUMIDITY_PRICES[ Assets.player.fc_humidity + 1 ]) + "?",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.getCash()>= ST.FC_HUMIDITY_PRICES[ Assets.player.fc_humidity + 1 ]  ) {
	 		 					Assets.player.changeCash( -ST.FC_HUMIDITY_PRICES[ Assets.player.fc_humidity + 1 ] );
	 		 					Assets.player.fc_humidity += 1;
	 		 					Dialogs.sweetDialog( "You purchased a better humidifier! It'll keep the relative humidity at %" + 
		 		 						ST.humidities[Assets.player.fc_humidity], new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) { }
		 				 			}
		 		 				);
		 		 				// if the Assets.player has an objective
		 		 				if ( Assets.player.objective!=null && 
		 		 						Assets.player.objective.type==Assets.player.objective.UPGRADE &&
		 		 						Assets.player.objective.text.equals("Fc Humidity") ) {
		 		 					Dialogs.objectiveDialog( "You successfully upgraded the humidifier in the fruiting chamber!" );
		 							Assets.player.objective = null;
		 							if ( Assets.player.objective_alert != null ) {
		 								Assets.removeAlert( Assets.player.objective_alert );
		 								Assets.player.objective_alert = null;
		 							}
		 							Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Upgrade FC Humidity", (long)0 );
		 		 				}
		 		 				Assets.getResolver().trackerSendEvent( "Upgrade", "Fruiting Chamber", "Humidity: " + ST.humidities[Assets.player.fc_humidity], (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialog( "You do not have enough cash for this purchase" );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		insideFcTable = new Table();
		insideFcNode = new Node( insideFcTable );
		fcNode.add( insideFcNode );
		
		
		// -----------FRIDGE----------------------
		button = new ScreenButton( "Fridge", "FridgeIcon", "laboratory", skin ) {
			int maxCapacity = 0;
			int capacity = 0 ;
			@Override
			public void act( float delta ) {
				int num = (Assets.player.numCasings( ST.FRIDGE ) + Assets.player.numJars( ST.FRIDGE ));
				if ( maxCapacity!= Assets.player.fridge_capacity || capacity!= num ) {
					maxCapacity = Assets.player.fridge_capacity;
					capacity = num;
					fitButton.setText( "Fridge (" + capacity + "/" + maxCapacity + ")" );
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fridgeNode = new Node( button.table );
		buttons.add( button );
		labNode.add( fridgeNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				fridgeNode.setExpanded( !fridgeNode.isExpanded() );
 				focusNode( fridgeNode );
 			}
 		});
		
		button = new ScreenButton( "Settings", "OptionsIcon", "topLevel", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fridgeSettingsNode = new Node( button.table );
		buttons.add( button );
		fridgeNode.add( fridgeSettingsNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				fridgeSettingsNode.setExpanded( !fridgeSettingsNode.isExpanded() );
 				focusNode( fridgeSettingsNode );
 			}
 		});
		
		button = new ScreenButton( "Upgrade Capacity", "CoinIcon", "useCoin", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		fridgeCapNode = new Node( button.table );
		buttons.add( button );
		fridgeSettingsNode.add( fridgeCapNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to increase the fridge capacity by 1 for " + Assets.player.getFridgeCapacityIncreaseCost() + 
 						" Blue Coins?",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.coins>= Assets.player.getFridgeCapacityIncreaseCost()  ) {
	 		 					Assets.player.changeCoins( -Assets.player.getFridgeCapacityIncreaseCost() );
	 		 					Assets.player.fridge_capacity += 1;
	 		 					Dialogs.sweetDialog( "You increased the incubator capacity to " + Assets.player.fridge_capacity, new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) { }
		 				 			}
		 		 				);
		 		 				Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Fridge Capacity", (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + Assets.player.getFridgeCapacityIncreaseCost() + "." );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		insideFridgeTable = new Table();
		insideFridgeNode = new Node( insideFridgeTable );
		fridgeNode.add( insideFridgeNode );
		
		populateJarsAndCasings();
		
		button = new ScreenButton( "Glove Box", "GbIcon", "laboratory", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		gloveBoxNode = new Node( button.table );
		buttons.add( button );
		labNode.add( gloveBoxNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				gloveBoxNode.setExpanded( !gloveBoxNode.isExpanded() );
 				focusNode( gloveBoxNode );
 			}
 		});
		
		button = new ScreenButton( "Settings", "OptionsIcon", "topLevel", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		gloveBoxSettingsNode = new Node( button.table );
		buttons.add( button );
		gloveBoxNode.add( gloveBoxSettingsNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Alert alert = Assets.hasAlertWithID( ST.ALERT_GLOVE_BOX );
 				if ( alert!=null )
 					Assets.removeAlert( alert );
 				gloveBoxSettingsNode.setExpanded( !gloveBoxSettingsNode.isExpanded() );
 				focusNode( gloveBoxSettingsNode );
 			}
 		});
		
		airPurity = new ProgressBar( new AirPurityBar(), skin );
		airPurity.barColor = skin.getColor( "white" );
		gloveBoxSettingsNode.add( new Node( airPurity.table() ) );
		
		filterLeft = new ProgressBar( new FilterLeftBar(), skin );
		filterLeft.barColor = skin.getColor( "white" );
		filterLeftTable = new Table();
		filterLeftTable.add( filterLeft );
		gloveBoxSettingsNode.add( new Node( filterLeftTable ) );
		
		button = new ScreenButton( "Buy Cheap Air Filter $180", "CashIcon", "action", skin ) {
			@Override
			public void act( float delta ) {
				fitButton.setDisabled( Assets.player.permanent_sterile );
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		gloveBoxSettingsNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to buy a new air filter for $180? This filter will last 5 minutes.",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.getCash()>= 180 ) {
	 		 					Assets.player.changeCash( -180 );
	 		 					Assets.player.last_gb_sterile_secs = 5*60;
	 		 					Assets.player.gb_sterile_secs = 5*60;
	 		 					Dialogs.sweetDialog( "You replaced the air filter in the glove box. This will guarantee that your jars and casings will be contamination-free for five minutes.", new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) { }
		 				 			}
		 		 				);
	 		 					Assets.getResolver().trackerSendEvent( "Glove Box", "Used", "Cheap Air Filter", (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialog( "You don't have enough cash for this. You need at least $" + 180 + "." );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		button = new ScreenButton( "Buy Air Filter 5 BC", "CoinIcon", "useCoin", skin ) {
			@Override
			public void act( float delta ) {
				fitButton.setDisabled( Assets.player.permanent_sterile );
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		gloveBoxSettingsNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "Would you like to buy a new air filter for 5 Blue Coins? This filter will last 120 minutes.",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.coins>= 5 ) {
	 		 					Assets.player.changeCoins( -5 );
	 		 					Assets.player.last_gb_sterile_secs = 120*60;
	 		 					Assets.player.gb_sterile_secs = 120*60;
	 		 					Dialogs.sweetDialog( "You replaced the air filter in the glove box. This will guarantee that your jars and casings will be contamination-free for two hours.", new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) { }
		 				 			}
		 		 				);
	 		 					Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Good Air Filter", (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + 5 + "." );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		button = new ScreenButton( "Buy Washable Air Filter 30 BC", "CoinIcon", "useCoin", skin ) {
			@Override
			public void act( float delta ) {
				fitButton.setDisabled( Assets.player.permanent_sterile );
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*4.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		gloveBoxSettingsNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.choiceDialog( "The 'PureAir' v2.0 ionic air purifier with its true HEPA, permanent, washable air filter is guaranteed effective for two lifetimes. No more swapping out new air filters. " +
 						"Would you like to buy this air filter for 30 Blue Coins?",
					new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				if ( Assets.player.coins>= 30 ) {
	 		 					Assets.player.changeCoins( -30 );
	 		 					Assets.player.permanent_sterile = true;
	 		 					Dialogs.sweetDialog( "You purchased the 'PureAir' air purifier. This will guarantee that your jars and casings will be contamination-free forever.", new ChangeListener() {
			 				 			public void changed (ChangeEvent event, Actor actor) { }
		 				 			}
		 		 				);
	 		 					Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Permanent Air Filter", (long)0 );
	 		 				} else {
	 		 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + 30 + "." );
	 		 				}
	 		 				
	 		 			}
					}
				);
 			}
 		});
		
		button = new ScreenButton( "Inoculation", "InnoculationIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		gloveBoxNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				InoculationPart screen = Assets.getScreenHandler().getScreenPart( InoculationPart.class );
// 				InoculationPart screen = (InoculationPart)Assets.game.getScreenPart( "Inoculation" );
 				screen.setSyringe( null );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
		button = new ScreenButton( "Make Casing", "Jar2CasingIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		gloveBoxNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				MakeCasingPart screen = Assets.getScreenHandler().getScreenPart( MakeCasingPart.class );
// 				MakeCasingPart screen = (MakeCasingPart)Assets.game.getScreenPart( "Make Casing" );
 				screen.setJar( null );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
		button = new ScreenButton( "G2G Transfer", "G2gIcon", "newColumn", skin ) {
			@Override
			public void act( float delta) {
				fitButton.setDisabled( !Assets.player.canG2g3() );
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		gloveBoxNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				G2gPart screen = Assets.getScreenHandler().getScreenPart( G2gPart.class );
// 				G2gPart screen = (G2gPart)Assets.game.getScreenPart( "G2g" );
 				screen.setJar( null );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
		button = new ScreenButton( "Combine Strains", "CustomStrainIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		gloveBoxNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				CombineStrainsPart screen = Assets.getScreenHandler().getScreenPart( CombineStrainsPart.class );
// 				CombineStrainsPart screen = (CombineStrainsPart)Assets.game.getScreenPart( "Combine Strains" );
 				screen.setSyringe( null );
 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
// ---------  PRESSURE COOKERS ------------------------------------------------
		button = new ScreenButton( "Pressure Cookers", "PcIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		pcNode = new Node( button.table );
		labNode.add( pcNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				PressureCookersPart screen = Assets.getScreenHandler().getScreenPart( PressureCookersPart.class );
 				screen.populated = false;
// 				ScreenPart screen = Assets.game.getScreenPart( "Pressure Cookers" );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
// ---------- DEHYDRATORS -----------------------------------------------
		button = new ScreenButton( "Dehydrators", "DhyIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		dehydNode = new Node( button.table );
		labNode.add( dehydNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				DehydratorsPart screen = Assets.getScreenHandler().getScreenPart( DehydratorsPart.class );
// 				ScreenPart screen = Assets.game.getScreenPart( "Dehydrators" );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
// --------------RESOURCES ------------------------------------------------
		button = new ScreenButton( "Resources", "ResourcesIcon", "topLevel", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		resourcesNode = new Node( button.table );
		buttons.add( button );
		tree.add( resourcesNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				resourcesNode.setExpanded( !resourcesNode.isExpanded() );
 				focusNode( resourcesNode );
 			}
 		});
		
		button = new ScreenButton( "Cash", "CashIcon", "resource", skin ) {
			BigInteger lastCash = BigInteger.ZERO;
			
			@Override
			public void act( float delta ) {
				if( Assets.player.getCash() >=2000000000 ) {
					fitButton.setText( "Cash: >$2B" );
				} else if ( !lastCash.equals( Assets.player.cash ) ) {
					fitButton.setText( "Cash: $" + Assets.player.getCashString() );
					fitButton.resizeText();
					lastCash = new BigInteger(  Assets.player.cash.toString() );
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		cashNode = new Node( button.table );
		buttons.add( button );
		resourcesNode.add( cashNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				cashNode.setExpanded( !cashNode.isExpanded() );
 				focusNode( cashNode );
 			}
 		});
		
		button = new ScreenButton( "Buy $1,000 for 5 BC", "CoinIcon", "useCoin", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		cashNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( Assets.player.coins<5 ) {
 					Dialogs.acceptDialogGetMoreCoins( "You do not have enough coins for this" );
 				} else {
 					Dialogs.choiceDialog( "Would you like to spend 5 Blue Coins for $1,000?",
						new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				Assets.player.changeCoins( -5 );
		 		 				Assets.player.changeCash( 1000 );
		 		 				Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "$1,000", (long)0 );
		 		 			}
	 					}
 					);
 				}
 			}
 		});
		
		button = new ScreenButton( "Stash", "StashIcon", "newColumn", skin ) {
			float lastStash = 0;
			@Override
			public void act( float delta ) {
				float newStash = Assets.player.getStash();
				if ( lastStash!=newStash ) {
					fitButton.setText( "Stash: " + ST.withComasAndTwoDecimals( Assets.player.getStash() ) + " g" );
					fitButton.resizeText();
					lastStash = newStash;
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		resourcesNode.add( new Node( button.table ) );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				StashPart screen = Assets.getScreenHandler().getScreenPart( StashPart.class );
// 				ScreenPart screen = Assets.game.getScreenPart( "Stash" );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
		button = new ScreenButton( "Coins", "CoinIcon", "resource", skin ) {
			int lastCoins = 0;
			
			@Override
			public void act( float delta ) {
				if ( lastCoins!=Assets.player.coins ) {
					lastCoins = Assets.player.coins;
					fitButton.setText( "Coins: " + ST.withComas( Assets.player.coins ) + " BC" );
					fitButton.resizeText();
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		resourcesNode.add( new Node( button.table ) );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				Dialogs.buyCoinsDialog();
 			}
 		});
		
		button = new ScreenButton( "Syringes", "SyringesIcon", "resource", skin ) {
			
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		syringesNode = new Node( button.table );
		resourcesNode.add( syringesNode );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				syringesNode.setExpanded( !syringesNode.isExpanded() );
 				focusNode( syringesNode );
 			}
 		});
		
		button = new ScreenButton( "Owned", "SyringesIcon", "newColumn", skin ) {
			int lastSyringes = 0;
			
			@Override
			public void act( float delta ) {
				int newSyringes = Assets.player.getNumSyringes();
				if ( lastSyringes!=newSyringes ) {
					lastSyringes = newSyringes;
					fitButton.setText( "Owned: " + ST.withComas( newSyringes ) );
					fitButton.resizeText();
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		syringesNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				SyringesOwnedPart screen = Assets.getScreenHandler().getScreenPart( SyringesOwnedPart.class );
// 				ScreenPart screen = Assets.game.getScreenPart( "Syringes Owned" );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
		button = new ScreenButton( "Syringe Store", "SyringesIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*3.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		syringesNode.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				SyringeStorePart screen = Assets.getScreenHandler().getScreenPart( SyringeStorePart.class );
// 				ScreenPart screen = Assets.game.getScreenPart( "Syringe Store" );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
 		});
		
		button = new ScreenButton( "Grain Jars", "GrainJarIcon", "resource", skin ) {
			int lastJars = 0;
			@Override
			public void act( float delta ) {
				if ( lastJars!=Assets.player.grain_jars ) {
					lastJars = Assets.player.grain_jars;
					fitButton.setText( "Grain Jars: " + ST.withComas( Assets.player.grain_jars ) );
					fitButton.resizeText();
				}
				if ( cheatScreenDelay>0.0f ) {
					cheatScreenDelay -= delta;
				} else
					cheatScreenTaps = 0;
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		resourcesNode.add( new Node( button.table ) );
		button.fitButton.addListener( new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
 				cheatScreenTaps += 1;
 				cheatScreenDelay = 2.0f;
 				if ( cheatScreenTaps>=10 ) {
 					EnterCodePart screen = Assets.getScreenHandler().getScreenPart( EnterCodePart.class );
// 					ScreenPart screen = Assets.game.getScreenPart( "Enter Code" );

 	 				screen.setDx( 0 );
 	 				setDx( -1f );
 	 				
 	 				Assets.getScreenHandler().setActivePart( screen );

 	 				ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
 	 				actionBarPart.setDy( -Assets.game.actionBarHeight );
 				}
 			}
 		});
		
		button = new ScreenButton( "Substrate Jars: " + ST.withComas( Assets.player.getSubstrateJars() ), "SubJarIcon", "resource", skin ) {
			int lastSubs = 0;
			@Override
			public void act( float delta ) {
				int newSubs = Assets.player.getSubstrateJars();
				if ( lastSubs!=newSubs ) {
					lastSubs = newSubs;
					fitButton.setText( "Substrate Jars: " + ST.withComas( newSubs ) );
					fitButton.resizeText();
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		substrateNode = new Node( button.table );
		resourcesNode.add( substrateNode );
		button.fitButton.addListener( new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				substrateNode.setExpanded( !substrateNode.isExpanded() );
 				focusNode( substrateNode );
 			}
 		});
		
		for ( int i = 0; i < 4; i ++ ) {
			final int j = i;
			ScreenLabel label = new ScreenLabel( new FitLabel( ST.substrates[i] + ": " + ST.withComas( Assets.player.sub_jars[i] ), skin ) ) {
				int num = -1;
				int g = j;
				@Override
				public void act( float delta ) {
					int newNum = Assets.player.sub_jars[g];
					if ( num!=newNum  ) {
						num = newNum;
						label.setText( ST.substrates[g] + ": " + ST.withComas( newNum ) );
//						System.out.println( ST.substrates[g] + ": " + ST.withComas( newNum ) + " <-> " + newNum );
					}
				}
				@Override
				public void resize( int buttonWidth, int buttonHeight ) {
					buttonWidth -= treeWidth*3.0f;
					super.resize( buttonWidth, buttonHeight );
				}
			};
			label.colorName = "resource";
			label.scaleY = 0.9f;
			label.label.setAlignment( Align.center );
			label.table = new Table();
			label.table.add( label.label );
			labels.add( label );
			substrateNode.add( new Node( label.table ) );
		}
		
//--------------DEALERS-----------------------------------
		button = new ScreenButton( "Dealers (" + Assets.player.dealers.size() + "/" + Assets.player.getMaxDealers() + ")", "DealersIcon", "topLevel", skin ) {
			int lastSize = Assets.player.dealers.size();
			@Override
			public void act( float delta ) {
				int newSize = Assets.player.dealers.size();
				if ( newSize!=lastSize ) {
					lastSize = newSize;
					fitButton.setText( "Dealers (" + Assets.player.dealers.size() + "/" + Assets.player.getMaxDealers() + ")" );
				}
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		dealersNode = new Node( button.table );
		buttons.add( button );
		tree.add( dealersNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				dealersNode.setExpanded( !dealersNode.isExpanded() );
 				focusNode( dealersNode );
 			}
 		});
		
		dealersListTable = new DealersTable( Assets.game );
		dealersNode.add( new Node( dealersListTable ) );
		
//---------SAM ----------------------------------------
		button = new ScreenButton( "Sam", "SamIcon", "topLevel", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		samNode = new Node( button.table );
		buttons.add( button );
		tree.add( samNode );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				samNode.setExpanded( !samNode.isExpanded() );
 				focusNode( samNode );
 			}
 		});
		
		samRemainingBar = new ProgressBar( new RemainingDebt(), skin );
		samNode.add( new Node( samRemainingBar.table() ) );
		
		samBonusBar = new ProgressBar( new NextBonus(), skin );
		samNode.add( new Node( samBonusBar.table() ) );
		
		button = new ScreenButton( "Pay Sam $" + ST.SAMS_BONUS_PRICE, "CashIcon", "action", skin ) {
			@Override
			public void act( float delta ) {
				fitButton.setDisabled( Assets.player.debt<=0 );
			}
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth*2.0f;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		samNode.add( new Node( button.table ) );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				if ( Assets.player.getCash()>ST.SAMS_BONUS_PRICE ) {
	 				if ( Assets.player.samBonusTimer<=0 ) {
	 					// bonus IS available
	 					Alert alert = Assets.hasAlertWithID( ST.ALERT_SAM_BONUS );
	 					if ( alert!=null )
	 						Assets.removeAlert( alert );
	 					makePayment();
	 				} else {
	 					Dialogs.choiceDialog( "Sam has no bonus for you yet, are you sure you want to pay $" + ST.SAMS_BONUS_PRICE, new ChangeListener() {
							public void changed (ChangeEvent event, Actor actor) {
								makePayment();
							}
	 					} );
	 				}
				} else {
					Dialogs.acceptDialog( "You do not have enough cash." );
				}
 			}
 		});
		
		samBackground = null;
		if ( Assets.player.debt<=0 ) {
			samBackground = new FitCheckButton( "Use Sam's Background", skin );
			samBackground.setChecked( Assets.player.useSamsBackground );
			samBackgroundTable = new Table();
			samBackgroundTable.add( samBackground );
			samNode.add( new Node( samBackgroundTable ) );
			samBackground.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Assets.player.useSamsBackground = !Assets.player.useSamsBackground;
	 				samBackground.setChecked( Assets.player.useSamsBackground );
	 				if ( Assets.player.useSamsBackground )
	 					Assets.getResolver().trackerSendEvent( "Sam", "Use", "Background", (long)0 );
	 				else
	 					Assets.getResolver().trackerSendEvent( "Sam", "Do Not Use", "Background", (long)0 );
	 			}
	 		});
		}
		
////-----------STRAIN SERVER--------------------------------
//		button = new ScreenButton( "Strains Online", "StrainsOnlineIcon", "newColumn", skin ) {
//			@Override
//			public void resize( int buttonWidth, int buttonHeight ) {
//				buttonWidth -= treeWidth;
//				super.resize( buttonWidth, buttonHeight );
//			}
//		};
//		button.table = new Table();
//		button.table.add( button.fitButton );
//		buttons.add( button );
//		tree.add( new Node( button.table ) );
//
//		button.fitButton.addListener( new ChangeListener() {
// 			public void changed (ChangeEvent event, Actor actor) {
// 				SyringesServerPart screen = Assets.getScreenHandler().getScreenPart( SyringesServerPart.class );
//// 				ScreenPart screen = Assets.game.getScreenPart( "Syringes Server" );
//
// 				screen.setDx( 0 );
// 				setDx( -1f );
//
// 				Assets.getScreenHandler().setActivePart( screen );
// 			}
// 		});
		
//---------Player----------------------------------------
		button = new ScreenButton( "Player", "PlayerIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		tree.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				PlayerPart screen = Assets.getScreenHandler().getScreenPart( PlayerPart.class );
 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
		});
		
//---------OPTIONS------------------------
		button = new ScreenButton( "Options", "OptionsIcon", "newColumn", skin ) {
			@Override
			public void resize( int buttonWidth, int buttonHeight ) {
				buttonWidth -= treeWidth;
				super.resize( buttonWidth, buttonHeight );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		buttons.add( button );
		tree.add( new Node( button.table ) );
		
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				OptionsPart screen = Assets.getScreenHandler().getScreenPart( OptionsPart.class );
 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 			}
		});
		
		if ( pane!=null )
			lastScrollY = pane.getScrollY();
		pane = new ScrollPane( tree, skin);
		add( pane );
		pane.validate();
		pane.setScrollY( lastScrollY );
		pane.updateVisualScroll();
		
	}
	
	public void populateJarsAndCasings() {
		insideIncTable.clear();
		insideFcTable.clear();
		insideFridgeTable.clear();
		jarButtons.clear();
		casingButtons.clear();
		
		ArrayList<Jar> jars = Assets.player.jars;
		for ( final Jar jar: jars ) {
			JarButton newButton = new JarButton( jar, skin );
			jarButtons.add( newButton );
			newButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
//	 				ShroomClass game = Assets.game;
	 				JarPart jarPart = Assets.getScreenHandler().getScreenPart( JarPart.class );
//	 				JarPart jarPart = (JarPart) game.getScreenPart( "Jar" );
	 				jarPart.setJar( jar );
	 				jarPart.setDx( 0 );
	 				setDx( -1f );
	 				Assets.getScreenHandler().setActivePart( jarPart );
	 			}
	 		});
			switch( jar.location ) {
			case ST.INCUBATOR:
				insideIncTable.add( newButton );
				insideIncTable.row();
				break;
			case ST.FC:
				insideFcTable.add( newButton );
				insideFcTable.row();
				break;
			case ST.FRIDGE:
				insideFridgeTable.add( newButton );
				insideFridgeTable.row();
				break;
			}
			
		}
		
		ArrayList<Casing> casings = Assets.player.casings;
		for ( final Casing casing: casings ) {
			CasingButton newButton = new CasingButton( casing, skin );
			casingButtons.add( newButton );
			newButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
//	 				ShroomClass game = Assets.game;
//	 				CasingInfoPart casingPart = (CasingInfoPart) game.getScreenPart( "CasingInfo" );
	 				CasingInfoPart casingPart = Assets.getScreenHandler().getScreenPart( CasingInfoPart.class );

					casingPart.setCasing( casing );
	 				casingPart.setDx( 0 );
	 				setDx( -1f );
	 				Assets.getScreenHandler().setActivePart( casingPart );
	 				
	 			}
	 		});
			switch( casing.location ) {
			case ST.INCUBATOR:
				insideIncTable.add( newButton );
				insideIncTable.row();
				break;
			case ST.FC:
				insideFcTable.add( newButton );
				insideFcTable.row();
				break;
			case ST.FRIDGE:
				insideFridgeTable.add( newButton );
				insideFridgeTable.row();
				break;
			}
		}
	}
	
	@Override
	protected void resize( ) {
		
		super.resize( );
		
		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
		
		tree.setStyle( skin.get( "small", TreeStyle.class ) );
		treeWidth = tree.getStyle().plus.getMinWidth();
		
		humidityBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
		humidityBar.setSize( buttonWidth - treeWidth*4.0f , buttonHeight );
//		fcHumidBarTable.getCell( humidityBar )
//			.width( buttonWidth - treeWidth*4.0f )
//			.height( buttonHeight );
		
		populateJarsAndCasings();
		
		for ( JarButton button: jarButtons ) {
			button.resize( buttonWidth - treeWidth*3.0f, buttonHeight * 2.0f );
		}
		
		for ( CasingButton button: casingButtons ) {
			button.resize( buttonWidth - treeWidth*3.0f, buttonHeight * 2.0f );
		}
		
		airPurity.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
		airPurity.setSize( buttonWidth - treeWidth*4.0f , buttonHeight );
//		airPurityTable.getCell( airPurity )
//			.width( buttonWidth - treeWidth*4.0f )
//			.height( buttonHeight );
		
		filterLeft.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
		filterLeftTable.getCell( filterLeft )
			.width( buttonWidth - treeWidth*4.0f )
			.height( buttonHeight );
		
		dealersListTable.resize( buttonWidth - treeWidth*2.0f, buttonHeight  );
		
		samRemainingBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
		samRemainingBar.setSize( buttonWidth - treeWidth*2.0f , buttonHeight );
//		samRemainingTable.getCell( samRemainingBar ).width( buttonWidth - treeWidth*2.0f ).height(buttonHeight);
		
		samBonusBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
		samBonusBar.setSize( buttonWidth - treeWidth*2.0f , buttonHeight );
//		samBonusTable.getCell( samBonusBar ).width( buttonWidth - treeWidth*2.0f ).height(buttonHeight);
		
		if ( samBackground!=null ) {
			samBackground.setStyle( skin.get( FitCheckButton.FitCheckButtonStyle.class )  );
			samBackgroundTable.getCell( samBackground ).width( buttonWidth - treeWidth*2.0f ).height( buttonHeight );
		}

		if ( pane!=null && getCell( pane )!=null ) {
			pane.setStyle( skin.get( ScrollPaneStyle.class ) );
			getCell( pane )
				.width( Assets.width*desiredWidth )
				.height( Assets.height*desiredHeight );
		}
	}
	
	@Override
	public void act( float delta ) {
		super.act( delta );

		if ( Assets.player!=null && populated ) {
			if ( humidityBar.progress>=1.0f )
				humidityBar.setColor( skin.getColor( "red" ) );
			else
				humidityBar.setColor( skin.getColor( "white" ) );
			
			if ( airPurity.progress<0.73f ) {
				airPurity.setColor( skin.getColor( "red" ) );
			} else
				airPurity.setColor( skin.getColor( "white" ) );
			
			if ( filterLeft.progress<0.1f )
				filterLeft.setColor( skin.getColor( "red" ) );
			else
				filterLeft.setColor( skin.getColor( "white" ) );
		}
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void back() {
		Assets.getResolver().exitDialog();
	}
	
	public void focusNode( Node node ) {
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
	
	public void quickOpen( int quickOpen ) {
		if ( Assets.player!=null && populated ) {
			if ( quickOpen!=NONE ) {
				Node theNode = null;
				tree.collapseAll();
				switch( quickOpen) {
					case MAIN:
					default:
						break;
					case INC: theNode = incNode; break;
					case INC_SETTINGS: theNode = incSettingsNode; break;
					case FC: theNode = fcNode; break;
					case FC_SETTINGS: theNode = fcSettingsNode; break;
					case FRIDGE: theNode = fridgeNode; break;
					case FRIDGE_SETTINGS: theNode = fridgeSettingsNode; break;
					case GLOVE_BOX: theNode = gloveBoxNode; break;
					case GLOVE_BOX_SETTINGS: theNode = gloveBoxSettingsNode; break;
					case PRESSURE_COOKERS: theNode = pcNode; break;
					case DEHYDRATORS: theNode = dehydNode; break;
					case RESOURCES: theNode = resourcesNode; break;
					case CASH: theNode = cashNode; break;
//					case STASH: theNode = fridgeSettingsNode; break;
					case SYRINGES: theNode = syringesNode; break;
//					case GRAIN_JARS: theNode = fridgeSettingsNode; break;
					case SUB_JARS: theNode = substrateNode; break;
					case DEALERS: theNode = dealersNode; break;
					case SAM: theNode = samNode; break;
					
				}
				if ( theNode!=null ) {
					theNode.expandTo();
					theNode.setExpanded( true );
					focusNode( theNode );
				}
			}
		}
	}
	
	public void makePayment() {
		Assets.player.changeCash( -ST.SAMS_BONUS_PRICE );
		Assets.player.debt -= ST.SAMS_BONUS_PRICE;
		if ( Assets.player.debt<= ST.SAMS_LOAN*0.5f && !Assets.player.half_paid ) {
			Assets.player.half_paid = true;
			
			Dialogs.speechDialog( "Sam Sincaid", "Thanks, dawg! I was needing a new amp for my guitar so this was perfect timing." +
					" So hey, I know a guy who can get a strain called 'Business Man' now if you're interested..." );
			Alert alert = Assets.alertPool.borrowObject();
			alert.reset();
			alert.icon = "SyringeIcon";
			alert.title = "Strain: Business Man";
			alert.message = "A new strain has been unlocked: Business Man";
			Assets.player.can_buy_strains.add( "Business Man" );
			alert.type = ST.TABLE;
			alert.uniqueID = ST.ALERT_BUSINESS_MAN;
//			alert.partName = "Strain";
			alert.nextPart = StrainPart.class;
			alert.partObject = StrainMaster.getStrain( "Business Man" );
//			alert.table = new StrainColumn( StrainMaster.getStrain( "Business Man" ) );
			Assets.addAlert( alert );
			Assets.player.increaseXP( 450 );
			Assets.getResolver().trackerSendEvent( "Sam", "Repaid Half Way", "Loan", (long)0 );
		}
		
		if ( Assets.player.debt<=0 && !Assets.player.full_paid ) {
			Assets.player.full_paid = true;
			Dialogs.speechDialog( "Sam Sincaid", "That means a lot to me, " + Assets.player.name + ". Most people would duck and avoid someone before they paid them back." +
					" Here's a little something as thanks: it's a photo of me dressed up as a transformer. Use it as a background!" );
			Assets.player.increaseXP( 1150 );
			Assets.getResolver().trackerSendEvent( "Sam", "Repaid In Full", "Loan", (long)0 );
			Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQEw" );
			populated = false;
		}
		
		Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQEg" );
		
		if ( Assets.player.samBonusTimer<=0 ) {
			// give the Assets.player a bonus
			Assets.player.samBonusTimer = ST.SAMS_BONUS_TIME;
			Assets.getResolver().trackerSendEvent( "Sam", "Bonus", "Loan", (long)0 );
			
			Random random = new Random();
			int randValue = random.nextInt( 100 ) + 1;
			
			String message = "";
			if ( randValue<=9 || Assets.player.firstBonus ) {
				// Blue Coins
				int num = random.nextInt(3) + 1;
//				int num = 100;
				if ( Assets.player.firstBonus )
					num = 6;
				Assets.player.changeCoins( num );
				message = "" + num + " Blue Coins";
			} else if ( randValue<=22) {
				// Syringe
				String name = Assets.player.can_buy_strains.get( random.nextInt( Assets.player.can_buy_strains.size() ) );
				Strain strain = StrainMaster.getStrain( name );
				Assets.player.syringes.add( strain );
				message = "a syringe of " + name;
			} else if ( randValue<=37) {
				// Grain Jars
				int num = random.nextInt(4) + 3;
				Assets.player.grain_jars += num;
				message = "" + num + " Jars of Grain";
			} else if ( randValue<=52) {
				// Substrate Jars
				int num = random.nextInt(3) + 2;
				int type = random.nextInt( 4 );
				Assets.player.sub_jars[type] += num;
				message = "" + num + " jars of " + ST.substrates[type];
			} else if ( randValue<=72) {
				// XP
				int num = (random.nextInt(11) + 5) * 10;
				Assets.player.increaseXP( num );
				message = "" + num + " XP";
			} else if ( randValue<=85) {
				// Cash
				int num = (random.nextInt(21) + 1) * 10 + ST.SAMS_BONUS_PRICE;
//				int num = 1000;
				Assets.player.changeCash( num );
				message = "$" + num;
			} else {
				// Batch
				String name = Assets.player.can_buy_strains.get( random.nextInt( Assets.player.can_buy_strains.size() ) );
				Strain strain = StrainMaster.getStrain( name );
				if ( strain!=null ) {
	 				Batch newBatch = new Batch( strain );
	 				int num_fruits = (int) (strain.batch_weight / strain.fruit_weight);
	 				for ( int j = 0; j < num_fruits; j ++ ) {
	 					Fruit newFruit = new Fruit( strain );
	 					newFruit.growth = strain.fruit_grow_time;
	 					newBatch.addFruit( newFruit );
	 				}
	 				
	 				Assets.player.addBatch( newBatch );
 				}
				message = "a wet batch of " + name;
			}
			
			Dialogs.speechDialog( "Sam Sincaid", "That's sweet of you! Here's " + message + "!" );
			Assets.player.firstBonus = false;
		}
	}
	
	public static class MainBuilder implements ScreenPartBuilder<MainPart> {
		@Override
		public MainPart build() {
			MainPart part = new MainPart( );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2.0f );
			part.setY( Assets.height * 0.5f );
			part.desiredX = 0f;
			part.desiredY = Assets.game.actionBarHeight;
			part.desiredWidth = 1f;
			part.desiredHeight = 1f - Assets.game.actionBarHeight;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
}
