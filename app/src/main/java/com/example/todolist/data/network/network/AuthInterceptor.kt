package com.example.todolist.data.network.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Класс, который добавляет в каждый запрос к серверу токен авторизации.
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", ID_TOKEN)
            .build()

        return chain.proceed(request)
    }

    companion object{
        const val ID_TOKEN = "Bearer conveyable"
    }
}