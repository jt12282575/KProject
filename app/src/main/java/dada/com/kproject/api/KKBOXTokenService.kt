package dada.com.kproject.api

import com.google.gson.JsonObject
import dada.com.kproject.const.ApiConst
import dada.com.kproject.model.TokenResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

private val BASE_AUTH_URL = "https://account.kkbox.com/"


private val service:KKBOXTokenService by lazy{
    val logger = HttpLoggingInterceptor()
    logger.level = HttpLoggingInterceptor.Level.BODY

    val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_AUTH_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    retrofit.create(KKBOXTokenService::class.java)
}

fun getKKBOXTokenService() = service

interface KKBOXTokenService {
    @POST("/oauth2/token")
    @FormUrlEncoded
    suspend fun getAccessToken(@Field("grant_type") grantType:String,
                       @Field("client_id") clientId:String,
                       @Field("client_secret") clientSecret:String
    ):TokenResponse
}