package com.pizza.kkomdae.data.model.dto

data class LoginResponse(
    val jwt : String,
    val refreshToken : String
)
