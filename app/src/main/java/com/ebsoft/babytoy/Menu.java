package com.ebsoft.babytoy;

import android.view.View;
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

        mMenuTextPlay.setOnClickListener(mPlay);
    }

    private View.OnClickListener mPlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getApplicationPreferences().edit().putBoolean(MainActivity.PREFERENCE_PARENTAL_MODE, true).commit();
            loadScene(new Game(mParentActivity));
        }
    };
}
