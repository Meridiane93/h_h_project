package com.meridiane.lection3.presentation.ui.signin

import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentSignInBinding
import com.meridiane.lection3.domain.models.Authorization
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

            when {
                !isEmailValid(textLogin.text.toString()) &&
                        textPassword.text!!.length <= 6 -> {
                    textPassword.error = "Поле содержит менее 8 символов"
                    textLogin.error = "Некорректный адрес электронной почты"
                }
                isEmailValid(textLogin.text.toString()) &&
                        textPassword.text!!.length <= 6 -> {
                    textPassword.error = "Поле содержит менее 8 символов"
                    textLogin.error = null
                }
                !isEmailValid(textLogin.text.toString()) &&
                        textPassword.text!!.length > 6 -> {
                    textPassword.error = null
                    textLogin.error = "Некорректный адрес электронной почты"
                }
                isEmailValid(textLogin.text.toString()) &&
                        textPassword.text!!.length > 6 -> {
                    textPassword.error = null
                    textLogin.error = null
                    viewModel.getLogin(
                        Authorization(
                            textLogin.text.toString(),
                            textPassword.text.toString()
                        )
                    )
                    buttonSignIn.isLoading = true
                    textViewError.visibility = View.GONE
                    buttonSignIn.colorButton(R.color.purple_700)
                }
            }


            viewModel.liveData.observe(viewLifecycleOwner) { data ->
                if (data == "Success(is success)") {
                    findNavController().navigate(R.id.bookFragment)
                } else {
                    textViewError.visibility = View.VISIBLE
                    textViewError.text =
                        view?.context?.getString(R.string.error_text, data)

                    buttonSignIn.isLoading
                    buttonSignIn.setText("Повторить")
                    buttonSignIn.colorButton(R.color.error_text)
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}