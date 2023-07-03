package com.meridiane.lection3.presentation.ui.signin

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentSignInBinding
import com.meridiane.lection3.presentation.viewModel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.getTokenBd() != "") findNavController().navigate(R.id.bookFragment)

        binding.buttonSignIn.setOnClickListener {
            checkLoginAndPassword(binding)
        }

        binding.textPassword.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                checkLoginAndPassword(binding)
                true
            } else false
        }
    }


    private fun checkLoginAndPassword(binding: FragmentSignInBinding) {
        with(binding) {

            when (viewModel.checkLoginAndPassword(
                textLogin.text.toString(),
                textPassword.text.toString()
            )) {
                1 -> {
                    textPassword.error = getString(R.string.length_little)
                    textLogin.error = getString(R.string.error_email)
                }

                2 -> {
                    textPassword.error = getString(R.string.length_little)
                    textLogin.error = null
                }

                3 -> {
                    textPassword.error = null
                    textLogin.error = getString(R.string.error_email)
                }

                4 -> {
                    textPassword.error = null
                    textLogin.error = null
                    viewModel.getTokenServer(
                        textLogin.text.toString(),
                        textPassword.text.toString()
                    )
                    buttonSignIn.isLoading = true
                    textViewError.visibility = View.GONE
                }
            }

            viewModel.liveData.observe(viewLifecycleOwner) { token ->

                try {
                    viewModel.saveTokenVM(token)
                    findNavController().navigate(R.id.bookFragment)
                } catch (e: Exception) {
                    textViewError.visibility = View.VISIBLE
                    textViewError.text =
                        view?.context?.getString(R.string.error_text, e)

                    buttonSignIn.isLoading
                    buttonSignIn.setText(getString(R.string.repeat_button_text))
                }
            }
        }

        viewModel.liveDataError.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.contrainLayoutSignIn, error, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(
                        ContextCompat.getColor(requireContext(), R.color.error_snack_bar)
                    )
                    .setActionTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    .show()
                binding.buttonSignIn.isLoading = false
                binding.buttonSignIn.setText(getString(R.string.repeat_button_text))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}