package com.example.todolist.presentation.util

import android.app.DatePickerDialog
import com.example.todolist.databinding.FragmentAddToDoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ShowDatePickerDialog(private val binding: FragmentAddToDoBinding) {

    fun showDialog() = showDatePickerDialog()

    private fun showDatePickerDialog() {
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
            }, year, month, day
        )

        datePickerDialog.setOnCancelListener {
            if (binding.tvDate.text.toString().isEmpty()) {
                binding.switchCalendar.isChecked = false
            }
        }

        datePickerDialog.show()
    }

}