package com.pizza.kkomdae.domain.model.login

data class LoginResponse(
    val jwt : String,
    val refreshToken : String
)
