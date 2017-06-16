package com.bytesbrothers.shroomtycoon.ScreenParts;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar.ProgressBarStyle;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Objective;

public class PlayerPart extends ScreenPart {

	private ScrollPane pane;
	private Table scrollTable;
	
	private ProgressBar levelBar;
	private Table objectiveTable;
	private Image objImage;
	public Objective objective = null;

	private float treeWidth;

	private Tree tree;

	private Node statsNode;
	
	public PlayerPart( ) {
		super( "Player" );
//		setPlayer( Assets.player );
	}

	@Override
	public void populate() {
		clear();

		scrollTable = new Table();
		
		ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				back();
 			}
 		});
		
		ScreenLabel label = new ScreenLabel( new FitLabel( Assets.player.name, skin ) );
		label.label.setAlignment( Align.center );
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		label.table.row();
		
		levelBar = new ProgressBar( Assets.player, skin );
		scrollTable.add( levelBar );
		scrollTable.row();
		
		if ( Assets.player.objective!=null ) {
			objective = Assets.player.objective;
			
			objectiveTable = new Table();
			
			objImage = new Image( skin.getSprite( "ObjectiveIcon" ) );
			objImage.setScaling( Scaling.fillX );
			objectiveTable.add( objImage );
			
			label = new ScreenLabel( new FitLabel( "Current Objective", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= height;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.center );
			label.table = objectiveTable;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();
			
			label = new ScreenLabel( new FitLabel( Assets.player.objective.definition, skin ) );
			label.label.setAlignment( Align.center );
			label.table = objectiveTable;
			label.table.add( label.label ).colspan( 2 );
			labels.add( label );
			label.table.row();
			
			button = new ScreenButton( "Skip this Objective (2 BC)", "CoinIcon", "useCoin", skin );
			button.table = objectiveTable;
			button.table.add( button.fitButton ).colspan( 2 );
			button.table.row();
			buttons.add( button );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Dialogs.choiceDialog( "Would you like to skip this objective for 2 Blue Coins?",
						new ChangeListener() {
						public void changed( ChangeEvent event, Actor actor ) {
							if ( Assets.player.coins>=2 ) {
								Dialogs.sweetDialog( "You have skipped this objective", new ChangeListener() {
									public void changed (ChangeEvent event, Actor actor ) {
										populate();
									}
								});
								Assets.getResolver().trackerSendEvent( "Objective", "Skipped", Assets.player.objective.text, (long)0 );
	 		 					Assets.player.changeCoins( -2 );
	 		 					Assets.player.objective = null;
	 							if ( Assets.player.objective_alert != null ) {
	 								Assets.removeAlert( Assets.player.objective_alert );
	 								Assets.player.objective_alert = null;
	 							}
	 							
	 							Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Skip Objective", (long)0 );
							} else {
								Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least 2." );
	 		 				}
						}
					});
	 			}
	 		});
			
			scrollTable.add( objectiveTable );
			scrollTable.row();
		} else {
			objectiveTable = null;
			objective = null;
		}
		
		if ( Assets.getResolver().isAndroid() ) {
			button = new ScreenButton( "Achievements", "games_achievements_green", "newColumn", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( !Assets.getResolver().isGoogleSignedIn() );
				}
			};
			button.table = scrollTable;
			button.table.add( button.fitButton ).padTop( 14 );
			button.table.row();
			buttons.add( button );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Assets.getResolver().showAchievements();
	 			}
	 		});
		}
// -------STATS----------------------------
		tree = new Tree( skin, "small" );
		
		tree.setIconSpacing( 0, 0 );
		treeWidth = tree.getStyle().plus.getMinWidth();
		
		button = new ScreenButton( "Statistics", "SideJobIcon", "topLevel", skin ) {
			@Override
			public void resize( int width, int height ) {
				width -= treeWidth;
				super.resize( width, height );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		statsNode = new Node( button.table );
		tree.add( statsNode );
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				statsNode.setExpanded( !statsNode.isExpanded() );
 				focusNode( statsNode );
 			}
 		});

		String[] statsStrings = { "Total Jars Made: " + ST.withComas( Assets.player.totalJarsMade ),
				"Total Casings Made: " + ST.withComas( Assets.player.totalCasingsMade ),
				"Total Syringes Made: " + ST.withComas( Assets.player.totalSyringesMade ),
				"Total Cash Earned: $" + Assets.player.getTotalCashMadeString(),
				"Total Cash Spent: $" + Assets.player.getTotalCashSpentString(),
				"Total Shrooms Made: " + ST.withComasAndTwoDecimals( Assets.player.totalStashWeighed ) + "g",
				"Total Shrooms Sold: " + ST.withComasAndTwoDecimals( Assets.player.totalStashSold ) + "g",
				"Total Blue Coins Earned: " + ST.withComas( Assets.player.totalBlueCoinsEarned ),
				"Total Blue Coins Used: " + ST.withComas( Assets.player.totalBlueCoinsUsed ),
				"Objectives Completed: " + ST.withComas( Assets.player.totalObjectivesDone ),
				"Total Cooks: " + ST.withComas( Assets.player.totalCooks ),
				"Total Harvests: " + ST.withComas( Assets.player.totalHarvests ),
				"Total Dealers Hired: " + ST.withComas( Assets.player.totalDealersHired ),
				"Total Contaminations: " + ST.withComas( Assets.player.totalContaminations )};
		
		for ( String string: statsStrings ) {
			label = new ScreenLabel( new FitLabel( string, skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth*2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.left );
			label.scaleY = 0.6f;
			label.table = new Table();
			label.table.add( label.label );
			labels.add( label );
			statsNode.add( new Node( label.table ) );
		}
		
		
		scrollTable.add( tree ).padTop( 14 );
		scrollTable.row();
// -----------END STATS-------------------------------
		
		button = new ScreenButton( "Manage Profiles", "SettingsUp", "newColumn", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).padTop( 14 );
		button.table.row();
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				ManageProfilesPart screen = Assets.getScreenHandler().getScreenPart( ManageProfilesPart.class );
// 				ScreenPart screen = Assets.game.getScreenPart( "Manage Profiles" );

 				screen.setDx( 0 );
 				setDx( -1f );
 				
 				Assets.getScreenHandler().setActivePart( screen );
 				
 				ActionBarPart actionBarPart = (ActionBarPart) Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
 				actionBarPart.setDy( -Assets.game.actionBarHeight );
 			}
 		});
		
//		button = new ScreenButton( "Compress Save game", "SettingsUp", "action", skin );
//		button.table = scrollTable;
//		button.table.add( button.fitButton ).padTop( 14 );
//		button.table.row();
//		buttons.add( button );
//
//		button.fitButton.addListener( new ChangeListener() {
// 			public void changed (ChangeEvent event, Actor actor) {
// 				
// 				String oldPlayer = new Gson().toJson( Assets.player );
// 				
// 				ShroomPlayerCompressor.compress( Assets.player );
// 				
// 				String newPlayer = new Gson().toJson( Assets.player );
// 				
// 				System.out.println( "Compressed from: " + oldPlayer.getBytes().length + " to " + newPlayer.getBytes().length );
// 				
// 				String justColors = new Gson().toJson( Assets.player.customColors );
// 				
// 				System.out.println( "Just colors size: " + justColors.getBytes().length );
// 			}
// 		});
		
		pane = new ScrollPane( scrollTable, skin);
		pane.addListener( Assets.stopTouchDown() );
		add( pane );

	}

	@Override
	protected void resize( ) {

		super.resize( );

		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );

		tree.setStyle( skin.get( "small", TreeStyle.class ) );
		treeWidth = tree.getStyle().plus.getMinWidth();
		
		levelBar.setStyle( skin.get( ProgressBarStyle.class ) );
		scrollTable.getCell( levelBar ).width( buttonWidth ).height( buttonHeight*0.7f ).padBottom( buttonHeight * 0.2f );
		
		if ( objectiveTable!=null ) {
			objImage.setDrawable( skin.getDrawable( "ObjectiveIcon" ) );
			objectiveTable.getCell( objImage ).width( buttonHeight ).height( buttonHeight );
		}
		
		pane.setStyle( skin.get( ScrollPaneStyle.class ) );
		getCell( pane )
			.width( Assets.width*desiredWidth )
			.height( Assets.height*desiredHeight );
	}
	
	@Override
	public void screenAct( float delta ) {
		boolean newObjective = false;
		if ( Assets.player!=null && objective==null && Assets.player.objective!=null )
			newObjective = true;
		if ( Assets.player!=null && objective!=null && !objective.equals( Assets.player.objective ) )
			newObjective = true;
		if ( newObjective ) {
			System.out.println( "PlayerPart.object needs reset" );
			populated = false;
		}
	}
	
//	@Override
//	public void passObject( Object object ) {
//		if ( object!=null && object.getClass().equals( ShroomPlayer.class ) )
//			setPlayer( (ShroomPlayer)object );
//	}
//	
//	public void setPlayer( ShroomPlayer Assets.player ) {
//		this.Assets.player = Assets.player;
//		populated = false;
//	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void back() {
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );
//		ScreenPart screen = Assets.game.getScreenPart( "Main" );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public void focusNode( Node node ) {
		if ( node.isExpanded() ) {
			boolean firstTime = true;
			float bottom = 0, top = 0;
			pane.validate();
			Array<Node> children = node.getChildren();
			for ( Node child: children ) {
				Actor actor = child.getActor();
				if ( actor!=null ) {
					float y = actor.getY();
					float height = actor.getHeight();
					if ( firstTime ) {
						bottom = y;
						top = y+height;
						firstTime = false;
					} else {
						if ( y<bottom )
							bottom = y;
						if ( y+height>top )
							top = y + height;
					}
				}
			}
			
			pane.scrollTo( 0, bottom, 0, top-bottom );
		}

	}
	
	public static class PlayerBuilder implements ScreenPartBuilder<PlayerPart> {
		@Override
		public PlayerPart build() {
			PlayerPart part = new PlayerPart( );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2.0f );
			part.setY( Assets.height * (0.5f+(Assets.game.actionBarHeight*0.5f)) );
			part.desiredX = 1f;
			part.desiredY = Assets.game.actionBarHeight;
			part.desiredWidth = 1f;
			part.desiredHeight = 1f - Assets.game.actionBarHeight;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
}
