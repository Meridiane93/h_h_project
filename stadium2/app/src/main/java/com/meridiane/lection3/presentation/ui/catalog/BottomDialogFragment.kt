package com.meridiane.lection3.presentation.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.meridiane.lection3.databinding.BottomDialogLayoutBinding
import com.meridiane.lection3.domain.entity.product_detail.SizeProduct
import com.meridiane.lection3.presentation.recyclerView.product.RcAdapterBottom

open class BottomDialogFragment(
    val list: List<SizeProduct>,
    private var onItemClicked: (size: String) -> Unit

) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomDialogLayoutBinding

    private val rcAdapterBottomAndPreView = RcAdapterBottom(list) { data ->
        onItemClicked(data)
        setFragmentResult(REQUEST_KEY, bundleOf(BUNDLE_KEY to data))
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcBottom.layoutManager = LinearLayoutManager(requireContext())
        binding.rcBottom.adapter = rcAdapterBottomAndPreView
    }

    companion object {
        const val TAG = "BottomDialogFragment"
        const val REQUEST_KEY = "request_key"
        const val BUNDLE_KEY = "bundleKey"
    }

}