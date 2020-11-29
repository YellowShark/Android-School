package ru.yellowshark.surfandroidschool.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonSerializer {
    val gson = Gson()

    fun toJson(obj: Any): String = gson.toJson(obj)

    inline fun <reified T> fromJson(json: String) = gson.fromJson<T>(json)

    inline fun <reified T> Gson.fromJson(json: String): T = this.fromJson(json, object: TypeToken<T>() {}.type)
}