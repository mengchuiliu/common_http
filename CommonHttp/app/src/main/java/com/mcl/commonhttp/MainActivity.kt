package com.mcl.commonhttp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.fastjson.JSON
import com.mcl.common_http.HttpAction
import com.mcl.common_http.ObserverListener
import com.mcl.common_http.ProgressObserver
import com.mcl.model.LoginInfo
import com.mcl.model.ResultData
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.util.HashMap

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv.setOnClickListener {
            HttpAction.Instance?.login(ProgressObserver(object : ObserverListener<ResultData<LoginInfo>> {
                override fun onNext(data: ResultData<LoginInfo>) {
                    data.ReturnDataTable
                    Logger.e("==1111==> %s", data.toString())
                }

                override fun onError(msg: Any?) {
                    Logger.e("==222==> %s", msg)
                }
            }, this@MainActivity), getParam(mutableListOf<Any>().apply {
                add("mcl")
                add(getMD5Str("12345678"))
                add("933f63a173540f09b587fb7f95625bbb")
                add("44")
                add("2")
                add("190e35f7e04f80bbc8e")
            }, "UserLogin"))
        }
    }


    /*
     * MD5加密
     */
    fun getMD5Str(str: String): String {
        var messageDigest: MessageDigest? = null
        try {
            messageDigest = MessageDigest.getInstance("MD5")
            messageDigest!!.reset()
            messageDigest.update(str.toByteArray(charset("UTF-8")))
        } catch (e: Exception) {
            println("MD5 加密异常")
        }

        assert(messageDigest != null)
        val byteArray = messageDigest!!.digest()
        val md5StrBuff = StringBuilder()
        for (aByteArray in byteArray) {
            if (Integer.toHexString(0xFF and aByteArray.toInt()).length == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF and aByteArray.toInt()))
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF and aByteArray.toInt()))
            }
        }
        // 16位加密，从第9位到25位
        // return md5StrBuff.substring(8, 24).toString().toUpperCase();
        // 32位大写MD5加密
        return md5StrBuff.toString()
    }

    //通用参数组装
    fun getParam(list: MutableList<Any>, tranName: String): String {
        val map = HashMap<String, Any>()
        map["Action"] = "Default"
        map["DBMarker"] = "DB_CFS_Loan"
        map["Marker"] = "HQServer"
        map["IsUseZip"] = false
        map["Function"] = ""
        map["ParamString"] = list
        map["TranName"] = tranName
        Logger.e("==通用数据参数==> %s", JSON.toJSONString(map))
        return JSON.toJSONString(map)
    }
}
