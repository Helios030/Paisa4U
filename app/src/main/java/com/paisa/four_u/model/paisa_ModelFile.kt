package com.paisa.four_u.model


class UserInfoModel(
    var user_loan_info: UserLoanInfoBean? = null,
    var user_base_info: UserBaseInfoBean? = null,
    var announcement_info: AnnouncementBean? = null,
    var guest_phone: String? = null
) {
    data class UserLoanInfoBean(
        var loan_max_amount_desc: String? = null,
        var loan_max_amount: String? = null,
        var remind_tip: String? = null,
        var action: String? = null,
        var button_words: String? = null,
        var loan_status: String = "",
        var extension_msg: String = "",
        var repayment_button_text: String = "",
        var payment_method_state: String = "",
        var status: String? = null,
        var amount: String? = null,
        var loan_daycount: String? = null,
        var order_no: String? = null,
        var end_repay_time: String? = null,
        var repay_day_des: String? = null
    )

    data class UserBaseInfoBean(
        var phone: String? = null,
//            2022年04月08日 添加是否需要活体检测 1 是开启 0 是关闭
        var is_license: Int? = null,
        var is_gdp_license: Int? = null,
        var current_points: String? = null,
        var slogen: String? = null
    )

    data class AnnouncementBean(
        var announcement_title: String = "",
        var announcement_content: String = "",
        var announcement_cancel: String = "",
        var announcement_agree: String = ""
    )
}


class AppInfoModel(
    var appName: String? = null,
    var packageName: String? = null,
    var firstInstallTime: String? = null,
    var lastUpdateTime: String? = null,
    var versionName: String? = null
)


class UserDataSecondModel(var additional_info_list: AdditionalInfoListBean? = null) {

    data class AdditionalInfoListBean(
        var additional_info: UserDataTwoModel? = null,
        var options: OptionsBean? = null,
        var tg_auth: TgAuthBean? = null
    ) {

        data class OptionsBean(
            var monthly_income: LinkedHashMap<String, String>? = null,
            var contacter_01_relationship: LinkedHashMap<String, String>? = null,
            var contacter_02_relationship: LinkedHashMap<String, String>? = null
        )

        data class TgAuthBean(
            var tg_gojek_url: String? = null,
            var tg_operator_url: String? = null,
            var tg_shopee_url: String? = null
        )
    }
}

data class phoneList(
    var list: List<PhoneModel>?
)


data class PhoneModel(

    var name: String? = null,
    var phone: String? = null

)


class UserDataThreeModel(var picture_info_list: UserDataThirdModel? = null)

data class UserDataOneModel(
    var first_name: String = "",
    var last_name: String = "",
    var age: String = "",
    var gender: String = "",
    var id_no: String = "",
    var job_category: String = "",
    var email: String = "",
    var education_degree: String = "",
    var family_province: String = "",
    var family_city: String = "",
    var family_area: String = "",
    var facebook_account: String = "",
    var residence_booklet_account: String = "",
    var family_town: String = "",
    var family_detail_address: String = "",
    var idcard_front: String = "",
    var idcard_hand: String = "",

    var birthday: String = "",
    var issue_date: String = "",


    var pan_card:String=""


)



class UserDataModel(
    var personal_info_list: PersonalInfoListBean? = null
) {
    data class PersonalInfoListBean(
        var user_info: UserDataOneModel? = null,
        var options: OptionsBean? = null
    ) {
        /**
         * job_category_options : {"1":"私人雇员","2":"企业家","3":"国有企业员工","4":"公务员",
         * "5":"学习中的人","6":"警察","7":"退休","8":"专业医生律师","9":"失业的","10":"学生","11":"其他"}
         * education_degree_options : {"1":"小学","2":"初中","3":"高中","4":"大专","5":"大学","6":"硕士","7":"博士"}
         * gender_options : {"1":"男","2":"女"}
         */
        data class OptionsBean(
            var job_category_options: HashMap<String, String>? = null,
            var education_degree_options: HashMap<String, String>? = null,
            var gender_options: HashMap<String, String>? = null
        )
    }


}

data class SearchIFSCBean(
    val bank_name: String,
    val branch: String,
    val district: String,
    val id: String,
    val ifsc_code: String,
    val micr_code: String,
    val state: String
)


class UserDataThirdModel(
    var idcard_hand: String? = null,
    var work_license: String? = null,
    var income_proof: String? = null
) {
    override fun toString(): String {
        return "UserDataThirdBean(idcard_hand=$idcard_hand, work_license=$work_license, income_proof=$income_proof)"
    }
}


data class ContactModel(
    var name: String = "",
    var numbers: ArrayList<PhoneModel> = ArrayList(),
//                       var pingyin: String = "",
    var sortString: String = "#"
) {
    fun addNumber(model: PhoneModel) {
//        if (numbers.isNullOrEmpty()) numbers = ArrayList()
        numbers.add(model)
    }
}


class WiFiModel(
    var mac: String = "",
    var ip: String = "",
    var wifiStatus: String = "",
    var wifiName: String = "",
    var bssid: String = "",
    var ssid: String = "",
    var networkId: String = "",
    var speed: String = ""
) {
    override fun toString(): String {
        return "WiFiBean(mac='$mac', ip='$ip', wifiStatus='$wifiStatus', wifiName='$wifiName', bssid='$bssid', ssid='$ssid', networkId='$networkId', speed='$speed')"
    }
}


/**
 * <pre>
 *     author : Kai
 *     time   : 2022/03/13 15:52
 *     desc   :
 * </pre>
 */
class RepaymentModel(
    var repay_amount: String = "",
    var repay_end_time: String = "",
    var bank_id: String = "",
    var bank_name: String = "",
    var bank_code: String = "",
    var account_name: String = "",
    var account_no: String = "",
    var va_expire_time: String = "",
    var voucher: String = ""
)


/**
 * repay_end_time : 0000-00-00
 * repay_amount : 1200000
 * va_code : 7014023180016750
 * repay_bank : Permata
 * va_expire_time : Kode VA berlaku selama 6 jam
 * account_name : Cash One
 * payment_channel : Bluepay
 */
class RepayModel(
    var repay_end_time: String = "",
    var repay_amount: String = "",
    var va_code: String = "",
    var repay_bank_title: String = "",
    var repay_bank: String = "",
    var va_expire_time: String = "",
    var account_name: String = "",
    var payment_channel: String = "",
    var repay_warning: String = "",
    var enter_amount: String = "",
)

//data class MenuItem(val menuName:String,var isSelected:Boolean=false,val cityBeanResult: CityBeanResult?=null)
data class MenuItemModel(
    val menuName: String,
    var isSelected: Boolean = false,
    val cityModel: RegionModel? = null,
    val menuCode: String = "0"
)

data class MpurseBean(
    var repay_end_time: String,
    var repay_amount: String,
    var token: String,
    var order_id: String,
    var va_code: String,
    var ifsc: String,
    var dc_va_code: String
)


class VerificationCodeModel(
    var date: Any,
    var jump_url: Int = 0,
    var needInvitationCode: Boolean = false
) {
}

/**
 * @Author Ben
 * @Date 2022/4/25 17:59
 * @desc:
 */
data class PhotoModel(
    val idcard_front: String,
    val idcard_hand: String
)


data class LoanOptionsModel(var option_list: List<OptionListBean>? = null) {
    /**
     * loan_option_id : 9
     * option_period : 7
     * option_min_value : 600000
     * option_max_value : 1200000
     * rate : 0.035
     * remind_tip : jumiah yang di terima:
     */
    data class OptionListBean(
        var loan_option_id: String? = null,
        var option_period: String? = null,
        var option_min_value: String? = null,
        var option_max_value: String? = null,
        var section_count: Int = 1,
        var rate: String? = null,
        var remind_tip: String? = null
    )
}


data class CallRecordModel(
    var call_time: String? = null,
    var phone: String? = null,
    var name: String? = null,
    var type: Int? = null, //类型，1呼入，2呼出，3未接通
    var call_duration: Long? = null,
    var is_connected: Int? = null //是否接通，1，接通，2未接通
)


data class LicenseModel(
    val packageName: String,
    val partnerKey: String,
    val partnerCode: String,
    val appName: String
)


data class BaseModel<T>(
    var ret: T,
    var success: Boolean = false,
    var msg: String? = null,
    var code: Int = 0
)






class DeferRepayModel(
    var loan_val: String = "",
    var extension_fee: String = "",
    var loan_daycount: String = "",
    var repay_end_time: String = "",
    var estimate_repay_end_time: String = "",
    var extension_number: String = "",
    var payment_code: String = ""
) {
    override fun toString(): String {
        return "DeferRepayBean(loan_val='$loan_val', extension_fee='$extension_fee', loan_daycount='$loan_daycount', repay_end_time='$repay_end_time', estimate_repay_end_time='$estimate_repay_end_time', extension_number='$extension_number', payment_code='$payment_code')"
    }
}


class LoginModel {
    var token: String = ""
    var user_id: String? = null
    override fun toString(): String {
        return "LoginBean{" +
                "token='" + token + '\''.toString() +
                ", user_id='" + user_id + '\''.toString() +
                '}'.toString()
    }
}

data class UploadPhotoModel(val content: String, val extension: String)




/**
 * sms_upload : 0
 * phonebook_upload : 1
 * callinfo_upload : 0
 */
class CheckApplyStatusModel(
    var sms_upload: Int = 0,
    var phonebook_upload: Int = 0,
    var callinfo_upload: Int = 0
)


class UserDataFourModel(
    var bank_name: String? = "",
    var account_no: String? = "",
    var invitation_code: String? = "",
    var bank_id: String? = "",
    var holder_name: String? = ""
)

data class smsList(
    var sms_list:List<SMSMessageModel>?

)

data class SMSMessageModel(
    var time: String? = null,
    var phone: String? = null,
    var sms_content: String? = null,
    var name: String? = null,
    var is_read: Int? = null,//是否已读,0未读，1已读
    var type: Int? = null//短信类型，1发送，2接收,3发送失败
)


data class UserDataTwoModel(
    var company_name: String = "",
    var company_address: String = "",
    var company_phone: String = "",
    var monthly_income: String = "",
    var get_salary_date: String = "",
    var contacter_01_name: String = "",
    var contacter_01_relationship: String = "",
    var contacter_01_phone: String = "",
    var contacter_02_name: String = "",
    var contacter_02_relationship: String = "",
    var contacter_02_phone: String = "",
    var addition_info: String = "",
    var contacter_03_name: String = "",
    var contacter_03_relationship: String = "",
    var contacter_03_phone: String = "",
    var contacter_04_name: String = "",
    var contacter_04_relationship: String = "",
    var contacter_04_phone: String = "",
    var contacter_05_name: String = "",
    var contacter_05_relationship: String = "",
    var contacter_05_phone: String = ""
)


class RegionModel {
    /**
     * id : 1
     * name : ACEH
     */
    var id: Int = 0
    var name: String = ""
    var parent_id: String = ""
    override fun toString(): String {
        return "RegionBean(id=$id, name='$name', parent_id='$parent_id')"
    }


}


class SureApplyModel(
    val expect_repay_end_time: String="",
    val gst_fee: String="",
    val interest_fee: String="",
    val loan_days: String="",
    val loan_real_amount: String="",
    val management_fee: String="",
    val overdue_fee: String="",
    val repay_amount: String="",
    val repayment_amount: String=""
)


class UserDataFourthModel(var bank_info_list: BankInfoListBean? = null) {
    data class BankInfoListBean(
        var bank_info: UserDataFourModel? = null,
        var options: LinkedHashMap<String, String>? = null
    )
}

data class ConfigBean(
    val app_apk: String?,
    val app_name: String?,
    val app_start_page: String?
)