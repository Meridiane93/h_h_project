package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentAllOrdersBinding
import com.meridiane.lection3.presentation.recyclerView.orders.AllOrderAdapter
import com.meridiane.lection3.presentation.recyclerView.orders.AllOrderStateAdapter
import com.meridiane.lection3.presentation.recyclerView.orders.TryAgainActionAllOrder
import com.meridiane.lection3.presentation.recyclerView.product.DefaultLoadStateAdapter
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllOrdersFragment : Fragment() {

    private lateinit var binding: FragmentAllOrdersBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder
    private val viewModel: OrdersViewModel by activityViewModels()

    private val allOrdersAdapter by lazy {
        AllOrderAdapter { order, numberOrder ->
            viewModel.cancelOrder(order.id!!)
            viewModel.cancelNumberOrder = numberOrder
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerState.state = ProgressContainer.State.Loading

        setupUsersList()

        testStateFlow()

        binding.swipeRefreshLayout.setOnRefreshListener {
            allOrdersAdapter.refresh()
        }

        stateAdapter(binding)

        ordersStateCancelException()
        ordersStateCancelSuccess()
    }

    private fun testStateFlow() {
        lifecycleScope.launch {
            viewModel.testStateFlow.collectLatest {
                allOrdersAdapter.submitData(it)
            }
        }
    }

    private fun ordersStateCancelException() {
        lifecycleScope.launch {
            viewModel.ordersStateCancelException.collect { except ->
                if (except != "") {
                    Snackbar.make(
                        binding.container,
                        getString(R.string.text_error_order),
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

    private fun ordersStateCancelSuccess() {
        lifecycleScope.launch {
            viewModel.ordersStateCancelSuccess.collect { value ->
                if (value != "") {
                    viewModel.ordersStateCancelSuccess.value = ""
                    Snackbar.make(
                        binding.container,
                        getString(R.string.text_cancel_order,value,viewModel.cancelNumberOrder),
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
    }

    private fun stateAdapter(binding: FragmentAllOrdersBinding){
        allOrdersAdapter.addLoadStateListener { state ->
            binding.swipeRefreshLayout.isRefreshing = false
            binding.containerState.state = when (state.source.refresh) {

                is LoadState.Error -> ProgressContainer.State.Notice(getString(R.string.mistake))
                is LoadState.Loading -> ProgressContainer.State.Loading

                is LoadState.NotLoading -> {
                    if (allOrdersAdapter.itemCount == 0)
                        ProgressContainer.State.Notice(getString(R.string.empty))
                     else
                        ProgressContainer.State.Success
                }

            }
        }
    }

    private fun setupUsersList() {

        val tryAgainAction: TryAgainActionAllOrder = { allOrdersAdapter.retry() }

        val footerAdapter = AllOrderStateAdapter(tryAgainAction)

        val adapterWithLoadState = allOrdersAdapter.withLoadStateFooter(footerAdapter)

        binding.recyclerViewAllOrders.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterWithLoadState

        }
        (binding.recyclerViewAllOrders.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )
    }

}