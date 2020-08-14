package dada.com.kproject.util.wrapper

import dada.com.kproject.const.ViewStateConst.Companion.NO_ITEM


data class Resource<out T>(val status: Status, val data: T?, val message: String?,val code:Int = NO_ITEM) {
    companion object {

        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> success(data: T,code:Int = NO_ITEM): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null,code = code)

        fun <T> error(data: T?, message: String = ""): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message, code = NO_ITEM)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null, code = NO_ITEM)
    }
}