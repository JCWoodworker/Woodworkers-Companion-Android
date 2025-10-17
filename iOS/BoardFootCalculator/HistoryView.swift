//
//  HistoryView.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import SwiftUI

struct HistoryView: View {
  @Environment(\.dismiss) private var dismiss
  @Environment(\.horizontalSizeClass) private var horizontalSizeClass
  @State private var savedOrders: [SavedOrder] = []
  @State private var selectedOrder: SavedOrder? = nil
  @State private var showingDeleteAllConfirmation = false
  @State private var orderToEdit: SavedOrder? = nil

  var isCompactDevice: Bool {
    horizontalSizeClass == .compact
  }

  var body: some View {
    ZStack(alignment: .topLeading) {
      Color.creamBackground.ignoresSafeArea()

      VStack(spacing: 20) {
        // Title - always centered
        Text("Order History")
          .font(.title)
          .fontWeight(.bold)
          .foregroundColor(.darkBrown)
          .padding(.top, 80)
          .frame(maxWidth: .infinity, alignment: .center)

        if savedOrders.isEmpty {
          // Centered empty state
          Spacer()

          VStack(spacing: 12) {
            Image(systemName: "tray")
              .font(.system(size: 60))
              .foregroundColor(.darkBrown.opacity(0.3))

            Text("No saved orders yet")
              .font(.title3)
              .foregroundColor(.darkBrown.opacity(0.6))
          }
          .frame(maxWidth: .infinity)

          Spacer()
        } else {
          // Delete All Button
          HStack {
            Spacer()

            Button(action: {
              showingDeleteAllConfirmation = true
            }) {
              HStack(spacing: 6) {
                Image(systemName: "trash")
                  .font(.caption)
                Text("Delete All")
                  .font(.caption)
                  .fontWeight(.semibold)
              }
              .foregroundColor(.white)
              .padding(.horizontal, 12)
              .padding(.vertical, 8)
              .background(Color.red.opacity(0.7))
              .clipShape(RoundedRectangle(cornerRadius: 8))
              .shadow(color: .black.opacity(0.2), radius: 3, x: 0, y: 2)
            }
          }
          .padding(.horizontal, 20)
          .padding(.bottom, 8)
          // Show cards for compact devices, table for regular
          if isCompactDevice {
            // Card View for iPhone and iPad mini
            List {
              ForEach(savedOrders) { order in
                OrderCard(order: order)
                  .listRowBackground(Color.clear)
                  .listRowInsets(EdgeInsets(top: 6, leading: 20, bottom: 6, trailing: 20))
                  .listRowSeparator(.hidden)
                  .onTapGesture {
                    selectedOrder = order
                  }
                  .swipeActions(edge: .trailing, allowsFullSwipe: false) {
                    Button(role: .destructive) {
                      deleteOrder(order)
                    } label: {
                      Label("Delete", systemImage: "trash")
                    }

                    Button {
                      orderToEdit = order
                      dismiss()
                    } label: {
                      Label("Edit", systemImage: "pencil")
                    }
                    .tint(Color.woodPrimary)

                    Button {
                      selectedOrder = order
                    } label: {
                      Label("Share", systemImage: "square.and.arrow.up")
                    }
                    .tint(Color.forestGreen)
                  }
              }
            }
            .listStyle(.plain)
            .scrollContentBackground(.hidden)
          } else {
            // Table View for larger iPads
            // Table Header
            HStack(spacing: 12) {
              Text("Order Name")
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(.darkBrown)
                .frame(maxWidth: .infinity, alignment: .leading)

              Text("Date")
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(.darkBrown)
                .frame(width: 100, alignment: .leading)

              Text("Time")
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(.darkBrown)
                .frame(width: 80, alignment: .leading)

              Text("Total")
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundColor(.darkBrown)
                .frame(minWidth: 80, alignment: .trailing)
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
            .background(Color.woodPrimary.opacity(0.2))
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .padding(.horizontal, 20)

            // Orders List
            List {
              ForEach(savedOrders) { order in
                OrderRow(order: order)
                  .listRowBackground(Color.clear)
                  .listRowInsets(EdgeInsets(top: 4, leading: 20, bottom: 4, trailing: 20))
                  .listRowSeparator(.hidden)
                  .onTapGesture {
                    selectedOrder = order
                  }
                  .swipeActions(edge: .trailing, allowsFullSwipe: false) {
                    Button(role: .destructive) {
                      deleteOrder(order)
                    } label: {
                      Label("Delete", systemImage: "trash")
                    }

                    Button {
                      orderToEdit = order
                      dismiss()
                    } label: {
                      Label("Edit", systemImage: "pencil")
                    }
                    .tint(Color.woodPrimary)

                    Button {
                      selectedOrder = order
                    } label: {
                      Label("Share", systemImage: "square.and.arrow.up")
                    }
                    .tint(Color.forestGreen)
                  }
              }
            }
            .listStyle(.plain)
            .scrollContentBackground(.hidden)
          }
        }
      }

      // Back button in top left
      Button(action: {
        dismiss()
      }) {
        HStack(spacing: 6) {
          Image(systemName: "arrow.left")
            .font(.system(size: 16, weight: .semibold))
          Text("Back to Board Foot Calculator")
            .font(.subheadline)
            .fontWeight(.semibold)
        }
        .foregroundColor(.white)
        .padding(.horizontal, 12)
        .padding(.vertical, 8)
        .background(Color.woodPrimary)
        .clipShape(RoundedRectangle(cornerRadius: 8))
        .shadow(color: .black.opacity(0.3), radius: 4, x: 0, y: 2)
      }
      .padding(.leading, 20)
      .padding(.top, 20)
      .zIndex(100)
    }
    .toolbar(.hidden, for: .navigationBar)
    .onAppear {
      loadOrders()
    }
    .sheet(item: $selectedOrder) { order in
      OrderDetailView(
        order: order,
        onDelete: {
          deleteOrder(order)
          selectedOrder = nil
        })
    }
    .alert("Delete All Orders", isPresented: $showingDeleteAllConfirmation) {
      Button("Cancel", role: .cancel) {}
      Button("Delete All", role: .destructive) {
        deleteAllOrders()
      }
    } message: {
      Text(
        "Are you sure you want to delete all \(savedOrders.count) orders? This cannot be undone.")
    }
    .onDisappear {
      // Pass edited order back to calculator
      if let order = orderToEdit {
        NotificationCenter.default.post(
          name: .loadOrderForEditing,
          object: nil,
          userInfo: ["order": order]
        )
      }
    }
  }

  private func loadOrders() {
    savedOrders = OrderPersistenceManager.shared.loadOrders()
  }

  private func deleteOrder(_ order: SavedOrder) {
    OrderPersistenceManager.shared.deleteOrder(order)
    loadOrders()
  }

  private func deleteAllOrders() {
    for order in savedOrders {
      OrderPersistenceManager.shared.deleteOrder(order)
    }
    loadOrders()
  }
}

// MARK: - Order Row (for Table View)
struct OrderRow: View {
  let order: SavedOrder

  var body: some View {
    HStack(spacing: 12) {
      Text(order.orderName)
        .font(.subheadline)
        .fontWeight(.medium)
        .foregroundColor(.darkBrown)
        .frame(maxWidth: .infinity, alignment: .leading)
        .lineLimit(1)

      Text(order.dateSaved.formatted(date: .numeric, time: .omitted))
        .font(.caption)
        .foregroundColor(.darkBrown.opacity(0.7))
        .frame(width: 100, alignment: .leading)

      Text(order.dateSaved.formatted(date: .omitted, time: .shortened))
        .font(.caption)
        .foregroundColor(.darkBrown.opacity(0.7))
        .frame(width: 80, alignment: .leading)

      Text("$\(String(format: "%.2f", order.totalCost))")
        .font(.subheadline)
        .fontWeight(.semibold)
        .foregroundColor(.forestGreen)
        .frame(minWidth: 80, alignment: .trailing)
        .fixedSize(horizontal: true, vertical: false)
    }
    .padding(.horizontal, 16)
    .padding(.vertical, 12)
    .background(Color.white.opacity(0.7))
    .clipShape(RoundedRectangle(cornerRadius: 8))
    .shadow(color: .black.opacity(0.1), radius: 2, x: 0, y: 1)
  }
}

// MARK: - Order Card (for Card View)
struct OrderCard: View {
  let order: SavedOrder

  var body: some View {
    VStack(alignment: .leading, spacing: 12) {
      // Order Name - Bold at top left
      Text(order.orderName)
        .font(.headline)
        .fontWeight(.bold)
        .foregroundColor(.darkBrown)
        .lineLimit(2)

      // Date and Time
      HStack(spacing: 4) {
        Image(systemName: "calendar")
          .font(.caption)
          .foregroundColor(.darkBrown.opacity(0.6))

        Text(order.dateSaved.formatted(date: .abbreviated, time: .omitted))
          .font(.subheadline)
          .foregroundColor(.darkBrown.opacity(0.7))

        Text("•")
          .foregroundColor(.darkBrown.opacity(0.4))

        Image(systemName: "clock")
          .font(.caption)
          .foregroundColor(.darkBrown.opacity(0.6))

        Text(order.dateSaved.formatted(date: .omitted, time: .shortened))
          .font(.subheadline)
          .foregroundColor(.darkBrown.opacity(0.7))
      }

      Divider()

      // Total Price - Highlighted
      HStack {
        Text("Total:")
          .font(.subheadline)
          .fontWeight(.medium)
          .foregroundColor(.darkBrown)

        Spacer()

        Text("$\(String(format: "%.2f", order.totalCost))")
          .font(.title3)
          .fontWeight(.bold)
          .foregroundColor(.forestGreen)
      }
    }
    .padding(16)
    .background(
      LinearGradient(
        colors: [Color.white, Color.white.opacity(0.95)],
        startPoint: .topLeading,
        endPoint: .bottomTrailing
      )
    )
    .clipShape(RoundedRectangle(cornerRadius: 12))
    .overlay(
      RoundedRectangle(cornerRadius: 12)
        .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1.5)
    )
    .shadow(color: .black.opacity(0.15), radius: 8, x: 0, y: 4)
  }
}

#Preview {
  HistoryView()
}
