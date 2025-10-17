//
//  BoardFootCalculatorView.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import SwiftUI

struct BoardFootCalculatorView: View {
  @Environment(\.dismiss) private var dismiss
  @State private var viewModel = BoardFootViewModel()
  @State private var showingExportSheet = false
  @State private var showingSaveOrderSheet = false
  @State private var showingHistory = false
  @State private var selectedBoardToEdit: BoardEntry? = nil
  @State private var showingSummary = false
  let summary: String

  var body: some View {
    ZStack(alignment: .topLeading) {
      Color.creamBackground.ignoresSafeArea()

      ScrollView {
        VStack(spacing: 24) {
          // Title
          Text("Board Foot Calculator")
            .font(.title)
            .fontWeight(.bold)
            .foregroundColor(.darkBrown)
            .padding(.top, 60)

          // Unit Toggle
          UnitToggleView(selectedUnit: $viewModel.selectedUnit)

          // Input Section (includes pricing)
          InputSectionView(viewModel: viewModel)

          // Save Order Button (only shows when boards exist)
          if !viewModel.boards.isEmpty {
            Button(action: {
              showingSaveOrderSheet = true
            }) {
              HStack {
                Image(systemName: "square.and.arrow.down")
                Text("Save Order")
                  .fontWeight(.semibold)
              }
              .foregroundColor(.white)
              .frame(maxWidth: .infinity)
              .padding(.vertical, 12)
              .background(Color.woodPrimary)
              .clipShape(RoundedRectangle(cornerRadius: 10))
              .shadow(color: .black.opacity(0.2), radius: 4, x: 0, y: 2)
            }
          }

          // Board List
          if !viewModel.boards.isEmpty {
            BoardListView(
              viewModel: viewModel,
              onEditBoard: { board in
                selectedBoardToEdit = board
              })
          }

          // Summary Section
          if !viewModel.boards.isEmpty {
            SummarySectionView(viewModel: viewModel)

            // Action Buttons
            ActionButtonsView(
              viewModel: viewModel,
              showingExportSheet: $showingExportSheet
            )
          }

          Spacer(minLength: 40)
        }
        .padding(.horizontal, 20)
      }

      // Home button and info button in top left - above ScrollView
      HStack(spacing: 12) {
        Button(action: {
          dismiss()
        }) {
          Image(systemName: "house.fill")
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(width: 25, height: 25)
            .foregroundColor(.darkBrown)
            .padding(8)
            .background(Color.white.opacity(0.9))
            .clipShape(Circle())
            .shadow(color: .black.opacity(0.3), radius: 4, x: 0, y: 2)
        }

        Button(action: {
          showingSummary = true
        }) {
          Image(systemName: "info.circle.fill")
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(width: 25, height: 25)
            .foregroundColor(.darkBrown)
            .padding(8)
            .background(Color.white.opacity(0.9))
            .clipShape(Circle())
            .shadow(color: .black.opacity(0.3), radius: 4, x: 0, y: 2)
        }
      }
      .padding(.leading, 20)
      .padding(.top, 20)
      .zIndex(100)

      // History button in top right
      VStack {
        HStack {
          Spacer()

          Button(action: {
            showingHistory = true
          }) {
            Text("History")
              .font(.subheadline)
              .fontWeight(.semibold)
              .foregroundColor(.white)
              .padding(.horizontal, 16)
              .padding(.vertical, 8)
              .background(Color.woodPrimary)
              .clipShape(RoundedRectangle(cornerRadius: 8))
              .shadow(color: .black.opacity(0.3), radius: 4, x: 0, y: 2)
          }
          .padding(.trailing, 20)
          .padding(.top, 20)
        }

        Spacer()
      }
      .zIndex(100)
    }
    .toolbar(.hidden, for: .navigationBar)
    .sheet(isPresented: $showingExportSheet) {
      ExportSheetView(
        exportData: viewModel.exportData(),
        onExport: {
          // Auto-save when exporting
          saveCurrentOrder()
        }
      )
    }
    .sheet(isPresented: $showingSaveOrderSheet) {
      SaveOrderView(boards: viewModel.boards) { orderName in
        let savedOrder = SavedOrder(orderName: orderName, boards: viewModel.boards)
        OrderPersistenceManager.shared.saveOrder(savedOrder)
        // Reset form completely after saving
        viewModel.clearAll()
        OrderPersistenceManager.shared.clearWorkInProgress()
      }
    }
    .fullScreenCover(isPresented: $showingHistory) {
      HistoryView()
    }
    .onAppear {
      // Load work in progress
      if let savedBoards = OrderPersistenceManager.shared.loadWorkInProgress() {
        viewModel.boards = savedBoards
      }

      // Listen for orders being loaded for editing
      NotificationCenter.default.addObserver(
        forName: .loadOrderForEditing,
        object: nil,
        queue: .main
      ) { notification in
        if let order = notification.userInfo?["order"] as? SavedOrder {
          viewModel.boards = order.boards
        }
      }
    }
    .onChange(of: viewModel.boards) { oldValue, newValue in
      // Auto-save work in progress
      OrderPersistenceManager.shared.saveWorkInProgress(boards: newValue)
    }
    .sheet(item: $selectedBoardToEdit) { board in
      EditBoardView(board: board) { updatedBoard in
        viewModel.updateBoard(updatedBoard)
      }
    }
    .overlay {
      if showingSummary {
        ToolSummaryModal(
          toolName: "Board Foot Calculator",
          summary: summary,
          isPresented: $showingSummary
        )
        .transition(.opacity)
        .zIndex(1000)
      }
    }
  }

  private func saveCurrentOrder() {
    guard !viewModel.boards.isEmpty else { return }
    let orderName = "Order \(OrderPersistenceManager.shared.getNextOrderNumber())"
    let savedOrder = SavedOrder(orderName: orderName, boards: viewModel.boards)
    OrderPersistenceManager.shared.saveOrder(savedOrder)
  }
}

// MARK: - Unit Toggle View
struct UnitToggleView: View {
  @Binding var selectedUnit: MeasurementUnit

  var body: some View {
    HStack(spacing: 0) {
      ForEach(MeasurementUnit.allCases, id: \.self) { unit in
        Button(action: {
          withAnimation(.easeInOut(duration: 0.2)) {
            selectedUnit = unit
          }
        }) {
          Text(unit.rawValue)
            .font(.subheadline)
            .fontWeight(.medium)
            .foregroundColor(selectedUnit == unit ? .white : .darkBrown)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .background(
              selectedUnit == unit ? Color.woodPrimary : Color.white.opacity(0.5)
            )
        }
      }
    }
    .clipShape(RoundedRectangle(cornerRadius: 10))
    .overlay(
      RoundedRectangle(cornerRadius: 10)
        .stroke(Color.woodPrimary, lineWidth: 1)
    )
    .frame(maxWidth: 300)
  }
}

// MARK: - Input Section View
struct InputSectionView: View {
  @Bindable var viewModel: BoardFootViewModel

  var dimensionLabel: (thickness: String, width: String, length: String) {
    switch viewModel.selectedUnit {
    case .imperial:
      return ("Thickness", "Width (inches)", "Length")
    case .metric:
      return ("Thickness (cm)", "Width (cm)", "Length (cm)")
    }
  }

  var body: some View {
    VStack(spacing: 16) {
      Text("Dimensions")
        .font(.headline)
        .foregroundColor(.darkBrown)
        .frame(maxWidth: .infinity, alignment: .leading)

      VStack(spacing: 12) {
        // Pricing Type Toggle
        HStack(spacing: 0) {
          ForEach(PricingType.allCases, id: \.self) { type in
            Button(action: {
              withAnimation(.easeInOut(duration: 0.2)) {
                viewModel.pricingType = type
              }
            }) {
              Text(type.rawValue)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(viewModel.pricingType == type ? .white : .darkBrown)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 10)
                .background(
                  viewModel.pricingType == type ? Color.woodPrimary : Color.white.opacity(0.5)
                )
            }
          }
        }
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .overlay(
          RoundedRectangle(cornerRadius: 8)
            .stroke(Color.woodPrimary, lineWidth: 1)
        )

        // Only show thickness and width for board foot pricing
        if viewModel.pricingType == .perBoardFoot {
          // Thickness input with special handling for quarters
          if viewModel.selectedUnit == .imperial {
            ThicknessQuartersInputField(
              label: dimensionLabel.thickness,
              value: $viewModel.thickness
            )
          } else {
            DimensionInputField(
              label: dimensionLabel.thickness,
              value: $viewModel.thickness
            )
          }

          DimensionInputField(
            label: dimensionLabel.width,
            value: $viewModel.width
          )
        }

        // Length input with toggle for Imperial
        if viewModel.selectedUnit == .imperial {
          LengthInputFieldWithToggle(
            label: dimensionLabel.length,
            value: $viewModel.length,
            lengthUnit: $viewModel.lengthUnit
          )
        } else {
          DimensionInputField(
            label: dimensionLabel.length,
            value: $viewModel.length
          )
        }

        DimensionInputField(
          label: "Quantity",
          value: $viewModel.quantity
        )

        // Wood Species Picker
        WoodSpeciesPickerField(value: $viewModel.woodSpecies)

        // Price Input
        VStack(alignment: .leading, spacing: 6) {
          Text("Price ($)")
            .font(.subheadline)
            .foregroundColor(.darkBrown.opacity(0.8))

          TextField("0.00", text: $viewModel.price)
            .keyboardType(.decimalPad)
            .font(.body)
            .foregroundColor(.darkBrown)
            .padding(12)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .overlay(
              RoundedRectangle(cornerRadius: 8)
                .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1)
            )
        }
      }

      Button(action: {
        viewModel.addBoard()
        // Clear dimension fields after adding (keep price and wood species to persist)
        viewModel.thickness = ""
        viewModel.width = ""
        viewModel.length = ""
        viewModel.quantity = "1"
      }) {
        HStack {
          Image(systemName: "plus.circle.fill")
          Text("Add Board")
            .fontWeight(.semibold)
        }
        .foregroundColor(.white)
        .frame(maxWidth: .infinity)
        .padding(.vertical, 14)
        .background(
          viewModel.canAddBoard ? Color.forestGreen : Color.gray.opacity(0.5)
        )
        .clipShape(RoundedRectangle(cornerRadius: 10))
        .shadow(color: .black.opacity(0.2), radius: 4, x: 0, y: 2)
      }
      .disabled(!viewModel.canAddBoard)
    }
    .padding(16)
    .background(Color.white.opacity(0.6))
    .clipShape(RoundedRectangle(cornerRadius: 12))
  }
}

// MARK: - Dimension Input Field
struct DimensionInputField: View {
  let label: String
  @Binding var value: String

  var body: some View {
    VStack(alignment: .leading, spacing: 6) {
      Text(label)
        .font(.subheadline)
        .foregroundColor(.darkBrown.opacity(0.8))

      TextField("0", text: $value)
        .keyboardType(.decimalPad)
        .font(.body)
        .foregroundColor(.darkBrown)
        .padding(12)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .overlay(
          RoundedRectangle(cornerRadius: 8)
            .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1)
        )
    }
  }
}

// MARK: - Thickness Quarters Input Field
struct ThicknessQuartersInputField: View {
  let label: String
  @Binding var value: String

  var body: some View {
    VStack(alignment: .leading, spacing: 6) {
      Text(label)
        .font(.subheadline)
        .foregroundColor(.darkBrown.opacity(0.8))

      HStack(spacing: 4) {
        TextField("0", text: $value)
          .keyboardType(.numberPad)
          .font(.body)
          .foregroundColor(.darkBrown)
          .padding(12)
          .background(Color.white)
          .clipShape(RoundedRectangle(cornerRadius: 8))
          .overlay(
            RoundedRectangle(cornerRadius: 8)
              .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1)
          )
          .frame(width: 80)

        Text("/4")
          .font(.body)
          .fontWeight(.medium)
          .foregroundColor(.darkBrown)

        Spacer()
      }
    }
  }
}

// MARK: - Wood Species Picker Field
struct WoodSpeciesPickerField: View {
  @Binding var value: String
  @State private var showingPicker = false

  var body: some View {
    VStack(alignment: .leading, spacing: 6) {
      Text("Wood Species (Optional)")
        .font(.subheadline)
        .foregroundColor(.darkBrown.opacity(0.8))

      HStack(spacing: 8) {
        TextField("Type or select", text: $value)
          .font(.body)
          .foregroundColor(.darkBrown)
          .padding(12)
          .background(Color.white)
          .clipShape(RoundedRectangle(cornerRadius: 8))
          .overlay(
            RoundedRectangle(cornerRadius: 8)
              .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1)
          )

        Button(action: {
          showingPicker = true
        }) {
          Image(systemName: "chevron.down.circle.fill")
            .font(.title3)
            .foregroundColor(.woodPrimary)
        }
      }
    }
    .sheet(isPresented: $showingPicker) {
      WoodSpeciesPickerView(selectedSpecies: $value)
    }
  }
}

// MARK: - Wood Species Picker View
struct WoodSpeciesPickerView: View {
  @Environment(\.dismiss) private var dismiss
  @Binding var selectedSpecies: String

  var body: some View {
    NavigationStack {
      ZStack {
        Color.creamBackground.ignoresSafeArea()

        List {
          ForEach(WoodSpecies.commonHardwoods, id: \.self) { species in
            Button(action: {
              selectedSpecies = species
              dismiss()
            }) {
              HStack {
                Text(species)
                  .foregroundColor(.darkBrown)

                Spacer()

                if selectedSpecies == species {
                  Image(systemName: "checkmark")
                    .foregroundColor(.forestGreen)
                    .fontWeight(.semibold)
                }
              }
            }
            .listRowBackground(Color.white)
          }
        }
        .listStyle(.insetGrouped)
        .scrollContentBackground(.hidden)
      }
      .navigationTitle("Select Wood Species")
      .navigationBarTitleDisplayMode(.inline)
      .toolbar {
        ToolbarItem(placement: .navigationBarTrailing) {
          Button("Done") {
            dismiss()
          }
          .foregroundColor(.woodPrimary)
        }
      }
    }
  }
}

// MARK: - Length Input Field With Toggle
struct LengthInputFieldWithToggle: View {
  let label: String
  @Binding var value: String
  @Binding var lengthUnit: LengthUnit

  var body: some View {
    VStack(alignment: .leading, spacing: 6) {
      HStack {
        Text(label)
          .font(.subheadline)
          .foregroundColor(.darkBrown.opacity(0.8))

        Spacer()

        // Toggle between feet and inches
        HStack(spacing: 0) {
          ForEach(LengthUnit.allCases, id: \.self) { unit in
            Button(action: {
              withAnimation(.easeInOut(duration: 0.2)) {
                lengthUnit = unit
              }
            }) {
              Text(unit.rawValue)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(lengthUnit == unit ? .white : .darkBrown)
                .padding(.horizontal, 12)
                .padding(.vertical, 6)
                .background(
                  lengthUnit == unit ? Color.woodPrimary : Color.white.opacity(0.5)
                )
            }
          }
        }
        .clipShape(RoundedRectangle(cornerRadius: 6))
        .overlay(
          RoundedRectangle(cornerRadius: 6)
            .stroke(Color.woodPrimary, lineWidth: 1)
        )
      }

      TextField("0", text: $value)
        .keyboardType(.decimalPad)
        .font(.body)
        .foregroundColor(.darkBrown)
        .padding(12)
        .background(Color.white)
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .overlay(
          RoundedRectangle(cornerRadius: 8)
            .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1)
        )
    }
  }
}

// MARK: - Board List View
struct BoardListView: View {
  @Bindable var viewModel: BoardFootViewModel
  let onEditBoard: (BoardEntry) -> Void

  var body: some View {
    VStack(spacing: 12) {
      Text("Added Boards")
        .font(.headline)
        .foregroundColor(.darkBrown)
        .frame(maxWidth: .infinity, alignment: .leading)

      VStack(spacing: 8) {
        ForEach(viewModel.boards) { board in
          BoardRowView(board: board, viewModel: viewModel)
            .onTapGesture {
              onEditBoard(board)
            }
        }
      }
    }
    .padding(16)
    .background(Color.white.opacity(0.6))
    .clipShape(RoundedRectangle(cornerRadius: 12))
  }
}

// MARK: - Board Row View
struct BoardRowView: View {
  let board: BoardEntry
  @Bindable var viewModel: BoardFootViewModel

  var body: some View {
    HStack(spacing: 12) {
      VStack(alignment: .leading, spacing: 4) {
        Text(board.displayString)
          .font(.subheadline)
          .fontWeight(.medium)
          .foregroundColor(.darkBrown)

        if board.pricingType == .perBoardFoot && board.boardFeet > 0 {
          Text("\(String(format: "%.2f", board.boardFeet)) bf")
            .font(.caption)
            .foregroundColor(.darkBrown.opacity(0.7))
        }

        if let price = board.price {
          Text("\(board.pricingType.rawValue): $\(String(format: "%.2f", price))")
            .font(.caption)
            .foregroundColor(.darkBrown.opacity(0.7))
        }

        if board.cost > 0 {
          Text("Cost: $\(String(format: "%.2f", board.cost))")
            .font(.caption)
            .foregroundColor(.forestGreen)
            .fontWeight(.semibold)
        }
      }

      Spacer()

      Button(action: {
        withAnimation {
          viewModel.removeBoard(board)
        }
      }) {
        Image(systemName: "trash.fill")
          .foregroundColor(.red.opacity(0.7))
          .frame(width: 36, height: 36)
          .background(Color.white)
          .clipShape(Circle())
      }
    }
    .padding(12)
    .background(Color.white)
    .clipShape(RoundedRectangle(cornerRadius: 10))
    .shadow(color: .black.opacity(0.1), radius: 2, x: 0, y: 1)
  }
}

// MARK: - Summary Section View
struct SummarySectionView: View {
  @Bindable var viewModel: BoardFootViewModel

  var body: some View {
    VStack(spacing: 16) {
      Text("Summary")
        .font(.headline)
        .foregroundColor(.darkBrown)
        .frame(maxWidth: .infinity, alignment: .leading)

      VStack(spacing: 12) {
        if viewModel.totalBoardFeet > 0 {
          HStack {
            Text("Total Board Feet:")
              .font(.body)
              .fontWeight(.medium)
              .foregroundColor(.darkBrown)

            Spacer()

            Text("\(String(format: "%.2f", viewModel.totalBoardFeet)) bf")
              .font(.title3)
              .fontWeight(.bold)
              .foregroundColor(.woodPrimary)
          }
        }

        if viewModel.totalCost > 0 {
          if viewModel.totalBoardFeet > 0 {
            Divider()
          }

          HStack {
            Text("Total Cost:")
              .font(.body)
              .fontWeight(.medium)
              .foregroundColor(.darkBrown)

            Spacer()

            Text("$\(String(format: "%.2f", viewModel.totalCost))")
              .font(.title2)
              .fontWeight(.bold)
              .foregroundColor(.forestGreen)
          }
        }
      }
      .padding(16)
      .background(Color.white)
      .clipShape(RoundedRectangle(cornerRadius: 10))
    }
    .padding(16)
    .background(
      LinearGradient(
        colors: [Color.woodSecondary.opacity(0.3), Color.woodPrimary.opacity(0.2)],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
      )
    )
    .clipShape(RoundedRectangle(cornerRadius: 12))
    .overlay(
      RoundedRectangle(cornerRadius: 12)
        .stroke(Color.woodPrimary.opacity(0.5), lineWidth: 2)
    )
  }
}

// MARK: - Action Buttons View
struct ActionButtonsView: View {
  @Bindable var viewModel: BoardFootViewModel
  @Binding var showingExportSheet: Bool

  var body: some View {
    HStack(spacing: 12) {
      Button(action: {
        showingExportSheet = true
      }) {
        HStack {
          Image(systemName: "square.and.arrow.up")
          Text("Export")
            .fontWeight(.semibold)
        }
        .foregroundColor(.white)
        .frame(maxWidth: .infinity)
        .padding(.vertical, 14)
        .background(Color.woodPrimary)
        .clipShape(RoundedRectangle(cornerRadius: 10))
        .shadow(color: .black.opacity(0.2), radius: 4, x: 0, y: 2)
      }

      Button(action: {
        withAnimation {
          viewModel.clearAll()
        }
      }) {
        HStack {
          Image(systemName: "trash")
          Text("Clear All")
            .fontWeight(.semibold)
        }
        .foregroundColor(.white)
        .frame(maxWidth: .infinity)
        .padding(.vertical, 14)
        .background(Color.red.opacity(0.7))
        .clipShape(RoundedRectangle(cornerRadius: 10))
        .shadow(color: .black.opacity(0.2), radius: 4, x: 0, y: 2)
      }
    }
  }
}

// MARK: - Export Sheet View
struct ExportSheetView: View {
  @Environment(\.dismiss) private var dismiss
  let exportData: String
  let onExport: () -> Void

  var body: some View {
    NavigationStack {
      VStack(spacing: 20) {
        ScrollView {
          Text(exportData)
            .font(.system(.body, design: .monospaced))
            .foregroundColor(.darkBrown)
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .padding()
        }

        ShareLink(item: exportData) {
          HStack {
            Image(systemName: "square.and.arrow.up")
            Text("Share / Save")
              .fontWeight(.semibold)
          }
          .foregroundColor(.white)
          .frame(maxWidth: .infinity)
          .padding(.vertical, 14)
          .background(Color.forestGreen)
          .clipShape(RoundedRectangle(cornerRadius: 10))
          .padding(.horizontal)
        }
      }
      .background(Color.creamBackground)
      .navigationTitle("Export Data")
      .navigationBarTitleDisplayMode(.inline)
      .toolbar {
        ToolbarItem(placement: .navigationBarTrailing) {
          Button("Done") {
            onExport()  // Call the auto-save
            dismiss()
          }
          .foregroundColor(.woodPrimary)
        }
      }
      .onAppear {
        onExport()  // Also auto-save when sheet appears
      }
    }
  }
}

#Preview {
  BoardFootCalculatorView(summary: ToolSummaries.boardFootCalculator)
}
