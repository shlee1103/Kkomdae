package com.pizza.kkomdae.data.source.remote

import com.pizza.kkomdae.data.model.dto.step4.FourthStageRequestDto
import com.pizza.kkomdae.data.model.dto.step4.GetAiPhotoResponseDto
import com.pizza.kkomdae.data.model.dto.step4.GetPdUrlResponseDto
import com.pizza.kkomdae.data.model.dto.step4.GetTotalResultResponseDto
import com.pizza.kkomdae.data.model.dto.step4.PostRePhotoResponseDto
import com.pizza.kkomdae.data.model.dto.step2.PostResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FinalService {

    // 로그인 토큰 보내기
    @POST("api/pdf/{testId}")
    suspend fun postPdf(@Path("testId") testId: Long ) : PostResponseDto

    @GET("api/ai-photo")
    suspend fun getAiPhoto( @Query("testId") testId: Long): GetAiPhotoResponseDto

    @POST("api/fourthStage")
    suspend fun postFourthStage( @Body fourthStageRequest: FourthStageRequestDto): PostResponseDto

    @GET("api/laptopTotalResult")
    suspend fun getLaptopTotalResult(@Query("testId") testId: Long): GetTotalResultResponseDto

    @GET("api/test-file/{file-name}")
    suspend fun getPdfUrl(@Path("file-name") name: String): GetPdUrlResponseDto

    @Multipart
    @POST("api/re-photo")
    suspend fun postRePhoto(
        @Query("photoType") photoType: Int,
        @Query("testId") testId: Long,
        @Part file: MultipartBody.Part,
        ): PostRePhotoResponseDto


}