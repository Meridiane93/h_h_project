package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.meridiane.lection3.data.repository.MockRepository
import com.meridiane.lection3.domain.models.Product
import com.meridiane.lection3.domain.useCaseProducts.GetProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getProducts: GetProducts) : ViewModel() {

    private var _productsState = MutableStateFlow<PagingData<Product>>(PagingData.empty())
    val productsState: StateFlow<PagingData<Product>> = _productsState


    fun getProduct() {

        viewModelScope.launch {

            getProducts.productList().cachedIn(viewModelScope)
                .collectLatest {

                    _productsState.value = it
                }
        }

    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}


