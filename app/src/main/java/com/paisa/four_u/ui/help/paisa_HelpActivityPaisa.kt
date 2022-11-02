package com.paisa.four_u.ui.help

import android.view.View
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityHelpBinding
import com.paisa.four_u.ui.paisa_CommVMPaisa
import com.paisa.four_u.util.expand.str

class paisa_HelpActivityPaisa : paisa_BaseActivity<ActivityHelpBinding, paisa_CommVMPaisa>(
    ActivityHelpBinding::inflate, paisa_CommVMPaisa::class.java
) {
    private var phoneBankOpen = true
    private var onlineBankOpen = true
    private var atmBankOpen = true
    override fun initData() {
    }

    override fun initView() {

        vb.toolbar.apply {
            tvTitle.text = str(R.string.paisa_repayment_help_title)
            ivExit.setOnClickListener { finish() }
        }
        vb.apply {

            phoneBankLl.setOnClickListener {
                if (phoneBankOpen) {
                    phoneBankOpen = false
                    phoneBankTxt.isSingleLine = true
                    phoneBankImb.animate().setDuration(500).rotation(180f).start()
                } else {
                    phoneBankOpen = true
                    phoneBankTxt.visibility = View.VISIBLE
                    phoneBankTxt.isSingleLine = false
                    phoneBankImb.animate().setDuration(500).rotation(0f).start()
                }

            }
            onlineBankLl.setOnClickListener {

                if (onlineBankOpen) {
                    onlineBankOpen = false
                    onlineBankTxt.isSingleLine = true
                    onlineBankImb.animate().setDuration(500).rotation(180f).start()
                } else {
                    onlineBankOpen = true
                    onlineBankTxt.visibility = View.VISIBLE
                    onlineBankTxt.isSingleLine = false
                    onlineBankImb.animate().setDuration(500).rotation(0f).start()
                }

            }
            ATMBankLl.setOnClickListener {

                if (atmBankOpen) {
                    atmBankOpen = false
                    ATMBankTxt.isSingleLine = true
                    ATMBankImb.animate().setDuration(500).rotation(180f).start()
                } else {
                    atmBankOpen = true
                    ATMBankTxt.visibility = View.VISIBLE
                    ATMBankTxt.isSingleLine = false
                    ATMBankImb.animate().setDuration(500).rotation(0f).start()
                }

            }

        }


    }

}