package org.example.cleanarchitecture.book.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object BookDatabaseConstructor : RoomDatabaseConstructor<FavoriteBookDataBase> {
    override fun initialize(): FavoriteBookDataBase
}