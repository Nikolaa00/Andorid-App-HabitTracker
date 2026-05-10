package com.example.habittrackerapp.di

import android.content.Context
import androidx.room.Room
import com.example.habittrackerapp.data.local.HabitDatabase
import com.example.habittrackerapp.data.local.dao.HabitDao
import com.example.habittrackerapp.data.local.dao.UserDao
import com.example.habittrackerapp.data.repository.AuthRepositoryImpl
import com.example.habittrackerapp.data.repository.HabitRepositoryImpl
import com.example.habittrackerapp.data.repository.UserRepositoryImpl
import com.example.habittrackerapp.domain.repository.AuthRepository
import com.example.habittrackerapp.domain.repository.HabitRepository
import com.example.habittrackerapp.domain.repository.UserRepository
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

    @Provides
    @Singleton
    fun provideHabitDatabase(@ApplicationContext context: Context): HabitDatabase {
        return Room.databaseBuilder(
            context,
            HabitDatabase::class.java,
            HabitDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(db: HabitDatabase): HabitDao = db.habitDao

    @Provides
    @Singleton
    fun provideUserDao(db: HabitDatabase): UserDao = db.userDao

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideHabitRepository(impl: HabitRepositoryImpl): HabitRepository = impl

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl
}
