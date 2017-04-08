package com.ebsoft.babytoy;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
                    ParentalDialog parentalDialog = new ParentalDialog();
                    parentalDialog.show(mParentActivity.getFragmentManager(), TAG);
                    parentalDialog.addOnCompletedRunnable(mOnMoreBoardsDialogAccepted);
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

        }
    };
}
