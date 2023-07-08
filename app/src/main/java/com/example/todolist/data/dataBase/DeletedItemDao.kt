package com.example.todolist.data.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DeletedItemDao {

    @Insert(entity = DeletedItems::class ,onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToDeletedList(deletedItem: DeletedItems)

    @Query("SELECT * FROM deletedItemsTable WHERE deleted = 1")
    suspend fun getDeletedItems(): List<DeletedItems>

}