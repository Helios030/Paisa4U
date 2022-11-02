package com.paisa.four_u.ui.pay.repayment

import android.content.pm.ActivityInfo
import android.text.TextUtils
import android.util.Base64
import android.view.View
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityRepaymentBinding
import com.paisa.four_u.model.RepaymentModel
import com.paisa.four_u.util.paisa_GlideEngine
import com.paisa.four_u.util.expand.openUri
import com.paisa.four_u.util.expand.str
import java.io.File

class paisa_RepaymentActivityPaisa : paisa_BaseActivity<ActivityRepaymentBinding, paisa_RepaymentVMPaisa>(
    ActivityRepaymentBinding::inflate, paisa_RepaymentVMPaisa::class.java
) {

    private val RC_CAMERA_PERM = 101
    private var bankID: String = ""
    private var amount: String = ""
    private var orderNo: String = ""
    private var helpPhone: String = ""
    override fun initData() {

   
        orderNo = intent.getStringExtra("order_no") ?: ""
        helpPhone = intent.getStringExtra("help_phone") ?: ""

        vm.apply {

            getRepaymentData(orderNo)

            liveRepaymentBean.observe(this@paisa_RepaymentActivityPaisa) {
                setDataValue(it)

            }


        }


    }

    override fun initView() {
        vb.apply {
            showCustomToast()
            repaymentHelpTxt.setOnClickListener {
                openUri(str(R.string.paisa_repayment_guidelines),BuildConfig.REPAYMENT_GUIDELINES)
            }

            repaymentVoucherBtn.setOnClickListener {

                selectPhoto()


            }

        }
    }


    private fun setDataValue(repayModel: RepaymentModel) {

        vb.apply {
            bankID = repayModel.bank_id
            amount = repayModel.repay_amount
            ivslOrderNumber.setLineStr(orderNo)
            ivslRepaymentAmount.setLineStr(repayModel.repay_amount)
            ivslRepaymentExpirationTime.setLineStr(repayModel.repay_end_time)
            ivslRepaymentBankName.setLineStr(repayModel.bank_name)
            ivslRepaymentBankCode.setLineStr(repayModel.bank_code)
            ivslReceivingPeople.setLineStr(repayModel.account_name)
            ivslReceivingAccounte.setLineStr(repayModel.account_no)
            ivslRepaymentInformation.setLineStr(repayModel.va_expire_time)
            if (!TextUtils.isEmpty(repayModel.voucher)) {
                voucherImg.visibility = View.VISIBLE

                Glide.with(this@paisa_RepaymentActivityPaisa).load(repayModel.voucher).into(voucherImg)
            } else {
                voucherImg.visibility = View.GONE
            }
        }


    }


    private fun selectPhoto() {
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .imageEngine(paisa_GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
            .isWeChatStyle(true)// 是否开启微信图片选择风格
            .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
            .maxSelectNum(1)// 最大图片选择数量
            .minSelectNum(1)// 最小选择数量
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
            .cutOutQuality(90)// 裁剪输出质量 默认100
            .minimumCompressSize(0)// 小于多少kb的图片不压缩
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    if (result != null && result.size > 0) {
                        val localMedia = result[0]
                        val imgPath =
                            if (localMedia.isCut && !localMedia.isCompressed) {
                                // 裁剪过
                                localMedia.cutPath
                            } else if (localMedia.isCompressed || localMedia.isCut && localMedia.isCompressed) {
                                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                                localMedia.compressPath
                            } else {
                                // 原图
                                localMedia.path
                            }
                        val file = File(imgPath)
                        if (file.exists()) {
                            uploadImg(file)
                            Glide.with(this@paisa_RepaymentActivityPaisa).load(file)
                                .into(vb.voucherImg)
                        }
                    }
                }

                override fun onCancel() {}

            })
    }

    private fun uploadImg(file: File) {
        val map = HashMap<String, Any>()
        map["order_no"] = orderNo
        if (TextUtils.isEmpty(vb.repayMoneyEdt.text.toString())) {
            map["amount"] = amount
        } else {
            map["amount"] = vb.repayMoneyEdt.text.toString()
        }
        map["bank_id"] = bankID
        val pictureMap = HashMap<String, Any?>()
        val type = MimeTypeMap.getFileExtensionFromUrl(file.path)
        val imgData: String = Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
        pictureMap["content"] = imgData
        pictureMap["extension"] = type
        map["picture"] = pictureMap
        vm.uploadVoucher(map)
    }

}