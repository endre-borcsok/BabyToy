package com.ebsoft.toy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Endre on 09/04/2017.
 */

public class BabyToyApplication extends Application {

    private Thread.UncaughtExceptionHandler mUnCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    if (getSharedPreferences(MainActivity.PREFERENCE_KEY, MODE_PRIVATE).getBoolean(MainActivity.PREFERENCE_PARENTAL_MODE, false)) {
                        Intent intent = new Intent(BabyToyApplication.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        System.exit(2);
                    }
                }
            };

    public BabyToyApplication() {
        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(mUnCaughtExceptionHandler);
    }

    public static Context getAppContext() {
        return getAppContext();

    }
}
