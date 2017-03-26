package com.ebsoft.babytoy;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Endre on 26/03/2017.
 */

public class ParentalDialog extends DialogFragment {

    private Typeface mTypeFace = null;
    private TextView mQuestionText;
    private TextView mMathsText;
    private TextView mSolutionText;
    private ImageView mPositive;
    private ImageView mNegative;
    private ImageView mAccept;
    private Runnable mOnDialogCompleted;

    private int mNumberOne;
    private int mNumberTwo;
    private int mSelectedNumber = 0;

    public static ParentalDialog newInstance(String title, Runnable onDialogCompleted) {
        ParentalDialog frag = new ParentalDialog();
        frag.addOnDialogCompletednTask(onDialogCompleted);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void addOnDialogCompletednTask(Runnable onDialogCompleted) {
        this.mOnDialogCompleted = onDialogCompleted;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.layout_parental_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTypeFace = Typeface.createFromAsset(getDialog().getContext().getAssets(),"fonts/Chewy.ttf");

        mQuestionText = (TextView) view.findViewById(R.id.textQuestion);
        mMathsText = (TextView) view.findViewById(R.id.textMaths);
        mSolutionText = (TextView) view.findViewById(R.id.textSolution);
        mPositive = (ImageView) view.findViewById(R.id.imagePositive);
        mNegative = (ImageView) view.findViewById(R.id.imageNegative);
        mAccept = (ImageView) view.findViewById(R.id.tick);

        mQuestionText.setTypeface(mTypeFace);
        mMathsText.setTypeface(mTypeFace);
        mSolutionText.setTypeface(mTypeFace);

        Random rand = new Random();
        mNumberOne = rand.nextInt(5 - 1 + 1) + 1;
        mNumberTwo = rand.nextInt(5 - 1 + 1) + 1;

        mMathsText.setText(mNumberOne + " + " + mNumberTwo + " = ?");

        mPositive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView)view).setImageResource(R.drawable.positive_red);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mSelectedNumber < 10) {
                            mSelectedNumber++;
                        }
                        mSolutionText.setText(Integer.toString(mSelectedNumber));
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView)view).setImageResource(R.drawable.positive_green);
                        break;
                }
                return true;
            }
        });

        mNegative.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView)view).setImageResource(R.drawable.negative_red);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mSelectedNumber > 1) {
                            mSelectedNumber--;
                        }
                        mSolutionText.setText(Integer.toString(mSelectedNumber));
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView)view).setImageResource(R.drawable.negative_green);
                        break;
                }
                return true;
            }
        });

        mAccept.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView)view).setImageResource(R.drawable.tick_red);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isVisible()) {
                            dismiss();
                        }

                        if (mSelectedNumber == mNumberOne + mNumberTwo) {
                            if (mOnDialogCompleted != null) {
                                mOnDialogCompleted.run();
                            }
                        }
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView)view).setImageResource(R.drawable.tick_green);
                        break;
                }
                return true;
            }
        });
    }
}
