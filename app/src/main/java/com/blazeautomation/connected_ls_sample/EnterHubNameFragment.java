package com.blazeautomation.connected_ls_sample;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.blazeautomation.connected_ls_sample.localdatabase.DatabaseClient;
import com.blazeautomation.connected_ls_sample.localdatabase.PhotoModel;
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

    private MessageAlertDialog alert;
    private MessageProgressDialog progress;

    public EnterHubNameFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new MessageAlertDialog(requireActivity());
        progress = new MessageProgressDialog(requireActivity());
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
        pwd.setText(model.hubId);

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

            progress.showProgress(getChildFragmentManager(), "Registering hub " + model.hubId);

            BlazeSDK.addHub(model.hubId, str, new BlazeCallBack() {

                @Override
                public void onSuccess(BlazeResponse blazeResponse) {

                    progress.dismissProgress();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("installerId", "android");

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<ResponseBody> call = apiInterface.install_hub(/*token,*/ model.hubId, hashMap);

                    progress.showProgress(getChildFragmentManager(), "Completing installation for " + model.hubId);

                    call.enqueue(new Callback<ResponseBody>() {

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            progress.dismissProgress();

                            if (response.code() == 200) {
                                Toast.makeText(getActivity(), "Hub Installed successfully", Toast.LENGTH_SHORT).show();
                                model.setSelectedHubName(str);
                                saveTask();
                                gotoF(R.id.action_to_nav_dashboard);

                            } else {
                                alert.showAlertMessage(getChildFragmentManager(), response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            progress.dismissProgress();
                            alert.showAlertMessage(getChildFragmentManager(), t.getMessage());

                        }
                    });
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            });
        });
    }
    
    private void saveTask() {

        // final File file = new File(audioSavePathInDevice);


        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //saving photo sensor data
                PhotoModel hub = new PhotoModel();
                hub.setHubId(model.hubId);
                hub.setHub_name(model.hubName);
                //adding to database
                DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .taskDao()
                        .insertHubs(hub);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


            }
        }

        SaveTask st = new SaveTask();
        st.execute();
    }
}
