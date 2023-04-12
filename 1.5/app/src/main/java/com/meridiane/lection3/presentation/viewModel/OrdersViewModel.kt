package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.meridiane.lection3.domain.models.Order
import com.meridiane.lection3.domain.useCaseProfile.GetAllOrderInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val interfaceGetAllOrdersRepository: GetAllOrderInterface) :
    ViewModel() {

    private var _ordersState = MutableStateFlow<PagingData<Order>>(PagingData.empty())
    val ordersState: StateFlow<PagingData<Order>> = _ordersState


    fun getOrders() {

        viewModelScope.launch {

            interfaceGetAllOrdersRepository.getAllOrder().cachedIn(viewModelScope)
                .collectLatest {

                    _ordersState.value = it
                }
        }

    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}


