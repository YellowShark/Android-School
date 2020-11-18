package ru.yellowshark.surfandroidschool.utils

import com.google.gson.Gson

class SingleGson {
    companion object {
        private var INSTANCE: Gson? = null
        fun getInstance(): Gson {
            if (INSTANCE == null) {
                INSTANCE = Gson()
            }
            return INSTANCE!!
        }
    }
}