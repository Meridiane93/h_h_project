package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.domain.entity.order.AddOrder
import com.meridiane.lection3.domain.entity.product_detail.ProductDetail
import com.meridiane.lection3.domain.useCaseAuthorization.GetCoordinate
import com.meridiane.lection3.domain.useCaseAuthorization.SaveCoordinate
import com.meridiane.lection3.domain.useCaseProducts.GetProductDetailInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailInterface: GetProductDetailInterface,
    private val getCoordinate: GetCoordinate,
    private val saveCoordinate: SaveCoordinate
) : ViewModel() {

    var idProduct = "" // id продукта
    var sizeProduct = "" // размер продукта

    var stateFlow = MutableStateFlow(1) // количество заказанных продуктов

    private var _productsState = MutableStateFlow(ProductDetail())
    val productsState: StateFlow<ProductDetail> = _productsState

    val errorState = MutableStateFlow("")
    val successState = MutableStateFlow("")

    val coordinateState = MutableStateFlow("")

    fun getProductDetails(id: String) {

        viewModelScope.launch {

            getProductDetailInterface.getDetailProduct(id).collectLatest { productDetails ->

                _productsState.value = productDetails
            }
        }
    }

    fun addOrder(order: AddOrder) {
        viewModelScope.launch {
            try {
                val getProf = getProductDetailInterface.addOrder(order).isSuccess.toString()
                successState.value = getProf
            } catch (e: Exception) {
                errorState.value = e.toString()
            }
        }
    }

    fun getCoordinate() {
        coordinateState.value = getCoordinate.getCoordinateBd()
    }

    fun saveCoordinate(coordinate: String) {
        saveCoordinate.saveCoordinate(coordinate)
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }

}


