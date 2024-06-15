package com.bangkit.capstone.gestura.remote.repository
import android.util.Log
import com.bangkit.capstone.gestura.data.pref.UserPreference
import com.bangkit.capstone.gestura.remote.response.ErrorResponse
import com.bangkit.capstone.gestura.remote.response.LoginResponse
import com.bangkit.capstone.gestura.remote.response.RegisterResponse
import com.bangkit.capstone.gestura.remote.ApiService
import com.bangkit.capstone.gestura.remote.request.LoginRequest
import com.bangkit.capstone.gestura.remote.request.RegisterRequest
import com.dicoding.picodiploma.gestura.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return try {
            val responseString = apiService.register(request)
            // Di sini Anda bisa menentukan cara memproses respons string sesuai kebutuhan
            if (responseString.contains("Username or email already exists", ignoreCase = true)) {
                RegisterResponse(error = true, message = "Username or email already exists")
            } else {
                RegisterResponse(error = false, message = "User registered successfully")
            }
        } catch (e: Exception) {
            RegisterResponse(error = true, message = e.message ?: "Unknown error")
        }
    }

//    suspend fun register(request: RegisterRequest): String {
//        return try {
//            apiService.register(request)
//            "User registered"
//        } catch (e: Exception) {
//            if (e.message.contains("JSON"))
//            Log.e("RegisterResponse", "Error registering user", e)
//            "Username or email already exists"
//        }
//    }

    suspend fun login(request: LoginRequest): LoginResponse {
        return try {

            apiService.login(request)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            throw Exception(errorBody.message ?: "An error occurred")
        }
    }
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
