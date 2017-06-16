package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ST;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Alert;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.elements.ToggleMark;
import com.bytesbrothers.shroomtycoon.elements.ToggleMark.ToggleMarkStyle;
import com.bytesbrothers.shroomtycoon.structures.Strain;

public class StrainPart extends ScreenPart {

	public Strain theStrain;
	
	private ScrollPane pane;
	private Tree tree;
	private ArrayList<ToggleMark> marks = new ArrayList<ToggleMark>();
	private Table imageTable;
	private Image icon;
	private Table descTable;
	private Label descLabel;
	private float treeWidth;
	private int cost;
	private Table statsTable;
	private Node statsNode;
	private Table moreStatsTable;
	private Table infoTable;
	private Node infoNode;
	private Table moreInfoTable;

	private boolean tryUpload = false;

	private float lastScrollY;
	
	public StrainPart( Strain strain ) {
		super( "Strain" );
		setStrain( strain );
	}

	@Override
	public void populate() {
		clear();
		marks.clear();
		
		if ( theStrain!=null ) {

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
			icon = new Image( skin.getDrawable( "SyringeIcon" ) );
			if ( Assets.player.reg_discount.equals( theStrain.name ) ) {
				icon.setDrawable( skin.getDrawable( "DiscountIcon" ) );
			}
			imageTable.add( icon );
			descTable = new Table();
			
			ScreenLabel label = new ScreenLabel( new FitLabel( theStrain.name, skin.get( "BasicWhite", LabelStyle.class ) ) ) {
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
			label.table.add( label.label );
			labels.add( label );
			label.table.row();
			
			descLabel = new Label( theStrain.description, skin.get( LabelStyle.class ) );
			descLabel.setAlignment( Align.center );
			descLabel.setWrap( true );
			descTable.add( descLabel );

			imageTable.add( descTable );
			tree.add( new Node( imageTable ) );
			
			if ( Assets.player.reg_discount.equals( theStrain.name ) ) {
				label = new ScreenLabel( new FitLabel( "", skin ) ) {
					int lastTime = 0;
					
					@Override
					public void act( float delta ) {
						if ( lastTime!= Assets.player.reg_discount_timer ) {
							lastTime = Assets.player.reg_discount_timer;
							int minutes = (int) ((float)lastTime/60.0f);
							int seconds = lastTime - minutes * 60;
							label.setText( "%" + ((int)(Assets.player.reg_discount_percent*100.0f)) + " off for " +
							( minutes>0? minutes + "m":"" ) + (seconds + "s!" ) );
						}
					}
					@Override
					public void resize( int width, int height ) {
						width -= treeWidth;
						super.resize( width, height );
						
					}
				};
				label.label.setAlignment( Align.center );
				label.scaleY = 0.75f;
				label.table = new Table();
				label.table.add( label.label );
				labels.add( label );
				tree.add( new Node( label.table ) );
			}
			
			label = new ScreenLabel( new FitLabel( "Owned: " + Assets.player.getNumSyringes( theStrain.name ), skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
					
				}
			};
			label.table = new Table();
			label.table.add( label.label );
			labels.add( label );
			tree.add( new Node( label.table ) );
			
			
			
			// "Buy More" button
			if ( !theStrain.costsCoins ) {
				cost = theStrain.getSyringePrice();
				if ( Assets.player.reg_discount.equals( theStrain.name ) )
					cost = cost - (int)( (float)cost*Assets.player.reg_discount_percent );
				
				button = new ScreenButton( "Buy 1 " + theStrain.name + " $" + ST.withComasAndTwoDecimals( cost ), "CashIcon", "purchase", skin ) {
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
		 				if ( Assets.player.getCash()<cost ) {
		 					Dialogs.acceptDialog( "You do not have enough cash for this purchase" );
		 				} else {
		 					Dialogs.choiceDialog( "Would you like to buy a syringe of " + theStrain.name + 
								" for $" + ST.withComasAndTwoDecimals( cost ) + "?",
								new ChangeListener() {
				 		 			public void changed (ChangeEvent event, Actor actor) {
				 		 				Assets.player.changeCash( -cost );
				 		 				Assets.player.syringes.add( theStrain );
				 		 				populated = false;
				 		 				Assets.getResolver().trackerSendEvent( "Strain", theStrain.name, "Purchase", (long)0 );
				 		 			}
			 					}
		 					);
		 				}
		 			}
		 		});
			} else {
				cost = theStrain.getCoinPrice();
				
				button = new ScreenButton( "Buy 1 " + theStrain.name + " " + cost + " BC", "CoinIcon", "useCoin", skin ) {
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
		 				if ( Assets.player.coins<cost ) {
		 					Dialogs.acceptDialogGetMoreCoins( "You do not have enough coins for this purchase" );
		 				} else {
		 					Dialogs.choiceDialog( "Would you like to buy a syringe of " + theStrain.name + 
								" for " + cost + " BC?",
								new ChangeListener() {
				 		 			public void changed (ChangeEvent event, Actor actor) {
				 		 				Assets.player.changeCoins( -cost );
				 		 				Assets.player.syringes.add( theStrain );
				 		 				populated = false;
				 		 				Assets.getResolver().trackerSendEvent( "Strain", theStrain.name, "Purchase BC", (long)0 );
				 		 			}
			 					}
		 					);
		 				}
		 			}
		 		});
			}
			
			button = new ScreenButton( "Inoculate", "InnoculationIcon", "newColumn", skin ) {
				@Override
				public void act( float delta ) {
					fitButton.setDisabled( !Assets.player.hasSyringe( theStrain.name ) );
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
	 				InoculationPart screen = Assets.getScreenHandler().getScreenPart( InoculationPart.class );
	 				if ( Assets.player.hasSyringe( theStrain.name ) )
	 					screen.setSyringe( theStrain );
	 				else
	 					screen.setSyringe( null );

	 				screen.setDx( 0 );
	 				setDx( -1f );
	 				
	 				Assets.getScreenHandler().setActivePart( screen );
	 			}
	 		});
			
			if ( Assets.player.strains.contains( theStrain ) ) {
				button = new ScreenButton( "Edit Description", "EditDescIcon", "action", skin ) {
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
		 				Dialogs.editDescDialog( "Edit Description", theStrain );
		 			}
		 		});

//				button = new ScreenButton( "Upload Strain to server", "UploadIcon", "action", skin ) {
//					@Override
//					public void act( float delta ) {
//						fitButton.setDisabled( tryUpload );
//					}
//					@Override
//					public void resize( int width, int height ) {
//						width -= treeWidth;
//						super.resize( width, height );
//					}
//				};
//				button.table = new Table();
//				button.table.add( button.fitButton );
//				buttons.add( button );
//				tree.add( new Node( button.table ) );
//
//				button.fitButton.addListener( new ChangeListener() {
//		 			public void changed (ChangeEvent event, Actor actor) {
//		 				Dialogs.choiceDialog( "Would you like to upload " + theStrain.name +
//							" to the server so other Assets.players can see it?",
//							new ChangeListener() {
//			 		 			public void changed (ChangeEvent event, Actor actor) {
//			 		 				HttpAssets.hasUserId();
//			 		 				tryUpload = true;
//
//			 		 			}
//		 					}
//	 					);
//		 			}
//		 		} );
			}
			
//			if ( Assets.player.serverStrains.contains( theStrain ) ) {
//				button = new ScreenButton( "Thumbs Up", "ThumbsUpIcon", "action", skin ) {
//					@Override
//					public void act( float delta ) {
//						fitButton.setDisabled( Assets.player.serverStrainsThumbed.contains( theStrain.id ) );
//					}
//					@Override
//					public void resize( int width, int height ) {
//						width -= treeWidth;
//						super.resize( width, height );
//					}
//				};
//				button.table = new Table();
//				button.table.add( button.fitButton );
//				buttons.add( button );
//				tree.add( new Node( button.table ) );
//
//				button.fitButton.addListener( new ChangeListener() {
//		 			public void changed (ChangeEvent event, Actor actor) {
//		 				HttpAssets.thumbsUp( theStrain );
//		 			}
//		 		});
//			}
			
			
			statsTable = new Table();
			button = new ScreenButton( "Strain Stats", "SyringeIcon", "topLevel", skin ){
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = statsTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			statsNode = new Node( statsTable );
			tree.add( statsNode );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				statsNode.setExpanded( !statsNode.isExpanded() );
	 				focusNode( statsNode );
	 			}
	 		});
			
			moreStatsTable = new Table();
			
			label = new ScreenLabel( new FitLabel( "Gross", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.right );
			label.scaleX = 0.3f;
			label.table = moreStatsTable;
			label.table.add( label.label );
			labels.add( label );

			int gross = theStrain.getGrossRating();
			for ( int i = 1; i <= 10; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMarkStyle.class ) );
				moreStatsTable.add( newMark );
				marks.add( newMark );
				newMark.toggle = i<=gross;
			}
			moreStatsTable.row();
			
			label = new ScreenLabel( new FitLabel( "Speed", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.right );
			label.scaleX = 0.3f;
			label.table = moreStatsTable;
			label.table.add( label.label );
			labels.add( label );
			int speed = theStrain.getSpeedRating();
			for ( int i = 1; i <= 10; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMarkStyle.class ) );
				moreStatsTable.add( newMark );
				marks.add( newMark );
				newMark.toggle = i<=speed;
			}
			moreStatsTable.row();
			
			label = new ScreenLabel( new FitLabel( "Potency", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.right );
			label.scaleX = 0.3f;
			label.table = moreStatsTable;
			label.table.add( label.label );
			labels.add( label );
			int potency = (int) (theStrain.potency/15.0f * 10.0f);
			for ( int i = 1; i <= 10; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMarkStyle.class ) );
				moreStatsTable.add( newMark );
				marks.add( newMark );
				newMark.toggle = i<=potency;
			}
	 		
	 		statsNode.add( new Node( moreStatsTable ) );
	 		
	 		infoTable = new Table();
			button = new ScreenButton( "Qualities", "SyringeIcon", "topLevel", skin ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth;
					super.resize( width, height );
				}
			};
			button.table = infoTable;
			button.table.add( button.fitButton );
			buttons.add( button );
			infoNode = new Node( infoTable );
			tree.add( infoNode );
	
			button.fitButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				infoNode.setExpanded( !infoNode.isExpanded() );
	 				focusNode( infoNode );
	 			}
	 		});
			
			moreInfoTable = new Table();
			
			label = new ScreenLabel( new FitLabel( "Thermostat Needed", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.right );
			label.scaleX = 0.5f;
			label.table = moreInfoTable;
			label.table.add( label.label );
			labels.add( label );
			
			label = new ScreenLabel( new FitLabel( ST.temps[ theStrain.temp_required ], skin.get( "Right", LabelStyle.class ) ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.styleName = "Right";
			label.label.setAlignment( Align.right );
			label.scaleX = 0.5f;
			label.table = moreInfoTable;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();
			
			label = new ScreenLabel( new FitLabel( "Humidity Cost", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.right );
			label.scaleX = 0.5f;
			label.table = moreInfoTable;
			label.table.add( label.label );
			labels.add( label );
			
			label = new ScreenLabel( new FitLabel( "%" + theStrain.humidity_cost, skin.get( "Right", LabelStyle.class ) ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.styleName = "Right";
			label.label.setAlignment( Align.right );
			label.scaleX = 0.5f;
			label.table = moreInfoTable;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();
			
			label = new ScreenLabel( new FitLabel( "Substrate", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.right );
			label.scaleX = 0.5f;
			label.table = moreInfoTable;
			label.table.add( label.label );
			labels.add( label );
			
			label = new ScreenLabel( new FitLabel( ST.substrates[theStrain.substrate], skin.get( "Right", LabelStyle.class ) ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= treeWidth * 2.0f;
					super.resize( width, height );
				}
			};
			label.styleName = "Right";
			label.label.setAlignment( Align.right );
			label.scaleX = 0.5f;
			label.table = moreInfoTable;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();
			
			infoNode.add( new Node( moreInfoTable ) );
			
			
			
			if ( pane!=null )
				lastScrollY = pane.getScrollY();
			pane = new ScrollPane( tree, skin);
			add( pane );
			pane.validate();
			pane.setScrollY( lastScrollY );
			pane.updateVisualScroll();
			pane.addListener( Assets.stopTouchDown() );
		}
	}
	
	@Override
	public void passObject( Object object ) {
		if ( object!=null && object.getClass().equals( Strain.class ) )
			setStrain( (Strain)object );
	}
	
	public void setStrain( Strain strain ) {
		theStrain = strain;
		if ( theStrain!=null ) {
			Alert alert;
			
			if ( theStrain.name.equals( "Business Man" ) ) {
				alert = Assets.hasAlertWithID( ST.ALERT_BUSINESS_MAN );
				if ( alert!=null )
					Assets.removeAlert( alert );
			} else if ( theStrain.name.equals( "Midnight" ) ) {
				alert = Assets.hasAlertWithID( ST.ALERT_MIDNIGHT );
				if ( alert!=null )
					Assets.removeAlert( alert );
			} else if ( theStrain.name.equals( "Midnight" ) ) {
				alert = Assets.hasAlertWithID( ST.ALERT_MIDNIGHT );
				if ( alert!=null )
					Assets.removeAlert( alert );
			}
		}
		
		
		populated = false;
	}
	
	@Override
	public void screenAct( float delta ) {
//		if ( tryUpload ) {
//			int id = HttpAssets.userId;
//			if ( id>=0 ) {
//				HttpAssets.uploadStrain( theStrain );
//				tryUpload = false;
//			} else if ( id==HttpAssets.USER_ERROR ) {
//				Dialogs.acceptDialog( "There was a problem getting/creating a user." );
//				System.out.println( "StrainPart: id was USER_ERROR" );
//				tryUpload = false;
//			}
//		}
	}
	
	@Override
	protected void resize( ) {
		
		super.resize( );
		
		if ( theStrain!=null ) {

			float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
			
			tree.setStyle( skin.get( "small", TreeStyle.class ) );
			treeWidth = tree.getStyle().plus.getMinWidth();
			
			if ( Assets.player.reg_discount.equals( theStrain.name ) ) {
				icon.setDrawable( skin.getDrawable( "DiscountIcon" ) );
			} else
				icon.setDrawable( skin.getDrawable( "SyringeIcon" ) );
			imageTable.getCell( icon )
				.width( buttonHeight )
				.height( buttonHeight );
			
			descLabel.setStyle( skin.get( LabelStyle.class ) );
			descLabel.setWidth( (buttonWidth * 0.9f) - buttonHeight );
			descTable.getCell( descLabel )
				.width( (buttonWidth - treeWidth) - buttonHeight ).height( descLabel.getPrefHeight() );
			
			for ( ToggleMark mark: marks ) {
				mark.setStyle( skin.get( "Star", ToggleMarkStyle.class ) );
				moreStatsTable.getCell( mark )
				.width( (buttonWidth - treeWidth*2.0f ) * 0.7f * 0.1f )
				.height( (buttonWidth - treeWidth*2.0f ) * 0.7f * 0.1f );
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
	
	public static class StrainBuilder implements ScreenPartBuilder<StrainPart> {
		@Override
		public StrainPart build() {
			StrainPart part = new StrainPart( null );
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
