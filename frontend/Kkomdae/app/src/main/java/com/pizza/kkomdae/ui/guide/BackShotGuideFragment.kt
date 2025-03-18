package com.pizza.kkomdae.ui.guide

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.pizza.kkomdae.CameraActivity
import com.pizza.kkomdae.MainActivity
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentBackShotGuideBinding
import com.pizza.kkomdae.databinding.FragmentFontShotGuideBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var camera: Camera? = null


private lateinit var cameraActivity: CameraActivity

/**
 * A simple [Fragment] subclass.
 * Use the [BackShotGuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BackShotGuideFragment :  BaseFragment<FragmentBackShotGuideBinding>(
    FragmentBackShotGuideBinding::bind,
    R.layout.fragment_back_shot_guide
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
        binding.btnCancel?.setOnClickListener {
            binding.clGuide?.isVisible = false
            binding.overlayView?.isVisible=true
        }

        binding.btnGuide?.setOnClickListener {
            binding.clGuide?.isVisible = true
            binding.overlayView?.isVisible=false
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BackShotGuideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BackShotGuideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.previewView?.surfaceProvider) }

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (e: Exception) {
                Log.e("CameraBigFrameFragment", "카메라 실행 오류: ${e.message}")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }



    override fun onResume() {
        super.onResume()
        cameraActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}