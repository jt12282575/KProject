package dada.com.kproject.data

import androidx.paging.PagingSource
import dada.com.kproject.api.KKBOXService
import dada.com.kproject.const.ApiConst.Companion.PAGING_INITIAL_OFFSET
import dada.com.kproject.const.ApiConst.Companion.PAGING_PAGE_SIZE
import dada.com.kproject.const.TerritoryEnum
import dada.com.kproject.model.PlayList
import retrofit2.HttpException
import java.io.IOException

class KKBOXPagingSource(
    private val service:KKBOXService,
    private var token:String
): PagingSource<Int, PlayList>()
{



    fun setToken(newToken:String){
        token = newToken
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PlayList> {
        val position =params.key ?: PAGING_INITIAL_OFFSET
        return try{
            val response =service.fetchPlayList(
                token = token,
                territory = TerritoryEnum.TAIWAN.countryCode,
                offset = position,
                limit = PAGING_PAGE_SIZE
            )
            val playLists = response.body()?.data?: mutableListOf()
            LoadResult.Page(
                data = playLists,
                prevKey = if (position == PAGING_INITIAL_OFFSET) null else position - PAGING_PAGE_SIZE,
                nextKey = if (playLists.isEmpty()) null else position + PAGING_PAGE_SIZE
            )
        }catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}