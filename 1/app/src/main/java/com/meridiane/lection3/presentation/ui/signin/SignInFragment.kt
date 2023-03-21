package com.meridiane.lection3.presentation.ui.signin

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.commit
import com.meridiane.lection3.R
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.meridiane.lection3.databinding.FragmentSignInBinding
import com.meridiane.lection3.presentation.ui.catalog.BookFragment
import com.meridiane.lection3.presentation.viewModel.SignInViewModel

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

        with(binding) {

            buttonSignIn.setOnClickListener {
                if (checkLoginAndPassword()) {
                        viewModel.getLogin()
                        buttonSignIn.isLoading = true
                        textViewError.visibility = View.GONE
                        buttonSignIn.colorButton(R.color.purple_700)

                        viewModel.liveData.observe(viewLifecycleOwner) { data ->
                            if (data == "Success(is success)") {
                                navigateToCatalog()
                            } else {
                                textViewError.visibility = View.VISIBLE
                                textViewError.text =
                                    view.context.getString(R.string.error_text, data)

                                buttonSignIn.isLoading
                                buttonSignIn.setText("Повторить")
                                buttonSignIn.colorButton(R.color.error_text)
                            }
                        }
                    }


                binding.textPassword.setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE ||
                        event.action == KeyEvent.ACTION_DOWN &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER
                    ) {
                        checkLoginAndPassword()
                        true
                    } else false
                }

            }

        }
    }

    private fun checkLoginAndPassword(): Boolean{

        when {
            binding.textPassword.text?.isNotEmpty()!!
                    && binding.textLogin.text!!.isNotBlank() -> {
                return true
            }
            binding.textLogin.text!!.isEmpty() -> {
                binding.textLogin.error = "Поле не заполнено"
                return false
            }
            binding.textPassword.text!!.isEmpty() -> {
                binding.textPassword.error = "Поле не заполнено"
                return false
            }
            binding.textLogin.text!!.isNotEmpty() -> {
                binding.textLogin.error = null
                return false
            }
            binding.textPassword.text!!.isEmpty() -> {
                binding.textPassword.error = "Поле не заполнено"
                return false
            }
            binding.textPassword.text!!.isNotEmpty() -> {
                binding.textPassword.error = null
                return false
            }

        }
        return true
    }

    private fun navigateToCatalog() {
        parentFragmentManager.commit {
            replace<BookFragment>(R.id.container)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}