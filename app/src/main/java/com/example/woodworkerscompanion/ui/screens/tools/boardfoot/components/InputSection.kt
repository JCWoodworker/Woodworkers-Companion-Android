package com.example.woodworkerscompanion.ui.screens.tools.boardfoot

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.woodworkerscompanion.data.models.LengthUnit
import com.example.woodworkerscompanion.data.models.MeasurementUnit
import com.example.woodworkerscompanion.data.models.PricingType
import com.example.woodworkerscompanion.ui.theme.WoodPrimary

@Composable
fun InputSection(
    viewModel: BoardFootViewModel,
    modifier: Modifier = Modifier
) {
    val selectedUnit by viewModel.selectedUnit.collectAsState()
    val lengthUnit by viewModel.lengthUnit.collectAsState()
    val pricingType by viewModel.pricingType.collectAsState()
    val thickness by viewModel.thickness.collectAsState()
    val width by viewModel.width.collectAsState()
    val length by viewModel.length.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val woodSpecies by viewModel.woodSpecies.collectAsState()
    val price by viewModel.price.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pricing Type Toggle
        PricingTypeToggle(
            selectedType = pricingType,
            onTypeSelected = { viewModel.setPricingType(it) }
        )
        
        // Thickness (only for Per Board Foot)
        if (pricingType == PricingType.PER_BOARD_FOOT) {
            TextField(
                value = thickness,
                onValueChange = { viewModel.setThickness(it) },
                label = { Text(if (selectedUnit == MeasurementUnit.IMPERIAL) "Thickness (quarters)" else "Thickness (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Width (only for Per Board Foot)
        if (pricingType == PricingType.PER_BOARD_FOOT) {
            TextField(
                value = width,
                onValueChange = { viewModel.setWidth(it) },
                label = { Text(if (selectedUnit == MeasurementUnit.IMPERIAL) "Width (inches)" else "Width (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Length
        Column {
            TextField(
                value = length,
                onValueChange = { viewModel.setLength(it) },
                label = { 
                    Text(
                        when {
                            selectedUnit == MeasurementUnit.METRIC -> "Length (cm)"
                            lengthUnit == LengthUnit.FEET -> "Length (feet)"
                            else -> "Length (inches)"
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Length unit toggle for Imperial
            if (selectedUnit == MeasurementUnit.IMPERIAL) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, WoodPrimary, RoundedCornerShape(6.dp))
                        .clip(RoundedCornerShape(6.dp))
                        .height(32.dp)
                ) {
                    LengthUnit.values().forEach { unit ->
                        val isSelected = lengthUnit == unit
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(if (isSelected) WoodPrimary else Color.Transparent)
                                .clickable { viewModel.setLengthUnit(unit) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = unit.displayName,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        }
        
        // Quantity
        TextField(
            value = quantity,
            onValueChange = { viewModel.setQuantity(it) },
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Wood Species
        TextField(
            value = woodSpecies,
            onValueChange = { viewModel.setWoodSpecies(it) },
            label = { Text("Wood Species (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Price
        TextField(
            value = price,
            onValueChange = { viewModel.setPrice(it) },
            label = { Text("Price per ${if (pricingType == PricingType.PER_BOARD_FOOT) "board foot" else "length unit"} (optional)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Add Board Button
        Button(
            onClick = { viewModel.addBoard() },
            enabled = viewModel.canAddBoard,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = WoodPrimary
            )
        ) {
            Text("Add Board", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PricingTypeToggle(
    selectedType: PricingType,
    onTypeSelected: (PricingType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, WoodPrimary, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .height(40.dp)
    ) {
        PricingType.values().forEach { type ->
            val isSelected = selectedType == type
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(if (isSelected) WoodPrimary else Color.Transparent)
                    .clickable { onTypeSelected(type) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.displayName,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

