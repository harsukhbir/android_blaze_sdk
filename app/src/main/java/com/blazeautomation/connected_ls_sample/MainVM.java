package com.blazeautomation.connected_ls_sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.BlazeAutomation.ConnectedLS.BlazeCallBack;
import com.BlazeAutomation.ConnectedLS.BlazeResponse;
import com.BlazeAutomation.ConnectedLS.BlazeSDK;
import com.google.gson.JsonObject;

import static android.content.Context.MODE_PRIVATE;

public class MainVM extends ViewModel {
    public String ssid;
    public String selectedSSID;
    public String resetCode;
    public String user;
    public String mail, pwd, code, accessToken, refreshToken;
    public String hubId, hubName;
    public boolean isInSetup;
    public boolean isHubOnline;
    private SharedPreferences main_pref;
    private String first_name, last_name;
    private long expireIn;
    boolean isbleListEnabled;
    int interval;

    public MainVM() {
        super();
    }

    void autoSignIn(OnCompletedListener callback) {

        String email = "ddcblaze@gmail.com";
        String password = "password123";

        BlazeSDK.loginUser(email, password, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse blazeResponse) {

                if (blazeResponse != null) {
                    if (blazeResponse.getStatus()) {
                        storeUser(email, password, blazeResponse);
                        getAccessToken(new OnCompletedListener() {
                            @Override
                            public void onSuccess() {
                                getUserDetails(new OnCompletedListener() {
                                    @Override
                                    public void onSuccess() {
                                        storeUser(email, password, blazeResponse);
                                        callback.onSuccess();
                                    }

                                    @Override
                                    public void onFailure() {
                                        callback.onFailure();
                                    }
                                });

                            }

                            @Override
                            public void onFailure() {
                                callback.onFailure();
                            }
                        });
                    } else {
                        String msg = blazeResponse.getMessage();
                        Loggers.error("Client" + " " + msg);
                        callback.onFailure();
                    }
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                String msg = blazeResponse.getMessage();
                Loggers.error("Client" + " " + msg);
                callback.onFailure();
            }
        });
    }

    /*comment  OnCompletedListener on 15 oct 2020 */

    void init(Context c/*, OnCompletedListener callback*/) {

        main_pref = c.getSharedPreferences("main_pref", MODE_PRIVATE);

        autoSignIn(new OnCompletedListener() {
            @Override
            public void onSuccess() {

                pwd = main_pref.getString("pwd", null);
                mail = main_pref.getString("email_id", null);
                code = main_pref.getString("code", null);
                accessToken = main_pref.getString("access_token", null);
                refreshToken = main_pref.getString("refresh_token", null);
                expireIn = main_pref.getLong("expire_in", 0L);
                user = main_pref.getString("user_id", null);
                first_name = main_pref.getString("first_name", null);
                last_name = main_pref.getString("last_name", null);
                hubId = main_pref.getString("selectedhubId", null);
                hubName = main_pref.getString("selectedhubName", null);
                interval = main_pref.getInt("interval", 0);
                isbleListEnabled = main_pref.getBoolean("isbleListEnabled", false);
                //hubId = "240AC4473600";
                BlazeSDK.setUserDetails(mail, user);
                if (hubId != null)
                    BlazeSDK.setHub(hubId);
                BlazeSDK.setAccessToken(accessToken);

                // callback.onSuccess();
            }

            @Override
            public void onFailure() {
                // callback.onFailure();
            }
        });
    }

    void storeUser(String mail, String pwd, BlazeResponse data) {
        SharedPreferences.Editor ed = main_pref.edit();

        if (!TextUtils.isEmpty(mail)) {
            this.mail = mail;
            ed.putString("email_id", mail).apply();
        }
        setPassword(pwd);
        if (!TextUtils.isEmpty(data.getCode())) {
            this.code = data.getCode();
            ed.putString("code", code).apply();
        }
        if (!TextUtils.isEmpty(data.getUserId())) {
            this.user = data.getUserId();
            ed.putString("user_id", user).apply();
        }
        if (!TextUtils.isEmpty(this.mail) && !TextUtils.isEmpty(this.user))
            BlazeSDK.setUserDetails(this.mail, user);
    }

    void storeProfile(JsonObject data) {
        if (data.has("first_name"))
            setFirstName(data.get("first_name").getAsString());

        if (data.has("last_name"))
            setLastName(data.get("last_name").getAsString());

        if (data.has("email_id")) {
            mail = data.get("email_id").getAsString();
            main_pref.edit().putString("email_id", mail).apply();
        }
    }

    void storeToken(String accessToken, String refreshToken, long expireIn) {
        if (!TextUtils.isEmpty(accessToken)) {
            this.accessToken = accessToken;
            main_pref.edit().putString("access_token", accessToken).apply();
            BlazeSDK.setAccessToken(accessToken);
        }
        if (!TextUtils.isEmpty(refreshToken)) {
            this.refreshToken = refreshToken;
            main_pref.edit().putString("refresh_token", refreshToken).apply();
        }
        if (expireIn != 0) {
            this.expireIn = expireIn;
            main_pref.edit().putLong("expire_in", expireIn).apply();
        }
    }

    public boolean isUserAvailable() {
        return !TextUtils.isEmpty(user) && !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(accessToken);
    }

    public String getName() {
        return first_name + " " + last_name;
    }

    public void getUserDetails(OnCompletedListener onCompletedListener) {
        BlazeSDK.getUserDetails(new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse data) {
                if (data != null) {
                    if (data.getStatus()) {
                        JsonObject result = (JsonObject) data.getResult();
                        if (result != null) {
                            storeProfile(result);
                            onCompletedListener.onSuccess();
                            return;
                        }
                    }
                    onCompletedListener.onFailure();
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                onCompletedListener.onFailure();
            }
        });

    }

    public void getAccessToken(OnCompletedListener onCompletedListener) {
        BlazeSDK.getAccessToken(user, code, new BlazeCallBack() {
            @Override
            public void onSuccess(BlazeResponse data) {
                if (data != null) {
                    if (data.getStatus()) {
                        storeToken(data.getAccessToken(), data.getRefreshToken(), data.getExpireIn());
                        onCompletedListener.onSuccess();
                        return;
                    }
                    onCompletedListener.onFailure();
                }
            }

            @Override
            public void onError(BlazeResponse blazeResponse) {
                onCompletedListener.onFailure();
            }
        });

    }

    public void setSelectedHubName(@NonNull String hubName) {
        this.hubName = hubName;
        main_pref.edit().putString("selectedhubName", hubName).apply();
    }

    public void setSelectedHub(@NonNull String hubId) {
        if (!TextUtils.equals(hubId, this.hubId)) {
            main_pref.edit().remove("isbleListEnabled").remove("interval").apply();
        }
        main_pref.edit().putString("selectedhubId", hubId).apply();
        this.hubId = hubId;
        BlazeSDK.setHub(hubId);
    }

    public String getHubId() {
        return hubId;
    }

    public String getHubName() {
        return hubName;
    }

    public String getFirstName() {
        return first_name;
    }

    void setFirstName(String fName) {
        if (!TextUtils.isEmpty(fName)) {
            first_name = fName;
            main_pref.edit().putString("first_name", first_name).apply();
        }
    }

    public String getLastName() {
        return last_name;
    }

    void setLastName(String lName) {
        if (!TextUtils.isEmpty(lName)) {
            last_name = lName;
            main_pref.edit().putString("last_name", last_name).apply();
        }
    }

    public String getPassword() {
        return pwd;
    }

    public void setPassword(String passwordStr) {
        if (!TextUtils.isEmpty(passwordStr)) {
            this.pwd = passwordStr;
            main_pref.edit().putString("pwd", passwordStr).apply();
        }
    }

    public void clearHubDetails() {
        hubId = null;
        hubName = null;
        main_pref.edit().remove("selectedhubId").remove("selectedhubName").apply();
    }

    public void logout() {
        hubId = null;
        hubName = null;
        main_pref.edit().clear().apply();
    }

    public void storeBleListInterval(boolean b, int d) {
        isbleListEnabled = b;
        main_pref.edit().putBoolean("isbleListEnabled", b).apply();
        if (b) {
            interval = d;
            main_pref.edit().putInt("interval", d).apply();
        }

    }
}
