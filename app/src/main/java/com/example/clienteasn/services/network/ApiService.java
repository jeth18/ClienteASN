package com.example.clienteasn.services.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @Multipart
    @POST("Publicacion/upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @Headers("Content-Type: application/json")
    @GET("Publicacion/uploads/{imageurl}")
    Call<ResponseBody> getImage(@Path("imageurl") String imageid);
}