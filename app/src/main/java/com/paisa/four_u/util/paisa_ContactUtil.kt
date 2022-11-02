package com.paisa.four_u.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageInfo
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import android.provider.Telephony
import android.text.TextUtils
import com.paisa.four_u.model.*
import java.text.DateFormat
import java.text.SimpleDateFormat

object paisa_ContactUtil {
    /**
     *获取联系人列表
     */
    @SuppressLint("Range")
    fun getContacts(context: Context): List<ContactModel> {
        try {
            val reslover = context.contentResolver
            // 获得联系人手机号码
            val cursor = reslover.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, arrayOf(ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER), null, null, null)
            val phoneMap = hashMapOf<String, ContactModel>()
            while (cursor != null && cursor.moveToNext()) {
                // 获得联系人姓名
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                // 获得联系人手机号
                var phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                if (TextUtils.isEmpty(phoneNumber)) continue
                phoneNumber = phoneNumber.replace(" ".toRegex(), "") // 去掉空格
                phoneNumber = phoneNumber.replace("-".toRegex(), "") // 去掉-符号
                val phoneBean = PhoneModel()
                phoneBean.name = name
                phoneBean.phone = phoneNumber
                if (!phoneMap.containsKey(name)) {
                    val contactBean = ContactModel()
                    var sortString = "#"
                    if (!TextUtils.isEmpty(name)) {
                        contactBean.name = name
//                        val pinyin: String = Cn2Spell.getPinYin(name)
//                        contactBean.pingyin = pinyin
//                        sortString = if (!TextUtils.isEmpty(pinyin) && pinyin.length > 0) pinyin.substring(0, 1).toUpperCase() else ""
                        sortString = if (!TextUtils.isEmpty(name) && name.length > 0) name.substring(0, 1).toUpperCase() else ""
                        // 正则表达式，判断首字母是否是英文字母
                        if (sortString.matches(Regex("[A-Z]"))) {
                            contactBean.sortString = sortString.toUpperCase()
                        }
//                        else {
//                            sortString = "#"
//                            contactBean.sortString = sortString
//                        }
                    } else {
                        contactBean.name = phoneNumber
//                        contactBean.sortString = sortString
                    }
                    contactBean.addNumber(phoneBean)
                    phoneMap[name] = contactBean
                } else {
                    val contactBean = phoneMap[name]
                    contactBean?.addNumber(phoneBean)
                }
            }
            cursor?.close()//关闭cursor，避免内存泄露
            return getListFromMap(phoneMap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ArrayList<ContactModel>()
    }

    fun getListFromMap(map: HashMap<String, ContactModel>): List<ContactModel> {
        val listValue = ArrayList<ContactModel>()
        val it = map.keys.iterator()
        while (it.hasNext()) {
            val key = it.next()
//            if (map.containsKey(key))
                map[key]?.let { it1 -> listValue.add(it1) }
        }
        //        if(!listValue.isEmpty())
//        {
//            GjjAccountAllInfo.ComparatorUser comparator=new GjjAccountAllInfo.ComparatorUser();
//            Collections.sort(listValue, comparator);
//        }
        return listValue
    }

    /**
     * 获取短信记录列表
     */
    @TargetApi(21)
    fun getSmsMessageLog(context: Context): ArrayList<SMSMessageModel>{
        val messageArrylist = ArrayList<SMSMessageModel>()
        val resolver = context.contentResolver
        val cursor = resolver.query(Telephony.Sms.CONTENT_URI, arrayOf(
                Telephony.Sms.ADDRESS,  //
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.READ,
                Telephony.Sms.STATUS,
                Telephony.Sms.TYPE), null, null, "date DESC limit 1500")
        if(cursor != null){
            while (cursor.moveToNext()){
                val message = SMSMessageModel()
                message.phone = cursor.getString(0)
                message.sms_content = cursor.getString(1)
                message.time = formateTime(cursor.getLong(2))
                message.is_read = cursor.getInt(3)
                message.type = getMessageType(cursor.getInt(5))
                message.name = getPerson(context, message.phone.toString())
                messageArrylist.add(message)
            }
            cursor.close() //关闭cursor，避免内存泄露
        }
        return messageArrylist
    }


    /**
     * 获取手机安装APP列表
     */
    fun getLocalApps(context: Context): ArrayList<AppInfoModel>{
        val myAppIfs = ArrayList<AppInfoModel>()
        val packageManager = context.packageManager
        val packageIfs:List<PackageInfo> = packageManager.getInstalledPackages(0)
        try {
            for(i in packageIfs.indices){
                val packageInfo:PackageInfo = packageIfs[i]
//                if (ApplicationInfo.FLAG_SYSTEM and packageInfo.applicationInfo.flags != 0){
//                    continue
//                }
                if (formateTime(packageInfo.firstInstallTime).contains("1970-01-01")){
                    continue
                }
                val myAppInfo = AppInfoModel()
                val name = packageInfo.applicationInfo.loadLabel(packageManager) as String
                myAppInfo.packageName = packageInfo.packageName
                myAppInfo.appName = name
                myAppInfo.firstInstallTime = formateTime(packageInfo.firstInstallTime)
                myAppInfo.lastUpdateTime = formateTime(packageInfo.lastUpdateTime)
                myAppInfo.versionName = packageInfo.versionName
                myAppIfs.add(myAppInfo)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return myAppIfs
    }

    /**
     * 获取通话记录列表
     */
    @SuppressLint("Recycle")
    @TargetApi(21)
    fun getCallLog(context: Context): ArrayList<CallRecordModel> {
        val callRecordArrayList: ArrayList<CallRecordModel> = ArrayList()
        val resolver = context.contentResolver
        val cursor = resolver.query(CallLog.Calls.CONTENT_URI, arrayOf(
                CallLog.Calls.CACHED_FORMATTED_NUMBER,
                CallLog.Calls.CACHED_MATCHED_NUMBER,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.GEOCODED_LOCATION), null, null, "date DESC limit 1500")
        if(cursor != null){
            try {
                while (cursor.moveToNext()){
                    val record = CallRecordModel()
                    record.phone = cursor.getString(1)
                    record.name = cursor.getString(2)
                    record.type = getCallType(cursor.getInt(3))
                    record.call_time = formateTime(cursor.getLong(4))
                    record.call_duration = cursor.getLong(5)
                    if (record.call_duration!! > 0) {
                        record.is_connected = 1
                    } else {
                        record.is_connected = 2
                    }
                    callRecordArrayList.add(record)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor.close() //关闭cursor，避免内存泄露
            }
        }
        return callRecordArrayList
    }

    @SuppressLint("SimpleDateFormat")
    private fun formateTime(time: Long): String {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return dateFormat.format(time)
    }

    private fun getMessageType(anInt: Int): Int {
        if (1 == anInt) {
            return 2 //"收到的"
        }
        if (2 == anInt) {
            return 1 //"已发出"
        }
        return 3 //其他返回 发送失败
    }

    private fun getPerson(context: Context, address: String): String? {
        try {
            val resolver = context.contentResolver
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address)
            val cursor = resolver.query(uri, arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME), null, null, null)
            if (cursor != null) {
                try {
                    if (cursor.count != 0) {
                        cursor.moveToFirst()
                        return cursor.getString(0)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                } finally {
                    cursor.close() //关闭cursor，避免内存泄露
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getCallType(anInt: Int): Int {
        when (anInt) {
            CallLog.Calls.INCOMING_TYPE -> return 1
            CallLog.Calls.OUTGOING_TYPE -> return 2
            CallLog.Calls.MISSED_TYPE -> return 3
            else ->{ }
        }
        return 3
    }

}
