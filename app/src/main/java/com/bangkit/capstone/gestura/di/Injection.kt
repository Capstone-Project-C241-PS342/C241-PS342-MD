package com.bangkit.capstone.gestura.di

import android.content.Context
import com.bangkit.capstone.gestura.remote.repository.UserRepository
import com.bangkit.capstone.gestura.data.pref.UserPreference
import com.bangkit.capstone.gestura.data.pref.dataStore
import com.bangkit.capstone.gestura.remote.ApiConfig

object Injection {
    private var userRepository: UserRepository? = null
//    private var storyRepository: StoryRepository? = null

    fun provideUserRepository(context: Context): UserRepository {
        if (userRepository == null) {
            userRepository = UserRepository.getInstance(UserPreference.getInstance(context.dataStore), ApiConfig.getApiService(""))
        }
        return userRepository!!
    }

//    fun provideStoryRepository(): StoryRepository {
//        if (storyRepository == null) {
//            storyRepository = StoryRepository()
//        }
//        return storyRepository!!
//    }
}
