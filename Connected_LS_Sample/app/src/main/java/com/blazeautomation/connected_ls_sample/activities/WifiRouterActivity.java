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
    String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJYelM3Y0tka1VXRm9mWVZUR2pYM0ZIbVVNSVQ1cnpGd2k2TW5LQktmYnI0In0.eyJleHAiOjE2MDI1MDg1NjQsImlhdCI6MTYwMjUwNjc2NCwiYXV0aF90aW1lIjoxNjAyNTAyMTEwLCJqdGkiOiI2NzA3OGJiNi1mZmNmLTQ0NWYtYjQzMC03Y2NmOGIwMzc3MzgiLCJpc3MiOiJodHRwczovL2F1dGguZGV2LmRhdGFkcml2ZW5jYXJlLm5ldC9hdXRoL3JlYWxtcy9kZGMiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzVjMDhhOWEtNTBhYy00NGMyLTk5YTYtMmE4OWU0MjkzODE4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGRjLXdlYiIsIm5vbmNlIjoiM2IwZDA3NmItZDMyMy00ZWQ4LThhMTgtMmY3OTk5MmQwNzM2Iiwic2Vzc2lvbl9zdGF0ZSI6ImI0MjgxZDJiLTg2NGMtNDQ1MS04YmE3LTZmZjYyMDA1ODZkNSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9hZG1pbi5kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cHM6Ly9kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cDovL2xvY2FsaG9zdDozMDAxIiwiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwic3VwZXJBZG1pbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoia2FyZWVtIn0.G-1WStEIHXlnXmzkGnqRfCHkrgHu80cnRP5pb305zdPnN0YjBNxluxPZWLxc8Ibkso_CWrweB-CEyw5-addgNOoMt5bZgWPCycC_JLZxzUso88bnH5nLiGPXthSOKuF__2kKGkS0paenTaSrc39PCyT7AaPzjEHDAZkzQJlcBdvKfZBE6Oa30Ft0RhGvLKeExef3kdo1iaPQ1QKHm5DlBbSylYIP5yxy4VP6axdwBNHUihfXrVV3F1cMLTBjl1cJjZvJONC_IK3QduLolLLBpwWEuMORvt9j9gK1aN0EQZ7TzT8w45jawmhDJV6ZuEmFMmCw79lUthMgBztiF5j_2A";


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
                //hubInstallaionApi();
                startActivity(new Intent(context,CaptureLocationActivity.class));


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
                if (response.code()==200){
                    Toast.makeText(WifiRouterActivity.this, "Hub Install successfully", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(context,CaptureLocationActivity.class));

                }else {
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