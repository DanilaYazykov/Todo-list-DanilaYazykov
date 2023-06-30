package com.example.todolist.presentation.presenters.listOfToDoViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.dto.TodoPostList
import com.example.todolist.domain.api.TodoNetworkInteractor
import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.ui.util.CheckingInternet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListOfTodoViewModel(
    private val todoInteractor: TodoStorageInteractor,
    private val todoNetworkInteractor: TodoNetworkInteractor,
    private val internet: CheckingInternet
) : ViewModel() {

    private var hideDoneItems = true
    private var sync = false
    private var searchJob: Job? = null
    private var networkCheckJob: Job? = null

    private val _liveTodoInfo = MutableLiveData<List<TodoItem>>()
    val liveTodoInfo = _liveTodoInfo

    private val _liveVisibility = MutableLiveData<Boolean>()
    val liveVisibility = _liveVisibility

    private val _internetLiveData = MutableLiveData<Boolean>()
    val getInternetStateLiveData = _internetLiveData

    private var unfiltredTodo: List<TodoItem> = emptyList()

    fun loadTodoList() {
        viewModelScope.launch {
            checkNetwork()
            todoInteractor.getTodoList().collect { result ->
                val filteredList = if (hideDoneItems) {
                    result.filter { !it.done }
                } else {
                    result
                }
                _liveTodoInfo.postValue(filteredList)
                unfiltredTodo = result
            }
        }
    }

    fun syncTodoListFromNetwork() {
        if (sync) return
        if (_internetLiveData.value == false) return
        viewModelScope.launch {
            todoNetworkInteractor.getListFromServer().collect { result ->
                val listFromNetwork = result.second.list
                val listFromLocal = unfiltredTodo

                val localItemIds = listFromLocal.map { it.id }
                val networkItemIds = listFromNetwork.map { it.id }

                val newItems = listFromNetwork.filter { it.id !in localItemIds }

                val updatedItems = listFromNetwork.filter { newItem ->
                    listFromLocal.find { localItem -> localItem.id == newItem.id }
                        ?.let { localItem ->
                            !newItem.equals(localItem)
                        } ?: false
                }

                val deletedItems = listFromLocal.filter { it.id !in networkItemIds }

                if (newItems.isNotEmpty() || updatedItems.isNotEmpty() || deletedItems.isNotEmpty()) {
                    newItems.forEach { newItem ->
                        todoInteractor.addTodoItem(newItem)
                    }
                    updatedItems.forEach { updatedItem ->
                        todoInteractor.addTodoItem(updatedItem)
                    }
                    deletedItems.forEach { deletedItem ->
                        todoInteractor.deleteTodoItem(deletedItem)
                    }
                    loadTodoList()
                }
            }
            sync = true
        }
    }

    fun updateDataServer() {
        if (_internetLiveData.value == false) return
        viewModelScope.launch {
            try {
                todoNetworkInteractor.placeListToServer(TodoPostList("ok", unfiltredTodo))
            } catch (e: Exception) {
                Log.e("ListOfTodoViewModel", "Error in updateDataServer", e)
            }
        }
    }
    fun changeVisibility() {
        hideDoneItems = !hideDoneItems
        loadTodoList()
        _liveVisibility.postValue(hideDoneItems)
    }

    fun addDone(itemId: String, isChecked: Boolean) {
        viewModelScope.launch {
            todoInteractor.addDone(itemId, isChecked)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            loadTodoList()
        }
    }

    private fun checkNetwork() {
        networkCheckJob?.cancel()
        networkCheckJob = viewModelScope.launch(Dispatchers.IO) {
            val result = internet.isNetworkAvailable()
            if (result) {
                syncTodoListFromNetwork()
            }
            _internetLiveData.postValue(result)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        networkCheckJob?.cancel()
    }
}