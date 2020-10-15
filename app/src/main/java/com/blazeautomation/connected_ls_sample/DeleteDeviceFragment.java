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
import com.BlazeAutomation.ConnectedLS.DeviceType;


public class DeleteDeviceFragment extends NavigationXFragment {
    String categoryId = null, bone_id;
    private ProgressFragment progress;
    private AlertFragment alert;

    public DeleteDeviceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressFragment();
        alert = new AlertFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arg = getArguments();

        if (arg != null) {
            bone_id = arg.getString("bone_id", null);
        }

        if (TextUtils.isEmpty(bone_id)) {
            goBack();
            return;
        }
        view.findViewById(R.id.btn_un_pair).setOnClickListener(v -> unPairDevice());

    }

    private void unPairDevice() {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.checkHubStatus(model.hubId, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    deleteDevice(false);
                } else {
                    forceDelete();
                }
            }


            @Override
            public void onError(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getResponseCode() == 444) {
                    forceDelete();
                } else {
                    alert.setCancelButtonVisibility(View.GONE);
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            }
        });

    }

    private void forceDelete() {
        alert.setOkButtonListener("YES", v -> deleteDevice(true));
        alert.setCancelButtonListener("No", null);
        alert.showAlertMessage(getChildFragmentManager(), "Device/Hub is offline. Do you want to force delete the Device?");
    }

    private void deleteDevice(boolean forceDelete) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeCallBack callback = new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    Loggers.error("_un_pair_success", blazeResponse);
                    alert.setCancelButtonVisibility(View.GONE);
                    alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_to_nav_dashboard));
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());

                } else {
                    alert.setCancelButtonVisibility(View.GONE);
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    Loggers.error("_un_pair_error", blazeResponse);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                Loggers.error("_un_pair_error", blazeResponse);
                progress.dismissProgress();
                if (!forceDelete && blazeResponse.getResponseCode() == 444 && !TextUtils.isEmpty(blazeResponse.getMessage())) {
                    forceDelete();
                } else {
                    alert.setCancelButtonVisibility(View.GONE);
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            }
        };
        if (!forceDelete) {
            BlazeSDK.deleteDevice(model.hubId, bone_id, DeviceType.ZIGBEE, callback);
        } else {
            BlazeSDK.forceDeleteDevice(model.hubId, bone_id, DeviceType.ZIGBEE, callback);
        }
    }
}
