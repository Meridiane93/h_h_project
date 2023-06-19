package com.meridiane.lection3.presentation.recyclerView.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.OrderFragmentBinding
import com.meridiane.lection3.domain.entity.product.PagingClass

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

        fun bind(order: PagingClass?) = with(itemBinding) {

                val date = order?.createdAt?.substringBefore('T')
                    ?.replace("-", ".", true)
                val day = date?.substringAfterLast(".")
                val month = date?.substringBeforeLast('.')?.substringAfterLast(".")
                val year = date?.substringBefore('.')?.drop(2)
                val time = order?.createdAt?.substringAfterLast("T")?.substringBeforeLast(":")


                textOrder.text = itemBinding.root.context.getString(
                    R.string.date_string_order,
                    absoluteAdapterPosition + 1,
                    day,
                    month,
                    year,
                    time
                )
                textStatus.setText(R.string.in_work)
                textSize.text = order?.productSize

                textdata.text = itemBinding.root.context.getString(
                    R.string.date_string,
                    day,
                    month,
                    year,
                    time
                )
                textaddress.text = order?.deliveryAddress
                imageProduct.load(order?.productPreview!!)

                itemView.setOnClickListener {
                    val menu = PopupMenu(itemBinding.root.context, imageCancel)
                    menu.inflate(R.menu.menu_order)
                    menu.show()
                    menu.setOnMenuItemClickListener { item ->
                        if (item.itemId == R.id.cancel) {
                            prBarCancel.visibility = View.VISIBLE
                            imageCancel.visibility = View.INVISIBLE
                            onItemClicked(order, absoluteAdapterPosition)
                        }
                        return@setOnMenuItemClickListener true
                    }
                }

        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<PagingClass>() {

        override fun areItemsTheSame(oldItem: PagingClass, newItem: PagingClass): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PagingClass, newItem: PagingClass): Boolean =
            oldItem == newItem
    }

}