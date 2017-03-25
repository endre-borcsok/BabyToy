package com.ebsoft.babytoy;

import android.view.View;

import java.io.Serializable;

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

    @Override
    public void run() {}
}
