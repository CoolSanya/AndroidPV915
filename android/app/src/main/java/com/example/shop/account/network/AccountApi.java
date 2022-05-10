package com.example.shop.account.network;

import com.example.shop.account.dto.AccountResponseDTO;
import com.example.shop.account.dto.EditUserDTO;
import com.example.shop.account.dto.LoginDTO;
import com.example.shop.account.dto.RegisterDTO;
import com.example.shop.account.userscard.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AccountApi {
    @POST("/api/account/register")
    public Call<AccountResponseDTO> register(@Body RegisterDTO model);
    @GET("/api/account/users")
    public Call<List<UserDTO>> users();
    @POST("/api/account/login")
    public Call<AccountResponseDTO> login(@Body LoginDTO model);
    @PUT("/api/account/updateuser")
    public Call<AccountResponseDTO> editUser(@Body EditUserDTO model);

}
