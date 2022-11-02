package com.paisa.four_u.ui.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.paisa.four_u.R
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.databinding.DialogCommBinding
import com.paisa.four_u.util.expand.onClickListener
import com.paisa.four_u.util.expand.str

/**
 * @Author Ben
 * @Date 2022/4/14 15:27
 * @desc:自定义弹框
 */
class paisa_CommDialog(context: Context) : AlertDialog(context, R.style.AlertDialog) {


    private val binding by lazy { DialogCommBinding.inflate(LayoutInflater.from(context)) }
    var onConfirm: () -> Unit? = {}
    var onCancel: () -> Unit? = {}

    var isSinging=false
    
    val contex =paisa_RApplication.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.root.layoutParams.width = (contex.resources.displayMetrics.widthPixels*0.8).toInt()
        binding.apply {
            tvCancel.onClickListener {
                onCancel()
                dismiss()
            }
            tvConfirm.onClickListener {
                onConfirm()
                dismiss()
            }
        }
    }


    var confirmStr: String? = str(R.string.paisa_ok)

    fun setConfirmStr(confirmStr: String?): paisa_CommDialog {
        this.confirmStr = confirmStr
        return this
    }


  fun   setIsSingin(bool: Boolean): paisa_CommDialog{
        this.isSinging=bool
      return this
    }


    var title: String? = str(R.string.app_name)

    fun setTitle(title: String?): paisa_CommDialog {
        this.title = title
        return this
    }

    var message: String? = null
    fun setMessage(message: String?): paisa_CommDialog {
        this.message = message
        return this
    }

    var imageUri: String? = null
    fun setImage(uri: String?): paisa_CommDialog {
        this.imageUri = uri
        return this
    }






    fun setonConfirm(listener: () -> Unit?): paisa_CommDialog {
        onConfirm = listener
        return this
    }

    fun setonCancel(listener: () -> Unit?): paisa_CommDialog {
        onCancel = listener
        return this
    }

    fun refreshView() {
        if (!TextUtils.isEmpty(title)) {
            binding.tvTitle.text = title
        }
        if (!TextUtils.isEmpty(message)) {
            binding.tvContent.text = message
        }

        if(isSinging){
            binding.tvCancel.visibility = View.GONE
            binding.viewLine.visibility = View.GONE
        }
        binding.tvConfirm.text = confirmStr
        if (!imageUri.isNullOrEmpty()) {
            binding.ivContent.visibility = View.VISIBLE
//todo 缺少图片
            if(imageUri?.contains("file") == true){
                Glide.with(context).load(R.mipmap.paisa_icon_apply_succeed).into(binding.ivContent)
            }else{
                Glide.with(context).load(imageUri).into(binding.ivContent);
            }

        } else {
            binding.ivContent.visibility = View.GONE
        }
    }


    override fun show() {
        super.show()
        refreshView()
    }


}
