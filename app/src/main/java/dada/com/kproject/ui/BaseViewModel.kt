package dada.com.kproject.ui

import androidx.lifecycle.*
import dada.com.kproject.Global
import dada.com.kproject.R
import dada.com.kproject.data.IRepository
import dada.com.kproject.util.wrapper.ApiWrapper
import dada.com.kproject.util.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

open class BaseViewModel(private  val repo:IRepository):ViewModel() {
    protected val _needFetchToken = MutableLiveData<Boolean>()

    protected val apiQueue: Queue<ApiWrapper> = LinkedList()

    fun refreshToken(){
        _needFetchToken.value = true
    }

    protected val _spinner = MutableLiveData<Boolean>()

    val spinner: LiveData<Boolean>
        get() = _spinner

    protected val _apiWrapperData = MutableLiveData<ApiWrapper>()

    protected val apiWrapperData:LiveData<ApiWrapper>
    get() = _apiWrapperData

    protected var token: String = (repo.getLocalToken() ?: "").also {
        if (it.isEmpty()) {
            _needFetchToken.value = true
        }
    }


    // 當 apiWrapperData 被餵資料進來時
    fun <T> launchApi(apiWrapper: ApiWrapper,block: suspend(token:String) -> Response<T>) =
        liveData(Dispatchers.IO) {
            _spinner.postValue(true)
            try {
                val response =block.invoke(token)

                if ( (response.code() == 401 || response.code() == 403) && apiWrapper.firstLoad){
                    apiQueue.add(apiWrapper.also {
                        it.firstLoad = false
                    })
                    _needFetchToken.postValue(true)
                } else if(response.isSuccessful) {
                    emit(
                        Resource.success(
                            data = response.body()
                        )
                    )
                    _spinner.postValue(false)
                }else{
                    emit(Resource.error(data = null, message =  Global.getContext().getString(
                        R.string.api_error)))
                    _spinner.postValue(false)
                }

            }catch (exception: Exception){
                emit(Resource.error(data = null, message = exception.message ?: Global.getContext().getString(
                    R.string.api_error)))
                _spinner.postValue(false)
            }
        }

    protected val _tokenRefreshed = MutableLiveData<Boolean>()
    fun isTokenRefreshed():LiveData<Boolean> = _tokenRefreshed

    fun fetchToken() = Transformations.switchMap(_needFetchToken) {

        launchDataLoad {
            repo.fetchRemoteToken()
        }
    }.map {
        viewModelScope.launch(Dispatchers.IO) {
            if (it.data != null) {
                token = "${it.data.tokenType} ${it.data.accessToken}"
                repo.saveToken(token)
                _tokenRefreshed.postValue(true)

                while (apiQueue.isNotEmpty()) {
                    val apiWrapper = apiQueue.poll()
                    _apiWrapperData.postValue(apiWrapper.also {
                        it.firstLoad = false
                    })
                }
            } else {
                apiQueue.clear()
            }
        }
    }


    fun <T> launchDataLoad (block: suspend() -> T) =
        liveData(Dispatchers.IO) {
            try {
                emit(Resource.success(data = block.invoke()))
            }catch (exception: Exception){
                emit(Resource.error(data = null, message = exception.message ?: Global.getContext().getString(
                    R.string.api_error)))
            }
        }







}