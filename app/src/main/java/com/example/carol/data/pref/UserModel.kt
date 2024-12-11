package com.example.carol.data.pref

data class UserModel(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isLogin: Boolean = false
)