package com.ebsoft.babytoy;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import static java.security.AccessController.getContext;

/**
 * Created by Endre on 09/04/2017.
 */

public class BabyToyApplication extends Application {

    private Thread.UncaughtExceptionHandler mUnCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    Intent intent = new Intent(BabyToyApplication.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    System.exit(2);
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
