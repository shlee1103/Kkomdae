package com.pizza.kkomdae.ui.guide

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentStep1GuideBinding
import com.pizza.kkomdae.databinding.FragmentStep2GuideBinding
import com.pizza.kkomdae.ui.QrScanFragment
import com.pizza.kkomdae.ui.step2.Step2ResultFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Step2GuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Step2GuideFragment : BaseFragment<FragmentStep2GuideBinding>(
    FragmentStep2GuideBinding::bind,
    R.layout.fragment_step2_guide
) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topBar.tvTitle.text = "Step 2"
        binding.topBar.pbStep.progress=200/3

        binding.btnNext.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fl_main, QrScanFragment())
            transaction.replace(R.id.fl_main, Step2ResultFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Step2GuideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Step2GuideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}