package dada.com.kproject.ui.songlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dada.com.kproject.Global
import dada.com.kproject.api.getKKBOXService
import dada.com.kproject.api.getKKBOXTokenService
import dada.com.kproject.data.KKBOXRepository
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.ui.BaseViewModel
import dada.com.kproject.ui.homepage.HomePageViewModel

class SonglistViewModel(
    private val repo: KKBOXRepository
): BaseViewModel(repo) {

}

object SonglistVMFactory : ViewModelProvider.Factory {

    private val repo = KKBOXRepository(
        getKKBOXService(),
        getKKBOXTokenService(),
        SharedPreferencesProvider(Global.getContext().applicationContext)
    )

    fun loadSonglist(songlistId:String){

    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SonglistViewModel(repo) as T
    }
}