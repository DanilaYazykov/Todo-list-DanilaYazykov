package com.example.todolist.data.network.network

enum class NetworkResult {
    SUCCESS_200,
    ID_TODO_NOT_FOUND_404,
    UNCORRECT_AUTHORIZATION_401,
    ERROR_UNSYNCHRONIZED_DATA_400,
    ERROR_SERVER_500,
    UNKNOWN_ERROR
}