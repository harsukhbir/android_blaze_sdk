package com.blazeautomation.connected_ls_sample;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SSIDFragment extends NavigationXFragment {
    private static final int MAX_SSID_PWD_LENGTH = 64;
    private AppCompatTextView ssidName;
    private AppCompatButton confirm;
    private ProgressFragment progressDialog;
    private AlertFragment alertDialog;

    public SSIDFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressFragment();
        alertDialog = new AlertFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ssid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ssidName = view.findViewById(R.id.ssid);
        TextInputLayout pwd_lay = view.findViewById(R.id.pwd_lay);
        TextInputEditText password = view.findViewById(R.id.password);

        TextView change_wifi = view.findViewById(R.id.change_wifi);

        confirm = view.findViewById(R.id.confirm);

        change_wifi.setOnClickListener(v -> gotoF(R.id.action_nav_ssid_to_nav_list));

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (password.getText().length() > MAX_SSID_PWD_LENGTH) {
                        String s = password.toString().substring(0, MAX_SSID_PWD_LENGTH);
                        alertDialog.showAlertMessage(getChildFragmentManager(), String.format(getString(R.string.pwd_length_max_reached), MAX_SSID_PWD_LENGTH));
                        password.setText(s);
                        password.setSelection(s.length());
                    }
                    pwd_lay.setError(null);
                    if (!TextUtils.isEmpty(password.getText().toString())) {
                        confirm.setVisibility(View.VISIBLE);
                    } else {
                        confirm.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirm.setOnClickListener(v -> {
            try {

                if (!BlazeUtils.isStringNotEmpty(password.getText().toString())) {
                    pwd_lay.setError(getString(R.string.invalid_password));
                    return;
                }

                if (password.length() < 8) {
                    pwd_lay.setError(getString(R.string.password_length_error2));
                    return;
                }
                String SSIDForSocket = ssidName.getText().toString().trim();
                String passwordForSocket = password.getText().toString().trim();
                progressDialog.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
                BlazeCallBack callback = new BlazeCallBack() {
                    @Override
                    public void onSuccess(BlazeResponse blazeResponse) {
                        progressDialog.dismissProgress();
                        if (!model.isInSetup) {
                            alertDialog.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                        } else
                            alertDialog.showAlertMessage(getChildFragmentManager(), getString(R.string.app_name),
                                    getString(R.string.connect_mobile_to_home_msg));
                        alertDialog.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_nav_ssid_to_nav_hub_status));
                    }

                    @Override
                    public void onError(BlazeResponse blazeResponse) {

                        progressDialog.dismissProgress();
                        alertDialog.setOkButtonListener(getString(R.string.ok), model.isInSetup ? v -> goToSettings() : null);
                        alertDialog.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    }
                };
                if (!model.isInSetup) {
                    BlazeSDK.changeAccessPoint(SSIDForSocket, passwordForSocket, model.hubId, model.isHubOnline ? "1" : "0", callback);
                } else {
                    BlazeSDK.sendWifiCredentials(SSIDForSocket, passwordForSocket, model.hubId, callback);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        if (!TextUtils.isEmpty(model.selectedSSID)) {
            ssidName.setText(model.selectedSSID);
        } else {
            String ssid = BlazeNetworkUtils.getWifiSSID(context);
            if (ssid != null) {
                ssidName.setText(ssid);
            }
        }
    }

    private void goToSettings() {
        try {
            Intent in = new Intent(Settings.ACTION_WIFI_SETTINGS);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
