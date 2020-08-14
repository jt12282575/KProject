package dada.com.kproject.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dada.com.kproject.Global
import dada.com.kproject.R
import dada.com.kproject.util.wrapper.Resource
import kotlinx.coroutines.Dispatchers

open class BaseViewModel:ViewModel() {
    protected val _spinner = MutableLiveData<Boolean>()

    protected val _needFetchToken = MutableLiveData<Boolean>()

    fun refreshToken(){
        _needFetchToken.value = true
    }

    val spinner: LiveData<Boolean>
        get() = _spinner

    fun <T> launchDataLoad (block: suspend() -> T) =
        liveData(Dispatchers.IO) {
            _spinner.postValue(true)
            try {
                emit(Resource.success(data = block.invoke()))
            }catch (exception: Exception){
                emit(Resource.error(data = null, message = exception.message ?: Global.getContext().getString(
                    R.string.api_error)))
            }finally {
                _spinner.postValue(false)
            }
        }

    protected val _tokenRefreshed = MutableLiveData<Boolean>()
    fun tokenRefreshed():LiveData<Boolean> = _tokenRefreshed

}