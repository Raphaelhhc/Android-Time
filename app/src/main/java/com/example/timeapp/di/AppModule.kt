package com.example.timeapp.di

import android.content.Context
import com.example.timeapp.domain.WorldClockRepository
import com.example.timeapp.presentation.timer.notification.TimerAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideTimerAlarmScheduler(@ApplicationContext context: Context): TimerAlarmScheduler {
        return TimerAlarmScheduler(context)
    }

    @Provides
    @Singleton
    fun provideWorldClockRepository(@ApplicationContext context: Context): WorldClockRepository {
        return WorldClockRepository(context)
    }

}