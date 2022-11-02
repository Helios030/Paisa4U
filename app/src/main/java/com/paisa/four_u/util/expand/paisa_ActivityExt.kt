package com.paisa.four_u.util.expand

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.paisa.four_u.ui.webview.paisa_WebActivityPaisa
import com.paisa.four_u.util.slog


inline fun <reified T : Activity> Context.launch(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    block(intent)
    startActivity(intent)
}



inline fun <reified T : Activity> Context.launchNewTask(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    block(intent)
    startActivity(intent)
}

//扩展打开网页
fun Activity.openUri(title: String, uri: String) {
    launch<paisa_WebActivityPaisa> {
        putExtra(paisa_WebActivityPaisa.WEB_URI, uri)
        putExtra(paisa_WebActivityPaisa.WEB_Title, title)
    }
}


fun Activity.makeCall(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(intent)
    }

}


fun Activity.showImageForUri(uri: String, view: ImageView,placeholder: Int) {
    if (uri.isNotEmpty())
        Glide.with(this).load(uri).fitCenter().placeholder(placeholder).into(view);
}


fun Activity.hideSoftInputWindow() {
    val imm: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (this.isInputMethodShowing()) {
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Activity.isInputMethodShowing(): Boolean {
    //获取当前屏幕内容的高度
    val screenHeight: Int = this.window.decorView.height
    //获取View可见区域的bottom
    val rect = Rect()
    this.window.decorView.getWindowVisibleDisplayFrame(rect)
    return (screenHeight - rect.bottom - this.getSoftButtonsBarHeight()) > 0
}

/**
 * 底部虚拟按键栏的高度
 * @return
 */
fun Activity.getSoftButtonsBarHeight(): Int {
    val metrics = DisplayMetrics()
    //这个方法获取可能不是真实屏幕的高度
    this.windowManager.defaultDisplay.getMetrics(metrics)
    val usableHeight = metrics.heightPixels
    //获取当前屏幕的真实高度
    this.windowManager.defaultDisplay.getRealMetrics(metrics)
    val realHeight = metrics.heightPixels
    return if (realHeight > usableHeight) {
        realHeight - usableHeight
    } else {
        0
    }
}


inline fun Activity.requestPermission(
    permission: Array<String>,
    crossinline agreeBlock: () -> Unit
) {
    if (XXPermissions.isGranted(this, permission)) {
        agreeBlock()
    } else {
        XXPermissions.with(this)
            .permission(permission)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (all) agreeBlock() else permissions.slog("获取部分权限成功，但部分权限未正常授予")
                }
                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    if (never) {
                        permissions.slog("被永久拒绝授权")
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                        XXPermissions.startPermissionActivity(context, permissions)
//                        CommDialog(this)
//                            .setTitle(str(R.string.title_settings_dialog))
//                            .setMessage(str(R.string.permission_not_open_setting))
//                            .setIsSingin(true)
//                            .setonConfirm {
//                                 XXPermissions.startPermissionActivity(context, permissions)
//                            }.show()

                    } else {
                        permissions.slog("为获得权限 ")

                    }
                }
            })
    }

}
