package com.paisa.four_u.ui.loan

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.LoanOptionsModel
import com.paisa.four_u.util.expand.createBody

/**
 * @Author Ben
 * @Date 2022/4/24 10:53
 * @desc:
 */
class paisa_LoanVMPaisa: paisa_BaseVM() {

    var liveLoanOptions=MutableLiveData<LoanOptionsModel>()
    fun getLoanOptions(){
        request({
            mApiService.getLoanOptions(HashMap<String, Any>().createBody())
        }, {
            liveLoanOptions.value = this
        })
    }


}