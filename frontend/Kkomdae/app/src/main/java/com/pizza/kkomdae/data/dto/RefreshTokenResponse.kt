package com.pizza.kkomdae.data.dto

data class RefreshTokenResponse(
    val jwt: String,
    val refreshToken: String
)
