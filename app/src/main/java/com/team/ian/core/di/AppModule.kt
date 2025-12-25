package com.team.ian.core.di

import com.team.ian.data.repo.AlumniRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
	@Provides
	@Singleton
	fun provideAlumniRepo(): AlumniRepo{
		return AlumniRepo.getInstance()
	}
}