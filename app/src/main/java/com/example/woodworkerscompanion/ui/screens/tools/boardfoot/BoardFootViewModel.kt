package com.example.woodworkerscompanion.ui.screens.tools.boardfoot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.woodworkerscompanion.data.database.AppDatabase
import com.example.woodworkerscompanion.data.models.*
import com.example.woodworkerscompanion.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BoardFootViewModel(database: AppDatabase) : ViewModel() {
    private val repository = OrderRepository(database.boardDao())
    
    // Unit selection
    private val _selectedUnit = MutableStateFlow(MeasurementUnit.IMPERIAL)
    val selectedUnit: StateFlow<MeasurementUnit> = _selectedUnit.asStateFlow()
    
    private val _lengthUnit = MutableStateFlow(LengthUnit.FEET)
    val lengthUnit: StateFlow<LengthUnit> = _lengthUnit.asStateFlow()
    
    // Input fields
    private val _thickness = MutableStateFlow("")
    val thickness: StateFlow<String> = _thickness.asStateFlow()
    
    private val _width = MutableStateFlow("")
    val width: StateFlow<String> = _width.asStateFlow()
    
    private val _length = MutableStateFlow("")
    val length: StateFlow<String> = _length.asStateFlow()
    
    private val _quantity = MutableStateFlow("1")
    val quantity: StateFlow<String> = _quantity.asStateFlow()
    
    private val _woodSpecies = MutableStateFlow("")
    val woodSpecies: StateFlow<String> = _woodSpecies.asStateFlow()
    
    // Pricing
    private val _pricingType = MutableStateFlow(PricingType.PER_BOARD_FOOT)
    val pricingType: StateFlow<PricingType> = _pricingType.asStateFlow()
    
    private val _price = MutableStateFlow("")
    val price: StateFlow<String> = _price.asStateFlow()
    
    // Board list
    private val _boards = MutableStateFlow<List<BoardEntry>>(emptyList())
    val boards: StateFlow<List<BoardEntry>> = _boards.asStateFlow()
    
    // Computed properties
    val totalBoardFeet: Double
        get() = _boards.value.sumOf { it.boardFeet }
    
    val totalCost: Double
        get() = _boards.value.sumOf { it.cost }
    
    val canAddBoard: Boolean
        get() {
            // For linear pricing, only need length
            if (_pricingType.value == PricingType.LINEAR) {
                val l = _length.value.toDoubleOrNull()
                val q = _quantity.value.toIntOrNull()
                return l != null && l > 0 && q != null && q > 0
            }
            
            // For board foot pricing, need all dimensions
            val t = _thickness.value.toDoubleOrNull()
            val w = _width.value.toDoubleOrNull()
            val l = _length.value.toDoubleOrNull()
            val q = _quantity.value.toIntOrNull()
            
            return t != null && t > 0 &&
                   w != null && w > 0 &&
                   l != null && l > 0 &&
                   q != null && q > 0
        }
    
    init {
        // Load work in progress
        viewModelScope.launch {
            val wip = repository.loadWorkInProgress()
            if (wip != null) {
                _boards.value = wip
            }
        }
    }
    
    // Actions
    fun setSelectedUnit(unit: MeasurementUnit) {
        _selectedUnit.value = unit
    }
    
    fun setLengthUnit(unit: LengthUnit) {
        _lengthUnit.value = unit
    }
    
    fun setThickness(value: String) {
        _thickness.value = value
    }
    
    fun setWidth(value: String) {
        _width.value = value
    }
    
    fun setLength(value: String) {
        _length.value = value
    }
    
    fun setQuantity(value: String) {
        _quantity.value = value
    }
    
    fun setWoodSpecies(value: String) {
        _woodSpecies.value = value
    }
    
    fun setPricingType(type: PricingType) {
        _pricingType.value = type
    }
    
    fun setPrice(value: String) {
        _price.value = value
    }
    
    fun addBoard() {
        val l = _length.value.toDoubleOrNull() ?: return
        val q = _quantity.value.toIntOrNull() ?: return
        val priceValue = _price.value.toDoubleOrNull()
        val speciesValue = _woodSpecies.value.trim().ifEmpty { null }
        
        val board = if (_pricingType.value == PricingType.LINEAR) {
            BoardEntry(
                thickness = null,
                width = null,
                length = l,
                quantity = q,
                unit = _selectedUnit.value,
                lengthUnit = if (_selectedUnit.value == MeasurementUnit.IMPERIAL) _lengthUnit.value else null,
                price = priceValue,
                pricingType = _pricingType.value,
                woodSpecies = speciesValue
            )
        } else {
            val t = _thickness.value.toDoubleOrNull() ?: return
            val w = _width.value.toDoubleOrNull() ?: return
            
            BoardEntry(
                thickness = t,
                width = w,
                length = l,
                quantity = q,
                unit = _selectedUnit.value,
                lengthUnit = if (_selectedUnit.value == MeasurementUnit.IMPERIAL) _lengthUnit.value else null,
                price = priceValue,
                pricingType = _pricingType.value,
                woodSpecies = speciesValue
            )
        }
        
        _boards.value = _boards.value + board
        saveWorkInProgress()
        
        // Clear input fields except price and species (they persist)
        _thickness.value = ""
        _width.value = ""
        _length.value = ""
        _quantity.value = "1"
    }
    
    fun removeBoard(board: BoardEntry) {
        _boards.value = _boards.value.filter { it.id != board.id }
        saveWorkInProgress()
    }
    
    fun updateBoard(updatedBoard: BoardEntry) {
        _boards.value = _boards.value.map {
            if (it.id == updatedBoard.id) updatedBoard else it
        }
        saveWorkInProgress()
    }
    
    fun clearAll() {
        _boards.value = emptyList()
        _thickness.value = ""
        _width.value = ""
        _length.value = ""
        _quantity.value = "1"
        _price.value = ""
        _woodSpecies.value = ""
        clearWorkInProgress()
    }
    
    fun saveOrder(orderName: String?) {
        viewModelScope.launch {
            repository.saveOrder(orderName, _boards.value)
            clearAll()
        }
    }
    
    private fun saveWorkInProgress() {
        viewModelScope.launch {
            repository.saveWorkInProgress(_boards.value)
        }
    }
    
    private fun clearWorkInProgress() {
        viewModelScope.launch {
            repository.clearWorkInProgress()
        }
    }
    
    fun exportData(): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val date = dateFormat.format(Date())
        
        var output = "Board Foot Calculator - $date\n"
        output += "Unit: ${_selectedUnit.value.displayName}\n"
        output += "${"-".repeat(50)}\n\n"
        
        _boards.value.forEachIndexed { index, board ->
            output += "Board ${index + 1}:\n"
            
            board.woodSpecies?.let { species ->
                output += "  Species: $species\n"
            }
            
            output += "  Dimensions: ${board.displayStringWithoutSpecies}\n"
            
            if (board.pricingType == PricingType.PER_BOARD_FOOT) {
                output += "  Board Feet: ${"%.2f".format(board.boardFeet)} bf\n"
            }
            
            board.price?.let { price ->
                output += "  Pricing: ${board.pricingType.displayName} @ $${"%.2f".format(price)}\n"
                output += "  Cost: $${"%.2f".format(board.cost)}\n"
            }
            output += "\n"
        }
        
        output += "${"-".repeat(50)}\n"
        
        // Break down board feet by species
        if (totalBoardFeet > 0) {
            val speciesBreakdown = mutableMapOf<String, Pair<Double, Double>>()
            
            _boards.value.forEach { board ->
                if (board.pricingType == PricingType.PER_BOARD_FOOT && board.boardFeet > 0) {
                    val species = board.woodSpecies ?: "Misc"
                    val current = speciesBreakdown[species] ?: (0.0 to 0.0)
                    speciesBreakdown[species] = (current.first + board.boardFeet) to (current.second + board.cost)
                }
            }
            
            if (speciesBreakdown.isNotEmpty()) {
                output += "BOARD FEET BY SPECIES:\n"
                speciesBreakdown.entries.sortedBy { it.key }.forEach { (species, data) ->
                    output += "  $species: ${"%.2f".format(data.first)} bf - $${"%.2f".format(data.second)}\n"
                }
                output += "\n"
            }
        }
        
        if (totalCost > 0) {
            output += "TOTAL COST: $${"%.2f".format(totalCost)}\n"
        }
        
        return output
    }
}

