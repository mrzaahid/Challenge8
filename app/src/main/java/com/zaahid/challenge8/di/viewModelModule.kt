package com.zaahid.challenge8.di

import com.zaahid.challenge8.data.local.database.datasource.UserDataSource
import com.zaahid.challenge8.data.local.preference.UserDataStoreDataSource
import com.zaahid.challenge8.data.network.datasource.MovieDataSource
import com.zaahid.challenge8.data.repository.LocalRepository
import com.zaahid.challenge8.data.repository.LocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object viewModelModule {
    @ViewModelScoped
    @Provides
    fun provideRepository(
        userDataStoreDataSource: UserDataStoreDataSource,
        userDataSource: UserDataSource,
        movieDataSource: MovieDataSource
    ): LocalRepository {
        return LocalRepositoryImpl(
            userDataStoreDataSource,
            userDataSource,
            movieDataSource
        )
    }
}