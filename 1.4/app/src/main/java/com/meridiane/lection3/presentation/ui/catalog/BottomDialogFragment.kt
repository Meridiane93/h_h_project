package com.meridiane.lection3.presentation.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.meridiane.lection3.OnItemBottomClick
import com.meridiane.lection3.databinding.BottomDialogLayoutBinding
import com.meridiane.lection3.presentation.recyclerView.RcAdapterBottom

open class BottomDialogFragment: BottomSheetDialogFragment() {

    private lateinit var binding: BottomDialogLayoutBinding

    private val rcAdapterBottomAndPreView = RcAdapterBottom { result ->
         val ob = object : OnItemBottomClick{
             override fun size(size: String): String = result
         }
        ob.size(result)
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
    }
}