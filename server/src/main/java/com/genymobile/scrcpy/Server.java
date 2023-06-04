package com.genymobile.scrcpy;

import java.util.Arrays;
import java.util.Objects;

public final class Server {

    private Server() {
        // not instantiable
    }

    public static void main(String ...args) {
        Ln.i("Server args " + Arrays.toString(args));
        if (Objects.equals(args[0], "off")) {
            boolean setPowerModeOk = Device.setScreenPowerMode(Device.POWER_MODE_OFF);
            Ln.i("Device screen turned off " + (setPowerModeOk ? "true" : "false"));
        } else {
            boolean setPowerModeOk = Device.setScreenPowerMode(Device.POWER_MODE_NORMAL);
            Ln.i("Device screen turned on " + (setPowerModeOk ? "true" : "false"));
        }
    }
}
