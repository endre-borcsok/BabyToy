package com.ebsoft.babytoy;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Endre on 25/03/2017.
 */

public class Menu extends Scene {
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
    }

    private View.OnTouchListener playButtonListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMenuTextPlay.setTextColor(Color.RED);
                    break;
                case MotionEvent.ACTION_UP:
                    getApplicationPreferences().edit().putBoolean(MainActivity.PREFERENCE_PARENTAL_MODE, true).commit();
                    loadScene(new Game(mParentActivity));
                case MotionEvent.ACTION_CANCEL:
                    mMenuTextPlay.setTextColor(Color.WHITE);
                    break;
            }
            return true;
        }
    };
}
