package com.meridiane.lection3.presentation.ui.catalog

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentProductDetalsBinding
import com.meridiane.lection3.presentation.recyclerView.RcPreView
import com.meridiane.lection3.presentation.recyclerView.RcViewPager
import com.meridiane.lection3.presentation.viewModel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentProductDetails : Fragment() {

    private lateinit var binding: FragmentProductDetalsBinding

    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString(Constants.SEND_ID_DETAILS_FRAGMENT)!!

        viewModel.getProductDetails(id)

        lifecycleScope.launch {
            viewModel.productsState.collectLatest { product ->
                Toast.makeText(requireContext(), "$product", Toast.LENGTH_SHORT).show()
                if (product.title != null) {
                    preView(product.preview)
                    imagePage(product.preview)
                }
            }
        }

        binding.btBack.setOnClickListener {
            findNavController().navigate(R.id.bookFragment)
        }

        binding.textInput.setOnClickListener {
            showDialog()
        }

    }


    private fun imagePage(image: Int?) {
        val rcViewPager = RcViewPager(mutableListOf(image, null, null))
        binding.viewPager2.adapter = rcViewPager
    }

    private fun preView(string: Int?) {
        val rcPreView = RcPreView(mutableListOf(string, null, null)) { item ->
            binding.viewPager2.currentItem = item
        }

        binding.rcPreView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcPreView.adapter = rcPreView
    }

    private fun showDialog() {
        childFragmentManager.setFragmentResultListener("request_key",this) { _, bundle ->
            val result = bundle.getString("bundleKey")

            binding.textInput.setText(result)
        }
        val modalBottomSheet = BottomDialogFragment()
        modalBottomSheet.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        modalBottomSheet.show(childFragmentManager, BottomDialogFragment.TAG)
    }

}