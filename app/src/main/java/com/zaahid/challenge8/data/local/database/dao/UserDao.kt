package com.zaahid.challenge8.data.local.database.dao

import androidx.room.*
import com.zaahid.challenge8.data.local.database.entity.UserEntity


@Dao
interface UserDao {
    @Query("SELECT * FROM USER WHERE USERNAME == :username LIMIT 1")
    suspend fun getUserByusername(username : String) : UserEntity

    @Update
    suspend fun updateUser(user: UserEntity): Int

    @Insert
    suspend fun insertUser(user : UserEntity) : Long
}