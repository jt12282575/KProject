package dada.com.kproject.ui.homepage

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dada.com.kproject.Global
import dada.com.kproject.R
import dada.com.kproject.api.getKKBOXService
import dada.com.kproject.api.getKKBOXTokenService
import dada.com.kproject.const.ApiConst.Companion.INITIAL_CATEGORY
import dada.com.kproject.const.ApiConst.Companion.NEW_RELEASE_CATEGORIES
import dada.com.kproject.const.ApiConst.Companion.PLAY_LIST
import dada.com.kproject.const.ApiConst.Companion.PLAY_LIST_PER_PAGE
import dada.com.kproject.data.KKBOXRepository
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.model.CategoryResponse
import dada.com.kproject.model.PlayList
import dada.com.kproject.ui.BaseViewModel
import dada.com.kproject.util.logi
import dada.com.kproject.util.wrapper.ApiWrapper
import dada.com.kproject.util.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.util.*

class HomePageViewModel(private val repo: KKBOXRepository) : BaseViewModel(repo) {



    fun loadTenCategories() {
        _apiWrapperData.value = ApiWrapper(
            apiType = NEW_RELEASE_CATEGORIES,
            offset = 0,
            limit = INITIAL_CATEGORY
        )
    }


    fun fetchTenNewReleaseCategories() = apiWrapperData.switchMap {
        launchApi(it) { token ->
            repo.fetchNewReleaseCategories(limit = INITIAL_CATEGORY,token = token)
        }
    }


    private var currentSearchResult: Flow<PagingData<PlayList>>? = null

    fun loadPlayList(): Flow<PagingData<PlayList>> {
        val lastResult = currentSearchResult
        if (lastResult != null){
            return lastResult
        }

        val newResult: Flow<PagingData<PlayList>> = repo.fetchPlayListStream(token)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }


}

object HomePageVMFactory : ViewModelProvider.Factory {

    private val repo = KKBOXRepository(
        getKKBOXService(),
        getKKBOXTokenService(),
        SharedPreferencesProvider(Global.getContext().applicationContext)
    )

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomePageViewModel(repo) as T
    }
}