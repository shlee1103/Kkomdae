package com.pizza.kkomdae.remote

import com.pizza.kkomdae.data.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {

    // 로그인 토큰 보내기
    @GET("api/sso/login")
    suspend fun getLogin( @Query("code") code: String): Response<LoginResponse>


}