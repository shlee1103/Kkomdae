package com.pizza.kkomdae.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyAndroidViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    private val _myPageOrderId = MutableLiveData<Int>()
    val myPageOrderId: LiveData<Int>
        get() = _myPageOrderId

    private val _step = MutableLiveData<Int?>()
    val step: LiveData<Int?>
        get() = _step


    private val _frontUri = MutableLiveData<Uri?>()
    val frontUri: LiveData<Uri?>
        get() = _frontUri

    private val _backUri = MutableLiveData<Uri?>()
    val backUri: LiveData<Uri?>
        get() = _backUri

    private val _leftUri = MutableLiveData<Uri?>()
    val leftUri: LiveData<Uri?>
        get() = _leftUri

    private val _rightUri = MutableLiveData<Uri?>()
    val rightUri: LiveData<Uri?>
        get() = _rightUri

    private val _screenUri = MutableLiveData<Uri?>()
    val screenUri: LiveData<Uri?>
        get() = _screenUri

    private val _keypadUri = MutableLiveData<Uri?>()
    val keypadUri: LiveData<Uri?>
        get() = _keypadUri

    // ✅ 사진 저장 메서드
    fun setFront(uri: Uri) {
        _frontUri.value = uri
    }

    fun setBack(uri: Uri) {
        _backUri.value = uri
    }

    fun setLeft(uri: Uri) {
        _leftUri.value = uri
    }

    fun setRight(uri: Uri) {
        _rightUri.value = uri
    }

    fun setScreen(uri: Uri) {
        _screenUri.value = uri
    }

    fun setKeypad(uri: Uri) {
        _keypadUri.value = uri
    }

    // ✅ 사진 저장 메서드
    fun setStep(step: Int) {
        _step.value = step
    }

}
