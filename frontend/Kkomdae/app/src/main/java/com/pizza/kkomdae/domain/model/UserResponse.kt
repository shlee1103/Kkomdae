package com.pizza.kkomdae.domain.model

import com.pizza.kkomdae.data.model.dto.UserRentTestRes

data class UserResponse(
    val onGoingTestId : Int,
    val stage : Int,
    val picStage : Int,
    val userRentTestRes : List<UserRentTestRes>
)
