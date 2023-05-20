package com.meridiane.lection3.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.meridiane.lection3.domain.entity.AllOrder
import com.meridiane.lection3.domain.useCaseProfile.GetActiveOrderInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val getActiveOrderInterface: GetActiveOrderInterface) :
    ViewModel() {

    private var allOrder = AllOrder()
    private val localChangesFlow = MutableStateFlow(OnChange(allOrder))

    private var _ordersState = MutableStateFlow<PagingData<AllOrder>>(PagingData.empty())
    val ordersState: StateFlow<PagingData<AllOrder>> = _ordersState

    var stateFlowAllOrder = MutableStateFlow(0) // количество всех заказов
    val stateFlowActiveOrder = MutableStateFlow(0) // количество активных заказов

    var _ordersStateCancel = MutableStateFlow(AllOrder())
    var _ordersStateCancelExeption = MutableStateFlow(String())

    var positionСlickOrder = 0 // позиция заказа который был отменён
    var oldOrder = AllOrder() // старый ордер


    fun getOrders() {
        viewModelScope.launch {
            getActiveOrderInterface.getListOrder().cachedIn(viewModelScope)
                .collectLatest {
                    _ordersState.value = it
                }

        }

    }

    fun cancelOrder(id:String){
        viewModelScope.launch {
            try {
                val answer =  getActiveOrderInterface.cancelOrder(id)
                Log.d("MyTag", "cancelOrder $answer")
                    _ordersStateCancel.value = answer.getOrNull()!!


                _ordersState.value = combine(
                    _ordersStateCancel,
                    _ordersState.debounce(50),
                    this::merge
                )
            } catch (e: Exception) {
                Log.d("MyTag", "Except $e")
                _ordersStateCancelExeption.value = e.message.toString()
            }
        }
    }




    override fun onCleared() {
        viewModelScope.cancel()
    }

    class OnChange<T>(val value: T)
}


