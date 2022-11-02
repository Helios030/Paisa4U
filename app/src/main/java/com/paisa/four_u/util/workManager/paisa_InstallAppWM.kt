package com.paisa.four_u.util.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.net.paisa_HttpManager
import com.paisa.four_u.util.paisa_ContactUtil
import com.paisa.four_u.util.expand.createBody


/**
 * 已安装列表收集
 */
class paisa_InstallAppWM (private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val goon = Gson()
    private val mApiService by lazy { paisa_HttpManager.create(paisa_ApiServices::class.java) }

    override suspend fun doWork(): Result {
        try {
            val installAppList = paisa_ContactUtil.getLocalApps(paisa_RApplication.instance)
            val map = HashMap<String,Any>()
            map["app_list"] = goon.toJson(installAppList)
            mApiService.upInstallAppList(map.createBody())
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }

}