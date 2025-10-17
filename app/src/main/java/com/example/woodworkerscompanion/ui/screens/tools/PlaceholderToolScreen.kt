package com.example.woodworkerscompanion.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.woodworkerscompanion.ui.components.*

@Composable
fun PlaceholderToolScreen(
    toolName: String,
    summary: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showInfoModal by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            
            // Development banner
            DevelopmentBanner()
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Tool title
            Text(
                text = toolName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Summary view - this handles its own scrolling
            ToolSummaryView(
                summary = summary,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Takes remaining space
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
        
        // Home button in top left
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HomeButton(onClick = onNavigateBack)
        }
    }
    
    if (showInfoModal) {
        InfoModal(
            toolName = toolName,
            summary = summary,
            onDismiss = { showInfoModal = false }
        )
    }
}

