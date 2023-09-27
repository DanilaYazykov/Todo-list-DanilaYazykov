package com.example.todolist.domain.models

data class InternetState(
    val internet: Boolean
) {
    companion object {
        fun default() = InternetState(
            internet = true
        )
    }
}
