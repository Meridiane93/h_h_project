package com.meridiane.lection3.presentation.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.meridiane.lection3.ApiState
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentProfileTransformBinding
import com.meridiane.lection3.presentation.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentProfileTransform : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileTransformBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileTransformBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btTransformProfile.setText(getString(R.string.transform_text_bt))


        binding.imageAddPhoto.setOnClickListener {
            showDialogPhoto(resources.getStringArray(R.array.photo))
        }

        binding.textWorkProfile.setOnClickListener {
            showDialogWork(resources.getStringArray(R.array.work))
        }

        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }

        getProfile()
        viewModel.getProfile()
    }

    private fun getProfile() {
        lifecycleScope.launch {

            viewModel.profileState.collectLatest { state ->
                when (state) {
                    is ApiState.Loading -> binding.btTransformProfile.isLoading = true

                    is ApiState.Success -> {
                        with(binding) {

                            textNameProfile.setText(state.data.name)
                            textSurnameProfile.setText(state.data.surname)
                            textWorkProfile.setText(state.data.occupation)

                            if (state.data.avatarUrl!!.all{ it.isDigit() }) imagePhoto.load(R.drawable.ic_photo)
                            else  {
                                imagePhoto.load(state.data.avatarUrl) {
                                    crossfade(true)
                                    crossfade(100)
                                    placeholder(R.drawable.ic_photo)
                                    transformations(CircleCropTransformation())
                                }

                            }
                        }
                        binding.btTransformProfile.setText(getString(R.string.transform_text_bt))
                    }

                    is ApiState.Error ->{
                       // Error
                    }
                }
            }
        }
    }

    private fun showDialogPhoto(listSize: Array<String>) {
        childFragmentManager.setFragmentResultListener("request_key_item", this) { _, bundle ->
            val result = bundle.getString("bundleKey_item")

            binding.textWorkProfile.setText(result)
        }

        val modalBottomSheet =  BottomDialogItemFragment(listSize)

        modalBottomSheet.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        modalBottomSheet.show(childFragmentManager, BottomDialogItemFragment.TAG)
    }

    private fun showDialogWork(listSize: Array<String>) {
        childFragmentManager.setFragmentResultListener("request_key_item", this) { _, bundle ->
            val result = bundle.getString("bundleKey_item")

            binding.textWorkProfile.setText(result)
            if (result == getString(R.string.work_another))
                binding.layoutWorkSwitchProfile.visibility = View.VISIBLE
            else binding.layoutWorkSwitchProfile.visibility = View.INVISIBLE

        }
        val modalBottomSheet = BottomDialogItemFragment(listSize)

        modalBottomSheet.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        modalBottomSheet.show(childFragmentManager, BottomDialogItemFragment.TAG)
    }

}