package com.ebsoft.babytoy;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Endre on 25/03/2017.
 */

public class Menu extends Scene {
    public static final int SCENE_ID = 0x02;
    TextView mStartButton;

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
        mStartButton = (TextView) findViewById(R.id.start);
        mStartButton.setOnClickListener(mStart);
    }

    private View.OnClickListener mStart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            loadScene(new Boards(mParentActivity));
        }
    };
}
