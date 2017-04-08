package com.ebsoft.babytoy.Scenes;

import android.content.IntentSender;
import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebsoft.babytoy.Dialogs.ParentalDialog;
import com.ebsoft.babytoy.Dialogs.PurchaseDialog;
import com.ebsoft.babytoy.Dialogs.SettingsDialog;
import com.ebsoft.babytoy.MainActivity;
import com.ebsoft.babytoy.Purchases;
import com.ebsoft.babytoy.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Endre on 25/03/2017.
 */

public class Menu extends Scene {
    private final String TAG = Menu.class.getSimpleName();
    public static final int SCENE_ID = 0x02;

    private TextView mMenuTextPlay = null;
    private TextView mMenuTextMoreBoards = null;
    private TextView mMenuTextOptions = null;

    public Menu(MainActivity parentActivity) {
        super(parentActivity);
    }

    @Override
    public int getID() {
        return SCENE_ID;
    }

    @Override
    public void startScene() {
        mParentActivity.setContentView(R.layout.layout_menu);
        mMenuTextPlay = (TextView) findViewById(R.id.textPlay);
        mMenuTextMoreBoards = (TextView) findViewById(R.id.textMoreBoards);
        mMenuTextOptions = (TextView) findViewById(R.id.textOptions);

        mMenuTextPlay.setTypeface(getTypeface());
        mMenuTextMoreBoards.setTypeface(getTypeface());
        mMenuTextOptions.setTypeface(getTypeface());

        RelativeLayout playButtonHolder = (RelativeLayout) findViewById(R.id.playButtonHolder);
        playButtonHolder.setOnTouchListener(playButtonListener);

        RelativeLayout moreBoardsHolder = (RelativeLayout) findViewById(R.id.moreBoardsHolder);
        moreBoardsHolder.setOnTouchListener(moreBoardsButtonListener);

        RelativeLayout settingsButtonHolder = (RelativeLayout) findViewById(R.id.settingsButtonHolder);
        settingsButtonHolder.setOnTouchListener(settingsButtonListener);
    }

    private View.OnTouchListener playButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMenuTextPlay.setTextColor(Color.RED);
                    vibrate();
                    break;
                case MotionEvent.ACTION_UP:
                    getApplicationPreferences().edit().putBoolean(MainActivity.PREFERENCE_PARENTAL_MODE, true).commit();
                    loadScene(new Game(mParentActivity));
                    Toast.makeText(mParentActivity, "Screen lock activated! Press the \"Back\" button to disable locked screen mode!", Toast.LENGTH_LONG).show();
                case MotionEvent.ACTION_CANCEL:
                    mMenuTextPlay.setTextColor(Color.WHITE);
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener moreBoardsButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMenuTextMoreBoards.setTextColor(Color.RED);
                    vibrate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mParentActivity.isAllBoardsAvailable()) {
                        ParentalDialog dialog = ParentalDialog.newInstance("asd", mOnMoreBoardsDialogAccepted);
                        dialog.show(mParentActivity.getFragmentManager(), TAG);
                    } else {
                        //TODO say thank you
                    }
                case MotionEvent.ACTION_CANCEL:
                    mMenuTextMoreBoards.setTextColor(Color.WHITE);
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener settingsButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMenuTextOptions.setTextColor(Color.RED);
                    vibrate();
                    break;
                case MotionEvent.ACTION_UP:
                    SettingsDialog settingsDialog = SettingsDialog.newInstance("title", null, getApplicationPreferences());
                    settingsDialog.show(mParentActivity.getFragmentManager(), TAG);
                case MotionEvent.ACTION_CANCEL:
                    mMenuTextOptions.setTextColor(Color.WHITE);
                    break;
            }
            return true;
        }
    };

    private Runnable mOnMoreBoardsDialogAccepted = new Runnable() {
        @Override
        public void run() {
            Runnable purchaseRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Purchases purchase = new Purchases(mParentActivity.getBillingService());
                        purchase.purchase(mParentActivity, Purchases.SKU_ALL_BOARDS);
                    } catch (RemoteException e) {
                        Log.e(TAG, e.toString());
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            };
            PurchaseDialog dialog = PurchaseDialog.newInstance("asd", purchaseRunnable, mParentActivity.getBillingService());
            dialog.show(mParentActivity.getFragmentManager(), TAG);
        }
    };
}
