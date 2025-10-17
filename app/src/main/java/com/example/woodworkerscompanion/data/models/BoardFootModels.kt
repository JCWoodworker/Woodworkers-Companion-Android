package com.example.woodworkerscompanion.data.models

import java.util.UUID

// MARK: - Measurement Unit
enum class MeasurementUnit(val displayName: String) {
    IMPERIAL("Imperial"),
    METRIC("Metric")
}

// MARK: - Length Unit (for Imperial)
enum class LengthUnit(val displayName: String, val symbol: String) {
    FEET("ft", "'"),
    INCHES("in", "\"")
}

// MARK: - Pricing Type
enum class PricingType(val displayName: String) {
    PER_BOARD_FOOT("Per Board Foot"),
    LINEAR("Linear")
}

// MARK: - Wood Species List
object WoodSpecies {
    val commonHardwoods = listOf(
        "Ash",
        "Black Limba",
        "Bloodwood",
        "Cherry",
        "Douglas Fir",
        "Hickory",
        "Maple",
        "Maple (Ambrosia)",
        "Maple (Birdseye)",
        "Maple (Curly)",
        "Oak (Red)",
        "Oak (White)",
        "Padauk",
        "Pine",
        "Poplar",
        "Purple Heart",
        "Tigerwood",
        "Walnut (Black)",
        "Walnut (Peruvian)",
        "Wenge",
        "Zebrawood"
    )
}

// MARK: - Board Entry
data class BoardEntry(
    val id: String = UUID.randomUUID().toString(),
    val thickness: Double? = null,
    val width: Double? = null,
    val length: Double,
    val quantity: Int,
    val unit: MeasurementUnit,
    val lengthUnit: LengthUnit? = null,  // Only used for Imperial
    val price: Double? = null,
    val pricingType: PricingType,
    val woodSpecies: String? = null
) {
    // Calculate board feet for this entry
    val boardFeet: Double
        get() {
            // Linear pricing doesn't use board feet
            if (pricingType == PricingType.LINEAR) {
                return 0.0
            }

            val t = thickness ?: return 0.0
            val w = width ?: return 0.0

            return when (unit) {
                MeasurementUnit.IMPERIAL -> {
                    // For Imperial, thickness is in quarters, so convert to inches first
                    // Board Feet = (Thickness in inches × Width in inches × Length in feet) / 12 × Quantity
                    val thicknessInInches = t / 4.0

                    // Convert length to feet if it's in inches
                    val lengthInFeet: Double = if (lengthUnit == LengthUnit.INCHES) {
                        length / 12.0
                    } else {
                        length
                    }

                    (thicknessInInches * w * lengthInFeet / 12.0) * quantity
                }
                MeasurementUnit.METRIC -> {
                    // Convert to board feet: 1 board foot = 2359.737 cm³
                    // Thickness (cm) × Width (cm) × Length (cm) / 2359.737 × Quantity
                    (t * w * length / 2359.737) * quantity
                }
            }
        }

    // Calculate cost for this entry
    val cost: Double
        get() {
            val p = price ?: return 0.0

            return when (pricingType) {
                PricingType.PER_BOARD_FOOT -> boardFeet * p
                PricingType.LINEAR -> {
                    // For linear, just multiply length by price and quantity
                    length * p * quantity
                }
            }
        }

    // Formatted display
    val displayString: String
        get() {
            val quantityStr = if (quantity > 1) "$quantity × " else ""
            val speciesStr = woodSpecies?.let { " - $it" } ?: ""

            if (pricingType == PricingType.LINEAR) {
                // Linear only shows length
                return when (unit) {
                    MeasurementUnit.IMPERIAL -> {
                        val lengthSymbol = if (lengthUnit == LengthUnit.INCHES) "\"" else "'"
                        "$quantityStr$length$lengthSymbol$speciesStr"
                    }
                    MeasurementUnit.METRIC -> {
                        "${quantityStr}${length}cm$speciesStr"
                    }
                }
            }

            val t = thickness ?: return ""
            val w = width ?: return ""

            return when (unit) {
                MeasurementUnit.IMPERIAL -> {
                    // Show thickness in quarters format
                    val thicknessInt = t.toInt()
                    val lengthSymbol = if (lengthUnit == LengthUnit.INCHES) "\"" else "'"
                    "$quantityStr$thicknessInt/4\" × $w\" × $length$lengthSymbol$speciesStr"
                }
                MeasurementUnit.METRIC -> {
                    "${quantityStr}${t}cm × ${w}cm × ${length}cm$speciesStr"
                }
            }
        }

    // Formatted display without species (for export text)
    val displayStringWithoutSpecies: String
        get() {
            val quantityStr = if (quantity > 1) "$quantity × " else ""

            if (pricingType == PricingType.LINEAR) {
                // Linear only shows length
                return when (unit) {
                    MeasurementUnit.IMPERIAL -> {
                        val lengthSymbol = if (lengthUnit == LengthUnit.INCHES) "\"" else "'"
                        "$quantityStr$length$lengthSymbol"
                    }
                    MeasurementUnit.METRIC -> {
                        "${quantityStr}${length}cm"
                    }
                }
            }

            val t = thickness ?: return ""
            val w = width ?: return ""

            return when (unit) {
                MeasurementUnit.IMPERIAL -> {
                    // Show thickness in quarters format
                    val thicknessInt = t.toInt()
                    val lengthSymbol = if (lengthUnit == LengthUnit.INCHES) "\"" else "'"
                    "$quantityStr$thicknessInt/4\" × $w\" × $length$lengthSymbol"
                }
                MeasurementUnit.METRIC -> {
                    "${quantityStr}${t}cm × ${w}cm × ${length}cm"
                }
            }
        }
}

// MARK: - Common Lumber Sizes
data class LumberPreset(
    val name: String,
    val thickness: Double,
    val width: Double
) {
    companion object {
        val imperialPresets = listOf(
            LumberPreset("1×2", 0.75, 1.5),
            LumberPreset("1×3", 0.75, 2.5),
            LumberPreset("1×4", 0.75, 3.5),
            LumberPreset("1×6", 0.75, 5.5),
            LumberPreset("1×8", 0.75, 7.25),
            LumberPreset("1×10", 0.75, 9.25),
            LumberPreset("1×12", 0.75, 11.25),
            LumberPreset("2×4", 1.5, 3.5),
            LumberPreset("2×6", 1.5, 5.5),
            LumberPreset("2×8", 1.5, 7.25),
            LumberPreset("2×10", 1.5, 9.25),
            LumberPreset("2×12", 1.5, 11.25),
            LumberPreset("4×4", 3.5, 3.5),
            LumberPreset("6×6", 5.5, 5.5)
        )

        val metricPresets = listOf(
            LumberPreset("2×5", 2.0, 5.0),
            LumberPreset("2×10", 2.0, 10.0),
            LumberPreset("2×15", 2.0, 15.0),
            LumberPreset("3×10", 3.0, 10.0),
            LumberPreset("3×15", 3.0, 15.0),
            LumberPreset("4×10", 4.0, 10.0),
            LumberPreset("4×15", 4.0, 15.0),
            LumberPreset("5×10", 5.0, 10.0),
            LumberPreset("5×15", 5.0, 15.0),
            LumberPreset("5×20", 5.0, 20.0)
        )
    }
}

