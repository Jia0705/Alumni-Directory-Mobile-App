package com.team.ian.core.di

import com.team.ian.data.repo.AlumniRepo
import com.team.ian.data.api.NotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
	@Provides
	@Singleton
	fun provideAlumniRepo(): AlumniRepo{
		return AlumniRepo.getInstance()
	}

	// Retrofit & Notification Service
	@Provides
	@Singleton
	fun provideLoggingInterceptor(): HttpLoggingInterceptor {
		return HttpLoggingInterceptor().apply {
			level = HttpLoggingInterceptor.Level.BODY
		}
	}

	@Provides
	@Singleton
	fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
		return OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.build()
	}

	@Provides
	@Singleton
	fun provideNotificationService(okHttpClient: OkHttpClient): NotificationService {
		val baseUrl = "http://10.0.2.2:9999"
		
		return Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
			.create(NotificationService::class.java)
	}
}