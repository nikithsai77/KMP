package org.example.cleanarchitecture.book.data.database

import androidx.room.Room
import android.content.Context
import androidx.room.RoomDatabase

class DatabaseFactoryImpl(context: Context) : DatabaseFactory {
    override var roomInstance: RoomDatabase.Builder<FavoriteBookDataBase> = Room.databaseBuilder(context = context, name = FavoriteBookDataBase.DB_NAME)
}