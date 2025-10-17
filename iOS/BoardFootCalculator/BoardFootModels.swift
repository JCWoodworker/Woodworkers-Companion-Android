//
//  BoardFootModels.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import Foundation

// MARK: - Measurement Unit
enum MeasurementUnit: String, CaseIterable, Codable {
  case imperial = "Imperial"
  case metric = "Metric"
}

// MARK: - Length Unit (for Imperial)
enum LengthUnit: String, CaseIterable, Codable {
  case feet = "ft"
  case inches = "in"
}

// MARK: - Pricing Type
enum PricingType: String, CaseIterable, Codable {
  case perBoardFoot = "Per Board Foot"
  case linear = "Linear"
}

// MARK: - Wood Species List
struct WoodSpecies {
  static let commonHardwoods: [String] = [
    "Ash",
    "Black Limba",
    "Bloodwood",
    "Cherry",
    "Douglas Fir",
    "Hickory",
    "Maple",
    "Maple (Ambrosia)",
    "Maple (Birdseye)",
    "Maple (Curly)",
    "Oak (Red)",
    "Oak (White)",
    "Padauk",
    "Pine",
    "Poplar",
    "Purple Heart",
    "Tigerwood",
    "Walnut (Black)",
    "Walnut (Peruvian)",
    "Wenge",
    "Zebrawood",
  ]
}

// MARK: - Board Entry
struct BoardEntry: Identifiable, Codable, Equatable {
  let id: UUID
  let thickness: Double?
  let width: Double?
  let length: Double
  let quantity: Int
  let unit: MeasurementUnit
  let lengthUnit: LengthUnit?  // Only used for Imperial
  let price: Double?
  let pricingType: PricingType
  let woodSpecies: String?

  init(
    id: UUID = UUID(), thickness: Double?, width: Double?, length: Double, quantity: Int,
    unit: MeasurementUnit, lengthUnit: LengthUnit?, price: Double?, pricingType: PricingType,
    woodSpecies: String?
  ) {
    self.id = id
    self.thickness = thickness
    self.width = width
    self.length = length
    self.quantity = quantity
    self.unit = unit
    self.lengthUnit = lengthUnit
    self.price = price
    self.pricingType = pricingType
    self.woodSpecies = woodSpecies
  }

  // Equatable conformance
  static func == (lhs: BoardEntry, rhs: BoardEntry) -> Bool {
    lhs.id == rhs.id
  }

  // Calculate board feet for this entry
  var boardFeet: Double {
    // Linear pricing doesn't use board feet
    if pricingType == .linear {
      return 0
    }

    guard let thickness = thickness, let width = width else { return 0 }

    switch unit {
    case .imperial:
      // For Imperial, thickness is in quarters, so convert to inches first
      // Board Feet = (Thickness in inches × Width in inches × Length in feet) / 12 × Quantity
      let thicknessInInches = thickness / 4.0

      // Convert length to feet if it's in inches
      let lengthInFeet: Double
      if lengthUnit == .inches {
        lengthInFeet = length / 12.0
      } else {
        lengthInFeet = length
      }

      return (thicknessInInches * width * lengthInFeet / 12.0) * Double(quantity)
    case .metric:
      // Convert to board feet: 1 board foot = 2359.737 cm³
      // Thickness (cm) × Width (cm) × Length (cm) / 2359.737 × Quantity
      return (thickness * width * length / 2359.737) * Double(quantity)
    }
  }

  // Calculate cost for this entry
  var cost: Double {
    guard let price = price else { return 0 }

    switch pricingType {
    case .perBoardFoot:
      return boardFeet * price
    case .linear:
      // For linear, just multiply length by price and quantity
      return length * price * Double(quantity)
    }
  }

  // Formatted display
  var displayString: String {
    let quantityStr = quantity > 1 ? "\(quantity) × " : ""
    let speciesStr = woodSpecies.map { " - \($0)" } ?? ""

    if pricingType == .linear {
      // Linear only shows length
      switch unit {
      case .imperial:
        let lengthSymbol = lengthUnit == .inches ? "\"" : "'"
        return "\(quantityStr)\(length)\(lengthSymbol)\(speciesStr)"
      case .metric:
        return "\(quantityStr)\(length)cm\(speciesStr)"
      }
    }

    guard let thickness = thickness, let width = width else { return "" }

    switch unit {
    case .imperial:
      // Show thickness in quarters format
      let thicknessInt = Int(thickness)
      let lengthSymbol = lengthUnit == .inches ? "\"" : "'"
      return
        "\(quantityStr)\(thicknessInt)/4\" × \(width)\" × \(length)\(lengthSymbol)\(speciesStr)"
    case .metric:
      return "\(quantityStr)\(thickness)cm × \(width)cm × \(length)cm\(speciesStr)"
    }
  }

  // Formatted display without species (for export text)
  var displayStringWithoutSpecies: String {
    let quantityStr = quantity > 1 ? "\(quantity) × " : ""

    if pricingType == .linear {
      // Linear only shows length
      switch unit {
      case .imperial:
        let lengthSymbol = lengthUnit == .inches ? "\"" : "'"
        return "\(quantityStr)\(length)\(lengthSymbol)"
      case .metric:
        return "\(quantityStr)\(length)cm"
      }
    }

    guard let thickness = thickness, let width = width else { return "" }

    switch unit {
    case .imperial:
      // Show thickness in quarters format
      let thicknessInt = Int(thickness)
      let lengthSymbol = lengthUnit == .inches ? "\"" : "'"
      return
        "\(quantityStr)\(thicknessInt)/4\" × \(width)\" × \(length)\(lengthSymbol)"
    case .metric:
      return "\(quantityStr)\(thickness)cm × \(width)cm × \(length)cm"
    }
  }
}

// MARK: - Common Lumber Sizes
struct LumberPreset {
  let name: String
  let thickness: Double
  let width: Double

  static let imperialPresets: [LumberPreset] = [
    LumberPreset(name: "1×2", thickness: 0.75, width: 1.5),
    LumberPreset(name: "1×3", thickness: 0.75, width: 2.5),
    LumberPreset(name: "1×4", thickness: 0.75, width: 3.5),
    LumberPreset(name: "1×6", thickness: 0.75, width: 5.5),
    LumberPreset(name: "1×8", thickness: 0.75, width: 7.25),
    LumberPreset(name: "1×10", thickness: 0.75, width: 9.25),
    LumberPreset(name: "1×12", thickness: 0.75, width: 11.25),
    LumberPreset(name: "2×4", thickness: 1.5, width: 3.5),
    LumberPreset(name: "2×6", thickness: 1.5, width: 5.5),
    LumberPreset(name: "2×8", thickness: 1.5, width: 7.25),
    LumberPreset(name: "2×10", thickness: 1.5, width: 9.25),
    LumberPreset(name: "2×12", thickness: 1.5, width: 11.25),
    LumberPreset(name: "4×4", thickness: 3.5, width: 3.5),
    LumberPreset(name: "6×6", thickness: 5.5, width: 5.5),
  ]

  static let metricPresets: [LumberPreset] = [
    LumberPreset(name: "2×5", thickness: 2, width: 5),
    LumberPreset(name: "2×10", thickness: 2, width: 10),
    LumberPreset(name: "2×15", thickness: 2, width: 15),
    LumberPreset(name: "3×10", thickness: 3, width: 10),
    LumberPreset(name: "3×15", thickness: 3, width: 15),
    LumberPreset(name: "4×10", thickness: 4, width: 10),
    LumberPreset(name: "4×15", thickness: 4, width: 15),
    LumberPreset(name: "5×10", thickness: 5, width: 10),
    LumberPreset(name: "5×15", thickness: 5, width: 15),
    LumberPreset(name: "5×20", thickness: 5, width: 20),
  ]
}
