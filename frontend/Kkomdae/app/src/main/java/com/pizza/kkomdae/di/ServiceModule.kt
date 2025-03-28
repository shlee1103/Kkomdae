package com.pizza.kkomdae.di

import com.pizza.kkomdae.data.source.remote.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Singleton
    @Provides
    fun providesContentService(retrofit: Retrofit) : LoginService = retrofit.create(LoginService::class.java)
}