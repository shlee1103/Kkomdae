package com.pizza.kkomdae.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentMainBinding
import com.pizza.kkomdae.databinding.FragmentOathBinding
import androidx.core.widget.ImageViewCompat
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.pizza.kkomdae.ui.guide.AllStepGuideFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OathFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OathFragment : BaseFragment<FragmentOathBinding>(
    FragmentOathBinding::bind,
    R.layout.fragment_oath
) {

    private var isOath1Checked = false
    private var isOath2Checked = false
    private var isOath3Checked = false
    private var isOath4Checked = false

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        isOath1Checked = false
        isOath2Checked = false
        isOath3Checked = false
        isOath4Checked = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 제목 설정
        val topBarTitle = view.findViewById<View>(R.id.top_bar).findViewById<TextView>(R.id.tv_title)
        topBarTitle.text = "수령확인서"
        
        val title1 = view.findViewById<View>(R.id.oath_title1).findViewById<TextView>(R.id.tv_model_number)
        title1.text = "01 손실 및 파손 책임"

        val title2 = view.findViewById<View>(R.id.oath_title2).findViewById<TextView>(R.id.tv_model_number)
        title2.text = "02 사용 규정 및 의무"

        val title3 = view.findViewById<View>(R.id.oath_title3).findViewById<TextView>(R.id.tv_model_number)
        title3.text = "03 반납 및 보관 의무"

        val title4 = view.findViewById<View>(R.id.oath_title4).findViewById<TextView>(R.id.tv_model_number)
        title4.text = "04 인수 확인"
        
        // 뒤로가기 버튼 눌렀을 때 메인화면으로 이동
        binding.topBar.btnBack.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, MainFragment())
            transaction.commit()
        }

        // 서약서 항목 클릭 이벤트
        binding.cardAgreement1.setOnClickListener {
            toggleOathSelection(it, R.id.oath_title1)
            isOath1Checked = !isOath1Checked
            updateCompleteButtonState()
            binding.cardAgreement1.isClickable=false
            binding.oathContent1.isVisible=false
            binding.oathTitle1.tvView.isVisible=true
        }
        binding.cardAgreement2.setOnClickListener {
            toggleOathSelection(it, R.id.oath_title2)
            isOath2Checked = !isOath2Checked
            updateCompleteButtonState()
            binding.cardAgreement2.isClickable=false
            binding.oathContent2.isVisible=false
            binding.oathTitle2.tvView.isVisible=true
        }
        binding.cardAgreement3.setOnClickListener {
            toggleOathSelection(it, R.id.oath_title3)
            isOath3Checked = !isOath3Checked
            binding.cardAgreement3.isClickable=false
            updateCompleteButtonState()
            binding.oathContent3.isVisible=false
            binding.oathTitle3.tvView.isVisible=true
        }
        binding.cardAgreement4.setOnClickListener {
            toggleOathSelection(it, R.id.oath_title4)
            isOath4Checked = !isOath4Checked
            updateCompleteButtonState()
            binding.cardAgreement4.isClickable=false
            binding.oathContent4.isVisible=false
            binding.oathTitle4.tvView.isVisible=true
        }
        
        // 서약서 내용 보기/닫기
        binding.oathTitle1.tvView.setOnClickListener {
            if ( binding.oathTitle1.tvView.text=="보기"){
                binding.oathContent1.isVisible=true
                binding.oathTitle1.tvView.text="닫기"
            }
            else {
                binding.oathContent1.isVisible=false
                binding.oathTitle1.tvView.text="보기"
            }
            
        }

        binding.oathTitle2.tvView.setOnClickListener {
            if ( binding.oathTitle2.tvView.text=="보기"){
                binding.oathContent2.isVisible=true
                binding.oathTitle2.tvView.text="닫기"
            }
            else {
                binding.oathContent2.isVisible=false
                binding.oathTitle2.tvView.text="보기"
            }

        }

        binding.oathTitle3.tvView.setOnClickListener {
            if ( binding.oathTitle3.tvView.text=="보기"){
                binding.oathContent3.isVisible=true
                binding.oathTitle3.tvView.text="닫기"
            }
            else {
                binding.oathContent3.isVisible=false
                binding.oathTitle3.tvView.text="보기"
            }

        }

        binding.oathTitle4.tvView.setOnClickListener {
            if ( binding.oathTitle4.tvView.text=="보기"){
                binding.oathContent4.isVisible=true
                binding.oathTitle4.tvView.text="닫기"
            }
            else {
                binding.oathContent4.isVisible=false
                binding.oathTitle4.tvView.text="보기"
            }

        }
        

        // 다음 화면 넘어가기
        binding.btnNext.setOnClickListener {
            if (isOath1Checked && isOath2Checked && isOath3Checked && isOath4Checked) {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fl_main, AllStepGuideFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

    }

    private fun toggleOathSelection(cardView: View, titleId: Int) {
        val oathTitleView = cardView.findViewById<View>(titleId)
        oathTitleView.isSelected = !oathTitleView.isSelected

        val checkImageView = oathTitleView.findViewById<ImageView>(R.id.iv_check)
        if (oathTitleView.isSelected) {
            ImageViewCompat.setImageTintList(
                checkImageView,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue500))
            )
        } else {
            ImageViewCompat.setImageTintList(
                checkImageView,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray300))
            )
        }
    }

    // 버튼 상태 업데이트 함수
    private fun updateCompleteButtonState() {
        val allChecked = isOath1Checked && isOath2Checked && isOath3Checked && isOath4Checked
        if (allChecked) {
            binding.btnNext.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue500))
        } else {
            binding.btnNext.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue200))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OathFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OathFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}