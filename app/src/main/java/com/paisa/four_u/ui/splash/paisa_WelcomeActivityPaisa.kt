package com.paisa.four_u.ui.splash

import android.animation.ObjectAnimator
import androidx.core.animation.doOnEnd
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityWelcomeBinding
import com.paisa.four_u.ui.main.paisa_MainActivityPaisa
import com.paisa.four_u.ui.permission.paisa_PermissionActivityPaisa
import com.paisa.four_u.util.paisa_DeviceUtils.getDeviceDefaultLanguage
import com.paisa.four_u.util.paisa_DeviceUtils.getUUID
import com.paisa.four_u.util.paisa_FBEvent
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.launch
import com.paisa.four_u.util.slog


class paisa_WelcomeActivityPaisa : paisa_BaseActivity<ActivityWelcomeBinding, paisa_WelcomeVMPaisa>(
    ActivityWelcomeBinding::inflate, paisa_WelcomeVMPaisa::class.java
) {


    override fun initData() {

        SpRepository.languageCode=if(getDeviceDefaultLanguage() == "id") "ID" else "EN"

        getUUID()
        vm.apply {
            getCustomerService()
            liveService.observe(this@paisa_WelcomeActivityPaisa) {
                SpRepository.customerService = it
            }
        }


        val animator = ObjectAnimator.ofFloat(vb.ivIcon,"alpha",0F,1F)
        animator.duration = 2000
        animator.start()
        animator.doOnEnd {

            if (SpRepository.token.isBlank()) {
                paisa_FBEvent.trackEvent_FirstOpen()
                launch<paisa_PermissionActivityPaisa> { }
            } else {
                launch<paisa_MainActivityPaisa> { }
            }
            finish()
        }




        if(SpRepository.oaid.isEmpty()){
            DeviceID.getOAID(this, object : IGetter {
                override fun onOAIDGetComplete(result: String) {
                    result.slog(" 获取oaid信息  完成")
                    SpRepository.oaid=result

                }
                override fun onOAIDGetError(error: Exception?) {
                    error.slog(" 获取oaid信息  失败")
                    SpRepository.oaid= DeviceIdentifier.getGUID(this@paisa_WelcomeActivityPaisa)
                }

            })
        }





    }

    override fun initView() {
    }


}