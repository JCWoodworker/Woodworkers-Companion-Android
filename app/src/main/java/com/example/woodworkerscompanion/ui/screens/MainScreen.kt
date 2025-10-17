package com.example.woodworkerscompanion.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.woodworkerscompanion.data.models.Tool
import com.example.woodworkerscompanion.ui.theme.WoodPrimary
import com.example.woodworkerscompanion.ui.theme.WoodSecondary

@Composable
fun MainScreen(
    onToolClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tools = Tool.allTools
    val scrollState = rememberScrollState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Banner image (placeholder for now - user will provide)
            // Uncomment when image is available:
            // Image(
            //     painter = painterResource(id = R.drawable.wwc_banner),
            //     contentDescription = "Woodworker's Companion Banner",
            //     modifier = Modifier
            //         .fillMaxWidth(0.9f)
            //         .clip(RoundedCornerShape(12.dp))
            //         .shadow(8.dp, RoundedCornerShape(12.dp))
            //         .padding(horizontal = 20.dp)
            // )
            
            // Temporary text banner until image is provided
            Text(
                text = "Woodworker's Companion",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(20.dp)
            )
            
            Spacer(modifier = Modifier.height(30.dp))
            
            // Tool Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),  // Will be adaptive based on screen size
                contentPadding = PaddingValues(24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 600.dp)
            ) {
                items(tools) { tool ->
                    ToolTile(
                        tool = tool,
                        onClick = { onToolClick(tool.id) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ToolTile(
    tool: Tool,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 300f
        ),
        label = "tile_scale"
    )
    
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .shadow(
                elevation = if (isPressed) 2.dp else 6.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        WoodSecondary.copy(alpha = if (isPressed) 0.7f else 1.0f),
                        WoodPrimary.copy(alpha = if (isPressed) 0.8f else 1.0f)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Inner shadow effect when pressed
        if (isPressed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
        
        // Tool name text
        Text(
            text = tool.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .shadow(2.dp)
        )
    }
}

