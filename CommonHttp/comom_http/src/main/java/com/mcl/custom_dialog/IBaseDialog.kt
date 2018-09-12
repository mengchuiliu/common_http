package com.mcl.custom_dialog

import android.content.DialogInterface

/**
 * @author MengChuiLiu
 */
interface IBaseDialog {
    /**
     * 显示对话框
     */
    fun show()

    /**
     * 取消对话框
     */
    fun dismiss()

    /**
     * 对象销毁
     */
    fun onDestroy()

    /**
     * 点击返回键监听
     */
    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener)
}
