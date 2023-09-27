package com.example.todolist.domain.models

data class DoneState(
    val doneVisibility: Boolean
) {
    companion object {
        fun default() = DoneState(
            doneVisibility = false
        )
    }
}
