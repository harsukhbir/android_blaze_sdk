package com.blazeautomation.connected_ls_sample.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.blazeautomation.connected_ls_sample.R;

public class Alert {

    public static Dialog startLoader(Context mcontext)
    {
        Dialog mprogressDialog = new Dialog(mcontext);
        mprogressDialog.setContentView(R.layout.gif_layout);
        mprogressDialog.setCancelable(false);
        mprogressDialog.setCanceledOnTouchOutside(false);
        mprogressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return mprogressDialog;
    }

    public static void showLog(String title, String msg) {
        Log.e(title, msg);
    }


}
