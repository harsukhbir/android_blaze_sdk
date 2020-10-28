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

import java.util.Objects;


public class CreateAccountFragment extends NavigationXFragment {

    private static final String TAG = "_CREATE_ACCOUNT";
    private String first_name_str, last_name_str, phone_str, pwd_str, mail_str;
    private MessageAlertDialog alert;
    private MessageProgressDialog progress;

    public CreateAccountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new MessageAlertDialog(requireContext());
        progress = new MessageProgressDialog(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout mail_lay = view.findViewById(R.id.mail_lay);
        TextInputEditText mail = view.findViewById(R.id.mail);
        TextInputLayout first_name_lay = view.findViewById(R.id.first_name_lay);
        TextInputEditText first_name = view.findViewById(R.id.first_name);
//        TextInputLayout last_name_lay = view.findViewById(R.id.last_name_lay);
        TextInputEditText last_name = view.findViewById(R.id.last_name);
//      TextInputLayout phone_lay = view.findViewById(R.id.phone_lay);

        TextInputLayout pwd_lay = view.findViewById(R.id.pwd_lay);
        TextInputEditText pwd = view.findViewById(R.id.pwd);

        view.findViewById(R.id.login).setOnClickListener(v -> {
            try {
                mail_str = Objects.requireNonNull(mail.getText()).toString().trim();
                first_name_str = Objects.requireNonNull(first_name.getText()).toString().trim();
                last_name_str = Objects.requireNonNull(last_name.getText()).toString().trim();
                // phone_str = Objects.requireNonNull(phone.getText()).toString().trim();
                pwd_str = Objects.requireNonNull(pwd.getText()).toString().trim();

                phone_str = "9999999999";
                if (TextUtils.isEmpty(mail_str)) {
                    mail_lay.setError("Enter Mail id");
                    return;
                } else
                    mail_lay.setError(null);

                if (TextUtils.isEmpty(first_name_str)) {
                    first_name_lay.setError("Enter first name");
                    return;
                } else first_name_lay.setError(null);
/*            if (TextUtils.isEmpty(phone_str)) {
                phone_lay.setError("Enter Phone number");
                return;
            }*/
                if (TextUtils.isEmpty(pwd_str)) {
                    pwd_lay.setError("Enter Password");
                    return;
                } else
                    pwd_lay.setError(null);

                progress.showProgress(getChildFragmentManager(), getString(R.string.creating_blaze_account));

                BlazeSDK.registerUser(first_name_str, last_name_str, mail_str, pwd_str, new BlazeCallBack() {
                    @Override
                    public void onSuccess(BlazeResponse body) {
                        progress.dismissProgress();
                        if (body != null) {
                            if (body.getStatus()) {
                                model.storeUser(mail_str, pwd_str, body);
                                model.setFirstName(first_name_str);
                                model.setLastName(last_name_str);
                                gotoF(R.id.action_nav_signup_to_nav_verify);
                                return;
                            }
                            alert.showAlertMessage(getChildFragmentManager(), body.getMessage());
                        }
                    }

                    @Override
                    public void onError(BlazeResponse blazeResponse) {
                        progress.dismissProgress();
                        alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
