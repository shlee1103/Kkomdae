package com.pizza.kkomdae.ui.step4

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pizza.kkomdae.MainActivity
import com.pizza.kkomdae.R
import com.pizza.kkomdae.base.BaseFragment
import com.pizza.kkomdae.databinding.FragmentStep4AiResultBinding
import com.pizza.kkomdae.presenter.model.Step4AiResult
import com.pizza.kkomdae.presenter.viewmodel.MainViewModel
import com.pizza.kkomdae.ui.step1.ImageDetailFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pizza.kkomdae.presenter.viewmodel.FinalViewModel
import com.pizza.kkomdae.ui.NoteFragment
import kotlinx.coroutines.launch
import okhttp3.internal.notify

class Step4AiResultFragment : BaseFragment<FragmentStep4AiResultBinding>(
    FragmentStep4AiResultBinding::bind,
    R.layout.fragment_step4_ai_result
) {

    // 액티비티
    private lateinit var mainActivity: MainActivity

    // 뷰모델
    private val viewModel : FinalViewModel by activityViewModels()

    // 기기 화면 선택(전면부, 후면부, 좌측면, 우측면, 모니터, 키보드)
    private var adaterIndex =0

    private var currentToast: Toast? = null

    private lateinit var backPressedCallback: OnBackPressedCallback

    private var param1: String? = null
    private var param2: String? = null
    private var count =0

    // 재촬영 하기 위한 step 저장( 1: 전면부, 2: 후면부, 3: 좌측면, 4: 우측면, 5: 모니터, 6: 키보드)
    private var step = 0

    // 누끼 딴 이미지 선택 리사이클러뷰 데이터 리스트
    val data = listOf(
        Step4AiResult(R.drawable.ic_front_laptop, "전면부",0),
        Step4AiResult(R.drawable.ic_guide_back, "후면부",0),
        Step4AiResult(R.drawable.ic_camera_left, "좌측면",0),
        Step4AiResult(R.drawable.ic_camera_right, "우측면",0),
        Step4AiResult(R.drawable.ic_guide_screen, "모니터",0),
        Step4AiResult(R.drawable.ic_guide_keypad, "키보드",0),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // 시스템 백 버튼 동작 설정
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 시스템 백 버튼 클릭 시 바텀시트 동작
                showQuitBottomSheet()
            }
        }

        // 콜백 등록
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 설정
        val adapter = initSetting()

        // 재촬영 설정
        initRePhoto(adapter)

        // 버튼 설정
        settingButton()

        class HorizontalSpaceItemDecoration(private val horizontalSpace: Int) : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = horizontalSpace
                outRect.left = horizontalSpace
            }
        }

        // 간격 추가
        binding.rvPosition.addItemDecoration(HorizontalSpaceItemDecoration(
            resources.getDimensionPixelSize(R.dimen.recyclerview_item_horizontal_spacing) // 값은 dimens.xml에 정의
        ))

        binding.rvPosition.adapter = adapter
        binding.rvPosition.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }

    // 버튼 설정
    private fun settingButton() {

        // 이미지 상세 보기 버튼 눌렀을 때
        clickDetailImageBtn()

        // 다음 버튼 눌렀을 때
        clickNextBtn()

        // X 클릭 버튼 눌렀을 때
        clickCloseBtn()

        // 재촬영 버튼 눌렀을 때
        clickRePhotoBtn()
    }

    private fun clickRePhotoBtn() {
        binding.btnRetry.setOnClickListener {
            viewModel.setReCameraStage(step + 1)
            mainActivity.reCamera(step)
        }
    }

    private fun clickCloseBtn() {
        binding.btnClose.setOnClickListener {
            showQuitBottomSheet()
        }
    }

    private fun clickNextBtn() {
        binding.btnConfirm.setOnClickListener {
            viewModel.clearPhoto()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, NoteFragment())
            transaction.commit()
        }
    }

    private fun clickDetailImageBtn() {
        binding.ivImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("param1", step)  // 이미지 URL을 전달

            // ImageDetailFragment 생성
            val imageDetailFragment = ImageDetailFragment()
            imageDetailFragment.arguments = bundle
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, imageDetailFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    // 재촬영 설정
    private fun initRePhoto(adapter: Step4AiResultAdapter) {
        Log.d(TAG, "onViewCreated: reCameraUri")
        // 재촬영 이미지 uri 서버로 보내기
        viewModel.reCameraUri.observe(viewLifecycleOwner) {
            it ?: return@observe


            Log.d(TAG, "onViewCreated: reCameraUri")

            binding.ivImage.visibility = View.INVISIBLE
            binding.loadingAnimation.visibility = View.VISIBLE
            binding.ivLoading.visibility = View.VISIBLE

            viewModel.reCameraStage.value?.let {
                adapter.showTextAt(it - 1)
            }

            Log.d(TAG, "onViewCreated: $it")
            viewModel.postRePhoto()
            viewModel.clearReCameraUri()
        }

        viewModel.rePhoto1.observe(viewLifecycleOwner) {
            it ?: return@observe
            adapter.hideTextAt(0)

            data[0].damage = it.data.photo_ai_damage
            // 로딩 애니메이션 숨기기
            changeImage(adaterIndex)
            binding.loadingAnimation.visibility = View.GONE
            binding.ivImage.visibility = View.VISIBLE

            // 토스트 메시지 표시
            showToast("전면부 사진이 재분석되었습니다.")
        }

        viewModel.rePhoto2.observe(viewLifecycleOwner) {
            it ?: return@observe
            adapter.hideTextAt(1)
            // 로딩 애니메이션 숨기기
            data[1].damage = it.data.photo_ai_damage
            changeImage(adaterIndex)
            binding.loadingAnimation.visibility = View.GONE
            binding.ivImage.visibility = View.VISIBLE

            // 토스트 메시지 표시
            showToast("후면부 사진이 재분석되었습니다.")
        }
        viewModel.rePhoto3.observe(viewLifecycleOwner) {
            it ?: return@observe
            adapter.hideTextAt(2)
            // 로딩 애니메이션 숨기기
            data[2].damage = it.data.photo_ai_damage
            changeImage(adaterIndex)
            binding.loadingAnimation.visibility = View.GONE
            binding.ivImage.visibility = View.VISIBLE

            // 토스트 메시지 표시
            showToast("좌측면 사진이 재분석되었습니다.")
        }
        viewModel.rePhoto4.observe(viewLifecycleOwner) {
            it ?: return@observe
            adapter.hideTextAt(3)
            // 로딩 애니메이션 숨기기
            data[3].damage = it.data.photo_ai_damage
            changeImage(adaterIndex)
            binding.loadingAnimation.visibility = View.GONE
            binding.ivImage.visibility = View.VISIBLE

            // 토스트 메시지 표시
            showToast("우측면 사진이 재분석되었습니다.")
        }
        viewModel.rePhoto5.observe(viewLifecycleOwner) {
            it ?: return@observe
            adapter.hideTextAt(4)
            // 로딩 애니메이션 숨기기
            data[4].damage = it.data.photo_ai_damage
            changeImage(adaterIndex)
            binding.loadingAnimation.visibility = View.GONE
            binding.ivImage.visibility = View.VISIBLE

            // 토스트 메시지 표시
            showToast("모니터 사진이 재분석되었습니다.")
        }
        viewModel.rePhoto6.observe(viewLifecycleOwner) {
            it ?: return@observe

            adapter.hideTextAt(5)
            // 로딩 애니메이션 숨기기

            data[5].damage = it.data.photo_ai_damage
            changeImage(adaterIndex)
            binding.loadingAnimation.visibility = View.GONE
            binding.ivImage.visibility = View.VISIBLE

            // 토스트 메시지 표시
            showToast("키보드 사진이 재분석되었습니다.")

        }
    }

    private fun initSetting(): Step4AiResultAdapter {
        // 이미지가 서버에서 데이터를 받아오기 전에 띄울수 있는 로딩 이미지 설정
        Glide.with(this)
            .asGif()
            .load(R.drawable.skeleton_ui) // 🔁 로딩용 GIF 리소스
            .into(binding.ivLoading)

        // 화면 선택 리사이클러뷰 설정
        val adapter = Step4AiResultAdapter(data, listen = {
            changeImage(it)
            adaterIndex = it

        }, viewModel = viewModel)
        adapter.selectItem(step)
        changeImage(step)

        // 서버에 데이터 받아 오기
        getApidata(adapter)
        return adapter
    }


    private fun getApidata(adapter: Step4AiResultAdapter) {
        if (count == 0) {
            lifecycleScope.launch {
                val result = viewModel.getAiPhoto()
                result.onSuccess {
                    if (it.success) {

                        // 이미지 로딩화면 안보이게 설정
                        binding.loadingAnimation.visibility = View.GONE

                        // 로딩이 끝났을때 화면이 보이게 설정
                        binding.ivImage.visibility = View.VISIBLE

                        Glide.with(binding.ivImage)
                            .load(it.data.Picture1_ai_url)
                            .into(object : CustomTarget<Drawable>() {
                                override fun onResourceReady(
                                    resource: Drawable,
                                    transition: Transition<in Drawable>?,
                                ) {
                                    binding.ivImage.setImageDrawable(resource)
                                    binding.ivImage.visibility = View.VISIBLE
                                    binding.ivLoading.visibility = View.GONE
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })

                        // 결함 여부
                        it.data.apply {
                            data[0].damage = photo1_ai_damage ?: 0
                            data[1].damage = photo2_ai_damage ?: 0
                            data[2].damage = photo3_ai_damage ?: 0
                            data[3].damage = photo4_ai_damage ?: 0
                            data[4].damage = photo5_ai_damage ?: 0
                            data[5].damage = photo6_ai_damage ?: 0
                            adapter.notifyDataSetChanged()
                            changeImage(0)
                        }

                        // 뷰모델에 서버에서 받아온 데이터 저장
                        viewModel.setAllPhoto(it.data)


                    } else {
                        // todo 에러 뜰때 추가
                        showErrorDialog()
                        // 이미지 로딩화면 안보이게 설정
                        binding.loadingAnimation.visibility = View.GONE

                        // 로딩이 끝났을때 화면이 보이게 설정
                        binding.ivImage.visibility = View.VISIBLE
                    }

                }.onFailure {
                    Log.d(TAG, "onViewCreated: $it")
                    showErrorDialog()

                    // 이미지 로딩화면 안보이게 설정
                    binding.loadingAnimation.visibility = View.GONE

                    // 로딩이 끝났을때 화면이 보이게 설정
                    binding.ivImage.visibility = View.VISIBLE
                }
            }
            count++
        }
    }


    // 에러 다이얼로그
    private fun showErrorDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_error_dialog)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        val confirmButton = dialog.findViewById<TextView>(R.id.tv_confirm)
        val errorText = dialog.findViewById<TextView>(R.id.tv_error_message)
        val errorTitleText = dialog.findViewById<TextView>(R.id.tv_error_title)
//
        errorTitleText.text="제출을 실패하였습니다."
        errorText.text="서버 오류로 인한 제출 실패입니다."
//
        confirmButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showQuitBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)

        // 계속하기 버튼 클릭 시 바텀시트 닫기
        val btnContinue = bottomSheetView.findViewById<View>(R.id.btn_continue)
        btnContinue.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // 그만두기 버튼 클릭 시 메인 화면으로 이동
        val btnQuit = bottomSheetView.findViewById<View>(R.id.btn_quit)
        btnQuit.setOnClickListener {
            bottomSheetDialog.dismiss()
            // 메인 화면으로 이동
            mainActivity.supportFragmentManager.popBackStack(null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
            val transaction = mainActivity.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_main, com.pizza.kkomdae.ui.MainFragment())
            transaction.commit()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }


    fun changeImage(it: Int){
        Log.d(TAG, "changeImage: $it")
        step=it
        val url = when(step){
            0-> viewModel.frontUri.value
            1-> viewModel.backUri.value
            2-> viewModel.leftUri.value
            3-> viewModel.rightUri.value
            4-> viewModel.screenUri.value
            5 -> viewModel.keypadUri.value
            else -> ""
        }

        // 결함 상태 텍스트
        if (data[it].damage > 0) {
//            binding.tvResultStatus.text = "${data[it].damage}개의 결함이 발견되었습니다."
            binding.tvResultStatus.text = "결함이 발견되었습니다."
            binding.tvResultStatus.setTextColor(resources.getColor(R.color.error, null))
        } else {
            binding.tvResultStatus.text = "발견된 결함이 없습니다."
            binding.tvResultStatus.setTextColor(resources.getColor(R.color.blue500, null))
        }

        Log.d(TAG, "changeImage_url:$url ")
        if(url==""){
            binding.loadingAnimation.visibility = View.VISIBLE
            binding.ivImage.visibility = View.INVISIBLE
            Glide.with(this)
                .load("")
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        binding.ivImage.setImageDrawable(resource)
                        binding.ivImage.visibility = View.VISIBLE
                        binding.ivLoading.visibility = View.GONE
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }else{

            Glide.with(binding.ivImage)
                .load(url)
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        binding.ivImage.setImageDrawable(resource)
                        binding.ivImage.visibility = View.VISIBLE
                        binding.ivLoading.visibility = View.GONE
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

            binding.loadingAnimation.visibility = View.GONE
            binding.ivImage.visibility = View.VISIBLE
        }

    }

    private fun showToast(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        currentToast?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearRePhoto()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onDestroy() {
        super.onDestroy()
        // 콜백 해제
        backPressedCallback.remove()
    }

    companion object {
        private const val TAG = "Step4AiResultFragment"
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Step4AiResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}