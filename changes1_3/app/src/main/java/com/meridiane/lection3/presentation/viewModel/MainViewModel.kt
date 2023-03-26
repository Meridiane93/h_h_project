package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.domain.repository.MockRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainViewModel : ViewModel() {

    private var _productsState = MutableStateFlow<PagingData<Product>>(PagingData.empty())
    val productsState: StateFlow<PagingData<Product>> = _productsState

    private val repository = MockRepository()

    fun getProduct() {

        viewModelScope.launch {

            repository.getList().cachedIn(viewModelScope)
                .collectLatest {

                    _productsState.value = it
                }
        }

    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}


