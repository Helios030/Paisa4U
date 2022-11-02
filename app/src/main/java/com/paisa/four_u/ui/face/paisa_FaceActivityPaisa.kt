package com.paisa.four_u.ui.face

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.liveness.dflivenesslibrary.DFTransferResultInterface
import com.liveness.dflivenesslibrary.liveness.DFSilentLivenessActivity
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityFaceBinding
import com.paisa.four_u.ui.userData.first.paisa_PersonalActivityPaisa
import com.paisa.four_u.util.expand.launch
import com.paisa.four_u.util.expand.requestPermission
import com.paisa.four_u.util.expand.str
import com.paisa.four_u.util.slog


class paisa_FaceActivityPaisa : paisa_BaseActivity<ActivityFaceBinding, paisa_FaceVMPaisa>(
    ActivityFaceBinding::inflate, paisa_FaceVMPaisa::class.java
) {
    companion object {
        private const val REQUEST_LIVENESS_CODE = 99
    }

    override fun initData() {}

    override fun initView() {
        vb.toolbar.apply {
            tvTitle.text = str(R.string.paisa_str_face)
            ivExit.setOnClickListener { finish() }
        }
        showCustomToast()
        vb.nextBtn.setOnClickListener {
            requestPermission(arrayOf(Manifest.permission.CAMERA)) {
                val bundle = Bundle()
                val intent = Intent()
                intent.setClass(this, DFSilentLivenessActivity::class.java)
                intent.putExtras(bundle)
                //设置返回图片结果
                intent.putExtra(DFSilentLivenessActivity.KEY_DETECT_IMAGE_RESULT, true)
                intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_HAS_FACE, "please keep still")
                intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_NO_FACE, "Please put the face in the circle")
                intent.putExtra(DFSilentLivenessActivity.KEY_HINT_MESSAGE_FACE_NOT_VALID, "please stay away")
                startActivityForResult(intent, REQUEST_LIVENESS_CODE)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val mResult = (this.application as DFTransferResultInterface).result
            //获取关键帧图像
            val imageResultArr = mResult.livenessImageResults;
            if (imageResultArr != null) {
                val size = imageResultArr.size
                if (size > 0) {
                    val imageResult = imageResultArr[0];
                    vm.uploadLiveness(mResult.livenessEncryptResult, imageResult.image)
                }
            }
            launch<paisa_PersonalActivityPaisa> { }
            slog(" 活体检测完成  ")
        } else {
            resultCode.slog("silent liveness cancel，error code")
        }

    }


}