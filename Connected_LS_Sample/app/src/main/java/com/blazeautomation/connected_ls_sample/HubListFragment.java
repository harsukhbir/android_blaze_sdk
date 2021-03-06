package com.blazeautomation.connected_ls_sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;


public class HubListFragment extends NavigationXFragment {
    private static final int REQ_PERMISSION = 54;
    private HubAdapter adapter;
    private ArrayList<BlazeHub> list = new ArrayList<>();
    private ProgressFragment progress;
    private AlertFragment alert;

    public HubListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressFragment();
        alert = new AlertFragment();
        adapter = new HubAdapter(context, list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hubs_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView hubList = view.findViewById(R.id.hub_list);
        hubList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        hubList.setAdapter(adapter);
        TextView hubs_txt = view.findViewById(R.id.hubs_txt);
        hubs_txt.setText(getString(R.string.hubs_available, list.size()));

        view.findViewById(R.id.logout).setOnClickListener(v -> BlazeSDK.logout(new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                model.logout();
                gotoF(R.id.action_to_nav_login);
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {

            }
        }));
        view.findViewById(R.id.add).setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQ_PERMISSION);
                return;
            }
            gotoManualScreen();
        });
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.getHubs(new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                list.clear();
                if (blazeResponse != null && blazeResponse.getStatus()) {
                    try {
                        JsonArray data = blazeResponse.getData().getAsJsonArray();
                        for (int i = 0; i < data.size(); i++) {
                            JsonObject jsonObject = data.get(i).getAsJsonObject();
                            BlazeHub hub = new BlazeHub();
                            hub.setHubId(jsonObject.get("hub_id").getAsString());
                            hub.setName(jsonObject.get("hub_name").getAsString());
                            hub.setFirmwareVersion(jsonObject.get("software_version").getAsString());
                            hub.setModelNumber(jsonObject.get("model_no").getAsString());
                            list.add(hub);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (list.size() == 0)
                    model.clearHubDetails();

                hubs_txt.setText(getString(R.string.hubs_available, list.size()));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }

    private void gotoManualScreen() {
        model.selectedSSID = BlazeNetworkUtils.getWifiSSID(context);
        gotoF(R.id.action_nav_hub_list_to_nav_manual);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotoManualScreen();
            }

        }
    }

    class HubAdapter extends RecyclerView.Adapter<HubAdapter.HubHolder> {
        private final Context context;
        private ArrayList<BlazeHub> list;

        public HubAdapter(Context context, ArrayList<BlazeHub> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public HubAdapter.HubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HubHolder(LayoutInflater.from(context).inflate(R.layout.hub_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull HubAdapter.HubHolder holder, int position) {
            BlazeHub hub = list.get(position);
            holder.hub_id.setText(hub.getHubId());
            holder.name.setText(hub.getName());
            holder.itemView.setOnClickListener(v -> {
                model.setSelectedHub(hub.getHubId());
                model.setSelectedHubName(hub.getName());
                gotoF(R.id.action_to_nav_dashboard);
            });
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class HubHolder extends RecyclerView.ViewHolder {
            private final TextView name, hub_id;

            public HubHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.hub_name);
                hub_id = itemView.findViewById(R.id.hub_id);
            }
        }
    }
}
