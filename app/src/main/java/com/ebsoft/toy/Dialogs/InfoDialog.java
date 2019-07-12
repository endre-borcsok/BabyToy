package com.ebsoft.toy.Dialogs;

import android.content.DialogInterface;
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

import com.ebsoft.toy.R;

/**
 * Created by Endre on 26/03/2017.
 */

public class InfoDialog extends Dialog {

    private Typeface mTypeFace = null;
    private TextView mInfoText;
    private ImageView mAccept;
    private String mInfo;

    private Runnable mDismissRunnable;

    public static InfoDialog newInstance(String title, String info, Runnable onDialogCompleted) {
        InfoDialog frag = new InfoDialog();
        frag.addOnCompletedRunnable(onDialogCompleted);
        frag.setInfo(info);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void addDismissRunnable(Runnable runnable) {
        this.mDismissRunnable = runnable;
    }

    private void setInfo(String info) {
        this.mInfo = info;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.layout_info_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTypeFace = Typeface.createFromAsset(getDialog().getContext().getAssets(),"fonts/Chewy.ttf");

        mInfoText = (TextView) view.findViewById(R.id.textQuestion);
        mAccept = (ImageView) view.findViewById(R.id.tick);

        mInfoText.setTypeface(mTypeFace);
        mInfoText.setText(mInfo);

        mAccept.setVisibility(View.GONE);
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

                        mOnDialogCompleted.run();
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView)view).setImageResource(R.drawable.tick_green);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface inf) {
        super.onDismiss(inf);
        if (mDismissRunnable != null) {
            mDismissRunnable.run();
        }
    }
}
