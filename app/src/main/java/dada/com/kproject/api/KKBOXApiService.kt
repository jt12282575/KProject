package dada.com.kproject.api

import dada.com.kproject.const.TerritoryEnum
import dada.com.kproject.model.CategoryResponse
import dada.com.kproject.model.PlayListResponse
import dada.com.kproject.model.SonglistResponse
import dada.com.kproject.model.Tracks
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


private val BASE_API_URL = "https://api.kkbox.com/v1.1/"

private val service:KKBOXService by lazy{
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY

    var okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

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
        @Path("category_id") categoryId:String,
        @Query("territory") territory:String = TerritoryEnum.TAIWAN.countryCode,
        @Query("offset") offset:Int = 0,
        @Query("limit") limit:Int = 15
    ):Response<CategoryResponse>

    @GET("featured-playlists")
    suspend fun fetchPlaylist(
        @Header("Authorization") token: String,
        @Query("territory") territory:String = TerritoryEnum.TAIWAN.countryCode,
        @Query("offset") offset:Int = 0,
        @Query("limit") limit:Int = 15
    ):Response<PlayListResponse>

    @GET("featured-playlists/{playlist_id}/tracks")
    suspend fun fetchSonglistInPlaylist(
        @Header("Authorization") token: String,
        @Path("playlist_id") playlistId:String,
        @Query("territory") territory:String = TerritoryEnum.TAIWAN.countryCode
    ):Response<Tracks>

    @GET("/albums/{album_id}/tracks")
    suspend fun fetchSonglistInAlbum(
        @Header("Authorization") token: String,
        @Path("album_id") albumId:String,
        @Query("territory") territory:String = TerritoryEnum.TAIWAN.countryCode
    ):Response<Tracks>


}