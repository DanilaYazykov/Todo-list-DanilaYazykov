package com.example.todolist.presentation.ui.listOfToDo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.app.App
import com.example.todolist.databinding.FragmentListOfToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModel
import com.example.todolist.presentation.presenters.listOfToDoViewModel.ListOfTodoViewModelFactory
import com.example.todolist.presentation.ui.addToDo.AddToDoFragment
import com.example.todolist.presentation.ui.api.OnCheckedClickListener
import com.example.todolist.presentation.ui.api.OnItemClickListener
import com.example.todolist.utils.BindingFragment
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * ListOfToDoFragment - UI класс одного из фрагментов, который отвечает за отображение списка задач.
 */
class ListOfToDoFragment : BindingFragment<FragmentListOfToDoBinding>(), OnItemClickListener,
    OnCheckedClickListener {

    @Inject
    lateinit var vmFactory: ListOfTodoViewModelFactory

    @Inject
    lateinit var renderClass: RenderClass
    private val viewModel: ListOfTodoViewModel by viewModels { vmFactory }
    private var adapter: ListToDoAdapter =
        ListToDoAdapter(this@ListOfToDoFragment, this@ListOfToDoFragment)

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListOfToDoBinding {
        return FragmentListOfToDoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as App).appComponent.fragmentComponentFactory().create().inject(this)
        adaptersInit()
        viewModel.loadTodoList()
        swipeToRefresh()
        viewModel.getStateLiveData.observe(viewLifecycleOwner) { result ->
            if (!result.internet) {
                showSnackBar()
                binding.swipeRefreshLayout.isEnabled = false
            } else {
                viewModel.updateDataServer()
            }
            eyeImageVisibility(result.doneVisibility)
        }
        viewModel.filteredTodoInfo.observe(viewLifecycleOwner) { list ->
            renderClass.renderList(list, binding)
            adapter.submitList(list.second)
            sumOfDoneTodos(list.second)
        }
        binding.ivEyeVisibility.setOnClickListener {
            viewModel.changeVisibility()
        }
    }

    private fun swipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.resetSyncFlag()
            viewModel.syncTodoListFromNetwork()
        }
    }

    private fun adaptersInit() = with(binding) {
        rcViewToDoList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ListToDoAdapter(this@ListOfToDoFragment, this@ListOfToDoFragment)
        rcViewToDoList.adapter = adapter
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, getString(R.string.data_not_sync), Snackbar.LENGTH_SHORT).show()
    }

    private fun sumOfDoneTodos(list: List<TodoItem>) {
        val doneCount = list.count { it.done }
        if (doneCount == 0) binding.tvSumOfDone.text = ""
        else binding.tvSumOfDone.text = getString(R.string.done_count, doneCount)
    }

    private fun eyeImageVisibility(visibility: Boolean) {
        if (visibility) {
            binding.ivEyeVisibility.setImageResource(R.drawable.ic_eye_visibility_gone)
        } else {
            binding.ivEyeVisibility.setImageResource(R.drawable.ic_eye_visibility)
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

    override fun onStop() {
        super.onStop()
        viewModel.updateDataServer()
    }
}