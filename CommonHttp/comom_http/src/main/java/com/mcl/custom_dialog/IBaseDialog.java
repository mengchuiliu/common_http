package com.mcl.custom_dialog;

/**
 * @author MengChuiLiu
 */
public interface IBaseDialog {
    /**
     * 显示对话框
     */
    void show();

    /**
     * 取消对话框
     */
    void dismiss();

    /**
     * 对象销毁
     */
    void onDestroy();
}
