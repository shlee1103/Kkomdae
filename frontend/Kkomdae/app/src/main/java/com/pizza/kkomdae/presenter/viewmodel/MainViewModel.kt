package com.pizza.kkomdae.presenter.viewmodel

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pizza.kkomdae.MainActivity
import com.pizza.kkomdae.domain.model.LoginResponse
import com.pizza.kkomdae.domain.model.UserResponse
import com.pizza.kkomdae.domain.usecase.LoginUseCase
import com.pizza.kkomdae.domain.usecase.MainUseCase
import com.pizza.kkomdae.presenter.model.UserInfoResponse
import com.pizza.kkomdae.presenter.model.UserRentTestResponse
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "MainViewModel"
@HiltViewModel
class MainViewModel@Inject constructor(
    private val mainUseCase: MainUseCase
): ViewModel() {

    private val _userInfoResult = MutableLiveData<UserInfoResponse>()
    val userInfoResult: LiveData<UserInfoResponse>
        get() = _userInfoResult


    fun getUserInfo(){
        viewModelScope.launch {
            val result = mainUseCase.getUserInfo()
            Log.d(TAG, "getUserInfo: $result")

            result.onSuccess { loginResponse ->
                // 로그인 성공 시 실제 데이터 처리
                loginResponse?.let {
                    val userRentTestRes = it.userRentTestRes

                    val data = UserInfoResponse(
                        onGoingTestId = it.onGoingTestId,
                        stage = it.stage,
                        picStage = it.picStage,
                        userRentTestRes = it.userRentTestRes.map {
                            UserRentTestResponse(
                                modelCode = it.modelCode,
                                dateTime = it.dateTime,
                                release = it.release,
                                rentPdfName = it.rentPdfName,
                                releasePdfName = it.releasePdfName,
                                onGoingTestId = it.onGoingTestId,
                                stage = it.stage,
                                picStage = it.picStage
                            )
                        }
                        )
                    _userInfoResult.postValue(data)


                }


            }.onFailure { exception ->
                // 로그인 정보 불러오기 실패

            }

        }
    }


}