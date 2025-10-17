package com.example.woodworkerscompanion.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.woodworkerscompanion.data.models.BoardEntry
import com.example.woodworkerscompanion.data.models.LengthUnit
import com.example.woodworkerscompanion.data.models.MeasurementUnit
import com.example.woodworkerscompanion.data.models.PricingType

@Entity(tableName = "board_entries")
data class BoardEntryEntity(
    @PrimaryKey val id: String,
    val thickness: Double?,
    val width: Double?,
    val length: Double,
    val quantity: Int,
    val unit: MeasurementUnit,
    val lengthUnit: LengthUnit?,
    val price: Double?,
    val pricingType: PricingType,
    val woodSpecies: String?
) {
    fun toBoardEntry(): BoardEntry {
        return BoardEntry(
            id = id,
            thickness = thickness,
            width = width,
            length = length,
            quantity = quantity,
            unit = unit,
            lengthUnit = lengthUnit,
            price = price,
            pricingType = pricingType,
            woodSpecies = woodSpecies
        )
    }

    companion object {
        fun fromBoardEntry(board: BoardEntry): BoardEntryEntity {
            return BoardEntryEntity(
                id = board.id,
                thickness = board.thickness,
                width = board.width,
                length = board.length,
                quantity = board.quantity,
                unit = board.unit,
                lengthUnit = board.lengthUnit,
                price = board.price,
                pricingType = board.pricingType,
                woodSpecies = board.woodSpecies
            )
        }
    }
}

