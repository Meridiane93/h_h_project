package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.meridiane.lection3.databinding.BottomDialogItemLayoutBinding
import com.meridiane.lection3.presentation.recyclerView.RcAdapterBottomItem

open class BottomDialogItemFragment(
    val list: Array<String>,

) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomDialogItemLayoutBinding

    private val rcAdapterBottomAndPreView = RcAdapterBottomItem(list) { data ->
        setFragmentResult("request_key_item", bundleOf("bundleKey_item" to data))
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomDialogItemLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcBottomItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rcBottomItem.adapter = rcAdapterBottomAndPreView
    }

    companion object {
        const val TAG = "BottomDialogFragmentItem"
    }

}