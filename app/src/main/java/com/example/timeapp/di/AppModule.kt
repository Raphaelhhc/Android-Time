package com.example.timeapp.di

import android.content.Context
import com.example.timeapp.presentation.timer.notification.TimerAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideTimerAlarmScheduler(@ApplicationContext context: Context): TimerAlarmScheduler {
        return TimerAlarmScheduler(context)
    }

}