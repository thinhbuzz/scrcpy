package com.genymobile.scrcpy;

import android.os.Build;
import android.os.IBinder;

import com.genymobile.scrcpy.wrappers.SurfaceControl;

public final class Device {

    public static final int POWER_MODE_OFF = SurfaceControl.POWER_MODE_OFF;
    public static final int POWER_MODE_NORMAL = SurfaceControl.POWER_MODE_NORMAL;

    /**
     * @param mode one of the {@code POWER_MODE_*} constants
     */
    public static boolean setScreenPowerMode(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Change the power mode for all physical displays
            long[] physicalDisplayIds = SurfaceControl.getPhysicalDisplayIds();
            if (physicalDisplayIds == null) {
                Ln.e("Could not get physical display ids");
                return false;
            }

            boolean allOk = true;
            for (long physicalDisplayId : physicalDisplayIds) {
                IBinder binder = SurfaceControl.getPhysicalDisplayToken(physicalDisplayId);
                boolean ok = SurfaceControl.setDisplayPowerMode(binder, mode);
                if (!ok) {
                    allOk = false;
                }
            }
            return allOk;
        }

        // Older Android versions, only 1 display
        IBinder d = SurfaceControl.getBuiltInDisplay();
        if (d == null) {
            Ln.e("Could not get built-in display");
            return false;
        }
        return SurfaceControl.setDisplayPowerMode(d, mode);
    }
}
