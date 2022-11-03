package com.paisa.four_u.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.provider.ContactsContract
import android.provider.Settings
import android.telephony.*
import android.text.TextUtils
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.model.WiFiModel
import com.paisa.four_u.util.expand.context
import com.scottyab.rootbeer.RootBeer
import java.io.*
import java.net.NetworkInterface
import java.util.*


object paisa_DeviceUtils {

    private val mContext by lazy { paisa_RApplication.instance }
    private var mUUID: UUID? = null
    private var gaid = ""
    private var DISTINGUISH = "-"


    /**
     *获取UUID
     */
    fun getUUID(): String {

        if (SpRepository.uuid.isNotBlank()) {
            return SpRepository.uuid
        } else {
            try {
                mUUID = UUID.randomUUID()
                mUUID?.let {
                    SpRepository.uuid = (it.toString().replace(DISTINGUISH, ""))
                }
            } catch (e: SecurityException) {
                e.printStackTrace()

            }
        }
        return SpRepository.uuid
    }


    /**
     * 获取设备品牌
     *
     * @return
     */
    fun getBrands(): String {
        return Build.BRAND
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    fun getMobil(): String {
        return Build.MODEL
    }

    /**
     * CPU型号
     *
     * @return
     */
    fun getCpuModel(): String {
        return Build.CPU_ABI
    }


    /**
     * 获取当前手机系统版本号
     */
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 屏幕分辨率
     * @return
     */
    fun getResolution(): String {
        val metric = DisplayMetrics()
        val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getRealMetrics(metric)
        val width = metric.widthPixels
        val height = metric.heightPixels
        val densityDpi = metric.densityDpi
        return "$width×$height -$densityDpi"
    }

    /**
     * 获取wifi名称
     */
    @SuppressLint("WifiManagerPotentialLeak", "MissingPermission")
    fun getWifiName(): String {
        var ssid = ""
        try {
            val wifiMgr: WifiManager =
                mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info: WifiInfo = wifiMgr.connectionInfo
            val networkId: Int = info.networkId
            val configuredNetworks: MutableList<WifiConfiguration> = wifiMgr.configuredNetworks
            run breaking@{
                configuredNetworks.forEach {
                    if (it.networkId == networkId) {
                        ssid = it.SSID
                        return@breaking
                    }
                }
            }
            if (ssid.contains("\"")) {
                ssid = ssid.replace("\"", "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ssid
    }

    /**
     * 获取wifi mac 地址
     */
    fun getWifiMacAddrss(): String {
        try {
            val networkInterfaces: Enumeration<NetworkInterface> =
                NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val element: NetworkInterface? = networkInterfaces.nextElement()
                val address: ByteArray = element?.hardwareAddress ?: continue
                if (element.name == "wlan0") {
                    val builder = StringBuilder()
                    address.forEach {
                        builder.append(String.format("%02X:", it))
                    }
                    if (builder.isNotEmpty()) {
                        builder.deleteCharAt(builder.length - 1)
                    }
                    return builder.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取wifi信号强度 state
     */
    @SuppressLint("WifiManagerPotentialLeak")
    fun getWifiState(): String {
        try {
            val wifiMgr: WifiManager =
                mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info: WifiInfo = wifiMgr.connectionInfo
            if (info.ssid != null) {
                val strength: Int = WifiManager.calculateSignalLevel(info.rssi, 5)
                return strength.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    /*
     * 打开App电量
     * @return [Int] 电量
     */
    var openAppBatteryLevel: Int? = 0

    /**
     * 获取电量
     * @return [Int] 电量
     */
    fun getBatteryLevel(): Int {
        val intent: Intent? =
            ContextWrapper(mContext).registerReceiver(
                null,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
        return intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)!! * 100 /
                intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    }

    /**
     * 获取开启App时间
     */
    var openAppTime: String = ""

    /**
     * 判断手机是否截屏
     */
    var whetherScreenshot: Int = 0


    /**
     * 获取RAM
     */
    fun getRAMInfo(): String {
        val manager: ActivityManager =
            mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo: ActivityManager.MemoryInfo = ActivityManager.MemoryInfo()
        manager.getMemoryInfo(memoryInfo)
        val totalSize: Long = memoryInfo.totalMem
        val availableSize: Long = memoryInfo.availMem
        return (Formatter.formatFileSize(mContext, availableSize) + "/"
                + Formatter.formatFileSize(mContext, totalSize))
    }


    /**
     * 获取Rom
     *
     * @return
     */
    @Suppress("DEPRECATION")
    fun getRomInfo(): String {
        val file: File = Environment.getExternalStorageDirectory()
        val statFs = StatFs(file.path)
        val blockSizeLong = statFs.blockSizeLong
        val blockCountLong = statFs.blockCountLong
        val availableBlocksLong = statFs.availableBlocksLong
        return Formatter.formatFileSize(mContext, availableBlocksLong * blockSizeLong) + "/" +
                Formatter.formatFileSize(mContext, blockCountLong * blockSizeLong)
    }

    /**
     * CPU核数
     *
     * @return
     */
    fun getCpuCores(): String {
        val count = getNumberOfCPUCores()
        return count.toString() + ""
    }

    private fun getNumberOfCPUCores(): Int {
        return try {
            File("/sys/devices/system/cpu/").listFiles(CPU_FILTER)!!.size
        } catch (e: SecurityException) {
            0
        }
    }

    private val CPU_FILTER = FileFilter { pathname ->
        val path = pathname.name
        if (path.startsWith("cpu")) {
            for (i in 3 until path.length) {
                if (path[i] < '0' || path[i] > '9') {
                    return@FileFilter false
                }
            }
            return@FileFilter true
        }
        false
    }


    /**
     * 是否root或是否实体
     * 1是root机和虚拟机
     * 0是非root机和实体机
     */
    fun isSuEnableRoot(): Int {
        var file: File?
        val paths = arrayOf(
            "/system/bin/",
            "/system/xbin/",
            "/system/sbin/",
            "/sbin/",
            "/vendor/bin/",
            "/su/bin/"
        )
        try {
            for (path in paths) {
                file = File(path + "su")
                if (file.exists() && file.canExecute()) {
                    Log.i("TAG", "find su in : $path")
                    return 1
                }
            }
        } catch (x: Exception) {
            x.printStackTrace()
        }
        return 0
    }


    /**
     * 获取经纬度
     */
    @SuppressLint("MissingPermission")
    fun Context.getLastKnownLocation(): Location? {

        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = locationManager.getProviders(true)
        val locationProvider: String = when {
            providers.contains(LocationManager.NETWORK_PROVIDER) -> {
                LocationManager.NETWORK_PROVIDER
            }
            providers.contains(LocationManager.GPS_PROVIDER) -> {
                LocationManager.GPS_PROVIDER
            }
            else -> {
                Slog.d("没有可用位置提供器")
                return null
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            Slog.d("位置请求权限")
        } else {
            val location: Location? = locationManager.getLastKnownLocation(locationProvider)
            if (location != null) {
                val gc = Geocoder(this, Locale.getDefault())
                try {
                    val result: List<Address> =
                        gc.getFromLocation(location.latitude, location.longitude, 1)
                    Slog.d("获取地址信息:$result")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return location
            } else {
                locationManager.requestLocationUpdates(locationProvider, 3000, 1f)
                { location ->
                    Slog.d("位置改变,经度=${location.longitude};纬度=${location.latitude};")
                }
            }
        }
        return null
    }


    /**
     * 获取WiFi信息
     */
    fun Context.getWifiInfo(): WiFiModel {
        val wiFiBean = WiFiModel()
        try {
            val wifiManager: WifiManager =
                this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo: WifiInfo = wifiManager.connectionInfo
            wiFiBean.mac = paisa_AppUtil.adresseMAC
            wiFiBean.ip = intToIp(wifiInfo.ipAddress)
            wiFiBean.ssid = wifiInfo.ssid
            if (wifiManager.wifiState == WifiManager.WIFI_STATE_ENABLED) {
                wiFiBean.wifiStatus = "WIFI_STATE_ENABLED"
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                wiFiBean.wifiName = wifiInfo.ssid.replace("\"", "")
            } else {
                wiFiBean.wifiName = wifiInfo.ssid
            }
            wiFiBean.bssid = wifiInfo.bssid
            wiFiBean.networkId = wifiInfo.networkId.toString()
            wiFiBean.speed = wifiInfo.linkSpeed.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            wiFiBean.mac = "unknown"
            wiFiBean.ip = "unknown"
            wiFiBean.ssid = "unknown"
            wiFiBean.wifiStatus = "unknown"
            wiFiBean.wifiName = "unknown"
            wiFiBean.bssid = "unknown"
            wiFiBean.networkId = "unknown"
            wiFiBean.speed = "unknown"
        }
        return wiFiBean
    }

    fun intToIp(ip: Int): String {
        return ((ip and 0xFF).toString() + "." + (ip shr 8 and 0xFF) + "." + (ip shr 16 and 0xFF) + "."
                + (ip shr 24 and 0xFF))
    }

    /**
     * 获取设备宽度（px）
     *
     */
    fun Context.getDeviceWidth(): Int {
        return this.resources.displayMetrics.widthPixels
    }

    /**
     * 获取设备高度（px）
     */
    fun Context.getDeviceHeight(): Int {
        return this.resources.displayMetrics.heightPixels
    }

    /**
     * 获取当前手机系统语言。
     */
    fun getDeviceDefaultLanguage(): String {
        return Locale.getDefault().language ?: ""
    }

//1 代表已经获得Root权限
    fun Context.isRoot(): String {
        return if(RootBeer(this).isRooted) "1" else "0"
    }

    fun  Context.getGuid():String{
        return DeviceIdentifier.getGUID(this)?:""
    }

    fun  Context.getOAID():String{
        return  DeviceIdentifier.getOAID(this)?:""
    }





    /**
     * 获取RAM全部内存
     */
    fun getRAMMemoryTotal(): String {
        val str1 = "/proc/meminfo"
        val str2: String
        val arrayOfString: Array<String>
        var initial_memory: Long = 0
        try {
            val localFileReader = FileReader(str1)
            val localBufferedReader = BufferedReader(localFileReader, 8192)
            str2 = localBufferedReader.readLine()
            arrayOfString = str2.split("\\s+".toRegex()).toTypedArray()
            initial_memory = java.lang.Long.valueOf(arrayOfString[1]) * 1024
            localBufferedReader.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Slog.e(e.toString())
        }
        return initial_memory.toString()
    }


    /**
     * 获取ROM可用内存
     */
    fun getROMMemoryAvailable(): String {
        val path: File = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long = stat.blockSizeLong
        val availableBlocks: Long = stat.availableBlocksLong
//        return Formatter.formatFileSize(context, availableBlocks * blockSize)
        return (availableBlocks * blockSize).toString()
    }


    /**
     * 获取RAM可用内存
     */
    fun Context.getRAMMemoryAvailable(): String {
        val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        //mi.availMem; 当前系统的可用内存
//        return Formatter.formatFileSize(context, mi.availMem)
        return mi.availMem.toString()
    }

    /**
     * sd is null ==rom
     * sd可用内存
     */
    fun getSDCardMemoryAvailable(): String {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
//        return Formatter.formatFileSize(context, availableBlocks * blockSize)
        return (availableBlocks * blockSize).toString()
    }

    /**
     * sd is null ==rom
     * sd总内存
     */
    fun getSDCardMemoryTotal(): String {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val blockCount = stat.blockCountLong
//        return Formatter.formatFileSize(context, blockCount * blockSize)
        return (blockCount * blockSize).toString()
    }


    fun Context.queryWithStorageManager(): String {
        val storageManager = this.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        try {
            val getVolumes = StorageManager::class.java.getDeclaredMethod("getVolumes") //6.0
            val getVolumeInfo = getVolumes.invoke(storageManager) as List<Any>
            var total = 0L
            var used = 0L
            var systemSize = 0L
            for (obj in getVolumeInfo) {
                val getType = obj.javaClass.getField("type")
                val type = getType.getInt(obj)
                if (type == 1) { //TYPE_
                    var totalSize = 0L
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                        val fsUuid = getFsUuid.invoke(obj) as? String
                        totalSize = getTotalSize(fsUuid) //8.0 以后使用
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) { //7.1.1
                        val getPrimaryStorageSize =
                            StorageManager::class.java.getMethod("getPrimaryStorageSize") //5.0 6.0 7.0没有
                        totalSize = getPrimaryStorageSize.invoke(storageManager) as Long
                    }
                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = isMountedReadable.invoke(obj) as Boolean
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as File
                        if (totalSize == 0L) {
                            totalSize = f.totalSpace
                        }
                        systemSize = totalSize - f.totalSpace
                        used += totalSize - f.freeSpace
                        total += totalSize
                    }
                } else if (type == 0) { //TYPE_PUBLIC
                    //外置存储
                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = isMountedReadable.invoke(obj) as Boolean
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as File
                        used += f.totalSpace - f.freeSpace
                        total += f.totalSpace
                    }
                } else if (type == 2) { //TYPE_EMULATED
                }
            }
            return total.toString() + DISTINGUISH + systemSize.toString()
//            return total.toString()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
        return ""
    }

    //storageTotalSize
    fun getTotalSize(): Long = getStorageInfo().first

    fun getStorageInfo(): Pair<Long, Long> {
        try {
            val storageTotalSize: String
            val storageAvailableSize: String
            val storageStr = context.queryWithStorageManager()
            if (storageStr.contains("-")) {
                val split = storageStr.split("-").toTypedArray()
                val storageTotalSizeStr = split[0]
                val storageAvailableSizeStr = split[1]
                storageTotalSize = storageTotalSizeStr
                storageAvailableSize = storageAvailableSizeStr
            } else {
                storageTotalSize = getExternalTotalSpace().toString()
                storageAvailableSize = getExternalAvailableSpace().toString()
            }
            return storageTotalSize.toLong() to storageAvailableSize.toLong()
        } catch (e: Exception) {
            return 0L to 0L
        }
    }

    fun getExternalTotalSpace(): Long = try {
        val stat = StatFs(getExternalStoragePath())
        stat.blockCountLong * stat.blockSizeLong
    } catch (e: Exception) {
        0L
    }

    fun getExternalAvailableSpace(): Long = try {
        val stat = StatFs(getExternalStoragePath())
        stat.availableBlocksLong * stat.blockSizeLong
    } catch (e: Exception) {
        0L
    }

    fun getExternalStoragePath(): String = try {
        Environment.getExternalStorageDirectory().path
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

    /**
     * 获取手机IMSI
     *
     * @param @param  context
     * @param @return
     * @return String
     * @throws
     * @Title: getImsi
     */
    @SuppressLint("MissingPermission")
    fun Context.getImsi(): String {
        try {
            val manager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return manager.subscriberId
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return SpRepository.uuid
    }


    /**
     * 获取手机IMEI
     *
     * @param @param  context
     * @param @return
     * @return String
     * @throws
     * @Title: getImei
     */
    @SuppressLint("MissingPermission")
    fun Context.getImei(): String {
        val deviceId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            val mTelephony = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return SpRepository.uuid
            }
            assert(mTelephony != null)
            if (mTelephony.deviceId != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mTelephony.imei
                } else {
                    mTelephony.deviceId
                }
            } else {
                Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            }
        }
        Slog.d("deviceId:$deviceId")


        return deviceId
    }


    /**
     * 获取网络类型
     */
    @SuppressLint("MissingPermission")
    fun Context.getNetWorkType(): String {
        try {
            val manager =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
                return "WIFI"
            }
            val telephonyManager =
                this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    ?: return "unknown"
            return when (telephonyManager.networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                else -> "unknown"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "unknown"
        }
    }

    /**
     * 获取时区ID
     */
    fun getTimeZoneID(): String {
        return TimeZone.getDefault().id
    }

    /**
     * 获取当前国家
     */
    fun getMobLocalCountry(): String {
        return Locale.getDefault().country ?: ""
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getTotalSize(fsUuid: String?): Long {
        return try {
            val id: UUID = if (fsUuid == null) {
                StorageManager.UUID_DEFAULT
            } else {
                UUID.fromString(fsUuid)
            }
            val stats = context.getSystemService(StorageStatsManager::class.java)
            stats.getTotalBytes(id)
        } catch (e: NoSuchFieldError) {
            e.printStackTrace()
            -1
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            -1
        } catch (e: NullPointerException) {
            e.printStackTrace()
            -1
        } catch (e: IOException) {
            e.printStackTrace()
            -1
        }
    }

    /**
     * 判断是不是模拟器 true是模拟器 false不是模拟器
     */
    @SuppressLint("QueryPermissionsNeeded")
    fun Context.checkEmulator(): Int {
        val url = "tel:" + "123456"
        val intent = Intent()
        intent.data = Uri.parse(url)
        intent.action = Intent.ACTION_DIAL
        // 是否可以处理跳转到拨号的 Intent
        val canCallPhone = intent.resolveActivity(this.packageManager) != null
        val result: Boolean =
            Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.toLowerCase()
                .contains("vbox") || Build.FINGERPRINT.toLowerCase()
                .contains("test-keys") || Build.MODEL.contains("google_sdk") ||
                    Build.MODEL.contains("Emulator") || Build.MODEL
                .contains("MuMu") || Build.MODEL.contains("virtual") ||
                    Build.SERIAL.equals("android", ignoreCase = true) || Build.MANUFACTURER
                .contains("Genymotion") || Build.BRAND.startsWith("generic") &&
                    Build.DEVICE.startsWith("generic") || ("google_sdk" == Build.PRODUCT) ||
                    ((this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkOperatorName
                        .toLowerCase(Locale.getDefault())
                            == "android") || !canCallPhone
        if (result && canCallPhone && TextUtils.equals(
                "HUAWEI",
                Build.BRAND
            ) && !this.hasLightSensor()
        ) {
            return 0
        }
        return if (result) {
            1
        } else {
            0
        }
    }


    /**
     * 获取手机信号强度
     */
    @SuppressLint("MissingPermission")
    fun Context.getMobileDbm(): String {
        try {
            val telephonyManager: TelephonyManager =
                this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val cellInfoList = telephonyManager.allCellInfo
            if (cellInfoList != null) {
                for (cellInfo in cellInfoList) {
                    if (cellInfo is CellInfoGsm) {
                        val cellSignalStrengthGsm = cellInfo.cellSignalStrength
                        return cellSignalStrengthGsm.dbm.toString()
                    } else if (cellInfo is CellInfoCdma) {
                        val cellSignalStrengthCdma = cellInfo.cellSignalStrength
                        return cellSignalStrengthCdma.dbm.toString()
                    } else if (cellInfo is CellInfoLte) {
                        val cellSignalStrengthLte = cellInfo.cellSignalStrength
                        return cellSignalStrengthLte.dbm.toString()
                    } else
                        if (cellInfo is CellInfoWcdma) {
                            val cellSignalStrengthWcdma = cellInfo.cellSignalStrength
                            return cellSignalStrengthWcdma.dbm.toString()
                        }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "unknown"
    }


    /**
     * 有没有光线传感器
     */
    fun Context.hasLightSensor(): Boolean {
        val sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //光
        return sensor == null
    }


    /**
     * 根据uri查询指定联系人信息
     *
     * @return String[name, number]
     */
    fun Context.queryContactByUri(uri: Uri?): Array<String>? {
        val resolver = this.getContentResolver();
        var data: Array<String>? = null
        if (uri != null) {
            val cursor = resolver.query(
                uri, arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                ),
                null, null, null
            )
            while (cursor != null && cursor.moveToNext()) {
                val number = cursor.getString(0)
                val name = cursor.getString(1)
                data = arrayOf(name, number)
            }
            cursor!!.close()
        }
        return data
    }

}