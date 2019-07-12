package com.ebsoft.toy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.android.vending.billing.IInAppBillingService;
import com.ebsoft.toy.Dialogs.InfoDialog;
import com.ebsoft.toy.Dialogs.SettingsDialog;
import com.ebsoft.toy.Scenes.Game;
import com.ebsoft.toy.Scenes.Menu;
import com.ebsoft.toy.Scenes.Scene;
import com.ebsoft.toy.Scenes.SplashScreen;

import net.hockeyapp.android.CrashManager;

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
    private Runnable mBackPressRunnable = null;
    private Vibrator mVibrator;
    private Purchases mPurchases;

    private boolean mAllBoardsAvailable = false;

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            mPurchases = new Purchases(mService);

            try {
                mAllBoardsAvailable = mPurchases.hasProductPurchased(MainActivity.this, Purchases.SKU_ALL_BOARDS, new Purchases.OnPurchaseEventListener() {
                    @Override
                    public void onError(String error) {
                        String errorMessage = getResources().getString(R.string.dialog_error) + " " + error;
                        InfoDialog dialog = InfoDialog.newInstance("asd", errorMessage, null);
                        dialog.show(getFragmentManager(), TAG);
                    }
                });
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

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

    public void vibrate() {
        if (mSharedPreferences.getBoolean(SettingsDialog.PREFERENCE_VIBRATION, true)) {
            if (mVibrator == null) {
                mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }
            mVibrator.vibrate(100);
        }
    }

    public boolean isAllBoardsAvailable() {
        return mAllBoardsAvailable;
    }

    public IInAppBillingService getBillingService() {
        return mService;
    }

    public Typeface getTypeface() {
        return mTypeFace;
    }

    public SharedPreferences getApplicationPreferences() {
        return mSharedPreferences;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCrashes();
        if (mSharedPreferences.getBoolean(PREFERENCE_PARENTAL_MODE, false)) {
            String message = getResources().getString(R.string.dialog_press_back_button);
            InfoDialog dialog = InfoDialog.newInstance("asd", message, null);
            dialog.addDismissRunnable(new Runnable() {
                @Override
                public void run() {
                    if (!isAllBoardsAvailable()) {
                        String message = getResources().getString(R.string.dialog_ask_for_donation);
                        InfoDialog dialog = InfoDialog.newInstance("asd", message, null);
                        dialog.show(getFragmentManager(), TAG);
                    }
                }
            });
            dialog.show(getFragmentManager(), TAG);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSharedPreferences.getBoolean(PREFERENCE_PARENTAL_MODE, false)) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void setBackPressRunnable(Runnable runnable) {
        mBackPressRunnable = runnable;
    }

    @Override
    public void onBackPressed() {
        if (mBackPressRunnable == null) {
            super.onBackPressed();
        } else {
            mBackPressRunnable.run();
        }
    }

    private void checkForCrashes() {
        //CrashManager.register(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            if (resultCode == RESULT_OK) {
                String errorMessage = getResources().getString(R.string.dialog_thanks_for_purchase);
                InfoDialog dialog = InfoDialog.newInstance("asd", errorMessage, null);
                dialog.show(getFragmentManager(), TAG);
            } else if (resultCode == RESULT_CANCELED) {
                //TODO Dialog box dismissed by the user. No action needed.
            }
        }
    }
}


