package com.paisa.four_u.ui.splash

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.util.expand.createBody

/**
 * @Author Ben
 * @Date 2022/5/16 16:57
 * @desc:
 */
class paisa_WelcomeVMPaisa: paisa_BaseVM() {

    val liveService= MutableLiveData<String>()
    fun getCustomerService(){
        request({
            mApiService.getCustomerService(  HashMap<String, Any>().createBody())
        }, {
            liveService.value = this
        }, isShowLoading = false)

    }

}