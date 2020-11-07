package ru.yellowshark.surfandroidschool.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun dateFromMillis(date: Long): String {
    val simpleDateFormat = SimpleDateFormat("MMM dd,yyyy HH:mm")
    val resultDate = Date(date)
    Log.d("TAG", "dateFromMillis: $resultDate")

    return simpleDateFormat.format(resultDate)
}