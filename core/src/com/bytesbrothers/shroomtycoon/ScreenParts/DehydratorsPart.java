package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar.ProgressBarStyle;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Batch;
import com.bytesbrothers.shroomtycoon.structures.Dehydrator;

public class DehydratorsPart extends ScreenPart {

	private ScrollPane pane;
	private Tree tree;
	private int treeWidth = 1;
	private Table dehydTable;
	ArrayList<DehydratorTable> dehydratorTables = new ArrayList<DehydratorTable>();
	ArrayList<BatchLine> dryingBatches = new ArrayList<BatchLine>();
	
	private float lastScrollY = 0;

	private Node driedNode;

	private Node queuedNode;
	private Table queuedTable;
	
	public DehydratorsPart( ) {
		super( "Dehydrators" );
	}

	@Override
	public void populate() {
		clear();
		dehydratorTables.clear();

		tree = new Tree( skin, "small" );
		
		tree.setIconSpacing( 0, 0 );
		treeWidth = (int)tree.getStyle().plus.getMinWidth();
		
		ScreenButton button = new ScreenButton( "Back", "BackIcon", "white", skin ) {
			@Override
			public void resize( int width, int height ) {
				width -= treeWidth;
				super.resize( width, height );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		tree.add( new Node( button.table ) );
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				back();
 			}
 		});
		
		button = new ScreenButton( "Dried", "BatchIcon", "topLevel", skin ) {
			@Override
			public void resize( int width, int height ) {
				width -= treeWidth;
				super.resize( width, height );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		boolean wasOpen = driedNode!=null? driedNode.isExpanded(): false;
		driedNode = new Node( button.table );
		driedNode.setExpanded( wasOpen );
		tree.add( driedNode );
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				driedNode.setExpanded( !driedNode.isExpanded() );
 				focusNode( driedNode );
 			}
 		});

		ArrayList<Batch> batches = Assets.player.batches;
		
		for ( final Batch batch: batches ) {
			batch.needsResize = false;
			if ( batch.getProgress()==1.0f && !batch.weighed && batch.dehydrator==-1 ) {
				button = new ScreenButton( batch.strain.name + " [Fruits: " + batch.getNumFruits() + "]",
						"WeighIcon", "newColumn", skin ) {
					@Override
					public void resize( int width, int height ) {
						width -= treeWidth*2.0f;
						super.resize( width, height );
					}
				};
				button.table = new Table();
				button.table.add( button.fitButton );
				driedNode.add( new Node( button.table ) );
				buttons.add( button );

				button.fitButton.addListener( new ChangeListener() {
		 			public void changed (ChangeEvent event, Actor actor) {
		 				WeighPart screen = Assets.getScreenHandler().getScreenPart( WeighPart.class );
//		 				WeighPart screen = (WeighPart)Assets.game.getScreenPart( "Weigh" );
		 				screen.setBatch( batch );
		 				
		 				screen.setDx( 0 );
		 				setDx( -1f );
		 				
		 				Assets.getScreenHandler().setActivePart( screen );
		 			}
		 		});
			}
		}
		
		dehydTable = new Table();
		
		ArrayList<Dehydrator> dehydrators = Assets.player.dehydrators;
		int index = 1;
		for ( final Dehydrator dehydrator: dehydrators ) {
			DehydratorTable cookerTable = new DehydratorTable( dehydrator, skin, index );
			
			dehydTable.add( cookerTable );
			dehydTable.row();
	 		dehydratorTables.add( cookerTable );
	 		
	 		index += 1;
		}
		
		tree.add( new Node( dehydTable ) );
		
		button = new ScreenButton( "Queued", "BatchIcon", "topLevel", skin ) {
			@Override
			public void resize( int width, int height ) {
				width -= treeWidth;
				super.resize( width, height );
			}
		};
		button.table = new Table();
		button.table.add( button.fitButton );
		wasOpen = queuedNode!=null? queuedNode.isExpanded(): false;
		queuedNode = new Node( button.table );
		queuedNode.setExpanded( wasOpen );
		tree.add( queuedNode );
		buttons.add( button );

		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				queuedNode.setExpanded( !queuedNode.isExpanded() );
 				focusNode( queuedNode );
 			}
 		});
		
		dryingBatches.clear();
		queuedTable = new Table();
		for ( Batch batch: batches ) {
			if ( batch.getProgress()!=1.0f && batch.dehydrator == -1 ) {
				BatchLine newLine = new BatchLine( batch, skin );
				queuedTable.add( newLine );
				queuedTable.row();
				dryingBatches.add( newLine );
			}
		}
		
		queuedNode.add( new Node( queuedTable ) );
		
		if ( pane!=null )
			lastScrollY = pane.getScrollY();
		pane = new ScrollPane( tree, skin);
		add( pane );
		pane.validate();
		pane.setScrollY( lastScrollY );
		pane.updateVisualScroll();

	}
	
	@Override
	public void screenAct( float delta ) {
		boolean needsResize = false;
		ArrayList<Batch> batches = Assets.player.batches;
		for ( Batch batch: batches ) {
			if ( batch.needsResize ) {
				needsResize = true;
				batch.needsResize = false;
			}
		}
		if ( needsResize ) {
			populated = false;
		}
	}

	@Override
	protected void resize( ) {
		
		super.resize( );

		int buttonHeight = (int)Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		int buttonWidth = (int)Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
		
		tree.setStyle( skin.get( "small", TreeStyle.class ) );
		treeWidth = (int)tree.getStyle().plus.getMinWidth();
		
		for ( DehydratorTable cookerTable: dehydratorTables ) {
			cookerTable.resize( buttonWidth - treeWidth*1, buttonHeight, skin );
//			dehydTable.getCell( cookerTable);
		}
		
		for ( BatchLine batchLine: dryingBatches ) {
			batchLine.resize( buttonWidth -  treeWidth*2, buttonHeight );
		}
		
		if ( pane!=null && getCell( pane )!=null ) {
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
		MainPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );
//		ScreenPart screen = Assets.game.getScreenPart( "Main" );

		screen.setDx( 0 );
		setDx( 1f );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public class DehydratorTable extends Table {
		Dehydrator dehydrator;
		Image image;
		int backgroundBorderWidth = 1;
//		FitLabel nameLabel;
//		FitButton rateButton;
		
		ArrayList<BatchLine> batchLines = new ArrayList<BatchLine>();
		
		public DehydratorTable ( Dehydrator mydehydrator, Skin skin, int index ) {
			this.dehydrator = mydehydrator;
			
			Drawable background = skin.getDrawable( "ButtonDown");
			setBackground( background );
			setColor( skin.getColor("resource" ) );
			
			backgroundBorderWidth = (int)(background.getLeftWidth() + background.getRightWidth());
			
			image = new Image( skin.getDrawable( "Dehydrator") );
			image.setScaling( Scaling.fillY );
			add( image );
			
			ScreenLabel label = new ScreenLabel( new FitLabel( "Dehydrator #" + index, skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= height + backgroundBorderWidth + treeWidth;
					super.resize( width, height );
				}
			};
//			label.label.setColor( skin.getColor( "black" ) );
//			label.colorName = "black";
			label.label.setAlignment( Align.center );
			label.scaleY = 0.5f;
			label.table = this;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();

			ScreenButton button = new ScreenButton( "Upgrade Dry Rate [%" + (int)(dehydrator.rate * 100.0f) + "]",
					"CashIcon", "action", skin ) {
				float lastRate = -1.0f;
				@Override
				public void act( float delta ) {
					if ( dehydrator.rate!=lastRate ) {
						lastRate = dehydrator.rate;
						fitButton.setText( "Upgrade Dry Rate [%" + (int)(dehydrator.rate * 100.0f) + "]" );
					}
				}
				@Override
				public void resize( int width, int height ) {
					width -= backgroundBorderWidth + treeWidth;
					super.resize( width, height );
				}
			};
			button.table = this;
			button.table.add( button.fitButton ).colspan( 2 );
			button.table.row();
			buttons.add( button );

			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Dialogs.choiceDialog( "Would you like to increase this dehydrator's dry speed for $650?",
						new ChangeListener() {
		 		 			public void changed (ChangeEvent event, Actor actor) {
		 		 				if ( Assets.player.getCash()>= 650 ) {
		 		 					Assets.player.changeCash( -650 );
		 		 					dehydrator.rate += 0.1f;
		 		 					Dialogs.sweetDialog( "You increased the dry speed to %" + (int)(dehydrator.rate * 100.0f) + "!", new ChangeListener() {
				 				 			public void changed (ChangeEvent event, Actor actor) { }
			 				 			}
			 		 				);
			 		 				Assets.getResolver().trackerSendEvent( "Upgrade", "Dehydrator", "Speed", (long)0 );
		 		 				} else {
		 		 					Dialogs.acceptDialog( "You do not have enough cash for this purchase" );
		 		 				}
		 		 				
		 		 			}
						}
					);
	 			}
	 		});
			
			ArrayList<Batch> batches = Assets.player.batches;
			
			for ( Batch batch: batches ) {
				if ( batch.dehydrator == (index-1) ) {
					BatchLine newLine = new BatchLine( batch, skin );

					add( newLine ).colspan( 2 );
					row();
					batchLines.add( newLine );
				}
			}
		}
		
		public void resize( int width, int height, Skin skin ) {
			Drawable background = skin.getDrawable( "ButtonDown");
			setBackground( background );

			backgroundBorderWidth = (int)(background.getLeftWidth() + background.getRightWidth());

			image.setDrawable( skin.getDrawable( "Dehydrator") );
			getCell( image ).width( height ).height( height );

			for ( BatchLine batchLine: batchLines ) {
				batchLine.resize( width - backgroundBorderWidth, height );
			}
		}
		
	}
	
	public class BatchLine extends Table {
		Batch batch;
		private FitLabel nameLabel;
		ProgressBar progressBar;
		private FitButton fastForwardButton;
		
		public BatchLine( Batch myBatch, Skin skin ) {
			batch = myBatch;
			nameLabel = new FitLabel( myBatch.strain.name + " [" + myBatch.getNumFruits() + "]", skin );
			nameLabel.setColor( skin.getColor( "black" ) );
			nameLabel.setAlignment( Align.center );
			add( nameLabel );
			
			progressBar = new ProgressBar( myBatch, skin );
			add( progressBar );
			
			fastForwardButton = new FitButton( "Fast Forward", skin.getRegion( "FastForwardIcon" ), skin );
			fastForwardButton.setColor( skin.getColor( "useCoin" ) );
			fastForwardButton.setDisabled( batch.fastForward );
			add(fastForwardButton);
			fastForwardButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				final int coin_cost = 1 + batch.getTimeLeft()/(60*60);
		 				if ( Assets.player.coins >= coin_cost ) {
		 					Dialogs.choiceDialog( "Would you like to fast forward this batch to fully dry for " + coin_cost + " Blue Coin(s)?",
		 						new ChangeListener() {
 	 		 		 			public void changed (ChangeEvent event, Actor actor) {
 	 		 		 				fastForwardButton.setDisabled( true );
 	 		 		 				batch.fastForward = true;
 	 		 		 				Assets.player.changeCoins( -coin_cost );
	 	 		 		 			Tween.to( batch, Batch.BatchTween.DRY_TIME, 2.0f )
	 	 		 			        .target( batch.max_dry_time )
	 	 		 			        .ease(TweenEquations.easeInQuad)
	 	 		 			        .start( Assets.getTweenManager() ); //** start it
	 	 		 		 			Assets.getResolver().trackerSendEvent( "Blue Coin", "Used", "Batch FF", (long)0 );
 	 		 		 			}
 		 					}
		 					);
		 				} else {
		 					Dialogs.acceptDialogGetMoreCoins( "You don't have enough Blue Coins for this. You need at least " + coin_cost + "." );
		 				}
	 			}
	 		});
		}
		
		public void resize( int width, int height ) {
			nameLabel.setStyle( skin.get( "BasicWhite", Label.LabelStyle.class ) );
			getCell( nameLabel ).width( width * 0.3f ).height( height * 0.8f );
			
			progressBar.setStyle( skin.get( ProgressBarStyle.class ) );
			getCell( progressBar ).width( width * 0.35f ).height( height * 0.5f ).padRight( width * 0.05f );
			
			fastForwardButton.setStyle( skin.get( FitButton.FitButtonStyle.class ) );
			fastForwardButton.image.setDrawable( skin.getDrawable( "FastForwardIcon" ) );
			getCell( fastForwardButton ).width( width * 0.3f ).height( height * 0.8f );
		}
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
	
	public static class DehydratorsBuilder implements ScreenPartBuilder<DehydratorsPart> {
		@Override
		public DehydratorsPart build() {
			DehydratorsPart part = new DehydratorsPart( );
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
