package org.alexcawl.todoapp.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import org.alexcawl.todoapp.data.network.api.TaskApi
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
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
        val newRequest: Request = chain!!.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer debamboozle")
            .build()
        chain.proceed(newRequest)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(
        url: String,
        factory: GsonConverterFactory,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(url)
        .addConverterFactory(factory)
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)

    @Provides
    @Singleton
    fun provideNetworkSource(api: TaskApi) = NetworkSource(api)
}