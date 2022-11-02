package com.paisa.four_u.ui.userData.fourth

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.UserDataFourModel
import com.paisa.four_u.model.UserDataFourthModel
import com.paisa.four_u.util.expand.createBody
import com.paisa.four_u.util.expand.toMap

/**
 * @Author Ben
 * @Date 2022/4/15 16:22
 * @desc:
 */
class paisa_BankVMPaisa : paisa_BaseVM() {
    var liveUserDataBean = MutableLiveData<UserDataFourthModel>()
    var liveUploadState = MutableLiveData<Boolean>()
    fun getUserDataFourth() {
        request({
            mApiService.getUserDataFourth(HashMap<String, Any>().apply { this["part_info"] = "4" }.createBody())
        }, {
            liveUserDataBean.value = this
        })
    }
    fun uploadUserDataFourths(userDataModel: UserDataFourModel) {
        request({
            mApiService.upUserDataFourth(userDataModel.toMap().createBody())
        }, {
            liveUploadState.value = true
        }, {
            liveUploadState.value = false
        })
    }
}