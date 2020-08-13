package dada.com.kproject.local

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dada.com.kproject.const.LocalConst.Companion.SHARED_PREFERENCES_NAME
import dada.com.kproject.const.LocalConst.Companion.TOKEN

class SharedPreferencesProvider(private val context: Context) {
    private val preferences:SharedPreferences
    get() = context.getSharedPreferences(SHARED_PREFERENCES_NAME,0)

    fun saveToken(token:String){
        preferences.edit()
            .putString(TOKEN,token)
            .apply()
    }

    fun getToken():String?{
        return preferences.getString(TOKEN,"")
    }
}