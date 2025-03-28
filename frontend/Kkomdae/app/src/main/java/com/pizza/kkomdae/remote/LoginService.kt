package com.pizza.kkomdae.remote

import com.pizza.kkomdae.data.dto.LoginResponse
import com.pizza.kkomdae.data.dto.RefreshTokenRequest
import com.pizza.kkomdae.data.dto.RefreshTokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    // 로그인 토큰 보내기
    @GET("api/sso/login")
    suspend fun getLogin( @Query("code") code: String): Response<LoginResponse>

    @POST("api/sso/refresh")
    fun postRefreshToken(
        @Body refreshToken: RefreshTokenRequest,
    ): Call<RefreshTokenResponse>


}