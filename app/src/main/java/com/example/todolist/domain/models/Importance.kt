package com.example.todolist.domain.models

enum class Importance {
    high, basic, low
}
    /**
     * Если сделать заглавными буквами и переопределить toString, то почему-то сервер не принимает это значение
     * несмотря на то, что запросы уходят идентичные.
     * Поэтому пока оставил как есть. Буду разбираться позже.
     * override fun toString(): String {
     * return super.toString().lowercase() } - не работает.
     */