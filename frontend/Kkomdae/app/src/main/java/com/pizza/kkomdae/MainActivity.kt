package com.pizza.kkomdae

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pizza.kkomdae.databinding.ActivityMainBinding
import com.pizza.kkomdae.ui.LaptopInfoInputFragment
import com.pizza.kkomdae.ui.MainFragment
import com.pizza.kkomdae.ui.OathFragment
import com.pizza.kkomdae.ui.guide.BackShotGuideFragment
import com.pizza.kkomdae.ui.guide.ScreenShotGuideFragment
import com.pizza.kkomdae.ui.guide.Step1GuideFragment
import com.pizza.kkomdae.ui.step1.Step1ResultFragment
import com.pizza.kkomdae.ui.step3.FinalResultFragment
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val cameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val photoUri = result.data?.getIntExtra("PHOTO_URI",0)
            if(photoUri==1){
                val transaction = supportFragmentManager.beginTransaction()
                supportFragmentManager.popBackStack()
//        transaction.replace(R.id.fl_main, MainFragment())
//        transaction.replace(R.id.fl_main, LaptopInfoInputFragment())
//        transaction.replace(R.id.fl_main, Step1GuideFragment())
                transaction.replace(R.id.fl_main, Step1ResultFragment())

                transaction.commit()
                checkCameraPermission()
            }
        }
    }


    private val REQUEST_CAMERA_PERMISSION = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        val transaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.popBackStack()
        transaction.replace(R.id.fl_main, MainFragment())

        transaction.commit()
        checkCameraPermission()
    }

    fun next(){
       val intent = Intent(this, CameraActivity::class.java)
        cameraResultLauncher.launch(intent)
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