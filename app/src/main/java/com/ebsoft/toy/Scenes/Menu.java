package com.ebsoft.toy.Scenes;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebsoft.toy.Dialogs.InfoDialog;
import com.ebsoft.toy.Dialogs.ParentalDialog;
import com.ebsoft.toy.Dialogs.PurchaseDialog;
import com.ebsoft.toy.Dialogs.SettingsDialog;
import com.ebsoft.toy.MainActivity;
import com.ebsoft.toy.R;
import com.ebsoft.toy.util.GooglePlayBilling;

import static com.ebsoft.toy.MainActivity.PREFERENCE_PARENTAL_MODE;

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
                    getApplicationPreferences().edit().putBoolean(PREFERENCE_PARENTAL_MODE, true).commit();
                    loadScene(new Game(mParentActivity));
                    if (getApplicationPreferences().getBoolean(PREFERENCE_PARENTAL_MODE, false)) {
                        String message =mParentActivity. getResources().getString(R.string.dialog_press_back_button);
                        InfoDialog dialog = InfoDialog.newInstance("asd", message, null);
                        dialog.addDismissRunnable(new Runnable() {
                            @Override
                            public void run() {
                                if (!mParentActivity.isAllBoardsAvailable()) {
                                    String message = mParentActivity.getResources().getString(R.string.dialog_ask_for_donation);
                                    InfoDialog dialog = InfoDialog.newInstance("asd", message, null);
                                    dialog.show(mParentActivity.getFragmentManager(), TAG);
                                }
                            }
                        });
                        dialog.show(mParentActivity.getFragmentManager(), TAG);
                    }
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
                        String message = mParentActivity.getResources().getString(R.string.dialog_boards_have_been_unlocked);
                        InfoDialog dialog = InfoDialog.newInstance("asd", message, null);
                        dialog.show(mParentActivity.getFragmentManager(), TAG);
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
                    mParentActivity.getBillingService().purchase(GooglePlayBilling.SKU_ALL_BOARDS);
                }
            };
            PurchaseDialog dialog = PurchaseDialog.newInstance("asd", purchaseRunnable, mParentActivity.getBillingService());
            dialog.show(mParentActivity.getFragmentManager(), TAG);
        }
    };
}
