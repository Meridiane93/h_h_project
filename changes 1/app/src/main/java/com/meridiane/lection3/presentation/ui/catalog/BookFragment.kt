package com.meridiane.lection3.presentation.ui.catalog

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meridiane.lection3.R
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.databinding.FragmentProductMainBinding
import com.meridiane.lection3.presentation.recyclerView.ActionListener
import com.meridiane.lection3.presentation.recyclerView.ProductAdapter
import com.meridiane.lection3.presentation.viewModel.MainViewModel

class BookFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentProductMainBinding? = null
    private val binding get() = _binding!!

    private val productsAdapter by lazy {
        ProductAdapter(object : ActionListener {
            override fun detailsProduct(product: Product) {
                Toast.makeText(context, "Product: $product", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductMainBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val divider = DividerItemDecoration(context,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(view.context, R.drawable.divider)!!)
        binding.rcView.addItemDecoration(divider)
        getData(binding)

        binding.rcView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = productsAdapter
        }

        viewModel.liveData.observe(viewLifecycleOwner) {
            productsAdapter.submitList(it)
            binding.container.state = ProgressContainer.State.Success
        }

        viewModel.liveDataError.observe(viewLifecycleOwner) {
            binding.container.state = ProgressContainer.State.Notice(it)
            binding.container.checkError(it)
        }

        binding.container.setListener {
            getData(binding)
        }

        binding.idProfile.setOnClickListener {
            Toast.makeText(context, "Профиль", Toast.LENGTH_LONG).show()
        }
        scrollListener()
    }

    private fun getData(binding: FragmentProductMainBinding) {

        viewModel.start()
        binding.container.state = ProgressContainer.State.Loading
        binding.container.visibility = View.VISIBLE
    }

    private fun scrollListener() = with(binding.rcView){
        addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {

                        // тут будут запускаться методы подгрузки данных порциями

                    Toast.makeText(context,"Больше нет продуктов",Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}