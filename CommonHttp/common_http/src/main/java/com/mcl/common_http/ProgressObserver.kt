package com.mcl.common_http

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.orhanobut.logger.Logger
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException

open class ProgressObserver<T>(private val context: Context) : Observer<T>, ProgressCancelListener {
    private var mObserverListener: ObserverListener<T>? = null
    private var mProgressDialogHandler: ProgressDialogHandler? = null
    private var disposable: Disposable? = null

    constructor(mObserverListener: ObserverListener<T>, context: Context, cancelable: Boolean = true) : this(context) {
        this.mObserverListener = mObserverListener
        if (!cancelable) {
            mProgressDialogHandler = null
        } else {
            mProgressDialogHandler = ProgressDialogHandler(context, this)
        }
    }

    private fun showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler?.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG)?.sendToTarget()
        }
    }

    private fun dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler?.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)?.sendToTarget()
            mProgressDialogHandler = null
        }
    }

    override fun onComplete() {
//        dismissProgressDialog()
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        showProgressDialog()
    }

    override fun onNext(t: T) {
        if (mObserverListener != null) {
//            dismissProgressDialog()
            mObserverListener?.onNext(t)
        }
    }

    override fun onError(e: Throwable) {
        dismissProgressDialog()
        if (!isConnected(context)) {
            mObserverListener?.onError("网络连接失败，请稍后重试")
        } else if (e is SocketTimeoutException) {
            mObserverListener?.onError("网络连接失败，请稍后重试")
        } else if (e is ConnectException) {
            mObserverListener?.onError("网络连接失败，请稍后重试")
        } else {
            mObserverListener?.onError(e.message)
            Logger.e("==httpError错误==> %s", e.message)
        }
    }

    //点击返回键取消数据请求
    override fun onCancelProgress() {
        if (disposable != null && !disposable?.isDisposed!!) {
            disposable?.dispose()
        }
    }

    //是否有网络
    private fun isConnected(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission") val info = connectivity.activeNetworkInfo
        if (null != info && info.isConnected) {
            if (info.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }
}