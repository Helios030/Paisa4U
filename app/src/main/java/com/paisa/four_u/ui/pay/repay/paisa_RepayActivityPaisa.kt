package com.paisa.four_u.ui.pay.repay

import android.text.TextUtils
import android.view.View
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityRepayBinding
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.expand.openUri
import com.paisa.four_u.util.expand.show
import com.paisa.four_u.util.expand.str

class paisa_RepayActivityPaisa : paisa_BaseActivity<ActivityRepayBinding, paisa_RePayVMPaisa>(
    ActivityRepayBinding::inflate, paisa_RePayVMPaisa::class.java
) {


    private var orderNo: String = ""
    private var repayBank: String = ""
    private var repayType: Int = 0
    private var helpPhone: String = ""

    override fun initData() {
        initToolbar()
        orderNo = intent.getStringExtra("order_no") ?: ""
        repayBank = intent.getStringExtra("repay_bank") ?: ""
        repayType = intent.getIntExtra("repay_type", 1)
        helpPhone = intent.getStringExtra("help_phone") ?: ""
        getData(false, "")

    }

    private fun initToolbar() {
        vb.toolbar.apply {
            tvTitle.text = str(R.string.paisa_pay_back)
            ivExit.setOnClickListener { finish() }
        }
    }

    override fun initView() {


        showCustomToast()

        vb.apply {
            customerServiceTxt.text = helpPhone

            refreshImg.setOnClickListener {
                getData(true, "")
            }

            repaymentHelpTxt.setOnClickListener {
                openUri(str(R.string.paisa_repayment_guidelines), paisa_ApiServices.helpUrl)
            }


            repaymentHelpTxt.setOnClickListener {

                openUri(str(R.string.paisa_repayment_guidelines), BuildConfig.REPAYMENT_GUIDELINES)
            }

//            customerServiceTxt.setOnClickListener {
//                if (helpPhone.isNotEmpty()) makeCall(helpPhone)
//            }


            repayGetInformation.setOnClickListener {

                if (vb.repayMoneyEdt.text.toString().isNotEmpty()) {
                    getData(false, vb.repayMoneyEdt.text.toString())
                } else {
                    R.string.paisa_please_enter_the_repayment_amount.show()
                }
            }

        }
    }

    private fun getData(isRefresh: Boolean, amount: String) {

        val map = HashMap<String, Any>()
        map["order_no"] = orderNo
        map["repay_type"] = repayType
        map["repay_bank"] = repayBank
        if (vb.repayMoneyEdt.text.toString().isNotEmpty()) {
            map["amount"] = amount
        }

        Slog.d("===> getData  $map  ")


        if (isRefresh) {
            vm.refreshRepaymentData(map)
        } else {
            vm.getRepaymentData(map)
        }

        vm.liveRepayBean.observe(this@paisa_RepayActivityPaisa) { repayBean ->

            Slog.d("===>   iveRepayBean.observe ")

            repayBean.let {
                vb.apply {

                    repayInformation.visibility = View.VISIBLE
                    ivslRepayAmount.setLineStr(it.repay_amount)
                    ivslRepaymentRepayEndTime.setLineStr(it.repay_end_time)
                    ivslRepaymentBank.setLineStr(it.repay_bank)
                    repaymentRepayVaCodeTxt.text = it.va_code
                    repayVaExpireTimeTxt.text = "*${it.va_expire_time}"
                    if (!TextUtils.isEmpty(it.repay_bank_title)) {
                        ivslRepaymentBank.setTitleLineStr(it.repay_bank_title)
                    }
                    if (!TextUtils.isEmpty(it.repay_warning)) {
                        repayWarningTxt.text = it.repay_warning
                    }
                    if (repayType != 1) {
                        ivslRepaymentAcceptAccountName.visibility = View.VISIBLE
                        ivslRepaymentPaymentChannel.visibility = View.VISIBLE
                        ivslRepaymentPaymentChannel.setLineStr(it.payment_channel)
                        ivslRepaymentAcceptAccountName.setLineStr(it.account_name)
                    }
                    if (!TextUtils.isEmpty(it.enter_amount)) {
                        repayMoneyEdt.hint = it.enter_amount
                        repayMoney.visibility = View.VISIBLE
                    }


                }


            }


        }


    }


}