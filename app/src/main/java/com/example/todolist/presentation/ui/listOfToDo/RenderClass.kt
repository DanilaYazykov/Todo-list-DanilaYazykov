package com.example.todolist.presentation.ui.listOfToDo

import android.view.View
import com.example.todolist.R
import com.example.todolist.data.network.network.NetworkResult
import com.example.todolist.databinding.FragmentListOfToDoBinding
import com.example.todolist.domain.models.DoneState
import com.example.todolist.domain.models.InternetState
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.ListOfTodoViewModel
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.UpdateDataServerEvent
import com.google.android.material.snackbar.Snackbar

/**
 * RenderClass - класс, который отвечает за отрисовку списка задач.
 */
class RenderClass {

    private fun renderList(list: Pair<NetworkResult, List<TodoItem>>, binding: FragmentListOfToDoBinding) =
        renderListPrivate(list, binding)

    private fun renderListPrivate(
        list: Pair<NetworkResult, List<TodoItem>>,
        binding: FragmentListOfToDoBinding
    ) {
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

    private fun getErrorMessage(
        networkResult: NetworkResult,
        binding: FragmentListOfToDoBinding
    ): String {
        return when (networkResult) {
            NetworkResult.ERROR_SERVER_500 -> binding.root.context.getString(R.string.error_server_message)
            NetworkResult.ID_TODO_NOT_FOUND_404 -> binding.root.context.getString(R.string.id_not_found_message)
            NetworkResult.UNCORRECT_AUTHORIZATION_401 -> binding.root.context.getString(R.string.unauthorized_message)
            NetworkResult.ERROR_UNSYNCHRONIZED_DATA_400 ->
                binding.root.context.getString(R.string.unsynchronized_data_message)
            NetworkResult.UNKNOWN_ERROR -> binding.root.context.getString(R.string.unknown_error_message)
            else -> binding.root.context.getString(R.string.unknown_error_message)
        }
    }

    fun showFilteredInfo(
        list: Pair<NetworkResult, List<TodoItem>>, binding: FragmentListOfToDoBinding,
        adapter: ListToDoAdapter
    ) {
        renderList(list, binding)
        adapter.submitList(list.second)
        sumOfDoneTodos(list.second, binding)
    }

    fun showInternetStatus(
        internetAndDoneStatus: InternetState,
        binding: FragmentListOfToDoBinding,
        viewModel: ListOfTodoViewModel
    ) {
        if (!internetAndDoneStatus.internet) {
            showSnackBar(binding)
            binding.swipeRefreshLayout.isEnabled = false
        } else {
            binding.swipeRefreshLayout.isEnabled = true
            viewModel.action(UpdateDataServerEvent())
        }
    }

    fun showDoneStatus(
        internetAndDoneStatus: DoneState,
        binding: FragmentListOfToDoBinding
    ) {
        eyeImageVisibility(internetAndDoneStatus.doneVisibility, binding)
    }

    private fun sumOfDoneTodos(list: List<TodoItem>, binding: FragmentListOfToDoBinding) {
        val doneCount = list.count { it.done }
        if (doneCount == 0) binding.tvSumOfDone.text = ""
        else binding.tvSumOfDone.text = binding.root.context.getString(R.string.done_count, doneCount)
    }

    private fun showSnackBar(binding: FragmentListOfToDoBinding) {
        Snackbar.make(
            binding.root,
            binding.root.context.getString(R.string.data_not_sync),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun eyeImageVisibility(visibility: Boolean, binding: FragmentListOfToDoBinding) {
        if (visibility) {
            binding.ivEyeVisibility.setImageResource(R.drawable.ic_eye_visibility_gone)
        } else {
            binding.ivEyeVisibility.setImageResource(R.drawable.ic_eye_visibility)
        }
    }

}