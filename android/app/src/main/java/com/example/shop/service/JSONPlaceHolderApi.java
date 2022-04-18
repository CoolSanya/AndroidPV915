package com.example.shop.service;

import com.example.shop.account.dto.LoginDTO;
import com.example.shop.account.dto.RegisterDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
    @GET("/posts/{id}")
    public Call<Post> getPostWithID(@Path("id") int id);

    @POST("/api/account/login")
    public Call<Void> login(@Body LoginDTO model);

    @POST("/api/account/register")
    public Call<Void> login(@Body RegisterDTO model);
}
