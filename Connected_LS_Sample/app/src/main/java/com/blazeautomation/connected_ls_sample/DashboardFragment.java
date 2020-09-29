package com.blazeautomation.connected_ls_sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class DashboardFragment extends NavigationXFragment {
    private ProgressFragment progress;
    private Handler handler;
    private HubAdapter adapter;
    private ArrayList<BlazeDevice> list = new ArrayList<>();
    private AlertFragment alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        progress = new ProgressFragment();
        alert = new AlertFragment();
        adapter = new HubAdapter(context, list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView hubList = view.findViewById(R.id.device_list);
        hubList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        hubList.setAdapter(adapter);
        TextView change_hub = view.findViewById(R.id.change_hub);
        change_hub.setOnClickListener(v -> gotoF(R.id.action_nav_dashboard_to_nav_hub_list));
        TextView hub_settings = view.findViewById(R.id.hub_settings);
        hub_settings.setOnClickListener(v -> gotoF(R.id.action_nav_dashboard_to_nav_hub_settings));
        TextView change_profile = view.findViewById(R.id.change_profile);
        change_profile.setOnClickListener(v -> gotoF(R.id.action_nav_dashboard_to_nav_user_profile));
        TextView selectedHubName = view.findViewById(R.id.selected_hub_name);
        TextView selectedHubId = view.findViewById(R.id.selected_hub_id);
        TextView name = view.findViewById(R.id.name);
        name.setText(getString(R.string.hi_dyn, model.getName()));
        selectedHubId.setText(model.getHubId());
        selectedHubName.setText(model.getHubName());
        view.findViewById(R.id.addDevice).setOnClickListener(v -> gotoF(R.id.action_nav_dashboard_to_nav_add));
        handler.postDelayed(() -> {
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.getDevices(model.hubId, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    if (blazeResponse != null && blazeResponse.getStatus()) {
                        try {
                            list.clear();
                            JsonArray data = blazeResponse.getDevices().getAsJsonArray();
                            for (int i = 0; i < data.size(); i++) {
                                JsonObject jsonObject = data.get(i).getAsJsonObject();
                                BlazeDevice hub = new BlazeDevice();
                                hub.setbOneId(jsonObject.get("device_b_one_id").getAsString());
                                hub.setName(jsonObject.get("device_name").getAsString());
                                hub.setCategoryId(jsonObject.get("category").getAsString());
                                hub.setDeviceCreateDate(jsonObject.get("create_at").getAsString());
                                list.add(hub);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            });
        }, 300);
    }

    class HubAdapter extends RecyclerView.Adapter<HubAdapter.HubHolder> {
        private final Context context;
        private ArrayList<BlazeDevice> list;

        public HubAdapter(Context context, ArrayList<BlazeDevice> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public HubAdapter.HubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new HubAdapter.HubHolder(LayoutInflater.from(context).inflate(R.layout.device_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull HubAdapter.HubHolder holder, int position) {
            BlazeDevice hub = list.get(position);
            holder.name.setText(hub.getName());
            holder.itemView.setOnClickListener(v -> {
                Bundle b = new Bundle();
                b.putSerializable("device", hub);
                gotoF(R.id.action_nav_dashboard_to_nav_device_hub, b);
            });
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public class HubHolder extends RecyclerView.ViewHolder {
            private final TextView name;

            public HubHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.device_name);
            }
        }
    }

}
