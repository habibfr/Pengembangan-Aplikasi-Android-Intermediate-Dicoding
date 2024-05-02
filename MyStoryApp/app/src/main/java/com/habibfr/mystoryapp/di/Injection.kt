package com.habibfr.mystoryapp.di

import android.content.Context
import com.habibfr.mystoryapp.data.UserRepository
import com.habibfr.mystoryapp.data.pref.UserPreference
import com.habibfr.mystoryapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}