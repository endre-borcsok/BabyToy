package com.ebsoft.toy;

import android.util.Log;

import java.io.Closeable;

/**
 * Created by Endre on 25/03/2017.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
