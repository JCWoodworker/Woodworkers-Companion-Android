package com.example.woodworkerscompanion.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.woodworkerscompanion.data.models.Tool
import com.example.woodworkerscompanion.ui.screens.MainScreen
import com.example.woodworkerscompanion.ui.screens.tools.PlaceholderToolScreen
import com.example.woodworkerscompanion.ui.screens.tools.boardfoot.BoardFootCalculatorScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        // Main home screen
        composable(Screen.Main.route) {
            MainScreen(
                onToolClick = { toolId ->
                    val route = when (toolId) {
                        1 -> Screen.BoardFootCalculator.route
                        2 -> Screen.CutListOptimizer.route
                        3 -> Screen.ProjectPricing.route
                        4 -> Screen.FinishMixing.route
                        5 -> Screen.ProjectManagement.route
                        6 -> Screen.QuotingInvoicing.route
                        7 -> Screen.InventoryManagement.route
                        8 -> Screen.DigitalSketchpad.route
                        9 -> Screen.ReferenceLibraries.route
                        else -> Screen.Main.route
                    }
                    navController.navigate(route)
                }
            )
        }
        
        // Board Foot Calculator (fully functional)
        composable(Screen.BoardFootCalculator.route) {
            val tool = Tool.allTools.find { it.id == 1 }!!
            BoardFootCalculatorScreen(
                onNavigateBack = { navController.navigateUp() },
                toolSummary = tool.summary
            )
        }
        
        // Cut List Optimizer (placeholder)
        composable(Screen.CutListOptimizer.route) {
            val tool = Tool.allTools.find { it.id == 2 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        // Project Pricing Engine (placeholder)
        composable(Screen.ProjectPricing.route) {
            val tool = Tool.allTools.find { it.id == 3 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        // Finish Mixing Calculator (placeholder)
        composable(Screen.FinishMixing.route) {
            val tool = Tool.allTools.find { it.id == 4 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        // Project Management (placeholder)
        composable(Screen.ProjectManagement.route) {
            val tool = Tool.allTools.find { it.id == 5 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        // Quoting & Invoicing (placeholder)
        composable(Screen.QuotingInvoicing.route) {
            val tool = Tool.allTools.find { it.id == 6 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        // Inventory Management (placeholder)
        composable(Screen.InventoryManagement.route) {
            val tool = Tool.allTools.find { it.id == 7 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        // Digital Sketchpad (placeholder)
        composable(Screen.DigitalSketchpad.route) {
            val tool = Tool.allTools.find { it.id == 8 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        // Reference Libraries (placeholder)
        composable(Screen.ReferenceLibraries.route) {
            val tool = Tool.allTools.find { it.id == 9 }!!
            PlaceholderToolScreen(
                toolName = tool.name,
                summary = tool.summary,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}

