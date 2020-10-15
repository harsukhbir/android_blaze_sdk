package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.BlazeAutomation.ConnectedLS.BlazeSDKListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Locale;


public class DeviceFragment extends NavigationXFragment implements BlazeSDKListener {
    private ProgressFragment progress;
    private AlertFragment alert;
    private TextView text1, text2, text3;
    private Handler handler;
    private BlazeDevice device;

    public DeviceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        progress = new ProgressFragment();
        alert = new AlertFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BlazeSDK.setListener(this);
        TextInputLayout name_lay = view.findViewById(R.id.name_lay);
        EditText name = view.findViewById(R.id.name);
        text1 = view.findViewById(R.id.lux_t);
        text2 = view.findViewById(R.id.temp);
        text3 = view.findViewById(R.id.humidity);
        View refresh = view.findViewById(R.id.refresh);

        AppCompatToggleButton edit = view.findViewById(R.id.edit);
        Bundle arg = getArguments();

        if (arg != null) {
            device = (BlazeDevice) arg.getSerializable("device");
        }

        if (device == null) {
            goBack();
            return;
        }
        view.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("bone_id", device.getbOneId());
            gotoF(R.id.action_nav_device_hub_to_nav_un_pair, b);
        });
        name.setText(device.getName());
        refresh.setOnClickListener(v -> getStatus(model.hubId, device.getbOneId()));
        edit.setOnCheckedChangeListener((compoundButton, b) -> {

            if (!edit.isPressed())
                return;
            name_lay.setEnabled(b);
            if (!b) {
                String s = name.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    name_lay.setError("Enter a valid name");
                    edit.setChecked(true);
                    return;
                }
                progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
                BlazeSDK.updateDevice(model.hubId, device.getbOneId(), s, new BlazeCallBack() {
                    @Override
                    public void onSuccess(BlazeResponse blazeResponse) {
                        Loggers.error("_update_success", blazeResponse);
                        progress.dismissProgress();
                        device.setName(s);
                        alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    }

                    @Override
                    public void onError(BlazeResponse blazeResponse) {
                        progress.dismissProgress();
                        Loggers.error("_update_error", blazeResponse);
                        alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    }
                });
            }
        });
        handler.postDelayed(() -> getStatus(model.hubId, device.getbOneId()), 100);
    }

    private void getStatus(String hubID, String bOneID) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));

        BlazeSDK.getDeviceCurrentStatus(hubID, bOneID, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                updateUI(blazeResponse);
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                Loggers.error("_status_error", blazeResponse);
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }

    private void updateUI(BlazeResponse blazeResponse) {
        try {
            Loggers.error("_status_success", blazeResponse);
            JsonElement data1 = blazeResponse.getData();
            if (data1 != null) {
                JsonObject data = data1.getAsJsonObject();
                switch (device.getCategoryId()) {
                    case AddDeviceFragment.HUB: {
                        if (data.has("rpt")) {
                            try {
                                JsonObject rpt = data.getAsJsonObject("rpt");
                                if (rpt.has("lux_value")) {
                                    String lux_value = rpt.get("lux_value").getAsString();
                                    text1.setText(String.format(Locale.ENGLISH, "Light Intensity: %s", lux_value));
                                }
                                if (rpt.has("temp_value")) {
                                    String temp_value = rpt.get("temp_value").getAsString();
                                    text2.setText(getString(R.string.temp_dynamic, temp_value));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        text3.setVisibility(View.GONE);
                    }
                    break;
                    case AddDeviceFragment.ZIGBEE_DOOR: {
                        if (data.has("door_status")) {
                            String status = data.get("door_status").getAsString();
                            text1.setText(TextUtils.equals(status, "1") ? "Door Opened" : "Door Closed");
                        }

                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.GONE);
                        text3.setVisibility(View.GONE);
                    }
                    break;
                    case AddDeviceFragment.ZIGBEE_MOTION: {
                        if (data.has("motion_status")) {
                            String status = data.get("motion_status").getAsString();
                            text1.setText(TextUtils.equals(status, "1") ? "Motion Detected" : "Everything is OK");
                        }
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.GONE);
                        text3.setVisibility(View.GONE);
                    }
                    break;
                    case AddDeviceFragment.ZIGBEE_SOS: {

                        if (data.has("sos_status")) {
                            String status = data.get("sos_status").getAsString();
                            text1.setText(TextUtils.equals(status, "1") ? "Button Pressed" : "Everything is OK");
                        }
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.GONE);
                        text3.setVisibility(View.GONE);
                    }
                    break;
                    case AddDeviceFragment.ZIGBEE_TEMP_HUMIDITY: {
                        if (data.has("battery_status")) {
                            String battery = data.get("battery_status").getAsString();
                            String s = "Battery : " + (TextUtils.isEmpty(battery) ? "--" : battery);
                            text3.setText(s);
                        }
                        if (data.has("temperature_value")) {
                            String temperature = data.get("temperature_value").getAsString();
                            text1.setText(getString(R.string.temp_dynamic, TextUtils.isEmpty(temperature) ? "--" : temperature));
                        }
                        if (data.has("relative_humidity_value")) {
                            String humidityV = data.get("relative_humidity_value").getAsString();
                            String s = "Humidity : " + (TextUtils.isEmpty(humidityV) ? "--" : humidityV);
                            text2.setText(s);
                        }
                        text1.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        text3.setVisibility(View.VISIBLE);


                    }
                    break;
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(@NonNull BlazeResponse blazeResponse) {
        updateUI(blazeResponse);
    }
}
