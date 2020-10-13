package com.blazeautomation.connected_ls_sample.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blazeautomation.connected_ls_sample.R;
import com.blazeautomation.connected_ls_sample.retrofit.ApiClient;
import com.blazeautomation.connected_ls_sample.retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptureLocationActivity extends BaseActivity implements View.OnClickListener {
    Context context = this;
    private TextView btnNext;
    private ImageView back_arrow, camera_icon;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    String encodedImage="";
    String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJYelM3Y0tka1VXRm9mWVZUR2pYM0ZIbVVNSVQ1cnpGd2k2TW5LQktmYnI0In0.eyJleHAiOjE2MDI1ODAyNDUsImlhdCI6MTYwMjU3ODQ0NSwiYXV0aF90aW1lIjoxNjAyNTY3ODkyLCJqdGkiOiIyMTFiNTU1OC00MTc5LTQ2Y2YtYTgxMC02YTMzMDk2ZTM3ODYiLCJpc3MiOiJodHRwczovL2F1dGguZGV2LmRhdGFkcml2ZW5jYXJlLm5ldC9hdXRoL3JlYWxtcy9kZGMiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiYzVjMDhhOWEtNTBhYy00NGMyLTk5YTYtMmE4OWU0MjkzODE4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiZGRjLXdlYiIsIm5vbmNlIjoiYWRjMDI4MTUtMmQ5Ni00YWY3LThkYjMtMmZiM2Y1Yzg5ZDUwIiwic2Vzc2lvbl9zdGF0ZSI6ImIxZDZhYTM4LWM3ODEtNGRmZS05YWJmLTAxYzk5MzkyZjNiZSIsImFjciI6IjAiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9hZG1pbi5kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cHM6Ly9kZXYuZGF0YWRyaXZlbmNhcmUubmV0IiwiaHR0cDovL2xvY2FsaG9zdDozMDAxIiwiaHR0cDovL2xvY2FsaG9zdDozMDAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwic3VwZXJBZG1pbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoia2FyZWVtIn0.ZrYbUXvUbyOy84cBJAX0MLBbYPBgkRrkJjE2MEXAAaOfTAobiEfi6TzydssuZ8dQw1anu5zsnD6fbdywWdsyLj_H2Sk2IijqRIszonn8RDGOHf_IUegYU1oxBPYIaujGQem6WnyQu-YFebUwwUStA64YLSq6NSsLoaYP0XW74FeFML_ELz-be5Mj28bqhysLNUqRqEDEzb-DSpI4u5sHtk3mH4AM43BruuKeCwHYa3PWiA8_AzKlWpNkeIu0nOPw8bSF1EcT3d5ttegyYxvp--bJeoSXYDkXIApeuhRBkp1Y-Pkx457TEiyA9HHuPvsNbP_Ta6Ml8QvNu9Pl54y9BQ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_location);
        initId();
    }

    private void initId() {
        btnNext = findViewById(R.id.btnNext);
        back_arrow = findViewById(R.id.back_arrow);
        camera_icon = findViewById(R.id.camera_icon);
        btnNext.setOnClickListener(this);
        back_arrow.setOnClickListener(this);
        camera_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (encodedImage.isEmpty()) {
                    Toast.makeText(context, "Please upload image", Toast.LENGTH_SHORT).show();
                } else {
                    addSensorApi();

                }

                break;
            case R.id.back_arrow:
                onBackPressed();
                break;

            case R.id.camera_icon:
                openCameraIntent();

                break;
        }
    }

    private void addSensorApi() {
        showLoading("Please Wait...");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("installationPhoto", "encodedImage");
        hashMap.put("location", "bathroom");
        hashMap.put("model", "doorv1");
        hashMap.put("pairingId", "string");
        hashMap.put("type", "door");
        Log.e("mapppppppppp", String.valueOf(hashMap));

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.addSensor(token, "C44F33354375", hashMap);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();
                if (response.code() == 200) {
                    Toast.makeText(CaptureLocationActivity.this, "Sensor added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, InstallationSensorActivity.class));

                } else {
                    Toast.makeText(CaptureLocationActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                hideLoading();

            }
        });

    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                camera_icon.setImageBitmap(imageBitmap);
                encodedImage = encodeImage(imageBitmap);
                Log.e("BASE_64_IMAGE", encodedImage);

            }
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}