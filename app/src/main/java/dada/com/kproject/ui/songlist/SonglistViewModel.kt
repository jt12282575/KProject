package dada.com.kproject.ui.songlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import dada.com.kproject.Global
import dada.com.kproject.R
import dada.com.kproject.api.getKKBOXService
import dada.com.kproject.api.getKKBOXTokenService
import dada.com.kproject.const.ApiConst.Companion.NEW_RELEASE_CATEGORIES_TRACKS
import dada.com.kproject.const.ApiConst.Companion.PLAY_LIST_TRACKS
import dada.com.kproject.data.KKBOXRepository
import dada.com.kproject.exception.KKBOXApiException
import dada.com.kproject.exception.KKBOXAuthException
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.ui.BaseViewModel
import dada.com.kproject.ui.homepage.HomePageViewModel
import dada.com.kproject.util.logi
import dada.com.kproject.util.wrapper.ApiWrapper
import dada.com.kproject.util.wrapper.Resource

class SonglistViewModel(
    private val repo: KKBOXRepository
) : BaseViewModel(repo) {
    fun loadSonglist(songlistId: String, type: Int) {
        _apiWrapperData.value = ApiWrapper(apiType = type, id = songlistId)
    }

  fun fetchTracks() =
        apiWrapperData.switchMap { apiWrapper ->

            when (apiWrapper.apiType) {
                NEW_RELEASE_CATEGORIES_TRACKS -> {
                    logi("category")
                    launchApi(apiWrapper) { token ->
                        repo.fetchAlbumTracks(
                            albumId = apiWrapper.id,
                            token = token
                        )
                    }
                }
                else -> {
                    launchApi(apiWrapper) { token ->
                        repo.fetchPlaylistTracks(
                            playlistId = apiWrapper.id,
                            token = token
                        )
                    }
                }

            }
        }

}

object SonglistVMFactory : ViewModelProvider.Factory {

    private val repo = KKBOXRepository(
        getKKBOXService(),
        getKKBOXTokenService(),
        SharedPreferencesProvider(Global.getContext().applicationContext)
    )


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SonglistViewModel(repo) as T
    }
}