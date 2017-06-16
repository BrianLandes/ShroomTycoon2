package com.bytesbrothers.shroomtycoon.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.bytesbrothers.shroomtycoon.ShroomClass;

public class HtmlLauncher extends GwtApplication {

        AppResolverHtml appResolver;

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                appResolver = new AppResolverHtml();
                return new ShroomClass(appResolver);
        }
}