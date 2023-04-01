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
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentProfileBinding
import com.meridiane.lection3.databinding.LogoutDialogBinding
import com.meridiane.lection3.presentation.viewModel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfile()

        with(binding){
            btBack.setOnClickListener {
                findNavController().navigate(R.id.bookFragment)
            }

            imageLogout.setOnClickListener {
                alertDialog()
            }
        }

        getProfile()
    }

    private fun getProfile() {
        lifecycleScope.launch {
            viewModel.profileState.collectLatest { profile ->
                with(binding){
                    textName.text = "${profile.name} ${profile.surname}"
                    textPost.text = profile.occupation.toString()
                    imagePhoto.load(profile.avatarUrl) {
                        crossfade(true)
                        crossfade(1000)
                        placeholder(R.drawable.img_1)
                        transformations(CircleCropTransformation())

                    }
                    }

                }
            }
        }

    private fun alertDialog(){
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
            binding.imagePhoto.setImageResource(R.drawable.img_1)
            dialog.dismiss()
        }
        dialog.show()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}