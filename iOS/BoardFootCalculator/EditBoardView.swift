//
//  EditBoardView.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import SwiftUI

struct EditBoardView: View {
  @Environment(\.dismiss) private var dismiss
  let board: BoardEntry
  let onSave: (BoardEntry) -> Void

  @State private var thickness: String
  @State private var width: String
  @State private var length: String
  @State private var quantity: String
  @State private var price: String
  @State private var pricingType: PricingType
  @State private var lengthUnit: LengthUnit
  @State private var woodSpecies: String

  init(board: BoardEntry, onSave: @escaping (BoardEntry) -> Void) {
    self.board = board
    self.onSave = onSave

    // Initialize state from board
    _thickness = State(initialValue: board.thickness.map { String($0) } ?? "")
    _width = State(initialValue: board.width.map { String($0) } ?? "")
    _length = State(initialValue: String(board.length))
    _quantity = State(initialValue: String(board.quantity))
    _price = State(initialValue: board.price.map { String($0) } ?? "")
    _pricingType = State(initialValue: board.pricingType)
    _lengthUnit = State(initialValue: board.lengthUnit ?? .feet)
    _woodSpecies = State(initialValue: board.woodSpecies ?? "")
  }

  var canSave: Bool {
    // For linear pricing, only need length
    if pricingType == .linear {
      guard let l = Double(length), l > 0,
        let q = Int(quantity), q > 0
      else {
        return false
      }
      return true
    }

    // For board foot pricing, need all dimensions
    guard let t = Double(thickness), t > 0,
      let w = Double(width), w > 0,
      let l = Double(length), l > 0,
      let q = Int(quantity), q > 0
    else {
      return false
    }
    return true
  }

  var dimensionLabel: (thickness: String, width: String, length: String) {
    switch board.unit {
    case .imperial:
      return ("Thickness", "Width (inches)", "Length")
    case .metric:
      return ("Thickness (cm)", "Width (cm)", "Length (cm)")
    }
  }

  var body: some View {
    NavigationStack {
      ScrollView {
        VStack(spacing: 20) {
          // Pricing Type Toggle
          VStack(alignment: .leading, spacing: 8) {
            Text("Pricing Type")
              .font(.headline)
              .foregroundColor(.darkBrown)

            HStack(spacing: 0) {
              ForEach(PricingType.allCases, id: \.self) { type in
                Button(action: {
                  withAnimation(.easeInOut(duration: 0.2)) {
                    pricingType = type
                  }
                }) {
                  Text(type.rawValue)
                    .font(.caption)
                    .fontWeight(.medium)
                    .foregroundColor(pricingType == type ? .white : .darkBrown)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 10)
                    .background(
                      pricingType == type ? Color.woodPrimary : Color.white.opacity(0.5)
                    )
                }
              }
            }
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .overlay(
              RoundedRectangle(cornerRadius: 8)
                .stroke(Color.woodPrimary, lineWidth: 1)
            )
          }

          // Dimensions Section
          VStack(spacing: 12) {
            // Only show thickness and width for board foot pricing
            if pricingType == .perBoardFoot {
              // Thickness
              if board.unit == .imperial {
                ThicknessQuartersInputField(
                  label: dimensionLabel.thickness,
                  value: $thickness
                )
              } else {
                DimensionInputField(
                  label: dimensionLabel.thickness,
                  value: $thickness
                )
              }

              // Width
              DimensionInputField(
                label: dimensionLabel.width,
                value: $width
              )
            }

            // Length
            if board.unit == .imperial {
              LengthInputFieldWithToggle(
                label: dimensionLabel.length,
                value: $length,
                lengthUnit: $lengthUnit
              )
            } else {
              DimensionInputField(
                label: dimensionLabel.length,
                value: $length
              )
            }

            // Quantity
            DimensionInputField(
              label: "Quantity",
              value: $quantity
            )

            // Wood Species
            WoodSpeciesPickerField(value: $woodSpecies)

            // Price
            VStack(alignment: .leading, spacing: 6) {
              Text("Price ($)")
                .font(.subheadline)
                .foregroundColor(.darkBrown.opacity(0.8))

              TextField("0.00", text: $price)
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
          .padding(16)
          .background(Color.white.opacity(0.6))
          .clipShape(RoundedRectangle(cornerRadius: 12))

          Spacer()
        }
        .padding(20)
      }
      .background(Color.creamBackground)
      .navigationTitle("Edit Board")
      .navigationBarTitleDisplayMode(.inline)
      .toolbar {
        ToolbarItem(placement: .navigationBarLeading) {
          Button("Cancel") {
            dismiss()
          }
          .foregroundColor(.darkBrown)
        }

        ToolbarItem(placement: .navigationBarTrailing) {
          Button("Save") {
            saveChanges()
          }
          .foregroundColor(.forestGreen)
          .fontWeight(.semibold)
          .disabled(!canSave)
        }
      }
    }
  }

  private func saveChanges() {
    guard let l = Double(length),
      let q = Int(quantity)
    else {
      return
    }

    let priceValue = Double(price)
    let species = woodSpecies.trimmingCharacters(in: .whitespacesAndNewlines)
    let speciesValue = species.isEmpty ? nil : species

    let updatedBoard: BoardEntry

    if pricingType == .linear {
      updatedBoard = BoardEntry(
        id: board.id,  // Keep same ID
        thickness: nil,
        width: nil,
        length: l,
        quantity: q,
        unit: board.unit,
        lengthUnit: board.unit == .imperial ? lengthUnit : nil,
        price: priceValue,
        pricingType: pricingType,
        woodSpecies: speciesValue
      )
    } else {
      guard let t = Double(thickness),
        let w = Double(width)
      else {
        return
      }

      updatedBoard = BoardEntry(
        id: board.id,  // Keep same ID
        thickness: t,
        width: w,
        length: l,
        quantity: q,
        unit: board.unit,
        lengthUnit: board.unit == .imperial ? lengthUnit : nil,
        price: priceValue,
        pricingType: pricingType,
        woodSpecies: speciesValue
      )
    }

    onSave(updatedBoard)
    dismiss()
  }
}
