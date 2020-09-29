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


public class EnterHubNameFragment extends NavigationXFragment {
    private AlertFragment alert;
    private ProgressFragment progress;

    public EnterHubNameFragment() {
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
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model.ssid = null;
        TextInputLayout pwd_lay = view.findViewById(R.id.pwd_lay);
        TextInputEditText pwd = view.findViewById(R.id.pwd);
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            String str = pwd.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                pwd_lay.setError("Please enter a name for the Hub.");
                return;
            } else
                pwd_lay.setError(null);
            progress.showProgress(getChildFragmentManager(), getString(R.string.creating_blaze_account));
            BlazeSDK.addHub(model.hubId, str, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    model.setSelectedHubName(str);
                    gotoF(R.id.action_to_nav_dashboard);
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            });
        });
    }
}
