package com.paisa.four_u.ui.userData.second

import android.Manifest
import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityAdditionalBinding
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.model.UserDataSecondModel
import com.paisa.four_u.model.UserDataTwoModel
import com.paisa.four_u.ui.userData.fourth.paisa_BankActivityPaisa
import com.paisa.four_u.ui.view.popview.paisa_SelectPoPView
import com.paisa.four_u.util.paisa_DeviceUtils.queryContactByUri
import com.paisa.four_u.util.paisa_FBEvent
import com.paisa.four_u.util.expand.*
import com.paisa.four_u.util.slog

class paisa_AdditionalActivityPaisa : paisa_BaseActivity<ActivityAdditionalBinding, paisa_AdditionalVMPaisa>(
    ActivityAdditionalBinding::inflate, paisa_AdditionalVMPaisa::class.java
) {

    companion object {
        val Intent_WORK_TYPE = "Intent_WORK_TYPE"
    }

    private var userDataModel: UserDataTwoModel? = null
    private var options: UserDataSecondModel.AdditionalInfoListBean.OptionsBean? = null

    //隐藏公司相关信息
    private var hideCompany = false

    //区分是否自由职业人或者生意人 (企业家)
    var workTypeArray = ""


    override fun initData() {


        val workType: String = intent.getStringArrayExtra(Intent_WORK_TYPE)?.toString()
            ?: str(R.string.paisa_work_type_others)
        workTypeArray = str(R.string.paisa_work_type_others) + "," + str(R.string.paisa_work_type_entrepreneur)
        if (workTypeArray.contains(workType)) {
            hideCompany = true
        }

        vm.apply {
            getUserDataSecond()
            liveUserDataBean.observe(this@paisa_AdditionalActivityPaisa) {
                userDataModel = it.additional_info_list?.additional_info ?: UserDataTwoModel()
                setDataValue(userDataModel)
                options = it.additional_info_list?.options

            }

            liveUploadState.observe(this@paisa_AdditionalActivityPaisa) {
                "第二页上传状态  $it".slog()
                if (it) {
                    paisa_FBEvent.trackEvent_Achieved2()
//                    launch<PhotoActivity> { }
                    launch<paisa_BankActivityPaisa> {  }

                }

            }

        }


    }

    private fun setDataValue(userDataModel: UserDataTwoModel?) {
        userDataModel?.let {
            vb.apply {


                iviCompanyName.setEditStr(it.company_name)
                iviCompanyTelephone.setEditStr(it.company_phone)
                iviCompanyAddress.setEditStr(it.company_address)
                iviPayday.setEditStr(it.get_salary_date)
                ivsMonthlySalary.setSelectStr(it.monthly_income)
                ivsContact1.setSelectStr(it.contacter_01_relationship)
                ivsContact2.setSelectStr(it.contacter_02_relationship)
//                ivsContact3.setSelectStr(it.contacter_03_relationship)
//                ivsContact4.setSelectStr(it.contacter_04_relationship)
//                ivsContact5.setSelectStr(it.contacter_05_relationship)
                iviContactName1.setEditStr(it.contacter_01_name)
                iviContactName2.setEditStr(it.contacter_02_name)
//                iviContactName3.setEditStr(it.contacter_03_name)
//                iviContactName4.setEditStr(it.contacter_04_name)
//                iviContactName5.setEditStr(it.contacter_05_name)
                ivsContactNumber1.setSelectStr(it.contacter_01_phone)
                ivsContactNumber2.setSelectStr(it.contacter_02_phone)
//                ivsContactNumber3.setSelectStr(it.contacter_03_phone)
//                ivsContactNumber4.setSelectStr(it.contacter_04_phone)
//                ivsContactNumber5.setSelectStr(it.contacter_05_phone)

                if (hideCompany) {
                    iviCompanyName.visibility = View.GONE
                    iviCompanyTelephone.visibility = View.GONE
                    iviPayday.visibility = View.GONE
                    iviCompanyName.setEditStr("NO COMPANY NAME")
                    iviCompanyTelephone.setEditStr("00000000")
                    iviPayday.setEditStr("01")
                    iviCompanyAddress.setTitleStr(str(R.string.paisa_job_address))
                    ivsMonthlySalary.setTitleStr(str(R.string.paisa_monthly_income))
                } else {
                    iviCompanyName.visibility = View.VISIBLE
                    iviCompanyTelephone.visibility = View.VISIBLE
                    iviPayday.visibility = View.VISIBLE
                    iviCompanyAddress.setTitleStr(str(R.string.paisa_job_address))
                    iviCompanyAddress.setTitleStr(str(R.string.paisa_company_address))
                    ivsMonthlySalary.setTitleStr(str(R.string.paisa_monthly_salary))
                }

            }
        }
    }

    fun getUserPhone(contractIntet: ActivityResultLauncher<Intent>) {
        requestPermission(arrayOf(Manifest.permission.READ_CONTACTS)) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            contractIntet.launch(intent)
        }
    }
  
    override fun initView() {
        vb.apply {

            toolbar.apply {
                tvTitle.text = str(R.string.paisa_additional_information)
                ivExit.setOnClickListener { finish() }
            }

            val contractIntent1 =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    val contact = getContacts(it.data) ?: return@registerForActivityResult
                    iviContactName1.setEditStr(contact.first) 
                    ivsContactNumber1.setSelectStr(
                    contact.second
                )
                }
            val contractIntent2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    val contact = getContacts(it.data)
                        ?: return@registerForActivityResult 
                    iviContactName2.setEditStr(contact.first)
                    ivsContactNumber2.setSelectStr(
                    contact.second
                )
                }
//            val contractIntent3 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                val contact = getContacts(it.data)
//                    ?: return@registerForActivityResult
//                iviContactName3.setEditStr(contact.first)
//                ivsContactNumber3.setSelectStr(
//                contact.second
//            )
//            }
//            val contractIntent4 =
//                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                    val contact = getContacts(it.data) ?: return@registerForActivityResult
//                    iviContactName4.setEditStr(contact.first)
//                    ivsContactNumber4.setSelectStr(
//                    contact.second
//                )
//                }
//            val contractIntent5 =
//                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//                    val contact = getContacts(it.data)
//                        ?: return@registerForActivityResult
//                    iviContactName5.setEditStr(contact.first)
//                    ivsContactNumber5.setSelectStr(
//                    contact.second
//                )
//                }

            ivsContactNumber1.setOnSelectListener { getUserPhone(contractIntent1) }
            ivsContactNumber2.setOnSelectListener { getUserPhone(contractIntent2) }
//            ivsContactNumber3.setOnSelectListener { getUserPhone(contractIntent3) }
//            ivsContactNumber4.setOnSelectListener { getUserPhone(contractIntent4) }
//            ivsContactNumber5.setOnSelectListener { getUserPhone(contractIntent5) }

            ivsMonthlySalary.setOnSelectListener {
                options?.monthly_income?.let {
                    showCommSelect(
                        str(R.string.paisa_monthly_salary),
                        it.map2MenuItem(), object : paisa_SelectPoPView.OnPOPSelected {
                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
                                ivsMonthlySalary.setSelectStr(itemModel.menuName)
                                userDataModel!!.monthly_income = itemModel.menuCode
                            }
                        })
                }
            }

            ivsContact1.setOnSelectListener {
                options?.contacter_01_relationship?.let {
                    showCommSelect(
                        str(R.string.paisa_contact_relationship),
                       it.map2MenuItem(),
                        object : paisa_SelectPoPView.OnPOPSelected {
                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
                                ivsContact1.setSelectStr(itemModel.menuName)
                                userDataModel!!.contacter_01_relationship = itemModel.menuCode
                            }
                        })
                }
            }
            ivsContact2.setOnSelectListener {
                options?.contacter_02_relationship?.let {
                    showCommSelect(
                        str(R.string.paisa_contact_relationship),
                         it.map2MenuItem(),
                        object : paisa_SelectPoPView.OnPOPSelected {
                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
                                ivsContact2.setSelectStr(itemModel.menuName)
                                userDataModel!!.contacter_02_relationship = itemModel.menuCode
                            }
                        })
                }
            }
//            ivsContact3.setOnSelectListener {
//                options?.contacter_02_relationship?.let {
//                    showCommSelect(
//                        str(R.string.contact_relationship),
//                         it.map2MenuItem(),
//                        object : SelectPoPView.OnPOPSelected {
//                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
//                                ivsContact3.setSelectStr(itemModel.menuName)
//                                userDataModel!!.contacter_03_relationship = itemModel.menuCode
//                            }
//                        })
//                }
//            }
//            ivsContact4.setOnSelectListener {
//                options?.contacter_02_relationship?.let {
//                    showCommSelect(
//                        str(R.string.contact_relationship),
//                         it.map2MenuItem(),
//                        object : SelectPoPView.OnPOPSelected {
//                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
//                                ivsContact4.setSelectStr(itemModel.menuName)
//                                userDataModel!!.contacter_04_relationship = itemModel.menuCode
//                            }
//                        })
//                }
//            }
//            ivsContact5.setOnSelectListener {
//                options?.contacter_02_relationship?.let {
//                    showCommSelect(
//                        str(R.string.contact_relationship),
//                         it.map2MenuItem(),
//                        object : SelectPoPView.OnPOPSelected {
//                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
//                                ivsContact5.setSelectStr(itemModel.menuName)
//                                userDataModel!!.contacter_05_relationship = itemModel.menuCode
//                            }
//                        })
//                }
//            }

            nextBtn.setOnClickListener { uploadUserData() }



        }

    }

    private fun uploadUserData() {
        vb.apply {

            userDataModel?.let {
                it.company_name = iviCompanyName.getEditStr()
                it.company_phone = iviCompanyTelephone.getEditStr()
                it.company_address = iviCompanyAddress.getEditStr()
                it.get_salary_date = iviPayday.getEditStr()
                it.contacter_01_name = iviContactName1.getEditStr()
                it.contacter_02_name = iviContactName2.getEditStr()
//                it.contacter_03_name = iviContactName3.getEditStr()
//                it.contacter_04_name = iviContactName4.getEditStr()
//                it.contacter_05_name = iviContactName5.getEditStr()
                it.contacter_01_phone = ivsContactNumber1.getSelectStr()?:""
                it.contacter_02_phone = ivsContactNumber2.getSelectStr()?:""
//                it.contacter_03_phone = ivsContactNumber3.getSelectStr()?:""
//                it.contacter_04_phone = ivsContactNumber4.getSelectStr()?:""
//                it.contacter_05_phone = ivsContactNumber5.getSelectStr()?:""
                options?.contacter_01_relationship?.let { education -> it.contacter_01_relationship =education.getHashMapByValue(ivsContact1.getSelectStr()?:"") }
                options?.contacter_02_relationship?.let { education -> it.contacter_02_relationship =education.getHashMapByValue(ivsContact2.getSelectStr()?:"") }
//                options?.contacter_02_relationship?.let { education -> it.contacter_03_relationship =education.getHashMapByValue(ivsContact3.getSelectStr()?:"") }
//                options?.contacter_02_relationship?.let { education -> it.contacter_04_relationship =education.getHashMapByValue(ivsContact4.getSelectStr()?:"") }
//                options?.contacter_02_relationship?.let { education -> it.contacter_05_relationship =education.getHashMapByValue(ivsContact5.getSelectStr()?:"") }
                options?.monthly_income?.let { education -> it.monthly_income =education. getHashMapByValue(ivsMonthlySalary.getSelectStr()?:"") }

                if (it.company_name.isEmpty()) {
                    R.string.paisa_please_input_company_name.show()
                    return
                }


                if (it.company_phone.isEmpty()) {
                    R.string.paisa_please_input_company_phone.show()
                    return
                }


                if (it.company_address.isEmpty()) {
                    R.string.paisa_please_input_companyAdderss.show()
                    return
                }

                if (it.monthly_income.isEmpty()) {
                    R.string.paisa_please_input_monthly_income.show()
                    return
                }
                if (it.get_salary_date.isEmpty()) {
                    R.string.paisa_please_input_payday.show()
                    return
                }

                if (

                    it.contacter_01_phone.isEmpty() ||
                    it.contacter_02_phone.isEmpty()
//                    it.contacter_03_phone.isEmpty() ||
//                    it.contacter_04_phone.isEmpty()
//                    it.contacter_05_phone.isEmpty()
                ) {
                    R.string.paisa_please_select_contacter_phone.show()
                    return
                }

                if (
                    it.contacter_01_name.isEmpty() ||
                    it.contacter_02_name.isEmpty()
//                    it.contacter_03_name.isEmpty() ||
//                    it.contacter_04_name.isEmpty()
//                    it.contacter_05_name.isEmpty()
                ) {
                    R.string.paisa_please_input_contacter_name.show()
                    return
                }
                if (
                    it.contacter_01_relationship.isEmpty() ||
                    it.contacter_02_relationship.isEmpty()
//                    it.contacter_03_relationship.isEmpty() ||
//                    it.contacter_04_relationship.isEmpty()
//                    it.contacter_05_relationship.isEmpty()

                ) {
                    R.string.paisa_please_select_contacter_relationship.show()
                    return
                }
            }
            vm.apply {
                userDataModel.slog("上传联系人信息")
                uploadUserDataSecond(userDataModel!!)

            }
        }
    }

    private fun getContacts(data: Intent?): Pair<String, String>? {
        val contactData = data?.data ?: return null
        var queryContactByUri = queryContactByUri(contactData)
        var number = ""
        var name = ""
        queryContactByUri = queryContactByUri ?: return null
        if (queryContactByUri.size >= 2) {
            name = queryContactByUri[0]
            number = queryContactByUri[1]
        }
        return Pair(name, number)
    }
}