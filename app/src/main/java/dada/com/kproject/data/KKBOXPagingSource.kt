package dada.com.kproject.data

import androidx.paging.PagingSource
import dada.com.kproject.api.KKBOXService
import dada.com.kproject.const.ApiConst.Companion.PAGING_INITIAL_OFFSET
import dada.com.kproject.const.ApiConst.Companion.PAGING_INITIAL_PAGE_SIZE
import dada.com.kproject.const.ApiConst.Companion.PAGING_PAGE_SIZE
import dada.com.kproject.const.TerritoryEnum
import dada.com.kproject.model.SongList
import retrofit2.HttpException
import java.io.IOException

class KKBOXPagingSource(
    private val service:KKBOXService,
    private var token:String
): PagingSource<Int, SongList>()
{



    fun setToken(newToken:String){
        token = newToken
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SongList> {
        val position =params.key ?: PAGING_INITIAL_OFFSET
        return try{
            val response =service.fetchSongList(
                token = token,
                territory = TerritoryEnum.TAIWAN.countryCode,
                offset = position,
                limit = PAGING_PAGE_SIZE
            )
            val songLists = response.body()?.data?: mutableListOf()
            LoadResult.Page(
                data = songLists,
                prevKey = if (position == PAGING_INITIAL_OFFSET) null else position - PAGING_PAGE_SIZE,
                nextKey = if (songLists.isEmpty()) null else position + PAGING_PAGE_SIZE
            )
        }catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}