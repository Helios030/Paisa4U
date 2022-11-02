package com.paisa.four_u.ui.view.item

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.paisa.four_u.R
import com.paisa.four_u.databinding.ItemViewShowLineBinding

class paisa_ItemViewShowLine(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val vb by lazy { ItemViewShowLineBinding.inflate(LayoutInflater.from(context)) }
    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ItemViewShowLine)
        initAttr(ta)
        ta.recycle()
        addView(vb.root)
    }

    private fun initAttr(ta: TypedArray) {
        vb.tvLineTitle.text = ta.getString(R.styleable.ItemViewShowLine_tv_line_title) ?: ""
        vb.tvLineValue.text = ta.getString(R.styleable.ItemViewShowLine_tv_line_value) ?: ""
    }

    fun setLineStr(str: String) = run { if(str.isNotEmpty()) vb.tvLineValue.text = str }

    fun getLineStr()=vb.tvLineValue.text.trim().toString()

    fun setTitleLineStr(str: String) = run { if(str.isNotEmpty()) vb.tvLineTitle.text = str }


}