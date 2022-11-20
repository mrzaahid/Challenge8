package com.zaahid.challenge8.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.zaahid.challenge8.data.local.AppDataBase
import com.zaahid.challenge8.data.local.database.dao.UserDao
import com.zaahid.challenge8.data.local.database.datasource.UserDataSource
import com.zaahid.challenge8.data.local.database.datasource.UserDataSourceImpl
import com.zaahid.challenge8.data.local.preference.UserDataStoreDataSource
import com.zaahid.challenge8.data.local.preference.UserDataStoreDataSourceImpl
import com.zaahid.challenge8.data.local.preference.UserDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val DB_NAME = "myUser.DB"
    @Singleton
    @Provides
    fun provideAppDataBase(
        @ApplicationContext context: Context
    ): AppDataBase {
        val passphrase: ByteArray =
            SQLiteDatabase.getBytes("myUser-hashed".toCharArray())
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            DB_NAME
            )
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao( appDataBase: AppDataBase): UserDao {
        return appDataBase.userDao()
    }
    @Singleton
    @Provides
    fun provideUserDataSource(userDao: UserDao): UserDataSource {
        return UserDataSourceImpl(userDao)
    }


    @Singleton
    @Provides
    fun provideUserPreferenceDataStore(@ApplicationContext context: Context): UserDataStoreManager {
        return UserDataStoreManager(context)

    }
    @Singleton
    @Provides
    fun provideUserDataStoreDataSource(userDataStoreManager: UserDataStoreManager): UserDataStoreDataSource {
        return UserDataStoreDataSourceImpl(userDataStoreManager)
    }
    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context):WorkManager{
        return WorkManager.getInstance(context)
    }
}