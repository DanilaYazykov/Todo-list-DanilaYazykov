package com.example.todolist.presentation.ui.addToDo

import android.view.View
import com.example.todolist.databinding.FragmentAddToDoBinding
import com.example.todolist.di.FragmentScope
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * VisibilityAddTodo - класс, который отвечает за отображение элементов во фрагменте AddToDoFragment.
 */
@FragmentScope
class VisibilityAddTodo (private val binding: FragmentAddToDoBinding) {

    private fun showViews(vararg views: View) {
        binding.tvImportanceBasic.visibility = View.GONE
        binding.tvImportanceLow.visibility = View.GONE
        binding.tvImportanceHigh.visibility = View.GONE
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }

    fun setVisibility(s: Int?) {
        when (s) {
            SHOW_IMPORTANCE_BASIC -> {
                showViews(binding.tvImportanceBasic)
                BottomSheetBehavior.from(binding.standardBottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
            }
            SHOW_IMPORTANCE_LOW -> {
                showViews(binding.tvImportanceLow)
                binding.switchImportance.isChecked = true
                BottomSheetBehavior.from(binding.standardBottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
            }

            SHOW_IMPORTANCE_HIGH -> {
                showViews(binding.tvImportanceHigh)
                binding.switchImportance.isChecked = true
                BottomSheetBehavior.from(binding.standardBottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
            }

            SHOW_NOTHING -> showViews()
            else -> Unit
        }
    }

    companion object {
        const val SHOW_IMPORTANCE_BASIC = 1
        const val SHOW_IMPORTANCE_LOW = 2
        const val SHOW_IMPORTANCE_HIGH = 3
        const val SHOW_NOTHING = 0
    }
}