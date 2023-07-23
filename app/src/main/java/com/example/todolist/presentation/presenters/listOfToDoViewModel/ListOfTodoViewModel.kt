package com.example.todolist.presentation.presenters.listOfToDoViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.api.TodoNetworkInteractor
import com.example.todolist.domain.dataBase.TodoLocalInteractor
import com.example.todolist.domain.models.ListState
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.utils.NetworkStateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * ListOfTodoViewModel - viewModel UI класса ListOfToDoFragment. Связывает слои Presentation и Domain.
 */
class ListOfTodoViewModel(
    private val todoNetworkInteractor: TodoNetworkInteractor,
    private val internetReceive: NetworkStateReceiver,
    private val database: TodoLocalInteractor
) : ViewModel() {

    private var hideDoneItems = true
    private var sync = false
    private var searchJob: Job? = null
    private var networkCheckJob: Job? = null
    private var replaceJob: Job? = null
    private var revision = 0

    private val _todoInfo = MutableLiveData<Pair<NetworkResult, List<TodoItem>>>()

    private val _filteredTodoInfo = MutableLiveData<Pair<NetworkResult, List<TodoItem>>>()
    val filteredTodoInfo = _filteredTodoInfo

    private val _internetAndDoneVisibility = MutableLiveData(ListState.default())
    val getStateLiveData = _internetAndDoneVisibility

    init {
        internetReceive.register()
    }

    fun loadTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            checkNetwork()
            database.getAllTodoItems().collect { result ->
                _todoInfo.postValue(Pair(NetworkResult.SUCCESS_200, result))
                if (hideDoneItems) {
                    _filteredTodoInfo.postValue(Pair(NetworkResult.SUCCESS_200, result.filter { !it.done }))
                } else {
                    _filteredTodoInfo.postValue(Pair(NetworkResult.SUCCESS_200, result))
                }
            }
        }
    }

    fun syncTodoListFromNetwork() {
        if (sync) return
        if (_internetAndDoneVisibility.value?.internet == false) return
        viewModelScope.launch {
            todoNetworkInteractor.getListFromServer().collect { result ->
                revision = result.second.revision
                val syncResult = result.second.list
                if (result.first == NetworkResult.SUCCESS_200) {
                    _todoInfo.postValue(Pair(result.first, syncResult))
                    database.apply {
                        deleteAllTodoItems()
                        insertListTodoItem(syncResult)
                    }
                    hideDoneItems = !hideDoneItems
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
        replaceJob = viewModelScope.launch(Dispatchers.IO) {
            val unsyncedItems = database.getUnsyncedItems()
            val deletedList = database.getDeletedItems()
            if (unsyncedItems.isEmpty() && deletedList.isEmpty()) return@launch
            val placeToServer = _todoInfo.value?.second ?: emptyList()
            delay(DELAY_1000)
            try {
                todoNetworkInteractor.placeListToServer(TodoPostList("ok", placeToServer), revision)
                unsyncedItems.forEach {
                    database.markSynced(it.id, true)
                }
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
        viewModelScope.launch(Dispatchers.IO) {
            val modificationDate = Calendar.getInstance().timeInMillis
            database.updateCurrentItemDone(itemId, isChecked, modification = modificationDate)
            database.markSynced(itemId, false)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(DELAY_300)
            loadTodoList()
        }
    }

    fun checkNetwork() {
        networkCheckJob?.cancel()
        networkCheckJob = viewModelScope.launch(Dispatchers.IO) {
            internetReceive.isNetworkAvailable().collect { result ->
                if (result) { syncTodoListFromNetwork() }
                _internetAndDoneVisibility.postValue(_internetAndDoneVisibility.value?.copy(internet = result))
            }
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
        internetReceive.unregister()
    }

    companion object {
        private const val TAG = "Exception"
        private const val DELAY_300 = 300L
        private const val DELAY_1000 = 1000L
    }
}