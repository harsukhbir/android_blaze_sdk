package com.blazeautomation.connected_ls_sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.blazeautomation.connected_ls_sample.retrofit.ApiClient;
import com.blazeautomation.connected_ls_sample.retrofit.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EnterHubNameFragment extends NavigationXFragment {
    private AlertFragment alert;
    private ProgressFragment progress;
    private String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJYelM3Y0tka1VXRm9mWVZUR2pYM0ZIbVVNSVQ1cnpGd2k2TW5LQktmYnI0In0.eyJleHAiOjE2MDI3Njc0NTksImlhdCI6MTYwMjc2NTY1OSwiYXV0aF90aW1lIjoxNjAyNzYxMzY3LCJqdGkiOiI4NjQwZWEyMS1iMjM3LTRkN2MtODY4Ni03OTIxOTg5YTkyOTgiLCJpc3MiOiJodHRwczovL2F1dGguZGV2LmRhdGFkcml2ZW5jYXJlLm5ldC9hdXRoL3JlYWxtcy9kZGMiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzVjMDhhOWEtNTBhYy00NGMyLTk5YTYtMmE4OWU0MjkzODE4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGRjLXdlYiIsIm5vbmNlIjoiNzg5YmYwYTYtNzJiZi00ZDQ4LTkyOWItMGQ2NWIyZjNmOGI2Iiwic2Vzc2lvbl9zdGF0ZSI6IjE5OWQ2N2QwLTU5NzctNGY4NC04ZGM2LTM2MmM5OGQ1MjdiZCIsImFjciI6IjAiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9hZG1pbi5kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cHM6Ly9kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cDovL2xvY2FsaG9zdDozMDAxIiwiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwic3VwZXJBZG1pbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoia2FyZWVtIn0.L34jBuqkkFQjMLPsMUJiA7ZDD6JDqtMchabC5nHdpgqnNkA3z3vIyMLui4l-T5unB6QQLXCAENJaHKk3S39iMJVPJNcEMjAlgj6zBXQyShdqXQ5XdTByq1WdND6qfdpeDuqy4fzelThvsmwSecFOtaBrSFbq6nTOc_8H8JW_aRMc4gmQB-eLrSeDn4PQ72TuPIjOlnIDk7oz6pgDVsYc0FX1P-qeX3EsJPAnh7-f1l735VMeDKUx0eSC10LVEOg1s52XMgA6Fjx86eteutOTCgzu8YGKW9Jgg5jrIvFdddCP5N7yTK1cfcl9EEUjcirBRmLaL1R7qOHQuCaRem2MXg";

    public EnterHubNameFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new AlertFragment();
        progress = new ProgressFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model.ssid = null;
        TextInputLayout pwd_lay = view.findViewById(R.id.pwd_lay);
        TextInputEditText pwd = view.findViewById(R.id.pwd);

        /* comment finish button previous funcionality and add it in below  code*/

        /*view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            String str = pwd.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                pwd_lay.setError("Please enter a name for the Hub.");
                return;
            } else
                pwd_lay.setError(null);
            progress.showProgress(getChildFragmentManager(), getString(R.string.creating_blaze_account));
            BlazeSDK.addHub(model.hubId, str, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    model.setSelectedHubName(str);
                    gotoF(R.id.action_to_nav_dashboard);
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            });
        });*/




        /*calling installation api  on 15 oct 2020*/
        view.findViewById(R.id.btnNext).setOnClickListener(v -> {
            String str = pwd.getText().toString().trim();
            if (TextUtils.isEmpty(str)) {
                pwd_lay.setError("Please enter a name for the Hub.");
                return;
            } else
                pwd_lay.setError(null);
            progress.showProgress(getChildFragmentManager(), getString(R.string.creating_blaze_account));
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("installerId", "android");

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.install_hub(token, "C44F33354375", hashMap);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progress.dismissProgress();
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), "Hub Installed successfully", Toast.LENGTH_SHORT).show();
                        model.setSelectedHubName(str);
                        gotoF(R.id.action_to_nav_dashboard);

                    } else {
                        Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), "failure");

                }
            });
        });

    }
}
