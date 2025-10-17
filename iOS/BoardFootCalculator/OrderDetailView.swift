//
//  OrderDetailView.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import SwiftUI

struct OrderDetailView: View {
  @Environment(\.dismiss) private var dismiss
  let order: SavedOrder
  let onDelete: () -> Void
  @State private var showingDeleteConfirmation = false

  var body: some View {
    NavigationStack {
      VStack(spacing: 20) {
        ScrollView {
          Text(order.exportText())
            .font(.system(.body, design: .monospaced))
            .foregroundColor(.darkBrown)
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(Color.white)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .padding()
        }

        VStack(spacing: 12) {
          ShareLink(item: order.exportText()) {
            HStack {
              Image(systemName: "square.and.arrow.up")
              Text("Share / Print")
                .fontWeight(.semibold)
            }
            .foregroundColor(.white)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 14)
            .background(Color.forestGreen)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .padding(.horizontal)
          }

          Button(action: {
            showingDeleteConfirmation = true
          }) {
            HStack {
              Image(systemName: "trash")
              Text("Delete Order")
                .fontWeight(.semibold)
            }
            .foregroundColor(.white)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 14)
            .background(Color.red.opacity(0.7))
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .padding(.horizontal)
          }
        }
        .padding(.bottom, 20)
      }
      .background(Color.creamBackground)
      .navigationTitle(order.orderName)
      .navigationBarTitleDisplayMode(.inline)
      .toolbar {
        ToolbarItem(placement: .navigationBarTrailing) {
          Button("Done") {
            dismiss()
          }
          .foregroundColor(.woodPrimary)
        }
      }
      .alert("Delete Order", isPresented: $showingDeleteConfirmation) {
        Button("Cancel", role: .cancel) {}
        Button("Delete", role: .destructive) {
          onDelete()
        }
      } message: {
        Text("Are you sure you want to delete \"\(order.orderName)\"? This cannot be undone.")
      }
    }
  }
}
