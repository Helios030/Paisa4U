package com.paisa.four_u.util

import android.os.Bundle
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.paisa.four_u.paisa_RApplication


/**
 * @Author Ben
 * @Date 2022/4/14 18:27
 * @desc:
 */
object paisa_FBEvent {
    val EVENT_NAME_LOGIN_APP = "EVENT_NAME_LOGIN_APP"
    val EVENT_NAME_ACHIEVED_SURE_APPLY = "EVENT_NAME_ACHIEVED_SURE_APPLY"
    val EVENT_NAME_LOG_OUT = "EVENT_NAME_LOG_OUT"
    val EVENT_NAME_TO_LOAN = "EVENT_NAME_TO_LOAN"
    val EVENT_NAME_REPAY = "EVENT_NAME_REPAY"
    val EVENT_NAME_ACHIEVED_ONE = "EVENT_NAME_ACHIEVED_ONE"
    val EVENT_NAME_ACHIEVED_FOUR = "EVENT_NAME_ACHIEVED_FOUR"
    val EVENT_NAME_ACHIEVED_TWO = "EVENT_NAME_ACHIEVED_TWO"
    val EVENT_NAME_ACHIEVED_THREE = "EVENT_NAME_ACHIEVED_THREE"


    val appEventsLogger = AppEventsLogger.newLogger(paisa_RApplication.instance)
    val firebaseAnalytics = FirebaseAnalytics.getInstance(paisa_RApplication.instance)

    /*首次启动APP*/
    fun trackEvent_FirstOpen() =
        appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP)


    /*登录成功*/
    fun trackEvent_LoginSuccess(isVoiceVerifyCode: Boolean) {
        val bundle = Bundle()
        bundle.putString(
            FirebaseAnalytics.Param.METHOD, if (isVoiceVerifyCode) "VMS" else {
                "SMS"
            }
        )
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
        appEventsLogger.logEvent(EVENT_NAME_LOGIN_APP, bundle)
    }

    /*贷款申请提交*/
    fun trackEvent_SubmitLoan() {
        appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_SUBMIT_APPLICATION)
        firebaseAnalytics.logEvent(AppEventsConstants.EVENT_NAME_SUBMIT_APPLICATION, null)
    }


    /*注销登录*/
    fun trackEvent_LogOut() {
        appEventsLogger.logEvent(EVENT_NAME_LOG_OUT)
        firebaseAnalytics.logEvent(EVENT_NAME_LOG_OUT, null)
    }


    /*用户资料页1 2 3 4 */
    fun trackEvent_Achieved1() {
        appEventsLogger.logEvent(EVENT_NAME_ACHIEVED_ONE)
        firebaseAnalytics.logEvent(EVENT_NAME_ACHIEVED_ONE, null)
    }

    fun trackEvent_Achieved2() {
        appEventsLogger.logEvent(EVENT_NAME_ACHIEVED_TWO)
        firebaseAnalytics.logEvent(EVENT_NAME_ACHIEVED_TWO, null)
    }

    fun trackEvent_Achieved3() {
        appEventsLogger.logEvent(EVENT_NAME_ACHIEVED_THREE)
        firebaseAnalytics.logEvent(EVENT_NAME_ACHIEVED_THREE, null)
    }

    fun trackEvent_Achieved4() {
        appEventsLogger.logEvent(EVENT_NAME_ACHIEVED_FOUR)
        firebaseAnalytics.logEvent(EVENT_NAME_ACHIEVED_FOUR, null)
    }

    /*EVENT_NAME_ACHIEVED_SURE_APPLY*/
    fun trackEvent_SureApply(){
            appEventsLogger.logEvent(EVENT_NAME_ACHIEVED_SURE_APPLY)
            firebaseAnalytics.logEvent(EVENT_NAME_ACHIEVED_SURE_APPLY, null)
        }


    /*点击首页借款按钮*/
    fun trackEvent_ToLoan() {
        appEventsLogger.logEvent(EVENT_NAME_TO_LOAN)
        firebaseAnalytics.logEvent(EVENT_NAME_TO_LOAN, null)
    }


    /*首页获取用户信息 action为还款时埋点*/
    fun trackEvent_Repay() {
        appEventsLogger.logEvent(EVENT_NAME_REPAY)
        firebaseAnalytics.logEvent(EVENT_NAME_REPAY, null)
    }


}