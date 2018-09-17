package com.mcl.custom_dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.mcl.common_http.R


/**
 * 对话框封装
 * @author  MengChuiLiu
 */
class BaseDialog private constructor(private val mBaseDialogParams: BaseDialogParams) : IBaseDialog {
    private var mDialog: Dialog? = null

    init {
        mDialog = Dialog(mBaseDialogParams.context!!, mBaseDialogParams.themeResId)
        if (null != mDialog) {
            mDialog!!.setContentView(mBaseDialogParams.view!!)
            mDialog!!.setCanceledOnTouchOutside(mBaseDialogParams.canceledOnTouchOutside)
            mDialog!!.setCancelable(mBaseDialogParams.cancelable)
        }
    }

    class Builder @JvmOverloads constructor(context: Context, view: View, themeResId: Int = R.style.dialog_transparent) {
        private val mParams: BaseDialogParams

        constructor(context: Context, @LayoutRes resource: Int) : this(context, LayoutInflater.from(context).inflate(resource, null, false)) {}

        init {
            val dm = context.resources.displayMetrics
            mParams = BaseDialogParams()
            mParams.view = view
            mParams.context = context
            mParams.themeResId = themeResId
        }

        fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): Builder {
            mParams.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            mParams.cancelable = cancelable
            return this
        }


        fun setPaddingLeft(left: Int): Builder {
            mParams.paddingLeft = left
            return this
        }

        fun setGravity(gravity: Int): Builder {
            mParams.gravity = gravity
            return this
        }

        /**
         * 设置距离底部的距离
         * 仅在设置 gravity 为 bottom 的时候有效
         *
         * @param bottom
         * @return
         */
        fun setMarginBottom(bottom: Int): Builder {
            mParams.marginBottom = bottom
            return this
        }

        /**
         * 宽度占据屏幕宽度的比例 （0～1）
         *
         * @param widthScale
         * @return
         */
        fun setWidthScale(widthScale: Float): Builder {
            mParams.widthScale = widthScale
            return this
        }

        /**
         * 高度占据屏幕宽度的比例 （0～1）
         *
         * @param heightScale
         * @return
         */
        fun setHeightScale(heightScale: Float): Builder {
            mParams.heightScale = heightScale
            return this
        }

        /**
         * 动画对应的资源文件
         *
         * @param resAnimId
         * @return
         */
        fun setResAnimId(@StyleRes resAnimId: Int): Builder {
            mParams.resAnimId = resAnimId
            return this
        }

        fun create(): BaseDialog {
            return BaseDialog(mParams)
        }
    }

    /**
     * 设置对话框的属性，比如宽度，位置
     *
     * @param dialog
     */
    private fun setCustomAttributes(dialog: Dialog?) {
        if (null != dialog) {
            val window = dialog.window
            if (null != window) {
                val wlp = window.attributes
                val d = window.windowManager.defaultDisplay
                val dm = DisplayMetrics()
                if (null != wlp && null != d) {
                    d.getMetrics(dm)
                    //获取屏幕宽
                    val width = dm.widthPixels
                    //获取屏幕高度
                    val height = dm.heightPixels

                    //设置新的宽度
                    if (mBaseDialogParams.widthScale > 0)
                        wlp.width = (width * mBaseDialogParams.widthScale).toInt()
                    //设置新的高度
                    if (mBaseDialogParams.heightScale > 0)
                        wlp.height = (height * mBaseDialogParams.heightScale).toInt()

                    //宽度按屏幕大小的百分比设置，这里我设置的是全屏显示
                    //wlp.gravity = Gravity.BOTTOM;
                    wlp.gravity = mBaseDialogParams.gravity
                    if (wlp.gravity == Gravity.BOTTOM) {
                        //如果是底部显示，则距离底部的距离
                        val bottom = mBaseDialogParams.marginBottom
                        wlp.y = bottom
                    }
                    window.attributes = wlp
                    if (0 != mBaseDialogParams.resAnimId) {
                        window.setWindowAnimations(mBaseDialogParams.resAnimId)
                    }
                }
            }
        }
    }

    override fun show() {
        if (null != mDialog) {
            mDialog!!.show()
            setCustomAttributes(mDialog)
        }
    }

    override fun dismiss() {
        if (null != mDialog && mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        dismiss()
        mDialog = null
    }

    override fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener) {
        if (null != mDialog) mDialog?.setOnCancelListener(onCancelListener)
    }
}
