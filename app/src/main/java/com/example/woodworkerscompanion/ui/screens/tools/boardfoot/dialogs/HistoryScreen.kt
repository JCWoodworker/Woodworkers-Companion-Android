package com.example.woodworkerscompanion.ui.screens.tools.boardfoot.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.woodworkerscompanion.data.database.SavedOrderWithBoards
import com.example.woodworkerscompanion.ui.screens.tools.boardfoot.HistoryViewModel
import com.example.woodworkerscompanion.ui.screens.tools.boardfoot.HistoryViewModelFactory
import com.example.woodworkerscompanion.ui.theme.DarkBrown
import com.example.woodworkerscompanion.ui.theme.ForestGreen
import com.example.woodworkerscompanion.ui.theme.WoodPrimary
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    onDismiss: () -> Unit,
    onLoadOrder: (String) -> Unit,
    viewModel: HistoryViewModel
) {
    val orders by viewModel.orders.collectAsState()
    var selectedOrder by remember { mutableStateOf<SavedOrderWithBoards?>(null) }
    var showDeleteAllConfirmation by remember { mutableStateOf(false) }
    var orderToDelete by remember { mutableStateOf<SavedOrderWithBoards?>(null) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            
            // Title
            Text(
                text = "Order History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            if (orders.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "No saved orders yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            } else {
                // Delete All button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { showDeleteAllConfirmation = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.7f)
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Delete All", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                
                // Orders list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        OrderCard(
                            order = order,
                            onClick = { selectedOrder = order },
                            onDelete = { orderToDelete = order },
                            onEdit = {
                                onLoadOrder(order.order.id)
                                onDismiss()
                            },
                            onShare = { selectedOrder = order }
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
        
        // Back button
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WoodPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back to Calculator", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
    
    // Order detail dialog
    selectedOrder?.let { order ->
        OrderDetailDialog(
            order = order,
            onDismiss = { selectedOrder = null },
            onDelete = {
                viewModel.deleteOrder(order.order.id)
                selectedOrder = null
            }
        )
    }
    
    // Delete confirmation dialog
    orderToDelete?.let { order ->
        AlertDialog(
            onDismissRequest = { orderToDelete = null },
            title = { Text("Delete Order") },
            text = { Text("Are you sure you want to delete \"${order.order.orderName ?: "this order"}\"? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteOrder(order.order.id)
                        orderToDelete = null
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { orderToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
    
    // Delete all confirmation
    if (showDeleteAllConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteAllConfirmation = false },
            title = { Text("Delete All Orders") },
            text = { Text("Are you sure you want to delete all ${orders.size} orders? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAllOrders()
                        showDeleteAllConfirmation = false
                    }
                ) {
                    Text("Delete All", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun OrderCard(
    order: SavedOrderWithBoards,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onShare: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val date = remember(order) { Date(order.order.timestamp) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Order name
            Text(
                text = order.order.orderName ?: "Unnamed Order",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkBrown
            )
            
            // Date and time
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = DarkBrown.copy(alpha = 0.6f)
                )
                Text(
                    text = dateFormat.format(date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkBrown.copy(alpha = 0.7f)
                )
                
                Text("•", color = DarkBrown.copy(alpha = 0.4f))
                
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = DarkBrown.copy(alpha = 0.6f)
                )
                Text(
                    text = timeFormat.format(date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkBrown.copy(alpha = 0.7f)
                )
            }
            
            // Primary wood species
            order.primaryWoodSpecies?.let { species ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = DarkBrown.copy(alpha = 0.6f)
                    )
                    Text(
                        text = species,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkBrown.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Divider()
            
            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = DarkBrown
                )
                
                Text(
                    text = "${"%.2f".format(order.totalCost)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
            }
            
            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = WoodPrimary
                    )
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Edit", fontSize = 12.sp)
                }
                
                OutlinedButton(
                    onClick = onShare,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ForestGreen
                    )
                ) {
                    Icon(Icons.Default.Share, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Share", fontSize = 12.sp)
                }
                
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Delete", fontSize = 12.sp)
                }
            }
        }
    }
}

