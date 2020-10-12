package com.blazeautomation.connected_ls_sample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.blazeautomation.connected_ls_sample.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddHubActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btnNext;
    Context context = this;
    private ImageView back_arrow;
    private EditText hub_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hub);
        findId();
    }

    private void findId() {
        btnNext = findViewById(R.id.btnNext);
        back_arrow = findViewById(R.id.back_arrow);
        hub_id = findViewById(R.id.hub_id);
        btnNext.setOnClickListener(this);
        back_arrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
             /*   if (hub_id.getText().toString().isEmpty()) {

                    hub_id.setError("Please enter a name for the Hub.");
                    return;
                } else {
                    hub_id.setError(null);*/
                    startActivity(new Intent(context, DiscoverWifiActivity.class));

                   /* BlazeSDK.addHub(hub_id.getText().toString().trim(), "android", new BlazeCallBack() {
                        @Override
                        public void onSuccess(BlazeResponse blazeResponse) {

                            Log.e("log_value","onSuccess");
                            startActivity(new Intent(context, DiscoverWifiActivity.class));

                        }

                        @Override
                        public void onError(BlazeResponse blazeResponse) {
                            Log.e("log_value","onError");

                            Log.e("log_value",blazeResponse.message);

                        }
                    });*/
               // }
                break;

            case R.id.back_arrow:
                onBackPressed();
                break;
        }
    }
}