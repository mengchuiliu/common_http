package com.mcl.custom_dialog

import android.content.Context
import android.support.annotation.StyleRes
import android.view.View

/**
 * @author  MengChuiLiu
 * 对话框参数
 */
class BaseDialogParams {
    var context: Context? = null
    var paddingLeft: Int = 0
    var view: View? = null
    var themeResId: Int = 0
    var canceledOnTouchOutside = true
    var cancelable = true
    /**
     * 宽度占据屏幕宽度的比例 （0～1）
     */
    var widthScale = 0.8f

    /**
     * 动画对应的资源文件
     */
    @StyleRes
    var resAnimId: Int = 0

    /**
     * 高度占据屏幕宽度的比例 （0～1） 0.8
     */
    var heightScale: Float = 0f
    /**
     * 对齐方向 Gravity.TOP Gravity.BOTTOM
     */
    var gravity: Int = 0

    /**
     * 具体底部的距离 (如果是底部显示，则距离底部的距离)
     */
    var marginBottom: Int = 0
}
