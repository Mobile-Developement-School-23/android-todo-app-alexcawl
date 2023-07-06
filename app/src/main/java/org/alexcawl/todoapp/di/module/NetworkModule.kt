package org.alexcawl.todoapp.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.alexcawl.todoapp.data.network.api.TaskApi
import org.alexcawl.todoapp.di.qualifiers.ApiUrlPath
import org.alexcawl.todoapp.di.qualifiers.AuthToken
import org.alexcawl.todoapp.di.scope.MainActivityScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface NetworkModule {
    companion object {
        @Provides
        @ApiUrlPath
        @MainActivityScope
        fun provideApiUrl(): String = "https://beta.mrdekk.ru/todobackend/"

        @Provides
        @AuthToken
        @MainActivityScope
        fun provideAuthToken(): String = "debamboozle"

        @Provides
        @MainActivityScope
        fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

        @Provides
        @MainActivityScope
        fun provideNetworkClient(
            @AuthToken token: String
        ): OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest: Request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        }.addInterceptor(HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }).build()

        @Provides
        @MainActivityScope
        fun provideRetrofit(
            @ApiUrlPath url: String,
            factory: GsonConverterFactory,
            client: OkHttpClient
        ): Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(factory)
            .client(client)
            .build()

        @Provides
        @MainActivityScope
        fun provideApi(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)
    }
}