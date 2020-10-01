package com.blazeautomation.connected_ls_sample.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blazeautomation.connected_ls_sample.R;

public class CaptureLocationActivity extends AppCompatActivity implements View.OnClickListener {
    Context context=this;
    private TextView btnNext;
    private ImageView back_arrow,camera_icon;
    private static final int REQUEST_CAPTURE_IMAGE = 100;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_location);
        initId();
    }

    private void initId() {
        btnNext=findViewById(R.id.btnNext);
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
                startActivity(new Intent(context,DiscoverWifiActivity.class));

                break;
            case R.id.back_arrow:
                onBackPressed();
                break;

            case R.id.camera_icon:
                openCameraIntent();

                break;
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
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
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}