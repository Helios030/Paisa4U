package com.paisa.four_u.ui.view.popview

import android.app.Activity
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paisa.four_u.R
import com.paisa.four_u.databinding.LayoutSelectPopwindowBinding
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.util.Slog
import com.paisa.four_u.util.expand.onClickListener


class paisa_SelectPoPView(context: Context?) : PopupWindow(context!!) {

    var mContext: Context? = context
    private var density: Float? = 1.0f

    var tvTitle: TextView? = null
    var rvMenu: RecyclerView? = null
    var rvMenu2: RecyclerView? = null
    var menuList = mutableListOf<MenuItemModel>()
    var menuList2 = mutableListOf<MenuItemModel>()


    var adapter: paisa_MenuAdapter? = null
    var adapter2: paisa_MenuAdapter? = null
    var currMenuModel:MenuItemModel?=null
    var isSingin=true
    fun setIsSingin(bool:Boolean):paisa_SelectPoPView{
        this.isSingin=bool
        return this
    }

    private fun initMenuAdapter(rvMenu: RecyclerView?) {
        rvMenu?.let { rv ->
            adapter = paisa_MenuAdapter(R.layout.item_pop_menu, menuList)
            rv.layoutManager = LinearLayoutManager(mContext)
            rv.adapter = adapter
            adapter?.setOnItemClickListener { _, _, position ->
                menuList.forEach { it.isSelected = false }
                currMenuModel=menuList[position]
                currMenuModel?.isSelected = true
                adapter?.notifyDataSetChanged()
                listener?.OnSelected(currMenuModel!!,position)
                Slog.d("是否关闭  $isSingin")
                if(isSingin){
                    android.os.Handler(Looper.getMainLooper()).postDelayed({ dismiss()},200)
                }
            }
        }
    }

    private fun initMenuAdapter2(rvMenu2: RecyclerView?) {
        rvMenu2?.let { rv ->
            adapter2 = paisa_MenuAdapter(R.layout.item_pop_menu, menuList2)
            rv.layoutManager = LinearLayoutManager(mContext)
            rv.adapter = adapter2
            adapter2?.setOnItemClickListener { _, _, position ->
                menuList2.forEach { it.isSelected = false }
                menuList2[position].isSelected = true
                adapter2?.notifyDataSetChanged()
                listener?.OnSelected( menuList2[position],position = position)
            }
        }
    }


     fun changeBackground(alpha: Float, activity: Activity) {
        val lp: WindowManager.LayoutParams? = activity.window?.attributes
        lp?.alpha = alpha
        activity.window?.attributes = lp
    }


    //初始化pop window
    private fun initPopupWindow() {
        animationStyle = R.style.popwindowAnim //设置动画
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())
        density = mContext?.resources?.displayMetrics?.density
    }

    /**
     * 显示popupWindow
     */
    fun showPopupWindow(parent: View, activity: Activity) {
        if (!isShowing) {

            showAtLocation(
                parent,
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                0,
                0
            )
            changeBackground(0.7F, activity)
        } else {
            dismiss()
        }

        this.setOnDismissListener {
            changeBackground(1F, activity)
        }
    }


    fun setOpentionList(list: List<MenuItemModel>): paisa_SelectPoPView {
        menuList.clear()
        menuList.addAll(list)
        adapter?.notifyDataSetChanged()

        return this
    }
    fun setOpentionList2(list2: List<MenuItemModel>): paisa_SelectPoPView {
        rvMenu2?.visibility=View.VISIBLE
        menuList2.clear()
        list2.let {
            menuList2.addAll(it)
        }
        adapter2?.notifyDataSetChanged()
        return this
    }



    var listener: OnPOPSelected? = null

    fun setOnPOPSelectedListener(listener: OnPOPSelected): paisa_SelectPoPView {
        this.listener = listener
        return this
    }

    fun setTitle(title: String):paisa_SelectPoPView {
        tvTitle?.text=title
        return this

    }



    interface OnPOPSelected {
        fun OnSelected(itemModel: MenuItemModel, position: Int)
    }

    init {
        initPopupWindow()
        val binding by lazy { LayoutSelectPopwindowBinding.inflate(LayoutInflater.from(context)) }
        contentView = binding.root
        tvTitle = contentView.findViewById(R.id.tv_title)
        rvMenu = contentView.findViewById(R.id.rv_menu)
        rvMenu2 = contentView.findViewById(R.id.rv_menu_2)
        initMenuAdapter(rvMenu)
        initMenuAdapter2(rvMenu2)
        height = (312 * density!!).toInt()
        width = (375 * density!!).toInt()
        tvTitle?.onClickListener {
            dismiss()
        }
    }


}