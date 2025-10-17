package com.example.woodworkerscompanion.ui.screens.tools.boardfoot

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.woodworkerscompanion.data.models.MeasurementUnit
import com.example.woodworkerscompanion.ui.theme.WoodPrimary

@Composable
fun UnitToggleView(
    selectedUnit: MeasurementUnit,
    onUnitSelected: (MeasurementUnit) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .border(2.dp, WoodPrimary, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .height(40.dp)
    ) {
        MeasurementUnit.values().forEach { unit ->
            val isSelected = selectedUnit == unit
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        if (isSelected) WoodPrimary else Color.Transparent
                    )
                    .clickable { onUnitSelected(unit) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = unit.displayName,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

