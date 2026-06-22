package com.example.bloodlink.di

import android.content.Context
import androidx.room.Room
import com.example.bloodlink.data.local.BloodDatabase
import com.example.bloodlink.data.local.UserDao
import com.example.bloodlink.data.repository.FirebaseAuthRepository
import com.example.bloodlink.data.repository.FirebaseBloodRepository
import com.example.bloodlink.domain.repository.AuthRepository
import com.example.bloodlink.domain.repository.BloodRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- LOCAL DATABASE (ROOM) ---
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
    fun provideUserDao(db: BloodDatabase): UserDao = db.userDao()

    // --- FIREBASE INSTANCES ---
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    // --- REPOSITORY BINDING ---
    @Provides
    @Singleton
    fun provideBloodRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): BloodRepository {
        // We have completely swapped FakeBloodRepository for FirebaseBloodRepository!
        return FirebaseBloodRepository(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository {
        return FirebaseAuthRepository(auth, firestore)
    }
}