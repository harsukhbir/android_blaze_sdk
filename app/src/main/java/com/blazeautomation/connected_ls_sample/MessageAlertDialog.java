package com.blazeautomation.connected_ls_sample;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

public final class MessageAlertDialog {
    private final View inflatedView;
    private final TextView message;
    private PositiveButtonListener positiveButtonListener;
    private NegativeButtonListener negativeButtonListener;
    private TextView positive;
    private TextView negative;
    private Handler handler = new Handler();
    private AlertDialog alertDialog;
    private Context context;
    private boolean dismissOnClick = true;
    private TextView body;

    public MessageAlertDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        inflatedView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_alert_layout, null, false);
        positive = inflatedView.findViewById(R.id.btnOk);
        negative = inflatedView.findViewById(R.id.btnCancel);
        message = inflatedView.findViewById(R.id.txtAlertMessage);

        negative.setVisibility(View.GONE);
        this.context = context;
        setOkButtonListener(context.getString(R.string.ok), v -> {

        });

        setCancelButtonListener(context.getString(R.string.cancel), v -> {

        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.setView(inflatedView);
    }

    public void showAlertMessage(final int title1, final int message1) {
        showAlertMessage(context.getString(title1), context.getString(message1));
    }

    public void showAlertMessage(final FragmentManager manager, final String message, boolean cancel) {
        if (!cancel) {
            setCancelButtonVisibility(View.GONE);
        }
        showAlertMessage("", message);
    }

    public void showAlertMessage(final FragmentManager manager, final String message) {
        showAlertMessage(manager, message, false);
    }

    public void showAlertMessage(final String title, final String message) {
        dismissAlert();
        handler.post(() -> {
            try {
                ((TextView) inflatedView.findViewById(R.id.txtAlertTitle)).setText(title);
                body = inflatedView.findViewById(R.id.txtAlertMessage);
                body.setText(message);
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showAlertMessage(final int id) {
        showAlertMessage(context.getString(id));
    }

    public void showAlertMessage(final String message) {
        dismissAlert();
        handler.post(() -> {
            try {
                String title = context.getString(R.string.app_name);
                ((TextView) inflatedView.findViewById(R.id.txtAlertTitle)).setText(title);
                body = inflatedView.findViewById(R.id.txtAlertMessage);
                body.setText(message);
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isShowing() {
        return alertDialog != null && alertDialog.isShowing();
    }

    public void dismissAlert() {

        handler.post(() -> {
            try {
                if (isShowing())
                    alertDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void setOkButtonListener(int name, PositiveButtonListener okButtonListener) {
        setOkButtonListener(context.getString(name), okButtonListener);
    }

    public void setOkButtonListener(String name, PositiveButtonListener okButtonListener) {
        positiveButtonListener = okButtonListener;

        if (name != null && !name.isEmpty()) {
            positive.setText(name);
        }

        positive.setVisibility(View.VISIBLE);
        //inflatedView.findViewById(R.id.alert_cancel).setVisibility(View.VISIBLE);
        positive.setOnClickListener(v -> {
            try {
                if (positiveButtonListener != null) {
                    positiveButtonListener.onOkClicked(v);
                    if (dismissOnClick)
                        positiveButtonListener = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (dismissOnClick)
                    alertDialog.dismiss();
            }
        });

    }

    public void setCancelButtonListener(String name, NegativeButtonListener cancelButtonListener) {
        negativeButtonListener = cancelButtonListener;

        if (name != null && !name.isEmpty()) {
            negative.setText(name);
        }
        positive.setVisibility(View.VISIBLE);
        negative.setVisibility(View.VISIBLE);
        negative.setOnClickListener(v -> {
            if (dismissOnClick)
                alertDialog.dismiss();
            if (negativeButtonListener != null)
                negativeButtonListener.onCancelClicked(v);
        });
    }


    public void setCancelButtonVisibility(final int state) {
        if (negative != null) negative.setVisibility(state);
    }

    public void setDismissOnClick(boolean dismissOnClick) {
        this.dismissOnClick = dismissOnClick;
    }

    public void setAllCaps(boolean b, Object o, boolean dismissOnClick) {
        this.dismissOnClick = dismissOnClick;
        if (positive != null)
            positive.setAllCaps(b);
        if (negative != null)
            negative.setAllCaps(b);
        if (o == null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            message.setLayoutParams(params);
            message.setGravity(Gravity.START);
        }
    }

    public void clear() {

        try {
            if (handler != null)
                handler.removeCallbacksAndMessages(null);
            if (alertDialog != null)
                alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface PositiveButtonListener {
        void onOkClicked(View v);
    }

    public interface NegativeButtonListener {
        void onCancelClicked(View v);
    }
}
