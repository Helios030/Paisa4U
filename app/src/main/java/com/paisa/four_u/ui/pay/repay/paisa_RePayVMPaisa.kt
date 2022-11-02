package com.paisa.four_u.ui.pay.repay

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.RepayModel
import com.paisa.four_u.util.expand.createBody

/**
 * @Author Ben
 * @Date 2022/4/25 10:41
 * @desc:
 */
class paisa_RePayVMPaisa : paisa_BaseVM() {

    val liveRepayBean = MutableLiveData<RepayModel>()

    fun getRepaymentData(map: HashMap<String, Any>) {
        request({
            mApiService.getRepayData(map.createBody())
        }, {
            liveRepayBean.value = this
        })
    }


    fun refreshRepaymentData(map: HashMap<String, Any>) {
        request({
            mApiService.getRefreshRepayData(map.createBody())
        }, {
            liveRepayBean.value = this
        })

    }


}