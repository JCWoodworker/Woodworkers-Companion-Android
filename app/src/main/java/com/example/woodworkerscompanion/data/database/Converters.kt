package com.example.woodworkerscompanion.data.database

import androidx.room.TypeConverter
import com.example.woodworkerscompanion.data.models.LengthUnit
import com.example.woodworkerscompanion.data.models.MeasurementUnit
import com.example.woodworkerscompanion.data.models.PricingType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMeasurementUnit(value: MeasurementUnit): String {
        return value.name
    }

    @TypeConverter
    fun toMeasurementUnit(value: String): MeasurementUnit {
        return MeasurementUnit.valueOf(value)
    }

    @TypeConverter
    fun fromLengthUnit(value: LengthUnit?): String? {
        return value?.name
    }

    @TypeConverter
    fun toLengthUnit(value: String?): LengthUnit? {
        return value?.let { LengthUnit.valueOf(it) }
    }

    @TypeConverter
    fun fromPricingType(value: PricingType): String {
        return value.name
    }

    @TypeConverter
    fun toPricingType(value: String): PricingType {
        return PricingType.valueOf(value)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}

