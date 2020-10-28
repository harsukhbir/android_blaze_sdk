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


public class UserProfileFragment extends NavigationXFragment {
    private MessageAlertDialog alert;
    private MessageProgressDialog progress;


    public UserProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new MessageAlertDialog(requireActivity());
        progress = new MessageProgressDialog(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout first_name_lay = view.findViewById(R.id.first_name_lay);
        TextInputLayout last_name_lay = view.findViewById(R.id.last_name_lay);
        TextInputLayout pwd_lay = view.findViewById(R.id.password_lay);
        TextInputEditText firstName = view.findViewById(R.id.first_name);
        TextInputEditText lastName = view.findViewById(R.id.last_name);
        TextInputEditText password = view.findViewById(R.id.password);


        View btnOTP = view.findViewById(R.id.btnOTP);
        firstName.setText(model.getFirstName());
        lastName.setText(model.getLastName());
        password.setText(model.getPassword());
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            String firstNameStr = firstName.getText().toString().trim();
            String lastNameStr = lastName.getText().toString().trim();
            if (TextUtils.isEmpty(firstNameStr)) {
                first_name_lay.setError("First Name can not be empty");
                return;
            }
/*            if (TextUtils.isEmpty(mail)) {
                email_id_lay.setError("Enter email id");
                return;
            }*/
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));

            BlazeSDK.updateUserDetails(firstNameStr, lastNameStr, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    model.setFirstName(firstNameStr);
                    model.setLastName(lastNameStr);
                    alert.showAlertMessage(getChildFragmentManager(), "updated successfully.");
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            });
        });
        btnOTP.setOnClickListener(v -> {
            String passwordStr = password.getText().toString().trim();
            if (TextUtils.isEmpty(passwordStr)) {
                pwd_lay.setError("Password can not be empty");
                return;
            }
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));

            BlazeSDK.changePassword(model.pwd, passwordStr, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    if (blazeResponse.getStatus()) {
                        model.setPassword(passwordStr);
                        alert.showAlertMessage(getChildFragmentManager(), "updated successfully.");
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());

                }
            });
        });
        view.findViewById(R.id.logout).setOnClickListener(v -> BlazeSDK.logout(new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                model.logout();
                gotoF(R.id.action_to_nav_login);
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {

            }
        }));
    }
}
