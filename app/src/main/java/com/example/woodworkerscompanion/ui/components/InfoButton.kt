package com.example.woodworkerscompanion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InfoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Info",
        modifier = modifier
            .shadow(4.dp, CircleShape)
            .background(Color.White.copy(alpha = 0.9f), CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp)
            .size(25.dp),
        tint = MaterialTheme.colorScheme.onBackground
    )
}

