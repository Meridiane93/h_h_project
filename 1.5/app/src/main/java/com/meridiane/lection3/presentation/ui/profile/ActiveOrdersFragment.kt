package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.lection3.databinding.FragmentActiveOrdersBinding
import com.meridiane.lection3.presentation.recyclerView.AllOrderAdapter
import com.meridiane.lection3.presentation.recyclerView.AllOrderStateAdapter
import com.meridiane.lection3.presentation.recyclerView.DefaultLoadStateAdapter
import com.meridiane.lection3.presentation.recyclerView.TryAgainActionAllOrder
import com.meridiane.lection3.presentation.ui.catalog.ProgressContainer
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActiveOrdersFragment : Fragment() {

    private lateinit var binding: FragmentActiveOrdersBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder
    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActiveOrdersBinding.inflate(inflater, container, false)
        return binding.root

    }

    private val allOrdersAdapter by lazy {
        AllOrderAdapter { order ->
            Toast.makeText(requireContext(),"$order", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerState.state = ProgressContainer.State.Loading

        setupUsersList()
        viewModel.getActiveOrders()

        lifecycleScope.launch {
            viewModel.ordersStateActiveOrder.collectLatest {
                allOrdersAdapter.submitData(it)
            }
        }

        stateAdapter(binding)

    }

    private fun stateAdapter(binding:FragmentActiveOrdersBinding){
        allOrdersAdapter.addLoadStateListener { state ->

            binding.containerState.state = when (state.source.refresh) {
                is LoadState.Error -> {
                    ProgressContainer.State.Notice("Ошибка")
                }
                is LoadState.Loading -> {
                    ProgressContainer.State.Loading
                }
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