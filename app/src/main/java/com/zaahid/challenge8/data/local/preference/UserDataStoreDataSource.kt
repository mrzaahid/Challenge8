package com.zaahid.challenge8.data.local.preference

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface UserDataStoreDataSource {
    suspend fun setUserPref(id:Int, name:String, email:String, address:String)
    fun getPref (context: Context): Flow<Preferences>
    fun getUserId() : Flow<Int>
    fun getUsername() :Flow<String>
    fun getUserEmail():Flow<String>
    fun getUserAddres():Flow<String>
    suspend fun setUserLang(lang:String)
    fun getUserLang():Flow<String>
    suspend fun setImageString(value : String)
    fun getImageString():Flow<String>
    suspend fun setSwitch(boolean: Boolean)
    fun getSwitch():Flow<Boolean>
}
class UserDataStoreDataSourceImpl(private val userPref: UserDataStoreManager):
    UserDataStoreDataSource {
    override suspend fun setUserPref(id: Int, name: String, email: String, address: String) {
        userPref.setUserPref(id,name,email,address)
    }

    override fun getPref(context: Context): Flow<Preferences> {
        return userPref.getPreferences(context)
    }

    override fun getUserId(): Flow<Int> {
        return userPref.userID
    }

    override fun getUsername(): Flow<String> {
        return userPref.userName
    }

    override fun getUserEmail(): Flow<String> {
        return userPref.userEmail
    }

    override fun getUserAddres(): Flow<String> {
        return userPref.userAddress
    }

    override suspend fun setUserLang(lang: String) {
        userPref.setUserLang(lang)
    }

    override fun getUserLang(): Flow<String> {
        return userPref.userLang
    }

    override suspend fun setImageString(value: String) {
        userPref.setImageStringPref(value)
    }

    override fun getImageString(): Flow<String> {
        return userPref.imageString
    }

    override suspend fun setSwitch(boolean: Boolean) {
        userPref.setSwitch(boolean)
    }

    override fun getSwitch(): Flow<Boolean> {
        return userPref.getSwitch
    }

}