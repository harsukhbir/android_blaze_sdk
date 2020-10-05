package com.blazeautomation.connected_ls_sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blazeautomation.connected_ls_sample.BlazeHub;
import com.blazeautomation.connected_ls_sample.R;

import java.util.ArrayList;

public class MotionAdapter extends RecyclerView.Adapter<MotionAdapter.ViewHolder> {
    Context context;
    ArrayList<BlazeHub>motion_list=new ArrayList<>();

    public MotionAdapter(Context context, ArrayList<BlazeHub> motion_list) {
        this.context=context;
        this.motion_list=motion_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.motion_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 4 ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            status=itemView.findViewById(R.id.status);
        }
    }
}
