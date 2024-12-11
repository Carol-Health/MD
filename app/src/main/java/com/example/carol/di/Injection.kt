package com.example.carol.di

import android.content.Context
import com.example.carol.data.UserRepository
import com.example.carol.data.pref.UserPreference
import com.example.carol.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}
