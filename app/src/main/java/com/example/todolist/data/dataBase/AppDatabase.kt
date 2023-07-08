package com.example.todolist.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.domain.models.TodoItem

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