package com.example.woodworkerscompanion.ui.screens.tools.boardfoot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woodworkerscompanion.data.database.AppDatabase
import com.example.woodworkerscompanion.data.database.SavedOrderWithBoards
import com.example.woodworkerscompanion.data.repository.OrderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(database: AppDatabase) : ViewModel() {
    private val repository = OrderRepository(database.boardDao())
    
    val orders: StateFlow<List<SavedOrderWithBoards>> = repository.getAllOrders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun deleteOrder(orderId: String) {
        viewModelScope.launch {
            repository.deleteOrder(orderId)
        }
    }
    
    fun deleteAllOrders() {
        viewModelScope.launch {
            repository.deleteAllOrders()
        }
    }
    
    suspend fun loadOrderForEditing(orderId: String) = repository.loadOrderForEditing(orderId)
}

