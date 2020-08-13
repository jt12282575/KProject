package dada.com.kproject.util

import android.util.Log
import dada.com.kproject.BuildConfig

fun logi(message: String) {
    if (BuildConfig.DEBUG) Log.i("kkbox", message)
}