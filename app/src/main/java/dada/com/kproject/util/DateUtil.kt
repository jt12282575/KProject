package dada.com.kproject.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {

        fun getApiDateFormat() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.TAIWAN)
        fun getShowDateFormat() = SimpleDateFormat("yyyy/MM/dd")


    }

}