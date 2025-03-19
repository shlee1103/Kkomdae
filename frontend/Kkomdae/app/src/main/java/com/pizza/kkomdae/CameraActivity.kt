package com.pizza.kkomdae

import android.os.Bundle
import android.view.View
import com.pizza.kkomdae.base.BaseActivity
import com.pizza.kkomdae.databinding.ActivityCameraBinding
import com.pizza.kkomdae.ui.guide.BackShotGuideFragment
import com.pizza.kkomdae.ui.guide.FrontShotGuideFragment
import com.pizza.kkomdae.ui.guide.KeypadGuideFragment
import com.pizza.kkomdae.ui.guide.LeftGuideFragment
import com.pizza.kkomdae.ui.guide.RightGuideFragment
import com.pizza.kkomdae.ui.guide.ScreenShotGuideFragment
import com.pizza.kkomdae.ui.step1.ResultFragment

class CameraActivity : BaseActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        // ✅ 상태바 제거 (전체 화면 모드)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        setContentView(binding.root)
        changeFragment(1)

    }

    fun moveToBack(){
        finish()
    }

    fun changeFragment(type: Int){
        when(type){
            0->{ // 촬영 확인
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_camera, ResultFragment())
                    .addToBackStack("sadfa")
                    .commit()
            }
            1->{ // 전면부 촬영 가이드
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_camera, FrontShotGuideFragment())
                    .addToBackStack("sadfa")
                    .commit()
            }
            2->{ // 후면부 촬영 가이드
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_camera, BackShotGuideFragment())
                    .addToBackStack("sadfa")
                    .commit()
            }
            3->{ // 좌측 촬영 가이드
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_camera, LeftGuideFragment())
                    .addToBackStack("sadfa")
                    .commit()
            }
            4->{ // 우측 촬영 가이드
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_camera, RightGuideFragment())
                    .addToBackStack("sadfa")
                    .commit()
            }5->{ // 화면 촬영 가이드
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_camera, ScreenShotGuideFragment())
                .addToBackStack("sadfa")
                .commit()
            }6->{ // 키패드 촬영 가이드
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_camera, KeypadGuideFragment())
                .addToBackStack("sadfa")
                .commit()
        }

        }
    }


}