package com.example.bloodlink.di

import android.content.Context
import androidx.room.Room
import com.example.bloodlink.data.local.BloodDatabase
import com.example.bloodlink.data.repository.FakeBloodRepository
import com.example.bloodlink.domain.repository.BloodRepository
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
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BloodDatabase {
        return Room.databaseBuilder(
            context,
            BloodDatabase::class.java,
            "bloodlink_db"
        ).build()
    }

    @Provides
    fun provideUserDao(db: BloodDatabase) = db.userDao()
    @Provides
    @Singleton
    fun provideBloodRepository(): BloodRepository {
        // We provide the Fake repository here.
        // When we build the real Firebase one later, we ONLY have to change this one single line!
        return FakeBloodRepository()
    }
}