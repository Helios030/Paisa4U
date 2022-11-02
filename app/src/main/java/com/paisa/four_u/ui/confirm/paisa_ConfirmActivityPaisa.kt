package com.paisa.four_u.ui.confirm

import android.os.Build
import android.os.Bundle
import com.facebook.appevents.AppEventsConstants
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityConfirmBinding
import com.paisa.four_u.model.SureApplyModel
import com.paisa.four_u.ui.main.paisa_MainActivityPaisa
import com.paisa.four_u.ui.view.paisa_CommDialog
import com.paisa.four_u.util.paisa_FBEvent
import com.paisa.four_u.util.paisa_FBEvent.appEventsLogger
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.*
import com.paisa.four_u.util.slog
import java.math.BigDecimal
import java.util.*

class paisa_ConfirmActivityPaisa : paisa_BaseActivity<ActivityConfirmBinding, paisa_ConfirmVMPaisa>(
    ActivityConfirmBinding::inflate, paisa_ConfirmVMPaisa::class.java
) {

    val uuid = SpRepository.uuid
    private var loanOptionId: String = ""
    private var applyAmount: Int = 0

    override fun initData() {

        loanOptionId = intent.getStringExtra("loan_option_id").toString()
        applyAmount = intent.getIntExtra("apply_amount", 0)
        vm.apply {
            val map = HashMap<String, Any>()
            map["loan_option_id"] = loanOptionId
            map["apply_amount"] = applyAmount.toString()
            map["imei"] = uuid
            map["imsi"] = uuid
            getApplyPageData(map)
            liveSureApplyBean.observe(this@paisa_ConfirmActivityPaisa) {
                setDataValue(it)
            }


        }

        vm.liveUpdateState.observe(this@paisa_ConfirmActivityPaisa) {

            if (it) {
                val bundle = Bundle()
                bundle.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, 1)
                bundle.putString(
                    AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, vb.ivslLoanAmount.getLineStr()
                )
                bundle.putString(
                    AppEventsConstants.EVENT_PARAM_CONTENT_ID, vb.ivslTerm.getLineStr()
                )
                bundle.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, "IDR")

                paisa_CommDialog(this@paisa_ConfirmActivityPaisa).setTitle(str(R.string.paisa_successful_application))
                    .setImage("file").setIsSingin(true).setMessage(
                        "${str(R.string.paisa_successful_application_tip)}\n\n${
                            str(
                                R.string.paisa_apply_tip
                            )
                        }"
                    ).setonConfirm {
                        paisa_FBEvent.trackEvent_SureApply()
                        launchNewTask<paisa_MainActivityPaisa> {

                        }

                    }.show()


                try {
                    val day = vb.ivslTerm.getLineStr()
                    appEventsLogger.logPurchase(
                        BigDecimal.valueOf(
                            if (day.contains(" ")) {
                                day.split(" ")[0].toLong()
                            } else {
                                day.toLong()
                            }
                        ), Currency.getInstance("IDR"), bundle
                    )
                } catch (e: java.lang.Exception) {
                    e.slog("格式化数字失败")
                }


            }

        }

        vm.liveCheckApplyStatusBean.observe(this@paisa_ConfirmActivityPaisa) {
            val map = HashMap<String, Any>()
            map["loan_option_id"] = loanOptionId
            map["apply_amount"] = applyAmount.toString()
            map["imei"] = uuid
            map["imsi"] = uuid
            map["phone_brand"] = Build.BRAND
            map["phone_model"] = Build.MODEL
            vm.applyLoan(map)


        }


    }

    private fun setDataValue(sureApplyModel: SureApplyModel?) {
        sureApplyModel?.let {
            vb.apply {
                ivslDailyFee.setLineStr(it.overdue_fee)
                ivslLoanAmount.setLineStr(it.repayment_amount)
                ivslInterestFee.setLineStr(it.interest_fee.toString())
                ivslExpectRepayEndTime.setLineStr(it.expect_repay_end_time)
                ivslGST.setLineStr(it.gst_fee)
                ivslTerm.setLineStr(it.loan_days)
                ivslServiceFee.setLineStr(it.management_fee.toString())
                ivslReceivedAmount.setLineStr(it.loan_real_amount.toString())
                ivslRepayAmount.setLineStr(it.repay_amount)
            }
        }
    }

    override fun initView() {

        vb.apply {
            toolbar.apply {
                tvTitle.text = str(R.string.paisa_apply_for_loan)
                ivExit.setOnClickListener { finish() }
            }

            agreementTxt.setOnClickListener {
                openUri(str(R.string.paisa_repayment_guidelines), BuildConfig.REPAYMENT_GUIDELINES)
            }

            nextBtn.setOnClickListener {

                submitLoan()

            }


        }
    }

    private fun submitLoan() {
        if (!vb.agreeCbx.isChecked) {
            R.string.paisa_loan_agree_this.show()
            return
        }
        vm.apply {
            checkApplyStatus()
        }
    }
}