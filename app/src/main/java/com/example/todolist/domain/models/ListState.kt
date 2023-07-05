package com.example.todolist.domain.models

data class ListState (
    val doneVisibility: Boolean,
    val internet: Boolean
) {
    companion object {
        fun default() = ListState(
            doneVisibility = false,
            internet = true
        )
    }
}
