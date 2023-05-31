package com.meridiane.lection3.presentation.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.lection3.Constants
import com.meridiane.lection3.R
import com.meridiane.lection3.databinding.FragmentProductMainBinding
import com.meridiane.lection3.presentation.recyclerView.product.DefaultLoadStateAdapter
import com.meridiane.lection3.presentation.recyclerView.product.PagingAdapter
import com.meridiane.lection3.presentation.recyclerView.TryAgainAction
import com.meridiane.lection3.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentProductMainBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder
    private val bundle = Bundle()

    private val productsAdapter by lazy {
        PagingAdapter { product ->
            bundle.putString(Constants.SEND_ID_DETAILS_FRAGMENT, product.id)
            findNavController().navigate(R.id.fragmentProductDetails2, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProduct()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUsersList()

        lifecycleScope.launch {
            viewModel.productsState.collectLatest {
                productsAdapter.submitData(it)
            }
        }

        binding.container.setListener {
            binding.container.state = ProgressContainer.State.Loading
            viewModel.getProduct()
        }

        binding.idProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

    }

    private fun setupUsersList() {

        val tryAgainAction: TryAgainAction = { productsAdapter.retry() }

        val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)

        val adapterWithLoadState = productsAdapter.withLoadStateFooter(footerAdapter)

        binding.rcView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterWithLoadState

        }
        (binding.rcView.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false

        mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            tryAgainAction
        )

        productsAdapter.addLoadStateListener { state ->
            binding.container.state = when (val currentState = state.source.refresh) {
                is LoadState.Error -> ProgressContainer.State.Notice("${currentState.error}")
                is LoadState.Loading -> ProgressContainer.State.Loading
                is LoadState.NotLoading -> {
                    if (productsAdapter.itemCount == 0) {
                        ProgressContainer.State.Notice(getString(R.string.empty))
                    } else {
                        ProgressContainer.State.Success
                    }
                }

            }
        }
    }

}