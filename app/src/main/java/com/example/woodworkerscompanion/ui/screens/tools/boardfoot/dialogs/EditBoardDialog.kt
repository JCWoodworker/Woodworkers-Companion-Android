package com.example.woodworkerscompanion.ui.screens.tools.boardfoot.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.woodworkerscompanion.data.models.*
import com.example.woodworkerscompanion.ui.theme.CreamBackground
import com.example.woodworkerscompanion.ui.theme.ForestGreen
import com.example.woodworkerscompanion.ui.theme.WoodPrimary

@Composable
fun EditBoardDialog(
    board: BoardEntry,
    onSave: (BoardEntry) -> Unit,
    onDismiss: () -> Unit
) {
    var thickness by remember { mutableStateOf(board.thickness?.toString() ?: "") }
    var width by remember { mutableStateOf(board.width?.toString() ?: "") }
    var length by remember { mutableStateOf(board.length.toString()) }
    var quantity by remember { mutableStateOf(board.quantity.toString()) }
    var price by remember { mutableStateOf(board.price?.toString() ?: "") }
    var pricingType by remember { mutableStateOf(board.pricingType) }
    var lengthUnit by remember { mutableStateOf(board.lengthUnit ?: LengthUnit.FEET) }
    var woodSpecies by remember { mutableStateOf(board.woodSpecies ?: "") }
    
    val canSave = remember(thickness, width, length, quantity, pricingType) {
        if (pricingType == PricingType.LINEAR) {
            length.toDoubleOrNull() != null && 
            length.toDouble() > 0 &&
            quantity.toIntOrNull() != null &&
            quantity.toInt() > 0
        } else {
            thickness.toDoubleOrNull() != null &&
            thickness.toDouble() > 0 &&
            width.toDoubleOrNull() != null &&
            width.toDouble() > 0 &&
            length.toDoubleOrNull() != null &&
            length.toDouble() > 0 &&
            quantity.toIntOrNull() != null &&
            quantity.toInt() > 0
        }
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CreamBackground)
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = MaterialTheme.colorScheme.onBackground)
                    }
                    
                    Text(
                        text = "Edit Board",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    TextButton(
                        onClick = {
                            val l = length.toDoubleOrNull() ?: return@TextButton
                            val q = quantity.toIntOrNull() ?: return@TextButton
                            val priceValue = price.toDoubleOrNull()
                            val speciesValue = woodSpecies.trim().ifEmpty { null }
                            
                            val updatedBoard = if (pricingType == PricingType.LINEAR) {
                                board.copy(
                                    thickness = null,
                                    width = null,
                                    length = l,
                                    quantity = q,
                                    lengthUnit = if (board.unit == MeasurementUnit.IMPERIAL) lengthUnit else null,
                                    price = priceValue,
                                    pricingType = pricingType,
                                    woodSpecies = speciesValue
                                )
                            } else {
                                val t = thickness.toDoubleOrNull() ?: return@TextButton
                                val w = width.toDoubleOrNull() ?: return@TextButton
                                
                                board.copy(
                                    thickness = t,
                                    width = w,
                                    length = l,
                                    quantity = q,
                                    lengthUnit = if (board.unit == MeasurementUnit.IMPERIAL) lengthUnit else null,
                                    price = priceValue,
                                    pricingType = pricingType,
                                    woodSpecies = speciesValue
                                )
                            }
                            
                            onSave(updatedBoard)
                        },
                        enabled = canSave
                    ) {
                        Text(
                            text = "Save",
                            color = if (canSave) ForestGreen else Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                Divider()
                
                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Pricing Type Toggle
                    Text(
                        text = "Pricing Type",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, WoodPrimary, RoundedCornerShape(8.dp))
                            .height(40.dp)
                    ) {
                        PricingType.values().forEach { type ->
                            val isSelected = pricingType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .background(if (isSelected) WoodPrimary else Color.Transparent)
                                    .clickable { pricingType = type },
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
                    
                    // Thickness (only for Per Board Foot)
                    if (pricingType == PricingType.PER_BOARD_FOOT) {
                        OutlinedTextField(
                            value = thickness,
                            onValueChange = { thickness = it },
                            label = { 
                                Text(if (board.unit == MeasurementUnit.IMPERIAL) "Thickness (quarters)" else "Thickness (cm)")
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Width (only for Per Board Foot)
                    if (pricingType == PricingType.PER_BOARD_FOOT) {
                        OutlinedTextField(
                            value = width,
                            onValueChange = { width = it },
                            label = { 
                                Text(if (board.unit == MeasurementUnit.IMPERIAL) "Width (inches)" else "Width (cm)")
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    
                    // Length
                    Column {
                        OutlinedTextField(
                            value = length,
                            onValueChange = { length = it },
                            label = { 
                                Text(
                                    when {
                                        board.unit == MeasurementUnit.METRIC -> "Length (cm)"
                                        lengthUnit == LengthUnit.FEET -> "Length (feet)"
                                        else -> "Length (inches)"
                                    }
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        // Length unit toggle for Imperial
                        if (board.unit == MeasurementUnit.IMPERIAL) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, WoodPrimary, RoundedCornerShape(6.dp))
                                    .height(32.dp)
                            ) {
                                LengthUnit.values().forEach { unit ->
                                    val isSelected = lengthUnit == unit
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .background(if (isSelected) WoodPrimary else Color.Transparent)
                                            .clickable { lengthUnit = unit },
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
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Wood Species
                    OutlinedTextField(
                        value = woodSpecies,
                        onValueChange = { woodSpecies = it },
                        label = { Text("Wood Species (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Price
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

