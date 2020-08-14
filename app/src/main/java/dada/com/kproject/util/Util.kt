package dada.com.kproject.util

import android.text.TextUtils
import android.util.Log
import dada.com.kproject.BuildConfig
import dada.com.kproject.const.ApiConst.Companion.FIRST_TIME_LOSS_TOKEN
import dada.com.kproject.exception.KKBOXAuthException
import java.lang.Exception

fun logi(message: String) {
    if (BuildConfig.DEBUG) Log.i("kkbox", message)
}

fun needToRefreshToken(throwable: Throwable):Boolean{
    return (throwable is KKBOXAuthException  && TextUtils.equals(throwable.message,FIRST_TIME_LOSS_TOKEN))
}