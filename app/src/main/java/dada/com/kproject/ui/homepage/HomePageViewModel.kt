package dada.com.kproject.ui.homepage

import androidx.lifecycle.*
import dada.com.kproject.Global
import dada.com.kproject.R
import dada.com.kproject.api.getKKBOXService
import dada.com.kproject.api.getKKBOXTokenService
import dada.com.kproject.const.ApiConst.Companion.INITIAL_CATEGORY
import dada.com.kproject.const.ApiConst.Companion.NEW_RELEASE_CATEGORIES
import dada.com.kproject.const.ApiConst.Companion.SONG_LIST
import dada.com.kproject.const.ApiConst.Companion.SONG_LIST_PAGE
import dada.com.kproject.data.KKBOXRepository
import dada.com.kproject.local.SharedPreferencesProvider
import dada.com.kproject.ui.BaseViewModel
import dada.com.kproject.util.logi
import dada.com.kproject.util.wrapper.ApiWrapper
import dada.com.kproject.util.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import java.util.*

class HomePageViewModel(private val repo: KKBOXRepository) : BaseViewModel() {

    private val apiQueue:Queue<ApiWrapper> = LinkedList()
    private val mutex = Mutex()
    private val _needFetchToken = MutableLiveData<Boolean>()


    private fun isFetchRemoteTokenIdle() = !mutex.isLocked
    private val _fetchNewReleaseCategories = MutableLiveData<ApiWrapper>().also {
        it.value = ApiWrapper(
            apiType = NEW_RELEASE_CATEGORIES,
            offset = 0,
            limit = INITIAL_CATEGORY)
    }

    private val fetchNewReleaseCategories: LiveData<ApiWrapper>
        get() = _fetchNewReleaseCategories

    private val _fetchSongList = MutableLiveData<ApiWrapper>().also {
        it.value = ApiWrapper(
            apiType = SONG_LIST,
            offset = 0,
            limit = SONG_LIST_PAGE
        )
    }
    private val fetchSongList:LiveData<ApiWrapper>
    get() = _fetchSongList

    private var token:String = (repo.fetchLocalToken()?:"").also {
        logi("local token : $it")
        if (it.isEmpty()){
            logi("token is Empty")
            _needFetchToken.value = true
        }
    }

    val songList = Transformations.switchMap(_fetchSongList){
        launchDataLoad {
            repo.fetchSongList(
                token = token,
                offset = it.offset,
                limit = it.limit
            )
        }
    }

    val tenNewReleaseCategories = Transformations.switchMap(fetchNewReleaseCategories) {apiWrapper ->
        liveData(Dispatchers.IO) {
            _spinner.postValue(true)
            try {
                val response = repo.fetchNewReleaseCategories(
                    limit = apiWrapper.limit,
                    token = token)
                if (response.isSuccessful){
                    logi("success +summary:  ${response.body()?.summary}")
                    emit(Resource.success(data = response.body()?.data))
                }else if(response.code() == 401 || response.code() == 403){
                    logi("inside error code: ${response.code()}")
                    if (apiWrapper.firstLoad) {
                        apiQueue.add(apiWrapper.also {
                            it.firstLoad =false
                        })
                        _needFetchToken.postValue(true)
                    }else{
                        emit(
                            Resource.error(data = null, message =  Global.getContext().getString(
                                R.string.api_error)))
                    }
                }

            }catch (exception: Exception){
                emit(
                    Resource.error(data = null, message = exception.message ?: Global.getContext().getString(
                    R.string.api_error)))
            }finally {
                _spinner.postValue(false)
            }
        }
    }




    val fetchToken = Transformations.switchMap(_needFetchToken) {
        logi("start fetch token")
        launchDataLoad {
                repo.fetchRemoteToken()

        }

    }.map {
        viewModelScope.launch(Dispatchers.IO) {
            logi("work in storage token")
            if(it.data!= null){
                _showError.postValue(true)
                token = "${it.data.tokenType} ${it.data.accessToken}"
                logi("token: $token")
                repo.saveToken(token)
                while (apiQueue.isNotEmpty()){
                    val apiWrapper = apiQueue.poll()
                    logi("type: ${apiWrapper.apiType}")
                    when(apiWrapper?.apiType){
                        NEW_RELEASE_CATEGORIES ->{
                            _fetchNewReleaseCategories.postValue(
                                apiWrapper.also {
                                    it.firstLoad = false
                                }
                            )
                        }
                        SONG_LIST->{

                        }
                    }
                }
            }else{
                _showError.value = true
                apiQueue.clear()
            }
        }
    }




    private val _showError = MutableLiveData<Boolean>()
    val showError:LiveData<Boolean>
    get() = _showError





}

object LiveDataVMFactory : ViewModelProvider.Factory {

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