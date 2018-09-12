package com.mcl.comom_http

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

open class HttpApi {
    companion object {
        const val BASE_URL = "http://192.168.31.154:2569/DuiService/"//开发测试
    }

    open lateinit var apiService: ApiService
    private val timeOut = 30L

    init {
        //手动创建一个OkHttpClient并设置超时时间
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(timeOut, TimeUnit.SECONDS)
        builder.writeTimeout(timeOut, TimeUnit.SECONDS)
        builder.readTimeout(timeOut, TimeUnit.SECONDS)
        builder.protocols(listOf(Protocol.HTTP_1_1))
        setSSL(builder)//绕开ssl证书
        val retrofit = Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(NullOnEmptyConverterFactory())//返回string
                .addConverterFactory(GsonConverterFactory.create())//返回格式化gson
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()

        apiService = retrofit.create<ApiService>(ApiService::class.java)
    }

    internal fun <T> toSubscribe(observable: Observable<T>, observer: Observer<T>) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    //设置绕开ssl证书问题
    @SuppressLint("TrustAllX509TrustManager")
    private fun setSSL(builder: OkHttpClient.Builder) {
        try {
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(x509Certificates: Array<X509Certificate>, s: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(x509Certificates: Array<X509Certificate>, s: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }), SecureRandom())
            builder.sslSocketFactory(sc.socketFactory)
            builder.hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, Any>? {
            val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter { body ->
                if (body.contentLength() == 0L) {
                    null
                } else delegate.convert(body)
            }
        }
    }
}