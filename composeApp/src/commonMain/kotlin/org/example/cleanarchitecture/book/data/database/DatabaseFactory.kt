package org.example.cleanarchitecture.book.data.database

import androidx.room.RoomDatabase

interface DatabaseFactory {
    var roomInstance: RoomDatabase.Builder<FavoriteBookDataBase>
}