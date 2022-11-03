package com.paisa.four_u.ui.confirm

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.base.paisa_BaseVM
import com.paisa.four_u.model.CheckApplyStatusModel
import com.paisa.four_u.model.SureApplyModel
import com.paisa.four_u.util.paisa_AppUtil
import com.paisa.four_u.util.paisa_DeviceUtils.checkEmulator
import com.paisa.four_u.util.paisa_DeviceUtils.getDeviceDefaultLanguage
import com.paisa.four_u.util.paisa_DeviceUtils.getDeviceHeight
import com.paisa.four_u.util.paisa_DeviceUtils.getDeviceWidth
import com.paisa.four_u.util.paisa_DeviceUtils.getImei
import com.paisa.four_u.util.paisa_DeviceUtils.getImsi
import com.paisa.four_u.util.paisa_DeviceUtils.getLastKnownLocation
import com.paisa.four_u.util.paisa_DeviceUtils.getMobLocalCountry
import com.paisa.four_u.util.paisa_DeviceUtils.getMobileDbm
import com.paisa.four_u.util.paisa_DeviceUtils.getNetWorkType
import com.paisa.four_u.util.paisa_DeviceUtils.getRAMMemoryAvailable
import com.paisa.four_u.util.paisa_DeviceUtils.getRAMMemoryTotal
import com.paisa.four_u.util.paisa_DeviceUtils.getROMMemoryAvailable
import com.paisa.four_u.util.paisa_DeviceUtils.getSDCardMemoryAvailable
import com.paisa.four_u.util.paisa_DeviceUtils.getSDCardMemoryTotal
import com.paisa.four_u.util.paisa_DeviceUtils.getTimeZoneID
import com.paisa.four_u.util.paisa_DeviceUtils.getWifiInfo
import com.paisa.four_u.util.paisa_DeviceUtils.isRoot
import com.paisa.four_u.util.paisa_DeviceUtils.queryWithStorageManager
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.createBody
import com.paisa.four_u.util.expand.str
import com.paisa.four_u.util.paisa_DeviceUtils.getUUID

/**
 * @Author Ben
 * @Date 2022/4/24 10:41
 * @desc:
 */
class paisa_ConfirmVMPaisa : paisa_BaseVM() {


    var liveSureApplyBean = MutableLiveData<SureApplyModel>()
    var liveCheckApplyStatusBean = MutableLiveData<CheckApplyStatusModel>()
    var liveUpdateState = MutableLiveData<Boolean>()


    fun getApplyPageData(map: HashMap<String, Any>) {
        request({
            mApiService.getApplyPageData(map.createBody())
        }, {
            liveSureApplyBean.value = this
        })
    }


    fun checkApplyStatus() {
        request({
            mApiService.checkApplyStatus(HashMap<String, Any>().createBody())
        }, {
            liveCheckApplyStatusBean.value = this
        }, isShowLoading = false)
    }


    fun applyLoan(map: HashMap<String, Any>) {
        request({
            mApiService.applyLoan(map.createBody())
        }, {
            liveUpdateState.value = true
        }, {
            liveUpdateState.value = false
        })
    }

    fun submitPhoneState() {

        val map = HashMap<String, Any>()
        val mContext = paisa_RApplication.instance
        val location = mContext.getLastKnownLocation()
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
        map["ram_total_size"] = getRAMMemoryTotal()
        map["ram_usable_size"] = mContext.getRAMMemoryAvailable()
        map["memory_card_size"] = getSDCardMemoryTotal()
        map["memory_card_size_use"] = getSDCardMemoryAvailable()
        map["internal_storage_usable"] = getROMMemoryAvailable()
        map["internal_storage_used"] = mContext.queryWithStorageManager().split("-")[1]
        map["internal_storage_total"] = mContext.queryWithStorageManager().split("-")[0]
        map["language"] = getDeviceDefaultLanguage()
        map["imsi"] = mContext.getImsi()
        map["imei"] = mContext.getImei()
        map["network_type"] = mContext.getNetWorkType()
        map["time_zone_id"] =getTimeZoneID()
        map["locale_iso_3_country"] = getMobLocalCountry()
        map["simulator"] = mContext.checkEmulator()
        map["dbm"] = mContext.getMobileDbm()
        map["ip"] = wiFiBean.ip
        map["mac"] = paisa_AppUtil.adresseMAC
        map["current_wifi_bssid"] = wiFiBean.bssid
        map["current_wifi_name"] = wiFiBean.wifiName
        map["current_wifi_ssid"] = wiFiBean.ssid
        map["current_wifi_mac"] = wiFiBean.mac
        map["latitude"] = location?.latitude ?: ""
        map["longitude"] = location?.longitude ?: ""
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
        map["oaid"] =   SpRepository.oaid
        map["root"] = mContext.isRoot()

        request({
            mApiService.upPhoneState(map.createBody())
        }, {

        }, isShowLoading = false)
    }

}