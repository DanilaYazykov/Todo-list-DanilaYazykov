package com.example.todolist.data.dataBase.domain.impl

import com.example.todolist.data.dataBase.domain.api.DeletedItemDao
import com.example.todolist.data.dataBase.models.DeletedItems
import javax.inject.Inject

/**
 * Domain класс. Содержит в себе бизнес-логику приложения.
 */
class DeletedItemDaoImpl @Inject constructor(
    private val deletedItemDao: DeletedItemDao
) : DeletedItemDao {
    override suspend fun addToDeletedList(deletedItem: DeletedItems) {
        deletedItemDao.addToDeletedList(deletedItem)
    }

    override suspend fun getDeletedItems(): List<DeletedItems> {
        return deletedItemDao.getDeletedItems()
    }
}