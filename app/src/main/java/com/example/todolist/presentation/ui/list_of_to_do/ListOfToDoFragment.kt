package com.example.todolist.presentation.ui.list_of_to_do

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentListOfToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModel
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModelFactory
import com.example.todolist.presentation.ui.add_to_do.AddToDoFragment
import com.example.todolist.presentation.ui.api.OnCheckedClickListener
import com.example.todolist.presentation.ui.api.OnItemClickListener
import com.example.todolist.presentation.ui.util.BindingFragment

class ListOfToDoFragment : BindingFragment<FragmentListOfToDoBinding>(), OnItemClickListener, OnCheckedClickListener {

    private lateinit var adapter: ListToDoAdapter
    private lateinit var viewModel: ListOfTodoViewModel

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListOfToDoBinding {
        return FragmentListOfToDoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this, ListOfTodoViewModelFactory())[ListOfTodoViewModel::class.java]

        adaptersInit()
        viewModel.loadTodoList()
        viewModel.liveTodoInfo.observe(viewLifecycleOwner) { list ->
           showTodoList(list)
        }

        binding.addFragmentButton.setOnClickListener {
            findNavController().navigate(R.id.action_listOfToDoFragment_to_addToDoFragment)
        }

        binding.ivEyeVisibility.setOnClickListener {
            viewModel.changeVisibility()
        }

        viewModel.liveVisibility.observe(viewLifecycleOwner) { visibility ->
            eyeImageVisibility(visibility)
        }
    }

    private fun adaptersInit() = with(binding) {
        rcViewToDoList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ListToDoAdapter(this@ListOfToDoFragment, this@ListOfToDoFragment)
        rcViewToDoList.adapter = adapter
    }

    private fun sumOfDoneTodos(list: List<TodoItem>) {
        val doneCount = list.count { it.done }
        if (doneCount == 0) binding.tvSumOfDone.text = ""
        else binding.tvSumOfDone.text = getString(R.string.done_count, doneCount)
    }

    private fun showTodoList(list: List<TodoItem>) {
        if (list.isEmpty()) {
            binding.rcViewToDoList.visibility = View.GONE
            binding.ivTodoAnim.visibility = View.VISIBLE
            binding.tvAddFirstTask.visibility = View.VISIBLE
        } else {
            binding.rcViewToDoList.visibility = View.VISIBLE
            binding.ivTodoAnim.visibility = View.GONE
            binding.tvAddFirstTask.visibility = View.GONE
        }
        adapter.submitList(list)
        sumOfDoneTodos(list)
    }

    private fun eyeImageVisibility(visibility: Boolean) {
        if (visibility) {
            binding.ivEyeVisibility.setImageResource(R.drawable.ic_eye_visibility)
        } else {
            binding.ivEyeVisibility.setImageResource(R.drawable.ic_eye_visibility_gone)
        }
    }

    override fun onItemClick(todo: TodoItem) {
        findNavController().navigate(
            R.id.action_listOfToDoFragment_to_addToDoFragment,
            AddToDoFragment.createArgs(todo)
        )
    }

    override fun onCheckedChange(todoItem: String, isChecked: Boolean) {
        viewModel.addDone(todoItem, isChecked)
    }
}