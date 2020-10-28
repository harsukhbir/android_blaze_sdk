package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.BlazeAutomation.ConnectedLS.DeviceType;
import com.google.android.material.textfield.TextInputLayout;


public class SettingsFragment extends NavigationXFragment {
    private MessageAlertDialog alert;
    private MessageProgressDialog progress;
    private AppCompatToggleButton btnBleEnable;

    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnBleEnable = (AppCompatToggleButton) view.findViewById(R.id.btnBleEnable);
        TextInputLayout duration_lay = view.findViewById(R.id.duration_lay);
        EditText textBleEnableTime = view.findViewById(R.id.textBleEnableTime);
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.checkHubStatus(model.hubId, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    if (blazeResponse.getStatus()) {
                        deleteHub(false);
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
                        alert.setOkButtonListener(getString(R.string.ok), null);
                        alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    }
                }
            });

        });

        view.findViewById(R.id.btnZigbee).setOnClickListener(v -> {
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.checkHubStatus(model.hubId, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    if (blazeResponse.getStatus()) {
                        deleteDevices(DeviceType.ZIGBEE, false);
                    } else {
                        progress.dismissProgress();
                        forceDeleteAllDevices(DeviceType.ZIGBEE);
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    if (blazeResponse.getResponseCode() == 444) {
                        forceDeleteAllDevices(DeviceType.ZIGBEE);
                    } else {
                        alert.setOkButtonListener(getString(R.string.ok), null);
                        alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    }
                }
            });
        });
        btnBleEnable.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!btnBleEnable.isPressed())
                return;
            final int duration;
            if (b) {
                String s = textBleEnableTime.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    duration_lay.setError("Enter a valid number between 5 - 999");
                    btnBleEnable.setChecked(false);
                    return;
                }
                duration = Integer.parseInt(s);
                if (duration < 5 || duration > 999) {
                    duration_lay.setError("Enter a valid number between 5 - 999");
                    btnBleEnable.setChecked(false);
                    return;
                }
            } else {
                duration = 0;
            }
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.enableBleDeviceList(model.hubId, b, duration, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    btnBleEnable.setChecked(blazeResponse.getStatus() == b);
                    model.storeBleListInterval(blazeResponse.getStatus() == b, duration);
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    btnBleEnable.setChecked(!b);
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            });
        });
        btnBleEnable.setChecked(model.isbleListEnabled);
        textBleEnableTime.setText(String.valueOf(model.interval));
        view.findViewById(R.id.btnChangeAccess).setOnClickListener(v -> {
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            String ssid = BlazeNetworkUtils.getWifiSSID(context);
            if (!TextUtils.isEmpty(ssid)) {
                if (TextUtils.equals(ssid, BlazeNetworkUtils.CONN_TYPE_GPRS)) {
                    progress.dismissProgress();
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), "Turn off mobile data");
                    return;
                }
                if (ssid.contains(model.hubId)) {
                    progress.dismissProgress();
                    model.isInSetup = false;
                    model.isHubOnline = false;
                    gotoF(R.id.action_nav_hub_settings_to_nav_ssid);
                } else {
                    BlazeSDK.checkHubStatus(model.hubId, new BlazeCallBack() {
                        @Override
                        public void onSuccess(BlazeResponse blazeResponse) {
                            progress.dismissProgress();
                            model.isInSetup = false;
                            model.isHubOnline = true;
                            gotoF(R.id.action_nav_hub_settings_to_nav_ssid);
                        }

                        @Override
                        public void onError(BlazeResponse blazeResponse) {
                            progress.dismissProgress();
                            model.isInSetup = false;
                            model.isHubOnline = false;
                            alert.setCancelButtonVisibility(View.GONE);
                            alert.setOkButtonListener(getString(R.string.ok), null);
                            if (blazeResponse.getResponseCode() == 222) {
                                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                            } else
                                alert.showAlertMessage(getChildFragmentManager(), "Hub seems to be Offline. Please connect to " + BlazeUtils.HUB_SSID_WITH_BRACKET + model.hubId + ") in Wi-Fi settings. And turnoff mobile data.");
                        }
                    });
                }

            }
        });
    }

    private void deleteDevices(DeviceType type, boolean forceDelete) {
        BlazeCallBack callBack = new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                alert.setCancelButtonVisibility(View.GONE);
                alert.setOkButtonListener(getString(R.string.ok), null);
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                alert.setCancelButtonVisibility(View.GONE);
                alert.setOkButtonListener(getString(R.string.ok), null);
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        };
        if (!forceDelete)
            BlazeSDK.deleteAllDevices(model.hubId, type, callBack);
        else
            BlazeSDK.forceDeleteAllDevices(model.hubId, type, callBack);
    }

    private void forceDeleteAllDevices(DeviceType deviceType) {
        alert.setOkButtonListener("YES", v -> deleteDevices(deviceType, true));
        alert.setCancelButtonListener("No", null);
        alert.showAlertMessage(getChildFragmentManager(), "Hub is offline. Do you want to delete all devices from cloud?", true);
    }

    private void forceDelete() {
        alert.setOkButtonListener("YES", v -> deleteHub(true));
        alert.setCancelButtonListener("No", null);
        alert.showAlertMessage(getChildFragmentManager(), "Hub is offline. Do you want to force delete the hub?", true);
    }

    private void deleteHub(boolean force) {
        BlazeSDK.getOtpToDeleteHub(model.hubId, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    model.resetCode = blazeResponse.getCode();
                    Bundle b = new Bundle();
                    b.putBoolean("force", force);
                    gotoF(R.id.action_nav_hub_settings_to_nav_hub_reset, b);
                } else {
                    alert.setOkButtonListener(getString(R.string.ok), null);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                alert.setOkButtonListener(getString(R.string.ok), null);
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }
}
