//
//  SaveOrderView.swift
//  Woodworker's Companion
//
//  Created by James Corey on 10/15/25.
//

import SwiftUI

struct SaveOrderView: View {
  @Environment(\.dismiss) private var dismiss
  @State private var orderName: String = ""
  let boards: [BoardEntry]
  let onSave: (String) -> Void

  var body: some View {
    NavigationStack {
      VStack(spacing: 24) {
        Text("Save Order")
          .font(.title2)
          .fontWeight(.bold)
          .foregroundColor(.darkBrown)
          .padding(.top, 20)

        Text("Give your order a name (optional)")
          .font(.subheadline)
          .foregroundColor(.darkBrown.opacity(0.7))

        TextField("Order name", text: $orderName)
          .font(.body)
          .foregroundColor(.darkBrown)
          .padding(12)
          .background(Color.white)
          .clipShape(RoundedRectangle(cornerRadius: 8))
          .overlay(
            RoundedRectangle(cornerRadius: 8)
              .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1)
          )
          .padding(.horizontal, 20)

        Spacer()

        VStack(spacing: 12) {
          Button(action: {
            let name = orderName.trimmingCharacters(in: .whitespacesAndNewlines)
            let finalName =
              name.isEmpty ? "Order \(OrderPersistenceManager.shared.getNextOrderNumber())" : name
            onSave(finalName)
            dismiss()
          }) {
            Text(
              orderName.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
                ? "Skip Naming and Save" : "Save"
            )
            .fontWeight(.semibold)
            .foregroundColor(.white)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 14)
            .background(Color.forestGreen)
            .clipShape(RoundedRectangle(cornerRadius: 10))
            .shadow(color: .black.opacity(0.2), radius: 4, x: 0, y: 2)
          }
          .padding(.horizontal, 20)

          Button(action: {
            dismiss()
          }) {
            Text("Cancel")
              .fontWeight(.medium)
              .foregroundColor(.darkBrown)
              .frame(maxWidth: .infinity)
              .padding(.vertical, 14)
              .background(Color.white.opacity(0.6))
              .clipShape(RoundedRectangle(cornerRadius: 10))
              .overlay(
                RoundedRectangle(cornerRadius: 10)
                  .stroke(Color.woodPrimary.opacity(0.3), lineWidth: 1)
              )
          }
          .padding(.horizontal, 20)
        }
        .padding(.bottom, 40)
      }
      .background(Color.creamBackground)
    }
  }
}
