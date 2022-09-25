package ru.netology.nmedia2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostDao = PostDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostDaoImpl.DDL))
                ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context, DDLs: Array<String>) = DbHelper(
            context, 1, "app.db", DDLs
        ).writableDatabase

    }
}

class DbHelper(context: Context, dbVersion: Int, dbName: String, private val DDLs: Array<String>) :
        SQLiteOpenHelper(context, dbName, null, dbVersion) {
            override fun onCreate(db: SQLiteDatabase) {
                DDLs.forEach {
                    db.execSQL(it)
                }
            }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}