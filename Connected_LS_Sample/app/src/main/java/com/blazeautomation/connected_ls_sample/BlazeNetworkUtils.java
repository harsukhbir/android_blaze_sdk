package com.blazeautomation.connected_ls_sample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

final class BlazeNetworkUtils {
    static final String CONN_TYPE_GPRS = "gprs";
    static final String CONN_TYPE_NONE = "none";
    private static final String CONN_TYPE_WIFI = "wifi";


    static boolean isNetworkAvailable(Context context) {
        return getNetworkType(context) != null;
    }

    private static String getNetworkType(Context context) {
        NetworkInfo.State tem;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return null;
        }
        NetworkInfo[] info = null;
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Network[] allNetworks = connectivity.getAllNetworks();
                if (allNetworks.length > 0) {
                    info = new NetworkInfo[allNetworks.length];
                    int length = allNetworks.length;
                    int i = 0;
                    int i2 = 0;
                    while (i < length) {
                        info[i2] = connectivity.getNetworkInfo(allNetworks[i]);
                        i++;
                        i2++;
                    }
                }
            } else {
                info = connectivity.getAllNetworkInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (info == null) {
            return null;
        }
        int i3 = 0;
        while (i3 < info.length) {
            if (info[i3] == null || !((tem = info[i3].getState()) == NetworkInfo.State.CONNECTED || tem == NetworkInfo.State.CONNECTING)) {
                i3++;
            } else {
                return info[i3].getTypeName() + " " + info[i3].getSubtypeName() + info[i3].getExtraInfo();
            }
        }
        return null;
    }

    private static String getNetConnType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return CONN_TYPE_NONE;
        }
        NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && NetworkInfo.State.CONNECTED == info.getState()) {
            return CONN_TYPE_WIFI;
        }
        NetworkInfo info2 = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (info2 == null || NetworkInfo.State.CONNECTED != info2.getState()) {
            return CONN_TYPE_NONE;
        }

        return CONN_TYPE_GPRS;
    }

    static boolean isWifiConnected(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return false;
        }
        NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info == null) {
            return false;
        }

        return info.isAvailable();
    }

    static boolean isGPRS(Context context) {
        String netConnType = getNetConnType(context);
        return CONN_TYPE_GPRS.equals(netConnType);
    }


    static String getWifiSSID(Context context) {

        String networkStatus = "";

        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //Check Wifi
            if (connectivityManager == null) {
                return CONN_TYPE_NONE;
            }
            final NetworkInfo wifi = connectivityManager.getActiveNetworkInfo();
            //Check for mobile data
            // final android.net.NetworkInfo mobile = connectivityManager.getActiveNetworkInfo();
            if (wifi != null) {
                if (wifi.getType() == ConnectivityManager.TYPE_WIFI) {
                    networkStatus = "wifi";
                    WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (manager != null && manager.isWifiEnabled()) {
                        WifiInfo wifiInfo = manager.getConnectionInfo();
                        if (wifiInfo != null) {
                            NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                            if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                                String ssid = wifiInfo.getSSID();
                                if (ssid == null || ssid.length() == 0 || "<unknown ssid>".equalsIgnoreCase(ssid)) {
                                    String ext = wifi.getExtraInfo();
                                    if (ext != null)
                                        return ext.replace("\"", "");
                                } else
                                    return ssid.replace("\"", "");
                            }
                        }
                    }
                } else if (wifi.getType() == ConnectivityManager.TYPE_MOBILE) {
                    networkStatus = CONN_TYPE_GPRS;
                    return networkStatus;
                } else {
                    networkStatus = CONN_TYPE_NONE;
                    return networkStatus;
                }
            }

            if (wifi == null) return CONN_TYPE_NONE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return networkStatus;
    }

}
