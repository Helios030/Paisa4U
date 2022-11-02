package com.paisa.four_u.util

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import com.paisa.four_u.paisa_RApplication
import java.io.*
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*

/**
 * Created by xuhao on 2017/12/6.
 * desc: APP 相关的工具类
 */

class paisa_AppUtil private constructor() {

    init {
        throw Error("Do not need instantiate!")
    }

    companion object {
        /**
         * 得到软件版本号
         * @param context 上下文
         * @return 当前版本Code
         */
        fun getVerCode(context: Context): Int {
            var verCode = -1
            try {
                val packageName = context.packageName
                verCode = context.packageManager
                        .getPackageInfo(packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verCode
        }

        /**
         * 获取应用运行的最大内存
         * @return 最大内存
         */
        val maxMemory: Long
            get() = Runtime.getRuntime().maxMemory() / 1024

        /**
         * 得到软件显示版本信息
         * @param context 上下文
         * @return 当前版本信息
         */
        fun getVerName(context: Context): String {
            var verName = ""
            try {
                val packageName = context.packageName
                verName = context.packageManager
                        .getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verName
        }

        /**
         * 获取应用签名
         * @param context 上下文
         * @param pkgName 包名
         * @return 返回应用的签名
         */
        @SuppressLint("PackageManagerGetSignatures")
        fun getSign(context: Context, pkgName: String): String? {
            return try {
                @SuppressLint("PackageManagerGetSignatures") val pis = context.packageManager
                        .getPackageInfo(pkgName,
                                PackageManager.GET_SIGNATURES)
                hexDigest(pis.signatures[0].toByteArray())
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                null
            }

        }

        /**
         * 将签名字符串转换成需要的32位签名
         * @param paramArrayOfByte 签名byte数组
         * @return 32位签名字符串
         */
        private fun hexDigest(paramArrayOfByte: ByteArray): String {
            val hexDigits = charArrayOf(48.toChar(), 49.toChar(), 50.toChar(), 51.toChar(), 52.toChar(), 53.toChar(), 54.toChar(), 55.toChar(), 56.toChar(), 57.toChar(), 97.toChar(), 98.toChar(), 99.toChar(), 100.toChar(), 101.toChar(), 102.toChar())
            try {
                val localMessageDigest = MessageDigest.getInstance("MD5")
                localMessageDigest.update(paramArrayOfByte)
                val arrayOfByte = localMessageDigest.digest()
                val arrayOfChar = CharArray(32)
                var i = 0
                var j = 0
                while (true) {
                    if (i >= 16) {
                        return String(arrayOfChar)
                    }
                    val k = arrayOfByte[i].toInt()
                    arrayOfChar[j] = hexDigits[0xF and k.ushr(4)]
                    arrayOfChar[++j] = hexDigits[k and 0xF]
                    i++
                    j++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * 获取设备的可用内存大小
         * @param context 应用上下文对象context
         * @return 当前内存大小
         */
        fun getDeviceUsableMemory(context: Context): Int {
            val am = context.getSystemService(
                    Context.ACTIVITY_SERVICE) as ActivityManager
            val mi = ActivityManager.MemoryInfo()
            am.getMemoryInfo(mi)
            // 返回当前系统的可用内存
            return (mi.availMem / (1024 * 1024)).toInt()
        }

        /**
         * 获取手机型号
         */
        fun getMobileModel(): String {
            var model: String? = Build.MODEL
            model = model?.trim { it <= ' ' } ?: ""
            return model
        }

        /**
         * 获取手机系统SDK版本
         * @return 如API 17 则返回 17
         */
        val sdkVersion: Int get() = Build.VERSION.SDK_INT

        private val marshmallowMacAddress = "02:00:00:00:00:00"
        private val fileAddressMac = "/sys/class/net/wlan0/address"
        /**
         * 获取手机Mac
         */
        val adresseMAC: String get() {
                val context = paisa_RApplication.instance
                val wifiMan = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInf = wifiMan.connectionInfo

                if (wifiInf != null && marshmallowMacAddress == wifiInf.macAddress) {
                    var result: String? = null
                    try {
                        result = adressMacByInterface
                        return if (result != null) {
                            result
                        } else {
                            result = getAddressMacByFile(wifiMan)
                            result
                        }
                    } catch (e: IOException) {
//                        Slog.e("MobileAccess", "Erreur lecture propriete Adresse MAC")

                    } catch (e: Exception) {
                        e.printStackTrace()
//                        Slog.d("MobileAcces", "Erreur lecture propriete Adresse MAC ")

                    }

                } else {
                    return if (wifiInf != null && wifiInf.macAddress != null) {
                        wifiInf.macAddress
                    } else {
                        ""
                    }
                }
                return marshmallowMacAddress
            }
        private val adressMacByInterface: String? get() {
                try {
                    val all = Collections.list(NetworkInterface.getNetworkInterfaces())
                    for (nif in all) {
                        if (nif.name.equals("wlan0", ignoreCase = true)) {
                            val macBytes = nif.hardwareAddress ?: return ""

                            val res1 = StringBuilder()
                            for (b in macBytes) {
                                res1.append(String.format("%02X:", b))
                            }

                            if (res1.length > 0) {
                                res1.deleteCharAt(res1.length - 1)
                            }
                            return res1.toString()
                        }
                    }

                } catch (e: Exception) {
                    Slog.d("MobileAcces", "Erreur lecture propriete Adresse MAC ")
                }

                return null
            }
        @Throws(Exception::class)
        private fun getAddressMacByFile(wifiMan: WifiManager): String {
            val ret: String
            val wifiState = wifiMan.wifiState

            wifiMan.isWifiEnabled = true
            val fl = File(fileAddressMac)
            val fin = FileInputStream(fl)
            ret = crunchifyGetStringFromStream(fin)
            fin.close()

            val enabled = WifiManager.WIFI_STATE_ENABLED == wifiState
            wifiMan.isWifiEnabled = enabled
            return ret
        }
        @Throws(IOException::class)
        private fun crunchifyGetStringFromStream(crunchifyStream: InputStream?): String {
            if (crunchifyStream != null) {
//                val crunchifyWriter = StringWriter()
//
//                val crunchifyBuffer = CharArray(2048)
//                try {
                val crunchifyReader = BufferedReader(InputStreamReader(crunchifyStream, "UTF-8"))
                return crunchifyReader.use(BufferedReader::readText)
//                    var counter: Int
//                    while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
//                        crunchifyWriter.write(crunchifyBuffer, 0, counter)
//                    }
//                } finally {
//                    crunchifyStream.close()
//                }
//                return crunchifyWriter.toString()
//            } else {
//                return "No Contents"
//            }
            }
            return "No Contents"
        }

        /**
         * 获取厂商名
         */
        val getDeviceManufacturer: String get() {
            return Build.MANUFACTURER
        }

        /**
         * 获取产品名
         */
        val getDeviceProduct: String get() {
            return Build.PRODUCT
        }

        /**
         * 获取手机品牌
         */
        val getDeviceBrand: String get() {
            return Build.BRAND
        }

        /**
         * 获取手机型号
         */
        val getDeviceModel: String get() {
            return Build.MODEL
        }

        /**
         * 获取手机主板名
         */
        val getDeviceBoard: String get() {
            return Build.BOARD
        }

        /**
         * 设备名
         */
        val getDeviceName: String get() {
            return Build.DEVICE
        }

        /**
         * 设备硬件序列号
         */
        val getSerialNumber: String get() {
            return Build.SERIAL?: ""
        }

        /**
         * fingerprit 信息
         */
        val getDeviceFubgerprint: String get() {
            return Build.FINGERPRINT
        }

        /**
         * Android 版本
         */
        val getAndroidRelease: String get() {
            return Build.VERSION.RELEASE
        }

        /**
         * Android SDK版本
         */
        val getAndroidSDK: Int get() {
            return Build.VERSION.SDK_INT
        }

        /**
         * 版本类型
         */
        val getPhoneType: String get() {
            return Build.TYPE
        }

        /**
         * 手机ID
         */
        val getID: String get() {
            return  Build.ID
        }

        /**
         * 手机显示ID
         */
        val getDisplayID: String get() {
            return Build.DISPLAY
        }

        /**
         * 用户名
         */
        val getPhoneUser: String get() {
            return Build.USER
        }

        /**
         * 硬件名
         */
        val getHardware: String get() {
            return Build.HARDWARE
        }

    }
}