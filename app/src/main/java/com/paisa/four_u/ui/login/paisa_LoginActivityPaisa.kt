package com.paisa.four_u.ui.login

import android.Manifest
import android.content.pm.PackageManager
import android.os.RemoteException
import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ActivityCompat
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityLoginBinding
import com.paisa.four_u.ui.main.paisa_MainActivityPaisa
import com.paisa.four_u.util.paisa_FBEvent
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.*
import com.paisa.four_u.util.slog

class paisa_LoginActivityPaisa : paisa_BaseActivity<ActivityLoginBinding, paisa_LoginVMPaisa>(
    ActivityLoginBinding::inflate, paisa_LoginVMPaisa::class.java
) {

    private var isVoiceVerifyCode: Boolean = false
    override fun onStart() {
        super.onStart()
        if (SpRepository.isReferrer) {
            initInstallReferrer()
        }
    }

    override fun initData() {

        vm.apply {
            liveCode.observe(this@paisa_LoginActivityPaisa) {
                vb.apply {
//                  设置不可点击
                    loginGetVerificationCodeBtn.isEnabled = it
                    loginGetVoiceVerificationCodeBtn.isEnabled = it
                    loginUserInvitationCodeEt. requestFocus()
                }


            }
            liveCodeStr.observe(this@paisa_LoginActivityPaisa) {
                vb.tvObtain.text = it
            }

            liveLoginBean.observe(this@paisa_LoginActivityPaisa) {
                paisa_FBEvent.trackEvent_LoginSuccess(isVoiceVerifyCode)
                SpRepository.isFirstOpenPP = false
                val phone=  vb.loginUserPhoneEt.text.toString()
                SpRepository.phone=phone
                upAPPData(
                    referrerUrl,
                    referrerClickTime,
                    appInstallTime,
                    instantExperienceLaunched,
                    phone
                )
                launch<paisa_MainActivityPaisa> { }
                finish()
            }
        }

    }

    override fun initView() {
        showCustomToast()

        vb.apply {
            loginGetVerificationCodeBtn.setOnClickListener {
                if (!loginProtocolCheck.isChecked) {
                    R.string.paisa_please_read_and_agree.show()
                    return@setOnClickListener
                }
                isVoiceVerifyCode=false
                vm.sendCode(loginUserPhoneEt.text.toString())
            }
            loginGetVoiceVerificationCodeBtn.setOnClickListener {
                if (!loginProtocolCheck.isChecked) {
                    R.string.paisa_please_read_and_agree.show()
                    return@setOnClickListener
                }
                isVoiceVerifyCode=true
                vm.sendVoiceCode(loginUserPhoneEt.text.toString())
            }



            llPrivacy.setOnClickListener {
                openUri( str(R.string.paisa_privacy_policy),BuildConfig.PRIVACY_POLICY)
            }

            loginUserInvitationCodeEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (str?.length == 6) {
                        if (!loginProtocolCheck.isChecked)
                            R.string.paisa_please_read_and_agree.show()
                        else{
                           hideSoftInputWindow()
                            vm.login(
                                loginUserPhoneEt.text.toString(),
                                loginUserInvitationCodeEt.text.toString()
                            )
                        }

                    }


                }

                override fun afterTextChanged(p0: Editable?)
                {}

            })


        }

    }


    var referrerDetails: ReferrerDetails?=null
    // 已安装软件包的引荐来源网址
    var referrerUrl=""
    // 引荐来源网址点击事件发生时的时间戳（以秒为单位）
    var referrerClickTime =0L
    // 安装开始时的时间戳（以秒为单位）
    var appInstallTime =0L
    // 用于检查用户在过去 7 天内是否曾与应用的免安装体验互动
    var instantExperienceLaunched =false

    /**
     * 通过谷歌商店获取流量来源信息
     */
    private fun initInstallReferrer() {
        val mReferrerClient: InstallReferrerClient = InstallReferrerClient.newBuilder(this).build()


        mReferrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established.(连接已建立)
                        try {
                            /**
                             * utm_source:   广告系列来源，用于确定具体的搜索引擎、简报或其他来源
                             * utm_medium:   广告系列媒介，用于确定电子邮件或采用每次点击费用 (CPC) 的广告等媒介
                             * utm_term:     广告系列字词，用于付费搜索，为广告提供关键字
                             * utm_content:  广告系列内容，用于 A/B 测试和内容定位广告，以区分指向相同网址的不同广告或链接
                             * utm_campaign: 广告系列名称，用于关键字分析，以标识具体的产品推广活动或战略广告系列
                             * gclid:        Google Ads 自动标记参数，用于衡量广告。此值会动态生成，请勿修改
                             */
                            referrerDetails = mReferrerClient.installReferrer
                            // 已安装软件包的引荐来源网址
                            referrerUrl = referrerDetails!!.installReferrer
                            // 引荐来源网址点击事件发生时的时间戳（以秒为单位）
                            referrerClickTime = referrerDetails!!.referrerClickTimestampSeconds
                            // 安装开始时的时间戳（以秒为单位）
                            appInstallTime = referrerDetails!!.installBeginTimestampSeconds
                            // 用于检查用户在过去 7 天内是否曾与应用的免安装体验互动
                            instantExperienceLaunched = referrerDetails!!.googlePlayInstantParam
                            upAPPData(
                                referrerUrl,
                                referrerClickTime,
                                appInstallTime,
                                instantExperienceLaunched,
                                ""
                            )
                        } catch (e: RemoteException) {
                            e.printStackTrace()
                            e.slog("来源获取出错")
                        }
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API not available on the current Play Store app. (当前Play商店应用中不提供API)
//                        Logger.i("LoginActivity", "当前Play商店应用中不提供API")
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // Connection couldn't be established. (无法建立连接)
//                        Logger.i("LoginActivity", "无法建立连接")
                    }
                }
                // 断开服务连接，获取引荐来源信息后，请对 InstallReferrerClient 实例调用 endConnection() 方法来断开连接。断开连接将有助于避免出现泄露和性能问题。
                mReferrerClient.endConnection()
            }

            override fun onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to (尝试在下一个请求重新启动连接)
                // Google Play by calling the startConnection() method. (通过调用startConnection方法)
//                Logger.i("LoginActivity", "尝试在下一个请求重新启动连接")
            }
        })
    }

    /**
     * 上传流量来源
     */
    private fun upAPPData(
        installReferrer: String,
        referrerClickTimestampSeconds: Long,
        installBeginTimestampSeconds: Long,
        instantExperienceLaunched: Boolean,
        phone:String
    ) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val mapAPPData = java.util.HashMap<String, Any>()
            mapAPPData["uuid"] = SpRepository.uuid
            mapAPPData["install_referrer"] = installReferrer
            mapAPPData["referrer_click_times"] = referrerClickTimestampSeconds
            mapAPPData["install_begin_times"] = installBeginTimestampSeconds
            mapAPPData["instant_experience_launched"] = if (instantExperienceLaunched) {
                1
            } else {
                2
            }
            mapAPPData["phone"] = BuildConfig.AREA_CODE+phone
            SpRepository.isReferrer=false

            vm.upInstallReferrer(mapAPPData)
        }
    }


}