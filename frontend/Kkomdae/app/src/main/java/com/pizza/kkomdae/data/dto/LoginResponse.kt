package com.pizza.kkomdae.data.dto

data class LoginResponse(
    val jwt : String,
    val refreshToken : String
)
