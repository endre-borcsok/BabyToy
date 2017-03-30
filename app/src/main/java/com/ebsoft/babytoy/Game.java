package com.ebsoft.babytoy;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebsoft.babytoy.Boards.Animals;
import com.ebsoft.babytoy.Boards.BoardElement;

import java.util.ArrayList;

/**
 * Created by Endre on 25/03/2017.
 */

public class Game extends Scene {
    public static final int SCENE_ID = 0x03;
    private final String TAG = Game.class.getSimpleName();
    private final int BOARD_ELEMENT_COUNT = 8;
    private final int BOARD_ROW_COUNT = 4;
    private final int BOARD_COLUMN_COUNT = 2;

    private LayoutInflater mInflater = null;
    private GridLayout mBoard;
    private TextView mCategory;
    private TextView mPrevious;
    private TextView mNext;
    private SoundPool mSoundPool;
    private View mDecorView;

    private boolean mBoardInitialised = false;

    public Game(MainActivity parentActivity) {
        super(parentActivity);
    }

    @Override
    public int getID() {
        return SCENE_ID;
    }

    @Override
    public void startScene() {
        mParentActivity.setContentView(R.layout.layout_boards);
        mInflater = LayoutInflater.from(mParentActivity);
        mBoard = (GridLayout) findViewById(R.id.board);
        mCategory = (TextView) findViewById(R.id.textCategory);
        mPrevious = (TextView) findViewById(R.id.textPrevious);
        mNext = (TextView) findViewById(R.id.textNext);
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        mCategory.setTypeface(getTypeface());
        mPrevious.setTypeface(getTypeface());
        mNext.setTypeface(getTypeface());

        Animals animals = new Animals();
        initBoard(animals.getElements());
        setBackPressRunnable(mBackPressRunnable);

        mDecorView = mParentActivity.getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0) {
                            Animals animals = new Animals();
                            initBoard(animals.getElements());
                        }
                    }
        });
    }

    private void initBoard(final ArrayList<BoardElement> boardElementList) {
        mBoardInitialised = false;
        mBoard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, "layout");
                if (!mBoardInitialised) {
                    mBoardInitialised = true;

                    int width = mBoard.getWidth() / BOARD_COLUMN_COUNT;
                    int height = mBoard.getHeight() / BOARD_ROW_COUNT;

                    for (int i = 0; i < BOARD_ELEMENT_COUNT; i++) {
                        View mBoardElement = mInflater.inflate(R.layout.layout_board_element, null);
                        int soundId = mSoundPool.load(mParentActivity, boardElementList.get(i).soundID, 1);
                        ImageView elementImage = (ImageView) mBoardElement.findViewById(R.id.elementImage);
                        RelativeLayout.LayoutParams elementParams = new RelativeLayout.LayoutParams(width, height);
                        mBoardElement.setLayoutParams(elementParams);
                        elementImage.setImageResource(boardElementList.get(i).imageID);
                        addTouchListener(mBoardElement);
                        mBoardElement.setTag(soundId);
                        mBoard.addView(mBoardElement, i);
                    }
                }
            }
        });
    }

    private void addTouchListener(final View v) {
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        vibrate();
                        ImageView frame = (ImageView) v.findViewById(R.id.frame);
                        frame.setImageResource(R.drawable.board_element_red);
                        mSoundPool.play((int) v.getTag(), 1, 1, 0, 0, 1);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ImageView frame1 = (ImageView) v.findViewById(R.id.frame);
                        frame1.setImageResource(R.drawable.board_element_green);
                        break;
                }
                return true;
            }
        });
    }

    Runnable mBackPressRunnable = new Runnable() {
        @Override
        public void run() {
            ParentalDialog dialog = ParentalDialog.newInstance("asd", new Runnable() {
                @Override
                public void run() {
                    mDecorView.setOnSystemUiVisibilityChangeListener(null);
                    getApplicationPreferences().edit().putBoolean(MainActivity.PREFERENCE_PARENTAL_MODE, false).commit();
                    setBackPressRunnable(null);
                    loadScene(new Menu(mParentActivity));
                }
            });
            dialog.show(mParentActivity.getFragmentManager(), TAG);
        }
    };
}
