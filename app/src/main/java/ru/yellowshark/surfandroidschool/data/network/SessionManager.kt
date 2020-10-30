package ru.yellowshark.surfandroidschool.data.network

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import ru.yellowshark.surfandroidschool.R
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse
import ru.yellowshark.surfandroidschool.data.network.auth.response.UserInfo

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_INFO = "user_info"
    }

    fun saveUser(response: AuthResponse) {
        saveAuthToken(response.accessToken)
        saveUserInfo(response.userInfo)
    }

    private fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    private fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserInfo(userInfo: UserInfo) {
        val editor = prefs.edit()
        val json = gson.toJson(userInfo)
        editor.putString(USER_INFO, json)
        editor.apply()
    }

    fun fetchUserInfo(): UserInfo? {
        val json = prefs.getString(USER_INFO, null)
        return if (json != null)
            gson.fromJson(json, UserInfo::class.java)
        else
            null
    }
}