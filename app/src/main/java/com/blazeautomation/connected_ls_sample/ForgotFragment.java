package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotFragment extends NavigationXFragment {
    boolean force = false;
    private MessageAlertDialog alert;
    private MessageProgressDialog progress;
    private String code;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new MessageAlertDialog(requireActivity());
        progress = new MessageProgressDialog(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_forgot_pasword, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputLayout mail_lay = view.findViewById(R.id.mail_lay);
        TextInputEditText mail = view.findViewById(R.id.mail);
        TextInputLayout otp_lay = view.findViewById(R.id.otp_lay);
        TextInputEditText otp = view.findViewById(R.id.otp);
        TextInputLayout pwd_lay = view.findViewById(R.id.password_lay);
        TextInputEditText pwd = view.findViewById(R.id.password);
        View update_lay = view.findViewById(R.id.update_lay);
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            String str = mail.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                mail_lay.setError("Enter Your registered email ID");
                return;
            } else
                mail_lay.setError(null);
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.getOTPToForgotPassword(str, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    if (blazeResponse.getStatus()) {
                        update_lay.setVisibility(View.VISIBLE);
                        code = blazeResponse.getCode();
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    update_lay.setVisibility(View.GONE);
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            });
        });
        view.findViewById(R.id.btnUpdate).setOnClickListener(v -> {
            String str = pwd.getText().toString().trim();
            String otp_str = otp.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                pwd_lay.setError("Enter your new password");
                return;
            } else
                pwd_lay.setError(null);
            if (TextUtils.isEmpty(otp_str)) {
                otp_lay.setError("Please enter a valid OTP");
                return;
            } else
                otp_lay.setError(null);
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.createPasswordUsingOTP(otp_str, code, str, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    if (blazeResponse.getStatus()) {
                        alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_nav_forgot_to_nav_login));
                        alert.showAlertMessage(getChildFragmentManager(), "Password changed successfully");
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());

                }
            });
        });
    }
}
