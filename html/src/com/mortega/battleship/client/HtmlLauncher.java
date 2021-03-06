package com.mortega.battleship.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.mortega.battleship.BattleShip;

public class HtmlLauncher extends GwtApplication {
        @Override
        public GwtApplicationConfiguration getConfig() {
                return null;
        }

        @Override
        public ApplicationListener createApplicationListener() {
                return null;
        }

       /* // USE THIS CODE FOR A FIXED SIZE APPLICATION
        // END CODE FOR FIXED SIZE APPLICATION

        // UNCOMMENT THIS CODE FOR A RESIZABLE APPLICATION
        // PADDING is to avoid scrolling in iframes, set to 20 if you have problems
        private static final int PADDING = 0;
        //

        public static void main (String [] args) {
                new GwtApplication(new BattleShip(), getConfig());
        }

        @Override
        public GwtApplicationConfiguration getConfig() {
                int w = Window.getClientWidth() - PADDING;
                int h = Window.getClientHeight() - PADDING;
                GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(w, h);
                Window.enableScrolling(false);
                Window.setMargin("0");
                Window.addResizeHandler(new ResizeHandler() {
                        @Override
                        public void onResize(ResizeEvent event) {
                                int width = event.getWidth() - PADDING;
                                int height = event.getHeight() - PADDING;
                                getRootPanel().setWidth("" + width + "px");
                                getRootPanel().setHeight("" + height + "px");
                                getApplicationListener().resize(width, height);
                                Gdx.graphics.setWindowedMode(width, height);
                        }
                });
                cfg.preferFlash = false;
                return cfg;
        }

        @Override
        public ApplicationListener createApplicationListener() {
                return new BattleShip();
        }
        */
}