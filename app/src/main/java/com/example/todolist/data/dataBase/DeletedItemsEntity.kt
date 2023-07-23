package com.example.todolist.data.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.domain.models.TodoItem

/**
 * DeletedItems - класс, представляющий собой модель данных для хранения удаленных элементов.
 * Нужен для работы в оффлайн режиме.
 */
@Entity(tableName = "deletedItemsTable")
data class DeletedItemsEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val importance: TodoItem.Importance = TodoItem.Importance.BASIC,
    val deadline: Long? = null,
    val done: Boolean,
    val color: String? = "#FFFFFF",
    val creationDate: Long? = null,
    val modificationDate: Long? = null,
    val lastUpdatedBy: String? = "unknown device",
    val synced: Boolean = false,
    val deleted: Boolean = false
)