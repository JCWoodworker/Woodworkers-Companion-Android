package com.example.woodworkerscompanion.ui.screens.tools.boardfoot.dialogs

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.woodworkerscompanion.data.database.SavedOrderWithBoards
import com.example.woodworkerscompanion.ui.theme.CreamBackground
import com.example.woodworkerscompanion.ui.theme.ForestGreen
import com.example.woodworkerscompanion.ui.theme.WoodPrimary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderDetailDialog(
    order: SavedOrderWithBoards,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    
    val exportText = remember(order) {
        generateExportText(order)
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
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = order.order.orderName ?: "Order",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    
                    TextButton(onClick = onDismiss) {
                        Text("Done", color = WoodPrimary, fontWeight = FontWeight.Medium)
                    }
                }
                
                Divider()
                
                // Scrollable order content
                Text(
                    text = exportText,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                )
                
                // Action buttons
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, exportText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Order")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Share / Print",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    
                    Button(
                        onClick = { showDeleteConfirmation = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.7f)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Delete Order",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
    
    // Delete confirmation
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Order") },
            text = { Text("Are you sure you want to delete \"${order.order.orderName}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Delete", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun generateExportText(order: SavedOrderWithBoards): String {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    val date = dateFormat.format(Date(order.order.timestamp))
    
    var output = "${order.order.orderName ?: "Order"}\n"
    output += "Saved: $date\n"
    output += "${"-".repeat(50)}\n\n"
    
    order.boards.forEachIndexed { index, board ->
        output += "Board ${index + 1}:\n"
        
        board.woodSpecies?.let { species ->
            output += "  Species: $species\n"
        }
        
        output += "  Dimensions: ${board.displayStringWithoutSpecies}\n"
        
        if (board.pricingType == com.example.woodworkerscompanion.data.models.PricingType.PER_BOARD_FOOT) {
            output += "  Board Feet: ${"%.2f".format(board.boardFeet)} bf\n"
        }
        
        board.price?.let { price ->
            output += "  Pricing: ${board.pricingType.displayName} @ $${"%.2f".format(price)}\n"
            output += "  Cost: $${"%.2f".format(board.cost)}\n"
        }
        output += "\n"
    }
    
    output += "${"-".repeat(50)}\n"
    
    // Break down by species
    if (order.totalBoardFeet > 0) {
        val speciesBreakdown = mutableMapOf<String, Pair<Double, Double>>()
        
        order.boards.forEach { board ->
            if (board.pricingType == com.example.woodworkerscompanion.data.models.PricingType.PER_BOARD_FOOT && board.boardFeet > 0) {
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
    
    if (order.totalCost > 0) {
        output += "TOTAL COST: $${"%.2f".format(order.totalCost)}\n"
    }
    
    return output
}

