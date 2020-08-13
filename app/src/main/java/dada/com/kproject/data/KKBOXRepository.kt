package dada.com.kproject.data

import dada.com.kproject.BuildConfig
import dada.com.kproject.api.KKBOXService
import dada.com.kproject.api.KKBOXTokenService
import dada.com.kproject.const.ApiConst
import dada.com.kproject.const.ApiConst.Companion.CATEGORY_COMPLEX
import dada.com.kproject.const.TerritoryEnum
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.model.CategoryResponse
import dada.com.kproject.model.SongListResponse
import dada.com.kproject.model.TokenResponse
import dada.com.kproject.util.logi
import retrofit2.Response

class KKBOXRepository(private val service: KKBOXService,
                      private val tokenService: KKBOXTokenService,
                      private val sharedPreferencesProvider: SharedPreferencesProvider
) {
    suspend fun fetchNewReleaseCategories(limit: Int,token:String): Response<CategoryResponse> {
        return service.fetchTenNewReleaseCategories(
            token = token,
            categoryId = CATEGORY_COMPLEX,
            territory = TerritoryEnum.TAIWAN.countryCode,
            offset = 0,
            limit = limit
        )
    }

   suspend fun fetchSongList(limit: Int,offset:Int,token:String):Response<SongListResponse>{
       return service.fetchSongList(
           token = token,
           limit = limit,
           offset = offset
       )
   }

    suspend fun fetchRemoteToken(): TokenResponse {
        return tokenService.getAccessToken(
            grantType = ApiConst.CLIENT_CREDENTIALS,
            clientId = BuildConfig.KKBOX_ID,
            clientSecret = BuildConfig.KKBOX_SECRET
        )
    }

    fun fetchLocalToken():String? {
        val tokenStr =  sharedPreferencesProvider.getToken()
        logi("fetch from local $tokenStr")

        return tokenStr
    }

    fun saveToken(token:String) = sharedPreferencesProvider.saveToken(token)

}