package com.paisa.four_u

import android.app.Application
import android.content.Context
import com.liveness.dflivenesslibrary.DFProductResult
import com.liveness.dflivenesslibrary.DFTransferResultInterface
import com.paisa.four_u.util.Slog
import me.jessyan.autosize.AutoSizeConfig


class paisa_RApplication : Application(), DFTransferResultInterface {
    companion object {
        lateinit var instance: Context
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        AutoSizeConfig.getInstance().setCustomFragment(true).isExcludeFontScale = true
        Slog.getSettings().setLogEnable(BuildConfig.DEBUG).setBorderEnable(BuildConfig.DEBUG)
    }

     var mResult: DFProductResult? = null

    override fun setResult(result: DFProductResult?) {
        mResult = result;
    }

    override fun getResult(): DFProductResult {
        return mResult!!
    }


}