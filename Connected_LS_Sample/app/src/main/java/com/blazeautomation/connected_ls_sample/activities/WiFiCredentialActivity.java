package com.blazeautomation.connected_ls_sample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.blazeautomation.connected_ls_sample.R;

public class WiFiCredentialActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btnNext;
    Context context = this;
    private ImageView back_arrow;
    private EditText ss_id, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_credential);
        initId();
    }


    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }
    private void initId() {
        btnNext = findViewById(R.id.btnNext);
        back_arrow = findViewById(R.id.back_arrow);
        ss_id = findViewById(R.id.ss_id);
        password = findViewById(R.id.password);
        btnNext.setOnClickListener(this);
        back_arrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:

              /*  if (ss_id.getText().toString().isEmpty()) {

                    ss_id.setError("Please enter ssid");
                    return;
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Please enter password");
                    return;
                } else {*/
                    startActivity(new Intent(context, WifiRouterActivity.class));
                   /* BlazeSDK.sendWifiCredentials("airtell", "123456", "hars", new BlazeCallBack() {
                        @Override
                        public void onSuccess(BlazeResponse blazeResponse) {
                            Log.e("log_value","onSuccess");
                            Log.e("log_value",blazeResponse.message);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}