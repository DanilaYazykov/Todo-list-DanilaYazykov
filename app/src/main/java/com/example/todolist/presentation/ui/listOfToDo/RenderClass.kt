package com.example.todolist.presentation.ui.listOfToDo

import android.view.View
import com.example.todolist.R
import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.databinding.FragmentListOfToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.google.android.material.snackbar.Snackbar

class RenderClass {

    fun renderList(list: Pair<NetworkResult, List<TodoItem>>, binding: FragmentListOfToDoBinding) =
        renderListPrivate(list, binding)

    private fun renderListPrivate(list: Pair<NetworkResult, List<TodoItem>>, binding: FragmentListOfToDoBinding) {
        binding.swipeRefreshLayout.isRefreshing = false
        when (list.first) {
            NetworkResult.SUCCESS_200 -> with(binding) {
                if (list.second.isEmpty()) {
                    swipeRefreshLayout.visibility = View.GONE
                    rcViewToDoList.visibility = View.GONE
                    ivTodoAnim.visibility = View.VISIBLE
                    tvAddFirstTask.visibility = View.VISIBLE
                } else {
                    swipeRefreshLayout.visibility = View.VISIBLE
                    rcViewToDoList.visibility = View.VISIBLE
                    ivTodoAnim.visibility = View.GONE
                    tvAddFirstTask.visibility = View.GONE
                }
            }
            else -> {
                val errorMessage = getErrorMessage(list.first, binding)
                showSnackBar(binding, errorMessage)
            }
        }
    }

    private fun showSnackBar(binding: FragmentListOfToDoBinding, message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getErrorMessage(networkResult: NetworkResult, binding: FragmentListOfToDoBinding): String {
        return when (networkResult) {
            NetworkResult.ERROR_SERVER_500 -> binding.root.context.getString(R.string.error_server_message)
            NetworkResult.ID_TODO_NOT_FOUND_404 -> binding.root.context.getString(R.string.id_not_found_message)
            NetworkResult.UNCORRECT_AUTHORIZATION_401 -> binding.root.context.getString(R.string.unauthorized_message)
            NetworkResult.ERROR_UNSYNCHRONIZED_DATA_400 -> binding.root.context.getString(R.string.unsynchronized_data_message)
            NetworkResult.UNKNOWN_ERROR -> binding.root.context.getString(R.string.unknown_error_message)
            else -> binding.root.context.getString(R.string.unknown_error_message)
        }
    }
}