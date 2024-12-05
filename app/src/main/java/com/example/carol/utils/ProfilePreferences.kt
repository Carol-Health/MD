package com.example.carol.utils

import android.content.Context
import android.content.SharedPreferences

class ProfilePreferences(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("ProfileData", Context.MODE_PRIVATE)

    fun saveProfile(name: String, email: String, contact: String) {
        val editor = preferences.edit()
        editor.putString("name", name)
        editor.putString("email", email)
        editor.putString("contact", contact)
        editor.apply()
    }

    fun getName(): String? = preferences.getString("name", "")
    fun getEmail(): String? = preferences.getString("email", "")
    fun getContact(): String? = preferences.getString("contact", "")

    fun clearProfile() {
        preferences.edit().clear().apply()
    }
}
