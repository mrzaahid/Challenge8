package com.zaahid.challenge8.data.local.preference

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStoreManager @Inject constructor(private val context : Context) {

    fun getPreferences(context: Context): Flow<Preferences>{
        return context.userDataStore.data
    }
    suspend fun setUserPref(id:Int, name:String, email:String, Address:String){
        context.userDataStore.edit {
            it[USER_ID] = id
            it[USERNAME] = name
            it[USER_EMAIL] = email
            it[USER_ADDRESS] = Address
        }
    }
    val userID : Flow<Int>
        get() = context.userDataStore.data.map {preferences->
            preferences[USER_ID] ?: 0
        }
    val userName : Flow<String>
        get() = context.userDataStore.data.map {
            it[USERNAME] ?: ""
        }
    val userEmail :Flow<String>
        get() = context.userDataStore.data.map {
            it[USER_EMAIL] ?: ""
        }
    val userAddress :Flow<String>
        get() = context.userDataStore.data.map {
            it[USER_ADDRESS] ?: ""
        }
    suspend fun setUserLang(lang : String){
        context.userDataStore.edit {
            it[USER_LANG] = lang
        }
    }
    val userLang :Flow<String>
        get() = context.userDataStore.data.map {
            it[USER_LANG] ?: "en-US"
        }
    suspend fun setImageStringPref(string: String){
        context.userDataStore.edit {
            it[IMAGE_STRING] = string
        }
    }
    val imageString : Flow<String>
        get() = context.userDataStore.data.map {
            it[IMAGE_STRING]?:""
        }
    suspend fun setSwitch(boolean: Boolean){
        context.userDataStore.edit {
            it[SWITCH_BOOL] = boolean
        }
    }
    val getSwitch : Flow<Boolean>
    get() = context.userDataStore.data.map {
        it[SWITCH_BOOL]?:false
    }


    companion object{
        private const val DATASTORE_NAME = "user_preference"
        private val USER_ID = intPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ADDRESS = stringPreferencesKey("user_address")
        private val USER_LANG = stringPreferencesKey("user_lang")
        private val IMAGE_STRING = stringPreferencesKey("image_string")
        private val SWITCH_BOOL = booleanPreferencesKey("switch_bool")

        private val Context.userDataStore by preferencesDataStore(
            name = DATASTORE_NAME
        )
    }
}