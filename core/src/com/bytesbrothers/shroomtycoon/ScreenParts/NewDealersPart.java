package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.elements.ToggleMark;
import com.bytesbrothers.shroomtycoon.structures.Dealer;

public class NewDealersPart extends ScreenPart {

	private ScrollPane pane;
	private Table scrollTable;
	ArrayList<Dealer> newDealers;
	ArrayList<NewDealerButton> dealerButtons = new ArrayList<NewDealerButton>();
	float backgroundWidth = 1;
	
	public NewDealersPart( ArrayList<Dealer> newDealers ) {
		super( "New Dealers" );
	}

	@Override
	public void populate() {
		clear();
		dealerButtons.clear();
		if ( newDealers!=null ) {

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
			
			ScreenLabel label = new ScreenLabel( new FitLabel( "Please select a new dealer:", skin ) );
			label.label.setAlignment( Align.center );
			label.table = scrollTable;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();
			
			if ( newDealers.size()==0 )
				label.label.setText( "No new dealers found." );
			
			for ( final Dealer dealer: newDealers ) {
				NewDealerButton newButton = new NewDealerButton( dealer, skin );
				dealerButtons.add( newButton );
				scrollTable.add( newButton );
				scrollTable.row();
				newButton.addListener( new ChangeListener() {
		 			public void changed (ChangeEvent event, Actor actor) {
		 				Dialogs.choiceDialog( "Hire " + dealer.name + "?",
	 						new ChangeListener() {
	 		 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 		 			// if the player has an objective
	 		 		 				if ( Assets.player.objective!=null &&
	 		 		 						Assets.player.objective.type==Assets.player.objective.HIRE ) {
	 		 		 					Dialogs.objectiveDialog( "You successfully hired " + dealer.name + "!" );
	 		 		 					Assets.player.objective = null;
	 		 							if ( Assets.player.objective_alert != null ) {
	 		 								Assets.removeAlert( Assets.player.objective_alert );
	 		 								Assets.player.objective_alert = null;
	 		 							}
	 		 		 				}
	 		 		 				Assets.player.dealers.add( dealer );
	 		 		 				Assets.player.totalDealersHired += 1;
	 		 		 				Assets.getResolver().trackerSendEvent( "Objective", "Completed", "Hire Dealer", (long)0 );
	 		 		 				Assets.getResolver().unlockAchievement( "CgkItKHH-4IEEAIQBQ" );
	 	 		 					back();
	 		 		 			}
	 	 					}
	 					);
		 			}
		 		});
			}
			
			pane = new ScrollPane( scrollTable, skin);
			add( pane );
			
		}
	}

	@Override
	protected void resize( ) {
		
		super.resize( );
		
		if ( newDealers!=null ) {

			float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
			float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );

			for( NewDealerButton button: dealerButtons ) {
				button.setStyle( skin.get( ButtonStyle.class ) );
				button.resize( buttonWidth, buttonHeight );
			}
			
			pane.setStyle( skin.get( ScrollPaneStyle.class ) );
			getCell( pane )
				.width( Assets.width*desiredWidth )
				.height( Assets.height*desiredHeight );
		}
	}
	
//	@Override
//	public void passObject( Object object ) {
//		if ( object!=null && object.getClass().equals( ArrayList<Dealer>.class ) )
//			setDealers( (ArrayList<Dealer>)object );
//	}
	
	public void setDealers( ArrayList<Dealer> dealers ) {
		newDealers = dealers;
		populated = false;
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
	
	public class NewDealerButton extends Button {
		public Dealer dealer;
		public Image image;
//		public FitLabel nameLabel;
		public Table statsTable;
//		public FitLabel speedLabel;
//		public FitLabel capacityLabel;
//		public FitLabel cutLabel;
		public ArrayList<ToggleMark> marks = new ArrayList<ToggleMark>();
		
		public NewDealerButton( Dealer newDealer, Skin skin ) {
			super( skin.get( ButtonStyle.class ) );
			dealer = newDealer;
			setColor( skin.getColor( "action" ) );
			
			image = new Image( skin.getDrawable( "DealerIcon" ) );
			image.setScaling( Scaling.fillX );
			add( image );
			
			ScreenLabel label = new ScreenLabel( new FitLabel( dealer.name, skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= height + backgroundWidth;
					super.resize( width, height );
				}
			};
			label.label.setAlignment( Align.center );
			label.table = this;
			label.table.add( label.label );
			labels.add( label );
			label.table.row();

			statsTable = new Table();
			
			label = new ScreenLabel( new FitLabel( "Speed", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= backgroundWidth;
					super.resize( width, height );
				}
			};
			label.scaleX = 0.3f;
			label.label.setAlignment( Align.right );
			label.table = statsTable;
			label.table.add( label.label );
			labels.add( label );

			for ( int i = 1; i <= 3; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.add( newMark );
				marks.add( newMark );
				newMark.toggle = i<=dealer.speed_level;
			}

			statsTable.row();
			
			label = new ScreenLabel( new FitLabel( "Capacity", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= backgroundWidth;
					super.resize( width, height );
				}
			};
			label.scaleX = 0.3f;
			label.label.setAlignment( Align.right );
			label.table = statsTable;
			label.table.add( label.label );
			labels.add( label );

			for ( int i = 1; i <= 3; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.add( newMark );
				marks.add( newMark );
				newMark.toggle = i<=dealer.capacity_level;
			}

			statsTable.row();
			
			label = new ScreenLabel( new FitLabel( "Overhead", skin ) ) {
				@Override
				public void resize( int width, int height ) {
					width -= backgroundWidth;
					super.resize( width, height );
				}
			};
			label.scaleX = 0.3f;
			label.label.setAlignment( Align.right );
			label.table = statsTable;
			label.table.add( label.label );
			labels.add( label );

			for ( int i = 1; i <= 3; i ++ ) {
				ToggleMark newMark = new ToggleMark( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.add( newMark );
				marks.add( newMark );
				newMark.toggle = i<=dealer.cut_level;
			}

			add( statsTable ).colspan( 2 );
		}
		
		public void resize( float width, float height ) {
			setStyle( skin.get( ButtonStyle.class ) );
			backgroundWidth = getStyle().up.getLeftWidth() + getStyle().up.getRightWidth();
			
			image.setDrawable( skin.getDrawable( "DealerIcon" ) );
			getCell( image ).width( height ).height( height );

			float statMarkSize = Math.max( height*0.8f, width*0.1f );
			
			for ( ToggleMark mark: marks ) {
				mark.setStyle( skin.get( "Star", ToggleMark.ToggleMarkStyle.class ) );
				statsTable.getCell( mark ).width( statMarkSize ).height( statMarkSize );
			}
		}
		
	}
	
	public static class NewDealersBuilder implements ScreenPartBuilder<NewDealersPart> {
		@Override
		public NewDealersPart build() {
			NewDealersPart part = new NewDealersPart( null );
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
