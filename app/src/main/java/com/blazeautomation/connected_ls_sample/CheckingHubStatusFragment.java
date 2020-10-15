package com.blazeautomation.connected_ls_sample;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;

public class CheckingHubStatusFragment extends NavigationXFragment {
    private AlertFragment alert;
    private ProgressFragment progress;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alert = new AlertFragment();
        progress = new ProgressFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return LayoutInflater.from(requireContext()).inflate(R.layout.fragment_checking_hub_status, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*        view.findViewById(R.id.btnLogin).setOnClickListener(v -> gotoF(R.id.action_nav_welcome_to_nav_login));*/
        TextView status = view.findViewById(R.id.status);
        View btnStatus = view.findViewById(R.id.btnCheck);
        TextView btnNext = view.findViewById(R.id.btnNext);
        TextView text_3 = view.findViewById(R.id.text_3);
        TextView hub_online_msg = view.findViewById(R.id.hub_online_msg);
        btnNext.setOnClickListener(v -> {
            if (model.isInSetup)
                gotoF(R.id.action_nav_hub_status_to_nav_complete);
            else
                gotoF(R.id.action_nav_hub_status_to_nav_settings);
        });
        if (model.isInSetup) {
            text_3.setText("3. If Hub LED blinks RED, Hub can not connect to the Preferred Wi-Fi with given credentials. set credentials once again.");
            hub_online_msg.setText("Now your Hub is online. Enter Hub name and click on 'Next' button.");
        } else {
            text_3.setText("3. If Hub LED blinks RED, Hub can not connect to the Preferred Wi-Fi with given credentials. Then do change access point once again.");
            btnNext.setText("Home");
            hub_online_msg.setText("Now your Hub is online. Please click on 'Home' button. ");
        }
        btnStatus.setOnClickListener(v -> {
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            BlazeSDK.checkHubStatus(model.hubId, new BlazeCallBack() {
                @Override
                public void onSuccess(BlazeResponse blazeResponse) {
                    progress.dismissProgress();
                    status.setText("Online");
                    btnNext.setEnabled(true);
                    hub_online_msg.setVisibility(View.VISIBLE);
                    btnNext.setAlpha(1f);
                    if (!model.isInSetup) {
                        alert.setCancelButtonVisibility(View.GONE);
                        alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_nav_hub_status_to_nav_settings));
                        alert.showAlertMessage(getChildFragmentManager(), "Change access point is completed successfully.");
                    }
                }

                @Override
                public void onError(BlazeResponse blazeResponse) {

                    progress.dismissProgress();
                    status.setText("Online");
                    btnNext.setEnabled(true);
                    hub_online_msg.setVisibility(View.VISIBLE);
                    btnNext.setAlpha(1f);
                    if (!model.isInSetup) {
                        alert.setCancelButtonVisibility(View.GONE);
                        alert.setOkButtonListener(getString(R.string.ok), v -> gotoF(R.id.action_nav_hub_status_to_nav_settings));
                        alert.showAlertMessage(getChildFragmentManager(), "Change access point is completed successfully.");
                    }

                    /**progress.dismissProgress();
                    status.setText("Offline");
                    alert.showAlertMessage(getChildFragmentManager(), blazeResponse.getMessage());
                    btnNext.setEnabled(false);
                    hub_online_msg.setVisibility(View.GONE);
                    btnNext.setAlpha(0.3f);
                     */
                }
            });
        });
        model.setSelectedHub(model.hubId);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            progress.showProgress(getChildFragmentManager(), getString(R.string.please_wait));
            handler.postDelayed(() -> progress.dismissProgress(), 5000);
        }, 300);
    }
}