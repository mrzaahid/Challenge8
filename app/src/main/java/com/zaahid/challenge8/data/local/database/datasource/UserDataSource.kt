package com.zaahid.challenge8.data.local.database.datasource

import com.zaahid.challenge8.data.local.database.dao.UserDao
import com.zaahid.challenge8.data.local.database.entity.UserEntity

interface UserDataSource {
    suspend fun getUserByusername(username: String): UserEntity?

    suspend fun insertUser(user: UserEntity): Long
    suspend fun updateUser(user : UserEntity):Int
}

class UserDataSourceImpl(private val userDao: UserDao) : UserDataSource {

    override suspend fun getUserByusername(username: String): UserEntity {
        return userDao.getUserByusername(username)
    }

    override suspend fun insertUser(user: UserEntity): Long {
        return userDao.insertUser(user)
    }

    override suspend fun updateUser(user: UserEntity):Int {
        return userDao.updateUser(user)
    }

}