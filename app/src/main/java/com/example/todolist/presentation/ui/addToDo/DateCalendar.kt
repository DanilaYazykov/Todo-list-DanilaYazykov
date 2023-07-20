package com.example.todolist.presentation.ui.addToDo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import com.example.todolist.databinding.FragmentAddToDoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * DateCalendar - класс, который отвечает за отображение календаря.
 */
class DateCalendar(private val binding: FragmentAddToDoBinding) {

    fun showDialog() {
        showDateCalendar()
    }

    private fun showDateCalendar() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            binding.root.context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDateCalendar.time)
                binding.tvDate.text = formattedDate
                showTimePicker(formattedDate)
            }, year, month, day
        )
        datePickerDialog.setOnCancelListener {
            if (binding.tvDate.text.toString().isEmpty()) {
                binding.switchCalendar.isChecked = false
            }
        }
        datePickerDialog.show()
    }

    private fun showTimePicker(formattedDate: String) {
        val timePicker = TimePickerDialog(
            binding.root.context,
            { _, hourOfDay, minute ->
                calendar(hourOfDay, minute, formattedDate)
            }, 0, 0, true
        )

        timePicker.setOnCancelListener {
            if (binding.tvDate.text.toString().isNotEmpty()) {
                calendar(0, 0, formattedDate)
            }
        }
        timePicker.show()
    }

    private fun calendar(hours: Int, minutes: Int, formattedDate: String) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hours)
        calendar.set(Calendar.MINUTE, minutes)
        val time = calendar.time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(time)
        val result = "$formattedDate $formattedTime"
        binding.tvDate.text = result
    }
}