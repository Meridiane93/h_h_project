package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.databinding.ProductFragmentBinding

interface ActionListener {
    fun detailsProduct(product: Product)
}

class PagingAdapter(private val actionListener: ActionListener) :
    PagingDataAdapter<Product, PagingAdapter.ProductViewHolder>(DiffUtilCallBack()),
    View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ProductFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position)) ?: return
    }

    inner class ProductViewHolder(private val itemBinding: ProductFragmentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(product: Product?) = with(itemBinding) {
            textProduct.text = product?.title
            textCatecory.text = product?.category
            imageProduct.load(product?.preview!!)

            itemView.setOnClickListener(this@PagingAdapter)
        }
    }

    override fun onClick(v: View) {
        val product = v.tag as Product
        actionListener.detailsProduct(product)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

}