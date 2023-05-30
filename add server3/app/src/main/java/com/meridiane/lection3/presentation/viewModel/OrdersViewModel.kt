package com.meridiane.lection3.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.meridiane.lection3.domain.entity.AllOrder
import com.meridiane.lection3.domain.entity.product.PagingClass
import com.meridiane.lection3.domain.useCaseProfile.GetActiveOrderInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val getActiveOrderInterface: GetActiveOrderInterface) :
    ViewModel() {

    // изспользуем для обновления списка
    val testStateFlow : Flow<PagingData<PagingClass>>

    // список всех заказов
    var _ordersState: Flow<PagingData<AllOrder>> =
        getActiveOrderInterface.getListOrder().cachedIn(viewModelScope)

    var stateFlowAllOrder = MutableStateFlow(0) // количество всех заказов
    val stateFlowActiveOrder = MutableStateFlow(0) // количество активных заказов


    var _ordersStateCancelExeption = MutableStateFlow(String()) // ошибка при отмене ордера

    val _ordersStateCancel = MutableStateFlow(OnChange(AllOrder()))

    init {
        testStateFlow = combine(
            _ordersState,
            _ordersStateCancel,
            this@OrdersViewModel::merges
        )
    }

    fun cancelOrder(id: String) {
        viewModelScope.launch {
            try {
                _ordersStateCancel.value =  OnChange(getActiveOrderInterface.cancelOrder(id).getOrNull()!!)

            } catch (e: Exception) {
                _ordersStateCancelExeption.value = e.message.toString()
            }
        }
    }

    fun merges(users: PagingData<AllOrder>, localChanges: OnChange<AllOrder>): PagingData<PagingClass> {
        return users
            .map { user ->
                val product =  if (user.productId == localChanges.value.productId)
                    user.copy(status = localChanges.value.status)
                else user
                PagingClass(product)


            }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }

    class OnChange<T>(val value: T)
}




