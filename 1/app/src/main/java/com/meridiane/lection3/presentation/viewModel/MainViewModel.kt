package com.meridiane.lection3.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meridiane.lection3.data.Product
import com.meridiane.lection3.domain.repository.MockRepository
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {

    val liveDataError = MutableLiveData<String>()
    val liveData = MutableLiveData<List<Product>>()

    private val repository = MockRepository()

    fun start() {
        viewModelScope.launch {
            try {
                fun getProducts() = viewModelScope.async {
                    repository.getProducts()
                }

                val listProducts = getProducts().await().apply {
                    if (this.isFailure) {
                        liveDataError.value = "getProducts()"
                        throw Exception("getProducts()")
                    }
                }.getOrNull()

                liveData.value = listProducts!!
            } catch (e: Exception) {
                liveDataError.value = e.message
            }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }
}


