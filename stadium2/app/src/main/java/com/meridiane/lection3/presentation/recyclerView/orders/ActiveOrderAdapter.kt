package com.meridiane.lection3.presentation.recyclerView.orders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.OrderFragmentBinding
import com.meridiane.lection3.domain.entity.product.PagingClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActiveOrderAdapter(private var onItemClicked: ((product: PagingClass, numberOrder: Int) -> Unit)) :
    PagingDataAdapter<PagingClass, ActiveOrderAdapter.ProductViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            OrderFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(

        private val itemBinding: OrderFragmentBinding,
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            with(itemBinding) {
                itemView.setOnClickListener {
                    val menu = PopupMenu(itemBinding.root.context, imageCancel)
                    menu.inflate(R.menu.menu_order)
                    menu.show()
                    menu.setOnMenuItemClickListener { item ->
                        if (item.itemId == R.id.cancel) {
                            prBarCancel.visibility = View.VISIBLE
                            imageCancel.visibility = View.INVISIBLE
                            onItemClicked(getItem(bindingAdapterPosition)!!, absoluteAdapterPosition)
                        }
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        }


        fun bind(order: PagingClass?) = with(itemBinding) {

            order?.let {
                val date = order.createdAt?.substringBefore('T')
                    ?.replace("-", ".", true)

                date?.let {
                    val day = date.substringAfterLast(".")
                    val month = date.substringBeforeLast('.').substringAfterLast(".")
                    val year = date.substringBefore('.').drop(2)
                    val time = order.createdAt.substringAfterLast("T").substringBeforeLast(":")

                    textOrder.text = itemBinding.root.context.getString(
                        R.string.date_string_order,
                        absoluteAdapterPosition + 1,
                        day,
                        month,
                        year,
                        time
                    )
                    textStatus.setText(R.string.in_work)
                    textSize.text = order.productSize

                    textdata.text = itemBinding.root.context.getString(
                        R.string.date_string,
                        day,
                        month,
                        year,
                        time
                    )
                    textaddress.text = order.deliveryAddress
                }

            }

            CoroutineScope(Dispatchers.Main).launch{
                val resized =
                    Bitmap.createScaledBitmap(getBitmap(order?.productPreview!!, itemBinding.root.context),
                        240, 240, true)
                val d: Drawable = BitmapDrawable(itemBinding.root.context.resources, resized)
                imageProduct.background = d
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

    class DiffUtilCallBack : DiffUtil.ItemCallback<PagingClass>() {

        override fun areItemsTheSame(oldItem: PagingClass, newItem: PagingClass): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PagingClass, newItem: PagingClass): Boolean =
            oldItem == newItem
    }

}