package com.paisa.four_u.ui.userData.second

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.UserDataSecondModel
import com.paisa.four_u.model.UserDataTwoModel
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.expand.createBody
import com.paisa.four_u.util.expand.toMap

/**
 * @Author Ben
 * @Date 2022/4/15 16:21
 * @desc:
 */
class paisa_AdditionalVMPaisa : paisa_BaseVM(){


    var liveUserDataBean = MutableLiveData<UserDataSecondModel>()
    var liveUploadState = MutableLiveData<Boolean>()

    fun getUserDataSecond() {
        request({
            mApiService.getUserDataTwo(HashMap<String, Any>().apply { this["part_info"] = "2" }.createBody())
        }, {
            liveUserDataBean.value = this
        })
    }

    fun uploadUserDataSecond(userDataModel: UserDataTwoModel) {
        request({
            mApiService.upUserDataTwo(userDataModel.toMap().createBody())
        }, {
            Slog.d("===>   request  陈工")
            liveUploadState.value = true
        },{
            Slog.d("===>   request  失败 $this")
            liveUploadState.value = false
        })
    }




}