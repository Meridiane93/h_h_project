package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.filter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentActiveOrdersBinding
import com.meridiane.lection3.presentation.recyclerView.product.DefaultLoadStateAdapter
import com.meridiane.lection3.presentation.recyclerView.orders.ActiveOrderAdapter
import com.meridiane.lection3.presentation.recyclerView.orders.ActiveOrderStateAdapter
import com.meridiane.lection3.presentation.recyclerView.orders.TryActiveOrder
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActiveOrdersFragment : Fragment() {

    private lateinit var binding: FragmentActiveOrdersBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder
    private val viewModel: OrdersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveOrdersBinding.inflate(inflater, container, false)
        return binding.root

    }

    private val activeOrdersAdapter by lazy {
        ActiveOrderAdapter { order, numberOrder ->
            viewModel.cancelOrder(order.id!!)
            viewModel.cancelNumberOrder = numberOrder
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerState.state = ProgressContainer.State.Loading

        setupUsersList()

        lifecycleScope.launch {
            viewModel.testStateFlow.collectLatest {product ->

                activeOrdersAdapter.submitData(product.filter { item ->
                    item.status!!.contains(getString(R.string.check_status_order), true)
                })
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            activeOrdersAdapter.refresh()
        }

        stateAdapter(binding)

        lifecycleScope.launch {
            viewModel.ordersStateCancelSuccess.collect {value ->
                if (value != "") {
                    viewModel.ordersStateCancelSuccess.value = ""
                    Snackbar.make(
                        binding.container,
                        "Заказ №$value от ${viewModel.cancelNumberOrder} был успешно отменён",
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
            }
        }

        lifecycleScope.launch {
            viewModel.ordersStateCancelException.collect { except ->
                if (except != "") {
                    Snackbar.make(
                        binding.container,
                        "Произошла ошибка во время отмены заказа",
                        Snackbar.LENGTH_LONG
                    )
                        .setBackgroundTint(
                            ContextCompat.getColor(requireContext(), R.color.error_snack_bar)
                        )
                        .setActionTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        .show()
                }
            }
        }

    }

    private fun stateAdapter(binding: FragmentActiveOrdersBinding) {

        activeOrdersAdapter.addLoadStateListener { state ->

            binding.swipeRefreshLayout.isRefreshing = false
            binding.containerState.state = when (state.source.refresh) {
                is LoadState.Error -> {
                    ProgressContainer.State.Notice(getString(R.string.mistake))
                }

                is LoadState.Loading -> {
                    ProgressContainer.State.Loading
                }

                is LoadState.NotLoading -> {
                    if (activeOrdersAdapter.itemCount == 0) {
                        ProgressContainer.State.Notice(getString(R.string.empty))
                    } else {
                        ProgressContainer.State.Success
                    }
                }

            }
        }
    }

    private fun setupUsersList() {

        val tryAgainAction: TryActiveOrder = { activeOrdersAdapter.retry() }

        val footerAdapter = ActiveOrderStateAdapter(tryAgainAction)

        val adapterWithLoadState = activeOrdersAdapter.withLoadStateFooter(footerAdapter)

        binding.recyclerViewActiveOrders.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterWithLoadState

        }
        (binding.recyclerViewActiveOrders.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations =
            false

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )
    }

}