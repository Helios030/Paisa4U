package com.paisa.four_u.ui.view.item

import android.content.Context
import android.content.res.TypedArray
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.paisa.four_u.R
import com.paisa.four_u.databinding.ItemViewInputBinding
import com.paisa.four_u.util.expand.onClickListener

class paisa_ItemViewInput (context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private val vb by lazy { ItemViewInputBinding.inflate(LayoutInflater.from(context)) }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ItemViewInput)
        initAttr(ta)
        ta.recycle()
        addView(vb.root)
    }

    private fun initAttr(ta: TypedArray) {
        setTitleStr(ta.getString(R.styleable.ItemViewInput_input_title_text) ?: "")
        setEditStr(ta.getString(R.styleable.ItemViewInput_input_edit_text) ?: "")
        if(ta.getBoolean(R.styleable.ItemViewInput_input_is_number,false) ) vb.etInput.inputType = InputType.TYPE_CLASS_NUMBER
        vb.llSelect.onClickListener {
            onClickListener()
        }
    }

    

    var onClickListener: () -> Unit? = {}

    // 一定不要用错
    fun setOnSelectListener(listener: () -> Unit?): paisa_ItemViewInput {
        onClickListener = listener
        return this
    }


    fun getTitleStr(): String = vb.tvTitle.text.trim().toString()
    fun setTitleStr(str: String) = run { vb.tvTitle.text = str.ifBlank { "" } }


    fun getEditStr(): String = vb.etInput.text.trim().toString()
    fun setEditStr(str: String) = run { vb.etInput.setText(str.ifBlank { "" } )}



}