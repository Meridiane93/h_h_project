package com.meridiane.lection3.presentation.ui.profile

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.google.android.material.tabs.TabLayoutMediator
import com.meridiane.lection3.databinding.FragmentMyOrdersBinding
import com.meridiane.lection3.presentation.recyclerView.AllOrderAdapter
import com.meridiane.lection3.presentation.recyclerView.PagerAdapterMyOrders
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MyOrdersFragment : Fragment() {

    private lateinit var binding: FragmentMyOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()

    private val allOrdersAdapter by lazy {
        AllOrderAdapter { order ->
            Toast.makeText(requireContext(), "$order", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initial()
        viewModel.getOrders()

//        setFragmentResult("request_key", bundleOf("bundleKey" to data))

//        lifecycleScope.launch {
//            viewModel.ordersState.collectLatest {
//
//            }
//        }

        allOrdersAdapter.addLoadStateListener { state ->

            binding.container.state = when (state.source.refresh) {
                is LoadState.Error -> ProgressContainer.State.Notice("Ошибка")
                is LoadState.Loading -> ProgressContainer.State.Loading
                is LoadState.NotLoading -> {
                    if (allOrdersAdapter.itemCount == 0) {
                        ProgressContainer.State.Notice("Пустота")

                    } else {
                        ProgressContainer.State.Success
                    }
                }

            }
        }

    }

    private fun initial() {
        binding.viewPagerMyOrders.adapter = PagerAdapterMyOrders(this)
        binding.tabsLayout.tabIconTint = null
        TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Все "
                1 -> tab.text = "Активные "
            }
        }.attach()

    }

}