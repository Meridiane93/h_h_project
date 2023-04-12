package com.meridiane.lection3.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.lection3.databinding.FragmentAllOrdersBinding
import com.meridiane.lection3.domain.models.Order
import com.meridiane.lection3.presentation.recyclerView.*
import com.meridiane.lection3.presentation.viewModel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllOrdersFragment : Fragment() {

    private lateinit var binding: FragmentAllOrdersBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder
    private val viewModel: OrdersViewModel by viewModels()

    private val allOrdersAdapter by lazy {
        AllOrderAdapter { order ->
            Toast.makeText(requireContext(),"$order", Toast.LENGTH_SHORT).show()
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

        setupUsersList()

        lifecycleScope.launch {
            viewModel.ordersState.collectLatest {
                allOrdersAdapter.submitData(it)
                Toast.makeText(requireContext(),"${allOrdersAdapter.submitData(it)}", Toast.LENGTH_SHORT).show()
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