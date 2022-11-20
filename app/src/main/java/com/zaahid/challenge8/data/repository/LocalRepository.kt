package com.zaahid.challenge8.data.repository

import com.zaahid.challenge8.data.local.database.datasource.UserDataSource
import com.zaahid.challenge8.data.local.database.entity.UserEntity
import com.zaahid.challenge8.data.local.preference.UserDataStoreDataSource
import com.zaahid.challenge8.data.network.datasource.MovieDataSource
import com.zaahid.challenge8.model.MovieRespons
import com.zaahid.challenge8.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun setUserPref(id: Int,name: String,email:String,address : String)
    fun getUserName() :Flow<String>
    fun getUserId() : Flow<Int>
    fun getEmail():Flow<String>
    fun getAddress():Flow<String>
    suspend fun setLang(lang: String)
    fun getLang(): Flow<String>
    suspend fun setSwitch(it:Boolean)
    fun getSwitch():Flow<Boolean>
    suspend fun setImageString(value : String)
    fun getImageString():Flow<String>
    suspend fun getUserByUsername(username : String): Resource<UserEntity?>
    suspend fun insertUser(user : UserEntity): Resource<Number>
    suspend fun updateUser(user: UserEntity): Resource<Number>
    suspend fun searchMovie(query : String,lang: String,page: Int) : Resource<MovieRespons>
    suspend fun popularMovie(lang: String, page: Int): Resource<MovieRespons>
}

class LocalRepositoryImpl(
    private val userPreferenceDataStoreSource: UserDataStoreDataSource,
    private val userDataSource: UserDataSource,
    private val movieDataSource: MovieDataSource
) : LocalRepository {
    override suspend fun setUserPref(id: Int, name: String, email: String, address: String) {
        userPreferenceDataStoreSource.setUserPref(id,name,email,address)
    }

    override fun getUserName(): Flow<String> {
        return userPreferenceDataStoreSource.getUsername()
    }
    override fun getUserId(): Flow<Int> {
        return userPreferenceDataStoreSource.getUserId()
    }
    override fun getEmail(): Flow<String> {
        return userPreferenceDataStoreSource.getUserEmail()
    }

    override fun getAddress(): Flow<String> {
        return userPreferenceDataStoreSource.getUserAddres()
    }


    override fun getLang(): Flow<String> {
        return userPreferenceDataStoreSource.getUserLang()
    }

    override suspend fun setLang(lang: String) {
        userPreferenceDataStoreSource.setUserLang(lang)
    }


    override fun getSwitch(): Flow<Boolean> {
        return userPreferenceDataStoreSource.getSwitch()
    }

    override suspend fun setImageString(value: String) {
        userPreferenceDataStoreSource.setImageString(value)
    }

    override fun getImageString(): Flow<String> {
        return userPreferenceDataStoreSource.getImageString()
    }

    override suspend fun setSwitch(it: Boolean) {
        userPreferenceDataStoreSource.setSwitch(it)
    }

    override suspend fun getUserByUsername(username: String): Resource<UserEntity?> {
        return proceed {
            userDataSource.getUserByusername(username)
        }
    }

    override suspend fun insertUser(user: UserEntity): Resource<Number> {
        return proceed {
            userDataSource.insertUser(user)
        }
    }

    override suspend fun updateUser(user: UserEntity): Resource<Number> {
        return proceed {
            userDataSource.updateUser(user)
        }
    }

    override suspend fun searchMovie(
        query: String,
        lang: String,
        page: Int
    ): Resource<MovieRespons> {
        return proceed {
            movieDataSource.searchMovie(query,lang,page)
        }
    }

    override suspend fun popularMovie(
        lang: String,
        page: Int
    ): Resource<MovieRespons> {
        return proceed {
            movieDataSource.popularMovie(lang,page)
        }
    }

    private suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutine.invoke())
        } catch (exception: Exception) {
            Resource.Error(exception)
        }
    }

}