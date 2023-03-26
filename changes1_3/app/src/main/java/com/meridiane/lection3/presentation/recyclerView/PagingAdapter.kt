package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.databinding.ProductFragmentBinding

class PagingAdapter(val listener: OnClickListener) :
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
        holder.bind(getItem(position),listener)
    }

    inner class ProductViewHolder(
        private val itemBinding: ProductFragmentBinding,
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(product: Product? , listener: OnClickListener) = with(itemBinding) {
            textProduct.text = product?.title
            textCatecory.text = product?.category
            imageProduct.load(product?.preview!!)

            itemView.setOnClickListener {
                val index = absoluteAdapterPosition
                Toast.makeText(itemBinding.root.context, "$index", Toast.LENGTH_LONG).show()
                listener.onClick(product)
            }
        }

    }

    interface OnClickListener {
        fun onClick(product: Product)
    }


    class DiffUtilCallBack : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

}