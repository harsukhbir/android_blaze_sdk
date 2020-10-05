package com.blazeautomation.connected_ls_sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blazeautomation.connected_ls_sample.BlazeHub;
import com.blazeautomation.connected_ls_sample.R;

import java.util.ArrayList;

public class DoorAdpater extends RecyclerView.Adapter<DoorAdpater.ViewHolder> {
    Context context;
    ArrayList<BlazeHub>door_list=new ArrayList<>();

    public DoorAdpater(Context context, ArrayList<BlazeHub> door_list) {
        this.context=context;
        this.door_list=door_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.door_list,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
