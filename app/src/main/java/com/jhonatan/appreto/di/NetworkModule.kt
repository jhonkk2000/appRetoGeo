package com.jhonatan.appreto.di

import android.content.Context
import com.jhonatan.appreto.R
import com.jhonatan.appreto.data.network.ApiGeo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiGeo(retrofit: Retrofit): ApiGeo {
        return retrofit.create(ApiGeo::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, @ApplicationContext context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.env))
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val client: OkHttpClient
        val builder = OkHttpClient().newBuilder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
        client = builder.build()
        return client
    }

}