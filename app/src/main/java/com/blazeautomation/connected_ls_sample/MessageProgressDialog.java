package com.blazeautomation.connected_ls_sample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.fragment.app.FragmentManager;

public final class MessageProgressDialog {

    private final ProgressDialog pd;
    private Context context;
    private OnFailureListener listener;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    try {
                        if (context != null)
                            pd.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 1:
                    try {
                        pd.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    try {
                        if (listener != null) {
                            listener.onFailed();
                            listener = null;
                        }
                        dismissProgress();

                        MessageAlertDialog messageAlertDialog = new MessageAlertDialog(context);
                        if (msg.getData() != null && !msg.getData().getString("failureMessage").equalsIgnoreCase("")) {
                            messageAlertDialog.showAlertMessage(msg.getData().getString("failureMessage"));
                            messageAlertDialog.setCancelButtonVisibility(View.GONE);
                        }
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        if (listener != null) {
                            listener.onFailed();
                            listener = null;
                        }
                        dismissProgress();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
            return false;
        }
    });

    public MessageProgressDialog(Context context) {
        this.context = context;
        pd = new ProgressDialog(context);
        try {
            pd.setCancelable(false);
//        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //pd.setIndeterminate(false);

    }

    public void showProgress(int message) {
        showProgress(context.getString(message));
    }

    public void showProgress(FragmentManager manager, String message) {
        showProgress(message);
    }

    public void showProgress(String message) {
        try {
            dismissProgress();
            pd.setMessage(message);
            showProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentView(int layoutResID) {
        try {
            pd.show();
            if (pd.getWindow() != null) {
                pd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            pd.setContentView(layoutResID);
            pd.setIndeterminate(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String msg) {
        try {
            pd.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress(String message, int autoDismissDuration, final String failureMessage) {
        try {
            dismissProgress();
            pd.setMessage(message);
            showProgress();

            Message message1 = new Message();
            message1.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("failureMessage", failureMessage);
            message1.setData(bundle);
            handler.sendMessageDelayed(message1, autoDismissDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showProgress(int message, int autoDismissDuration) {
        showProgress(context.getString(message), autoDismissDuration);
    }

    public void showProgress(String message, int autoDismissDuration) {
        try {
            listener = null;
            showProgress(message, autoDismissDuration, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgress(String message, int autoDismissDuration, OnFailureListener failureListener) {
        try {
            dismissProgress();
            pd.setMessage(message);

            showProgress();
            listener = failureListener;

            Message message1 = new Message();
            message1.what = 3;
            handler.sendMessageDelayed(message1, autoDismissDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showProgress(String message, int autoDismissDuration, final String failureMessage, OnFailureListener failureListener) {
        try {
            pd.setMessage(message);
            showProgress();
            listener = failureListener;

            Message message1 = new Message();
            message1.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("failureMessage", failureMessage);
            message1.setData(bundle);
            handler.sendMessageDelayed(message1, autoDismissDuration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismissProgress() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            handler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* if (handler != null)
            handler.sendEmptyMessage(1);*/
    }

    public void showProgress() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (pd != null && !pd.isShowing())
                pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        try {
            return pd.isShowing();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clear() {

        try {
            if (handler != null)
                handler.removeCallbacksAndMessages(null);

            if (pd != null)
                pd.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnFailureListener {
        void onFailed();
    }
}
