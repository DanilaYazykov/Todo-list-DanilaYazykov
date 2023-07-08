package com.example.todolist.data.dataBase.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todolist.data.dataBase.models.DeletedItems

/**
 * Несмотря на то, что находится в пакете dataBase(слоя data), является частью слоя domain.
 * Не стал разделять на разные пакеты, для лучшей навигации.
 * Представляет собой интерфейс для работы с базой данных.
 */
@Dao
interface DeletedItemDao {

    @Insert(entity = DeletedItems::class ,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDeletedList(deletedItem: DeletedItems)

    @Query("SELECT * FROM deletedItemsTable WHERE deleted = 1")
    suspend fun getDeletedItems(): List<DeletedItems>

}