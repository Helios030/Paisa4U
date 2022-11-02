package com.paisa.four_u.ui.face

import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.LicenseModel
import com.paisa.four_u.util.expand.createBody
import com.paisa.four_u.util.slog

/**
 * @Author Ben
 * @Date 2022/4/15 15:42
 * @desc:
 */
class paisa_FaceVMPaisa: paisa_BaseVM() {


    val liveAdvancelicense=MutableLiveData<LicenseModel>()

    fun getAdvancelicense(){
        request({
            mApiService.getAdvancelicense(HashMap<String, Any>().createBody())
        }, {
            liveAdvancelicense.value=this

        })
    }

    fun uploadLivenessId(id:String){
        request({
            val dataMap = HashMap<String, Any>()
            dataMap["livenessid"] = id
            mApiService.uploadLivenessId(dataMap.createBody())

        }, {
            this.slog("uploadLivenessId 结果 ")
        })
    }

    fun uploadLiveness(bytes:ByteArray,img:ByteArray){
        request({
            val dataMap = HashMap<String, Any>()
            dataMap["liveness"] = bytes
            dataMap["image"] = img
            mApiService.uploadLiveness(dataMap.createBody())

        }, {
            this.slog("uploadLiveness 结果 ")
        })
    }





}