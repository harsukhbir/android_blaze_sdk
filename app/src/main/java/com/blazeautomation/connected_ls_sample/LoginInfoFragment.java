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


public class LoginInfoFragment extends NavigationXFragment {

    private AlertFragment alert;
    private ProgressFragment progress;

    public LoginInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new AlertFragment();
        progress = new ProgressFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout pwd_lay = view.findViewById(R.id.pwd_lay);
        TextInputLayout email_id_lay = view.findViewById(R.id.email_id_lay);
        TextInputEditText pwd = view.findViewById(R.id.pwd);
        TextInputEditText email_id = view.findViewById(R.id.email_id);
        view.findViewById(R.id.forgot).setOnClickListener(v -> gotoF(R.id.action_nav_login_to_nav_forgot));
        view.findViewById(R.id.login).setOnClickListener(v -> {
            String pwd1 = pwd.getText().toString().trim();
            String mail = email_id.getText().toString().trim();
            if (TextUtils.isEmpty(pwd1)) {
                pwd_lay.setError("Enter password");
                return;
            }
            if (TextUtils.isEmpty(mail)) {
                email_id_lay.setError("Enter email id");
                return;
            }
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.loginUser(mail, pwd1, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {

                    if (blazeResponse != null) {
                        if (blazeResponse.getStatus()) {
                            model.storeUser(mail, pwd1, blazeResponse);// it is not required.
                            model.getAccessToken(new OnCompletedListener() {
                                @Override
                                public void onSuccess() {
                                    progress.dismissProgress();
                                    model.getUserDetails(new OnCompletedListener() {
                                        @Override
                                        public void onSuccess() {
                                            model.storeUser(mail, pwd1, blazeResponse);
                                            gotoF(R.id.action_to_nav_hub_list);
                                        }

                                        @Override
                                        public void onFailure() {
                                            gotoF(R.id.action_to_nav_hub_list);
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {
                                    progress.dismissProgress();
                                }
                            });
                        } else {
                            progress.dismissProgress();
                            String msg = blazeResponse.getMessage();
                            Loggers.error("Client" + " " + msg);
                            alert.showAlertMessage(getChildFragmentManager(), msg);
                        }
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    String msg = blazeResponse.getMessage();
                    alert.showAlertMessage(getChildFragmentManager(), msg);
                    Loggers.error("Client" + " " + msg);
                }
            });


        });
    }
}
