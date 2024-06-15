package com.bangkit.capstone.gestura.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.gestura.remote.repository.UserRepository
import com.bangkit.capstone.gestura.remote.request.LoginRequest
import com.bangkit.capstone.gestura.remote.request.RegisterRequest
import com.dicoding.picodiploma.gestura.data.pref.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    suspend fun login(email: String, password: String): Result<UserModel> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, password)
                val response = repository.login(request)

                if (response.token != null) {
                    val user = UserModel(email = email, token = response.token, isLogin = true)
                    saveSession(user)
                    Result.success(user)
                } else {
                    Result.failure(Exception("Username atau password salah"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}