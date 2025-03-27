package com.pizza.kkomdae.ui.guide

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentStep2GuideBinding
import com.pizza.kkomdae.ui.step2.QrScanFragment
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
            transaction.replace(R.id.fl_main, QrScanFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        showIntroDialog()
    }

    private fun showIntroDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dialog_step2_intro)

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