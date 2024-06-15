package com.bangkit.capstone.gestura.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("token")
    val token: String
)

data class LoginResult(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)
