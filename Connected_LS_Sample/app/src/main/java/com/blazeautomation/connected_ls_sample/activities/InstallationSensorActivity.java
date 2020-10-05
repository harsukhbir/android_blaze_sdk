package com.blazeautomation.connected_ls_sample.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.blazeautomation.connected_ls_sample.BlazeHub;
import com.blazeautomation.connected_ls_sample.R;
import com.blazeautomation.connected_ls_sample.adapter.DoorAdpater;
import com.blazeautomation.connected_ls_sample.adapter.MotionAdapter;
import com.blazeautomation.connected_ls_sample.adapter.OtherAdapter;
import com.blazeautomation.connected_ls_sample.adapter.TempAdapter;

import java.util.ArrayList;

public class InstallationSensorActivity extends AppCompatActivity implements View.OnClickListener {
    Context context=this;
    private TextView btnNext;
    private ImageView back_arrow;
    RecyclerView motion_recycler,door_recycler,temp_recycler,other_recycler;
    ArrayList<BlazeHub>motion_list=new ArrayList<>();
    ArrayList<BlazeHub>door_list=new ArrayList<>();
    ArrayList<BlazeHub>temp_list=new ArrayList<>();
    ArrayList<BlazeHub>other_list=new ArrayList<>();
    DoorAdpater doorAdpater;
    MotionAdapter motionAdapter;
    OtherAdapter otherAdapter;
    TempAdapter tempAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_sensor);
        findId();
        setDoorAdapter();
        setMotionAdapter();
        setOtherAdapter();
        setTempAdapter();

    }

    private void setTempAdapter() {
        tempAdapter=new TempAdapter(context,temp_list);
        temp_recycler.setAdapter(tempAdapter);

    }

    private void setDoorAdapter() {
        doorAdpater=new DoorAdpater(context,door_list);
        door_recycler.setAdapter(doorAdpater);

    }

    private void setOtherAdapter() {
        otherAdapter=new OtherAdapter(context,other_list);
        other_recycler.setAdapter(otherAdapter);

    }

    private void setMotionAdapter() {
        motionAdapter=new MotionAdapter(context,motion_list);
        motion_recycler.setAdapter(motionAdapter);
    }

    private void findId() {
        btnNext=findViewById(R.id.btnNext);
        back_arrow=findViewById(R.id.back_arrow);
        motion_recycler=findViewById(R.id.motion_recycler);
        door_recycler=findViewById(R.id.door_recycler);
        temp_recycler=findViewById(R.id.temp_recycler);
        other_recycler=findViewById(R.id.other_recycler);
        motion_recycler.setLayoutManager(new LinearLayoutManager(context));
        motion_recycler.setHasFixedSize(true);
        door_recycler.setLayoutManager(new LinearLayoutManager(context));
        door_recycler.setHasFixedSize(true);
        temp_recycler.setLayoutManager(new LinearLayoutManager(context));
        temp_recycler.setHasFixedSize(true);
        other_recycler.setLayoutManager(new LinearLayoutManager(context));
        other_recycler.setHasFixedSize(true);

        btnNext.setOnClickListener(this);
        back_arrow.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                startActivity(new Intent(context,SensorActivity.class));

                break;
            case R.id.back_arrow:
                onBackPressed();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}