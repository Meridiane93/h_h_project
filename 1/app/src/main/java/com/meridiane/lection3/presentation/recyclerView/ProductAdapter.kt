package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.databinding.ProductFragmentBinding


interface ActionListener{
    fun detailsProduct(product: Product)
}

class ProductAdapter(private val actionListener: ActionListener)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(), View.OnClickListener {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ProductFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.itemView.tag = differ.currentList[position]
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ProductViewHolder(private val itemBinding: ProductFragmentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(product: Product) = with(itemBinding) {
            textProduct.text = product.title
            textCatecory.text = product.category
            imageProduct.load(product.preview!!)

            itemView.setOnClickListener(this@ProductAdapter)
        }
    }

    fun submitList(products: List<Product>) {
        differ.submitList(products)
    }

    override fun onClick(v: View) {
        val product = v.tag as Product
        actionListener.detailsProduct(product)
    }

}

