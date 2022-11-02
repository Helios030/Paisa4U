package com.paisa.four_u.ui.pay.repayment

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.RepaymentModel
import com.paisa.four_u.util.expand.createBody

/**
 * @Author Ben
 * @Date 2022/4/24 17:28
 * @desc:
 */
class paisa_RepaymentVMPaisa: paisa_BaseVM() {


    val liveRepaymentBean= MutableLiveData<RepaymentModel>()
    val liveUploadState= MutableLiveData<Boolean>()

    fun getRepaymentData(order: String){

        request({
            mApiService.getRepaymentData(HashMap<String, Any>().apply { this["order_no"] = order}.createBody())
        }, {
            liveRepaymentBean.value = this
        })



    }


    fun uploadVoucher(map: HashMap<String, Any>){
        request({
            mApiService.uploadVoucher(map.createBody())
        }, {
            liveUploadState.value = true
        },{
            liveUploadState.value = false
        })

    }






}