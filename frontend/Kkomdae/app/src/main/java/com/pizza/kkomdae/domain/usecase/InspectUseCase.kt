package com.pizza.kkomdae.domain.usecase

import com.pizza.kkomdae.domain.repository.InspectRepository
import javax.inject.Inject

class InspectUseCase@Inject constructor(
    private val inspectRepository: InspectRepository
)  {
    suspend fun postTest(rentId: Int?): Result<Long> {
        return try {
            val response = inspectRepository.postTest(rentId =rentId )
            Result.success(response)  // ✅ 성공 시 Result.success 반환
        } catch (e: Exception) {
            Result.failure(e)  // ✅ 실패 시 Result.failure 반환
        }
    }
}