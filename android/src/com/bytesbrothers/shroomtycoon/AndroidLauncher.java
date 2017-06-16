package com.bytesbrothers.shroomtycoon;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bytesbrothers.shroomtycoon.ShroomClass;

public class AndroidLauncher extends AndroidApplication {

	public AppResolverAndroid appResolver;
	ShroomClass game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		appResolver = new AppResolverAndroid();
		appResolver.activity = this;
		game = new ShroomClass( appResolver );

		initialize(game, config);
	}
}
