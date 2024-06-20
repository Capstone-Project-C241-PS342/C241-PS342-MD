package com.bangkit.capstone.gestura.remote.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("token")
    val token: String,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profilePictureUrl")
    val profile_picture_url: String?
)

data class LoginResult(



    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("token")
    val token: String? = null
)
