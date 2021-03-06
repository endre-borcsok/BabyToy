package com.ebsoft.toy.Dialogs;

import android.content.SharedPreferences;
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
 * Created by Endre on 27/03/2017.
 */

public class SettingsDialog extends Dialog {

    public static final String PREFERENCE_VIBRATION = "Vibration";

    private Typeface mTypeFace = null;
    private TextView mVibrationtext;
    private ImageView mVibrationTick;
    private ImageView mAccept;
    private SharedPreferences mPreferences;

    public static SettingsDialog newInstance(String title, Runnable onDialogCompleted, SharedPreferences preferences) {
        SettingsDialog frag = new SettingsDialog();
        frag.addOnCompletedRunnable(onDialogCompleted);
        frag.setPreferences(preferences);
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.mPreferences = preferences;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.layout_settings_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTypeFace = Typeface.createFromAsset(getDialog().getContext().getAssets(),"fonts/Chewy.ttf");

        mVibrationtext = (TextView) view.findViewById(R.id.textVibration);
        mVibrationTick = (ImageView) view.findViewById(R.id.vibrationTick);
        mAccept = (ImageView) view.findViewById(R.id.tick);

        mVibrationtext.setTypeface(mTypeFace);

        if (mPreferences.getBoolean(PREFERENCE_VIBRATION, true)) {
            ((ImageView)view.findViewById(R.id.vibrationTick)).setImageResource(R.drawable.tick_green);
        } else {
            ((ImageView)view.findViewById(R.id.vibrationTick)).setImageResource(R.drawable.button_grey);
        }

        mVibrationTick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mPreferences.getBoolean(PREFERENCE_VIBRATION, true)) {
                            ((ImageView)view).setImageResource(R.drawable.button_grey);
                            mPreferences.edit().putBoolean(PREFERENCE_VIBRATION, false).commit();
                        } else {
                            ((ImageView)view).setImageResource(R.drawable.tick_green);
                            mPreferences.edit().putBoolean(PREFERENCE_VIBRATION, true).commit();
                        }
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
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView)view).setImageResource(R.drawable.tick_green);
                        break;
                }
                return true;
            }
        });
    }
}
