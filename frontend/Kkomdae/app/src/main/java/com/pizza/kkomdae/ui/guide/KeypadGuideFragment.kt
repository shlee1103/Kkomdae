package com.pizza.kkomdae.ui.guide

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.pizza.kkomdae.CameraActivity
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentKeypadGuideBinding
import com.pizza.kkomdae.databinding.FragmentScreenShotGuideBinding
import com.pizza.kkomdae.ui.MyAndroidViewModel
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var imageCapture: ImageCapture? = null
private var camera: Camera? = null
private lateinit var viewModel: MyAndroidViewModel
private lateinit var cameraActivity: CameraActivity

/**
 * A simple [Fragment] subclass.
 * Use the [KeypadGuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KeypadGuideFragment : BaseFragment<FragmentKeypadGuideBinding>(
    FragmentKeypadGuideBinding::bind,
    R.layout.fragment_keypad_guide
) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cameraActivity = context as CameraActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startCamera()
        viewModel = ViewModelProvider(requireActivity()).get(MyAndroidViewModel::class.java)
        // 가이드 닫기 버튼 눌렀을 때
        binding.btnCancel?.setOnClickListener {
            binding.clGuide?.isVisible = false
            binding.overlayView?.isVisible=true
            binding.btnBack?.isVisible = true
            binding.btnShot?.isVisible = true
            binding?.btnGuide?.isVisible = true
        }

        // 가이드 보기 버튼 눌렀을 떄
        binding.btnGuide?.setOnClickListener {
            binding.clGuide?.isVisible = true
            binding.overlayView?.isVisible=false
            binding.btnBack?.isVisible = false
            binding.btnShot?.isVisible = false
            binding?.btnGuide?.isVisible = false
        }
        // 뒤로 가기 버튼 눌렀을 때
        binding.btnBack?.setOnClickListener {
            cameraActivity.moveToBack()
        }

        binding.btnShot?.setOnClickListener {
            takePhoto()
        }

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // ✅ ImageCapture 설정 (16:9 비율 유지)
            imageCapture = ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9) // 📌 비율 설정
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // 빠른 캡처 모드
                .build()

            // ✅ Preview 설정 (16:9 비율 유지)
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9) // 📌 비율 설정
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView?.surfaceProvider)
                }

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("CameraFragment", "카메라 실행 오류: ${e.message}")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(requireContext().externalMediaDirs.firstOrNull(),
            "IMG_${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d("CameraFragment", "사진 저장됨: $savedUri")

                    // ✅ ViewModel에 사진 저장
                    viewModel.setKeypad(savedUri)
                    viewModel.setStep(6)



                    cameraActivity.changeFragment(0)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "사진 촬영 실패: ${exception.message}")
                }
            })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KeypadGuideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KeypadGuideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}