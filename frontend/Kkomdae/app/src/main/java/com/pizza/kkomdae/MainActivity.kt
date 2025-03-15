package com.pizza.kkomdae

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pizza.kkomdae.databinding.ActivityMainBinding
import com.pizza.kkomdae.ui.LaptopInfoInputFragment
import com.pizza.kkomdae.ui.MainFragment
import com.pizza.kkomdae.ui.OathFragment
import com.pizza.kkomdae.ui.guide.Step1GuideFragment
import com.pizza.kkomdae.ui.step1.FontResultFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.popBackStack()
//        transaction.replace(R.id.fl_main, MainFragment())
//        transaction.replace(R.id.fl_main, LaptopInfoInputFragment())
//        transaction.replace(R.id.fl_main, Step1GuideFragment())
        transaction.replace(R.id.fl_main, FontResultFragment())

        transaction.commit()
    }
}