package com.example.todolist.presentation.ui.add_to_do

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.presenters.addToDoViewModel.AddTodoViewModel
import com.example.todolist.presentation.presenters.addToDoViewModel.AddTodoViewModelFactory
import com.example.todolist.presentation.util.ShowDatePickerDialog
import java.time.Instant
import java.util.Calendar

class AddToDoFragment : Fragment() {

    private lateinit var binding: FragmentAddToDoBinding
    private lateinit var viewModel: AddTodoViewModel
    private var currentId = NOTHING_STRING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddToDoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, AddTodoViewModelFactory())[AddTodoViewModel::class.java]
        openPreviousSavedTodo()
        putCalendarDate()
        saveDataTodo()
        deleteDataTodo()
        closeFragment()

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
                        IMPORTANCE_LOW -> binding.radioButtonLow.isChecked = true
                        IMPORTANCE_BASIC -> binding.radioButtonNone.isChecked = true
                        IMPORTANCE_HIGH -> binding.radioButtonHigh.isChecked = true
                    }
                }
                todoItem.deadline?.let { binding.tvDate.text = it }
            }
        }
    }

    private fun currentTodo(): TodoItem {
        val importance = when (binding.radioGroup.checkedRadioButtonId) {
            binding.radioButtonLow.id -> IMPORTANCE_LOW
            binding.radioButtonNone.id -> IMPORTANCE_BASIC
            binding.radioButtonHigh.id -> IMPORTANCE_HIGH
            else -> NOTHING_STRING
        }
        val currentTime = Instant.now().epochSecond
        return TodoItem(
            id = currentId.ifEmpty { currentTime.toString() },
            text = binding.editTextInputText.text.toString(),
            importance = importance,
            deadline = binding.tvDate.text.toString(),
            done = false,
            creationDate = Calendar.DATE.toString(),
            modificationDate = currentTime.toString()
        )
    }

    private fun saveDataTodo() {
        binding.tvSave.setOnClickListener {
            val result = currentTodo()
            when {
                result.text.isNotEmpty() -> {
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
            val result = currentTodo()
            viewModel.deleteTodoItem(result)
            findNavController().navigateUp()
        }
    }


    private fun putCalendarDate() {
        binding.switchCalendar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) ShowDatePickerDialog(binding).showDialog()
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
        const val IMPORTANCE_LOW = "low"
        const val IMPORTANCE_BASIC = "basic"
        const val IMPORTANCE_HIGH = "high"

        fun createArgs(textId: TodoItem): Bundle =
            bundleOf(ID_TEXT to textId)
    }
}