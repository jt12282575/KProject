package dada.com.kproject.exception

import dada.com.kproject.Global
import dada.com.kproject.R
import java.lang.Exception

class KKBOXAuthException(private val refreshMessage: String) : Exception() {
    override val message: String?
        get() = refreshMessage
}

class KKBOXApiException() : Exception() {
    override val message: String?
        get() = Global.getContext().getString(
            R.string.api_exception
        )
}