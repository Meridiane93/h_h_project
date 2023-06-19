package com.meridiane.lection3.presentation.recyclerView.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meridiane.lection3.databinding.RcViewBottomItemBinding

class RcAdapterBottomItem(
    val list: Array<String>,
    private var onItemClicked: ((size: String) -> Unit)
) :
    RecyclerView.Adapter<RcAdapterBottomItem.ImageHolder>() {

    private val mainArray = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val binding =
            RcViewBottomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) =
        holder.setData(mainArray[position])

    override fun getItemCount(): Int = mainArray.size

    inner class ImageHolder(private val binding: RcViewBottomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(size: String) {
            binding.textBottomItem.text = size
            binding.textBottomItem.setOnClickListener {
                onItemClicked(size)
            }
        }
    }

}