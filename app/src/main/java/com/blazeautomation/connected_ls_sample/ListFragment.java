package com.blazeautomation.connected_ls_sample;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.google.gson.JsonObject;

import java.util.ArrayList;


public class ListFragment extends NavigationXFragment {

    private static final String TAG = "_LIST";
    private ArrayList<JsonObject> arrayList = new ArrayList<>();
    private HubWifiListAdapter adapter;
    private MessageProgressDialog progressDialog;
    private MessageAlertDialog alertDialog;

    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new MessageProgressDialog(requireActivity());
        alertDialog = new MessageAlertDialog(requireActivity());
        adapter = new HubWifiListAdapter(context, arrayList, (adapterView, view, position, l) -> {
            Loggers.error(TAG, "selected SSID:-:" + arrayList.get(position));
            JsonObject obj = arrayList.get(position);
            model.selectedSSID = obj.get("name").getAsString();
            goBack();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView listViewWifiNetworks = view.findViewById(R.id.listWifiNetworkList);
        listViewWifiNetworks.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        listViewWifiNetworks.setAdapter(adapter);
        view.findViewById(R.id.refresh).setOnClickListener(v -> load());
        view.findViewById(R.id.manual).setOnClickListener(v -> gotoF(R.id.action_nav_list_to_nav_manual_ssid));
        load();
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

    private void load() {
        progressDialog.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.getWiFiList(model.hubId, !model.isInSetup && model.isHubOnline ? "1" : "0", new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progressDialog.dismissProgress();
                if (blazeResponse != null) {
                    arrayList.clear();
                    arrayList.addAll(blazeResponse.getWifiList());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                progressDialog.dismissProgress();
                alertDialog.setOkButtonListener(getString(R.string.ok), !model.isInSetup && model.isHubOnline ? null : v -> goToSettings());
                alertDialog.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }

}
