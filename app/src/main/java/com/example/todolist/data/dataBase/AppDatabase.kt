package com.example.todolist.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.dataBase.domain.api.DeletedItemDao
import com.example.todolist.data.dataBase.domain.api.TodoLocalDao
import com.example.todolist.data.dataBase.models.DeletedItems
import com.example.todolist.domain.models.TodoItem

/**
 * AppDatabase - базовый класс для работы с локальной базой данных(ROOM).
 */
@Database(
    version = 3,
    entities = [
        TodoItem::class, DeletedItems::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoDao(): TodoLocalDao

    abstract fun getDeletedItemDao(): DeletedItemDao

}