package com.example.woodworkerscompanion.ui.screens.tools.boardfoot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.woodworkerscompanion.data.database.AppDatabase

class BoardFootViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardFootViewModel::class.java)) {
            return BoardFootViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

