package com.team.ian.core.di

import com.team.ian.data.repo.AlumniRepo
import com.team.ian.data.repo.AlumniRepoRealImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
	@Binds
	@Singleton
	abstract fun bindAlumniRepo(impl: AlumniRepoRealImpl): AlumniRepo
}
