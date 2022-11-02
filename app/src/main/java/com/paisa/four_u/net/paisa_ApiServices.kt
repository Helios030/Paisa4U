package com.paisa.four_u.net
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.model.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface paisa_ApiServices {



    /**
     * 通过手机号获取验证码
     */
    @POST("user/get_validate_code")
    suspend fun getVerificationCode(@Body body: RequestBody): BaseModel<VerificationCodeModel>

    /**
     * 通过手机号获取语音验证码
     */
    @POST("user/get_vo_code")
    suspend fun getVoiceVerificationCode(@Body body: RequestBody): BaseModel<VerificationCodeModel>

    /**
     * 用户登录
     */
    @POST("user/login")
    suspend fun login(@Body body: RequestBody): BaseModel<LoginModel>

    /**
     * 上传流量来源
     */
    @POST("user/user_app_log")
    suspend fun upInstallReferrer(@Body body: RequestBody): BaseModel<String>

    /**
     * 获取用户信息
     */
    @POST("user/get_user_info")
    suspend fun getUserInfo(@Body body: RequestBody): BaseModel<UserInfoModel>

    /**
     * 获取还款银行列表
     */
    @POST("loan/get_repay_bank_list")
    suspend fun getRepayBankList(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 获取还款数据
     */
    @POST("loan/get_repayment_data")
    suspend fun getRepayData(@Body body: RequestBody): BaseModel<RepayModel>

    /**
     * 刷新还款数据
     */
    @POST("loan/refresh_repayment_data")
    suspend fun getRefreshRepayData(@Body body: RequestBody): BaseModel<RepayModel>

    /**
     * 获取还款数据方式二
     */
    @POST("loan/get_repayment_new")
    suspend fun getRepaymentData(@Body body: RequestBody): BaseModel<RepaymentModel>

    /**
     * 上传照片
     */
    @POST("user/get_repayment_voucher")
    suspend fun uploadVoucher(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 获取展期数据
     * 服务器代码太多了，不使用展期接口
     *
     *
     */
//    @POST("order/extension_data")
    @POST("loan/get_repayment_data")
    suspend fun getDeferRepayData(@Body body: RequestBody): BaseModel<DeferRepayModel>

    /**
     * 获取展期va码
     */
    @POST("loan/get_repayment_data_extension")
    suspend fun getDeferRepayVaData(@Body body: RequestBody): BaseModel<DeferRepayModel>

    /**
     * 获取用户第一页数据
     */
    @POST("user/get_user_detail_info")
    suspend fun getUserDataOne(@Body body: RequestBody): BaseModel<UserDataModel>

    /**
     * 上传用户第一页数据
     */
    @POST("user/update_user_detail_info")
    suspend fun upUserDataOne(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 上传照片
     */
    @POST("user/pic_upload")
    suspend fun upPicture(@Body body: RequestBody): BaseModel<PhotoModel>

    /**
     *获取地址列表
     */
    @POST("user/get_region_list")
    suspend fun getRegionList(@Body body: RequestBody): BaseModel<List<RegionModel>>

    /**
     * 获取用户第二页数据
     */
    @POST("user/get_user_detail_info")
    suspend fun getUserDataTwo(@Body body: RequestBody): BaseModel<UserDataSecondModel>

    /**
     * 上传用户第二页数据
     */
    @POST("user/update_user_detail_info")
    suspend fun upUserDataTwo(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 获取用户第三页数据
     */
    @POST("user/get_user_detail_info")
    suspend fun getUserDataThree(@Body body: RequestBody): BaseModel<UserDataThreeModel>

    /**
     * 检查用户第三页数据上传
     */
    @POST("user/check_upload_picture")
    suspend fun checkUploadPicture(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 获取用户第四页数据
     */
    @POST("user/get_user_detail_info")
    suspend fun getUserDataFourth(@Body body: RequestBody): BaseModel<UserDataFourthModel>

    /**
     * 上传用户第四页数据
     */
    @POST("user/update_user_detail_info")
    suspend fun upUserDataFourth(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 获取订单选项
     */
    @POST("loan/get_loan_options")
    suspend fun getLoanOptions(@Body body: RequestBody): BaseModel<LoanOptionsModel>

    /**
     * 获取确认订单详情
     */
    @POST("loan/get_apply_page_data")
    suspend fun getApplyPageData(@Body body: RequestBody): BaseModel<SureApplyModel>

    /**
     * 检查支付环境
     */
    @POST("user/check_upload_status")
    suspend fun checkApplyStatus(@Body body: RequestBody): BaseModel<CheckApplyStatusModel>
    /**
     * 提交借款申请
     */
    @POST("loan/apply_loan")
    suspend fun applyLoan(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 上传联系人
     */
    @POST("user/upload_address_list")
    suspend fun upContactsList(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 上传短信
     */
    @POST("user/upload_sms")
    suspend fun upSmsMessageList(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 上传通话记录
     */
    @POST("user/upload_call_record")
    suspend fun upCallRecordList(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 上传应用列表
     */
    @POST("user/upload_install_app_list")
    suspend fun upInstallAppList(@Body body: RequestBody): BaseModel<List<String>>


    /**
     * 上传设备信息
     */
    @POST("user/app_info")
    suspend fun upPhoneState(@Body body: RequestBody): BaseModel<List<String>>

    /**
     * 获取活体许可
     */
    @POST("user/license")
    suspend fun getAdvancelicense(@Body body: RequestBody): BaseModel<LicenseModel>


    /**
     * 上传活体结果
     */
    @POST("user/liveness")
    suspend fun uploadLivenessId(@Body body: RequestBody): BaseModel<Any>

    @POST("devcenter/liveness")
    suspend fun uploadLiveness(@Body body: RequestBody): BaseModel<Any>

    /**
     * 上传活体结果
     */
    @POST("user/customer_service")
    suspend fun getCustomerService(@Body body: RequestBody): BaseModel<String>


    @POST("user/hierarchical_search_ifsc_list")
    suspend  fun getBankInfo(@Body jsonObject: RequestBody): BaseModel<List<String>>

    @POST("user/hierarchical_search_ifsc")
    suspend  fun findBankInfo(@Body jsonObject: RequestBody): BaseModel<List<SearchIFSCBean>>

    @POST("user/ifsc_like")
    suspend fun searchBankInfo(@Body jsonObject: RequestBody): BaseModel<List<SearchIFSCBean>>

    @POST("Loan/pay_mpurse_sdk")
    suspend fun getMpurseInfo(@Body jsonObject: RequestBody): BaseModel<MpurseBean>


    @POST("user/pic_number")
    suspend fun getPicNumber(@Body jsonObject: RequestBody): BaseModel<String>

    @POST("user/get_pics")
    suspend fun uploadPics(@Body body: RequestBody): BaseModel<String>




    companion object {
        val invite =   "${BuildConfig.BASE_URL}/static/appServer/invite.html"//    邀请好友
        val ktpImage =   "${BuildConfig.BASE_URL}/images/app/pic_shili_0.png"
        val handPhotoImage =   "${BuildConfig.BASE_URL}/images/app/pic_shili_1.png"
        val workPermitImage =   "${BuildConfig.BASE_URL}/images/app/pic_shili_2.png"
        val incomeImage =   "${BuildConfig.BASE_URL}/images/app/pic_shili_3.png"
        val privacyUrl = "${BuildConfig.BASE_URL}/h5/pages/privacy.html"
        val termsUrl = "${BuildConfig.BASE_URL}h5/pages/terms.html"
        val agreementUrl = "${BuildConfig.BASE_URL}/html/agreement.html"
        val helpUrl = "${BuildConfig.BASE_URL}/html/rupee_help.html"
    }
    

}