package com.example.woodworkerscompanion.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {
    // Board Entries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoard(board: BoardEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoards(boards: List<BoardEntryEntity>)

    @Query("SELECT * FROM board_entries WHERE id = :boardId")
    suspend fun getBoardById(boardId: String): BoardEntryEntity?

    @Query("SELECT * FROM board_entries WHERE id IN (:boardIds)")
    suspend fun getBoardsByIds(boardIds: List<String>): List<BoardEntryEntity>

    @Delete
    suspend fun deleteBoard(board: BoardEntryEntity)

    @Query("DELETE FROM board_entries WHERE id = :boardId")
    suspend fun deleteBoardById(boardId: String)

    @Query("DELETE FROM board_entries WHERE id IN (:boardIds)")
    suspend fun deleteBoardsByIds(boardIds: List<String>)

    // Saved Orders
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: SavedOrderEntity)

    @Query("SELECT * FROM saved_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<SavedOrderEntity>>

    @Query("SELECT * FROM saved_orders WHERE id = :orderId")
    suspend fun getOrderById(orderId: String): SavedOrderEntity?

    @Delete
    suspend fun deleteOrder(order: SavedOrderEntity)

    @Query("DELETE FROM saved_orders WHERE id = :orderId")
    suspend fun deleteOrderById(orderId: String)

    @Query("DELETE FROM saved_orders")
    suspend fun deleteAllOrders()

    // Work in Progress
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWorkInProgress(boards: List<BoardEntryEntity>)

    @Query("SELECT * FROM board_entries WHERE id LIKE 'wip_%'")
    suspend fun getWorkInProgress(): List<BoardEntryEntity>

    @Query("DELETE FROM board_entries WHERE id LIKE 'wip_%'")
    suspend fun clearWorkInProgress()
}

