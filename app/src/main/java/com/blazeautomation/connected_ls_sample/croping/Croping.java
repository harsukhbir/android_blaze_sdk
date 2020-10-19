package com.blazeautomation.connected_ls_sample.croping;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.blazeautomation.connected_ls_sample.R;
import com.blazeautomation.connected_ls_sample.utils.Alert;
import com.steelkiwi.cropiwa.CropIwaView;
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig;
import com.steelkiwi.cropiwa.image.CropIwaResultReceiver;

import java.io.File;
import java.io.IOException;

public class Croping extends AppCompatActivity implements View.OnClickListener{
    private String imagePath;
    private LinearLayout done;
    private TextView tvCancel, tvtitle, tvDone;
    private CropIwaView imageview;
    private Dialog dialog;
    public static final String CIRCLE="CIRCLE",SQUARE="SQUARE";
    private CropIwaResultReceiver resultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_croping);
        dialog= Alert.startLoader(Croping.this);


        resultReceiver = new CropIwaResultReceiver();
        resultReceiver.setListener(new CropIwaResultReceiver.Listener() {
            @Override
            public void onCropSuccess(Uri croppedUri) {
                if(imagePath!=null)
                {
                    if(dialog!=null)
                    {
                        if(dialog.isShowing())
                        {
                            dialog.cancel();
                        }
                    }
                    Intent intentt = new Intent();
                    intentt.putExtra("PATH", imagePath);
                    Croping.this.setResult(RESULT_OK, intentt);
                    finish();
                }
            }

            @Override
            public void onCropFailed(Throwable e) {

            }
        });
        resultReceiver.register(Croping.this);

        if(getIntent().hasExtra(CIRCLE))
        {
            imageview = (CropIwaView) findViewById(R.id.circle_view);
        }
        if(getIntent().hasExtra(SQUARE))
        {
            imageview = (CropIwaView) findViewById(R.id.square_view);
        }
        else
        {
            imageview = (CropIwaView) findViewById(R.id.crop_view);
        }
        imageview.setVisibility(View.VISIBLE);

        tvCancel = (TextView) findViewById(R.id.tvCancel);
        done = (LinearLayout) findViewById(R.id.done);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvtitle.setText("CROP");
        done.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        if (getIntent().hasExtra("PROFILE")) {
            File image = new File(getIntent().getStringExtra("PROFILE"));
            if (image.exists()) {
                try {
                    loadNewImage(getIntent().getStringExtra("PROFILE"));
                } catch (Exception e) {
                    Alert.showLog("Exception", e.toString()+" - "+e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(resultReceiver!=null)
            unregisterReceiver(resultReceiver);
    }

    private void loadNewImage(String filePath)
    {
        if(getIntent().hasExtra("IMAGE_URL"))
        {
            imageview.setImageUri(Uri.parse(getIntent().getStringExtra("IMAGE_URL")));
        }
        else
        {
            imageview.setImageUri(Uri.fromFile(new File(filePath)));
        }
        imageview.setErrorListener(new CropIwaView.ErrorListener() {
            @Override
            public void onError(Throwable e) {
                Alert.showLog("onError", "onError-- "+e.getMessage());
            }
        });
        imageview.setCropSaveCompleteListener(new CropIwaView.CropSaveCompleteListener() {
            @Override
            public void onCroppedRegionSaved(Uri bitmapUri) {
                Alert.showLog("imageview", "setCropSaveCompleteListener");
            }
        });
    }

    public void doneAction()
    {
        Uri uri=destinationPath();
        imageview.crop(new CropIwaSaveConfig.Builder(uri)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                //setSize(outWidth, outHeight) //Optional. If not specified, SRC dimensions will be used
               // .setQuality(100) //Hint for lossy compression formats
                .setQuality(20)
                .build());
        imagePath = new File(uri.getPath()).getPath();
        if(dialog!=null)
        {
            dialog.show();
        }
    }

    private Uri destinationPath()
    {
        File mypath = new File(Controller.getImagePath(), System.currentTimeMillis()+"blaze.png");
        return Uri.fromFile(mypath);
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;

        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done:
                doneAction();
                break;
            case R.id.tvCancel:
                onBackPressed();
                break;

        }

    }


    public float getBackCameraResolutionInMp()
    {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0;i < noOfCameras;i++)
        {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
            {
                Camera camera = Camera.open(i);;
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0;j < cameraParams.getSupportedPictureSizes().size();j++)
                {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount)
                    {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float)pixelCountTemp) / (1024000.0f);
                    }
                }

                camera.release();
            }
        }
        return maxResolution;
    }
}
