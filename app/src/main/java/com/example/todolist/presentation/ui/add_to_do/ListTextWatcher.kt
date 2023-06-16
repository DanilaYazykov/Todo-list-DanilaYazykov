package com.example.todolist.presentation.ui.add_to_do

import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddToDoBinding

class ListTextWatcher(private val binding: FragmentAddToDoBinding, private val addTodoFragment: AddToDoFragment) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s.toString().isBlank()) {
            binding.apply {
                val drawable = tvDeleteToDo.compoundDrawablesRelative[0].mutate().constantState?.newDrawable()
                DrawableCompat.setTint(drawable!!, ContextCompat.getColor(binding.root.context, R.color.grey))
                tvDeleteToDo.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
                tvDeleteToDo.setOnClickListener(null)
                tvDeleteToDo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
            }
        } else {
            binding.apply {
                val drawable = tvDeleteToDo.compoundDrawablesRelative[0].mutate().constantState?.newDrawable()
                DrawableCompat.setTint(drawable!!, ContextCompat.getColor(binding.root.context, R.color.red))
                tvDeleteToDo.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
                tvDeleteToDo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                if (s.toString().isNotEmpty()) {
                    tvDeleteToDo.setOnClickListener {
                        editTextInputText.text.clear()
                        tvDate.text = ""
                        switchCalendar.isChecked = false
                        radioGroup.check(R.id.radio_button_none)
                        addTodoFragment.deleteDataTodo()
                    }
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) = Unit
}