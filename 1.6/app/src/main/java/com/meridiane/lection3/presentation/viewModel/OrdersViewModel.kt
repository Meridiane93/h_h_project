package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.meridiane.lection3.domain.entity.Order
//import com.meridiane.lection3.domain.useCaseProfile.GetActiveOrderInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor() :
    ViewModel() {

    private var _ordersState = MutableStateFlow<PagingData<Order>>(PagingData.empty())
    val ordersState: StateFlow<PagingData<Order>> = _ordersState

    private var _ordersStateActiveOrder = MutableStateFlow<PagingData<Order>>(PagingData.empty())
    val ordersStateActiveOrder: StateFlow<PagingData<Order>> = _ordersStateActiveOrder

    val stateFlowAllOrder = MutableStateFlow(0)
    val stateFlowActiveOrder = MutableStateFlow(0)

    fun getOrders() {

        viewModelScope.launch {

//            getActiveOrderInterface.getListOrder().cachedIn(viewModelScope)
//                .collectLatest {
//
//                    _ordersState.value = it
//                }
        }

    }

    fun getActiveOrders() {

        viewModelScope.launch {

//            getActiveOrderInterface.getListActiveOrder().cachedIn(viewModelScope)
//                .collectLatest {
//
//                    _ordersStateActiveOrder.value = it
//                }
        }

    }

    fun getAllOrderSize(){
        viewModelScope.launch {
//            stateFlowAllOrder.emit(getActiveOrderInterface.getSizeAllOrder())
        }
    }

    fun getActiveOrderSize(){
        viewModelScope.launch {
//            stateFlowActiveOrder.emit(getActiveOrderInterface.getSizeActiveOrder())
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}


