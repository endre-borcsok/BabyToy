package com.ebsoft.babytoy;

import android.app.Activity;

/**
 * Created by Endre on 25/03/2017.
 */

public class SplashScreen extends Scene {

    private final long SPLASHCREEN_TIMEOUT_MILLIS = 2500;
    public static final int SCENE_ID = 0x01;

    public SplashScreen(MainActivity parentActivity) {
        super(parentActivity);
    }

    @Override
    public int getID() {
        return SCENE_ID;
    }

    @Override
    public void startScene() {
        mParentActivity.setContentView(R.layout.layout_splash_screen);
        Thread splashScreen = new Thread(this);
        splashScreen.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(SPLASHCREEN_TIMEOUT_MILLIS);
        } catch (InterruptedException Ignored) {
        } finally {
            loadScene(new Menu(mParentActivity));
        }
    }
}
