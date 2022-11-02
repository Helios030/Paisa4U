package com.paisa.four_u.ui.view.item

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.paisa.four_u.R
import com.paisa.four_u.databinding.ItemViewShowBinding

class paisa_ItemViewShow(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val vb by lazy { ItemViewShowBinding.inflate(LayoutInflater.from(context)) }
    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ItemViewShow)
        initAttr(ta)
        ta.recycle()
        addView(vb.root)
    }

    private fun initAttr(ta: TypedArray) {
        vb.tvText.text = ta.getString(R.styleable.ItemViewShow_tv_text) ?: ""
    }


}