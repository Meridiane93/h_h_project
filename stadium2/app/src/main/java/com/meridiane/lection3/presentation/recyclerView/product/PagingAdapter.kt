package com.meridiane.lection3.presentation.recyclerView.product

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.meridiane.lection3.databinding.ProductFragmentBinding
import com.meridiane.lection3.domain.entity.product.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        init {
            itemBinding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { item -> onItemClicked(item) }
            }
        }

        fun bind(product: Product?) = with(itemBinding) {
            if (product != null) {
                textProduct.text = if (product.title!!.length > 16) product.title.substring(
                    0,
                    16
                ) else product.title
                textCatecory.text = product.category


                CoroutineScope(Dispatchers.Main).launch{
                    val resized =
                        Bitmap.createScaledBitmap(getBitmap(product.preview!!, itemBinding.root.context),
                            240, 240, true)
                    val d: Drawable = BitmapDrawable(itemBinding.root.context.resources, resized)
                    imageProduct.background = d
                }

            }
        }

    }

    suspend fun getBitmap(url: String, context: Context): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable

        return (result as BitmapDrawable).bitmap
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

}