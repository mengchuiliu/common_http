package com.mcl.custom_dialog

/**
 * @author  MengChuiLiu
 * 自定义dialog 点击回调接口
 */
interface BaseDialogInterface {

    /**
     * 点击确定按钮
     */
    interface OnConfirmClickListener {
        fun onConfirm(baseDialog: BaseDialog)
    }

    /**
     * 点击关闭按钮
     */
    interface OnCancelClickListener {
        fun onCancel(baseDialog: BaseDialog)
    }

    /**
     * 点击标题更多
     */
    interface OnMoreClickListener {
        fun onMore(baseDialog: BaseDialog)
    }
}
