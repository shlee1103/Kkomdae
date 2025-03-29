package com.pizza.kkomdae.di

import com.pizza.kkomdae.data.repository.LoginRepositoryImpl
import com.pizza.kkomdae.data.repository.UserInfoRepositoryImpl
import com.pizza.kkomdae.domain.repository.LoginRepository
import com.pizza.kkomdae.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserInfoRepositoryImpl
    ): UserRepository
}