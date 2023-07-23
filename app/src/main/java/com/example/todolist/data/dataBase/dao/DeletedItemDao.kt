package com.example.todolist.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todolist.data.dataBase.DeletedItemsEntity

/**
 * Представляет собой интерфейс для работы с базой данных.
 */
@Dao
interface DeletedItemDao {

    @Insert(entity = DeletedItemsEntity::class ,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDeletedList(deletedItem: DeletedItemsEntity)

    @Query("SELECT * FROM deletedItemsTable WHERE deleted = 0")
    suspend fun getDeletedItems(): List<DeletedItemsEntity>

    @Query("DELETE FROM deletedItemsTable")
    suspend fun deleteAllDeletedItems()

}