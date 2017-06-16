package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.FloatingText;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.elements.ToggleMark;
import com.bytesbrothers.shroomtycoon.pools.FloatingTextPool;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Dealer;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class DealerPart extends ScreenPart {

	public Dealer theDealer;
	
	private ScrollPane pane;
	private Tree tree;
	private float treeWidth = 1;
	private ArrayList<ToggleMark> marks = new ArrayList<ToggleMark>();
	private ArrayList<ToggleMark> statMarks = new ArrayList<ToggleMark>();
	private Table imageTable;
	private Image icon;
	private Table descTable;
	private Table statsTable;
	private Node statsNode;
	private ProgressBar progressBar;
	private Table progressTable;
	private Table infoTable;

	
	public DealerPart( Dealer dealer ) {
		super( "Dealer" );
		setDealer( dealer );
	}

	@Override
	public void populate() {
		clear();
		marks.clear();
		statMarks.clear();
		
		if ( theDealer!=null ) {
			tree = new Tree( skin, "small" );
			
			tree.setIconSpacing( 0, 0 );
			treeWidth = tree.getStyle().plus.getMinWidth();
	
			ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin ){
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = new Table();
			button.table.add( button.fitButton );
			buttons.add( button );
			tree.add( new Node( button.table ) );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				back();
	 			}
	 		});
			
			imageTable = new Table();
			icon = new Image( skin.getDrawable( "DealerIcon" ) );
			imageTable.add( icon );
			descTable = new Table();
			
			ScreenLabel label = new ScreenLabel( new FitLabel( theDealer.name, skin.get( "BasicWhite", Label.LabelStyle.class ) ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth + height;
					super.resize( width, height );
				}
			};
			label.styleName = "BasicWhite";
			label.colorName = "black";
			label.label.setAlignment( Align.center );
			label.table = descTable;
			label.table.add( label.label ).colspan( 9 );
			labels.add( label );
			label.table.row();
			
			for ( int i = 1; i <= 9; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				descTable.add( newMark );
				marks.add( newMark );
				newMark.toggle = i<=theDealer.rating;
			}

			imageTable.add( descTable );
			tree.add( new Node( imageTable ) );
			
			progressBar = new ProgressBar( theDealer, skin );
			progressTable = new Table();
			progressTable.add( progressBar );
			tree.add( new Node( progressTable ) );
			
			button = new ScreenButton( "Front Stash", "FrontIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( theDealer.batches.size() >=theDealer.capacity );
				}
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = new Table();
			button.table.add( button.fitButton );
			buttons.add( button );
			tree.add( new Node( button.table ) );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				boolean at_least_one = false;

	 				Iterator<Batch> batchIt = Assets.player.batches.iterator();
	 				while ( batchIt.hasNext() && !at_least_one ) {
	 					final Batch currentBatch = batchIt.next();
	 					if ( currentBatch.weighed ) {
	 						at_least_one = true;
	 					}
	 				}
	 				
	 				if ( !at_least_one ) {
	 					// there are no weighed batches
	 					Dialogs.acceptDialog( "You have no frontable batches." );
	 				} else {
		 				ArrayList<Button> buttons = new ArrayList<Button>();
		 				Sprite icon = Assets.getAtlas().createSprite( "BatchIcon" );

		 				batchIt = Assets.player.batches.iterator();
		 				while ( batchIt.hasNext() ) {
		 					final Batch currentBatch = batchIt.next();
		 					if ( currentBatch.weighed ) {
		 						FitButton myButton = new FitButton( currentBatch.strain.name + " (" +
	 	 							ST.withComasAndTwoDecimals( currentBatch.getWeight() ) + "g)",
	 	 							icon, skin );
		 						myButton.setColor( skin.getColor( "resource" ));
	 	 		 				myButton.left();
	 	 		 				myButton.addListener( new ChangeListener() {
	 	 		 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 							Assets.player.batches.remove( currentBatch );
		 		 		 				theDealer.batches.add( currentBatch );
//		 		 		 				theDealer.alertedOnSale = false;
//	 		 		 		 			FloatingText fText = new FloatingText( "-" + (int)currentBatch.getWeight() + "g", skin.get( "Stash", FitLabelStyle.class ) );
		 		 		 				if ( Assets.game.fTextPool==null )
		 		 		 					Assets.game.fTextPool = new FloatingTextPool();
				 		 				FloatingText fText = Assets.game.fTextPool.borrowObject();
				 		 				fText.setText( "-" + (int)currentBatch.getWeight() + "g" );
				 		 				fText.setStyle( skin.get( "Stash", Label.LabelStyle.class ) );
//	 		 		 					fText.setWidth( game.width * 0.25f );
//	 		 		 					fText.setHeight( game.height * 0.1f );
	 		 		 					fText.setX( Assets.width * 0.25f );
	 		 		 					fText.setY( Assets.height/9.0f );
	 		 		 					Assets.game.addFloatingText( fText );
//	 		 		 					Tween.to( fText, FloatingTextTween.Y, 1.0f )
//	 		 		 			        .target(  game.height/9.0f * 2.0f )
//	 		 		 			        .ease(TweenEquations.easeOutQuad)
//	 		 		 			        .start( Assets.getTweenManager() ); //** start it
//	 		 		 					
//	 		 		 					Tween.to( fText, FloatingTextTween.ALPHA, 1.0f )
//	 		 		 			        .target( 0.0f )
//	 		 		 			        .delay( 0.7f )
//	 		 		 			        .ease(TweenEquations.easeInQuad)
//	 		 		 			        .start( Assets.getTweenManager() ); //** start it
//	 		 		 					
//	 		 		 					fText.failSafe = 2.0f;
	 	 		 		 			}
	 	 		 		 		});
		 						
		 						buttons.add( myButton );
		 					}
		 				}
		 				
		 				Dialogs.scrollTableDialog( "Choose a batch to give to this dealer:", buttons );
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Collect Cash", "CollectIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( theDealer.getProgress()!=1.0f );
				}
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = new Table();
			button.table.add( button.fitButton );
			buttons.add( button );
			tree.add( new Node( button.table ) );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				float total_cash = 0.0f;
	 				float total_weight = 0.0f;
	 				float average_potency = -1.0f;
	 				boolean obj_strain_sold = false;
	 				Iterator<Batch> batchIt = theDealer.batches.iterator();
	 				while ( batchIt.hasNext() ) {
	 					Batch currentBatch = batchIt.next();
	 					if ( average_potency==-1.0f )
	 						average_potency = currentBatch.strain.potency;
	 					else
	 						average_potency = (average_potency+currentBatch.strain.potency)*0.5f;
	 					float weight = currentBatch.getWeight();
	 					total_weight += weight;
	 					total_cash += weight * currentBatch.strain.potency;
	 					theDealer.total_sold += weight;
	 					Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQBg" );
	 					
	 					// if the Assets.player has a sell certain strain objective
	 	 				if ( Assets.player.objective!=null && 
	 	 						Assets.player.objective.type == Assets.player.objective.STRAIN &&
	 	 						currentBatch.strain.name.equals( Assets.player.objective.text )) {
	 	 					obj_strain_sold = true;
	 	 				}
	 				}
	 				
	 				final float dealers_cut = total_cash * theDealer.cut;
	 				final int collection = (int) Math.ceil( total_cash - dealers_cut );
	 				Random random = new Random();
	 				// %10 we'll get feedback on this sale
	 				if ( random.nextFloat()>=0.9f ) {
	 					Assets.player.reg_dealer_name = theDealer.name;
	 					if ( average_potency<9.0f ) {
	 						Assets.player.reg_last_batch = 0;
		 				} else if ( average_potency<=10.5f ) {
		 					Assets.player.reg_last_batch = 1;
		 				} else {
		 					Assets.player.reg_last_batch = 2;
		 				}
	 				}
	 				// if the Assets.player has a sell certain strain objective
	 				if ( obj_strain_sold ) {
	 					
	 					Dialogs.objectiveDialog( "You successfully sold a batch of " + Assets.player.objective.text + "!" );
	 					Assets.player.objective = null;
						if ( Assets.player.objective_alert != null ) {
							Assets.removeAlert( Assets.player.objective_alert );
							Assets.player.objective_alert = null;
						}
						Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Sold Strain", (long)0 );
	 				}
	 				Assets.player.totalStashSold += total_weight;
	 				Dialogs.sweetDialog( "You collected $" + ST.withComasAndTwoDecimals( collection ) +
	 						" from " + theDealer.name + "!", new ChangeListener() {
			 			public void changed (ChangeEvent event, Actor actor) {
			 				theDealer.batches.clear();
			 				Assets.player.changeCash( collection );
			 				theDealer.total_cash_made += collection;
			 			}
			 		} );
	 				
	 				if ( !Assets.player.hasStamets && Assets.player.totalStashSold>= 448*2 ) {
	 					Assets.player.hasStamets = true;
			 			Assets.player.can_buy_strains.add( "Stamets" );
	 					Dialogs.sweetDialog("By selling over 2lbs of mushrooms you've unlocked a new strain: Stamets", new ChangeListener() {
	 			 			public void changed (ChangeEvent event, Actor actor) {
	 			 				StrainPart strainPart = Assets.getScreenHandler().getScreenPart( StrainPart.class );
//	 			 				StrainPart strainPart = (StrainPart) game.getScreenPart( "Strain" );
	 			 				strainPart.setStrain( StrainMaster.getStrain( "Stamets" ) );
	 			 				
	 			 				strainPart.setDx( 0 );
	 			 				setDx( -1f );
	 			 				
	 			 				Assets.getScreenHandler().setActivePart( strainPart );
	 			 				
	 			 			}
	 					});
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Fast Forward", "FastForwardIcon", "useCoin", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( theDealer.batches.size()==0 || theDealer.getProgress()==1.0f );
				}
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = new Table();
			button.table.add( button.fitButton );
			buttons.add( button );
			tree.add( new Node( button.table ) );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				final int coin_cost = 1 + theDealer.getTimeLeft()/(60*60);
		 			if ( Assets.player.coins >= coin_cost ) {
		 				Dialogs.choiceDialog( "Would you like to fast forward this sale for " + coin_cost + " Blue Coins?",
							new ChangeListener() {
			 		 			public void changed (ChangeEvent event, Actor actor) {
			 		 				Assets.player.changeCoins( -coin_cost );
			 		 				Iterator<Batch> batchIt = theDealer.batches.iterator();
			 		 				while ( batchIt.hasNext() ) {
			 		 					Batch currentBatch = batchIt.next();
			 		 					Tween.to( currentBatch, Batch.BatchTween.SOLD_WEIGHT, 2.0f )
			 		 			        .target( currentBatch.getWeight() )
			 		 			        .ease(TweenEquations.easeInQuad)
			 		 			        .start( Assets.getTweenManager() ); //** start 
			 		 				}
			 		 				Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Dealer FF", (long)0 );
			 		 			}
		 					}
						);
		 			} else {
		 				Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + coin_cost + "." );
	 				}
	 			}
	 		});
			
			button = new ScreenButton( "Dealer Stats", "DealerIcon", "topLevel", skin ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = new Table();
			button.table.add( button.fitButton );
			buttons.add( button );
			statsNode = new Node( button.table );
			tree.add( statsNode );
			
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				statsNode.setExpanded( !statsNode.isExpanded() );
					focusNode( statsNode );
	 			}
	 		});
		
			statsTable = new Table();
			
			label = new ScreenLabel( new FitLabel( "Speed", skin ) );
			label.label.setAlignment( Align.right );
			label.scaleX = 0.3f;
			label.table = statsTable;
			label.table.add( label.label );
			labels.add( label );
			
			for ( int i = 1; i <= 3; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.add( newMark );
				statMarks.add( newMark );
				newMark.toggle = i<=theDealer.speed_level;
			}
			statsTable.row();
			
			label = new ScreenLabel( new FitLabel( "Capacity", skin ) );
			label.label.setAlignment( Align.right );
			label.scaleX = 0.3f;
			label.table = statsTable;
			label.table.add( label.label );
			labels.add( label );
			
			for ( int i = 1; i <= 3; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.add( newMark );
				statMarks.add( newMark );
				newMark.toggle = i<=theDealer.capacity_level;
			}
			statsTable.row();
			
			label = new ScreenLabel( new FitLabel( "Overhead", skin ) );
			label.label.setAlignment( Align.right );
			label.scaleX = 0.3f;
			label.table = statsTable;
			label.table.add( label.label );
			labels.add( label );
			
			for ( int i = 1; i <= 3; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.add( newMark );
				statMarks.add( newMark );
				newMark.toggle = i<=theDealer.cut_level;
			}
			statsNode.add( new Node( statsTable ) );
			
			infoTable = new Table();
			
			label = new ScreenLabel( new FitLabel( "Total Sold: " + ST.withComasAndTwoDecimals( theDealer.total_sold ) + "g", skin ) ) {
				float lastSold = 0;
				@Override
				public void act( float delta ) {
					if ( lastSold!=theDealer.total_sold ) {
						lastSold = theDealer.total_sold;
						label.setText( "Total Sold: " + ST.withComasAndTwoDecimals( theDealer.total_sold ) + "g" );
					}
				}
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.center );
			label.scaleY = 0.8f;
			label.table = infoTable;
			label.table.add( label.label );
			labels.add( label );
			infoTable.row();
			
			label = new ScreenLabel( new FitLabel( "Total Made: $" + ST.withComas( theDealer.total_cash_made ) , skin ) ) {
				int lastCash = 0;
				@Override
				public void act( float delta ) {
					if ( lastCash!=theDealer.total_cash_made ) {
						lastCash = theDealer.total_cash_made;
						label.setText( "Total Made: $" + ST.withComas( theDealer.total_cash_made ) );
					}
				}
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.center );
			label.scaleY = 0.8f;
			label.table = infoTable;
			label.table.add( label.label );
			labels.add( label );
			infoTable.row();
			
			label = new ScreenLabel( new FitLabel( "Known for " + theDealer.days + "d " + theDealer.hours + "h " +
					theDealer.mins + "m " + theDealer.seconds + "s", skin ) ) {
				int lastSecond = 0;
				@Override
				public void act( float delta ) {
					if ( lastSecond!=theDealer.seconds ) {
						lastSecond = theDealer.seconds;
						label.setText(  "Known for " + theDealer.days + "d " + theDealer.hours + "h " +
								theDealer.mins + "m " + theDealer.seconds + "s" );
					}
				}
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.center );
			label.scaleY = 0.8f;
			label.table = infoTable;
			label.table.add( label.label );
			labels.add( label );
			infoTable.row();
			
			statsNode.add( new Node( infoTable ) );
			
			button = new ScreenButton( "Fire Dealer", "TrashIcon", "action", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( theDealer.batches.size()>0 );
				}
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = new Table();
			button.table.add( button.fitButton );
			buttons.add( button );
			tree.add( new Node( button.table ) );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Dialogs.choiceDialog( "Are you sure you want to fire this dealer?",
 						new ChangeListener() {
 		 		 			public void changed (ChangeEvent event, Actor actor) {
 		 		 				// if the Assets.player has an objective
 		 		 				if ( Assets.player.objective!=null && 
 		 		 						Assets.player.objective.type==Assets.player.objective.FIRE &&
 		 		 						(Assets.player.objective.text.equals("") || Assets.player.objective.text.equals( theDealer.name )) ) {
 		 		 					Dialogs.objectiveDialog("You successfully fired that no good " + theDealer.name + "!" );
 		 							if ( Assets.player.objective_alert != null ) {
 		 								Assets.removeAlert( Assets.player.objective_alert );
 		 								Assets.player.objective_alert = null;
 		 							}
 		 							Assets.player.objective = null;
 		 							Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Fire Dealer", (long)0 );
 		 		 				}
 		 		 				Assets.player.dealers.remove( theDealer );
 		 		 				back();
 		 		 			}
 	 					}
 					);
	 			}
	 		});
			
			pane = new ScrollPane( tree, skin);
			add( pane );
			pane.addListener( Assets.stopTouchDown() );
		}
	}
	
	@Override
	public Object getObject() {
		return theDealer;
	}
	
	@Override
	public void passObject( Object object ) {
		if ( object!=null && object.getClass().equals( Dealer.class ) )
			setDealer( (Dealer)object );
	}
	
	public void setDealer( Dealer dealer ) {
		theDealer = dealer;
		if ( theDealer!=null )
			theDealer.useAlert();
		populated = false;
	}
	
	@Override
	protected void resize( ) {
		
//		game = Assets.game;
		super.resize( );
		
		if ( theDealer!=null ) {

			float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
			
			tree.setStyle( skin.get( "small", TreeStyle.class ) );
			treeWidth = tree.getStyle().plus.getMinWidth();
			
			icon.setDrawable( skin.getDrawable( "DealerIcon" ) );
			imageTable.getCell( icon )
				.width( buttonHeight )
				.height( buttonHeight );
			
			float markSize = ( buttonWidth - ( treeWidth + buttonHeight ) ) / 9.0f;
			
			for ( ToggleMark mark: marks ) {
				mark.setStyle( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				descTable.getCell( mark )
					.width( markSize )
					.height( markSize );
				
			}
			
			progressBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
			progressTable.getCell( progressBar )
				.width( buttonWidth - treeWidth ).height( buttonHeight * 0.5f );
			progressTable.invalidateHierarchy();
			
			float statMarkSize = Math.max( buttonHeight*0.8f, buttonWidth*0.1f );
			
			for ( ToggleMark mark: statMarks ) {
				mark.setStyle( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.getCell( mark )
					.width( statMarkSize ).height( statMarkSize );
				
			}
			
			pane.setStyle( skin.get( ScrollPaneStyle.class ) );
			getCell( pane )
				.width( Assets.width*desiredWidth )
				.height( Assets.height*desiredHeight );
		}
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void back() {
//		ScreenPart screen = Assets.game.getScreenPart( "Main" );
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );

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
	
	public static class DealerBuilder implements ScreenPartBuilder<DealerPart> {
		@Override
		public DealerPart build() {
			DealerPart part = new DealerPart( null );
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
