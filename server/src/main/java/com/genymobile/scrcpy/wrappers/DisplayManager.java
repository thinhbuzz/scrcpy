package com.genymobile.scrcpy.wrappers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import com.genymobile.scrcpy.AndroidVersions;
import com.genymobile.scrcpy.Ln;

import java.lang.reflect.Method;

@SuppressLint("PrivateApi,DiscouragedPrivateApi")
public final class DisplayManager {

    private final Object manager; // instance of hidden class android.hardware.display.DisplayManagerGlobal
    private Method requestDisplayPowerMethod;

    static DisplayManager create() {
        try {
            Class<?> clazz = Class.forName("android.hardware.display.DisplayManagerGlobal");
            Method getInstanceMethod = clazz.getDeclaredMethod("getInstance");
            Object dmg = getInstanceMethod.invoke(null);
            return new DisplayManager(dmg);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private DisplayManager(Object manager) {
        this.manager = manager;
    }

    private Method getRequestDisplayPowerMethod() throws NoSuchMethodException {
        if (requestDisplayPowerMethod == null) {
            requestDisplayPowerMethod = manager.getClass().getMethod("requestDisplayPower", int.class, boolean.class);
        }
        return requestDisplayPowerMethod;
    }

    @TargetApi(AndroidVersions.API_35_ANDROID_15)
    public boolean requestDisplayPower(int displayId, boolean on) {
        try {
            Method method = getRequestDisplayPowerMethod();
            return (boolean) method.invoke(manager, displayId, on);
        } catch (ReflectiveOperationException e) {
            Ln.e("Could not invoke method", e);
            return false;
        }
    }

    public int[] getDisplayIds() {
        try {
            return (int[]) manager.getClass().getMethod("getDisplayIds").invoke(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
