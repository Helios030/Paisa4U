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
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paisa.four_u.R
import com.paisa.four_u.databinding.LayoutAddressSelectPopwindowBinding
import com.paisa.four_u.model.MenuItemModel
import com.paisa.four_u.util.expand.onClickListener


class paisa_SelectAddressPoPView(context: Context?) : PopupWindow(context!!) {

    var mContext: Context? = context
    private var density: Float? = 1.0f
    var tvTitle: TextView? = null
    var rvMenu: RecyclerView? = null
    var menuList = mutableListOf<MenuItemModel>()
    var currIndex:Int=0
    var adapter: paisa_MenuAdapter? = null
    var currMenuModel: MenuItemModel? = null

    //定义ViewModelStore变量
    private var mViewModelStore: ViewModelStore = ViewModelStore()

    companion object {
        const val ADDRESS_LEVEL_1 = 1
        const val ADDRESS_LEVEL_2 = 2
        const val ADDRESS_LEVEL_3 = 3
        const val ADDRESS_LEVEL_4 = 4
    }

    val binding:LayoutAddressSelectPopwindowBinding


    //初始化 pop window
    private fun initPopupWindow() {
        animationStyle = R.style.popwindowAnim //设置动画
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(BitmapDrawable())
        density = mContext?.resources?.displayMetrics?.density
        currIndex=ADDRESS_LEVEL_1
    }

    var addressmap = HashMap<String, MenuItemModel>()


    private fun initMenuAdapter(rvMenu: RecyclerView?) {
        rvMenu?.let { rv ->
            adapter = paisa_MenuAdapter(R.layout.item_pop_menu, menuList)
            rv.layoutManager = LinearLayoutManager(mContext)
            rv.adapter = adapter
            adapter?.setOnItemClickListener { _, _, position ->
                menuList.forEach { it.isSelected = false }
                currMenuModel = menuList[position]
                currMenuModel?.isSelected = true
                adapter?.notifyDataSetChanged()
                addressmap[currIndex.toString()] = currMenuModel!!
                listener?.onSelected(currMenuModel!!, currPager =  currIndex )


                when (currIndex) {
                    ADDRESS_LEVEL_1 -> {
                        binding.tvTab1.text=currMenuModel?.menuName
                    }
                    ADDRESS_LEVEL_2 -> {
                        binding.tvTab2.text=currMenuModel?.menuName
                        binding.tvTab2.visibility =View.VISIBLE

                    }
                    ADDRESS_LEVEL_3 -> {
                        binding.tvTab3.text=currMenuModel?.menuName
                        binding.tvTab3.visibility =View.VISIBLE
                    }
                    ADDRESS_LEVEL_4 -> {
                        binding.tvTab4.text=currMenuModel?.menuName
                        binding.tvTab4.visibility =View.VISIBLE
                    }

                }


                currIndex++
                if(currIndex >ADDRESS_LEVEL_4){

                   finishSelect()
                }


            }
        }
    }

    private fun finishSelect() {

        val addressSB=StringBuilder()
        addressmap.forEach {
            addressSB.append("${it.value.menuName} ")
        }

        listener?.onSelectedFinished(addressSB.toString(),addressmap)

        android.os.Handler(Looper.getMainLooper()).postDelayed({ dismiss()},200)
    }


    fun changeBackground(alpha: Float, activity: Activity) {
        val lp: WindowManager.LayoutParams? = activity.window?.attributes
        lp?.alpha = alpha
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        activity.window?.attributes = lp
    }



    /**
     * 显示popupWindow
     */
    fun showPopupWindow(parent: View, activity: Activity) {
        if (!this.isShowing) {
            showAtLocation(
                parent,
                Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL,
                0,
                0
            )
            changeBackground(0.5F, activity)
        } else {
            dismiss()
        }

        this.setOnDismissListener {
            changeBackground(1F, activity)
        }
    }


    fun setOpentionList(list: List<MenuItemModel>): paisa_SelectAddressPoPView {

//        有的地址没有4级 提前结束
        if(list.isEmpty()){
            finishSelect()
        }else{
            menuList.clear()
            menuList.addAll(list)
            adapter?.notifyDataSetChanged()

        }

        return this
    }


    var listener: OnPOPSelected? = null

    fun setOnPOPSelectedListener(listener: OnPOPSelected): paisa_SelectAddressPoPView {
        this.listener = listener
        return this
    }

    fun setTitle(title: String): paisa_SelectAddressPoPView {
        tvTitle?.text = title
        return this

    }


    interface OnPOPSelected {
        fun onSelected(itemModel: MenuItemModel, currPager: Int)

        fun onTabSelected(itemModel: MenuItemModel?, currPager: Int)

        fun onSelectedFinished(addressStr:String, map:HashMap<String, MenuItemModel>)

    }

    init {
        initPopupWindow()
        binding=  LayoutAddressSelectPopwindowBinding.inflate(LayoutInflater.from(context))
        contentView = binding.root
        tvTitle = contentView.findViewById(R.id.tv_title)
        rvMenu = contentView.findViewById(R.id.rv_menu)
        initMenuAdapter(rvMenu)
        height = (400 * density!!).toInt()
        width = (375 * density!!).toInt()
        tvTitle?.onClickListener {
            dismiss()
        }
        binding.tvTab1.onClickListener {
            currIndex=ADDRESS_LEVEL_1
            listener?.onTabSelected(addressmap[currIndex.toString()],currIndex)
            binding.tvTab2.visibility =View.GONE
            binding.tvTab3.visibility =View.GONE
            binding.tvTab4.visibility =View.GONE
        }
        binding.tvTab2.onClickListener {
            currIndex=ADDRESS_LEVEL_2
            binding.tvTab3.visibility =View.GONE
            binding.tvTab4.visibility =View.GONE
            listener?.onTabSelected(addressmap[currIndex.toString()],currIndex)
        }
        binding.tvTab3.onClickListener {
            currIndex=ADDRESS_LEVEL_3
            listener?.onTabSelected(addressmap[currIndex.toString()],currIndex)
            binding.tvTab4.visibility =View.GONE
        }
    }


}