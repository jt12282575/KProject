package dada.com.kproject.util

import dada.com.kproject.const.ApiConst.Companion.KKBOX_PACKAGE_NAME

class SchemeUtil {
    companion object{
        fun getMarketSearchUri(scheme:String):String = "market://search?q=${scheme}"
        fun getMarketDetailUri():String = "market://details?id=$KKBOX_PACKAGE_NAME"
        fun getMarketSearchHttpsUrl(scheme: String):String = "https://play.google.com/store/search?q=${scheme}"
        fun getMarketDetailHttpsUrl():String = "https://play.google.com/store/apps/details?id=$KKBOX_PACKAGE_NAME"
    }
}