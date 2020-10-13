package com.blazeautomation.connected_ls_sample.retrofit;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

   /* @Headers({"Accept: application/json"})
    @POST("hubs/{id}/installation")
    Call<ResponseBody> install_hub(@Path("id") String id, @Query("installerId") String installerId, @Header("authorization") String auth);
*/

    @POST("hubs/{id}/installation")
    Call<ResponseBody> install_hub(@Header("authorization") String auth,@Path("id") String id, @Body HashMap<String, String> hashMap);

    @POST("hubs/{id}/sensors")
    Call<ResponseBody> addSensor(@Header("authorization") String auth,@Path("id") String id, @Body HashMap<String, String> hashMap);

}
