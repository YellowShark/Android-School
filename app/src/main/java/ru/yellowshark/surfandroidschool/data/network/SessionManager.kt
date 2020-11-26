package ru.yellowshark.surfandroidschool.data.network

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.domain.User

private const val USER_TOKEN = "user_token"
private const val USER_INFO = "user_info"

class SessionManager(
    val context: Context,
    val gson: Gson
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun saveUser(token: String, user: User) {
        saveAuthToken(token)
        saveUserInfo(user)
    }

    private fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    private fun saveUserInfo(user: User) {
        val editor = prefs.edit()
        val json = gson.toJson(user)
        editor.putString(USER_INFO, json)
        editor.apply()
    }

    fun fetchUserInfo(): User? {
        val json = prefs.getString(USER_INFO, null)
        return if (json != null)
            gson.fromJson(json, User::class.java)
        else
            null
    }

    fun forgetUser() {
        val editor = prefs.edit()
        editor.putString(USER_INFO, null)
        editor.putString(USER_TOKEN, null)
        editor.apply()
    }
}