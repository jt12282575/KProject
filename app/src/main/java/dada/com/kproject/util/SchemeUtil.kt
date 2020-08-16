package dada.com.kproject.util

class SchemeUtil {
    companion object{
        fun getMarketSearchUri(scheme:String):String = "market://search?q=${scheme}"
        fun getMarketSearchHttpsUrl(scheme: String):String = "https://play.google.com/store/search?q=${scheme}"
    }
}