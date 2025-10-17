//
//  SavedOrderModels.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import Foundation

// MARK: - Notification Names
extension Notification.Name {
  static let loadOrderForEditing = Notification.Name("loadOrderForEditing")
}

// MARK: - Saved Order
struct SavedOrder: Identifiable, Codable {
  let id: UUID
  let orderName: String
  let dateSaved: Date
  let boards: [BoardEntry]
  let totalBoardFeet: Double
  let totalCost: Double

  init(id: UUID = UUID(), orderName: String, boards: [BoardEntry]) {
    self.id = id
    self.orderName = orderName
    self.dateSaved = Date()
    self.boards = boards
    self.totalBoardFeet = boards.reduce(0) { $0 + $1.boardFeet }
    self.totalCost = boards.reduce(0) { $0 + $1.cost }
  }

  // Generate export text
  func exportText() -> String {
    var output = "\(orderName)\n"
    output += "Saved: \(dateSaved.formatted())\n"
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

// MARK: - Persistence Manager
class OrderPersistenceManager {
  static let shared = OrderPersistenceManager()

  private let ordersKey = "savedOrders"
  private let workInProgressKey = "workInProgress"

  private init() {}

  // MARK: - Saved Orders

  func saveOrder(_ order: SavedOrder) {
    var orders = loadOrders()
    orders.insert(order, at: 0)  // Add to beginning for newest first

    if let encoded = try? JSONEncoder().encode(orders) {
      UserDefaults.standard.set(encoded, forKey: ordersKey)
    }
  }

  func loadOrders() -> [SavedOrder] {
    guard let data = UserDefaults.standard.data(forKey: ordersKey),
      let orders = try? JSONDecoder().decode([SavedOrder].self, from: data)
    else {
      return []
    }
    return orders
  }

  func deleteOrder(_ order: SavedOrder) {
    var orders = loadOrders()
    orders.removeAll { $0.id == order.id }

    if let encoded = try? JSONEncoder().encode(orders) {
      UserDefaults.standard.set(encoded, forKey: ordersKey)
    }
  }

  func getNextOrderNumber() -> Int {
    let orders = loadOrders()
    // Find highest order number
    let numbers = orders.compactMap { order -> Int? in
      if order.orderName.starts(with: "Order ") {
        return Int(order.orderName.replacingOccurrences(of: "Order ", with: ""))
      }
      return nil
    }
    return (numbers.max() ?? 0) + 1
  }

  // MARK: - Work in Progress

  func saveWorkInProgress(boards: [BoardEntry]) {
    if let encoded = try? JSONEncoder().encode(boards) {
      UserDefaults.standard.set(encoded, forKey: workInProgressKey)
    }
  }

  func loadWorkInProgress() -> [BoardEntry]? {
    guard let data = UserDefaults.standard.data(forKey: workInProgressKey),
      let boards = try? JSONDecoder().decode([BoardEntry].self, from: data)
    else {
      return nil
    }
    return boards
  }

  func clearWorkInProgress() {
    UserDefaults.standard.removeObject(forKey: workInProgressKey)
  }
}
