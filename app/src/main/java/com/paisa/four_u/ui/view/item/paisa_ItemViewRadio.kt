package com.paisa.four_u.ui.view.item

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.paisa.four_u.R
import com.paisa.four_u.databinding.ItemViewRadioBinding
import com.paisa.four_u.util.expand.color

class paisa_ItemViewRadio(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val vb by lazy { ItemViewRadioBinding.inflate(LayoutInflater.from(context)) }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ItemViewRadio)
        initAttr(ta)
        ta.recycle()
        addView(vb.root)
    }

    private fun initAttr(ta: TypedArray) {
        vb.apply {

            rgSex.setOnCheckedChangeListener { _, checkedId ->
                setChecked(checkedId==R.id.rb_Male)
            }
        }

    }

    var currStr=""
    fun getCurrSelect():String= currStr

    fun setChecked(isFirst:Boolean){
        vb.apply {
            if(isFirst){
                rbMale.isChecked=true
                rbFeMale.isChecked=false

                rbMale.setTextColor(color(R.color.colorPrimary))
                rbFeMale.setTextColor(color(R.color.black_9))
                onCheckListener(rbMale.text.toString())
                currStr=rbMale.text.toString()
            }else{
                rbMale.isChecked=false
                rbFeMale.isChecked=true
                rbMale.setTextColor(color(R.color.black_9))
                rbFeMale.setTextColor(color(R.color.colorPrimary))
                onCheckListener(rbFeMale.text.toString())
                currStr=rbFeMale.text.toString()
            }
        }


    }

    var onCheckListener: (str: String) -> Unit? = {}

    // 一定不要用错
    fun setOnCheckListener(listener: (str: String) -> Unit?): paisa_ItemViewRadio {
        onCheckListener = listener
        return this
    }


    fun isMail():Boolean= vb.rbMale.isChecked

}