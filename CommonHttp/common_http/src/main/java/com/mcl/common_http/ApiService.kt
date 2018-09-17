package com.mcl.common_http

import com.mcl.model.LoginInfo
import com.mcl.model.ResultData
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT

interface ApiService {
    @PUT("Tran")
    fun login(@Header("Support") Support: String, @Body param: RequestBody): Observable<ResultData<LoginInfo>> //登录接口
}