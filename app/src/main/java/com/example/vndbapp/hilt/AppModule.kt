package com.example.vndbapp.hilt

import com.example.vndbapp.datalayer.api.repository.VisualNovelRepository
import com.example.vndbapp.datalayer.api.repository.VisualNovelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindVisualNovelRepository(
        visualNovelRepositoryImpl: VisualNovelRepositoryImpl
    ): VisualNovelRepository
}