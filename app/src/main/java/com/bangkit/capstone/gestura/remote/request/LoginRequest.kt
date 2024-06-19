package com.bangkit.capstone.gestura.remote.request

data class LoginRequest(
    val email: String,
    val password: String,
)