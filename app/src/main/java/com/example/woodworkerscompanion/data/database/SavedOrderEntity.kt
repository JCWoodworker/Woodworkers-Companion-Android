package com.example.woodworkerscompanion.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.woodworkerscompanion.data.models.BoardEntry

@Entity(tableName = "saved_orders")
@TypeConverters(Converters::class)
data class SavedOrderEntity(
    @PrimaryKey val id: String,
    val orderName: String?,
    val timestamp: Long,
    val boardIds: List<String>
)

data class SavedOrderWithBoards(
    val order: SavedOrderEntity,
    val boards: List<BoardEntry>
) {
    val totalBoardFeet: Double
        get() = boards.sumOf { it.boardFeet }
    
    val totalCost: Double
        get() = boards.sumOf { it.cost }
    
    val primaryWoodSpecies: String?
        get() = boards
            .mapNotNull { it.woodSpecies }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
}

