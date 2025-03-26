package com.pizza.kkomdae.ui.guide

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.content.ContextCompat
import com.pizza.kkomdae.AppData
import com.pizza.kkomdae.MainActivity
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentLaptopInfoInputBinding
import com.pizza.kkomdae.databinding.FragmentMainBinding
import com.pizza.kkomdae.databinding.FragmentStep1GuideBinding
private lateinit var mainActivity: MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Step1GuideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Step1GuideFragment : BaseFragment<FragmentStep1GuideBinding>(
    FragmentStep1GuideBinding::bind,
    R.layout.fragment_step1_guide
){

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
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
        binding.topBar.tvTitle.text = "외관 촬영 가이드"
        binding.topBar.pbStep.progress=100/3
        val color = ContextCompat.getColorStateList(requireContext(), R.color.blue500)

        val step =AppData.step
        when(step){
            1-> {
                binding.layoutStep.flStep1.backgroundTintList=color
            }
            2->{
                binding.layoutStep.flStep1.backgroundTintList=color
                binding.layoutStep.flStep2.backgroundTintList=color
            }
            3->{
                binding.layoutStep.flStep1.backgroundTintList=color
                binding.layoutStep.flStep2.backgroundTintList=color
                binding.layoutStep.flStep3.backgroundTintList=color
            }
            4->{
                binding.layoutStep.flStep1.backgroundTintList=color
                binding.layoutStep.flStep2.backgroundTintList=color
                binding.layoutStep.flStep3.backgroundTintList=color
                binding.layoutStep.flStep4.backgroundTintList=color
            }
            5->{
                binding.layoutStep.flStep1.backgroundTintList=color
                binding.layoutStep.flStep2.backgroundTintList=color
                binding.layoutStep.flStep3.backgroundTintList=color
                binding.layoutStep.flStep4.backgroundTintList=color
                binding.layoutStep.flStep5.backgroundTintList=color
            }
            6->{
                binding.layoutStep.flStep1.backgroundTintList=color
                binding.layoutStep.flStep2.backgroundTintList=color
                binding.layoutStep.flStep3.backgroundTintList=color
                binding.layoutStep.flStep4.backgroundTintList=color
                binding.layoutStep.flStep5.backgroundTintList=color
                binding.layoutStep.flStep6.backgroundTintList=color
            }
            else->{

            }
        }
        binding.layoutStep.flStep1

        binding.btnNext.setOnClickListener {
            mainActivity.next()
        }

        showIntroDialog()

    }

    private fun showIntroDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_step1_intro)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        val confirmButton = dialog.findViewById<Button>(R.id.btn_confirm)
        confirmButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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
}