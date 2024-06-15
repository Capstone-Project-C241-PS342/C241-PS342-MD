package com.bangkit.capstone.gestura.remote.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)