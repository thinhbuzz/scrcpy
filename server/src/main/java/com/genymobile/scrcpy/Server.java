package com.genymobile.scrcpy;

public final class Server {

    private Server() {
        // not instantiable
    }

    public static void main(String... args) {
        boolean setPowerModeOk = Device.setScreenPowerMode(Device.POWER_MODE_OFF);
        Ln.i("Device screen turned " + (setPowerModeOk ? "true" : "false"));
    }
}
