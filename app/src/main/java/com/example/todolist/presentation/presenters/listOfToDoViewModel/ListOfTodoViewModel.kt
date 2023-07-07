package com.example.todolist.presentation.presenters.listOfToDoViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.api.TodoNetworkInteractor
import com.example.todolist.domain.api.TodoStorageInteractor
import com.example.todolist.domain.models.ListState
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.utils.CheckingInternet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ListOfTodoViewModel - viewModel UI класса ListOfToDoFragment. Связывает слои Presentation и Domain.
 */
class ListOfTodoViewModel(
    private val todoInteractor: TodoStorageInteractor,
    private val todoNetworkInteractor: TodoNetworkInteractor,
    private val internet: CheckingInternet,
    private val dataParser: DataParser
) : ViewModel() {

    private var hideDoneItems = true
    private var sync = false
    private var searchJob: Job? = null
    private var networkCheckJob: Job? = null
    private var replaceJob: Job? = null
    private var revision = 0
    private var unfiltredTodo: List<TodoItem> = emptyList()

    private val _todoInfo = MutableLiveData<Pair<NetworkResult, List<TodoItem>>>()
    val todoInfo = _todoInfo

    private val _internetAndDoneVisibility = MutableLiveData(ListState.default())
    val getStateLiveData = _internetAndDoneVisibility

    fun loadTodoList() {
        viewModelScope.launch {
            checkNetwork()
            todoInteractor.getTodoList().collect { result ->
                val filteredList = if (hideDoneItems) {
                    result.filter { !it.done }
                } else {
                    result
                }
                _todoInfo.postValue(Pair(NetworkResult.SUCCESS_200, filteredList))
                unfiltredTodo = result
            }
        }
    }

    fun syncTodoListFromNetwork() {
        if (sync) return
        if (_internetAndDoneVisibility.value?.internet == false) return
        viewModelScope.launch {
            todoNetworkInteractor.getListFromServer().collect { result ->
                revision = result.second.revision
                if (result.first == NetworkResult.SUCCESS_200) {
                    val currentList = dataParser.parseData(result, todoInteractor, unfiltredTodo, hideDoneItems)
                    unfiltredTodo = currentList.second
                    _todoInfo.postValue(currentList)
                    _internetAndDoneVisibility.postValue(
                        _internetAndDoneVisibility.value?.copy(doneVisibility = false)
                    )
                    changeVisibility()
                } else {
                    _todoInfo.postValue(Pair(result.first, result.second.list))
                }
            }
            sync = true
        }
    }

    fun updateDataServer() {
        if (_internetAndDoneVisibility.value?.internet == false) return
        replaceJob?.cancel()
        replaceJob = viewModelScope.launch {
            delay(DELAY_1000)
            try {
                val unsyncedItems = todoInteractor.getUnsyncedItems()
                todoNetworkInteractor.placeListToServer(TodoPostList("ok", unfiltredTodo), revision)
                unsyncedItems.forEach { todoInteractor.markAsSynced(it.id) }
            } catch (e: Exception) {
                Log.e(TAG, "updateDataServer: ${e.message}")
            }
        }
    }

    fun changeVisibility() {
        viewModelScope.launch {
            hideDoneItems = !hideDoneItems
            _internetAndDoneVisibility.value = _internetAndDoneVisibility.value?.copy(doneVisibility = hideDoneItems)
            loadTodoList()
        }
    }

    fun addDone(itemId: String, isChecked: Boolean) {
        viewModelScope.launch {
            todoInteractor.addDone(itemId, isChecked)
            todoInteractor.markAsNotSynced(itemId)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(DELAY_300)
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
            _internetAndDoneVisibility.postValue(_internetAndDoneVisibility.value?.copy(
                internet = result
            ))
        }
    }

    fun resetSyncFlag() {
        sync = false
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
        networkCheckJob?.cancel()
        replaceJob?.cancel()
    }

    companion object {
        private const val TAG = "Exception"
        private const val DELAY_300 = 300L
        private const val DELAY_1000 = 1000L
    }
}