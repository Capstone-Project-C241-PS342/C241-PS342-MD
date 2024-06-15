package com.bangkit.capstone.gestura.remote

import com.bangkit.capstone.gestura.remote.request.LoginRequest
import com.bangkit.capstone.gestura.remote.request.RegisterRequest
import com.bangkit.capstone.gestura.remote.response.LoginResponse
import com.bangkit.capstone.gestura.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
//    @FormUrlEncoded
//    @POST("api/auth/register")
//    suspend fun register(
//        @Field("username") username: String,
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): RegisterResponse

    @POST("api/auth/register")
    @Headers("Accept: text/html")
    suspend fun register(
        @Body request: RegisterRequest
    ): String

    @POST("api/auth/login")
    @Headers("Accept: application/json")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @FormUrlEncoded
    @GET("api/auth/user")
    suspend fun getUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

}


