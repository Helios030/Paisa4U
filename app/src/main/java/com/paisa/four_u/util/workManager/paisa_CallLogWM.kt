package com.paisa.four_u.util.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.net.paisa_HttpManager
import com.paisa.four_u.util.paisa_ContactUtil
import com.paisa.four_u.util.expand.createBody


/**
 * 通话记录收集
 */
class paisa_CallLogWM (private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val mApiService by lazy { paisa_HttpManager.create(paisa_ApiServices::class.java) }

    override suspend fun doWork(): Result {
        try {
            val callRecordList = paisa_ContactUtil.getCallLog(paisa_RApplication.instance)
            val map = HashMap<String,Any>()
            map["call_record_list"] = callRecordList
            mApiService.upCallRecordList(map.createBody())
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }

}