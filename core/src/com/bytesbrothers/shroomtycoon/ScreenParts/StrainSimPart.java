package com.bytesbrothers.shroomtycoon.ScreenParts;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import com.bytesbrothers.shroomtycoon.Assets;
import com.bytesbrothers.shroomtycoon.ScreenParts.ScreenPartBuilder;
import com.bytesbrothers.shroomtycoon.ScreenParts.main.MainPart;
import com.bytesbrothers.shroomtycoon.elements.Dialogs;
import com.bytesbrothers.shroomtycoon.elements.FitButton;
import com.bytesbrothers.shroomtycoon.elements.FitCheckButton;
import com.bytesbrothers.shroomtycoon.elements.FitCheckButton.FitCheckButtonStyle;
import com.bytesbrothers.shroomtycoon.elements.FitLabel;
import com.bytesbrothers.shroomtycoon.elements.ScreenPart;
import com.bytesbrothers.shroomtycoon.structures.Strain;
import com.bytesbrothers.shroomtycoon.structures.StrainMaster;

public class StrainSimPart extends ScreenPart {

	private ScrollPane pane;
	private Table scrollTable;
	
	private TextField JarSpeedField;
	private TextField CasingSpeedField;
	private TextField FruitSpeedField;
	private TextField FruitWeightField;
	private TextField PinDelayField;
	private TextField TotalBatchWeightField;
	private TextField PotencyField;
	private TextField DollarPerHourField;
	
	private boolean preferredSubstrate = false;
	private boolean soaked = false;
	private boolean prematureCasing = false;
	
	private FitCheckButton subbutton;
	private FitCheckButton soakbutton;
	private FitCheckButton prematurebutton;
	
	private FitLabel timeLabel;
	private FitLabel fruitsLabel;
	private FitLabel grossLabel;
	private FitLabel dphLabel;
	
	public StrainSimPart( ) {
		super( "Strain Sim" );
	}

	@Override
	public void populate() {
		clear();

		skin = Assets.getSkin();
		
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
			
		button = new ScreenButton( "Pick Strain", "SyringeIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				ArrayList<Button> buttons = new ArrayList<Button>();
 				ArrayList<Strain> strains = StrainMaster.getStrains();
				int size = strains.size();
				for ( int index = 0; index<size; index ++ ) {
					final Strain currentStrain = strains.get( index );
					FitButton myButton = new FitButton( currentStrain.name, skin.getRegion( "SyringeIcon" ), skin );
	 				myButton.left();
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				applyStrain( currentStrain );
	 		 			}
	 		 		});
	 				buttons.add( myButton );
				}
				
				for ( int i = 0; i<Assets.player.strains.size(); i ++ ) {
					final Strain currentStrain = Assets.player.strains.get( i );
					FitButton myButton = new FitButton( currentStrain.name, skin.getRegion( "SyringeIcon" ), skin );
	 				myButton.left();
	 				myButton.addListener( new ChangeListener() {
	 		 			public void changed (ChangeEvent event, Actor actor) {
	 		 				applyStrain( currentStrain );
	 		 			}
	 		 		});
	 				buttons.add( myButton );
				}
  				
  				Dialogs.scrollTableDialog( "Please choose a strain:", buttons );
 			}
 		});
		
		ScreenLabel label = new ScreenLabel( new FitLabel( "Jar Time:", skin ) );
		label.scaleX = 0.5f;
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		JarSpeedField = new TextField( JarSpeedField==null?"":JarSpeedField.getText(), skin );
		JarSpeedField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(JarSpeedField);
		scrollTable.row();
		
		label = new ScreenLabel( new FitLabel( "Casing Time:", skin ) );
		label.scaleX = 0.5f;
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		CasingSpeedField = new TextField( CasingSpeedField==null?"":CasingSpeedField.getText(), skin );
		CasingSpeedField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(CasingSpeedField);
		scrollTable.row();
		
		label = new ScreenLabel( new FitLabel( "Fruit Time:", skin ) );
		label.scaleX = 0.5f;
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		FruitSpeedField = new TextField( FruitSpeedField==null?"":FruitSpeedField.getText(), skin );
		FruitSpeedField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(FruitSpeedField);
		scrollTable.row();
		
		label = new ScreenLabel( new FitLabel( "Fruit Weight:", skin ) );
		label.scaleX = 0.5f;
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		FruitWeightField = new TextField( FruitWeightField==null?"":FruitWeightField.getText(), skin );
		FruitWeightField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(FruitWeightField);
		scrollTable.row();
		
		label = new ScreenLabel( new FitLabel( "Pin Delay:", skin ) );
		label.scaleX = 0.5f;
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		PinDelayField = new TextField( PinDelayField==null?"":PinDelayField.getText(), skin );
		PinDelayField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(PinDelayField);
		scrollTable.row();
		
		label = new ScreenLabel( new FitLabel( "Total Batch Weight:", skin ) );
		label.scaleX = 0.5f;
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		TotalBatchWeightField = new TextField( TotalBatchWeightField==null?"":TotalBatchWeightField.getText(), skin );
		TotalBatchWeightField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(TotalBatchWeightField);
		scrollTable.row();
		
		label = new ScreenLabel( new FitLabel( "Potency:", skin ) );
		label.scaleX = 0.5f;
		label.table = scrollTable;
		label.table.add( label.label );
		labels.add( label );
		PotencyField = new TextField( PotencyField==null?"":PotencyField.getText(), skin );
		PotencyField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(PotencyField);
		scrollTable.row();
		
		subbutton = new FitCheckButton( "Preferred Substrate", skin );
		subbutton.setChecked( preferredSubstrate );
		scrollTable.add(subbutton).colspan(2);
		scrollTable.row();
 		subbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				preferredSubstrate = !preferredSubstrate;
 				subbutton.setChecked( preferredSubstrate );
 			}
 		});
 		
 		soakbutton = new FitCheckButton( "Soak Casing", skin );
 		soakbutton.setChecked( soaked );
 		scrollTable.add(soakbutton).colspan(2);
 		scrollTable.row();
 		soakbutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				soaked = !soaked;
 				soakbutton.setChecked( soaked );
 			}
 		});
 		
 		prematurebutton = new FitCheckButton( "Premature Casing", skin );
 		prematurebutton.setChecked( prematureCasing );
 		scrollTable.add(prematurebutton).colspan(2);
 		scrollTable.row();
 		prematurebutton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				prematureCasing = !prematureCasing;
 				prematurebutton.setChecked( prematureCasing );
 			}
 		});
 		
 		button = new ScreenButton( "Calculate", "SideJobIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				float time = Float.parseFloat( JarSpeedField.getText() ) + Float.parseFloat( CasingSpeedField.getText() );
 				
 				float TBW = Float.parseFloat( TotalBatchWeightField.getText() );
 				if ( preferredSubstrate )
 					TBW += Float.parseFloat( TotalBatchWeightField.getText() ) * 0.2f;
 				if ( prematureCasing ) {
 					TBW -= Float.parseFloat( TotalBatchWeightField.getText() ) * 0.5f;
 					time -= Float.parseFloat( CasingSpeedField.getText() ) * 0.5f;
 				}
 				if ( soaked ) {
 					TBW += TBW * 0.5f;
 					time += Float.parseFloat( CasingSpeedField.getText() );
 				}

 				int pinDelay = (int) Float.parseFloat( PinDelayField.getText() );
 				int fruits = 0;
 				while ( TBW>0 ) {
 					// now we're in the FC
 					time += pinDelay;
 					// add a fruit
					fruits ++;
					TBW -= Float.parseFloat( FruitWeightField.getText() );
 				}
 				
 				// allow the very last fruit to mature
 				time += Float.parseFloat( FruitSpeedField.getText() );
 				time /= 60.0f;
 				time /= 60.0f;
 				timeLabel.setText( "Time: " + time + " hours" );
 				fruitsLabel.setText( "Fruits: " + fruits );
 				float gross = fruits * Float.parseFloat( FruitWeightField.getText() ) * Float.parseFloat( PotencyField.getText() );
 				grossLabel.setText( "Gross: " + gross );
 				
 				DollarPerHourField.setText( "" + (gross/time) );
 			}
 		});
		
		// Time
		timeLabel = new FitLabel( timeLabel==null?"Time: ":timeLabel.getText(), skin );
		scrollTable.add(timeLabel).colspan( 2 );
		scrollTable.row();
		
		// fruits
		fruitsLabel = new FitLabel( fruitsLabel==null?"Fruits: ":fruitsLabel.getText(), skin );
		scrollTable.add(fruitsLabel).colspan( 2 );
		scrollTable.row();
		
		// gross
		grossLabel = new FitLabel( grossLabel==null?"Fruits: ":grossLabel.getText(), skin );
		scrollTable.add(grossLabel).colspan( 2 );
		scrollTable.row();
		
		// dollar per hour
		dphLabel = new FitLabel( "$ Per Hour:", skin );
		scrollTable.add(dphLabel);
		DollarPerHourField = new TextField( DollarPerHourField==null?"":DollarPerHourField.getText(), skin );
		DollarPerHourField.addListener( Assets.stopTouchDown() ); // Stops touchDown events from propagating to the FlickScrollPane.
		scrollTable.add(DollarPerHourField);
		scrollTable.row();
		
		button = new ScreenButton( "Generate From DPH", "SideJobIcon", "action", skin );
		button.table = scrollTable;
		button.table.add( button.fitButton ).colspan( 2 );
		button.table.row();
		buttons.add( button );
		button.fitButton.addListener( new ChangeListener() {
 			public void changed (ChangeEvent event, Actor actor) {
 				float dph = Float.parseFloat( DollarPerHourField.getText() );
 				Random random = new Random();
 				float weight = 40.0f + (20.0f - 40.0f * random.nextFloat() ); // 40 plus or minus 20
 				float potency = 11.0f + (5.0f - 10.0f * random.nextFloat() );// 11 plus or minus 5
 				float gross = weight * potency;
 				float time = gross/dph; // in hours
 				float fruit_weight = 0.75f + (0.5f - 1.0f * random.nextFloat() );// 0.75 plus or minus 0.5
 				int num_fruits = (int) (weight/fruit_weight);
 				int pin_delay = 30 + random.nextInt( 60 ); // 60 plus or minus 30
 				
 				timeLabel.setText( "Time: " + time + " hours" );
 				time *= 60.0f;
 				time *= 60.0f; // now its in seconds
 				
 				float time_fruiting = pin_delay * num_fruits; // in seconds
 				float fruit_time = 50*60 + (30*60 - 60*60 * random.nextFloat() ); // (in seconds) 50 minutes plus or minus 30
 				float casing_time = (time - (time_fruiting + fruit_time) ) * (0.25f + 0.5f * random.nextFloat() ); // between %25 and %75 of the time leftover
 				float jar_time = time - (time_fruiting + fruit_time + casing_time);// whatevers left
 				
 				
 				fruitsLabel.setText( "Fruits: " + num_fruits );
 				grossLabel.setText( "Gross: " + gross );
 				
 				JarSpeedField.setText( "" + jar_time );
 				CasingSpeedField.setText( "" + casing_time );
 				FruitSpeedField.setText( "" + fruit_time );
 				FruitWeightField.setText( "" + fruit_weight );
 				PinDelayField.setText( "" + pin_delay );
 				TotalBatchWeightField.setText( "" + weight );
 				PotencyField.setText( "" + potency );
 			}
 		});
		
		
		pane = new ScrollPane( scrollTable, skin);
		add( pane );
	}
	
	@Override
	protected void resize( ) {
		
		super.resize( );
		
		float buttonHeight = Math.max( MIN_BUTTON_HEIGHT, Assets.height * desiredHeight * BUTTON_HEIGHT_MOD );
		float buttonWidth = Math.max( MIN_BUTTON_WIDTH, Assets.width * desiredWidth * BUTTON_WIDTH_MOD );
		
		JarSpeedField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( JarSpeedField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		CasingSpeedField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( CasingSpeedField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		FruitSpeedField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( FruitSpeedField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		FruitWeightField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( FruitWeightField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		PinDelayField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( PinDelayField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		TotalBatchWeightField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( TotalBatchWeightField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		PotencyField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( PotencyField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		timeLabel.setStyle( skin.get( LabelStyle.class ) );
		scrollTable.getCell( timeLabel )
			.width( buttonWidth )
			.height( buttonHeight );
		
		fruitsLabel.setStyle( skin.get( LabelStyle.class ) );
		scrollTable.getCell( fruitsLabel )
			.width( buttonWidth )
			.height( buttonHeight );
		
		grossLabel.setStyle( skin.get( LabelStyle.class ) );
		scrollTable.getCell( grossLabel )
			.width( buttonWidth )
			.height( buttonHeight );
		
		dphLabel.setStyle( skin.get( LabelStyle.class ) );
		scrollTable.getCell( dphLabel )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		DollarPerHourField.setStyle( skin.get( TextFieldStyle.class ) );
		scrollTable.getCell( DollarPerHourField )
			.width( buttonWidth * 0.5f )
			.height( buttonHeight );
		
		prematurebutton.setStyle( skin.get( FitCheckButtonStyle.class ) );
		scrollTable.getCell( prematurebutton )
			.width( buttonWidth )
			.height( buttonHeight );
		
		soakbutton.setStyle( skin.get( FitCheckButtonStyle.class ) );
		scrollTable.getCell( soakbutton )
			.width( buttonWidth )
			.height( buttonHeight );
		
		subbutton.setStyle( skin.get( FitCheckButtonStyle.class ) );
		scrollTable.getCell( subbutton )
			.width( buttonWidth )
			.height( buttonHeight );
		
		
		pane.setStyle( skin.get( ScrollPaneStyle.class ) );
		getCell( pane )
			.width( Assets.width*desiredWidth )
			.height( Assets.height*desiredHeight );
	}
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void back() {
		ScreenPart screen = Assets.getScreenHandler().getScreenPart( MainPart.class );

		screen.setDx( 0 );
		setDx( 1f );
		
		ActionBarPart actionBarPart = Assets.getScreenHandler().getScreenPart( ActionBarPart.class );
		actionBarPart.setDy( 0 );
		
		Assets.getScreenHandler().setActivePart( screen );
	}
	
	public void applyStrain ( Strain newStrain ) {
		JarSpeedField.setText( "" + newStrain.jar_grow_time );
		CasingSpeedField.setText( "" + newStrain.casing_grow_time );
		FruitSpeedField.setText( "" + newStrain.fruit_grow_time );
		FruitWeightField.setText( "" + newStrain.fruit_weight );
		PinDelayField.setText( "" + newStrain.pins_time );
		TotalBatchWeightField.setText( "" + newStrain.batch_weight );
		PotencyField.setText( "" + newStrain.potency );
	}
	
	public static class StrainSimBuilder implements ScreenPartBuilder<StrainSimPart> {
		@Override
		public StrainSimPart build() {
			StrainSimPart part = new StrainSimPart( );
//			ShroomClass game =  Assets.game;
			part.setX( Assets.width * 2.0f );
			part.setY( Assets.height * 0.5f );
			part.desiredX = 1f;
			part.desiredY = 0;
			part.desiredWidth = 1f;
			part.desiredHeight = 1f;
			Assets.game.stage.addActor( part );
			return part;
		}
	}
	
	
}
