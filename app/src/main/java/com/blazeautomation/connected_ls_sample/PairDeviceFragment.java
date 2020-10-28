package com.blazeautomation.connected_ls_sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.blazeautomation.connected_ls_sample.model.PhotoSaveModel;
import com.blazeautomation.connected_ls_sample.retrofit.ApiClient;
import com.blazeautomation.connected_ls_sample.retrofit.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class PairDeviceFragment extends NavigationXFragment {
    String categoryId = null;
    private MessageProgressDialog progress;
    private MessageAlertDialog alert;
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
    String currentphotopath = "";


    public PairDeviceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = new MessageProgressDialog(requireActivity());
        alert = new MessageAlertDialog(requireActivity());
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

            if (encodedImage != null && !encodedImage.equalsIgnoreCase("")) {
                addDevice(arg.getString("location", null));
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

        String fileName = "photo";
        File storageDirectory = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imagefile = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentphotopath = imagefile.getAbsolutePath();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageuri = FileProvider.getUriForFile(getActivity(), "com.blazeautomation.connected_ls_sample.fileprovider", imagefile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
            startActivityForResult(intent, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // if (data != null && data.getExtras() != null) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(currentphotopath);
            //Bitmap imageBitmap = (Bitmap)data.getExtras().get("data");
            Bitmap rotated_bitmap = RotateBitmap(imageBitmap, 90);

            imageView.setImageBitmap(rotated_bitmap);
            encodedImage = encodeImage(rotated_bitmap);

            // }
        }
    }


    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    private void addSensorApi() {

        progress.showProgress(getChildFragmentManager(), getString(R.string.creating_blaze_account));

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("installationPhoto", encodedImage);
        hashMap.put("location", device_location);
        hashMap.put("model", device_model);
        hashMap.put("pairingId", bOneId);
        hashMap.put("type", device_type);


        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<PhotoSaveModel> call = apiInterface.addSensor(/*token,*/ model.hubId, hashMap);

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
                    addSensorApi();
                } else {
                    Loggers.error("_add_error", blazeResponse);
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());

                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {

                Loggers.error("_add_error", blazeResponse);
                 progress.dismissProgress();
                 alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
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

                Loggers.error("_pair_error", blazeResponse);
                 progress.dismissProgress();
                 add_lay.setVisibility(View.GONE);
                 alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
            }
        });
    }

}
