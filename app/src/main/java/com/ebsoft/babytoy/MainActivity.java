package com.ebsoft.babytoy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Endre on 25/03/2017.
 */

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String PREFERENCE_PARENTAL_MODE = "ParentalMode";

    private String mSavedLastScenePath = null;
    private Typeface mTypeFace = null;
    private SharedPreferences mSharedPreferences = null;
    private Runnable mBackPressRunnalbe = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSavedLastScenePath = getFilesDir().getAbsolutePath() + "/lastScene.obj";
        mTypeFace = Typeface.createFromAsset(getAssets(),"fonts/Chewy.ttf");
        mSharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);

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
        } else if (ID == Game.SCENE_ID) {
            return new Game(this);
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
        if (mSharedPreferences.getBoolean(PREFERENCE_PARENTAL_MODE, false)){
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
        } else {
            return savedSceneID;
        }
    }

    protected Typeface getTypeface() {
        return mTypeFace;
    }

    protected SharedPreferences getApplicationPreferences() {
        return mSharedPreferences;
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setBackPressRunnable(Runnable runnable) {
        mBackPressRunnalbe = runnable;
    }

    @Override
    public void onBackPressed() {
        if (mBackPressRunnalbe == null) {
            super.onBackPressed();
        } else {
            mBackPressRunnalbe.run();
        }
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


