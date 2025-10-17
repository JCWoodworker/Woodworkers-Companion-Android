package com.example.woodworkerscompanion.ui.screens.tools.boardfoot

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.woodworkerscompanion.ui.theme.WoodPrimary

@Composable
fun ActionButtons(
    onExport: () -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onExport,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = WoodPrimary
            )
        ) {
            Text("Export", fontWeight = FontWeight.SemiBold)
        }
        
        OutlinedButton(
            onClick = onClearAll,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Red
            )
        ) {
            Text("Clear All", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun SaveOrderButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = WoodPrimary
        )
    ) {
        Text("Save Order", fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun HistoryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = WoodPrimary
        )
    ) {
        Text("History", fontWeight = FontWeight.SemiBold)
    }
}

