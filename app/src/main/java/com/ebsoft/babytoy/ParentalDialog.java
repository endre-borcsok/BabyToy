package com.ebsoft.babytoy;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Endre on 26/03/2017.
 */

public class ParentalDialog extends DialogFragment {

    private Typeface mTypeFace = null;
    private TextView mQuestionText;
    private TextView mMathsText;
    private TextView mSolutionText;

    public static ParentalDialog newInstance(String title) {
        ParentalDialog frag = new ParentalDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
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

        mQuestionText.setTypeface(mTypeFace);
        mMathsText.setTypeface(mTypeFace);
        mSolutionText.setTypeface(mTypeFace);
    }
}
