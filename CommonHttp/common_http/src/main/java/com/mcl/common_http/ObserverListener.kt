package com.mcl.common_http

interface ObserverListener<T> {
    fun onNext(t: T)

    fun onError(msg: Any?)
}