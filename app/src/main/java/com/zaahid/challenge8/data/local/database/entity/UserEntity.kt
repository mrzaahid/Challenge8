package com.zaahid.challenge8.data.local.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId :Int = 0,
    @ColumnInfo(name = "username")
    var username : String,
    @ColumnInfo(name = "useremail")
    var useremail : String?,
    @ColumnInfo(name = "userpassword")
    var userpassword : String?,
    @ColumnInfo(name = "alamat")
    var alamat : String?
) :Parcelable