package com.meridiane.lection3.presentation.recyclerView.orders

import android.util.Log
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
import com.meridiane.lection3.domain.entity.AllOrder

class AllOrderAdapter(private var onItemClicked: ((product: AllOrder,position:Int) -> Unit)) :
    PagingDataAdapter<AllOrder, AllOrderAdapter.ProductViewHolder>(DiffUtilCallBack()) {

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

            prBarCancel.visibility = View.INVISIBLE
            imageCancel.visibility = View.VISIBLE

            imageCancel.setOnClickListener {
                val menu = PopupMenu(itemBinding.root.context,imageCancel)
                menu.inflate(R.menu.menu_order)
                menu.show()
                menu.setOnMenuItemClickListener { item ->
                    if (item.itemId == R.id.cancel){
                        prBarCancel.visibility = View.VISIBLE
                        imageCancel.visibility = View.INVISIBLE
                        onItemClicked(order,absoluteAdapterPosition)
                    }
                   return@setOnMenuItemClickListener true
                }
            }
        }

    }

    fun cancelOrder(position: Int,newOrder: AllOrder,oldOrder:AllOrder ){
        Log.d("MyTag","cancelOrder: $position,,,$newOrder,,,$oldOrder")
        DiffUtilCallBack().areContentsTheSame(oldOrder,newOrder)
        notifyItemChanged(position)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<AllOrder>() {

        override fun areItemsTheSame(oldItem: AllOrder, newItem: AllOrder): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AllOrder, newItem: AllOrder): Boolean =
            oldItem == newItem
    }

}