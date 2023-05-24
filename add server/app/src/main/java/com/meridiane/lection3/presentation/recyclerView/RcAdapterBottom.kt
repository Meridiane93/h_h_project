package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meridiane.lection3.databinding.RcViewBottomBinding
import com.meridiane.lection3.domain.entity.product_detail.SizeProduct

class RcAdapterBottom(
    val list: List<SizeProduct>,
    private var onItemClicked: ((size: String) -> Unit)
) :
    RecyclerView.Adapter<RcAdapterBottom.ImageHolder>() {

    private val mainArray = list // список размеров

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

        fun setData(size: SizeProduct) {
            binding.textBottom.text = size.value
            binding.textBottom.setOnClickListener {
                onItemClicked(size.value!!)
            }
        }
    }

}