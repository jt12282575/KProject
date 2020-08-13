package dada.com.kproject.ui.homepage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dada.com.kproject.Global
import dada.com.kproject.api.getKKBOXService
import dada.com.kproject.api.getKKBOXTokenService
import dada.com.kproject.data.KKBOXRepository
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.ui.homepage.HomePageViewModel



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import dada.com.kproject.R
import dada.com.kproject.util.logi

class HomePageActivity : AppCompatActivity() {

    val model:HomePageViewModel by viewModels() { LiveDataVMFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        model.tenNewReleaseCategories.observe(this, Observer {
            logi("size : ${it.data?.size}")
            if(it?.message != null){
                logi("error : ${it?.message}")
            }
        })

        model.showError.observe(this, Observer {
            //TODO 畫面是否 error message 蓋住
        })

        model.songList.observe(this,
            Observer{
                logi("song list size : ${it.data?.body()?.data?.size}")
                if(it?.message != null){
                    logi("error : ${it?.message}")
                }
            }
        )

        model.fetchToken.observe(this, Observer {
            logi("finish fetch")
        })

    }
}