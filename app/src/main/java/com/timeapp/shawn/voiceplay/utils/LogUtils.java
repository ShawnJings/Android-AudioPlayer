package com.timeapp.shawn.voiceplay.utils;

import android.util.Log;

/**
 * Created by java on 2016/12/12.
 */

public class LogUtils {

    private static final int LEVEL = 0;
    private static final String TAG = "debug";

    private static final int DEBUG = 1;
    private static final int ERROR = 2;
    private static final int WARN = 3;
    private static final int INFO = 4;

    public static void d(String msg) {
        d(null, msg);
    }

    public static void e(String msg) {
        e(null, msg);
    }

    public static void w(String msg) {
        if (LEVEL <= WARN) {
            Log.w(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= INFO) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            if (tag == null) {
                Log.d(TAG, msg);
            } else {
                Log.d(tag, msg);
            }
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            if (tag == null) {
                Log.e(TAG, msg);
            } else {
                Log.e(tag, msg);
            }
        }
    }

    public static void el(String tag, String dataStr) {
        if (LEVEL <= ERROR) {
            if (tag == null) {
                tag = TAG;
            }

            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < dataStr.length(); i++) {
                buffer.append(dataStr.charAt(i));
                if (buffer.length() >= 1024) {
                    LogUtils.e(tag, buffer.toString());
                    buffer.delete(0, buffer.length());
                }
            }

            if (buffer.length() > 0) {
                LogUtils.e(tag, buffer.toString());
            }

        }
    }

}
