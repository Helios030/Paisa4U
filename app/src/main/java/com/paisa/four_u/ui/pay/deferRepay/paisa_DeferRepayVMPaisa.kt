package com.paisa.four_u.ui.pay.deferRepay

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.DeferRepayModel
import com.paisa.four_u.util.expand.createBody

/**
 * @Author Ben
 * @Date 2022/4/24 17:28
 * @desc:
 */
class paisa_DeferRepayVMPaisa : paisa_BaseVM() {


    val liveDeferRepayBean = MutableLiveData<DeferRepayModel>()
    val liveRePayBankBean = MutableLiveData<List<String>>()

    fun getDeferRepayData(order: String) {


        val map = HashMap<String, Any>()
        map["order_no"] = order
        map["repay_type"] = "5"
        map["repay_bank"] = "UPI"

        request({
            mApiService.getDeferRepayData(map.createBody())
        }, {
            liveDeferRepayBean.value = this
        })
    }

    fun getDeferRepayVaData(orderID: String, type: Int, repayBank: String) {
        val map = HashMap<String, Any>()
        map["order_no"] = orderID
        map["repay_type"] = type
        map["repay_bank"] = repayBank
        request({
            mApiService.getDeferRepayVaData(map.createBody())
        }, {
            liveDeferRepayBean.value = this
        })

    }

    fun getRepayBankList(type: Int) {
        request({
            mApiService.getRepayBankList(HashMap<String, Any>().apply { this["type"] = type }
                .createBody())
        }, {
            liveRePayBankBean.value = this
        })

    }


}