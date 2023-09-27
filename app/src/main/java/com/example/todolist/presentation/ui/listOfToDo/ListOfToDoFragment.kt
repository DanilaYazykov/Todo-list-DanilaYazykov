package com.example.todolist.presentation.ui.listOfToDo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.app.App
import com.example.todolist.databinding.FragmentListOfToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.ListOfTodoViewModel
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.ListOfTodoViewModelFactory
import com.example.todolist.presentation.ui.addToDo.AddToDoFragment
import com.example.todolist.presentation.ui.listOfToDo.api.OnCheckedClickListener
import com.example.todolist.presentation.ui.listOfToDo.api.OnItemClickListener
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.AddDoneEvent
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.ChangeVisibilityEvent
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.CheckNetworkEvent
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.ListOfTodoScreenState
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.LoadTodoListEvent
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.ResetSyncFlagEvent
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.SyncTodoListFromNetworkEvent
import com.example.todolist.presentation.viewModels.listOfToDoViewModel.UpdateDataServerEvent
import com.example.todolist.utils.BindingFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ListOfToDoFragment - UI класс одного из фрагментов, который отвечает за отображение списка задач */
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
        viewModel.action(LoadTodoListEvent())
        swipeToRefresh()
        observeViewModel()
        binding.ivEyeVisibility.setOnClickListener {
            viewModel.action(ChangeVisibilityEvent())
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.uiState.collect { screenState ->
                when (screenState) {
                    is ListOfTodoScreenState.TodoInfo -> Unit
                    is ListOfTodoScreenState.FilteredTodoInfo ->
                        renderClass.showFilteredInfo(screenState.filteredTodoInfo, binding, adapter)
                    is ListOfTodoScreenState.DoneVisibility ->
                        renderClass.showDoneStatus(screenState.doneStatus, binding)
                    is ListOfTodoScreenState.InternetVisibility ->
                        renderClass.showInternetStatus(screenState.internetStatus, binding, viewModel)
                }
            }
        }
    }

    private fun swipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.action(CheckNetworkEvent())
            viewModel.action(ResetSyncFlagEvent())
            viewModel.action(SyncTodoListFromNetworkEvent())
        }
    }

    private fun adaptersInit() = with(binding) {
        rcViewToDoList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ListToDoAdapter(this@ListOfToDoFragment, this@ListOfToDoFragment)
        rcViewToDoList.adapter = adapter
    }

    override fun onItemClick(todo: TodoItem) {
        findNavController().navigate(
            R.id.action_listOfToDoFragment_to_addToDoFragment,
            AddToDoFragment.createArgs(todo)
        )
    }

    override fun onCheckedChange(todoItem: String, isChecked: Boolean) {
        viewModel.action(AddDoneEvent(todoItem, isChecked))
    }

    override fun onStop() {
        super.onStop()
        viewModel.action(UpdateDataServerEvent())
    }
}