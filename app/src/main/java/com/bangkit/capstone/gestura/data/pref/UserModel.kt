package com.dicoding.picodiploma.gestura.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val profile_picture_url: String,
    val isLogin: Boolean = false,
    val username: String
)