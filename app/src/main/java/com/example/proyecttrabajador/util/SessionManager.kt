package com.tuapp.trabajador.util

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("trabajador_prefs", Context.MODE_PRIVATE)
    fun saveToken(token: String) = prefs.edit().putString("token", token).apply()
    fun getToken(): String? = prefs.getString("token", null)
    fun clearToken() = prefs.edit().remove("token").apply()
}