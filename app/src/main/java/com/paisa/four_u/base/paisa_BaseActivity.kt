package com.paisa.four_u.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar
import com.hjq.xtoast.XToast
import com.paisa.four_u.R
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.ui.view.paisa_Loading
import com.paisa.four_u.ui.view.popview.paisa_SelectPoPView
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.hideSoftInputWindow


abstract class paisa_BaseActivity<VB : ViewBinding, VM : paisa_BaseVM>(
    private val layout: (LayoutInflater) -> VB, private val clazz: Class<VM>
) : ComponentActivity() {
    protected val vb by lazy { layout(layoutInflater) }
    protected val vm by lazy { ViewModelProvider(this)[clazz] }
    private val paisaLoading by lazy { paisa_Loading(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vb.root)
        initBar()
        vm.liveLoading.observe(this@paisa_BaseActivity) { if (it) showLoading() else hideLoading() }
        initData()
        initView()
    }

     fun showLoading() {

        try {
            paisaLoading.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

     fun hideLoading() {
        try {
            paisaLoading.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    abstract fun initData()
    abstract fun initView()

    protected open fun initBar() {
        ImmersionBar.with(this)
            .fitsSystemWindows(false)
            .transparentStatusBar()
            .keyboardEnable(true)
            .statusBarDarkFont(true)
            .init()
    }

    private var popupWindow = PopupWindow(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    )

    fun showCommSelect(
        title: String,
        operationList: List<MenuItemModel>,
        listener: paisa_SelectPoPView.OnPOPSelected,
    ) {
        this.hideSoftInputWindow()
        paisa_SelectPoPView(this)
            .setTitle(title)
            .setOpentionList(operationList)
            .setOnPOPSelectedListener(listener)
            .showPopupWindow(this.window.decorView.findViewById(android.R.id.content), this)

    }


    fun showCustomToast() {

        if (SpRepository.customerService.isNotEmpty()) {
            XToast<XToast<*>>(this).apply {
                setContentView(R.layout.window_phone)
                setDraggable()
                setAnimStyle(android.R.style.Animation_Translucent)
                setOutsideTouchable(true)
                setXOffset((360 * 0.8f).toInt())
                setYOffset((640 * 0.8f).toInt())
                setOnClickListener(
                    R.id.icon_float
                ) { _, _ ->
                    startActivity(   Intent(Intent.ACTION_VIEW, Uri.parse(SpRepository.customerService)))
                }

            }.show()
        }


    }


}