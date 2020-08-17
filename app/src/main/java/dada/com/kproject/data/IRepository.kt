package dada.com.kproject.data

import dada.com.kproject.model.TokenResponse

interface IRepository {
    fun saveToken(token:String)
    fun getLocalToken():String?
    suspend fun fetchRemoteToken(): TokenResponse
}