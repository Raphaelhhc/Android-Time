package com.example.timeapp.di

import android.content.Context
import androidx.room.Room
import com.example.timeapp.data.alarm.AlarmDao
import com.example.timeapp.data.alarm.AlarmDataBase
import com.example.timeapp.data.world_clock.WorldClockDao
import com.example.timeapp.data.world_clock.WorldClockDatabase
import com.example.timeapp.domain.WorldClockRepository
import com.example.timeapp.presentation.alarm.notification.AlarmScheduler
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
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AlarmScheduler(context)
    }

    @Provides
    @Singleton
    fun provideWorldClockDatabase(@ApplicationContext context: Context): WorldClockDatabase {
        return Room.databaseBuilder(
            context,
            WorldClockDatabase::class.java,
            "world_clock.db"
        ).build()
    }

    @Provides
    fun provideWorldClockDao(db: WorldClockDatabase): WorldClockDao {
        return db.worldClockDao()
    }

    @Provides
    @Singleton
    fun provideWorldClockRepository(
        @ApplicationContext context: Context,
        dao: WorldClockDao
    ): WorldClockRepository {
        return WorldClockRepository(context, dao)
    }

    @Provides
    @Singleton
    fun provideAlarmDatabase(@ApplicationContext context: Context): AlarmDataBase {
        return Room.databaseBuilder(
            context,
            AlarmDataBase::class.java,
            "alarm.db"
        ).build()
    }

    @Provides
    fun provideAlarmDao(db: AlarmDataBase): AlarmDao {
        return db.alarmDao()
    }

}