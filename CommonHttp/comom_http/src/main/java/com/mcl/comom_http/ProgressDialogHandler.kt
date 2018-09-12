package com.mcl.comom_http

import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.mcl.custom_dialog.BaseDialog

class ProgressDialogHandler(private val context: Context) : Handler() {
    private var mBaseDialog: BaseDialog? = null
    private var cancelable: Boolean = true
    private var mProgressCancelListener: ProgressCancelListener? = null

    constructor(context: Context, mProgressCancelListener: ProgressCancelListener, cancelable: Boolean = true) : this(context) {
        this.mProgressCancelListener = mProgressCancelListener
        this.cancelable = cancelable
    }

    companion object {
        const val SHOW_PROGRESS_DIALOG = 1
        const val DISMISS_PROGRESS_DIALOG = 2
    }

    private fun showProgressDialog() {
        if (mBaseDialog == null) {
            val builder = BaseDialog.Builder(context, getProgressView())
            builder.setCancelable(cancelable)
            builder.setCanceledOnTouchOutside(true)
            builder.setGravity(Gravity.CENTER)
            mBaseDialog = builder.create()
            if (cancelable) {
                mBaseDialog?.setOnCancelListener(DialogInterface.OnCancelListener {
                    mProgressCancelListener?.onCancelProgress()
                })
            }
            mBaseDialog?.show()
        } else {
            mBaseDialog?.show()
        }
    }

    //隐藏加载框
    private fun dismissProgressDialog() {
        if (mBaseDialog != null) {
            mBaseDialog?.dismiss()
            mBaseDialog = null
        }
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when (msg.what) {
            SHOW_PROGRESS_DIALOG -> showProgressDialog()
            DISMISS_PROGRESS_DIALOG -> dismissProgressDialog()
        }
    }

    //获取加载框视图
    fun getProgressView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null, false)
        val spaceshipImage = view.findViewById(R.id.img) as ImageView
        val tipTextView = view.findViewById(R.id.tipTextView) as TextView// 提示文字
        // 加载动画
        val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation)
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation)
        tipTextView.text = "加载中..."// 设置加载信息
        return view
    }
}