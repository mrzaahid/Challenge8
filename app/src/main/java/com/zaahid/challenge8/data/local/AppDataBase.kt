package com.zaahid.challenge8.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zaahid.challenge8.data.local.database.dao.UserDao
import com.zaahid.challenge8.data.local.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao() : UserDao
}