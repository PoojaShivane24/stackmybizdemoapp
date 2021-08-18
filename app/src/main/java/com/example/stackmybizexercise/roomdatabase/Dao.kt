package com.example.stackmybizexercise.roomdatabase

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    fun insert(entity: UserEntity) : Long

    @Query("Select * from UserEntity where userName = :userName AND password = :password")
    fun getUser(userName : String, password : String) : UserEntity?

    @Query("Select * from UserEntity")
    fun getUserList() : List<UserEntity?>
}