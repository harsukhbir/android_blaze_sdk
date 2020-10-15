package com.blazeautomation.connected_ls_sample;

import android.app.Application;

import com.BlazeAutomation.ConnectedLS.BlazeSDK;

public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BlazeSDK.init(this, "b501cd4d-bf62-4e26-a383-31827fd250a6", "021f6589-130b-4e3f-99ef-e2c3b4944cc7");
    }
}
