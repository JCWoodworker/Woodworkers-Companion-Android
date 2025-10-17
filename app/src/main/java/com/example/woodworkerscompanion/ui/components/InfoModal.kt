package com.example.woodworkerscompanion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.woodworkerscompanion.ui.theme.CreamBackground
import com.example.woodworkerscompanion.ui.theme.WoodSecondary

@Composable
fun InfoModal(
    toolName: String,
    summary: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.9f)
                    .shadow(20.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(CreamBackground)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { /* Consume click */ }
                    )
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WoodSecondary)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = toolName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                
                // Content
                val scrollState = rememberScrollState()
                Text(
                    text = parseMarkdown(summary),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                )
            }
        }
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

