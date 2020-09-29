package com.blazeautomation.connected_ls_sample;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

public final class ProgressFragment extends AppCompatDialogFragment {
    private static final String TAG = "_PROGRESS";
    private TextView message = null;
    private String messageStr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_progress_alert_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View inflatedView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(inflatedView, savedInstanceState);
        message = inflatedView.findViewById(R.id.txtAlertMessage);
        message.setText(messageStr);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public void showProgress(FragmentManager manager, String message) {
        try {
            dismissProgress();
            setMessage(message);
            show(manager, TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMessage(String msg) {
        try {
            messageStr = msg;
            if (message != null)
                message.setText(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgress() {
        try {
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* if (handler != null)
            handler.sendEmptyMessage(1);*/
    }

    public interface OnFailureListener {
        void onFailed();
    }
}
