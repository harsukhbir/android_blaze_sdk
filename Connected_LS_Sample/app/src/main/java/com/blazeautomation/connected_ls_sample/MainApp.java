package com.blazeautomation.connected_ls_sample;

import android.app.Application;

import com.BlazeAutomation.ConnectedLS.BlazeSDK;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BlazeSDK.init(this, "YOUR_COMPANY_CLIENT_ID", "YOUR_COMPANY_SECRET_KEY");
    }
}
