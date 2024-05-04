package com.habibfr.mystoryapp.di

import android.content.Context
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.pref.UserPreference
import com.habibfr.mystoryapp.data.pref.dataStore
import com.habibfr.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()

        return UserRepository.getInstance(pref, apiService)
    }
}