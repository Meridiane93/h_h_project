package com.meridiane.lection3.presentation.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.databinding.FragmentProductMainBinding
import com.meridiane.lection3.presentation.recyclerView.DefaultLoadStateAdapter
import com.meridiane.lection3.presentation.recyclerView.PagingAdapter
import com.meridiane.lection3.presentation.recyclerView.TryAgainAction
import com.meridiane.lection3.presentation.viewModel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class BookFragment : Fragment(), PagingAdapter.OnClickListener {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentProductMainBinding
    private lateinit var mainLoadStateHolder: DefaultLoadStateAdapter.Holder

    private val productsAdapter by lazy {
        PagingAdapter(this)
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

        getData(binding)
        setupUsersList()

        lifecycleScope.launch {
            viewModel.productsState.collectLatest {
                productsAdapter.submitData(it)
            }
        }

        binding.container.setListener {
            getData(binding)
            viewModel.getProduct()
        }

        binding.idProfile.setOnClickListener {
            Toast.makeText(context, "Профиль", Toast.LENGTH_LONG).show()
        }

        viewModel.getProduct()

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

            binding.container.state = when (state.source.refresh) {
                is LoadState.Error -> ProgressContainer.State.Notice("Ошибка")
                is LoadState.Loading -> ProgressContainer.State.Loading
                is LoadState.NotLoading -> {
                    if (productsAdapter.itemCount == 0) {
                        ProgressContainer.State.Notice("Пустота")

                    } else {
                        ProgressContainer.State.Success
                    }
                }

            }
        }


    }

    private fun getData(binding: FragmentProductMainBinding) {
        binding.container.state = ProgressContainer.State.Loading
        binding.container.visibility = View.VISIBLE
    }

    override fun onClick(product: Product) {
        Toast.makeText(binding.root.context, "$product", Toast.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookFragment()
    }

}