package com.example.todolist.presentation.presenters.listOfToDoViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.TodoItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListOfTodoViewModel(
    private val todoInteractor: TodoStorageInteractor,
) : ViewModel() {

    private var hideDoneItems = true
    private var searchJob: Job? = null

    private val _liveTodoInfo = MutableLiveData<List<TodoItem>>()
    val liveTodoInfo = _liveTodoInfo

    private val _liveVisibility = MutableLiveData<Boolean>()
    val liveVisibility = _liveVisibility

    fun loadTodoList() {
        viewModelScope.launch {
            todoInteractor.getTodoList().collect { result ->
                val filteredList = if (hideDoneItems) {
                    result.filter { !it.done }
                } else {
                    result
                }
                _liveTodoInfo.postValue(filteredList)
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

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}