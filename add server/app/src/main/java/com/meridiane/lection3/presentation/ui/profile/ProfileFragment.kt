package com.meridiane.lection3.presentation.ui.profile

import android.app.AlertDialog
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
import com.meridiane.lection3.databinding.FragmentProfileBinding
import com.meridiane.lection3.databinding.LogoutDialogBinding
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btBack.setOnClickListener {
                findNavController().popBackStack()
            }

            imageLogout.setOnClickListener {
                alertDialog()
            }

            imageMyBuy.setOnClickListener {
                findNavController().navigate(R.id.myOrdersFragment)
            }

            imageSettings.setOnClickListener {
                findNavController().navigate(R.id.fragmentProfileTransform)
            }
        }
        binding.container.setListener {
            binding.container.state = ProgressContainer.State.Loading
            viewModel.getProfile()
        }

        getProfile()
        viewModel.getProfile()
    }

    private fun getProfile() {
        lifecycleScope.launch {

            viewModel.profileState.collectLatest { state ->
                when (state) {
                    is ApiState.Loading -> binding.container.state = ProgressContainer.State.Loading

                    is ApiState.Success -> {
                        binding.container.state = ProgressContainer.State.Success
                        with(binding) {
                            textVersion.visibility = View.VISIBLE

                            imageSettings.visibility = View.VISIBLE
                            imageMyBuy.visibility = View.VISIBLE
                            imageLogout.visibility = View.VISIBLE
                            imageMyBuy.load(R.drawable.my_buy)
                            imageSettings.load(R.drawable.settings)
                            imageLogout.load(R.drawable.logout)

                            textName.text = binding.root.context.getString(
                                R.string.name_surname_profile_text,
                                state.data.name,
                                state.data.surname
                            )
                            textPost.text = state.data.occupation.toString()
                            textVersion.text = binding.root.context.getString(R.string.text_version)

                            if (state.data.avatarUrl!!.all { it.isDigit() }) imagePhoto.load(R.drawable.ic_photo)
                            else {
                                imagePhoto.load(state.data.avatarUrl) {
                                    crossfade(true)
                                    crossfade(100)
                                    placeholder(R.drawable.ic_photo)
                                    transformations(CircleCropTransformation())
                                }
                            }

                        }
                    }

                    is ApiState.Error -> {
                        binding.container.state = ProgressContainer.State.Notice(state.data)
                    }
                }
            }
        }
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(context)
        val rootDialogElement = LogoutDialogBinding.inflate(activity?.layoutInflater!!)

        val dialog = builder.setView(rootDialogElement.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        rootDialogElement.textCancel.setOnClickListener {
            dialog.dismiss()
        }

        rootDialogElement.textLogout.setOnClickListener {
            binding.textName.text = "Имя Фамилия"
            binding.textPost.text = "Должность"
            binding.imagePhoto.setImageResource(R.drawable.ic_photo)
            dialog.dismiss()
        }
        dialog.show()
    }

}
