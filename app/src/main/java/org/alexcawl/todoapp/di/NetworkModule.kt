package org.alexcawl.todoapp.di

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.alexcawl.todoapp.data.network.api.TaskApi
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.presentation.util.PreferencesCommitter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideApiUrl(): String = "https://beta.mrdekk.ru/todobackend/"

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideNetworkClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        Log.d("NETWORK", chain.request().toString())
        val newRequest: Request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer debamboozle")
            .build()
        chain.proceed(newRequest)
    }.addInterceptor(HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }).build()

    @Provides
    @Singleton
    fun provideRetrofit(
        url: String,
        factory: GsonConverterFactory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(factory)
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)

    @Provides
    @Singleton
    fun provideNetworkSource(committer: PreferencesCommitter, api: TaskApi) = NetworkSource(committer, api)
}