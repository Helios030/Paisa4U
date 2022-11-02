package com.paisa.four_u.ui.pay.deferRepay

import android.text.TextUtils
import android.view.View
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityDeferRepayBinding
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.ui.view.popview.paisa_SelectPoPView
import com.paisa.four_u.util.expand.*

class paisa_DeferRepayActivityPaisa : paisa_BaseActivity<ActivityDeferRepayBinding, paisa_DeferRepayVMPaisa>(
    ActivityDeferRepayBinding::inflate, paisa_DeferRepayVMPaisa::class.java
) {


    private var orderID: String = ""
    private var typeID: Int = 5
    private var helpPhone: String = ""
    private var repayBank: String = ""

    override fun initData() {
        orderID = intent.getStringExtra("order_no") ?: ""
        helpPhone = intent.getStringExtra("help_phone") ?: ""
        repayBank = intent.getStringExtra("repay_bank") ?: "UPI"
        vm.getDeferRepayData(orderID)
        observeValue()
    }



    override fun initView() {
        showCustomToast()
        vb.toolbar.apply {
            tvTitle.text =str(R.string.paisa_loan_extension)
            ivExit.setOnClickListener { finish() }
        }

        vb.apply {
            repayAtm.setOnClickListener {
                vm.getRepayBankList(2)
                typeID = 1
            }
//            repayAda.setOnClickListener {
//                vm.getRepayBankList(1)
//                typeID = 2
//            }

            if (helpPhone.isEmpty()) {
                customerServiceTxt.visibility = View.GONE
            } else {
                customerServiceTxt.text = helpPhone
            }
//
//            customerServiceTxt.setOnClickListener {
//                if (helpPhone.isNumberal()) {
//                    makeCall(helpPhone)
//                }
//            }


            repaymentHelpTxt.setOnClickListener {
                openUri(str(R.string.paisa_repayment_guidelines),paisa_ApiServices.helpUrl)
            }



        }


    }

    private fun observeValue() {
        vm.apply {
            liveDeferRepayBean.observe(this@paisa_DeferRepayActivityPaisa) {
                vb.apply {
                    ivslLoanAmount.setLineStr(it.loan_val)
                    ivslLoanDays.setLineStr(it.repay_end_time)
                    ivslRolloverFee.setLineStr(it.extension_fee)
                    ivslNumberOfDaysToRollover.setLineStr("${it.loan_daycount} ${getString(R.string.paisa_day)}")
                    ivslRenewalExpiryTime.setLineStr(it.estimate_repay_end_time)
                    ivslNumberOfRolloversUsed.setLineStr(it.extension_number)
                    if (TextUtils.isEmpty(it.payment_code)) {
                        deferRepayVaLl.visibility = View.GONE
                    } else {
                        deferRepayVaTxt.text = it.payment_code
//                        deferRepayVaLl.visibility = View.VISIBLE
                    }
                    deferRepayVaBtnLl.visibility = View.VISIBLE
                }
            }


            liveRePayBankBean.observe(this@paisa_DeferRepayActivityPaisa){

                showCommSelect(
                    str(R.string.paisa_bank_name),
                    it.str2MenuItem(),
                    object : paisa_SelectPoPView.OnPOPSelected {
                        override fun OnSelected(itemModel: MenuItemModel, position: Int) {
                            vm.getDeferRepayVaData(orderID,typeID,itemModel.menuName)
                        }
                    }
                )







            }







        }
    }


}