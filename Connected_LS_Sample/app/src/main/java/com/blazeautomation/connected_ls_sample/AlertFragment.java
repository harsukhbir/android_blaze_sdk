package com.blazeautomation.connected_ls_sample;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import static android.view.View.VISIBLE;

public final class AlertFragment extends AppCompatDialogFragment {
    private static final String TAG = "_ALERT";
    private TextView title = null, message = null;
    private PositiveButtonListener positiveButtonListener;
    private NegativeButtonListener negativeButtonListener;
    private TextView positive;
    private TextView negative;
    private Handler handler = new Handler();


    private boolean dismissOnClick = true;
    private boolean negativeVisible = false;
    private String positiveTxt;
    private String negativeTxt;
    private String titleStr, messageStr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dialog_alert_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View inflatedView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(inflatedView, savedInstanceState);
        try {
            positive = inflatedView.findViewById(R.id.btnOk);
            negative = inflatedView.findViewById(R.id.btnCancel);
            title = inflatedView.findViewById(R.id.txtAlertTitle);
            message = inflatedView.findViewById(R.id.txtAlertMessage);

            title.setText(titleStr);
            message.setText(messageStr);
/*            Typeface font = BOneApplication.getInstance().font;
            negative.setTypeface(font, Typeface.BOLD);
            positive.setTypeface(font, Typeface.BOLD);
            title.setTypeface(font, Typeface.BOLD);
            message.setTypeface(font);*/

            negative.setVisibility(negativeVisible ? VISIBLE : View.GONE);
            if (negativeTxt == null)
                negativeTxt = getString(R.string.cancel);
            negative.setText(negativeTxt);
            negative.setOnClickListener(v -> {
                if (dismissOnClick)
                    dismiss();
                if (negativeButtonListener != null)
                    negativeButtonListener.onCancelClicked(v);
            });

            if (positiveTxt == null)
                positiveTxt = getString(R.string.ok);
            positive.setVisibility(VISIBLE);
            positive.setText(positiveTxt);
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
                        dismiss();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public void showAlertMessage(FragmentManager manager, final String title1, final String message1) {
        dismissAlert();
        handler.post(() -> {
            try {
                titleStr = title1;
                messageStr = message1;
                if (title != null)
                    title.setText(title1);
                if (message != null)
                    message.setText(message1);
                show(manager, TAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void showAlertMessage(FragmentManager manager, final String message1) {
        dismissAlert();
        handler.post(() -> {
            try {
                titleStr = null;
                messageStr = message1;
                if (message != null)
                    message.setText(message1);
                show(manager, TAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void dismissAlert() {
        handler.post(() -> {
            try {
                // if (isVisible())
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    public void setOkButtonListener(String name, PositiveButtonListener okButtonListener) {
        positiveButtonListener = okButtonListener;
        positiveTxt = name;
        if (positive != null) {
            positive.setVisibility(VISIBLE);
            positive.setText(positiveTxt);
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
                        dismiss();
                }
            });
        }
    }


    public void setCancelButtonListener(String name, NegativeButtonListener cancelButtonListener) {
        negativeButtonListener = cancelButtonListener;
        negativeTxt = name;
        negativeVisible = true;
        if (negative != null) {
            if (name != null && !name.isEmpty()) {
                negative.setText(name);
            }
            positive.setVisibility(VISIBLE);
            negative.setVisibility(VISIBLE);
            negative.setOnClickListener(v -> {
                if (dismissOnClick)
                    dismiss();
                if (negativeButtonListener != null)
                    negativeButtonListener.onCancelClicked(v);
            });
        }
    }


    public void setCancelButtonVisibility(final int state) {
        negativeVisible = (state == VISIBLE);
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
            dismiss();
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
