package com.paisa.four_u.ui.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.paisa.four_u.R
import com.paisa.four_u.databinding.LoadingBinding


class paisa_Loading(context: Context) : AlertDialog(context, R.style.Loading) {

    private val binding by lazy { LoadingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val layoutParams = binding.root.layoutParams as FrameLayout.LayoutParams
        layoutParams.width = 125
        layoutParams.height = 125
        binding.root.layoutParams = layoutParams
        setCancelable(false)
    }
}