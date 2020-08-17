package dada.com.kproject.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dada.com.kproject.BuildConfig
import dada.com.kproject.api.KKBOXService
import dada.com.kproject.api.KKBOXTokenService
import dada.com.kproject.const.ApiConst
import dada.com.kproject.const.ApiConst.Companion.CATEGORY_COMPLEX
import dada.com.kproject.const.ApiConst.Companion.PAGING_PAGE_SIZE
import dada.com.kproject.const.ApiConst.Companion.PREFETCH_DISTANCE_SIZE
import dada.com.kproject.const.TerritoryEnum
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.model.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class KKBOXRepository(private val service: KKBOXService,
                      private val tokenService: KKBOXTokenService,
                      private val sharedPreferencesProvider: SharedPreferencesProvider
):IRepository
{


    suspend fun fetchNewReleaseCategories(limit: Int,token:String): Response<CategoryResponse> {
        return service.fetchTenNewReleaseCategories(
            token = token,
            categoryId = CATEGORY_COMPLEX,
            territory = TerritoryEnum.TAIWAN.countryCode,
            offset = 0,
            limit = limit
        )
    }



    override suspend fun fetchRemoteToken(): TokenResponse {
        return tokenService.getAccessToken(
            grantType = ApiConst.CLIENT_CREDENTIALS,
            clientId = BuildConfig.KKBOX_ID,
            clientSecret = BuildConfig.KKBOX_SECRET
        )
    }



    private var pagingSource:KKBOXPagingSource? = null

    override fun saveToken(token:String){
        sharedPreferencesProvider.saveToken(token)
        pagingSource?.setToken(token)
    }

    override fun getLocalToken(): String? {
        return sharedPreferencesProvider.getToken()
    }

    fun fetchPlayListStream(token:String): Flow<PagingData<PlayList>> {
        pagingSource = KKBOXPagingSource(service,token)

        return Pager(
            config = PagingConfig(
                prefetchDistance = PREFETCH_DISTANCE_SIZE,
                pageSize = PAGING_PAGE_SIZE,
                enablePlaceholders = false),
            pagingSourceFactory ={pagingSource!!}
        ).flow
    }

    suspend fun fetchAlbumTracks(albumId:String, token:String): Response<Tracks>{
        return service.fetchSonglistInAlbum(
            token = token,
            albumId = albumId
        )
    }

    suspend fun fetchPlaylistTracks(playlistId:String, token:String): Response<Tracks>{
        return service.fetchSonglistInPlaylist(
            token = token,
            playlistId = playlistId
        )
    }

}

