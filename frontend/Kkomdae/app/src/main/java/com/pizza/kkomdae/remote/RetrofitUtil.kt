package com.pizza.kkomdae.remote

import com.pizza.kkomdae.base.ApplicationClass

class RetrofitUtil {
    companion object{
        val loginService = ApplicationClass.retrofit.create(LoginService::class.java)
    }
}