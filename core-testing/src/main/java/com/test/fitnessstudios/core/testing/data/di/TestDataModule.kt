package com.test.fitnessstudios.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    /*@Singleton
    @Binds
    fun bindsMapsRepo(
        MapsRepository: com.test.fitnessstudios.core.testing.data.repository.FakeDrivingPtsRepository
    ): DrivingPtsRepository*/
}
