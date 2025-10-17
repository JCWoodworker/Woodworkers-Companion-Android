package com.example.woodworkerscompanion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.woodworkerscompanion.ui.theme.OrangeDevelopment

@Composable
fun DevelopmentBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(
                color = OrangeDevelopment.copy(alpha = 0.85f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Build,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "In Development",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

