package com.bangkit.capstone.gestura.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.capstone.gestura.remote.repository.UserRepository
import com.bangkit.capstone.gestura.di.Injection
import com.bangkit.capstone.gestura.view.login.LoginViewModel
import com.bangkit.capstone.gestura.view.main.MainViewModel
import com.bangkit.capstone.gestura.view.signup.SignupViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
//            modelClass.isAssignableFrom(CameraActivityViewModel::class.java) -> {
//                SignupViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(DetectorActivityViewModel::class.java) -> {
//                SignupViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(ResultActivityViewModel::class.java) -> {
//                SignupViewModel(repository) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideUserRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}