package com.pizza.kkomdae

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pizza.kkomdae.databinding.ActivityCameraBinding
import com.pizza.kkomdae.databinding.ActivityMainBinding
import com.pizza.kkomdae.ui.guide.FontShotGuideFragment
import com.pizza.kkomdae.ui.guide.KeypadGuideFragment
import com.pizza.kkomdae.ui.guide.LeftGuideFragment
import com.pizza.kkomdae.ui.guide.RightGuideFragment
import com.pizza.kkomdae.ui.guide.ScreenShotGuideFragment
import com.pizza.kkomdae.ui.guide.Step1GuideFragment
import com.pizza.kkomdae.ui.step1.CameraBigFrameFragment

class CameraActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        setContentView(binding.root)
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.popBackStack()
//        transaction.replace(R.id.fl_main, MainFragment())
//        transaction.replace(R.id.fl_main, LaptopInfoInputFragment())
//        transaction.replace(R.id.fl_main, Step1GuideFragment())
        transaction.replace(R.id.fl_camera, FontShotGuideFragment())

        transaction.commit()

    }

}