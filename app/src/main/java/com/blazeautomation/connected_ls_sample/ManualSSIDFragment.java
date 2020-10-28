package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class ManualSSIDFragment extends NavigationXFragment {
    private static final int MAX_SSID_LENGTH = 32;
    private MessageAlertDialog alert;

    public ManualSSIDFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new MessageAlertDialog(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manual_ssid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout manual_ssid_lay = view.findViewById(R.id.manual_ssid_lay);
        TextInputEditText manual_ssid = view.findViewById(R.id.manual_ssid);
        View select = view.findViewById(R.id.select);
        select.setOnClickListener(v -> {
            String ssid = manual_ssid.getText().toString();
            if (BlazeUtils.isStringNotEmpty(ssid)) {
                if (ssid.length() > MAX_SSID_LENGTH) {
                    //alert.showAlertMessage(getChildFragmentManager(), String.format(getString(R.string.ssid_length_max_reached), MAX_SSID_LENGTH));
                    manual_ssid_lay.setError(String.format(getString(R.string.ssid_length_max_reached), MAX_SSID_LENGTH));
                    return;
                }
                model.selectedSSID = ssid;
                gotoF(R.id.action_nav_manual_ssid_to_nav_ssid);
            } else {
                alert.showAlertMessage(getChildFragmentManager(), getString(R.string.please_enter_ssid));
            }
        });
        manual_ssid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (manual_ssid.length() <= MAX_SSID_LENGTH) {
                    manual_ssid_lay.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {

                    if (editable.length() > 0) {
                        select.setVisibility(View.VISIBLE);
                        if (editable.length() > MAX_SSID_LENGTH) {
                            String s = editable.toString().substring(0, MAX_SSID_LENGTH);
                            //alert.setCancelButtonVisibility(View.GONE);
                            //alert.showAlertMessage(getChildFragmentManager(), );
                            manual_ssid.setText(s);
                            manual_ssid.setSelection(s.length());
                            manual_ssid_lay.setError(String.format(getString(R.string.ssid_length_max_reached), MAX_SSID_LENGTH));
                        }
                    } else {
                        select.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
