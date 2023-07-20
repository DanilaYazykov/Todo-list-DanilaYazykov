package com.example.todolist.data.dataBase.converters

import com.example.todolist.data.dataBase.DeletedItemsEntity
import com.example.todolist.domain.models.TodoItem

class DeleteItemDbConverter {

    fun map (deletedItem: TodoItem): DeletedItemsEntity {
        return DeletedItemsEntity(
            id = deletedItem.id,
            text = deletedItem.text,
            importance = deletedItem.importance,
            deadline = deletedItem.deadline,
            done = deletedItem.done,
            color = deletedItem.color,
            creationDate = deletedItem.creationDate,
            modificationDate = deletedItem.modificationDate,
            lastUpdatedBy = deletedItem.lastUpdatedBy,
            synced = deletedItem.synced,
            deleted = deletedItem.deleted
        )
    }
}