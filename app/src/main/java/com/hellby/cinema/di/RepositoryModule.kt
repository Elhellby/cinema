package com.hellby.cinema.di

import com.hellby.cinema.data.repository.CatalogRepositoryImpl
import com.hellby.cinema.data.repository.SettingsRepositoryImpl
import com.hellby.cinema.domain.repository.CatalogRepository
import com.hellby.cinema.domain.repository.SettingsRepository
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
    abstract fun bindCatalogRepository(impl: CatalogRepositoryImpl): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
