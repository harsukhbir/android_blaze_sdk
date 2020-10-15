package com.blazeautomation.connected_ls_sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;

import static com.blazeautomation.connected_ls_sample.BlazeNetworkUtils.CONN_TYPE_GPRS;


public class ManualFragment extends NavigationXFragment {

    private static final int REQ_PERM = 65;
    private final String HUB_SSID_WITH_BRACKET = "Home-Ultimate(";
    //private final String HUB_SSID_WITH_BRACKET1 = "Home-Plus(";
    private AlertFragment alertDialog;
    private ProgressFragment progress;

    public ManualFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manual, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialog = new AlertFragment();
        progress = new ProgressFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model.isInSetup = true;
        //if user exist only it will comes to this screen
        view.findViewById(R.id.btnNext).setOnClickListener(v -> connect());
        view.findViewById(R.id.btnSettings).setOnClickListener(v -> goToSettings());
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERM && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            connect();
        }
    }

    private void connect() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_PERM);
            return;
        }
       // String ssid = BlazeNetworkUtils.getWifiSSID(context);
        String ssid = "Home-Ultimate(MyHub)";

        if (TextUtils.equals(CONN_TYPE_GPRS, ssid)) {
            Toast.makeText(context, "Please Turn off mobile data.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ssid.contains(HUB_SSID_WITH_BRACKET)) {
            Toast.makeText(context, "Please connect to Home-Ultimate Hub.", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] split = ssid.split("\\(");
        model.ssid = split[1].substring(0, split[1].length() - 1);
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));

        BlazeSDK.connectHub(model.ssid, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                model.hubId = model.ssid;
                gotoF(R.id.action_nav_success_to_nav_ssid);
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {

                progress.dismissProgress();
                model.hubId = model.ssid;
                gotoF(R.id.action_nav_success_to_nav_ssid);

                /**
                progress.dismissProgress();
                alertDialog.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                 */
            }
        });
    }

}
