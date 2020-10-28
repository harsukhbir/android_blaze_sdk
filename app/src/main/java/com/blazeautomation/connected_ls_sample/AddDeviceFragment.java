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
    private MessageProgressDialog progress;
    private MessageAlertDialog alert;

    public AddDeviceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new MessageProgressDialog(requireContext());
        alert = new MessageAlertDialog(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_blaze_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.bathroom1).setOnClickListener(v -> {
            pairDevice(AddDeviceFragment.ZIGBEE_MOTION, "bathroom1","motion","motionv1");
        });
        view.findViewById(R.id.bathroom2).setOnClickListener(v -> {
            pairDevice(AddDeviceFragment.ZIGBEE_MOTION, "bathroom2","motion","motionv1");
        });
        view.findViewById(R.id.livingroom).setOnClickListener(v -> {
            pairDevice(ZIGBEE_MOTION, "livingroom","motion","motionv1");
        });
        view.findViewById(R.id.hallway).setOnClickListener(v -> {
            pairDevice(ZIGBEE_MOTION, "hallway","motion","motionv1");
        });
        view.findViewById(R.id.kitchen).setOnClickListener(v -> {
            pairDevice(ZIGBEE_MOTION, "dining room","motion","motionv1");
        });
        view.findViewById(R.id.front_door).setOnClickListener(v -> {
            pairDevice(ZIGBEE_DOOR, "frontdoor","door","doorv1");
        });
        view.findViewById(R.id.back_door).setOnClickListener(v -> {
            pairDevice(ZIGBEE_DOOR, "backdoor","door","doorv1");
        });
        view.findViewById(R.id.fridge).setOnClickListener(v -> {
            pairDevice(ZIGBEE_DOOR, "fridge","door","doorv1");
        });
        view.findViewById(R.id.bathroom_humidity).setOnClickListener(v -> {
            pairDevice(ZIGBEE_TEMP_HUMIDITY, "bathroom","temp","tempv1");
        });
        view.findViewById(R.id.sos).setOnClickListener(v -> {
            pairDevice(ZIGBEE_SOS, "sos","sos","sosv1");
        });

    }

    private void pairDevice(String cat_type, String location,String type,String sensor_model) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.checkHubStatus(model.hubId, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                Bundle b = new Bundle();
                b.putString("cat_type", cat_type);
                b.putString("location", location);
                b.putString("type", type);
                b.putString("sensor_model", sensor_model);
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
