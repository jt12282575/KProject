package dada.com.kproject.api

import android.net.Uri
import android.text.TextUtils
import dada.com.kproject.Global
import dada.com.kproject.R
import dada.com.kproject.const.TerritoryEnum
import dada.com.kproject.model.CategoryResponse
import dada.com.kproject.model.PlayListResponse
import dada.com.kproject.model.Tracks
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


private val BASE_API_URL = "https://api.kkbox.com/v1.1/"

private val service: KKBOXService by lazy {
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY
    val interceptor = MockInterceptor().also {
        it.setInterceptorListener(object : MockInterceptor.MockInterceptorListener {
            override fun setAPIResponse(url: String): MockAPIResponse? {
                val uri = Uri.parse(url)
                val offset = uri.getQueryParameter("offset")
                val path = uri.path
                var fileId = R.raw.mock_playlist_offset_30
                if (TextUtils.equals(path,"/v1.1/featured-playlists")) {
                    fileId = when (offset) {
                        "0" -> {
                            R.raw.mock_playlist_offset_0
                        }
                        "10" -> {
                            R.raw.mock_playlist_offset_10
                        }
                        "20" -> {
                            R.raw.mock_playlist_offset_20
                        }
                        else -> {
                            R.raw.mock_playlist_offset_30

                        }
                    }
                }

                val mockApiResponse = MockAPIResponse()
                mockApiResponse.status = 200
                mockApiResponse.responseString =
                    Global.getContext().resources.openRawResource(fileId)
                        .bufferedReader().use { it.readText() }
                return mockApiResponse
            }

        })
    }

    var useFakePlaylist = dada.com.kproject.BuildConfig.USE_FAKE_PLAYLIST
    var okHttpClientBuilder = OkHttpClient.Builder()
        .addInterceptor(logger)
    if (useFakePlaylist){
        okHttpClientBuilder.addInterceptor(interceptor)
    }
    val okHttpClient = okHttpClientBuilder.build()


    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    retrofit.create(KKBOXService::class.java)
}

fun getKKBOXService() = service

interface KKBOXService {

    @GET("new-release-categories/{category_id}/albums")
    suspend fun fetchTenNewReleaseCategories(
        @Header("Authorization") token: String,
        @Path("category_id") categoryId: String,
        @Query("territory") territory: String = TerritoryEnum.TAIWAN.countryCode,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 15
    ): Response<CategoryResponse>

    @GET("featured-playlists")
    suspend fun fetchPlaylist(
        @Header("Authorization") token: String,
        @Query("territory") territory: String = TerritoryEnum.TAIWAN.countryCode,
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10
    ): Response<PlayListResponse>

    @GET("featured-playlists/{playlist_id}/tracks")
    suspend fun fetchSonglistInPlaylist(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId: String,
        @Query("territory") territory: String = TerritoryEnum.TAIWAN.countryCode
    ): Response<Tracks>

    @GET("albums/{album_id}/tracks")
    suspend fun fetchSonglistInAlbum(
        @Header("Authorization") token: String,
        @Path("album_id") albumId: String,
        @Query("territory") territory: String = TerritoryEnum.TAIWAN.countryCode
    ): Response<Tracks>


}