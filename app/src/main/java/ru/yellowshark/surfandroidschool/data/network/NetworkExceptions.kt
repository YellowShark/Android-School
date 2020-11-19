package ru.yellowshark.surfandroidschool.data.network

import ru.yellowshark.surfandroidschool.utils.ERROR_NO_INTERNET
import java.io.IOException

class NoConnectivityException(msg: String = ERROR_NO_INTERNET) : IOException(msg)