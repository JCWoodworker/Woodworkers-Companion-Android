package com.example.woodworkerscompanion.ui.screens.tools.boardfoot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.woodworkerscompanion.data.database.AppDatabase
import com.example.woodworkerscompanion.ui.components.HomeButton
import com.example.woodworkerscompanion.ui.components.InfoButton
import com.example.woodworkerscompanion.ui.components.InfoModal

@Composable
fun BoardFootCalculatorScreen(
    onNavigateBack: () -> Unit,
    toolSummary: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val viewModel: BoardFootViewModel = viewModel(
        factory = BoardFootViewModelFactory(database)
    )
    
    var showInfoModal by remember { mutableStateOf(false) }
    var showSaveOrderDialog by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    
    val selectedUnit by viewModel.selectedUnit.collectAsState()
    val boards by viewModel.boards.collectAsState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Title
            Text(
                text = "Board Foot Calculator",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Unit Toggle
            UnitToggleView(
                selectedUnit = selectedUnit,
                onUnitSelected = { viewModel.setSelectedUnit(it) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Input Section
            InputSection(viewModel = viewModel)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Save Order Button (only shows when boards exist)
            if (boards.isNotEmpty()) {
                SaveOrderButton(
                    onClick = { showSaveOrderDialog = true }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Board List
            if (boards.isNotEmpty()) {
                BoardListView(
                    boards = boards,
                    onDeleteBoard = { viewModel.removeBoard(it) },
                    onEditBoard = { /* TODO: Show edit dialog */ }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Summary Section
            if (boards.isNotEmpty()) {
                SummarySection(
                    totalBoardFeet = viewModel.totalBoardFeet,
                    totalCost = viewModel.totalCost
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Action Buttons
                ActionButtons(
                    onExport = { showExportDialog = true },
                    onClearAll = { viewModel.clearAll() }
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
        
        // Top buttons
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeButton(onClick = onNavigateBack)
            InfoButton(onClick = { showInfoModal = true })
        }
        
        // History button (top right)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
        ) {
            HistoryButton(onClick = { showHistory = true })
        }
    }
    
    // Modals
    if (showInfoModal) {
        InfoModal(
            toolName = "Board Foot Calculator",
            summary = toolSummary,
            onDismiss = { showInfoModal = false }
        )
    }
    
    // TODO: Add dialogs for save order, history, export
}

