package com.pizza.kkomdae.ui
import androidx.camera.core.Camera
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentBackShotGuideBinding
import com.pizza.kkomdae.databinding.FragmentQrScanBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QrScanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrScanFragment : BaseFragment<FragmentQrScanBinding>(
    FragmentQrScanBinding::bind,
    R.layout.fragment_qr_scan
)  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var camera: Camera? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.previewView
        startCamera()
    }
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // 후면 카메라 선택
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Preview 설정
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (e: Exception) {
                Log.e("QrScanFragment", "카메라 실행 오류: ${e.message}")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QrScanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QrScanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}