package com.pizza.kkomdae.ui.step1

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.pizza.kkomdae.AppData
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentFontResultBinding
import com.pizza.kkomdae.databinding.FragmentImageDetailBinding
import com.pizza.kkomdae.ui.guide.AllStepOnboardingFragment.OnboardingStep
import com.pizza.kkomdae.ui.guide.Step2GuideFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageDetailFragment :  BaseFragment<FragmentImageDetailBinding>(
    FragmentImageDetailBinding::bind,
    R.layout.fragment_image_detail
) {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null

    data class ImageDetailStep(
        val title: String,
        val stepNumber: Int,
        val url: Uri?=null,
    )

    private val list = listOf(
        ImageDetailStep(
            "전면부",
            1,
            AppData.frontUri,
        ),
        ImageDetailStep(
            "후면부",
            2,
            AppData.backUri
        ),
        ImageDetailStep(
            "좌측면",
            3,
            AppData.leftUri
        ),ImageDetailStep(
            "우측면",
            4,
            AppData.rightUri
        ),ImageDetailStep(
            "모니터",
            5,
            AppData.screenUri
        ),ImageDetailStep(
            "키보드",
            6,
            AppData.keypadUri
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter=ImageDetailAdapter(list)
        binding.viewPager.setCurrentItem((param1?:1)-1,false)

        binding.btnCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()

        }

        // 테이블 레이아웃 설정
        TabLayoutMediator(
            binding.tlStep,
            binding.viewPager,
        ) { tab, position ->
            binding.viewPager.currentItem = tab.position
        }.attach()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            ImageDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}