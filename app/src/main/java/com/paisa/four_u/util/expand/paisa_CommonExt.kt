package com.paisa.four_u.util.expand

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.SpRepository
import okhttp3.MediaType
import okhttp3.RequestBody


val context= paisa_RApplication.instance


fun Int.show() {
    try {
        str(this).show()
    }catch (e:Exception){
        e.printStackTrace()
    }

}

fun String.show() {

    try {
        if (this.isNotEmpty())
            Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
    }catch (e:Exception){
        e.printStackTrace()
    }
}


fun str(resId: Int):String {
 return  context.getString(resId)
}

fun color(resId: Int):Int {
    return   context.getColor(resId)
}


var lastClickTime = 0L
fun View.onClickListener(delay: Long = 500L, block: View.() -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < delay)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        block()
    }
}



fun HashMap<String, Any>.createBody(): RequestBody {
    val json = Gson().toJson(this.createCommonParams())
    Slog.d(" 当前API参数  json  $json")
    return RequestBody.create(
        MediaType.parse("application/json;charset=UTF-8"),
        json
    )
}

fun HashMap<String, Any>.createCommonParams(): HashMap<String, Any> {
    this.apply {
       this["place"] = "google"
       this["sub_place_code"] = ""
       this["other_info"] = ""
       this["phone_brand"] = Build.BRAND
       this["phone_model"] = Build.MODEL
       this["device_id"] = SpRepository.uuid
       this["pck_name"] = BuildConfig.APPLICATION_ID
       this["invitationCode"] = ""
    }
    return this
}

fun HashMap<String, String>.associateMap(): Map<String, String> {
  return this.entries.associateBy({ it.value }) { it.key }
}


fun HashMap<String, String>.getHashMapByValue(value: String): String {
    var key: String? = null
    val set: Set<Map.Entry<String, String>> = this.entries
    for ((key1, value1) in set) {
        if (value1 == value) {
            key = key1
            break
        }
    }
    return key?:""
}


fun Any.toMap(): HashMap<String, Any> {
    val map: HashMap<String, Any> = HashMap()
    for (field in this.javaClass.declaredFields) {

     try{
         val flag = field.isAccessible
         field.isAccessible = true
         val o = field[this]
         map[field.name] = o
         field.isAccessible = flag
     }catch(e: Exception){
         e.printStackTrace()
      continue
     }


    }
    return map
}



fun   isNetAvailable(): Boolean {
    var isNetUsable = false
    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities: NetworkCapabilities? =
        manager.getNetworkCapabilities(manager.activeNetwork)
    if (networkCapabilities != null) {
        isNetUsable =
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    return isNetUsable
}

// map转pop windows 数据类
fun  Map<String, String?>.map2MenuItem(): List<MenuItemModel> = this.map { MenuItemModel(it.value!!, false, menuCode = it.key) }

fun  List<String>.str2MenuItem(): List<MenuItemModel> = this.map { MenuItemModel(it, false) }






