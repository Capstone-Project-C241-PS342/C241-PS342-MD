package com.bangkit.capstone.gestura.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.gestura.remote.ApiConfig
import com.bangkit.capstone.gestura.remote.ApiService
import com.bangkit.capstone.gestura.remote.repository.UserRepository
import com.bangkit.capstone.gestura.utils.getFeedback
import com.dicoding.picodiploma.gestura.data.pref.UserModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    val userProfile: LiveData<UserModel> get() = _userProfile
    private val _userProfile = MutableLiveData<UserModel>()


    fun fetchUserProfile(token: String) {
        viewModelScope.launch {
            try {
                val user = ApiConfig.getApiService(token).getUser()
                _userProfile.value = user
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

//    val feedbackUser = MutableLiveData<getFeedback>()

//    fun getUser(){
//        viewModelScope.launch {
//            flow {
//                val response = ApiConfig
//                    .getApiService()
//                    .getUser()
//                emit(response)
//            }.onStart {
//               feedbackUser.value = getFeedback.Loading(true)
//            }.onCompletion {
//               feedbackUser.value = getFeedback.Loading(false)
//            }.catch {
//               feedbackUser.value = getFeedback.Error(it)
//            }.collect{
//              feedbackUser.value = getFeedback.Success(it)
//            }
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}