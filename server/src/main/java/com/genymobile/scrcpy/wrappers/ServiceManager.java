package com.genymobile.scrcpy.wrappers;

import android.annotation.SuppressLint;

@SuppressLint("PrivateApi,DiscouragedPrivateApi")
public final class ServiceManager {
    private static DisplayManager displayManager;


    // The DisplayManager may be used from both the Controller thread and the video (main) thread
    public static synchronized DisplayManager getDisplayManager() {
        if (displayManager == null) {
            displayManager = DisplayManager.create();
        }
        return displayManager;
    }
}
