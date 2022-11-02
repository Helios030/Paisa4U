package com.paisa.four_u.ui.userData.first

import android.Manifest
import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.app.DatePickerDialog
import android.content.pm.ActivityInfo
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.language.LanguageConfig
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityPersonalBinding
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.model.RegionModel
import com.paisa.four_u.model.UserDataModel
import com.paisa.four_u.model.UserDataOneModel
import com.paisa.four_u.net.paisa_ApiServices
import com.paisa.four_u.ui.userData.second.paisa_AdditionalActivityPaisa
import com.paisa.four_u.ui.view.paisa_CommDialog
import com.paisa.four_u.ui.view.popview.paisa_SelectAddressPoPView
import com.paisa.four_u.ui.view.popview.paisa_SelectPoPView
import com.paisa.four_u.util.*
import com.paisa.four_u.util.expand.*
import java.io.File
import java.util.*

class paisa_PersonalActivityPaisa : paisa_BaseActivity<ActivityPersonalBinding, paisa_PersonalVMPaisa>(
    ActivityPersonalBinding::inflate, paisa_PersonalVMPaisa::class.java
) {
    private var userDataOneModel: UserDataOneModel = UserDataOneModel()
    private var options: UserDataModel.PersonalInfoListBean.OptionsBean? = null
    var cityBeanResults = mutableListOf<RegionModel>()


    override fun initData() {
        vm.apply {
            getCityList("0")
            getUserDataOne()
            liveUserDataBean.observe(this@paisa_PersonalActivityPaisa) { userDataBean ->
                userDataBean.personal_info_list?.user_info?.let {
                    Slog.d("===>     被覆盖？")

                    userDataOneModel = it
                    options = userDataBean.personal_info_list?.options
                    setDataValue(userDataOneModel)
                }

            }

            liveCityBean.observe(this@paisa_PersonalActivityPaisa) { cityResults ->
                cityBeanResults.clear()
                cityBeanResults.addAll(cityResults)

                selectPoPView?.setOpentionList(cityBeanResults.map {
                    MenuItemModel(it.name, false, it)
                })


            }

            liveUploadState.observe(this@paisa_PersonalActivityPaisa) {
                "第一页数据上传结果 $it".slog()
                if (it) {
                    paisa_FBEvent.trackEvent_Achieved1()
                    launch<paisa_AdditionalActivityPaisa> {
                        putExtra(paisa_AdditionalActivityPaisa.Intent_WORK_TYPE, vb.ivsWorType.getSelectStr())
                    }
                }

            }

            livePhotoBean.observe(this@paisa_PersonalActivityPaisa) {
                if (it.idcard_front.isNotEmpty()) {
                    "身份证上传返回  $it ".slog()
                    userDataOneModel.idcard_front = it.idcard_front
                    SpRepository.idPhoto = it.idcard_front
                }

            }

            livePhotoBackBean.observe(this@paisa_PersonalActivityPaisa) {
                if (it.idcard_hand.isNotEmpty()) {
                    "身份证b背面上传返回  $it ".slog()
                    userDataOneModel.idcard_hand = it.idcard_hand
                    SpRepository.idPhotoHand = it.idcard_hand

                }

            }


        }


    }

    //回显服务器数据
    private fun setDataValue(userDataBean: UserDataOneModel?) {

        userDataBean?.let {
            vb.apply {
                if (it.idcard_front.isNotEmpty()) {
                    SpRepository.idPhoto = it.idcard_front
                    showImageForUri(it.idcard_front, ivIdcardFront,R.mipmap.paisa_img_upload_img)
                }


                if (it.idcard_hand.isNotEmpty()) {
                    SpRepository.idPhotoHand = it.idcard_hand
                    showImageForUri(it.idcard_hand, ivIdcardBack,R.mipmap.paisa_img_upload_back)
                }

                if (it.last_name.isNotEmpty()) {
                    iviLastName.setEditStr(it.last_name)
                }

                if (it.first_name.isNotEmpty()) {
                    iviFirstName.setEditStr(it.first_name)
                }
                if (it.id_no.isNotEmpty()) {
                    iviId.setEditStr(it.id_no)
                }

//                if (it.age.isNotEmpty()) {
//                    iviAge.setEditStr(it.age)
//                }
                if (it.gender.isNotEmpty()) {
                    ivrGender.setChecked(it.gender == str(R.string.paisa_pria))
                }
                if (it.email.isNotEmpty()) {
                    iviEmail.setEditStr(it.email)
                }
                if (it.facebook_account.isNotEmpty()) {
                    iviFacebookEmail.setEditStr(it.facebook_account)
                }
                if (it.education_degree.isNotEmpty()) {
                    ivsEducation.setSelectStr(it.education_degree)
                }
                if (it.job_category.isNotEmpty()) {
                    ivsWorType.setSelectStr(it.job_category)
                }
                if (it.family_province.isNotEmpty()) {
                    ivsForeverAddress.setSelectStr("${it.family_province} ${it.family_city} ${it.family_city} ${it.family_town}")
                }
                if (it.family_detail_address.isNotEmpty()) {
                    iviFullAddress.setEditStr(it.family_detail_address)
                }


                if (it.birthday.isNotEmpty()) {
                    ivsBirth.setSelectStr(it.birthday)
                }

                if (it.issue_date.isNotEmpty()) {
                    ivsIssue.setSelectStr(it.issue_date)
                }


//                if(it.pan_card.isNotEmpty()){
//                    iviPanCard.setEditStr(it.pan_card)
//                }


            }
        }
    }


    override fun initView() {
        vb.apply {
            toolbar.apply {
                tvTitle.text = str(R.string.paisa_personal_information)
                ivExit.setOnClickListener { finish() }
            }
            ivrGender.setOnCheckListener {
                options?.gender_options?.let { education ->
                    userDataOneModel.gender = education.getHashMapByValue(ivrGender.getCurrSelect())
                }
            }

            ivsForeverAddress.setOnSelectListener {
                showPopupWindow()
            }
            ivIdcardFront.setOnClickListener {
                paisa_CommDialog(this@paisa_PersonalActivityPaisa).setTitle(str(R.string.paisa_PictureLoadModal_first_title_1))
                    .setImage(paisa_ApiServices.ktpImage)
                    .setMessage("${str(R.string.paisa_PictureLoadModal_first_title_2)}\n${str(R.string.paisa_PictureLoadModal_first_title_3)}")
                    .setonConfirm {
                        takePhoto(true)
                    }.show()


            }

            ivIdcardBack.setOnClickListener {
                paisa_CommDialog(this@paisa_PersonalActivityPaisa).setTitle(str(R.string.paisa_PictureLoadModal_third_title_1))
                    .setImage(paisa_ApiServices.workPermitImage)
                    .setMessage("${str(R.string.paisa_PictureLoadModal_third_title_2)}\n${str(R.string.paisa_PictureLoadModal_third_title_3)}")
                    .setonConfirm {
                        takePhoto(false)
                    }.show()


            }


            nextBtn.setOnClickListener {
                uploadUserDataOne()
            }


            ivsEducation.setOnSelectListener {

                options?.education_degree_options?.let {
                    showCommSelect(str(R.string.paisa_education),
                        it.map2MenuItem(),
                        object : paisa_SelectPoPView.OnPOPSelected {
                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
                                ivsEducation.setSelectStr(itemModel.menuName)
                                userDataOneModel!!.education_degree = itemModel.menuCode
                            }
                        })
                }

            }



            ivsWorType.setOnSelectListener {

                options?.job_category_options?.let {
                    showCommSelect(str(R.string.paisa_wor_type),
                        it.map2MenuItem(),
                        object : paisa_SelectPoPView.OnPOPSelected {
                            override fun OnSelected(itemModel: MenuItemModel, position: Int) {
                                ivsWorType.setSelectStr(itemModel.menuName)
                                userDataOneModel.job_category = itemModel.menuCode

                            }
                        })
                }

            }

            ivsBirth.setOnSelectListener {


                val calendar: Calendar = Calendar.getInstance()

                DatePickerDialog(
                    this@paisa_PersonalActivityPaisa,
                    THEME_HOLO_LIGHT,
                    { _, year, month, day ->
//月份从0开始 需要+1
                        val dateStr = "$year-${month + 1}-$day"
                        ivsBirth.setSelectStr(dateStr)

                        userDataOneModel.birthday = dateStr
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()


            }

            ivsIssue.setOnSelectListener {


                val calendar: Calendar = Calendar.getInstance()

                DatePickerDialog(
                    this@paisa_PersonalActivityPaisa,
                    THEME_HOLO_LIGHT,
                    { _, year, month, day ->
//月份从0开始 需要+1
                        val dateStr = "$year-${month + 1}-$day"
                        ivsIssue.setSelectStr(dateStr)

                        userDataOneModel.issue_date = dateStr

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()


            }


        }


    }


    //    上传个人信息
    private fun uploadUserDataOne() {
        vb.apply {
            userDataOneModel.first_name = iviFirstName.getEditStr()
            userDataOneModel.last_name = iviLastName.getEditStr()
            userDataOneModel.id_no = iviId.getEditStr()
//            userDataOneModel.age = iviAge.getEditStr()
            userDataOneModel.email = iviEmail.getEditStr()
            userDataOneModel.facebook_account = iviFacebookEmail.getEditStr()
            userDataOneModel.family_detail_address = iviFullAddress.getEditStr()
//            userDataOneModel.pan_card = iviPanCard.getEditStr()


            if (SpRepository.idPhoto.isNotEmpty()) {
                userDataOneModel.idcard_front = SpRepository.idPhoto
            }

            if (SpRepository.idPhotoHand.isNotEmpty()) {
                userDataOneModel.idcard_hand = SpRepository.idPhotoHand
            }


            userDataOneModel.gender = if (ivrGender.isMail()) "1" else "2"


            options?.education_degree_options?.let { education ->
                ivsEducation.getSelectStr()?.let {
                    userDataOneModel.education_degree = education.getHashMapByValue(it)
                }

            }

            options?.job_category_options?.let { education ->
                ivsWorType.getSelectStr()?.let {
                    userDataOneModel.job_category = education.getHashMapByValue(it)
                }

            }


            if (userDataOneModel.idcard_hand.isEmpty()) {
                R.string.paisa_please_upload_id_card.show()
                return
            }



            if (userDataOneModel.birthday.isEmpty()) {
                R.string.paisa_please_birthday.show()
                return
            }
            if (userDataOneModel.issue_date.isEmpty()) {
                R.string.paisa_please_issue.show()
                return
            }



            if (userDataOneModel.idcard_front.isEmpty()) {
                R.string.paisa_please_upload_id_card.show()
                return
            }

            if (userDataOneModel.first_name.isEmpty()) {
                R.string.paisa_please_input_firstname.show()
                return
            }


            if (!iviFirstName.getEditStr().isUsername()) {
                R.string.paisa_please_confirm_your_first_name.show()
                return
            }
            if (userDataOneModel.last_name.isEmpty()) {
                R.string.paisa_please_input_lastname.show()
                return
            }
            if (!iviLastName.getEditStr().isUsername()) {
                R.string.paisa_please_confirm_your_last_name.show()
                return
            }

            if (userDataOneModel.id_no.isEmpty()) {
                R.string.paisa_please_input_id_no.show()
                return
            }

//            if (userDataOneModel.id_no.length != 12) {
//                R.string.please_confirm_your_ID_number.show()
//                return
//            }

            if (userDataOneModel.gender.isEmpty()) {
                R.string.paisa_please_choose_gender.show()
                return
            }

            if (userDataOneModel.education_degree.isEmpty()) {
                R.string.paisa_please_choose_education.show()
                return
            }
            if (userDataOneModel.job_category.isEmpty()) {
                R.string.paisa_please_choose_work_type.show()
                return
            }
            if (userDataOneModel.email.isEmpty()) {
                R.string.paisa_please_input_email.show()
                return
            }

            if (userDataOneModel.facebook_account.isEmpty()) {
                R.string.paisa_please_input_facebook_account.show()
                return
            }

            if (userDataOneModel.facebook_account.isEmpty()) {
                R.string.paisa_please_choose_a_permanent_resident_address.show()
                return
            }
            if (userDataOneModel.family_detail_address.isEmpty()) {
                R.string.paisa_please_input_full_address.show()
                return
            }
//            if (userDataOneModel.pan_card.isEmpty() || !userDataOneModel.pan_card.isPanCard()) {
//                R.string.please_input_pancard.show()
//            }
            vm.uploadUserDataOne(userDataOneModel)
        }


    }


    var selectPoPView: paisa_SelectAddressPoPView? = null
    fun showPopupWindow() {
        hideSoftInputWindow()
        val cityList = cityBeanResults.map { MenuItemModel(it.name, false, it) }
        selectPoPView = paisa_SelectAddressPoPView(this).setTitle(getString(R.string.paisa_forever_address))
            .setOpentionList(cityList)
            .setOnPOPSelectedListener(object : paisa_SelectAddressPoPView.OnPOPSelected {
                override fun onSelected(itemModel: MenuItemModel, currPager: Int) {
                    if (currPager != 4) {
                        vm.getCityList(itemModel.cityModel?.id.toString())
                    }
                }

                override fun onTabSelected(itemModel: MenuItemModel?, currPager: Int) {
                    itemModel?.let {
                        vm.getCityList(it.cityModel?.parent_id.toString())
                    }
                }

                override fun onSelectedFinished(
                    addressStr: String, map: HashMap<String, MenuItemModel>
                ) {
                    userDataOneModel!!.let {
                        it.family_province = map["1"]?.menuName ?: ""
                        it.family_city = map["2"]?.menuName ?: ""
                        it.family_area = map["3"]?.menuName ?: ""
                        it.family_town = map["4"]?.menuName ?: ""

                    }
                    vb.ivsForeverAddress.setSelectStr(addressStr)
                }
            })

        selectPoPView?.showPopupWindow(vb.llMain, this)
        selectPoPView?.setOnDismissListener {
            selectPoPView?.changeBackground(1F, this)
            vm.getCityList("0")
        }

    }


    private fun takePhoto(isFront: Boolean) {
        requestPermission(
            arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {

            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(paisa_GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                .maxSelectNum(1)// 最大图片选择数量
                .setLanguage(LanguageConfig.ENGLISH).minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                .isPreviewImage(true)// 是否可预览图片
                .isCamera(true)// 是否显示拍照按钮
                .isCompress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {
                        if (result != null && result.size > 0) {
                            val localMedia = result[0]
                            val imgPath = if (localMedia.isCut && !localMedia.isCompressed) {
                                /* 裁剪过*/
                                localMedia.cutPath
                            } else if (localMedia.isCompressed || localMedia.isCut && localMedia.isCompressed) {
                                /* 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准*/
                                localMedia.compressPath
                            } else {
                                /* 原图*/
                                localMedia.path
                            }
                            val file = File(imgPath)
                            if (file.exists()) {

                                if (isFront) {
                                    Glide.with(this@paisa_PersonalActivityPaisa).load(file)
                                        .into(vb.ivIdcardFront)
                                    vm.uploadImg(file)
                                } else {
                                    Glide.with(this@paisa_PersonalActivityPaisa).load(file)
                                        .into(vb.ivIdcardBack)
                                    vm.uploadImgBack(file)
                                }


                            }


                        }
                    }

                    override fun onCancel() {

                    }

                })


        }


    }


}