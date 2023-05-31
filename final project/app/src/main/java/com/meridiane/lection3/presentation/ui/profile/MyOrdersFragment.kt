package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentMyOrdersBinding
import com.meridiane.lection3.presentation.recyclerView.orders.PagerAdapterMyOrders
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrdersFragment : Fragment() {

    private lateinit var binding: FragmentMyOrdersBinding
    private val viewModel: OrdersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.container.state = ProgressContainer.State.Loading
        binding.container.visibility = View.VISIBLE


        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }
        initial()

        lifecycleScope.launch {
            viewModel.stateFlowAllOrder.collect {value ->
                TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
                    when (pos) {
                        0 -> tab.text = getString(R.string.all_order_tab, value)
                        1 -> tab.text = getString(R.string.active_order_tab, viewModel.stateFlowActiveOrder.value)
                    }
                }.attach()
            }

        }

        lifecycleScope.launch {
            viewModel.stateFlowActiveOrder.collect { value ->
                TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
                    when (pos) {
                        0 -> tab.text = getString(R.string.all_order_tab, viewModel.stateFlowAllOrder.value)
                        1 -> tab.text = getString(R.string.active_order_tab, value)
                    }
                }.attach()
            }
        }

    }

    private fun initial() {
        binding.container.state = ProgressContainer.State.Success
        binding.viewPagerMyOrders.adapter = PagerAdapterMyOrders(this)
        binding.tabsLayout.tabIconTint = null
        TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
            when (pos) {
                0 -> tab.text = getString(R.string.all_order_tab)
                1 -> tab.text = getString(R.string.active_order_tab)
            }
        }.attach()

    }

}