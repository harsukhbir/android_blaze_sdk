package com.blazeautomation.connected_ls_sample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blazeautomation.connected_ls_sample.R;

public class WifiRouterActivity extends AppCompatActivity implements View.OnClickListener {
    Context context=this;
    private TextView btnNext;
    private ImageView back_arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_router);
        findId();
    }

    private void findId() {
        btnNext=findViewById(R.id.btnNext);
        back_arrow=findViewById(R.id.back_arrow);
        btnNext.setOnClickListener(this);
        back_arrow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                startActivity(new Intent(context,InstallationSensorActivity.class));

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