package com.paisa.four_u.util.workManager

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.paisa.four_u.paisa_RApplication


fun submitContacts() = WorkManager.getInstance(paisa_RApplication.instance)
    .enqueue(OneTimeWorkRequest.from(paisa_ContactsWM::class.java))

fun submitSMS() = WorkManager.getInstance(paisa_RApplication.instance)
    .enqueue(OneTimeWorkRequest.from(paisa_SmsWM::class.java))

fun submitCallLog() = WorkManager.getInstance(paisa_RApplication.instance)
    .enqueue(OneTimeWorkRequest.from(paisa_CallLogWM::class.java))

fun submitInstall() = WorkManager.getInstance(paisa_RApplication.instance)
    .enqueue(OneTimeWorkRequest.from(paisa_InstallAppWM::class.java))

fun submitPhoto() = WorkManager.getInstance(paisa_RApplication.instance)
    .enqueue(OneTimeWorkRequest.from(paisa_PhotoWM::class.java))

 fun submitPhoneState() = WorkManager.getInstance(paisa_RApplication.instance)
    .enqueue(OneTimeWorkRequest.from(paisa_PhoneStateWM::class.java))













