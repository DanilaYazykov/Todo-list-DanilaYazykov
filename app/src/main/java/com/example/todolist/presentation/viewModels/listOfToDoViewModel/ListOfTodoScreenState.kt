package com.example.todolist.presentation.viewModels.listOfToDoViewModel

import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.domain.models.DoneState
import com.example.todolist.domain.models.InternetState
import com.example.todolist.domain.models.TodoItem

sealed interface ListOfTodoScreenState {

    data class TodoInfo(val todoInfo: Pair<NetworkResult, List<TodoItem>>) : ListOfTodoScreenState

    data class FilteredTodoInfo(val filteredTodoInfo: Pair<NetworkResult, List<TodoItem>>) :
        ListOfTodoScreenState

    data class DoneVisibility(val doneStatus: DoneState = DoneState.default()) :
        ListOfTodoScreenState

    data class InternetVisibility(val internetStatus: InternetState = InternetState.default()) :
        ListOfTodoScreenState

}