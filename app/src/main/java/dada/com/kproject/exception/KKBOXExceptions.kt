package dada.com.kproject.exception

import dada.com.kproject.Global
import dada.com.kproject.R
import java.lang.Exception

class KKBOXAuthException: Exception(){
    override val message: String?
        get() = Global.getContext().getString(R.string.auth_exception)
}