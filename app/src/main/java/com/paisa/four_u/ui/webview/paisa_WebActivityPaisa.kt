package com.paisa.four_u.ui.webview

import android.net.Uri
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityWebViewBinding
import com.paisa.four_u.ui.paisa_CommVMPaisa
import com.paisa.four_u.ui.login.paisa_LoginActivityPaisa
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.color
import com.paisa.four_u.util.expand.launch
import com.paisa.four_u.util.expand.onClickListener
import com.paisa.four_u.util.slog
import kotlin.math.abs

class paisa_WebActivityPaisa : paisa_BaseActivity<ActivityWebViewBinding, paisa_CommVMPaisa>(
    ActivityWebViewBinding::inflate, paisa_CommVMPaisa::class.java

) {
    companion object {
        const val WEB_URI = "WEB_URI"
        const val WEB_Title = "WEB_Title"
    }

    override fun initData() {


        intent?.let {
            val uri = intent.getStringExtra(WEB_URI)
            val title = intent.getStringExtra(WEB_Title) ?: ""
            Slog.d("  加载网页   $uri ")
            uri?.let { loadURI(it) }
            vb.apply {
            if (SpRepository.isFirstOpenPP) {
                    ppButton.visibility = View.VISIBLE
           setBtn(false)



                    btnConfirm.onClickListener {
                        launch<paisa_LoginActivityPaisa> { }
                        finish()
                    }
                }
                toolbar.apply {
                    tvTitle.text = title
                    ivExit.onClickListener { finish() }
                }
            }

        }


    }

    private fun loadURI(url: String) {
        val mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                vb.flMain,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(R.color.colorPrimary)
            .setWebChromeClient(mWebViewClient)
            .setWebViewClient(webViewClient)//加入webViewClient
            .createAgentWeb()
            .ready()
            .go(url)
        //优先使用网络
        val webViewSetting = mAgentWeb?.webCreator?.webView?.settings
        webViewSetting?.cacheMode = WebSettings.LOAD_DEFAULT
        webViewSetting?.useWideViewPort = true
        webViewSetting?.displayZoomControls = false
        webViewSetting?.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webViewSetting?.loadsImagesAutomatically = true
        webViewSetting?.setNeedInitialFocus(true)
        webViewSetting?.useWideViewPort = true
        webViewSetting?.javaScriptEnabled = true
        webViewSetting?.loadWithOverviewMode = true
        webViewSetting?.domStorageEnabled = true
        webViewSetting?.builtInZoomControls = true
        webViewSetting?.setSupportZoom(true)
        webViewSetting?.allowFileAccess = true
        webViewSetting?.allowFileAccessFromFileURLs = true
        webViewSetting?.allowUniversalAccessFromFileURLs = true
        mAgentWeb?.webCreator?.webView
            ?.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mAgentWeb!!.webCreator
                            .webView.canGoBack()
                    ) { // 表示按返回键时的操作
                        mAgentWeb.webCreator!!.webView.goBack() // 后退
                        return@OnKeyListener true // 已处理
                    } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                        finish()
                    }
                }
                false
            })

        mAgentWeb?.webCreator?.webView?.setOnScrollChangeListener { view, _, p2, p3, p4 ->
            vb.apply {
                if (SpRepository.isFirstOpenPP) {
                    val contentHeight=   mAgentWeb.webCreator?.webView?.contentHeight!!.toFloat()
                    val scale=   mAgentWeb.webCreator?.webView?.scale!!
                    val webViewContentHeight: Float =contentHeight * scale
                    val webViewCurrentHeight: Float = view.height + p2.toFloat()

                    setBtn(abs(webViewContentHeight - webViewCurrentHeight) <= 100f)


                }
            }
        }


    }

    private val mWebViewClient: WebChromeClient = object : WebChromeClient() {

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                return true
            }



        }


    private val webViewClient: WebViewClient = object : WebViewClient() {
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
           "$request $error".slog("网页加载错误")
            setBtn(true)
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            "$request $errorResponse".slog("网页加载错误2")

//            setBtn(true)

        }

    }


    override fun initView() {
    }

    fun setBtn(isEnable :Boolean){
        vb.apply {

            btnConfirm.isEnabled = isEnable

            if(  btnConfirm.isEnabled){
                btnConfirm.setBackgroundColor(color(R.color.colorPrimary))
            }else{
                btnConfirm.setBackgroundColor(color(R.color.gray_f6))
            }

        }


    }

}