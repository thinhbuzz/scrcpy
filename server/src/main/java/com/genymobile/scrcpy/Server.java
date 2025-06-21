package com.genymobile.scrcpy;

import java.util.Arrays;
import java.util.Objects;

public final class Server {

    private Server() {
        // not instantiable
    }

    // adb push "C:\Users\thinhbuzz\Downloads\scrcpy-server-v3.3.1" /data/local/tmp/off-screen-3.3.1
    // adb push /release/off-screen-3.3.1" /data/local/tmp/off-screen-3.3.1
    // export offScreenPath=/data/local/tmp/off-screen-3.3.1
    // [[ -f $offScreenPath ]] && CLASSPATH=$offScreenPath app_process / com.genymobile.scrcpy.Server on

    public static void main(String... args) {
        Ln.i("Server args " + Arrays.toString(args));

        if (Objects.equals(args[0], "off")) {
            boolean setPowerModeOk = Device.setDisplayPower(false);
            Ln.i("Device screen turned off " + (setPowerModeOk ? "true" : "false"));
        } else {
            boolean setPowerModeOk = Device.setDisplayPower(true);
            Ln.i("Device screen turned on " + (setPowerModeOk ? "true" : "false"));
        }
    }
}
