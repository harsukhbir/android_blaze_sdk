package com.blazeautomation.connected_ls_sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class HubWifiListAdapter extends RecyclerView.Adapter<HubWifiListAdapter.MyViewHolder> {

    private final ArrayList<JsonObject> data;
    private final AdapterView.OnItemClickListener listener;
    private Context mContext;

    public HubWifiListAdapter(Context context, ArrayList<JsonObject> data, AdapterView.OnItemClickListener onItemClickListener) {
        mContext = context;
        this.data = data;
        listener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowViewObj = LayoutInflater.from(mContext).inflate(R.layout.list_item_connect_hub, parent, false);
        return new MyViewHolder(rowViewObj);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        JsonObject object = data.get(position);
        holder.mWifiNmae.setText(object.get("name").getAsString());
        holder.mWifiSecurityType.setText(object.get("security_type").getAsString());
        holder.view.setOnClickListener(v -> listener.onItemClick(null, null, position, 0));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private TextView mWifiNmae, mWifiSecurityType;

        MyViewHolder(View rowViewObj) {
            super(rowViewObj);
            view = rowViewObj;
            mWifiNmae = rowViewObj.findViewById(R.id.txtWifiName);
            mWifiSecurityType = rowViewObj.findViewById(R.id.txtWifiSecurityType);
        }
    }
}
