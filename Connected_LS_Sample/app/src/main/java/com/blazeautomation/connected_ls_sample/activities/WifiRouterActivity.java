package com.blazeautomation.connected_ls_sample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.blazeautomation.connected_ls_sample.R;
import com.blazeautomation.connected_ls_sample.model.HubInstallation;
import com.blazeautomation.connected_ls_sample.retrofit.ApiClient;
import com.blazeautomation.connected_ls_sample.retrofit.ApiInterface;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WifiRouterActivity extends BaseActivity implements View.OnClickListener {
    Context context = this;
    private TextView btnNext;
    private ImageView back_arrow;
    String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJYelM3Y0tka1VXRm9mWVZUR2pYM0ZIbVVNSVQ1cnpGd2k2TW5LQktmYnI0In0.eyJleHAiOjE2MDI2ODA4NTAsImlhdCI6MTYwMjY3OTA1MCwiYXV0aF90aW1lIjoxNjAyNjYzMzQwLCJqdGkiOiIwMWFjOGU2OS04MWYzLTRjMTctOTZiNi0yM2FmMzAzZmQ2MTEiLCJpc3MiOiJodHRwczovL2F1dGguZGV2LmRhdGFkcml2ZW5jYXJlLm5ldC9hdXRoL3JlYWxtcy9kZGMiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzVjMDhhOWEtNTBhYy00NGMyLTk5YTYtMmE4OWU0MjkzODE4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGRjLXdlYiIsIm5vbmNlIjoiZWY1NzYwM2EtY2MzOC00NjUxLTgzNjYtMDk4MzA0ZTYyZWJiIiwic2Vzc2lvbl9zdGF0ZSI6ImRhYjVhOGFhLWNhODMtNGEzZS05ZGU3LWQ3MTliMmY3ZWJlNiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9hZG1pbi5kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cHM6Ly9kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cDovL2xvY2FsaG9zdDozMDAxIiwiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwic3VwZXJBZG1pbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoia2FyZWVtIn0.riDSHuxTEuXRXuWeoXGSrZyvjIbd2ZkAQufK2CcxDwSnv4IrsnhIsuSLV-v2qM6NQdGy9qM9q3MJBEXL6oCMPNZktdxp_5ijZN0n-n1B8v5YlsSgZL1aDeMdIQfxmHz2D1dcUuwVo1Jd5vJ2eas59HBz_JZxKwOIdwkNvUXcgW1w6y94slEhfxTKnW68a5ZedLnYUV-1pO_l1y_KqOrxpiKyE9RNssh0IxL4MSBET6njaEXGo7h2-RMhtvGbCjd38mSaoRzOLMP0dR9DorZWYx4RpJWRzAFtY5jeQcUaQovKfUEQ-SKpNal7sH3AUpMVlMlL8fFhiu6WUb8gEOsibw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_router);
        findId();
    }

    private void findId() {
        btnNext = findViewById(R.id.btnNext);
        back_arrow = findViewById(R.id.back_arrow);
        btnNext.setOnClickListener(this);
        back_arrow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                hubInstallaionApi();


                break;
            case R.id.back_arrow:
                onBackPressed();

                break;
        }
    }

    private void hubInstallaionApi() {

        showLoading("Please Wait...");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("installerId", "android");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.install_hub(token, "C44F33354375", hashMap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                if (response.code() == 200) {
                    Toast.makeText(WifiRouterActivity.this, "Hub Installed successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, InstallationSensorActivity.class));

                } else {
                    Toast.makeText(WifiRouterActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                hideLoading();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}