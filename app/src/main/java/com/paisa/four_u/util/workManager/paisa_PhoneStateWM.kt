package com.paisa.four_u.util.workManager

import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.net.paisa_HttpManager
import com.paisa.four_u.util.paisa_AppUtil
import com.paisa.four_u.util.paisa_DeviceUtils
import com.paisa.four_u.util.paisa_DeviceUtils.checkEmulator
import com.paisa.four_u.util.paisa_DeviceUtils.getDeviceHeight
import com.paisa.four_u.util.paisa_DeviceUtils.getDeviceWidth
import com.paisa.four_u.util.paisa_DeviceUtils.getMobileDbm
import com.paisa.four_u.util.paisa_DeviceUtils.getNetWorkType
import com.paisa.four_u.util.paisa_DeviceUtils.getRAMMemoryAvailable
import com.paisa.four_u.util.paisa_DeviceUtils.getWifiInfo
import com.paisa.four_u.util.paisa_DeviceUtils.isRoot
import com.paisa.four_u.util.paisa_DeviceUtils.queryWithStorageManager
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.createBody
import com.paisa.four_u.util.expand.str
import com.paisa.four_u.util.paisa_DeviceUtils.getUUID


/**
 * 设备信息收集
 */
class paisa_PhoneStateWM(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val mApiService by lazy { paisa_HttpManager.create(paisa_ApiServices::class.java) }


    override suspend fun doWork(): Result {
        try {


            val map = HashMap<String, Any>()
            val mContext = paisa_RApplication.instance

            val wiFiBean = mContext.getWifiInfo()
//            手机号码
            map["phone"] = BuildConfig.AREA_CODE + SpRepository.phone
//            设备抓板
            map["phone_brand"] = Build.BRAND
//            设备类型
            map["phone_model"] = Build.MODEL
            //设备ID
            map["device_id"] = getUUID()
            //系统版本
            map["release"] = Build.VERSION.RELEASE
            //SDK版本
            map["sdk_version"] = Build.VERSION.SDK_INT
            //设备名称
            map["device_name"] = Build.DEVICE
            map["device_width"] = mContext.getDeviceWidth()//屏幕宽度
            map["device_height"] = mContext.getDeviceHeight()//屏幕高度
            map["ram_total_size"] = paisa_DeviceUtils.getRAMMemoryTotal()
            map["ram_usable_size"] = mContext.getRAMMemoryAvailable()
            map["memory_card_size"] = paisa_DeviceUtils.getSDCardMemoryTotal()
            map["memory_card_size_use"] = paisa_DeviceUtils.getSDCardMemoryAvailable()
            map["internal_storage_usable"] = paisa_DeviceUtils.getROMMemoryAvailable()
            map["internal_storage_used"] = mContext.queryWithStorageManager().split("-")[1]
            map["internal_storage_total"] = mContext.queryWithStorageManager().split("-")[0]
            map["language"] = paisa_DeviceUtils.getDeviceDefaultLanguage()
            map["imsi"] = SpRepository.uuid
            map["imei"] = SpRepository.uuid
            map["network_type"] = mContext.getNetWorkType()
            map["time_zone_id"] = paisa_DeviceUtils.getTimeZoneID()
            map["locale_iso_3_country"] = paisa_DeviceUtils.getMobLocalCountry()
            map["simulator"] = mContext.checkEmulator()
            map["dbm"] = mContext.getMobileDbm()
            map["ip"] = wiFiBean.ip
            map["mac"] = paisa_AppUtil.adresseMAC
            map["current_wifi_bssid"] = wiFiBean.bssid
            map["current_wifi_name"] = wiFiBean.wifiName
            map["current_wifi_ssid"] = wiFiBean.ssid
            map["current_wifi_mac"] = wiFiBean.mac
            map["latitude"] = SpRepository.location_lat
            map["longitude"] = SpRepository.location_lon
            map["phone_token"] = ""
            map["app"] = str(R.string.app_name)
            map["sub_app"] = str(R.string.app_name) + "_android"
            map["password"] = ""
            map["new_password"] = ""
            map["ov"] = Build.VERSION.RELEASE
            map["place"] = "google"
            map["sub_place_code"] = ""
            map["other_info"] = ""
            map["passport"] = ""
            map["oaid"] = SpRepository.oaid
            map["root"] = mContext.isRoot()

            mApiService.upPhoneState(map.createBody())
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Slog.e("===>  设备信息上传出错 $e ")
            return Result.failure()
        }

    }

}