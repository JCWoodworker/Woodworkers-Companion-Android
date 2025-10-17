package com.example.woodworkerscompanion.data.models

data class Tool(
    val id: Int,
    val name: String,
    val inDevelopment: Boolean,
    val summary: String
) {
    companion object {
        // Add or remove tools from this list to grow/shrink the grid
        val allTools = listOf(
            Tool(
                id = 1,
                name = "Board Foot Calculator",
                inDevelopment = false,
                summary = ToolSummaries.boardFootCalculator
            ),
            Tool(
                id = 2,
                name = "Cut List Optimizer",
                inDevelopment = true,
                summary = ToolSummaries.cutListOptimizer
            ),
            Tool(
                id = 3,
                name = "Project Pricing Engine",
                inDevelopment = true,
                summary = ToolSummaries.projectPricingEngine
            ),
            Tool(
                id = 4,
                name = "Finish Mixing Calculator",
                inDevelopment = true,
                summary = ToolSummaries.finishMixingCalculator
            ),
            Tool(
                id = 5,
                name = "Project Management",
                inDevelopment = true,
                summary = ToolSummaries.projectManagement
            ),
            Tool(
                id = 6,
                name = "Quoting & Invoicing",
                inDevelopment = true,
                summary = ToolSummaries.quotingAndInvoicing
            ),
            Tool(
                id = 7,
                name = "Inventory Management",
                inDevelopment = true,
                summary = ToolSummaries.inventoryManagement
            ),
            Tool(
                id = 8,
                name = "Digital Sketchpad",
                inDevelopment = true,
                summary = ToolSummaries.digitalSketchpad
            ),
            Tool(
                id = 9,
                name = "Reference Libraries",
                inDevelopment = true,
                summary = ToolSummaries.referenceLibraries
            ),
            Tool(
                id = 10,
                name = "Woodshop Calculator",
                inDevelopment = true,
                summary = ToolSummaries.woodshopCalculator
            )
        )
    }
}

