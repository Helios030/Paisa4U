package com.paisa.four_u.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.paisa.four_u.R
import com.paisa.four_u.databinding.LayoutProgressViewBinding

/**
 * @Author Ben
 * @Date 2022/4/19 11:00
 * @desc:
 */
class paisa_ProgressView constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {
    companion object {
        private const val first = 0x1
        private const val second = 0x2
        private const val third = 0x3
        private const val fourth = 0x4
    }


    private var pageIndex: Int = first
    private val binding by lazy { LayoutProgressViewBinding.inflate(LayoutInflater.from(context)) }

    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
        pageIndex = ta.getInt(R.styleable.ProgressView_page, first)
        binding.apply {
            when (pageIndex) {
                second -> {
                    ivUser.setImageResource(R.mipmap.paisa_icon_sign_user)
                    ivContent.setImageResource(R.mipmap.paisa_icon_sign_contacts_ing)
                }
//                third -> {
//                    ivUser.setImageResource(R.mipmap.icon_sign_user)
//                    ivContent.setImageResource(R.mipmap.icon_sign_contacts_success)
////                    ivPhoto.setImageResource(R.mipmap.icon_sign_picture_ing)
//
//                }
                fourth -> {
                    ivUser.setImageResource(R.mipmap.paisa_icon_sign_user)
//                    ivPhoto.setImageResource(R.mipmap.icon_sign_picture_success)
                    ivContent.setImageResource(R.mipmap.paisa_icon_sign_contacts_success)
                    ivBank.setImageResource(R.mipmap.paisa_icon_sign_bank)

                }

            }
        }

        ta.recycle()
        addView(binding.root)

    }


}