package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemDao {
    @Query("SELECT * FROM saved_ai_items ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<SavedAiItem>>

    @Query("SELECT * FROM saved_ai_items WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteItems(): Flow<List<SavedAiItem>>

    @Query("SELECT * FROM saved_ai_items WHERE toolId = :toolId ORDER BY timestamp DESC")
    fun getItemsByTool(toolId: String): Flow<List<SavedAiItem>>

    @Query("SELECT * FROM saved_ai_items WHERE inputPrompt LIKE '%' || :query || '%' OR outputContent LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchItems(query: String): Flow<List<SavedAiItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SavedAiItem): Long

    @Update
    suspend fun updateItem(item: SavedAiItem)

    @Delete
    suspend fun deleteItem(item: SavedAiItem)

    @Query("DELETE FROM saved_ai_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM saved_ai_items")
    suspend fun clearAll()
}
