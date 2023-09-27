package com.example.todolist.presentation.ui.addToDo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.app.App
import com.example.todolist.databinding.FragmentAddToDoBinding
import com.example.todolist.domain.models.TodoItem
import com.example.todolist.presentation.viewModels.addToDoViewModel.AddTodoItemEvent
import com.example.todolist.presentation.viewModels.addToDoViewModel.AddTodoViewModel
import com.example.todolist.presentation.viewModels.addToDoViewModel.AddTodoViewModelFactory
import com.example.todolist.presentation.viewModels.addToDoViewModel.DeleteTodoItemEvent
import com.example.todolist.presentation.viewModels.addToDoViewModel.MarkAsNotSyncedEvent
import com.example.todolist.utils.AlarmReceiver
import com.example.todolist.utils.BindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * AddToDoFragment - UI класс, который отвечает за добавление нового элемента в список.
 */
class AddToDoFragment : BindingFragment<FragmentAddToDoBinding>() {

    @Inject
    lateinit var vmFactory: AddTodoViewModelFactory
    @Inject
    lateinit var calendar: Calendar
    private val setVisibility by lazy { VisibilityAddTodo(binding) }
    private var currentId = EMPTY_STRING
    private val viewModel: AddTodoViewModel by viewModels { vmFactory }
    private var importance = TodoItem.Importance.BASIC

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddToDoBinding {
        return FragmentAddToDoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as App).appComponent.fragmentComponentFactory().create().inject(this)
        openPreviousSavedTodo()
        putCalendarDate()
        deleteDataTodo()
        closeFragment()
        saveDataTodo()
        bottomSheetManager()
        getImportance()
        binding.editTextInputText.addTextChangedListener(ListTextWatcher(binding, this))
    }

    private fun openPreviousSavedTodo() {
        val todoItem = getParcelable()
        if (todoItem != null) {
            currentId = todoItem.id
            todoItem.text.let {
                binding.editTextInputText.setText(it)
            }
            ListTextWatcher(binding, this).onTextChanged(todoItem.text, 0, 0, todoItem.text.length)
            todoItem.importance.let {
                setVisibility.setVisibility(VisibilityAddTodo.SHOW_NOTHING)
                when (it) {
                    TodoItem.Importance.LOW -> setVisibility.setVisibility(VisibilityAddTodo.SHOW_IMPORTANCE_LOW)
                    TodoItem.Importance.BASIC -> setVisibility.setVisibility(VisibilityAddTodo.SHOW_IMPORTANCE_BASIC)
                    TodoItem.Importance.IMPORTANT -> setVisibility.setVisibility(VisibilityAddTodo.SHOW_IMPORTANCE_HIGH)
                }
            }
            todoItem.deadline?.let {
                binding.tvDate.text =
                    SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault()).format(Date(it))
                binding.switchCalendar.isChecked = true
            }
            binding.tvDeleteToDo.setText(R.string.text_delete)
        }
    }

    private fun getParcelable(): TodoItem? {
        val args = arguments
        var todoItem: TodoItem? = null
        if (args != null) {
            todoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                args.getParcelable(ID_TEXT, TodoItem::class.java)
            } else {
                @Suppress("DEPRECATION")
                args.getParcelable(ID_TEXT)
            }
        }
        return todoItem
    }

    private fun currentTodo(): TodoItem {
        val importance = importance
        val currentTime = Instant.now().epochSecond
        val dateString = binding.tvDate.text.toString()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
        val deadline = if (dateString.isNotEmpty()) {
            val date = dateFormat.parse(dateString)
            date?.time
        } else {
            null
        }
        val modificationDate = calendar.timeInMillis
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

    private fun getImportance() = with(binding) {
        binding.bottomLowImportance.setOnClickListener {
            setVisibility.setVisibility(VisibilityAddTodo.SHOW_IMPORTANCE_LOW)
            importance = TodoItem.Importance.LOW
        }
        binding.bottomBasicImportance.setOnClickListener {
            setVisibility.setVisibility(VisibilityAddTodo.SHOW_IMPORTANCE_BASIC)
            importance = TodoItem.Importance.BASIC
        }
        binding.bottomHighImportance.setOnClickListener {
            setVisibility.setVisibility(VisibilityAddTodo.SHOW_IMPORTANCE_HIGH)
            tvImportanceHigh.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.highlight_animation))
            importance = TodoItem.Importance.IMPORTANT
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(binding.editTextInputText.windowToken, 0)
    }

    private fun bottomSheetManager() = with(binding) {
        val bottomSheetBehavior = BottomSheetBehavior.from(standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            dimView.visibility = View.GONE
        }

        switchImportance.setOnClickListener {
            if(switchImportance.isChecked) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                hideKeyboard()
                dimView.visibility = View.VISIBLE
            } else setVisibility.setVisibility(VisibilityAddTodo.SHOW_NOTHING)
        }

        dimView.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            switchImportance.isChecked = false
            dimView.visibility = View.GONE
        }
        bottomSheetCallBack(bottomSheetBehavior)
    }

    private fun bottomSheetCallBack(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>) {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> Unit
                    BottomSheetBehavior.STATE_COLLAPSED -> Unit
                    BottomSheetBehavior.STATE_HIDDEN -> binding.dimView.visibility = View.GONE
                    else -> Unit
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
    }

    private fun sendNotification(result: TodoItem) {
        if (result.deadline == null) return
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java).let { intent ->
            intent.putExtra(AlarmReceiver.TITLE, result.text)
            intent.putExtra(AlarmReceiver.IMPORTANCE, result.importance.toString())
            PendingIntent.getBroadcast(requireContext(), result.id.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)
        }
        val calendar = Calendar.getInstance().apply {
            timeInMillis = result.deadline
        }

        alarmManager?.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }

    private fun deleteNotification(result: TodoItem) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val alarmIntent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent
            .getBroadcast(requireContext(), result.id.toInt(), alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent?.let {
            alarmManager?.cancel(it)
            it.cancel()
        }
    }

    private fun saveDataTodo() {
        binding.tvSave.setOnClickListener {
            val result = currentTodo()
            when {
                result.text.isNotEmpty() -> {
                    sendNotification(result)
                    viewModel.action(MarkAsNotSyncedEvent(result.id))
                    viewModel.action(AddTodoItemEvent(result))
                    findNavController().navigateUp()
                }
                else -> {
                    viewModel.action(DeleteTodoItemEvent(result))
                    findNavController().navigateUp()
                }
            }
        }
    }

    fun deleteDataTodo() {
        binding.tvDeleteToDo.setOnClickListener {
            deleteNotification(currentTodo())
            binding.ivTodoAnim.visibility = View.VISIBLE
            viewLifecycleOwner.lifecycleScope.launch {
                delay(ANIMATION_PLAYING)
                viewModel.action(DeleteTodoItemEvent(currentTodo()))
                findNavController().navigateUp()
            }
        }
    }

    private fun putCalendarDate() {
        binding.switchCalendar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) DateCalendar(binding).showDialog()
            else binding.tvDate.text = EMPTY_STRING
        }
    }

    private fun closeFragment() {
        binding.ivCloseButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        private const val ID_TEXT = "id"
        private const val EMPTY_STRING = ""
        private const val ANIMATION_PLAYING = 1500L

        fun createArgs(textId: TodoItem): Bundle =
            bundleOf(ID_TEXT to textId)
    }
}