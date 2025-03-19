package com.pizza.kkomdae

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pizza.kkomdae.databinding.ActivityMainBinding
import com.pizza.kkomdae.ui.guide.Step1GuideFragment
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val REQUEST_CAMERA_PERMISSION = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.popBackStack()
//        transaction.replace(R.id.fl_main, MainFragment())
//        transaction.replace(R.id.fl_main, LaptopInfoInputFragment())
//        transaction.replace(R.id.fl_main, Step1GuideFragment())
        transaction.replace(R.id.fl_main, Step1GuideFragment())

        transaction.commit()
        checkCameraPermission()
    }

    fun next(){
       val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            // ✅ 카메라 권한이 이미 허용된 경우, CameraActivity 실행

        } else {
            // ❌ 권한이 없으면 사용자에게 요청
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // ✅ 사용자가 권한을 허용했을 경우, CameraActivity 실행

            } else {
                // ❌ 사용자가 권한을 거부했을 경우
                showToast("카메라 권한이 필요합니다.")
            }
        }
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}