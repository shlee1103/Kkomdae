package com.pizza.kkomdae.ui.step1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentFontResultBinding
import com.pizza.kkomdae.databinding.FragmentStep1ResultBinding
import com.pizza.kkomdae.ui.MyAndroidViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var viewModel: MyAndroidViewModel
private const val TAG = "Step1ResultFragment"
/**
 * A simple [Fragment] subclass.
 * Use the [Step1ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Step1ResultFragment : BaseFragment<FragmentStep1ResultBinding>(
    FragmentStep1ResultBinding::bind,
    R.layout.fragment_step1_result
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
        viewModel = ViewModelProvider(requireActivity()).get(MyAndroidViewModel::class.java)
        Log.d(TAG, "onViewCreated: ${viewModel.frontUri.value}")
        Glide.with(binding.ivImage)
            .load(viewModel.frontUri.value)
            .into(binding.ivImage)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Step1ResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Step1ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}