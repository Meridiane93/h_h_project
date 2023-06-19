package com.meridiane.lection3.presentation.recyclerView.orders

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.OrderFragmentBinding
import com.meridiane.lection3.domain.entity.product.PagingClass

class AllOrderAdapter(private var onItemClicked: ((product: PagingClass, numberOrder: Int) -> Unit)) :
    PagingDataAdapter<PagingClass, AllOrderAdapter.ProductViewHolder>(DiffUtilCallBack()) {

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

            textSize.text = order?.productSize

            if(order?.status == "cancelled"){

                imageCancel.visibility = View.GONE
                prBarCancel.visibility = View.GONE

                textStatus.setText(R.string.canceled_status)
                textStatus.setTextColor(
                    ContextCompat.getColor(
                        itemBinding.root.context,
                        R.color.cancel_order_status
                    )
                )

                textaddress.text = itemBinding.root.context.getString(
                    R.string.cancel_order_status,
                    day, month,year,time
                )
                textaddress.typeface = Typeface.DEFAULT_BOLD

                imageProduct.load(order.productPreview!!)
                imageProduct.alpha = 0.2f

            } else {
                imageCancel.visibility = View.VISIBLE
                textaddress.typeface = Typeface.DEFAULT
                textdata.text = itemBinding.root.context.getString(
                    R.string.date_string,
                    day,
                    month,
                    year,
                    time
                )

                textaddress.text = itemBinding.root.context.getString(
                    R.string.adress_order_string,
                    order?.deliveryAddress
                )

                imageProduct.alpha = 1.0f
                imageProduct.load(order?.productPreview!!)

                textStatus.setText(R.string.in_work)
                textStatus.setTextColor(
                    ContextCompat.getColor(
                        itemBinding.root.context,
                        R.color.work_order_status
                    )
                )
            }

            imageCancel.setOnClickListener {
                val menu = PopupMenu(itemBinding.root.context, imageCancel)
                menu.inflate(R.menu.menu_order)
                menu.show()
                menu.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.cancel) {
                        prBarCancel.visibility = View.VISIBLE
                        imageCancel.visibility = View.INVISIBLE
                        onItemClicked(order,absoluteAdapterPosition)
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