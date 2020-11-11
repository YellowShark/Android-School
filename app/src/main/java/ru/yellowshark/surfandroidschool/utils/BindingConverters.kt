package ru.yellowshark.surfandroidschool.utils

import androidx.databinding.BindingConversion
import java.text.SimpleDateFormat
import java.util.*

object BindingConverters {
    @BindingConversion
    @JvmStatic fun unixDateConverter(dateMillis: Int): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val resultDate = Date(dateMillis.toLong())
        return simpleDateFormat.format(resultDate)
    }
}