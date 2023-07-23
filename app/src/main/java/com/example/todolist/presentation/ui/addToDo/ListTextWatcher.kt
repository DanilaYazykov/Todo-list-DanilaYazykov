package com.example.todolist.presentation.ui.addToDo

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddToDoBinding

/**
 * ListTextWatcher - класс, который отвечает за обработку ввода текста.
 */
class ListTextWatcher(
    private val binding: FragmentAddToDoBinding,
    private val addTodoFragment: AddToDoFragment
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s.toString().isBlank()) {
            resetTextView()
        } else {
            updateTextView()
            setTextViewClickListener(s.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) = Unit

    private fun resetTextView() {
        binding.apply {
            tvDeleteToDo.setText(R.string.text_delete)
            val drawable = getDrawableWithColor(R.color.white)
            tvDeleteToDo.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
            tvDeleteToDo.setOnClickListener(null)
            tvDeleteToDo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }
    }

    private fun updateTextView() {
        binding.apply {
            val drawable = getDrawableWithColor(R.color.red)
            tvDeleteToDo.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
            tvDeleteToDo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
        }
    }

    private fun setTextViewClickListener(text: String) {
        binding.apply {
            if (text.isNotEmpty()) {
                tvDeleteToDo.setText(R.string.text_clean)
                tvDeleteToDo.setOnClickListener {
                    editTextInputText.text.clear()
                    tvDate.text = ""
                    switchCalendar.isChecked = false
                    switchImportance.isChecked = false
                    tvImportanceBasic.visibility = View.GONE
                    tvImportanceHigh.visibility = View.GONE
                    tvImportanceLow.visibility = View.GONE
                    addTodoFragment.deleteDataTodo()
                }
            }
        }
    }

    private fun getDrawableWithColor(colorResId: Int): Drawable? {
        val drawable =
            binding.tvDeleteToDo.compoundDrawablesRelative[0].mutate().constantState?.newDrawable()
        if (drawable != null) {
            DrawableCompat.setTint(
                drawable,
                ContextCompat.getColor(binding.root.context, colorResId)
            )
        }
        return drawable
    }

}