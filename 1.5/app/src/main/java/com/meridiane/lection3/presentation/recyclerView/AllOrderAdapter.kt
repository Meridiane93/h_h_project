package com.meridiane.lection3.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.databinding.OrderFragmentBinding
import com.meridiane.lection3.domain.models.Order

class AllOrderAdapter(private var onItemClicked: ((product: Order) -> Unit)) :
    PagingDataAdapter<Order, AllOrderAdapter.ProductViewHolder>(DiffUtilCallBack()) {

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

        fun bind(order: Order?) = with(itemBinding) {
            textOrder.text = order?.numberOrder
            textStatus.text = order?.status
            textSize.text = order?.size
            textdata.text = order?.dateGet
            textaddress.text = order?.address
            imageProduct.load(order?.image!!)

            itemView.setOnClickListener {
                onItemClicked(order)
            }
        }

    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Order>() {

        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
    }

}