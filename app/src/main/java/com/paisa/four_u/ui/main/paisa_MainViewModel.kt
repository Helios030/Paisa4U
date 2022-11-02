package com.paisa.four_u.ui.main

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.UserInfoModel
import com.paisa.four_u.util.paisa_DeviceUtils
import com.paisa.four_u.util.expand.createBody

/**
 * @Author Ben
 * @Date 2022/4/14 16:04
 * @desc:
 */
class paisa_MainViewModel : paisa_BaseVM() {

    val liveUserInfo = MutableLiveData<UserInfoModel>()
    val liveRePayBank = MutableLiveData<List<String>>()

    fun getUserInfo() {
        val dataMap = HashMap<String, Any>()
        dataMap["isRoot"] = paisa_DeviceUtils.isSuEnableRoot()
        request({
            mApiService.getUserInfo(dataMap.createBody())
        }, {
            liveUserInfo.value = this
        })
    }

    fun getrePaybank(map: HashMap<String,Any>) {
        request({
            mApiService.getRepayBankList(map.createBody())
        }, {
            liveRePayBank.value = this
        })
    }


}