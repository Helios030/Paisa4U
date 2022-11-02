package com.paisa.four_u.ui.view.popview

import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.paisa.four_u.R
import com.paisa.four_u.paisa_RApplication
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.util.expand.color

class paisa_MenuAdapter(layoutResId: Int, data: MutableList<MenuItemModel>) :
    BaseQuickAdapter<MenuItemModel, BaseViewHolder>(layoutResId, data) {

    val sContext= paisa_RApplication.instance

    override fun convert(holder: BaseViewHolder, itemModel: MenuItemModel) {
        holder.setText(R.id.tv_menu, itemModel.menuName)
        val ll_main=  holder.getView<LinearLayout>(R.id.ll_main)
        if (itemModel.isSelected) {
            ll_main.setBackgroundColor(
                color(R.color.colorPrimary)
            )
            holder.setTextColor(R.id.tv_menu,  color(R.color.white))
        }else{
            ll_main .background=  AppCompatResources.getDrawable(sContext,R.drawable.paisa_ripple_bg)
            holder.setTextColor(R.id.tv_menu,color(R.color.alpha_50_black))
        }
    }
}