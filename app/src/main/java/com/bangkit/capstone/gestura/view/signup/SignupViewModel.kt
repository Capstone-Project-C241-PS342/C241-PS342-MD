package com.bangkit.capstone.gestura.view.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bangkit.capstone.gestura.remote.repository.UserRepository
import com.bangkit.capstone.gestura.remote.request.RegisterRequest
import com.bangkit.capstone.gestura.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
//    suspend fun register(username: String, email: String, password: String): RegisterResponse {
//        return withContext(Dispatchers.IO) {
//            val request = RegisterRequest(username, email, password)
//            userRepository.register(request)
//        }
//    }
//}

// SignupViewModel.kt
class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

//    suspend fun register(username: String, email: String, password: String): String {
//        return try {
//            val request = RegisterRequest(username, email, password)
//            val response = userRepository.register(request)
//
//            // Memeriksa nilai error dan message dari response
//            if (response.error == true) {
//                val responseString = response.toString()
//                // Jika error true, return Registration Error
//                "Registration Error: ${responseString ?: "Unknown error"}"
//            } else {
//                // Jika tidak ada error, return User registered successfully
//                "User registered successfully"
//            }
//        } catch (e: Exception) {
//            "Registration failed: ${e.message}" // atau pesan error lainnya
//        }
//    }
    suspend fun register(username: String, email: String, password: String): String {
        return try {
            val request = RegisterRequest(username, email, password)
            val response = userRepository.register(request)

            // Memeriksa jika respons mengandung "HTTP 400"
            if (response.message?.contains("HTTP 400") == true) {
                return "Username or email already exists"
            } else {
                return "User registered"
            }

        } catch (e: Exception) {
            "Registration failed: ${e.message}" // Tangani kesalahan jika terjadi
        }
    }
}

