package com.ebsoft.babytoy.Scenes;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebsoft.babytoy.Boards.Farm;
import com.ebsoft.babytoy.Boards.Board;
import com.ebsoft.babytoy.Boards.BoardElement;
import com.ebsoft.babytoy.Boards.Music;
import com.ebsoft.babytoy.Boards.Jungle;
import com.ebsoft.babytoy.Dialogs.ParentalDialog;
import com.ebsoft.babytoy.MainActivity;
import com.ebsoft.babytoy.R;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by Endre on 25/03/2017.
 */

public class Game extends Scene {
    public static final int SCENE_ID = 0x03;
    private final String LAST_BOARD_KEY = "LastBoardKey";
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
    private String mCurrentBoard;
    private int mCurrentBoardIndex = 0;
    private ArrayList<Board> mBoardList;

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
        mPrevious.setOnTouchListener(mTouchPrevious);
        mNext.setTypeface(getTypeface());
        mNext.setOnTouchListener(mTouchNext);

        initBoardList();
        initBoard(getLastBoard());
        setBackPressRunnable(mBackPressRunnable);

        mDecorView = mParentActivity.getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) != 0) {
                            initBoard(getLastBoard());
                        }
                    }
        });
    }

    private void initBoard(Board board) {
        mBoardInitialised = false;
        saveBoard(board);
        mBoard.requestLayout();
        mBoard.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!mBoardInitialised) {
                    Log.d(TAG, "layout");
                    mBoardInitialised = true;

                    Board board = getLastBoard();
                    mCategory.setText(board.getBoardName());
                    ArrayList<BoardElement> boardElementList = board.getElements();

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

                    if (mCurrentBoardIndex == 0) {
                        mPrevious.setVisibility(GONE);
                    } else {
                        mPrevious.setVisibility(View.VISIBLE);
                    }

                    if (mCurrentBoardIndex == mBoardList.size()-1) {
                        mNext.setVisibility(GONE);
                    } else {
                        mNext.setVisibility(View.VISIBLE);
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

    private void initBoardList() {
        mBoardList = new ArrayList<>();
        mBoardList.add(new Farm());
        mBoardList.add(new Music());
        mBoardList.add(new Jungle());
    }

    private Board getLastBoard() {
        mCurrentBoard = getApplicationPreferences().getString(LAST_BOARD_KEY, Farm.BOARD_ANIMALS);

        for (int i = 0;i < mBoardList.size(); i++) {
            if (mCurrentBoard.equalsIgnoreCase(mBoardList.get(i).getBoardName())) {
                mCurrentBoardIndex = i;
            }
        }

        if (mCurrentBoard.equalsIgnoreCase(Farm.BOARD_ANIMALS)) {
            return new Farm();
        } else if (mCurrentBoard.equalsIgnoreCase(Music.BOARD_INSTRUMENTS)) {
            return new Music();
        } else if (mCurrentBoard.equalsIgnoreCase(Jungle.BOARD_JUNGLE)) {
            return new Jungle();
        } else {
            return new Farm();
        }
    }

    private void getNextBoard() {
        if (mCurrentBoardIndex + 1 <= mBoardList.size()-1) {
            ++mCurrentBoardIndex;
            initBoard(mBoardList.get(mCurrentBoardIndex));
        }
    }

    private void getPreviousBoard() {
        if (mCurrentBoardIndex >= 0) {
            --mCurrentBoardIndex;
            initBoard(mBoardList.get(mCurrentBoardIndex));
        }
    }

    private void saveBoard(Board currentBoard) {
        getApplicationPreferences().edit().putString(LAST_BOARD_KEY, currentBoard.getBoardName()).commit();
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

    private View.OnTouchListener mTouchPrevious = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    vibrate();
                    mPrevious.setTextColor(Color.RED);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    getPreviousBoard();
                    mPrevious.setTextColor(Color.WHITE);
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener mTouchNext = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    vibrate();
                    mNext.setTextColor(Color.RED);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    getNextBoard();
                    mNext.setTextColor(Color.WHITE);
                    break;
            }
            return true;
        }
    };
}
