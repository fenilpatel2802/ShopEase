package com.wipro.shopease.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wipro.shopease.domain.model.UserData

@Database(
    entities = [
        UserData::class
    ],
    version = 1
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}