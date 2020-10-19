package com.blazeautomation.connected_ls_sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.BlazeAutomation.ConnectedLS.DeviceType;
import com.blazeautomation.connected_ls_sample.croping.Controller;
import com.blazeautomation.connected_ls_sample.croping.Croping;
import com.blazeautomation.connected_ls_sample.localdatabase.DatabaseClient;
import com.blazeautomation.connected_ls_sample.localdatabase.PhotoModel;
import com.blazeautomation.connected_ls_sample.model.PhotoSaveModel;
import com.blazeautomation.connected_ls_sample.retrofit.ApiClient;
import com.blazeautomation.connected_ls_sample.retrofit.ApiInterface;
import com.blazeautomation.connected_ls_sample.utils.Alert;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private String idd = "", hub_model = "", type = "", location = "", photo_url = "", installed = "", hubId = "";
    private String device_location = "", device_type = "", device_model = "";
    String hub_name = "";
    private File camProfilePic;
    File file;


    public PairDeviceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new ProgressFragment();
        alert = new AlertFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pair, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arg = getArguments();

        //hub_name=model.getHubName();

        if (arg != null) {
            categoryId = arg.getString("cat_type", null);

            /*get device location from add device */

            device_location = arg.getString("location", null);

            device_type = arg.getString("type", null);

            device_model = arg.getString("hub_model", null);

            Log.e("device_location", device_location);

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


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                camProfilePic = new File(Controller.getImagePath(), timeStamp + "post.png");
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.blazeautomation.connected_ls_sample"+".provider", camProfilePic);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 502);
            } catch (Exception e) {
                e.printStackTrace();
                Alert.showLog("Exception", "openCamera Exception-- " + e.toString() + "  " + e.getMessage());
            }
        }

        /*Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(pictureIntent, 1);
        }*/


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {

                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(imageBitmap);
                encodedImage = encodeImage(imageBitmap);

            }
        }*/

        switch (requestCode) {

            case 502:
                if (resultCode == RESULT_OK & camProfilePic != null) {
                    String path = camProfilePic.getPath();
                    Log.e("CAMERA_PATH", path);
                    Intent intent = new Intent(getActivity(), Croping.class);
                    intent.putExtra("PROFILE", path);
                    intent.putExtra("CAMERA", "CAMERA");
                    startActivityForResult(intent, 303);
                }
                break;


            case 303:
                if (resultCode == RESULT_OK & data != null) {
                    Alert.showLog("EDIT_PROFILE", "EDIT_PROFILE_PATH_AYAAA");
                    setImageafterCrop(data.getExtras().get("PATH").toString());
                }
                break;
        }


    }



    private void setImageafterCrop(String s) {
        file = new File(s);
        Log.e("FILEEEEEEEEE", String.valueOf(file));
    }



    private void addSensorApi() {
        progress.showProgress(getChildFragmentManager(), getString(R.string.creating_blaze_account));
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("installationPhoto", encodedImage);
        hashMap.put("location", device_location);
        hashMap.put("model", device_model);
        hashMap.put("pairingId", bOneId);
        hashMap.put("type", device_type);

        Log.e("parameters", String.valueOf(hashMap));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PhotoSaveModel> call = apiInterface.addSensor(/*token,*/ "C44F33354375", hashMap);

        call.enqueue(new Callback<PhotoSaveModel>() {
            @Override
            public void onResponse(Call<PhotoSaveModel> call, Response<PhotoSaveModel> response) {
                progress.dismissProgress();

                if (response.code() == 200) {

                    idd = response.body().getId();
                    hub_model = response.body().getModel();
                    type = response.body().getType();
                    location = response.body().getLocation();
                    photo_url = response.body().getInstallationPhotoUrl();
                    installed = response.body().getInstalled();
                    hubId = response.body().getHubId();
                    hub_name = model.getHubName();

                    Toast.makeText(getActivity(), "Sensor added successfully", Toast.LENGTH_SHORT).show();
                    gotoF(R.id.action_to_nav_dashboard);


                } else {
                    Toast.makeText(getActivity(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PhotoSaveModel> call, Throwable t) {
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
