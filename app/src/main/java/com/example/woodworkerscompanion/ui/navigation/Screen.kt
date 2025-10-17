package com.example.woodworkerscompanion.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object BoardFootCalculator : Screen("board_foot_calculator")
    object CutListOptimizer : Screen("cut_list_optimizer")
    object ProjectPricing : Screen("project_pricing")
    object FinishMixing : Screen("finish_mixing")
    object ProjectManagement : Screen("project_management")
    object QuotingInvoicing : Screen("quoting_invoicing")
    object InventoryManagement : Screen("inventory_management")
    object DigitalSketchpad : Screen("digital_sketchpad")
    object ReferenceLibraries : Screen("reference_libraries")
    object WoodshopCalculator : Screen("woodshop_calculator")
}

