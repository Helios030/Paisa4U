package com.paisa.four_u.ui.loan

import android.view.View
import com.paisa.four_u.R
import com.paisa.four_u.base.paisa_BaseActivity
import com.paisa.four_u.databinding.ActivityLoanBinding
import com.paisa.four_u.model.LoanOptionsModel
import com.paisa.four_u.ui.confirm.paisa_ConfirmActivityPaisa
import com.paisa.four_u.util.paisa_FBEvent
import com.paisa.four_u.util.expand.color
import com.paisa.four_u.util.expand.launch
import com.paisa.four_u.util.expand.str
import com.paisa.four_u.util.slog
import com.xw.repo.BubbleSeekBar
import kotlin.math.roundToInt

class paisa_LoanActivityPaisa : paisa_BaseActivity<ActivityLoanBinding, paisa_LoanVMPaisa>(
    ActivityLoanBinding::inflate, paisa_LoanVMPaisa::class.java
) {
    private var loanOptionsBean = LoanOptionsModel()
    private var currantPage = 0
    private var currantAmount = 0

    override fun initData() {

        vm.apply {
            getLoanOptions()
            liveLoanOptions.observe(this@paisa_LoanActivityPaisa) {
                currantPage = 0
                loanOptionsBean = it
                setSeekBar(currantPage)
            }
        }
        showCustomToast()
    }

    override fun initView() {
        vb.apply {
            toolbar.apply {
                tvTitle.text = str(R.string.paisa_apply_for_loan)
                ivExit.setOnClickListener { finish() }
            }


            bsbMoney.onProgressChangedListener = object : BubbleSeekBar.OnProgressChangedListener {
                override fun onProgressChanged(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
                ) {
                }

                override fun getProgressOnActionUp(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float
                ) {
                }

                override fun getProgressOnFinally(
                    bubbleSeekBar: BubbleSeekBar?,
                    progress: Int,
                    progressFloat: Float,
                    fromUser: Boolean
                ) {
                    currantAmount = progress
                    vb.moneyTxt.text = currantAmount.toString()
                    loanOptionsBean.option_list?.get(currantPage)?.let {
                        val rate: Float = it.rate?.toFloat() ?: 0f
                        val period: Int = it.option_period?.toInt() ?: 0
                        val totalAmount = ((1 - rate * period) * progress).roundToInt()
                        vb.totalTxt.text = it.remind_tip + totalAmount
                    }
                }
            }


            optionRg.setOnCheckedChangeListener { radioGroup, checkedId ->
                when (checkedId) {
                    R.id.option1_rb -> {
                        vb.option1Rb.setTextColor(color(R.color.black))
                        vb.option2Rb.setTextColor(color(R.color.colorPrimary))
                        currantPage = 0
                    }
                    R.id.option2_rb -> {
                        vb.option1Rb.setTextColor(color(R.color.colorPrimary))
                        vb.option2Rb.setTextColor(color(R.color.black))
                        currantPage = 1
                    }
                }
                setSeekBar(currantPage)
            }


            nextBtn.setOnClickListener {
                loanOptionsBean.option_list?.get(currantPage)?.let {
                    paisa_FBEvent.trackEvent_SubmitLoan()
                    launch<paisa_ConfirmActivityPaisa> {
                        putExtra("loan_option_id", it.loan_option_id ?: "")
                        putExtra("apply_amount", currantAmount)
                    }
                }
            }


        }
    }


    private fun setSeekBar(currPage: Int) {
        loanOptionsBean.slog("setSeekBar  ")

        loanOptionsBean.option_list?.let {
            if (it.size > currPage) {
                val optionListBean: LoanOptionsModel.OptionListBean = it[currPage]
                val minValue  = optionListBean.option_min_value
                currantAmount = minValue?.toInt() ?:0
                val min = minValue?.toFloat() ?:0f
                val max = optionListBean.option_max_value?.toFloat() ?:0f
                val sectionCount: Int = if(optionListBean.section_count >= 10){
                    ((max - min) / optionListBean.section_count).roundToInt()
                } else{
                    optionListBean.section_count
                }
                vb.bsbMoney.configBuilder
                    .min(min)
                    .max(max)
                    .sectionCount(sectionCount)
                    .hideBubble()
                    .touchToSeek()
                    .seekBySection()
                    .autoAdjustSectionMark()
                    .build()


                when (it.size) {
                    1 -> {
                        vb.option1Rb.text = it[0].option_period + " "+str(R.string.paisa_day)
                        vb.option2Rb.visibility = View.GONE
                    }
                    2 -> {
                        vb.option1Rb.text = it[0].option_period + " "+str(R.string.paisa_day)
                        vb.option2Rb.text = it[1].option_period + " "+str(R.string.paisa_day)

                    }

                }
            }
        }


    }

}