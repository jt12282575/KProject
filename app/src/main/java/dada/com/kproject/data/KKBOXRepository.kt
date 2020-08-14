package dada.com.kproject.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dada.com.kproject.BuildConfig
import dada.com.kproject.api.KKBOXService
import dada.com.kproject.api.KKBOXTokenService
import dada.com.kproject.const.ApiConst
import dada.com.kproject.const.ApiConst.Companion.CATEGORY_COMPLEX
import dada.com.kproject.const.TerritoryEnum
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.model.CategoryResponse
import dada.com.kproject.model.PlayList
import dada.com.kproject.model.PlayListResponse
import dada.com.kproject.model.TokenResponse
import dada.com.kproject.util.logi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class KKBOXRepository(private val service: KKBOXService,
                      private val tokenService: KKBOXTokenService,
                      private val sharedPreferencesProvider: SharedPreferencesProvider
):BaseRepository
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



   suspend fun fetchPlayList(limit: Int, offset:Int, token:String):Response<PlayListResponse>{
       return service.fetchPlaylist(
           token = token,
           limit = limit,
           offset = offset
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
        val tokenStr =  sharedPreferencesProvider.getToken()
        logi("fetch from local $tokenStr")
        return tokenStr
    }

    fun fetchPlayListStream(token:String): Flow<PagingData<PlayList>> {
        pagingSource = KKBOXPagingSource(service,token)

        return Pager(
            config = PagingConfig(
                pageSize = ApiConst.PAGING_PAGE_SIZE,
                enablePlaceholders = false),
            pagingSourceFactory ={pagingSource!!}
        ).flow
    }

}