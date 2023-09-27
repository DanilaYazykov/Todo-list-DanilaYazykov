package com.example.todolist.presentation.viewModels.listOfToDoViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.domain.models.TodoPostList
import com.example.todolist.domain.api.TodoNetworkInteractor
import com.example.todolist.domain.dataBase.TodoLocalInteractor
import com.example.todolist.domain.models.DoneState
import com.example.todolist.domain.models.InternetState
import com.example.todolist.utils.NetworkStateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

/** ListOfTodoViewModel - viewModel UI класса ListOfToDoFragment. Связывает слои Presentation и Domain */
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

    private val _uiState = MutableStateFlow<ListOfTodoScreenState>(ListOfTodoScreenState.InternetVisibility())
    val uiState: StateFlow<ListOfTodoScreenState> = _uiState

    init {
        internetReceive.register()
    }

    fun action(event: ListOfTodoEvent) {
        when(event) {
            is LoadTodoListEvent -> loadTodoList()
            is SyncTodoListFromNetworkEvent -> syncTodoListFromNetwork()
            is UpdateDataServerEvent -> updateDataServer()
            is ChangeVisibilityEvent -> changeVisibility()
            is AddDoneEvent -> addDone(event.itemId, event.isChecked)
            is CheckNetworkEvent -> checkNetwork()
            is ResetSyncFlagEvent -> resetSyncFlag()
        }
    }

    private fun loadTodoList() {
        viewModelScope.launch(Dispatchers.IO) {
            checkNetwork()
            database.getAllTodoItems().collect { result ->
                _uiState.value = ListOfTodoScreenState.TodoInfo(Pair(NetworkResult.SUCCESS_200, result))
                if (hideDoneItems) {
                    _uiState.value = ListOfTodoScreenState.FilteredTodoInfo(Pair(NetworkResult.SUCCESS_200, result.filter { !it.done }))
                } else {
                    _uiState.value = ListOfTodoScreenState.FilteredTodoInfo(Pair(NetworkResult.SUCCESS_200, result))
                }
            }
        }
    }

    private fun syncTodoListFromNetwork() {
        if (sync) return
        if (_uiState.value.let { it is ListOfTodoScreenState.InternetVisibility && !it.internetStatus.internet }) return
        viewModelScope.launch {
            todoNetworkInteractor.getListFromServer().collect { result ->
                revision = result.second.revision
                val syncResult = result.second.list
                if (result.first == NetworkResult.SUCCESS_200) {
                    _uiState.value = ListOfTodoScreenState.TodoInfo(Pair(result.first, syncResult))
                    database.apply {
                        deleteAllTodoItems()
                        insertListTodoItem(syncResult)
                    }
                    hideDoneItems = !hideDoneItems
                    changeVisibility()
                } else {
                    _uiState.value = ListOfTodoScreenState.TodoInfo(Pair(result.first, result.second.list))
                }
            }
            sync = true
        }
    }

    private fun updateDataServer() {
        if (_uiState.value.let { it is ListOfTodoScreenState.InternetVisibility && !it.internetStatus.internet }) return
        replaceJob?.cancel()
        replaceJob = viewModelScope.launch(Dispatchers.IO) {
            val unsyncedItems = database.getUnsyncedItems()
            val deletedList = database.getDeletedItems()
            if (unsyncedItems.isEmpty() && deletedList.isEmpty()) return@launch
            val placeToServer = _uiState.value.let {
                when (it) {
                    is ListOfTodoScreenState.TodoInfo -> it.todoInfo.second
                    else -> emptyList()
                }
            }
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

    private fun changeVisibility() {
        viewModelScope.launch {
            hideDoneItems = !hideDoneItems
            _uiState.value = ListOfTodoScreenState.DoneVisibility(doneStatus = DoneState(hideDoneItems))
            loadTodoList()
        }
    }

    private fun addDone(itemId: String, isChecked: Boolean) {
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

    private fun checkNetwork() {
        networkCheckJob?.cancel()
        networkCheckJob = viewModelScope.launch(Dispatchers.IO) {
            internetReceive.isNetworkAvailable().collect { result ->
                if (result) { syncTodoListFromNetwork() }
                _uiState.value = ListOfTodoScreenState.InternetVisibility(InternetState(internet = result))
            }
        }
    }

    private fun resetSyncFlag() {
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