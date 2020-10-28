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

public class ResetFragment extends NavigationXFragment {
    private MessageAlertDialog alert;
    private MessageProgressDialog progress;
    boolean force = false;

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
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_reset_hub, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle b = getArguments();
        if (b != null) {
            force = b.getBoolean("force", false);
        }
        TextInputLayout pwd_lay = view.findViewById(R.id.pwd_lay);
        TextInputEditText pwd = view.findViewById(R.id.pwd);
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            String str = pwd.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                pwd_lay.setError("Enter OTP");
                return;
            } else
                pwd_lay.setError(null);
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeCallBack callback = new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.setCancelButtonVisibility(View.GONE);

                    if (blazeResponse != null && blazeResponse.getStatus()) {
                        model.clearHubDetails();
                        alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_to_nav_hub_list));
                        alert.showAlertMessage(getChildFragmentManager(), "Hub has been reset successfully");
                    } else {
                        alert.setOkButtonListener(R.string.ok, null);
                        alert.showAlertMessage(getChildFragmentManager(), "Please enter a valid OTP");
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            };
            if (force) {
                BlazeSDK.forceDeleteHub(model.hubId, model.resetCode, str, callback);
            } else {
                BlazeSDK.deleteHub(model.hubId, model.resetCode, str, callback);
            }
        });
    }
}
