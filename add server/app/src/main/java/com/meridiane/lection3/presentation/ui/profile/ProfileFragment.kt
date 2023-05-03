package com.meridiane.lection3.presentation.ui.profile

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private lateinit var  binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmerViewContainer.startShimmerAnimation()
        viewModel.getProfile()


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
                Toast.makeText(context,"Мои настройки",Toast.LENGTH_SHORT).show()
            }
        }

        getProfile()
    }

    private fun getProfile() {
        lifecycleScope.launch {
            viewModel.profileState.collectLatest { profile ->
                with(binding) {
                    if (profile.name != null) {
                        shimmerViewContainer.stopShimmerAnimation()
                        shimmerViewContainer.visibility = View.GONE
                        textVersion.visibility = View.VISIBLE

                        imageSettings.visibility = View.VISIBLE
                        imageMyBuy.visibility = View.VISIBLE
                        imageLogout.visibility = View.VISIBLE
                        imageMyBuy.load(R.drawable.my_buy)
                        imageSettings.load(R.drawable.settings)
                        imageLogout.load(R.drawable.logout)

                        textName.text = binding.root.context.getString(
                            R.string.name_surname_profile_text,
                            profile.name,
                            profile.surname
                        )
                        textPost.text = profile.occupation.toString()
                        textVersion.text = binding.root.context.getString(R.string.text_version)
                        imagePhoto.load(profile.avatarUrl) {
                            crossfade(true)
                            crossfade(100)
                            placeholder(R.drawable.ic_photo)
                            transformations(CircleCropTransformation())

                        }
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

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment()
    }

}