package com.genymobile.scrcpy;

import com.genymobile.scrcpy.wrappers.DisplayManager;
import com.genymobile.scrcpy.wrappers.ServiceManager;

import java.util.Arrays;
import java.util.Objects;

public final class Server {

    private Server() {
        // not instantiable
    }

    // export offScreenPath=/data/local/tmp/off-screen-3.3.1
    // [[ -f $offScreenPath ]] && CLASSPATH=$offScreenPath app_process / com.genymobile.scrcpy.Server on

    public static void main(String... args) {
        Ln.i("Server args " + Arrays.toString(args));

        DisplayManager displayManager = ServiceManager.getDisplayManager();
        int[] displayIds = displayManager.getDisplayIds();
        if (displayIds == null || displayIds.length == 0) {
            Ln.e("No display found, cannot turn screen on/off");
            return;
        }
        if (Objects.equals(args[0], "off")) {
            boolean setPowerModeOk = Device.setDisplayPower(displayIds[0], false);
            Ln.i("Device screen turned off " + (setPowerModeOk ? "true" : "false"));
        } else {
            boolean setPowerModeOk = Device.setDisplayPower(displayIds[0], true);
            Ln.i("Device screen turned on " + (setPowerModeOk ? "true" : "false"));
        }
    }
}
