package com.mcl.commonhttp

import android.app.Application
import android.content.pm.ApplicationInfo

import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * Created by Administrator on 2017/7/25.
 *
 * @author meng
 */

class App : Application() {
    companion object {
        @JvmStatic
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //初始化logger打印
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 是否显示线程信息，默认为ture
                .methodCount(1) // 显示的方法行数，默认为2
                //                .methodOffset(0) // 隐藏内部方法调用到偏移量，默认为0
                .tag("mcl")   // 每个日志的全局标记。默认PRETTY_LOGGER
                .build()

        //正式库不打印log
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return applicationInfo != null && applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            }
        })
    }

}
