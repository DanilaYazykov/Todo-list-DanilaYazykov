package com.example.todolist.presentation.presenters.listOfToDoViewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.domain.api.TodoInteractor
import com.example.todolist.domain.models.TodoItem

class ListOfTodoViewModel(
    private val todoInteractor: TodoInteractor,
) : ViewModel() {

    private var hideDoneItems = true
    private val handler = Handler(Looper.getMainLooper())

    private val _liveTodoInfo = MutableLiveData<List<TodoItem>>()
    val liveTodoInfo = _liveTodoInfo

    private val _liveVisibility = MutableLiveData<Boolean>()
    val liveVisibility = _liveVisibility

    private val todoInfoConsumer: TodoInteractor.TodoInfoConsumer =
        object : TodoInteractor.TodoInfoConsumer {
            override fun onTodoInfoReceived(todoInfo: List<TodoItem>) {
                val filteredList = if (hideDoneItems) {
                    todoInfo.filter { !it.done }
                } else {
                    todoInfo
                }
                _liveTodoInfo.postValue(filteredList)
            }
        }

    fun loadTodoList() {
        todoInteractor.getTodoList(todoInfoConsumer = todoInfoConsumer)
    }

    fun changeVisibility() {
        hideDoneItems = !hideDoneItems
        loadTodoList()
        _liveVisibility.postValue(hideDoneItems)
    }

    fun addDone(itemId: String, isChecked: Boolean) {
        todoInteractor.addDone(itemId, isChecked)
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            loadTodoList()
        }, 300)
    }
}