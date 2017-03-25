package com.ebsoft.babytoy;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Endre on 25/03/2017.
 */

public class MainActivity extends Activity {

    private final String TAG = MainActivity.class.getSimpleName();
    private String mSavedLastScenePath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedLastScenePath = getFilesDir().getAbsolutePath() + "/lastScene.obj";
        //Return to the previous scene.
        int savedSceneID = getLastScene();
        if (savedSceneID == 0) {
            Log.d(TAG, "Couldn't find previous screen. Starting splash screen");
            loadScene(new SplashScreen(this));
        } else {
            Log.d(TAG, "Found previous screen. Loading saved screen.");
            loadScene(getSceneByID(savedSceneID));
        }
    }

    public void loadScene(final Scene newScene) {
        saveScene(newScene);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newScene.startScene();
            }
        });
    }

    private Scene getSceneByID(int ID) {
        if (ID == SplashScreen.SCENE_ID) {
            return new SplashScreen(this);
        } else if (ID == Menu.SCENE_ID) {
            return new Menu(this);
        } else if (ID == Boards.SCENE_ID) {
            return new Boards(this);
        } else {
            return new SplashScreen(this);
        }
    }

    private void saveScene(Scene scene) {
        FileOutputStream fos = null;
        try {
            Log.d(TAG, "Saving scene to: " + mSavedLastScenePath);
            fos = new FileOutputStream(mSavedLastScenePath);
            try {
                byte sceneID = (byte) scene.getID();
                fos.write(sceneID);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            } finally {
                Utils.closeStream(fos);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    private int getLastScene() {
        FileInputStream fis = null;
        int savedSceneID = 0;
        try {
            Log.d(TAG, "Loading scene from: " + mSavedLastScenePath);
            fis = new FileInputStream(mSavedLastScenePath);
            byte[] rawSavedSceneID = new byte[1];
            fis.read(rawSavedSceneID);
            savedSceneID = rawSavedSceneID[0];
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            Utils.closeStream(fis);
            return savedSceneID;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}


