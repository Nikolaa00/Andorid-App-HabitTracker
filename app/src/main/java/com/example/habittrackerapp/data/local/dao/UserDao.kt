package com.example.habittrackerapp.data.local.dao

import androidx.room.*
import com.example.habittrackerapp.data.local.entity.AppSettingsEntity
import com.example.habittrackerapp.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getUserById(uid: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity)

    @Query("SELECT * FROM app_settings WHERE userId = :userId")
    fun getSettingsByUserId(userId: String): Flow<AppSettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSettings(settings: AppSettingsEntity)

    @Query("DELETE FROM app_settings WHERE userId = :userId")
    suspend fun deleteUserSettings(userId: String)

    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun deleteUserRecord(uid: String)
}
