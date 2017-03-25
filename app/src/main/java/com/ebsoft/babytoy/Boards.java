package com.ebsoft.babytoy;

import android.widget.TextView;

/**
 * Created by Endre on 25/03/2017.
 */

public class Boards extends Scene {
    public static final int SCENE_ID = 0x03;
    private TextView mBoardsText;

    public Boards(MainActivity parentActivity) {
        super(parentActivity);
    }

    @Override
    public int getID() {
        return SCENE_ID;
    }

    @Override
    public void startScene() {
        mParentActivity.setContentView(R.layout.layout_boards);
        mBoardsText = (TextView) findViewById(R.id.boardsText);
    }
}
