package com.genymobile.scrcpy;

import android.os.Build;
import android.os.IBinder;

import com.genymobile.scrcpy.wrappers.DisplayControl;
import com.genymobile.scrcpy.wrappers.ServiceManager;
import com.genymobile.scrcpy.wrappers.SurfaceControl;

public final class Device {
    public static final int DISPLAY_ID_NONE = -1;

    public static final int POWER_MODE_OFF = SurfaceControl.POWER_MODE_OFF;
    public static final int POWER_MODE_NORMAL = SurfaceControl.POWER_MODE_NORMAL;

    // The new display power method introduced in Android 15 does not work as expected:
    // <https://github.com/Genymobile/scrcpy/issues/5530>
    private static final boolean USE_ANDROID_15_DISPLAY_POWER = false;

    private Device() {
        // not instantiable
    }

    public static boolean setDisplayPower(int displayId, boolean on) {
        if (displayId == Device.DISPLAY_ID_NONE) {
            return false;
        }

        if (USE_ANDROID_15_DISPLAY_POWER && Build.VERSION.SDK_INT >= AndroidVersions.API_35_ANDROID_15) {
            return ServiceManager.getDisplayManager().requestDisplayPower(displayId, on);
        }

        boolean applyToMultiPhysicalDisplays = Build.VERSION.SDK_INT >= AndroidVersions.API_29_ANDROID_10;

        if (applyToMultiPhysicalDisplays
                && Build.VERSION.SDK_INT >= AndroidVersions.API_34_ANDROID_14
                && Build.BRAND.equalsIgnoreCase("honor")
                && SurfaceControl.hasGetBuildInDisplayMethod()) {
            // Workaround for Honor devices with Android 14:
            //  - <https://github.com/Genymobile/scrcpy/issues/4823>
            //  - <https://github.com/Genymobile/scrcpy/issues/4943>
            applyToMultiPhysicalDisplays = false;
        }

        int mode = on ? POWER_MODE_NORMAL : POWER_MODE_OFF;
        if (applyToMultiPhysicalDisplays) {
            // On Android 14, these internal methods have been moved to DisplayControl
            boolean useDisplayControl =
                    Build.VERSION.SDK_INT >= AndroidVersions.API_34_ANDROID_14 && !SurfaceControl.hasGetPhysicalDisplayIdsMethod();

            // Change the power mode for all physical displays
            long[] physicalDisplayIds = useDisplayControl ? DisplayControl.getPhysicalDisplayIds() : SurfaceControl.getPhysicalDisplayIds();
            if (physicalDisplayIds == null) {
                Ln.e("Could not get physical display ids");
                return false;
            }

            boolean allOk = true;
            for (long physicalDisplayId : physicalDisplayIds) {
                IBinder binder = useDisplayControl ? DisplayControl.getPhysicalDisplayToken(
                        physicalDisplayId) : SurfaceControl.getPhysicalDisplayToken(physicalDisplayId);
                allOk &= SurfaceControl.setDisplayPowerMode(binder, mode);
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
