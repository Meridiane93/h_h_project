package com.meridiane.lection3.presentation.recyclerView.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.meridiane.lection3.databinding.OrderFragmentBinding
import com.meridiane.lection3.domain.entity.AllOrder

class ActiveOrderAdapter(private var onItemClicked: ((product: AllOrder) -> Unit)) :
    PagingDataAdapter<AllOrder, ActiveOrderAdapter.ProductViewHolder>(DiffUtilCallBack()) {

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

        fun bind(order: AllOrder?) = with(itemBinding) {
            textOrder.text = order?.createdAt
            textStatus.text = order?.status
            textSize.text = order?.productSize
            textdata.text = order?.etd
            textaddress.text = order?.deliveryAddress
            imageProduct.load(order?.productPreview!!)

            itemView.setOnClickListener {
                onItemClicked(order)
            }
        }

    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<AllOrder>() {

        override fun areItemsTheSame(oldItem: AllOrder, newItem: AllOrder): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AllOrder, newItem: AllOrder): Boolean =
            oldItem == newItem
    }

}