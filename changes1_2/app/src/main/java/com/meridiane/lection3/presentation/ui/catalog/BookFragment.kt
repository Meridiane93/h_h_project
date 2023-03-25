package com.meridiane.lection3.presentation.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meridiane.lection3.R
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.databinding.FragmentProductMainBinding
import com.meridiane.lection3.presentation.recyclerView.ActionListener
import com.meridiane.lection3.presentation.recyclerView.PagingAdapter
import com.meridiane.lection3.presentation.viewModel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BookFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: FragmentProductMainBinding


    private val productsAdapter by lazy {
        PagingAdapter(object : ActionListener {
            override fun detailsProduct(product: Product) {
                Toast.makeText(context, "Product: $product", Toast.LENGTH_LONG).show()
            }
        })
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

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(view.context, R.drawable.divider)!!)
        binding.rcView.addItemDecoration(divider)

        binding.rcView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = productsAdapter.apply {
                addLoadStateListener { state ->

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
        }

        lifecycleScope.launch {
            viewModel.productsState.collectLatest {
                Log.d("MyTag","launch")
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

        getData(binding)

        viewModel.getProduct()
        scrollListener()
    }

    private fun getData(binding: FragmentProductMainBinding) {

        binding.container.state = ProgressContainer.State.Loading
        binding.container.visibility = View.VISIBLE
    }

    private fun scrollListener() = with(binding.rcView) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    Toast.makeText(context, "Загружаем данные", Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookFragment()
    }

}