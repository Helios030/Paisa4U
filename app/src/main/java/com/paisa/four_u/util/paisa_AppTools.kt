//package com.rupee.prima.util
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.ActivityManager
//import android.app.usage.StorageStatsManager
//import android.content.Context
//import android.content.Context.SENSOR_SERVICE
//import android.content.Intent
//import android.content.pm.PackageInfo
//import android.content.pm.PackageManager
//import android.hardware.Sensor
//import android.hardware.SensorManager
//import android.location.Address
//import android.location.Geocoder
//import android.location.Location
//import android.location.LocationManager
//import android.media.AudioManager
//import android.net.ConnectivityManager
//import android.net.Uri
//import android.net.wifi.WifiInfo
//import android.net.wifi.WifiManager
//import android.os.Build
//import android.os.Environment
//import android.os.StatFs
//import android.os.storage.StorageManager
//import android.provider.Settings
//import android.telephony.*
//import android.text.TextUtils
//import android.text.format.Formatter
//import androidx.annotation.RequiresApi
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.rupee.prima.model.WiFiModel
//import com.rupee.prima.Application
//import org.json.JSONObject
//import java.io.*
//import java.net.Inet4Address
//import java.net.NetworkInterface
//import java.net.SocketException
//import java.util.*
//
//
//object AppTools {
//    var telephonyManager: TelephonyManager? = null
//    const val DISTINGUISH = "-"
//    val application = Application.instance
//
//
//    /**
//     * @param context
//     * @return
//     */
//    fun getTelephonyManager(context: Context): TelephonyManager? {
//        if (telephonyManager == null) {
//            telephonyManager =
//                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        }
//        return telephonyManager
//    }
//
//    /**
//     * 获取手机的ip
//     *
//     * @return
//     */
//    val localIpAddress: String?
//        get() {
//            try {
//                val en = NetworkInterface
//                    .getNetworkInterfaces()
//                while (en.hasMoreElements()) {
//                    val intf = en.nextElement()
//                    val enumIpAddr = intf
//                        .inetAddresses
//                    while (enumIpAddr.hasMoreElements()) {
//                        val inetAddress = enumIpAddr.nextElement()
//                        if (!inetAddress.isLoopbackAddress
//                            && inetAddress is Inet4Address
//                        ) {
//                            return inetAddress.getHostAddress()
//                        }
//                    }
//                }
//            } catch (ex: SocketException) {
//                ex.printStackTrace()
//            }
//            return null
//        }
//
//    /**
//     * Android查询本机安装的应用市场选择
//     *
//     * @param @param context
//     * @return void
//     * @throws
//     * @Title: toAppMarket
//     */
//    fun toAppMarket(context: Context) {
//        try {
//            val uri = Uri.parse(
//                "market://details?id="
//                        + context.packageName
//            )
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            // ShowToast.showToast("您未安装任何应用商店，请安装后再评分。", context);
//        }
//    }
//
//    /**
//     * 客户端版本信息
//     *
//     * @param @param  context
//     * @param @return
//     * @return String
//     * @throws
//     * @Title: getAppVersionName
//     */
//    fun getAppVersionName(context: Context): String? {
//        var versionName = "unknown"
//        try {
//            val pm = context.packageManager
//            val pi = pm.getPackageInfo(context.packageName, 0)
//            versionName = pi.versionName
//            if (versionName == null || versionName.isEmpty()) {
//                return " "
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return versionName
//    }// 系统版本// 型号// 手机品牌
//
//    /**
//     * 获取手机系统
//     * @param @return
//     * @return String
//     * @throws
//     * @Title: getPhoneInfo
//     */
//    val phoneInfo: String
//        get() {
//            try {
//                val brand = Build.BRAND // 手机品牌
//                val model = Build.MODEL // 型号
//                val device = Build.VERSION.RELEASE // 系统版本
//                return "$brand - $model - $device"
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return "unknown"
//        }
//
//    /**
//     * 获取版本信息
//     *
//     * @param @param  context
//     * @param @return
//     * @return String
//     * @throws
//     * @Title: getDeviceInfo
//     */
//    @SuppressLint("HardwareIds", "MissingPermission")
//    fun getDeviceInfo(context: Context): String? {
//        try {
//            val json = JSONObject()
//            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            var device_id = tm.deviceId
//            val wifi =
//                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//            val mac = wifi.connectionInfo.macAddress
//            json.put("mac", mac)
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = mac
//            }
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = Settings.Secure.getString(
//                    context.contentResolver,
//                    Settings.Secure.ANDROID_ID
//                )
//            }
//            json.put("device_id", device_id)
//            return json.toString()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    /**
//     * 获取版本信息
//     *
//     * @param @param context
//     * @return void
//     * @throws
//     * @Title: getPackageInfo
//     */
//    fun getPackageVersionCode(context: Context): Int {
//        var packageInfo: PackageInfo? = null // 版本号
//        try {
//            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
//            return packageInfo.versionCode
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return 0
//    }
//
//    /**
//     * 获取系统SDK版本
//     *
//     * @return
//     */
//    val sDKVersionNumber: Int
//        get() {
//            val sdkVersion: Int
//            sdkVersion = try {
//                Integer.valueOf(Build.VERSION.SDK)
//            } catch (e: NumberFormatException) {
//                0
//            }
//            return sdkVersion
//        }
//
//    /**
//     * 安装app
//     *
//     * @param file
//     */
//    fun apkInstallIntent(file: File?, context: Context) {
//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
//        context.startActivity(intent)
//    }
//
//    /**
//     * 卸载app
//     */
//    fun apkUninstallIntent(context: Context) {
//        val packageURI = Uri.parse("package:com.dituiba_android")
//        val uninstallIntent = Intent(Intent.ACTION_DELETE, packageURI)
//        uninstallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(uninstallIntent)
//    }
//
//    /**
//     * 获取屏幕高度，减去状态栏高度
//     *
//     * @param mContext
//     * @return
//     */
//    fun getScreenHeight(mContext: Context): Int {
//        val displayMetrics = mContext.resources.displayMetrics
//        return displayMetrics.heightPixels - getStatusBarHeight(mContext)
//    }
//
//    /**
//     * 获取状态栏高度
//     *
//     * @param context
//     * @return
//     */
//    fun getStatusBarHeight(context: Context): Int {
//        try {
//            val c = Class.forName("com.android.internal.R\$dimen")
//            val obj = c.newInstance()
//            val field = c.getField("status_bar_height")
//            val x = field[obj].toString().toInt()
//            return context.resources.getDimensionPixelSize(x)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return 0
//    }
//
//    /**
//     * 获取手机安装的应用
//     */
//    fun getPhoneApps(context: Context) {
//        val pm = context.packageManager
//        //PackageManager.GET_UNINSTALLED_PACKAGES==8192
//        val list = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)
//        //PackageManager.GET_SHARED_LIBRARY_FILES==1024
//        //List<PackageInfo> list2=pm.getInstalledPackages(PackageManager.GET_SHARED_LIBRARY_FILES);
//        //PackageManager.GET_META_DATA==128
//        //List<PackageInfo> list2=pm.getInstalledPackages(PackageManager.GET_META_DATA);
//        //List<PackageInfo> list2=pm.getInstalledPackages(0);
//        //List<PackageInfo> list2=pm.getInstalledPackages(-10);
//        //List<PackageInfo> list2=pm.getInstalledPackages(10000);
//        for ((i, packageInfo) in list.withIndex()) { //得到手机上已经安装的应用的名字,即在AndriodMainfest.xml中的app_name。
//            val appName = packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
//            //得到应用所在包的名字,即在AndriodMainfest.xml中的package的值。
//            val packageName = packageInfo.packageName
//            //  Slog.e("应用的名字:" + appName + "---应用的包名字:" + packageName);
//        }
//        //Slog.e("应用的总个数:" + i);
//    }
//
//    /**
//     * 获取手机内安装的应用列表
//     *
//     * @param context
//     * @return
//     */
//    fun getPhoneAppList(context: Context): List<PackageInfo>? {
//        return try {
//            val pm = context.packageManager
//            pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)
//        } catch (e: Exception) {
//            null
//        }
//    }
//
//    /**
//     * 获取本地版本名称
//     */
//    fun getAppVersion(context: Context?): String {
//        if (null == context) {
//            return "0"
//        }
//        val codeName: String
//        try {
//            val manager = context.packageManager
//            val info = manager.getPackageInfo(context.packageName, 0)
//            codeName = info.versionName // 版本名称1.1.1
//            return codeName
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        return "0"
//    }
//
//    /**
//     * 获取本地版本名称
//     */
//    fun getAppVersionCode(context: Context?): Int {
//        if (null == context) {
//            return 0
//        }
//        var codeName = 0
//        try {
//            val manager = context.packageManager
//            val info = manager.getPackageInfo(context.packageName, 0)
//            codeName = info.versionCode // 版本名称1.1.1
//            return codeName
//        } catch (e: PackageManager.NameNotFoundException) {
//            e.printStackTrace()
//        }
//        return 0
//    }
//
//    /**
//     * 手机是否通话状态
//     *
//     * @param context
//     * @return
//     */
//    fun isTelephonyCalling(context: Context): Boolean {
//        var calling = false
//        val telephonyManager =
//            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        if (TelephonyManager.CALL_STATE_OFFHOOK == telephonyManager.callState || TelephonyManager.CALL_STATE_RINGING == telephonyManager.callState) {
//            calling = true
//        }
//        return calling
//    }
//
//    /**
//     * 手机是否播放音乐
//     *
//     * @param context
//     * @return
//     */
//    fun isPlayMusic(context: Context): Boolean {
//        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//        return audioManager.isMusicActive
//    }
//
//
//
//
//    /**
//     * 通过第三方获取设备码
//     */
//    fun getDeviceId(): String {
//        return SpRepository.uuid
//    }
//
//    /**
//     * 获取CV
//     */
//    fun getLinuxCoreVer(): String {
//        var process: Process? = null
//        var kernelVersion = ""
//        try {
//            process = Runtime.getRuntime().exec("cat /proc/version")
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val outs = process?.inputStream ?: return ""
////                val isrout = InputStreamReader(outs)
////                val brout = BufferedReader(isrout, 8 * 1024)
////                var result = ""
//        val line: String
////                try {
////                    while ((line = brout.readLine()) != null) {
////                        result += line
////                    }
////                } catch (e: IOException) {
////                    e.printStackTrace()
////                }
//        val crunchifyReader = BufferedReader(InputStreamReader(outs, "UTF-8"))
//        val result = crunchifyReader.use(BufferedReader::readText)
//        try {
//            if (result.isNotEmpty()) {
//                val Keyword = "version "
//                var index = result.indexOf(Keyword)
//                line = result.substring(index + Keyword.length)
//                index = line.indexOf(" ")
//                kernelVersion = line.substring(0, index)
//            }
//        } catch (e: IndexOutOfBoundsException) {
//            e.printStackTrace()
//        }
//
//        return kernelVersion
//    }
//
//    /**
//     * 获取WiFi信息
//     */
//    fun getWifiInfo(context: Context): WiFiModel {
//        val wiFiBean = WiFiModel()
//        try {
//            val wifiManager: WifiManager =
//                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//            val wifiInfo: WifiInfo = wifiManager.connectionInfo
//            wiFiBean.mac = AppUtil.adresseMAC
//            wiFiBean.ip = intToIp(wifiInfo.ipAddress)
//            wiFiBean.ssid = wifiInfo.ssid
//            if (wifiManager.wifiState == WifiManager.WIFI_STATE_ENABLED) {
//                wiFiBean.wifiStatus = "WIFI_STATE_ENABLED"
//            }
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//                wiFiBean.wifiName = wifiInfo.ssid.replace("\"", "")
//            } else {
//                wiFiBean.wifiName = wifiInfo.ssid
//            }
//            wiFiBean.bssid = wifiInfo.bssid
//            wiFiBean.networkId = wifiInfo.networkId.toString()
//            wiFiBean.speed = wifiInfo.linkSpeed.toString()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            wiFiBean.mac = "unknown"
//            wiFiBean.ip = "unknown"
//            wiFiBean.ssid = "unknown"
//            wiFiBean.wifiStatus = "unknown"
//            wiFiBean.wifiName = "unknown"
//            wiFiBean.bssid = "unknown"
//            wiFiBean.networkId = "unknown"
//            wiFiBean.speed = "unknown"
//        }
//        return wiFiBean
//    }
//
//    fun intToIp(ip: Int): String {
//        return ((ip and 0xFF).toString() + "." + (ip shr 8 and 0xFF) + "." + (ip shr 16 and 0xFF) + "."
//                + (ip shr 24 and 0xFF))
//    }
//
//
//
//    /**
//     * 获取设备宽度（px）
//     *
//     */
//    fun getDeviceWidth(context: Context): Int {
//        return context.resources.displayMetrics.widthPixels
//    }
//
//    /**
//     * 获取设备高度（px）
//     */
//    fun getDeviceHeight(context: Context): Int {
//        return context.resources.displayMetrics.heightPixels
//    }
//
//
//
//
//
//
//
//    /**
//     * 有没有光线传感器
//     */
//    fun hasLightSensor(context: Context): Boolean {
//        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
//        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) //光
//        return sensor == null
//    }
//
//    /**
//     * 获取ROM全部内存(不算系统固件)
//     */
//    fun getROMMemoryTotal(context: Context): String {
//        val path: File = Environment.getDataDirectory()
//        val stat = StatFs(path.path)
//        val blockSize: Long = stat.blockSizeLong
//        val totalBlocks: Long = stat.blockCountLong
//        return (totalBlocks * blockSize).toString()
//    }
//
//
//
//
//
//
//    /**
//     * 判断是否包含SIM卡
//     *
//     * @param context 上下文
//     * @return 状态 是否包含SIM卡
//     */
//    fun hasSimCard(context: Context): Boolean {
//        var result = true
//        try {
//            result = when (getTelephonyManager(context)?.simState) {
//                TelephonyManager.SIM_STATE_ABSENT -> false
//                TelephonyManager.SIM_STATE_UNKNOWN -> false
//                else -> {
//                    true
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return result
//    }
//
//
//
//
//
//
//    //storageTotalSize
//    fun getTotalSize(): Long = getStorageInfo().first
//
//    fun getStorageInfo(): Pair<Long, Long> {
//        try {
//            val storageTotalSize: String
//            val storageAvailableSize: String
//            val storageStr = queryWithStorageManager()
//            if (storageStr.contains("-")) {
//                val split = storageStr.split("-").toTypedArray()
//                val storageTotalSizeStr = split[0]
//                val storageAvailableSizeStr = split[1]
//                storageTotalSize = storageTotalSizeStr
//                storageAvailableSize = storageAvailableSizeStr
//            } else {
//                storageTotalSize = getExternalTotalSpace().toString()
//                storageAvailableSize = getExternalAvailableSpace().toString()
//            }
//            return storageTotalSize.toLong() to storageAvailableSize.toLong()
//        } catch (e: Exception) {
//            return 0L to 0L
//        }
//    }
//
//
//    fun getExternalTotalSpace(): Long = try {
//        val stat = StatFs(getExternalStoragePath())
//        stat.blockCountLong * stat.blockSizeLong
//    } catch (e: Exception) {
//        0L
//    }
//    fun getExternalAvailableSpace(): Long = try {
//        val stat = StatFs(getExternalStoragePath())
//        stat.availableBlocksLong * stat.blockSizeLong
//    } catch (e: Exception) {
//        0L
//    }
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getTotalSize(fsUuid: String?): Long {
//        return try {
//            val id: UUID = if (fsUuid == null) {
//                StorageManager.UUID_DEFAULT
//            } else {
//                UUID.fromString(fsUuid)
//            }
//            val stats = application.getSystemService(StorageStatsManager::class.java)
//            stats.getTotalBytes(id)
//        } catch (e: NoSuchFieldError) {
//            e.printStackTrace()
//            -1
//        } catch (e: NoClassDefFoundError) {
//            e.printStackTrace()
//            -1
//        } catch (e: NullPointerException) {
//            e.printStackTrace()
//            -1
//        } catch (e: IOException) {
//            e.printStackTrace()
//            -1
//        }
//    }
//
//    fun getExternalStoragePath(): String = try {
//        Environment.getExternalStorageDirectory().path
//    } catch (e: Exception) {
//        ""
//    }
//
//
//
//}