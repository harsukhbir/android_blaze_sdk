package com.blazeautomation.connected_ls_sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.BlazeAutomation.ConnectedLS.DeviceType;
import com.blazeautomation.connected_ls_sample.retrofit.ApiClient;
import com.blazeautomation.connected_ls_sample.retrofit.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class PairDeviceFragment extends NavigationXFragment {
    String categoryId = null;
    private ProgressFragment progress;
    private AlertFragment alert;
    private String nodeId, bOneId;
    private View add_lay;
    private TextView b_one_id, node_id;
    private ImageView imageView;
    private String encodedImage = "";
    String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJYelM3Y0tka1VXRm9mWVZUR2pYM0ZIbVVNSVQ1cnpGd2k2TW5LQktmYnI0In0.eyJleHAiOjE2MDI3Njc0NTksImlhdCI6MTYwMjc2NTY1OSwiYXV0aF90aW1lIjoxNjAyNzYxMzY3LCJqdGkiOiI4NjQwZWEyMS1iMjM3LTRkN2MtODY4Ni03OTIxOTg5YTkyOTgiLCJpc3MiOiJodHRwczovL2F1dGguZGV2LmRhdGFkcml2ZW5jYXJlLm5ldC9hdXRoL3JlYWxtcy9kZGMiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzVjMDhhOWEtNTBhYy00NGMyLTk5YTYtMmE4OWU0MjkzODE4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGRjLXdlYiIsIm5vbmNlIjoiNzg5YmYwYTYtNzJiZi00ZDQ4LTkyOWItMGQ2NWIyZjNmOGI2Iiwic2Vzc2lvbl9zdGF0ZSI6IjE5OWQ2N2QwLTU5NzctNGY4NC04ZGM2LTM2MmM5OGQ1MjdiZCIsImFjciI6IjAiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9hZG1pbi5kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cHM6Ly9kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cDovL2xvY2FsaG9zdDozMDAxIiwiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwic3VwZXJBZG1pbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoia2FyZWVtIn0.L34jBuqkkFQjMLPsMUJiA7ZDD6JDqtMchabC5nHdpgqnNkA3z3vIyMLui4l-T5unB6QQLXCAENJaHKk3S39iMJVPJNcEMjAlgj6zBXQyShdqXQ5XdTByq1WdND6qfdpeDuqy4fzelThvsmwSecFOtaBrSFbq6nTOc_8H8JW_aRMc4gmQB-eLrSeDn4PQ72TuPIjOlnIDk7oz6pgDVsYc0FX1P-qeX3EsJPAnh7-f1l735VMeDKUx0eSC10LVEOg1s52XMgA6Fjx86eteutOTCgzu8YGKW9Jgg5jrIvFdddCP5N7yTK1cfcl9EEUjcirBRmLaL1R7qOHQuCaRem2MXg";

    public PairDeviceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressFragment();
        alert = new AlertFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pair, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arg = getArguments();

        if (arg != null) {
            categoryId = arg.getString("cat_type", null);
        }
        if (categoryId == null) {
            goBack();
            return;
        }
        view.findViewById(R.id.btn_pair).setOnClickListener(v -> {
            pairDevice(categoryId);
        });

        add_lay = view.findViewById(R.id.add_lay);
        b_one_id = view.findViewById(R.id.b_one_id);
        node_id = view.findViewById(R.id.node_id);
        imageView = view.findViewById(R.id.imageView);
        TextInputLayout nameLay = view.findViewById(R.id.name_lay);
        TextInputEditText name = view.findViewById(R.id.name);

        /* implement add device DDC Api on 15 oct 2020 */

        view.findViewById(R.id.btn_add).setOnClickListener(v -> {

            /**String nameStr = name.getText().toString().trim();

             if (TextUtils.isEmpty(nameStr)) {
             nameLay.setError("Please Enter a name");
             return;
             }*/

            //addDevice(arg.getString("location", null));
            if (encodedImage != null && !encodedImage.equalsIgnoreCase("")) {
                addSensorApi();
            } else {
                Toast.makeText(context, "Please upload image", Toast.LENGTH_SHORT).show();
            }

        });


        /* implement camera functionality on 15 oct 2020 */

        view.findViewById(R.id.button).setOnClickListener(v -> {
            requestPermission();
        });

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        } else {
            openCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCameraIntent();
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);
                encodedImage = encodeImage(imageBitmap);

            }
        }
    }

    private void addSensorApi() {
        progress.showProgress(getChildFragmentManager(), getString(R.string.creating_blaze_account));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("installationPhoto", encodedImage);
        hashMap.put("location", "bathroom");
        hashMap.put("model", "doorv1");
        hashMap.put("pairingId", "string");
        hashMap.put("type", "door");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.addSensor(token, "C44F33354375", hashMap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progress.dismissProgress();

                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Sensor added successfully", Toast.LENGTH_SHORT).show();
                    gotoF(R.id.action_to_nav_dashboard);

                } else {
                    Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progress.dismissProgress();


            }
        });

    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);
        return encImage;
    }

    private void addDevice(String nameStr) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.addDevice(model.hubId, categoryId, DeviceType.ZIGBEE, nameStr, nodeId, bOneId, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    // TODO Add DDC API call /hub/{id}/sensors
                    alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_to_nav_dashboard));
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                } else {
                    Loggers.error("_add_error", blazeResponse);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());

                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {

                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    // TODO Add DDC API call /hub/{id}/sensors
                    alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_to_nav_dashboard));
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                } else {
                    Loggers.error("_add_error", blazeResponse);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }

                /**Loggers.error("_add_error", blazeResponse);
                 progress.dismissProgress();
                 alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());*/
            }
        });
    }

    private void pairDevice(String cat_type) {
        progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
        BlazeSDK.pairDevice(model.hubId, cat_type, DeviceType.ZIGBEE, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {
                progress.dismissProgress();
                if (blazeResponse.getStatus()) {
                    Loggers.error("_pair_success", blazeResponse);
//                alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    nodeId = blazeResponse.getNodeId();
                    bOneId = blazeResponse.getbOneId();
                    b_one_id.setText(bOneId);
                    node_id.setText(nodeId);
                    add_lay.setVisibility(View.VISIBLE);
                } else {
                    Loggers.error("_pair_error", blazeResponse);
                    add_lay.setVisibility(View.GONE);

                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {

                progress.dismissProgress();

                nodeId = "test";
                bOneId = "test";
                b_one_id.setText(bOneId);
                node_id.setText(nodeId);
                add_lay.setVisibility(View.VISIBLE);

                /**Loggers.error("_pair_error", blazeResponse);
                 progress.dismissProgress();
                 add_lay.setVisibility(View.GONE);
                 alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                 */
            }
        });
    }

}
