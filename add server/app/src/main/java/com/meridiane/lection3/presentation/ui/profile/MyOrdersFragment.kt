package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentMyOrdersBinding
import com.meridiane.lection3.presentation.recyclerView.PagerAdapterMyOrders
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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

        viewModel.getOrders()
        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }
        initial(0,0)

        lifecycleScope.launch {
            viewModel.stateFlowAllOrder.collect {value ->
                TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
                    when (pos) {
                        0 -> tab.text = "Все $value"
                        1 -> tab.text = "Активные ${viewModel.stateFlowActiveOrder.value}"
                    }
                }.attach()
            }

        }

        lifecycleScope.launch {
            viewModel.stateFlowActiveOrder.collect { value ->
                TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
                    when (pos) {
                        0 -> tab.text = "Все ${viewModel.stateFlowAllOrder.value}"
                        1 -> tab.text = "Активные $value"
                    }
                }.attach()
            }
        }

    }

    private fun initial(c1:Int , c2:Int) {
        binding.container.state = ProgressContainer.State.Success
        binding.viewPagerMyOrders.adapter = PagerAdapterMyOrders(this)
        binding.tabsLayout.tabIconTint = null
        TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Все $c1"
                1 -> tab.text = "Активные $c2"
            }
        }.attach()

    }

}