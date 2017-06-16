package com.bytesbrothers.shroomtycoon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bytesbrothers.shroomtycoon.ShroomClass;

public class DesktopLauncher {

	public static AppResolverDesktop actionResolver;

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Shroom Tycoon 2";
//		cfg.useGL20 = false;
		cfg.width = 960;
		cfg.height = 660;

		actionResolver = new AppResolverDesktop();
		ShroomClass sClass = new ShroomClass( actionResolver );
		actionResolver.activity = new LwjglApplication( sClass, cfg);
	}
}
