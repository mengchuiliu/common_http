package com.mcl.comom_http

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

    fun login() {

    }
}