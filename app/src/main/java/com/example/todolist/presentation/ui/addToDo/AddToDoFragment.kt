package com.example.todolist.presentation.ui.addToDo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentAddToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.presenters.addToDoViewModel.AddTodoViewModel
import com.example.todolist.presentation.presenters.addToDoViewModel.AddTodoViewModelFactory
import com.example.todolist.presentation.ui.util.BindingFragment
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddToDoFragment : BindingFragment<FragmentAddToDoBinding>() {

    private lateinit var viewModel: AddTodoViewModel
    private var currentId = NOTHING_STRING

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddToDoBinding {
        return FragmentAddToDoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, AddTodoViewModelFactory())[AddTodoViewModel::class.java]
        openPreviousSavedTodo()
        putCalendarDate()
        deleteDataTodo()
        closeFragment()
        saveDataTodo()

        binding.editTextInputText.addTextChangedListener(ListTextWatcher(binding, this))
    }

    private fun openPreviousSavedTodo() {
        val args = arguments
        if (args != null) {
            val todoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                args.getParcelable(ID_TEXT, TodoItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                args.getParcelable(ID_TEXT)
            }
            if (todoItem != null) {
                currentId = todoItem.id
                todoItem.text.let {
                    binding.editTextInputText.setText(it)
                }
                ListTextWatcher(binding, this).onTextChanged(todoItem.text, 0, 0, todoItem.text.length)
                todoItem.importance.let {
                    when (it) {
                        TodoItem.Importance.LOW -> binding.radioButtonLow.isChecked = true
                        TodoItem.Importance.BASIC -> binding.radioButtonNone.isChecked = true
                        TodoItem.Importance.IMPORTANT -> binding.radioButtonHigh.isChecked = true
                    }
                }
                todoItem.deadline?.let {
                    binding.tvDate.text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(it))
                }
            }
        }
    }

    private fun currentTodo(): TodoItem {
        val importance = when (binding.radioGroup.checkedRadioButtonId) {
            binding.radioButtonLow.id -> TodoItem.Importance.LOW
            binding.radioButtonNone.id -> TodoItem.Importance.BASIC
            binding.radioButtonHigh.id -> TodoItem.Importance.IMPORTANT
            else -> TodoItem.Importance.BASIC
        }
        val currentTime = Instant.now().epochSecond
        val dateString = binding.tvDate.text.toString()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val deadline = if (dateString.isNotEmpty()) {
            val date = dateFormat.parse(dateString)
            date?.time
        } else {
            null
        }
        val modificationDate = Calendar.getInstance().timeInMillis
        return TodoItem(
            id = currentId.ifEmpty { currentTime.toString() },
            text = binding.editTextInputText.text.toString(),
            importance = importance,
            deadline = deadline,
            done = false,
            creationDate = currentTime,
            modificationDate = modificationDate,
            lastUpdatedBy = Build.MODEL
        )
    }

    private fun saveDataTodo() {
        binding.tvSave.setOnClickListener {
            val result = currentTodo()
            when {
                result.text.isNotEmpty() -> {
                    viewModel.markAsNotSynced(result.id)
                    viewModel.addTodoItem(result)
                    findNavController().navigateUp()
                }
                else -> {
                    viewModel.deleteTodoItem(result)
                    findNavController().navigateUp()
                }
            }
        }
    }

    fun deleteDataTodo() {
        binding.tvDeleteToDo.setOnClickListener {
            viewModel.deleteTodoItem(currentTodo())
            findNavController().navigateUp()
        }
    }

    private fun putCalendarDate() {
        binding.switchCalendar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) DateCalendar(binding).showDialog()
            else binding.tvDate.text = NOTHING_STRING
        }
    }

    private fun closeFragment() {
        binding.ivCloseButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        private const val ID_TEXT = "id"
        private const val NOTHING_STRING = ""

        fun createArgs(textId: TodoItem): Bundle =
            bundleOf(ID_TEXT to textId)
    }
}