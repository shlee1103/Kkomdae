package com.pizza.kkomdae.presenter.model


data class UserInfoResponse(
    val onGoingTestId : Int,
    val stage : Int,
    val picStage : Int,
    val userRentTestRes : List<UserRentTestResponse>
)
