package com.genymobile.scrcpy;

import android.util.Log;

/**
 * Log both to Android logger (so that logs are visible in "adb logcat") and standard output/error (so that they are visible in the terminal
 * directly).
 */
public final class Ln {

    private static final String TAG = "scrcpy";
    private static final String PREFIX = "[server] ";

    enum Level {
        INFO,
        ERROR
    }

    private static final Level threshold = Level.INFO;

    private Ln() {
        // not instantiable
    }

    public static boolean isEnabled(Level level) {
        return level.ordinal() >= threshold.ordinal();
    }

    public static void i(String message) {
        if (isEnabled(Level.INFO)) {
            Log.i(TAG, message);
            System.out.print(PREFIX + "INFO: " + message + '\n');
        }
    }

    public static void e(String message, Throwable throwable) {
        if (isEnabled(Level.ERROR)) {
            Log.e(TAG, message, throwable);
            System.err.print(PREFIX + "ERROR: " + message + "\n");
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }
    }

    public static void e(String message) {
        e(message, null);
    }
}
