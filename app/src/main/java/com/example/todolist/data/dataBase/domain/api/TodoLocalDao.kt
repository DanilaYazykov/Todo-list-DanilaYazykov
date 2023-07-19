package com.example.todolist.data.dataBase.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

/**
 * Несмотря на то, что находится в пакете dataBase(слоя data), является частью слоя domain.
 * Не стал разделять на разные пакеты, для лучшей навигации.
 * Представляет собой интерфейс для работы с базой данных.
 */
@Dao
interface TodoLocalDao {

    @Insert(entity = TodoItem::class ,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(todoItem: TodoItem)

    @Insert(entity = TodoItem::class ,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListTodoItem(todoItem: List<TodoItem>)

    @Update(entity = TodoItem::class ,onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodoItem(todoItem: TodoItem)

    @Delete(entity = TodoItem::class)
    suspend fun deleteTodoItem(todoItem: TodoItem)

    @Query("SELECT * FROM todoItemsTable")
    fun getAllTodoItems(): Flow<List<TodoItem>>

    @Query("DELETE FROM todoItemsTable")
    suspend fun deleteAllTodoItems()

    @Query("UPDATE todoItemsTable SET done = :isChecked, modificationDate = :modification WHERE id = :id")
    suspend fun updateCurrentItemDone(id: String, isChecked: Boolean, modification: Long)

    @Query("SELECT * FROM todoItemsTable WHERE synced = 0")
    suspend fun getUnsyncedItems(): List<TodoItem>

    @Query("UPDATE todoItemsTable SET synced = :synced WHERE id = :id")
    suspend fun markSynced(id: String, synced: Boolean)

}