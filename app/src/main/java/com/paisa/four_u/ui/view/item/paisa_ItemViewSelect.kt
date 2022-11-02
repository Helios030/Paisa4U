package com.paisa.four_u.ui.view.item

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.paisa.four_u.R
import com.paisa.four_u.databinding.ItemViewSelectBinding
import com.paisa.four_u.util.expand.onClickListener
import com.paisa.four_u.util.expand.str

class paisa_ItemViewSelect(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val vb by lazy { ItemViewSelectBinding.inflate(LayoutInflater.from(context)) }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ItemViewSelect)
        initAttr(ta)
        ta.recycle()
        addView(vb.root)
    }

    private fun initAttr(ta: TypedArray) {
        setTitleStr(ta.getString(R.styleable.ItemViewSelect_title_text) ?: "")
        setSelectStr(ta.getString(R.styleable.ItemViewSelect_select_text) ?: "")
        vb.llSelect.onClickListener {
            onClickListener()
        }
    }

    var onClickListener: () -> Unit? = {}

    // 一定不要用错
    fun setOnSelectListener(listener: () -> Unit?): paisa_ItemViewSelect {
        onClickListener = listener
        return this
    }


    fun getTitleStr(): String = vb.tvTitle.text.trim().toString()
    fun setTitleStr(str: String) = run { vb.tvTitle.text = str.ifBlank { "" } }


    fun getSelectStr(): String? {
        val selectStr=  vb.tvSelect.text.trim().toString()
        if(selectStr!= str(R.string.paisa_please_choose))
            return selectStr else  return null
    }


    fun setSelectStr(str: String) = run { if(str.isNotEmpty()) vb.tvSelect.text = str }


}