package com.example.todolist.data.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.domain.models.TodoItem

/**
 * DeletedItem - класс, который отвечает за хранение информации о том, был ли удален элемент.
 * Нужен для офлайн-режима
 */
@Entity(tableName = "deletedItemsTable")
data class DeletedItems(
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

fun DeletedItems.toUI(): TodoItem =
    TodoItem(
        id = id,
        text = text,
        importance = importance,
        deadline = deadline,
        done = done,
        color = color,
        creationDate = creationDate,
        modificationDate = modificationDate,
        lastUpdatedBy = lastUpdatedBy,
        synced = synced,
        deleted = deleted
    )

fun TodoItem.toDeleted(): DeletedItems =
    DeletedItems(
        id = id,
        text = text,
        importance = importance,
        deadline = deadline,
        done = done,
        color = color,
        creationDate = creationDate,
        modificationDate = modificationDate,
        lastUpdatedBy = lastUpdatedBy,
        synced = synced,
        deleted = deleted
    )
