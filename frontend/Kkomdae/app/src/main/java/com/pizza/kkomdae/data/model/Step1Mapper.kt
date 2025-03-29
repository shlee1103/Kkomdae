package com.pizza.kkomdae.data.model

import com.pizza.kkomdae.data.model.dto.PhotoResponseDto
import com.pizza.kkomdae.domain.model.PhotoResponse

object Step1Mapper {
    fun toPhotoResponse(photoResponseDto: PhotoResponseDto)= PhotoResponse(
        statusCode = photoResponseDto.statusCode,
        message = photoResponseDto.message
        )
}