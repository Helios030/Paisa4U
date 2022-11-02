package com.paisa.four_u.ui.login

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.LoginModel
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.createBody
import com.paisa.four_u.util.expand.show
import com.paisa.four_u.util.expand.str
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @Author Ben
 * @Date 2022/4/14 16:04
 * @desc:
 */
class paisa_LoginVMPaisa : paisa_BaseVM() {

    val liveCode = MutableLiveData<Boolean>()
    val liveCodeStr = MutableLiveData<String>()
    val liveLoginBean = MutableLiveData<LoginModel>()

    fun sendCode(phone: String) {
        if (phone.isEmpty()) {
            R.string.paisa_please_enter_phone.show()
            return
        }
        val dataMap = HashMap<String, Any>()
        dataMap["phone"] = BuildConfig.AREA_CODE + phone
        request({
            Slog.d("发起请求    ")
            mApiService.getVerificationCode(dataMap.createBody())
        }, {
            Slog.d("返回结果  $this   ")
            changeCodeStatus()
        }, {
            Slog.d("访问错误  $this")
        })
    }

    fun sendVoiceCode(phone: String) {
        if (phone.isEmpty()) {
            R.string.paisa_please_enter_phone.show()
            return
        }
        val dataMap = HashMap<String, Any>()
        dataMap["phone"] = BuildConfig.AREA_CODE + phone
        request({
            Slog.d("发起请求    ")
            mApiService.getVoiceVerificationCode(dataMap.createBody())
        }, {
            Slog.d("返回结果  $this   ")
            changeCodeStatus()
        }, {
            Slog.d("访问错误  $this")
        })
    }


    private fun changeCodeStatus() {
        viewModelScope.launch {
            liveCode.value = false
            for (i in 60 downTo 1) {
                liveCodeStr.value = "${i}s"
                delay(1000)
            }
            liveCode.value = true
            liveCodeStr.value = ""
        }
    }


    fun login(phone: String, code: String) {
        if (phone.isEmpty()) {
          R.string.paisa_please_enter_phone.show()
            return
        }

        if (code.isEmpty()) {
             R.string.paisa_fill_code.show()
            return
        }

        val dataMap = HashMap<String, Any>()
        dataMap["phone"] = BuildConfig.AREA_CODE + phone
        dataMap["valid_code"] = code
        dataMap["phone_token"] = ""
        dataMap["app"] = str(R.string.app_name)
        dataMap["sub_app"] = str(R.string.app_name) + "_android"
        dataMap["password"] = ""
        dataMap["new_password"] = ""
        dataMap["ov"] = Build.VERSION.RELEASE
        dataMap["place"] = "google"
        dataMap["sub_place_code"] = ""
        dataMap["other_info"] = ""
        dataMap["passport"] = ""
        request({
            mApiService.login(dataMap.createBody())
        }, {
            SpRepository.token = this.token
            liveLoginBean.value = this
        })


    }


    fun upInstallReferrer(map: HashMap<String, Any>) {
        request({
            mApiService.upInstallReferrer(map.createBody())
        }, {
            Slog.d("===>  来源上传  ")
        })

    }


}