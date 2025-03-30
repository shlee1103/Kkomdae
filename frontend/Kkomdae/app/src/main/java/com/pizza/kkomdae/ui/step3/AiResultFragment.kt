package com.pizza.kkomdae.ui.step3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pizza.kkomdae.AppData
import com.pizza.kkomdae.MainActivity
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.data.Step1Result
import com.pizza.kkomdae.databinding.FragmentAiResultBinding
import com.pizza.kkomdae.ui.MyAndroidViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var viewModel: MyAndroidViewModel
private const val TAG = "AiResultFragment"

class AiResultFragment : BaseFragment<FragmentAiResultBinding>(
    FragmentAiResultBinding::bind,
    R.layout.fragment_ai_result
) {
    private lateinit var mainActivity: MainActivity
    private var step = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    private var param1: String? = null
    private var param2: String? = null

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

        binding.topBar.tvTitle.text = ""

        // 초기 이미지 설정
        Glide.with(binding.ivImage)
            .load(AppData.frontUri)
            .into(binding.ivImage)

        // 이미지 클릭 이벤트 설정
        binding.ivImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("param1", step)

            // ImageDetailFragment 생성
            val imageDetailFragment = com.pizza.kkomdae.ui.step1.ImageDetailFragment()
            imageDetailFragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, imageDetailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // RecyclerView 데이터 설정
        val data = listOf(
            Step1Result(R.drawable.ic_front_laptop, "전면부"),
            Step1Result(R.drawable.ic_guide_back, "후면부"),
            Step1Result(R.drawable.ic_camera_left, "좌측"),
            Step1Result(R.drawable.ic_camera_right, "우측"),
            Step1Result(R.drawable.ic_guide_screen, "화면"),
            Step1Result(R.drawable.ic_guide_keypad, "키판")
        )

        // RecyclerView 어댑터 및 레이아웃 매니저 설정
        binding.rvPosition.adapter = AiResultAdapter(data) { position ->
            changeImage(position)
        }
        binding.rvPosition.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // 버튼 이벤트 설정
        binding.btnRetake.setOnClickListener {
            // 재촬영 기능 구현 (이전 화면으로 돌아가기)
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.btnConfirm.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, FinalResultFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun changeImage(position: Int) {
        Log.d(TAG, "changeImage: $position")
        step = position + 1
        var imageUri = when(step) {
            1 -> AppData.frontUri
            2 -> AppData.backUri
            3 -> AppData.leftUri
            4 -> AppData.rightUri
            5 -> AppData.screenUri
            6 -> AppData.keypadUri
            else -> AppData.frontUri
        }

        Glide.with(binding.ivImage)
            .load(imageUri)
            .into(binding.ivImage)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AiResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}