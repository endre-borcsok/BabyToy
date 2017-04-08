package com.ebsoft.babytoy;

import android.app.DialogFragment;
import android.util.DisplayMetrics;

/**
 * Created by Endre on 03/04/2017.
 */

public class Dialog extends DialogFragment {

    protected Runnable mOnDialogCompleted;

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.8f);
        int height = (int) (width * (4f/3f));
        getDialog().getWindow().setLayout(width, height);
    }

    public void addOnCompletedRunnable(Runnable onDialogCompleted) {
        this.mOnDialogCompleted = onDialogCompleted;
    }
}
