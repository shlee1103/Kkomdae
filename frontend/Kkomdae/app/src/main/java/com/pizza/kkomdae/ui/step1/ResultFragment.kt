package com.pizza.kkomdae.ui.step1

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pizza.kkomdae.CameraActivity
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentFontResultBinding
import com.pizza.kkomdae.ui.MyAndroidViewModel
import com.pizza.kkomdae.ui.guide.Step1GuideFragment
import kotlin.math.log

/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "ResultFragment"
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var cameraActivity: CameraActivity

class ResultFragment : BaseFragment<FragmentFontResultBinding>(
    FragmentFontResultBinding::bind,
    R.layout.fragment_font_result
){
    private lateinit var viewModel: MyAndroidViewModel




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
        viewModel = ViewModelProvider(requireActivity()).get(MyAndroidViewModel::class.java)

        var url :Uri? = null
        if(viewModel.step.value ==1){
            url = viewModel.frontUri.value
        }else if (viewModel.step.value ==2){
            url = viewModel.backUri.value
        }
        binding.ivProduct?.let {
            Log.d(TAG, "CameraFragment: ")
            Glide.with(it)
                .load(url)
                .into(it)
        }

        binding.btnBack?.setOnClickListener {
            cameraActivity.changeFragment(viewModel.step.value?:0)
        }
        binding.btnCheck?.setOnClickListener {
            cameraActivity.changeFragment((viewModel.step.value?:-1)+1)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Step1GuideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Step1GuideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onResume() {
        super.onResume()
        cameraActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }




}
