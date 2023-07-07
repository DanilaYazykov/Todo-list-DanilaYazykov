package com.example.todolist.di

import android.content.Context
import android.content.SharedPreferences
import com.example.todolist.data.network.network.NetworkClientImpl
import com.example.todolist.data.network.network.TodoApi
import com.example.todolist.data.sharedPreferences.TodoLocalStorageImpl.Companion.TODO_PREFS
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(TODO_PREFS, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideApiService(
        @Named("network_request")
        interceptor: Interceptor,
        @Named("authorization_key")
        authInterceptor: Interceptor
    ): TodoApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://beta.mrdekk.ru/todobackend/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TodoApi::class.java)
    }

    @Named("network_request")
    @Provides
    fun provideLoggingInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            chain.proceed(request)
        }
    }

    @Named("authorization_key")
    @Provides
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", NetworkClientImpl.ID_TOKEN)
                .build()

            chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideGson() : Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideCalendar() : Calendar {
        return Calendar.getInstance()
    }

}