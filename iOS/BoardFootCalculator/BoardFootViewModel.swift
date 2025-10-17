//
//  BoardFootViewModel.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import Foundation
import SwiftUI

@Observable
class BoardFootViewModel {
  // Unit selection
  var selectedUnit: MeasurementUnit = .imperial
  var lengthUnit: LengthUnit = .feet

  // Input fields
  var thickness: String = ""
  var width: String = ""
  var length: String = ""
  var quantity: String = "1"
  var woodSpecies: String = ""

  // Pricing
  var pricingType: PricingType = .perBoardFoot
  var price: String = ""

  // Board list
  var boards: [BoardEntry] = []

  // Computed properties
  var totalBoardFeet: Double {
    boards.reduce(0) { $0 + $1.boardFeet }
  }

  var totalCost: Double {
    boards.reduce(0) { $0 + $1.cost }
  }

  var canAddBoard: Bool {
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

  // MARK: - Actions

  func addBoard() {
    guard let l = Double(length),
      let q = Int(quantity)
    else {
      return
    }

    let priceValue = Double(price)

    let species = woodSpecies.trimmingCharacters(in: .whitespacesAndNewlines)
    let speciesValue = species.isEmpty ? nil : species

    // For linear pricing, thickness and width are optional/nil
    if pricingType == .linear {
      let board = BoardEntry(
        thickness: nil,
        width: nil,
        length: l,
        quantity: q,
        unit: selectedUnit,
        lengthUnit: selectedUnit == .imperial ? lengthUnit : nil,
        price: priceValue,
        pricingType: pricingType,
        woodSpecies: speciesValue
      )
      boards.append(board)
      return
    }

    // For board foot pricing, need thickness and width
    guard let t = Double(thickness),
      let w = Double(width)
    else {
      return
    }

    let board = BoardEntry(
      thickness: t,
      width: w,
      length: l,
      quantity: q,
      unit: selectedUnit,
      lengthUnit: selectedUnit == .imperial ? lengthUnit : nil,
      price: priceValue,
      pricingType: pricingType,
      woodSpecies: speciesValue
    )

    boards.append(board)
  }

  func removeBoard(at offsets: IndexSet) {
    boards.remove(atOffsets: offsets)
  }

  func removeBoard(_ board: BoardEntry) {
    boards.removeAll { $0.id == board.id }
  }

  func updateBoard(_ updatedBoard: BoardEntry) {
    if let index = boards.firstIndex(where: { $0.id == updatedBoard.id }) {
      boards[index] = updatedBoard
    }
  }

  func clearAll() {
    boards.removeAll()
    thickness = ""
    width = ""
    length = ""
    quantity = "1"
    price = ""
    woodSpecies = ""
  }

  func applyPreset(_ preset: LumberPreset) {
    thickness = String(preset.thickness)
    width = String(preset.width)
  }

  func exportData() -> String {
    var output = "Board Foot Calculator - \(Date().formatted())\n"
    output += "Unit: \(selectedUnit.rawValue)\n"
    output += String(repeating: "-", count: 50) + "\n\n"

    for (index, board) in boards.enumerated() {
      output += "Board \(index + 1):\n"

      if let species = board.woodSpecies {
        output += "  Species: \(species)\n"
      }

      output += "  Dimensions: \(board.displayStringWithoutSpecies)\n"

      if board.pricingType == .perBoardFoot {
        output += "  Board Feet: \(String(format: "%.2f", board.boardFeet)) bf\n"
      }

      if board.price != nil {
        output +=
          "  Pricing: \(board.pricingType.rawValue) @ $\(String(format: "%.2f", board.price!))\n"
        output += "  Cost: $\(String(format: "%.2f", board.cost))\n"
      }
      output += "\n"
    }

    output += String(repeating: "-", count: 50) + "\n"

    // Break down board feet by species
    if totalBoardFeet > 0 {
      var speciesBreakdown: [String: (boardFeet: Double, cost: Double)] = [:]

      for board in boards {
        if board.pricingType == .perBoardFoot && board.boardFeet > 0 {
          let species = board.woodSpecies ?? "Misc"
          let current = speciesBreakdown[species] ?? (boardFeet: 0, cost: 0)
          speciesBreakdown[species] = (
            boardFeet: current.boardFeet + board.boardFeet,
            cost: current.cost + board.cost
          )
        }
      }

      if !speciesBreakdown.isEmpty {
        output += "BOARD FEET BY SPECIES:\n"
        for (species, data) in speciesBreakdown.sorted(by: { $0.key < $1.key }) {
          output +=
            "  \(species): \(String(format: "%.2f", data.boardFeet)) bf - $\(String(format: "%.2f", data.cost))\n"
        }
        output += "\n"
      }
    }

    if totalCost > 0 {
      output += "TOTAL COST: $\(String(format: "%.2f", totalCost))\n"
    }

    return output
  }
}
