package com.ebsoft.babytoy;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by Endre on 25/03/2017.
 */

public class Scene implements Runnable {

    protected MainActivity mParentActivity;

    public Scene(MainActivity parentActivity) {
        this.mParentActivity = parentActivity;
    }

    protected View findViewById(int viewId) {
        return mParentActivity.findViewById(viewId);
    }

    protected void loadScene(Scene newScene) {
        mParentActivity.loadScene(newScene);
    }

    public void startScene(){};

    public int getID() {return 0;};

    public Typeface getTypeface() {
        return mParentActivity.getTypeface();
    }

    public SharedPreferences getApplicationPreferences()  {
        return mParentActivity.getApplicationPreferences();
    }

    public void setBackPressRunnable(Runnable runnable) {
        mParentActivity.setBackPressRunnable(runnable);
    }

    @Override
    public void run() {}
}
