package com.meridiane.lection3.presentation.recyclerView.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.meridiane.lection3.databinding.PartDefaultLoadStateBinding

typealias TryActiveOrder = () -> Unit

class ActiveOrderStateAdapter(
    private val tryAgainAction: TryActiveOrder
) : LoadStateAdapter<ActiveOrderStateAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val binding = PartDefaultLoadStateBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return Holder(binding, null, tryAgainAction)
    }

    class Holder(
        private val binding: PartDefaultLoadStateBinding,
        private val swipeRefreshLayout: SwipeRefreshLayout?,
        private val tryAgainAction: TryActiveOrder
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tryAgainButton.setOnClickListener { tryAgainAction() }
        }

        fun bind(loadState: LoadState) = with(binding) {

            messageTextView.isVisible = loadState is LoadState.Error
            tryAgainButton.isVisible = loadState is LoadState.Error

            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.isRefreshing = loadState is LoadState.Loading
                progressBar.isVisible = false
            } else progressBar.isVisible = loadState is LoadState.Loading

        }
    }

}