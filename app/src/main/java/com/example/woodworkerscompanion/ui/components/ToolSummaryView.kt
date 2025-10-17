package com.example.woodworkerscompanion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun ToolSummaryView(
    summary: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .padding(horizontal = 20.dp)
    ) {
        val scrollState = rememberScrollState()
        
        Text(
            text = parseMarkdown(summary),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(vertical = 20.dp)
        )
    }
}

@Composable
private fun parseMarkdown(text: String): androidx.compose.ui.text.AnnotatedString {
    val annotatedString = buildAnnotatedString {
        // Simple markdown parser for **bold** text
        val regex = Regex("\\*\\*([^*]+)\\*\\*")
        var lastIndex = 0
        
        regex.findAll(text).forEach { matchResult ->
            // Add text before the match
            append(text.substring(lastIndex, matchResult.range.first))
            
            // Add bold text
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(matchResult.groupValues[1])
            }
            
            lastIndex = matchResult.range.last + 1
        }
        
        // Add remaining text
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }
    
    return annotatedString
}

