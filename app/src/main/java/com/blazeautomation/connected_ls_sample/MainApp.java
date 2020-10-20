package com.blazeautomation.connected_ls_sample;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.BlazeAutomation.ConnectedLS.BlazeSDK;

import java.io.File;

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BlazeSDK.init(this, "b501cd4d-bf62-4e26-a383-31827fd250a6", "021f6589-130b-4e3f-99ef-e2c3b4944cc7");
    }

}
