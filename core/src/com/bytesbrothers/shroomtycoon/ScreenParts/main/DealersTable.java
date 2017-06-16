package com.bytesbrothers.shroomtycoon.ScreenParts.main;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.DealerPart;
import com.bytesbrothers.shroomtycoon.ScreenParts.NewDealersPart;
import com.bytesbrothers.shroomtycoon.ShroomClass;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ProgressBar;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Dealer;
import com.bytesbrothers.shroomtycoon.structures.Project;

public class DealersTable extends Table {

	private Skin skin;
	ShroomClass game;

	ArrayList<DealerButton> dealerButtons = new ArrayList<DealerButton>();
	private FitButton newDealersButton;
	
	public class DealerButton extends Button {
		Dealer dealer;
		Image image;
		Table table;
		FitLabel nameLabel;
		ProgressBar progressBar;
		
		public DealerButton( Dealer dealer, ButtonStyle style ) {
			super( style );
			this.dealer = dealer;
			setColor( skin.getColor( "newColumn" ) );
			image = new Image( skin.getDrawable( "DealerIcon" ) );
			image.setScaling( Scaling.fillX );
			add( image );
			
			table = new Table();
			nameLabel = new FitLabel( dealer.name, skin.get("BasicWhite", Label.LabelStyle.class ) );
//			nameLabel.setColor( skin.getColor( "royalBlue" ) );
			table.add(nameLabel);
			table.row();
			progressBar = new ProgressBar( dealer, skin );
			table.add(progressBar);
			
			add( table );
		}
		
		public void resize( float width, float height ) {
			width -= getStyle().up.getLeftWidth() + getStyle().up.getRightWidth();
			height -= getStyle().up.getTopHeight() + getStyle().up.getBottomHeight();
			
			image.setDrawable(  skin.getDrawable( "DealerIcon" ) );
			getCell( image ).width( height ).height( height );
			
			nameLabel.setStyle( skin.get( "BasicWhite", Label.LabelStyle.class ) );
			table.getCell( nameLabel ).width( width - height ).height( height * 0.5f );
			
			progressBar.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
			table.getCell( progressBar ).width( width - height ).height( height * 0.5f );
		}
		
	}
	
	public DealersTable ( ShroomClass theGame ) {
		game = theGame;
		skin = Assets.getSkin();
		populate();
	}

	public void populate() {
		
		clear();
		dealerButtons.clear();
		newDealersButton = null;
		ArrayList<Dealer> dealers = Assets.player.dealers;
		
		for ( final Dealer dealer: dealers ) {
			DealerButton dealerButton = new DealerButton( dealer, skin.get( ButtonStyle.class ) );
			
			add( dealerButton );
	 		dealerButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				ShroomClass game = Assets.game;
	 				game.screenHandler.setActivePartDx( -1f );
	 				
	 				ScreenPart screen = game.screenHandler.getScreenPart( DealerPart.class );
	 				((DealerPart)screen).setDealer( dealer );
	 				screen.setDx( 0 );
	 				
	 				game.screenHandler.setActivePart( screen );
	 			}
	 		});
	 		row();
	 		dealerButtons.add( dealerButton );
		}
		
		if ( dealers.size() < Assets.player.getMaxDealers() ) {
			final Sprite icon = Assets.getAtlas().createSprite( "AddDealerIcon" );
			newDealersButton = new FitButton( "Find New Dealers", icon, skin );
			newDealersButton.setColor( skin.getColor( "project" ) );
			newDealersButton.addListener( new ChangeListener() {
	 			public void changed (ChangeEvent event, Actor actor) {
	 				Project newProject = new Project( "Find Dealers", "AddDealerIcon" );
					newProject.message = "New dealers have been found. Please choose one.";
					ArrayList<Dealer> newDealers = new ArrayList<Dealer>();
					Random random = new Random();
					int num = 1 + random.nextInt(5);
					
					int max = Assets.player.getMaxDealerRating();
					int min = Math.max( max - 3, 0 );
					for ( int i = 0; i <num; i ++ ) {
						Dealer dealer = new Dealer( min + random.nextInt( max+1-min ), game );
						newDealers.add( dealer );
					}
					newProject.nextPart = Assets.getScreenHandler().getScreenPart( NewDealersPart.class );
					((NewDealersPart)newProject.nextPart).setDealers( newDealers );
					Assets.game.startProjectPart( newProject );
//					
//					newProject.nextColumn = new NewDealersColumn( newDealers );
//	
//					game.addTable( new ProjectColumn( newProject ) );
	 			}
	 		});
			add( newDealersButton );
		}
	}

	public void resize( float width, float height ) {
		skin = Assets.getSkin();

		if ( dealerButtons.size()!=Assets.player.dealers.size() )
			populate();
		
		for ( DealerButton myButton: dealerButtons ) {
			myButton.setStyle( skin.get( ButtonStyle.class ) );
			myButton.resize( width, height );
			getCell( myButton ).width( width ).height( height ).padBottom( height * 0.05f );
		}
		
		if ( newDealersButton!=null ) {
			newDealersButton.image.setDrawable( skin.getDrawable( "AddDealerIcon" ) );
			newDealersButton.setStyle( skin.get( FitButton.FitButtonStyle.class ) );
			getCell( newDealersButton ).width( width ).height( height ).padBottom( height * 0.05f );
		}
	}
}
