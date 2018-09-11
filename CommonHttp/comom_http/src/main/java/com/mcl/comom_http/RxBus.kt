package com.mcl.comom_http

import com.orhanobut.logger.Logger
import java.util.HashMap

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class RxBus {
    private var mSubscriptionMap: HashMap<Any, CompositeDisposable>? = null
    private val mSubject: Subject<Any> = PublishSubject.create<Any>().toSerialized()

    companion object {
        @Volatile
        private var mRxBus: RxBus? = null

        //单列模式
        val getInstanceBus: RxBus?
            get() {
                if (mRxBus == null) {
                    synchronized(RxBus::class.java) {
                        if (mRxBus == null) {
                            mRxBus = RxBus()
                        }
                    }
                }
                return mRxBus
            }
    }

    /**
     * 发送消息
     */
    fun post(o: Any) {
        mSubject.onNext(o)
    }

    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param <T>
     * @param type
     * @return
    </T> */
    private fun <T> getObservable(type: Class<T>): Flowable<T> {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER).ofType(type)
    }


    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    fun hasObservers(): Boolean {
        return mSubject.hasObservers()
    }

    /**
     * 注册RxBus事件进行订阅
     * @param key 观察者标识key
     * @param eventType 事件泛型类
     * @param action 发送事件接收的Consumer
     */
    fun <T> registerRxBus(key: Any, eventType: Class<T>, action: Consumer<T>) {
        val disposable = getObservable(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action, Consumer { throwable -> Logger.e("RxBusError->", throwable.toString()) })

        if (mSubscriptionMap == null) {
            mSubscriptionMap = HashMap()
        }
        if (mSubscriptionMap!![key] != null) {
            mSubscriptionMap!![key]?.add(disposable)
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            val disposables = CompositeDisposable()
            disposables.add(disposable)
            mSubscriptionMap!![key] = disposables
        }
    }

    /**
     * 取消订阅
     *
     * @param key 观察者处理器标识
     */
    fun unSubscribe(key: Any) {
        if (mSubscriptionMap == null) {
            return
        }
        if (!mSubscriptionMap!!.containsKey(key)) {
            return
        }
        if (mSubscriptionMap!![key] != null) {
            mSubscriptionMap!![key]?.dispose()
        }
        mSubscriptionMap!!.remove(key)
    }

}
