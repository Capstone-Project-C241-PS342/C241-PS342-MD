package com.bangkit.capstone.gestura.remote

import com.bangkit.capstone.gestura.data.response.LoginResponse
import com.bangkit.capstone.gestura.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @GET("getUser")
    suspend fun getUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

}


