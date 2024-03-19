package com.wipro.shopease.domain.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wipro.shopease.domain.model.UserData

@Dao
interface UserDao {

    @Insert
    fun insertUserData(userData: UserData)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserList(id: Int): UserData?

    @Query("SELECT uId FROM user WHERE id = :id  LIMIT 1")
    fun getExistUserData(id: Int?): Int?

}