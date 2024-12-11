package com.example.carol.data.pref

data class UserModel(
    val id: String,
    val email: String?,
    val username: String?,
    val password: String?,
    val createdAt: String?,
    val isLogin: Boolean = false
)