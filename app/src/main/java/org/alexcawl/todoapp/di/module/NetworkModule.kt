package org.alexcawl.todoapp.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.alexcawl.todoapp.data.network.api.TaskApi
import org.alexcawl.todoapp.data.network.datasource.NetworkSource
import org.alexcawl.todoapp.di.qualifiers.ApiUrlPath
import org.alexcawl.todoapp.di.qualifiers.AuthToken
import org.alexcawl.todoapp.di.scope.ApplicationScope
import org.alexcawl.todoapp.domain.source.INetworkSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface NetworkModule {
    companion object {
        @Provides
        @ApiUrlPath
        @ApplicationScope
        fun provideApiUrl(): String = "https://beta.mrdekk.ru/todobackend/"

        @Provides
        @AuthToken
        @ApplicationScope
        fun provideAuthToken(): String = "debamboozle"

        @Provides
        @ApplicationScope
        fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

        @Provides
        @ApplicationScope
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
        @ApplicationScope
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
        @ApplicationScope
        fun provideApi(retrofit: Retrofit): TaskApi = retrofit.create(TaskApi::class.java)
    }

    @Binds
    @ApplicationScope
    fun bindNetworkSource(source: NetworkSource): INetworkSource
}