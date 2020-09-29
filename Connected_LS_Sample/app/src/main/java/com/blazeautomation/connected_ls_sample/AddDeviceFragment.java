package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;


public class AddDeviceFragment extends NavigationXFragment {
    public static final String HUB = "HUB";
    public static final String ZIGBEE_DOOR = "ZB01";
    public static final String ZIGBEE_SOS = "ZB10";
    public static final String ZIGBEE_MOTION = "ZB00";
    public static final String ZIGBEE_TEMP_HUMIDITY = "ZB02";
    private ProgressFragment progress;
    private AlertFragment alert;

    public AddDeviceFragment() {
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
        return inflater.inflate(R.layout.fragment_add_blaze_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.zigbee_door).setOnClickListener(v -> {
            pairDevice(AddDeviceFragment.ZIGBEE_DOOR);
        });
        view.findViewById(R.id.zigbee_motion).setOnClickListener(v -> {
            pairDevice(ZIGBEE_MOTION);
        });
        view.findViewById(R.id.zigbee_temp).setOnClickListener(v -> {
            pairDevice(ZIGBEE_TEMP_HUMIDITY);
        });
        view.findViewById(R.id.zigbee_sos).setOnClickListener(v -> {
            pairDevice(ZIGBEE_SOS);
        });
    }

    private void pairDevice(String cat_type) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.checkHubStatus(model.hubId, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                Bundle b = new Bundle();
                b.putString("cat_type", cat_type);
                gotoF(R.id.action_nav_add_to_nav_pair, b);
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                Loggers.error("_pair_error", blazeResponse);
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }

}
