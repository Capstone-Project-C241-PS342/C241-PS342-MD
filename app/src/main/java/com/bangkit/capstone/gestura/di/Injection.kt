package com.bangkit.capstone.gestura.di

import android.content.Context
import com.bangkit.capstone.gestura.data.UserRepository
import com.bangkit.capstone.gestura.data.pref.UserPreference
import com.bangkit.capstone.gestura.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}