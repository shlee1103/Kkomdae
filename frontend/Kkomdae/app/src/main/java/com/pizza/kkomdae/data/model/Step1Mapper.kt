package com.pizza.kkomdae.data.model

import com.pizza.kkomdae.data.model.dto.PhotoResponseDto
import com.pizza.kkomdae.domain.model.PhotoResponse

object Step1Mapper {
    fun toPhotoResponse(photoResponseDto: PhotoResponseDto)= PhotoResponse(
        success = photoResponseDto.success,
        message = photoResponseDto.message
        )
}