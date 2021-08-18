package com.example.stackmybizexercise.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDetailDao() : Dao
    companion object {
        var userDatabase: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            var tempInstance = userDatabase
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                var instance =
                    Room.databaseBuilder(context, UserDatabase::class.java, "userDatabase").build()
                userDatabase = instance
                return instance
            }
        }
    }
}