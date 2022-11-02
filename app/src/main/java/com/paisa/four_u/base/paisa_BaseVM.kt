package com.paisa.four_u.base
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paisa.four_u.model.BaseModel
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.net.paisa_HttpManager
import com.paisa.four_u.util.expand.show
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException


abstract class paisa_BaseVM : ViewModel() {

    protected val mApiService by lazy { paisa_HttpManager.create(paisa_ApiServices::class.java) }

    val liveLoading = MutableLiveData<Boolean>()

    protected fun <T> request(
        block: suspend () -> BaseModel<T>,
        data: T.() -> Unit,
        error: (Throwable.() -> Unit) = {},
        isShowLoading: Boolean = true
    ) = viewModelScope.launch {
        runCatching {
            liveLoading.value = isShowLoading
            block()
        }.onSuccess {
            if (it.success) {


                it.ret?.let { data -> data(data) }

            } else {
                it.msg?.show()
            }
            liveLoading.value = false
        }.onFailure {
            if (it is HttpException) {
                it.message?.show()
            }
            it.printStackTrace()
            liveLoading.value = false
            error(it)
        }
    }

    protected fun <T> async(block: suspend () -> T): Deferred<T> {
        return viewModelScope.async { block() }
    }







}


