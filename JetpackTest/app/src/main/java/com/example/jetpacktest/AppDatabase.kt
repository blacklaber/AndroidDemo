package com.example.jetpacktest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(version = 3, entities = [User::class,Book::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao

    abstract fun BookDao() : BookDao

    companion object {

        val Migration_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table Book(id integer primary key autoincrement not null,name text not null,pages integer not null)")
            }
        }

        val Migration_2_3 = object : Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("alter table Book add column author text not null default 'unknown'")
            }
        }
        private var instance: AppDatabase? = null

        @Synchronized
        fun getDataBase(context: Context): AppDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,"app_database")
                .addMigrations(Migration_1_2, Migration_2_3)
                .build().apply {
                    instance = this
                }
        }
    }
}