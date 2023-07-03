package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentMyOrdersBinding
import com.meridiane.lection3.presentation.recyclerView.orders.PagerAdapterMyOrders
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MyOrdersFragment : Fragment() {

    private lateinit var binding: FragmentMyOrdersBinding
    private val viewModel: OrdersViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tabOrders()
        viewModel.navigation = arguments?.getBoolean(Constants.SEND_ID_ADD_ORDER_FRAGMENT) ?: false
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
        binding.container.state = ProgressContainer.State.Loading
        binding.container.visibility = View.VISIBLE


        binding.btBack.setOnClickListener {
            if (!viewModel.navigation) findNavController().popBackStack()
            else {
                findNavController().navigate(R.id.bookFragment)
                viewModel.navigation = false
            }
        }
        initial()

        stateFlowAllOrder()

        stateFlowActiveOrder()

    }

    private fun stateFlowActiveOrder() {
        lifecycleScope.launch {
            viewModel.stateFlowActiveOrder.collect { value ->
                TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
                    when (pos) {
                        0 -> tab.text =
                            getString(R.string.all_order_tab, viewModel.stateFlowAllOrder.value)

                        1 -> tab.text = getString(R.string.active_order_tab, value)
                    }
                }.attach()
            }
        }
    }

    private fun stateFlowAllOrder() {
        lifecycleScope.launch {
            viewModel.stateFlowAllOrder.collect { value ->

                if (value != 0) {
                    if (viewModel.navigation) {
                        val sdf = SimpleDateFormat(
                            getString(R.string.date_format_orders),
                            Locale.getDefault()
                        )
                        val currentDate = sdf.format(Date())


                        Snackbar.make(
                            binding.constrainLayotMyOrder,
                            getString(R.string.text_add_order, value, currentDate),
                            Snackbar.LENGTH_LONG
                        )
                            .setBackgroundTint(
                                ContextCompat.getColor(requireContext(), R.color.purple_700)
                            )
                            .setActionTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            .show()
                    }

                    TabLayoutMediator(binding.tabsLayout, binding.viewPagerMyOrders) { tab, pos ->
                        when (pos) {
                            0 -> tab.text = getString(R.string.all_order_tab, value)
                            1 -> tab.text = getString(
                                R.string.active_order_tab,
                                viewModel.stateFlowActiveOrder.value
                            )
                        }
                    }.attach()
                }
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