package com.meridiane.lection3.presentation.ui.catalog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentProductDetalsBinding
import com.meridiane.lection3.domain.entity.product_detail.SizeProduct
import com.meridiane.lection3.presentation.recyclerView.product.RcPreView
import com.meridiane.lection3.presentation.recyclerView.product.RcViewPager
import com.meridiane.lection3.presentation.viewModel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentProductDetails : Fragment() {

    private lateinit var binding: FragmentProductDetalsBinding

    private val viewModel: ProductDetailsViewModel by activityViewModels()
    private val bundle = Bundle()

    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = arguments?.getString(Constants.SEND_ID_DETAILS_FRAGMENT)!!

        viewModel.getProductDetails(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productsState()

        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.textInput.setOnClickListener {
            showDialog(productsState())
        }

        binding.btAddOrder.setOnClickListener {
            if (viewModel.sizeProduct == "") {
                Toast.makeText(requireContext(), getString(R.string.not_size), Toast.LENGTH_LONG)
                    .show()
            } else {
                bundle.putString(Constants.SEND_ID_ADD_ORDER, id)
                findNavController().navigate(R.id.addOrderFragment2, bundle)
            }
        }
    }

    private fun productsState() : List<SizeProduct>? {

        var listSize: List<SizeProduct>? = null

        lifecycleScope.launch {
            viewModel.productsState.collectLatest { product ->

                if (product.title != null) {
                    binding.textPrice.text = product.price.toString()
                    binding.textProduct.text = product.title
                    binding.tittleAppBar.text =
                        if (product.title.length > 28) product.title.substring(0, 28)
                        else product.title
                    binding.textCatecory.text = product.department
                    binding.textDescription.text = product.description
                    product.images?.let { preView(it) }
                    product.images?.let { imagePage(it) }

                    listSize = product.sizes
                }
            }
        }
        return listSize
    }

    private fun imagePage(image: List<String>) {
        binding.viewPager2.adapter =
            when (image.size) {
                1 -> RcViewPager(mutableListOf(image[0], null, null))
                2 -> RcViewPager(mutableListOf(image[0], image[1], null))
                else -> RcViewPager(mutableListOf(image[0], image[1], image[2]))
            }
    }

    private fun preView(image: List<String>) {
        val rcPreView = when (image.size) {
            1 -> RcPreView(mutableListOf(image[0], null, null)) { item ->
                binding.viewPager2.currentItem = item
            }

            2 -> RcPreView(mutableListOf(image[0], image[1], null)) { item ->
                binding.viewPager2.currentItem = item
            }

            else -> RcPreView(mutableListOf(image[0], image[1], image[2])) { item ->
                binding.viewPager2.currentItem = item
            }
        }

        binding.rcPreView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcPreView.adapter = rcPreView
    }

    private fun showDialog(listSize: List<SizeProduct>?) {
        childFragmentManager
            .setFragmentResultListener(BottomDialogFragment.REQUEST_KEY, this) { _, bundle ->

            val result = bundle.getString(BottomDialogFragment.BUNDLE_KEY)

            binding.textInput.setText(result)
            viewModel.sizeProduct = result.toString()
        }
        val modalBottomSheet = listSize?.let {
            BottomDialogFragment(it) { size ->
                viewModel.sizeProduct = size
            }
        }
        modalBottomSheet?.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        modalBottomSheet?.show(childFragmentManager, BottomDialogFragment.TAG)
    }

}