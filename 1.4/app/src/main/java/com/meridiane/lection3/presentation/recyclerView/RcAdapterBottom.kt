package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meridiane.lection3.databinding.RcViewBottomBinding

class RcAdapterBottom(private var onItemClicked: ((size: String) -> Unit)) :
    RecyclerView.Adapter<RcAdapterBottom.ImageHolder>() {

    val mainArray = listOf("S", "M", "L", "XL") // список размеров

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding =
            RcViewBottomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) =
        holder.setData(mainArray[position])

    override fun getItemCount(): Int = mainArray.size

    inner class ImageHolder(private val binding: RcViewBottomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(size: String) {
            binding.textBottom.text = size
            binding.textBottom.setOnClickListener {
                onItemClicked(size)
            }
        }
    }

}