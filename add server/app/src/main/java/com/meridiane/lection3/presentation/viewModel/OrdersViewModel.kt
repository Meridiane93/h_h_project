package com.meridiane.lection3.presentation.viewModel

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.meridiane.lection3.MutableLiveEvent
import com.meridiane.lection3.domain.entity.AllOrder
import com.meridiane.lection3.domain.entity.CancelOrder
import com.meridiane.lection3.domain.useCaseProfile.GetActiveOrderInterface
import com.meridiane.lection3.publishEvent
import com.meridiane.lection3.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val getActiveOrderInterface: GetActiveOrderInterface) :
    ViewModel() {

//    var allOrderPager : Flow<PagingData<AllOrder>> = TODO()
//    var cancelOrder : Flow<PagingData<AllOrder>> = TODO()
//    var testOrder : Flow<PagingData<AllOrder>> = TODO()

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
                  //  _ordersStateCancel.value = answer

            } catch (e: Exception) {
                Log.d("MyTag", "Except $e")
                _ordersStateCancelExeption.value = e.message.toString()
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }


//
//
//
//    val usersFlow: Flow<PagingData<AllOrder>>
//
//    private val searchBy = MutableLiveData("")
//
//    private val localChanges = LocalChanges()
//    private val localChangesFlow = MutableStateFlow(OnChange(localChanges))
//
//    private val _errorEvents = MutableLiveEvent<Int>()
//    val errorEvents = _errorEvents.share()
//
//    private val _scrollEvents = MutableLiveEvent<Unit>()
//    val scrollEvents = _scrollEvents.share()
//
//    private var _invalidateEvents = MutableLiveEvent<Unit>()
//    val invalidateEvents = _invalidateEvents.share()
//
////    init {
////        val originUsersFlow = searchBy.asFlow()
////            // if user types text too quickly -> filtering intermediate values to avoid excess loads
////            .debounce(500)
////            .flatMapLatest { _ ->
////                getActiveOrderInterface.getListOrder()
////            }
////            .cachedIn(viewModelScope)
////
////        usersFlow = combine(
////            originUsersFlow,
////            localChangesFlow.debounce(50),
////            this::merge
////        )
////    }
//
////    override fun onUserDelete(userListItem: AllOrder) {
////        if (isInProgress(userListItem)) return
////        viewModelScope.launch {
////            try {
////                setProgress(userListItem, true)
////                delete(userListItem)
////            } catch (e: Exception) {
////                _ordersStateCancelExeption.value = e.message!!
////            } finally {
////                setProgress(userListItem, false)
////            }
////        }
////    }
//
//    override fun onToggleFavoriteFlag(userListItem: AllOrder) {
//        if (isInProgress(userListItem)) return
//        viewModelScope.launch {
//            try {
//                setProgress(userListItem, true)
//                setFavoriteFlag(userListItem)
//            } catch (e: Exception) {
//                _ordersStateCancelExeption.value = e.message!!
//            } finally {
//                setProgress(userListItem, false)
//            }
//        }
//    }
//
//
//    private fun setProgress(userListItem: AllOrder, inProgress: Boolean) {
//        if (inProgress) {
//            localChanges.idsInProgress.add(userListItem.id)
//        } else {
//            localChanges.idsInProgress.remove(userListItem.id)
//        }
//        localChangesFlow.value = OnChange(localChanges)
//    }
//
//    private fun isInProgress(userListItem: AllOrder) =
//        localChanges.idsInProgress.contains(userListItem.id)
//
////    private suspend fun setFavoriteFlag(userListItem: AllOrder) {
////        val newFlagValue = userListItem.status
////        usersRepository.setIsFavorite(userListItem.productId, newFlagValue)
////        localChanges.favoriteFlags[userListItem.productId] = newFlagValue
////        localChangesFlow.value = OnChange(localChanges)
////    }
//
//    private fun scrollListToTop() {
//        _scrollEvents.publishEvent(Unit)
//    }
//
//    private fun invalidateList() {
//        _invalidateEvents.publishEvent(Unit)
//    }
//
//    private fun merge(users: PagingData<CancelOrder>, localChanges: OnChange<LocalChanges>): PagingData<AllOrder> {
//        return users
//            .map { user ->
//                val isInProgress = localChanges.value.idsInProgress.contains(user.productId)
//                val localFavoriteFlag = localChanges.value.favoriteFlags[user.productId]
//
//                val userWithLocalChanges = if (localFavoriteFlag == null) {
//                    user
//                } else {
//                    user.copy(isFavorite = localFavoriteFlag)
//                }
//                AllOrder(userWithLocalChanges, isInProgress)
//            }
//    }
//
//    /**
//     * Non-data class which allows passing the same reference to the
//     * MutableStateFlow multiple times in a row.
//     */
//    class OnChange<T>(val value: T)
//
//    /**
//     * Contains:
//     * 1) identifiers of items which are processed now (deleting or favorite
//     * flag updating).
//     * 2) local favorite flag updates to avoid list reloading
//     */
//    class LocalChanges {
//        val idsInProgress = mutableSetOf<String?>()
//        val favoriteFlags = mutableMapOf<String?, String?>()
//    }
}


