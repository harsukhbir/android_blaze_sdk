package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.BlazeAutomation.ConnectedLS.DeviceType;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class PairDeviceFragment extends NavigationXFragment {
    String categoryId = null;
    private ProgressFragment progress;
    private AlertFragment alert;
    private String nodeId, bOneId;
    private View add_lay;
    private TextView b_one_id, node_id;

    public PairDeviceFragment() {
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
        return inflater.inflate(R.layout.fragment_pair, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arg = getArguments();

        if (arg != null) {
            categoryId = arg.getString("cat_type", null);
        }
        if (categoryId == null) {
            goBack();
            return;
        }
        view.findViewById(R.id.btn_pair).setOnClickListener(v -> {
            pairDevice(categoryId);
        });

        add_lay = view.findViewById(R.id.add_lay);
        b_one_id = view.findViewById(R.id.b_one_id);
        node_id = view.findViewById(R.id.node_id);
        TextInputLayout nameLay = view.findViewById(R.id.name_lay);
        TextInputEditText name = view.findViewById(R.id.name);
        view.findViewById(R.id.btn_add).setOnClickListener(v -> {
            String nameStr = name.getText().toString().trim();
            if (TextUtils.isEmpty(nameStr)) {
                nameLay.setError("Please Enter a name");
                return;
            }
            addDevice(nameStr);
        });
    }

    private void addDevice(String nameStr) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.addDevice(model.hubId, categoryId, DeviceType.ZIGBEE, nameStr, nodeId, bOneId, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_to_nav_dashboard));
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                } else {
                    Loggers.error("_add_error", blazeResponse);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                Loggers.error("_add_error", blazeResponse);
                progress.dismissProgress();
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }

    private void pairDevice(String cat_type) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.pairDevice(model.hubId, cat_type, DeviceType.ZIGBEE, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    Loggers.error("_pair_success", blazeResponse);
//                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    nodeId = blazeResponse.getNodeId();
                    bOneId = blazeResponse.getbOneId();
                    b_one_id.setText(bOneId);
                    node_id.setText(nodeId);
                    add_lay.setVisibility(View.VISIBLE);
                } else {
                    Loggers.error("_pair_error", blazeResponse);
                    add_lay.setVisibility(View.GONE);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                Loggers.error("_pair_error", blazeResponse);
                progress.dismissProgress();
                add_lay.setVisibility(View.GONE);
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }

}
