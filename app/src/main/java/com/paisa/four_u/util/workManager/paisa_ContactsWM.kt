package com.paisa.four_u.util.workManager

import android.content.Context
import android.text.TextUtils
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.model.PhoneModel
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.net.paisa_HttpManager
import com.paisa.four_u.util.paisa_ContactUtil
import com.paisa.four_u.util.expand.createBody


/**
 * 通讯录数据
 */
class paisa_ContactsWM(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    private val mApiService by lazy { paisa_HttpManager.create(paisa_ApiServices::class.java) }

    override suspend fun doWork(): Result {
        try {
            val contactsList = paisa_ContactUtil.getContacts(paisa_RApplication.instance)
            val phoneList = ArrayList<PhoneModel>()
            for (contacts in contactsList){
                val phones = contacts.numbers
                if (phones.isNotEmpty()){
                    for (phoneBean in phones){
                        if (!TextUtils.isEmpty(phoneBean.phone)){
                            phoneList.add(phoneBean)
                        }
                    }
                }
            }

            val map = HashMap<String,Any>()
            map["list"] = phoneList
            mApiService.upContactsList(map.createBody())
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }




}