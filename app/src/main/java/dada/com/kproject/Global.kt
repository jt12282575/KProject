package dada.com.kproject

import android.app.Application
import android.content.Context

class Global: Application() {
    companion object{
        private lateinit var context: Context
        fun getContext(): Context = context
    }
    override fun onCreate() {
        super.onCreate()
        context = this
    }
}