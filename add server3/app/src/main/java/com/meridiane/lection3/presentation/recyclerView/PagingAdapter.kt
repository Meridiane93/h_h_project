package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.domain.entity.product.Product
import com.meridiane.lection3.databinding.ProductFragmentBinding

class PagingAdapter(private var onItemClicked: ((product: Product) -> Unit)) :
    PagingDataAdapter<Product, PagingAdapter.ProductViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ProductFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val itemBinding: ProductFragmentBinding,
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(product: Product?) = with(itemBinding) {
            if(product != null) {
                textProduct.text = if (product.title!!.length > 16) product.title.substring(
                    0,
                    16
                ) else product.title
                textCatecory.text = product.category
                imageProduct.load(product.preview!!)

                itemView.setOnClickListener {
                    onItemClicked(product)
                }
            }
        }

    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

}