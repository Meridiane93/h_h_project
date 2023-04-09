package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.domain.models.Product
import com.meridiane.lection3.domain.useCaseProducts.GetProductDetailsInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val getProductDetailsInterface: GetProductDetailsInterface) :
    ViewModel() {

    private var _productsState = MutableStateFlow(Product())
    val productsState: StateFlow<Product> = _productsState


    fun getProductDetails(id: String) {

        viewModelScope.launch {

            getProductDetailsInterface.product(id).collectLatest { productDetails ->

                _productsState.value = productDetails
            }
        }

    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}


