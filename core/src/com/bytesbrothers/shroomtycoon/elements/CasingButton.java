package com.bytesbrothers.shroomtycoon.elements;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bytesbrothers.shroomtycoon.structures.Casing;

public class CasingButton extends Button {
	public Casing casing;
	public Image icon;
	public Table infoTable;
	public FitLabel nameLabel;
	public ProgressBar progress;
	public ProgressBar maintenance;
	private Skin skin;
	
	public CasingButton ( Casing theCasing, Skin skin ) {
		super( skin );
		this.skin = skin;
		setColor( skin.getColor( "resource") );
		
		icon = new Image( skin.getDrawable( "CasingIcon" ) );
		add( icon );
		
		infoTable = new Table();
		
		nameLabel = new FitLabel( theCasing.name, skin );
		nameLabel.setColor( skin.getColor( "black" ) );
		nameLabel.setAlignment( Align.center );
		infoTable.add( nameLabel );
		infoTable.row();
		
		progress = new ProgressBar( theCasing, skin );
		infoTable.add( progress );
		infoTable.row();
		
		maintenance = new ProgressBar( theCasing.getMaintenanceBar(), skin.get( "orange", ProgressBar.ProgressBarStyle.class ) );
		infoTable.add( maintenance );
		
		add( infoTable );
	}
	
	public void resize( float width, float height ) {
		setStyle( skin.get( ButtonStyle.class ) );
		
		width -= getStyle().up.getLeftWidth() + getStyle().up.getRightWidth();
		height -= getStyle().up.getTopHeight() + getStyle().up.getBottomHeight();
		
		icon.setDrawable( skin.getDrawable( "CasingIcon" ) );
		getCell( icon ).width( height ).height( height );
		
		nameLabel.setStyle( skin.get( "BasicWhite", Label.LabelStyle.class ) );
		infoTable.getCell( nameLabel ).width( width - height ).height( height * 0.3f );
		
		progress.setStyle( skin.get( ProgressBar.ProgressBarStyle.class ) );
		infoTable.getCell( progress ).width( width - height ).height( height * 0.3f );
		
		maintenance.setStyle( skin.get( "orange", ProgressBar.ProgressBarStyle.class ) );
		infoTable.getCell( maintenance ).width( width - height ).height( height * 0.3f );
	}
}