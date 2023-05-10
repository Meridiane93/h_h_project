package com.meridiane.lection3.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.ApiState
import com.meridiane.lection3.domain.entity.AddOrder
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import com.meridiane.lection3.domain.repository.InterfaceGetProductDetailsRepository
import com.meridiane.lection3.domain.useCaseProducts.GetProductDetailInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailInterface: GetProductDetailInterface
) : ViewModel() {

    private var _productsState = MutableStateFlow(ProductDetail())
    val productsState: StateFlow<ProductDetail> = _productsState

    fun getProductDetails(id: String) {

        viewModelScope.launch {

            getProductDetailInterface.getDetailProduct(id).collectLatest { productDetails ->

                _productsState.value = productDetails
            }
        }
    }

    fun addOrder(order:AddOrder){
        viewModelScope.launch {
            try {
                val getProf = getProductDetailInterface.addOrder(order)!!
                Log.d("MyTag",getProf.isSuccess.toString())
                Log.d("MyTag","VM $getProf")
            } catch (e: Exception) {
                Log.d("MyTag","Except $e")
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}


