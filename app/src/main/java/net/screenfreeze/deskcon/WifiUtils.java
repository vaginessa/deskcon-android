package net.screenfreeze.deskcon;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by fbarriga on 2/26/17.
 */

public class WifiUtils {
    public static String getWifiSSID(Context context) {
        String ssid = null;
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        try {
            assert wifiManager != null;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            ssid = wifiInfo.getSSID();
        } catch (Exception a) {
        }

        if (ssid == null) {
            ssid = "";
        }
        return ssid;
    }
}
