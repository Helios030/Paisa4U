package com.paisa.four_u.ui.main

import android.Manifest
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityMainBinding
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.ui.face.paisa_FaceActivityPaisa
import com.paisa.four_u.ui.login.paisa_LoginActivityPaisa
import com.paisa.four_u.ui.pay.deferRepay.paisa_DeferRepayActivityPaisa
import com.paisa.four_u.ui.pay.repay.paisa_RepayActivityPaisa
import com.paisa.four_u.ui.pay.repayment.paisa_RepaymentActivityPaisa
import com.paisa.four_u.ui.userData.first.paisa_PersonalActivityPaisa
import com.paisa.four_u.ui.view.paisa_CommDialog
import com.paisa.four_u.util.paisa_DeviceUtils.getLastKnownLocation
import com.paisa.four_u.util.paisa_FBEvent
import com.paisa.four_u.util.PreferencesUtil
import com.paisa.four_u.util.SpRepository
import com.paisa.four_u.util.expand.*
import com.paisa.four_u.util.slog

class paisa_MainActivityPaisa : paisa_BaseActivity<ActivityMainBinding, paisa_MainViewModel>(
    ActivityMainBinding::inflate, paisa_MainViewModel::class.java
) {
    private var helpPhone: String = ""
    private var currantType: Int = 2
    private var orderNo: String = ""
    var isLicense: Int? = 0
    override fun initData() {
        getUserInfo()
        observeValue()
        showCustomToast()

        setUserLocation()

    }

    private fun setUserLocation() {


        requestPermission(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        ) {
            getLastKnownLocation()?.let {
                SpRepository.location_lat = it.latitude.toString()
                SpRepository.location_lon = it.longitude.toString()

            }
        }


    }

    override fun initView() {
        vb.apply {
            radioGroup.setOnCheckedChangeListener { _, checkedId ->

                when (checkedId) {
                    R.id.rb_home -> {
                        vb.llUser.visibility = View.GONE
                        vb.rlMain.visibility = View.VISIBLE
                    }
                    R.id.rb_me -> {
                        vb.llUser.visibility = View.VISIBLE
                        vb.rlMain.visibility = View.GONE

                    }

                }
            }

            mainRefreshImg.setOnClickListener {
                getUserInfo()
            }

            llHelp.setOnClickListener {


                openUri(str(R.string.paisa_help_center), paisa_ApiServices.helpUrl)

            }


            llVersion.setOnClickListener {
                paisa_CommDialog(this@paisa_MainActivityPaisa).setTitle(str(R.string.paisa_version))
                    .setMessage("V${BuildConfig.VERSION_NAME}").show()

            }

            llExit.setOnClickListener {

                paisa_CommDialog(this@paisa_MainActivityPaisa)
                    .setTitle(str(R.string.paisa_exit_login))
                    .setMessage(str(R.string.paisa_log_out_tip))
                    .setonConfirm {
                        paisa_FBEvent.trackEvent_LogOut()
                        PreferencesUtil.clear()
                        launch<paisa_LoginActivityPaisa> { }
                        finish()
                    }
                    .setonCancel { }
                    .show()


            }


        }

    }

//    var isPayAll = true
    private fun getUserInfo() {
        vm.getUserInfo()
    }


//    private fun getRepayBankList() {
//        vm.getrePaybank(HashMap<String, Any>().apply { this["type"] = currantType })
//
//    }

    private fun observeValue() {
        vm.liveUserInfo.observe(this) { userinfo ->
            //公告弹窗
            userinfo.announcement_info?.let {
                if (SpRepository.isShowDialog) {
                    paisa_CommDialog(this)
                        .setTitle(it.announcement_title)
                        .setMessage(it.announcement_content)
                        .setIsSingin(true)
                        .show()
                    SpRepository.isShowDialog = false
                }
            }



            vb.apply {
                helpPhone = userinfo.guest_phone.toString()
                mainCustomerTv.text = str(R.string.paisa_hot_line) + helpPhone
                mainRepayBtn.setOnClickListener {
//                    isPayAll = true
//                    getRepayBankList()
                    launch<paisa_RepayActivityPaisa> {
                        putExtra("order_no", orderNo)
                        putExtra("repay_bank","Repayment Link")
                        putExtra("help_phone", helpPhone)
                        putExtra("repay_type", 1)
                    }

                }

                mainHelpTv.setOnClickListener {
//                    launch<HelpActivity> { }
                    openUri(str(R.string.paisa_help_center), paisa_ApiServices.helpUrl)
                }

                mainRepaymentBtn.setOnClickListener {
                    launch<paisa_RepaymentActivityPaisa> {
                        putExtra("order_no", orderNo)
                        putExtra("help_phone", helpPhone)
                    }
                }

                mainCustomerTv.setOnClickListener {
                    if (helpPhone.isNotEmpty() && !helpPhone.contains("@")) {
                        makeCall(helpPhone)
                    }
                }


                mainDeferRepayBtn.setOnClickListener {
//                    isPayAll = false
//                    getRepayBankList()

                    launch<paisa_DeferRepayActivityPaisa> {
                        putExtra("order_no", orderNo)
                        putExtra("repay_bank", "Repayment Link Extension")
                        putExtra("help_phone", helpPhone)
                        putExtra("repay_type", 5)
                    }
                }


                userinfo.user_loan_info?.let {
                    orderNo = if (it.order_no?.isNotEmpty() == true) it.order_no.toString() else ""
                    it.slog("zhaungtai  ")
                    when (it.action) {
                        "start" -> {
                            mainApplyLl.visibility = View.VISIBLE
                            mainResultLl.visibility = View.GONE
                            mainRefuseStatusLl.visibility = View.GONE
                            mainRepayStatusLl.visibility = View.GONE
                            tvProgress.visibility = View.VISIBLE
                            mainLoanMoneyTipTv.text = it.loan_max_amount_desc
                            mainLoanMoneyTv.text = it.loan_max_amount
                            mainLoanBtn.text = it.button_words
                        }

                        "repay" -> {
                            paisa_FBEvent.trackEvent_Repay()
                            mainApplyLl.visibility = View.GONE
                            mainResultLl.visibility = View.VISIBLE
                            mainRefuseStatusLl.visibility = View.GONE
                            llReview.visibility = View.VISIBLE
                            tvProgress.visibility = View.GONE
                            mainRepayStatusLl.visibility = View.VISIBLE
                            mainOrderNoTv.text = str(R.string.paisa_order_num) + it.order_no
                            mainApplyAmountTv.text = it.amount
                            mainRepayStatusTv.text = it.status
                            mainRemindTipTv.text = it.remind_tip
                            mainLoanDaysTv.text = it.end_repay_time


                            if (TextUtils.isEmpty(it.button_words)) {
                                mainRepayBtn.visibility = View.GONE
                            } else {
                                mainRepayBtn.text = it.button_words
                                mainRepayBtn.visibility = View.VISIBLE
                            }
                            if (TextUtils.isEmpty(it.repayment_button_text)) {
                                mainRepaymentBtn.visibility = View.GONE
                            } else {
                                mainRepaymentBtn.text = it.repayment_button_text
                                mainRepaymentBtn.visibility = View.VISIBLE
                            }
                            if (TextUtils.isEmpty(it.payment_method_state)) {
                                optionRg.visibility = View.GONE
                            } else {
                                optionRg.visibility = View.VISIBLE
                            }
                            optionRg.setOnCheckedChangeListener { _, checkedId ->

                                when (checkedId) {
                                    R.id.option1_rb -> {
                                        option1Rb.setTextColor(color(R.color.white))
                                        option2Rb.setTextColor(color(R.color.colorPrimary))
                                        currantType = 2
                                    }
                                    R.id.option2_rb -> {
                                        option1Rb.setTextColor(color(R.color.colorPrimary))
                                        option2Rb.setTextColor(color(R.color.white))
                                        currantType = 1
                                    }

                                }
                            }
                            currantType = if (option1Rb.isChecked) {
                                2
                            } else {
                                1
                            }

                        }

                        else -> {
                            mainApplyLl.visibility = View.GONE
                            mainResultLl.visibility = View.VISIBLE
                            llReview.visibility = View.VISIBLE
                            mainRefuseStatusLl.visibility = View.GONE
                            mainOrderNoTv.text = str(R.string.paisa_order_num) + it.order_no
                            mainApplyAmountTv.text = it.amount
                            mainLoanDaysTv.text = "${it.loan_daycount} " + str(R.string.paisa_day)
                            mainRepayStatusTv.text = it.status
                            mainRemindTipTv.text = it.remind_tip
                        }


                    }


                    when (it.loan_status) {
                        "repay" -> {
                            mainDeferRepayBtn.visibility = View.VISIBLE
                            mainDeferRepayTipTxt.visibility = View.GONE
                        }
                        "end_extension" -> {
                            mainDeferRepayTipTxt.text = it.extension_msg
                            mainDeferRepayBtn.visibility = View.GONE
                            mainDeferRepayTipTxt.visibility = View.VISIBLE
                        }
                        else -> {
                            mainDeferRepayBtn.visibility = View.GONE
                            mainDeferRepayTipTxt.visibility = View.GONE
                        }
                    }

                    userinfo.user_base_info?.let {
                        isLicense = it.is_license
                        tvUserPhone.text = getString(R.string.paisa_hello, "${BuildConfig.AREA_CODE} ${it.phone}")
                        if (TextUtils.isEmpty(it.current_points)) {
                            pointsTxt.visibility = View.GONE
                        } else {
                            pointsTxt.text =
                                getString(R.string.paisa_your_current_points, it.current_points)
                            pointsTxt.visibility = View.VISIBLE
                        }

                    }

                }






                mainAgreementTv.setOnClickListener {
                    openUri(str(R.string.paisa_privacy_policy), paisa_ApiServices.agreementUrl)

                }




                mainLoanBtn.setOnClickListener {

                    if (!mainAgreeCheck.isChecked) {
                        R.string.paisa_agree_this.show()
                        return@setOnClickListener
                    }
                    paisa_FBEvent.trackEvent_ToLoan()
                    if (isLicense == null || isLicense == 0) {
                        launch<paisa_PersonalActivityPaisa> { }
                    } else {
                        launch<paisa_FaceActivityPaisa> { }
                    }
                }
            }
        }

//        vm.liveRePayBank.observe(this) {
//            showCommSelect(
//                str(R.string.select),
//                it.str2MenuItem(),
//                object : SelectPoPView.OnPOPSelected {
//                    override fun OnSelected(itemModel: MenuItemModel, position: Int) {
//
//
////                        Repayment Link
//
//                        if (isPayAll) {
//                            launch<RepayActivity> {
//                                putExtra("order_no", orderNo)
//                                putExtra("repay_bank","Repayment Link")
//                                putExtra("help_phone", helpPhone)
//                                putExtra("repay_type", 1)
//                            }
//                        } else {
//                            launch<DeferRepayActivity> {
//                                putExtra("order_no", orderNo)
//                                putExtra("repay_bank", "itemModel.menuName")
//                                putExtra("help_phone", helpPhone)
//                                putExtra("repay_type", 2)
//                            }
//
//                        }
//
//
//                    }
//                }
//            )
//
//
//        }
    }


    // 定义一个变量，来标识是否退出
    private var isExit = false
    private var mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            isExit = false
        }
    }

    override fun onBackPressed() {
        if (!isExit) {
            isExit = true
            R.string.paisa_closed_tip.show()
            mHandler.sendEmptyMessageDelayed(0, 2000)
        } else {
            finish()
        }
    }


}