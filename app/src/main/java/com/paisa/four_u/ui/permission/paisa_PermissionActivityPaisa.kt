package com.paisa.four_u.ui.permission

import android.Manifest
import com.paisa.four_u.BuildConfig
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityPermissionBinding
import com.paisa.four_u.ui.paisa_CommVMPaisa
import com.paisa.four_u.util.expand.onClickListener
import com.paisa.four_u.util.expand.openUri
import com.paisa.four_u.util.expand.requestPermission
import com.paisa.four_u.util.expand.str

class paisa_PermissionActivityPaisa : paisa_BaseActivity<ActivityPermissionBinding, paisa_CommVMPaisa>(
    ActivityPermissionBinding::inflate, paisa_CommVMPaisa::class.java
) {
    override fun initData() {
        vb.apply {
            toolbar.apply {
                tvTitle.text = str(R.string.paisa_pp_title)
                ivExit.onClickListener { finish() }
            }
            tvContact.text = str(R.string.paisa_pp_tip2).format(
                str(R.string.app_name),
                BuildConfig.BASE_URL
            )
            tvSms.text = str(R.string.paisa_pp_tip3).format(
                str(R.string.app_name),
                BuildConfig.BASE_URL
            )
            tvSafe.text = str(R.string.paisa_pp_tip6).format(BuildConfig.BASE_URL)
            btnCancel.onClickListener {
                finish()
            }
            
           
            btnConfirm.onClickListener {
                requestPermission(
                    arrayOf(
                        Manifest.permission.READ_SMS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    )
                ) {
                    openUri(str(R.string.paisa_privacy_policy), BuildConfig.PRIVACY_POLICY)
                    finish()
                }

            }


        }

    }

    override fun initView() {
    }

}