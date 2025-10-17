package com.example.woodworkerscompanion.data.repository

import com.example.woodworkerscompanion.data.database.BoardDao
import com.example.woodworkerscompanion.data.database.BoardEntryEntity
import com.example.woodworkerscompanion.data.database.SavedOrderEntity
import com.example.woodworkerscompanion.data.database.SavedOrderWithBoards
import com.example.woodworkerscompanion.data.models.BoardEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class OrderRepository(private val boardDao: BoardDao) {

    // Work in Progress
    suspend fun saveWorkInProgress(boards: List<BoardEntry>) {
        // Clear existing WIP
        boardDao.clearWorkInProgress()
        
        // Save new WIP with special IDs
        if (boards.isNotEmpty()) {
            val wipBoards = boards.mapIndexed { index, board ->
                BoardEntryEntity.fromBoardEntry(
                    board.copy(id = "wip_$index")
                )
            }
            boardDao.insertBoards(wipBoards)
        }
    }

    suspend fun loadWorkInProgress(): List<BoardEntry>? {
        val wipBoards = boardDao.getWorkInProgress()
        return if (wipBoards.isNotEmpty()) {
            wipBoards.map { it.toBoardEntry() }
        } else {
            null
        }
    }

    suspend fun clearWorkInProgress() {
        boardDao.clearWorkInProgress()
    }

    // Saved Orders
    suspend fun saveOrder(orderName: String?, boards: List<BoardEntry>): String {
        val orderId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()
        
        // Save board entries
        val boardEntities = boards.map { BoardEntryEntity.fromBoardEntry(it) }
        boardDao.insertBoards(boardEntities)
        
        // Save order
        val order = SavedOrderEntity(
            id = orderId,
            orderName = orderName,
            timestamp = timestamp,
            boardIds = boards.map { it.id }
        )
        boardDao.insertOrder(order)
        
        return orderId
    }

    fun getAllOrders(): Flow<List<SavedOrderWithBoards>> {
        return boardDao.getAllOrders().map { orders ->
            orders.map { order ->
                val boards = boardDao.getBoardsByIds(order.boardIds)
                    .map { it.toBoardEntry() }
                SavedOrderWithBoards(order, boards)
            }
        }
    }

    suspend fun getOrderById(orderId: String): SavedOrderWithBoards? {
        val order = boardDao.getOrderById(orderId) ?: return null
        val boards = boardDao.getBoardsByIds(order.boardIds)
            .map { it.toBoardEntry() }
        return SavedOrderWithBoards(order, boards)
    }

    suspend fun deleteOrder(orderId: String) {
        val order = boardDao.getOrderById(orderId)
        if (order != null) {
            // Delete associated board entries
            boardDao.deleteBoardsByIds(order.boardIds)
            // Delete order
            boardDao.deleteOrderById(orderId)
        }
    }

    suspend fun deleteAllOrders() {
        val orders = boardDao.getAllOrders()
        boardDao.deleteAllOrders()
        // Note: We might want to also delete orphaned board entries
    }

    suspend fun loadOrderForEditing(orderId: String): List<BoardEntry>? {
        val order = getOrderById(orderId)
        return order?.boards
    }
}

