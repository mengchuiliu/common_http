package com.mcl.common_http

import com.mcl.model.LoginInfo
import com.mcl.model.ResultData
import io.reactivex.functions.Function
import okhttp3.RequestBody

class HttpAction private constructor() : HttpApi() {
    companion object {
        /*单例*/
        @Volatile
        private var httpAction: HttpAction? = null

        /*获取单例*/
        val Instance: HttpAction?
            get() {
                if (httpAction == null) {
                    synchronized(HttpAction::class.java) {
                        if (httpAction == null) httpAction = HttpAction()
                    }
                }
                return httpAction
            }
    }

    fun login(observer: ProgressObserver<ResultData<LoginInfo>>, ParamString: String) {
//        val o = apiService.login("SimulateSync",
//                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), ParamString)).map {
//            Function<ResultData<LoginInfo>, MutableList<LoginInfo>> { t ->
//                t.ReturnDataTable
//            }
//        }
        toSubscribe(apiService.login("SimulateSync",
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), ParamString)), observer)
    }
}