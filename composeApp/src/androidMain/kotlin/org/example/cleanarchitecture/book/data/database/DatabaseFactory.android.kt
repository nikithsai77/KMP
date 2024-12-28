package org.example.cleanarchitecture.book.data.database

import androidx.room.Room
import android.content.Context
import androidx.room.RoomDatabase

class DatabaseFactoryImpl(private val context: Context) : DatabaseFactory {
    override var roomInstance: RoomDatabase.Builder<FavoriteBookDataBase> = getInstance()

    private fun getInstance(): RoomDatabase.Builder<FavoriteBookDataBase> {
        val dbFile = context.getDatabasePath(FavoriteBookDataBase.DB_NAME)
        return Room.databaseBuilder(context = context, name = dbFile.absolutePath)
    }

}