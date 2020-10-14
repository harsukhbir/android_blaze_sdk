package com.blazeautomation.connected_ls_sample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blazeautomation.connected_ls_sample.R;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView btnNext, tvPowerUp;
    private ImageView back_arrow;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        findId();
        setTextColor();
        setTextColor2();
    }

    private void setTextColor2() {
        TextView textView = (TextView) findViewById(R.id.textview2);
        Spannable word = new SpannableString("If it's ");

        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(word);
        Spannable wordTwo = new SpannableString("solid blue");

        wordTwo.setSpan(new ForegroundColorSpan(Color.BLUE), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(wordTwo);
        Spannable wordThree = new SpannableString(", or ");

        wordTwo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(wordThree);


        Spannable wordfour = new SpannableString("blinking red ");

        wordfour.setSpan(new ForegroundColorSpan(Color.RED), 0, wordfour.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(wordfour);

        Spannable wordfive = new SpannableString("press and hold the reset button for 8 seconds.");

        wordfive.setSpan(new ForegroundColorSpan(Color.BLACK), 0, wordfive.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(wordfive);


    }

    private void setTextColor() {
        TextView textView = (TextView) findViewById(R.id.tvPowerUp);
        Spannable word = new SpannableString("Power up the device and ensure the LED indicator is solid ");

        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(word);
        Spannable wordTwo = new SpannableString("Red ");

        wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(wordTwo);
        Spannable wordThree = new SpannableString("Colour.");

        wordTwo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.append(wordThree);


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

                requestPermission();



                break;
            case R.id.back_arrow:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(context, AddHubActivity.class));
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        } else {
            startActivity(new Intent(context, AddHubActivity.class));
        }
    }
}