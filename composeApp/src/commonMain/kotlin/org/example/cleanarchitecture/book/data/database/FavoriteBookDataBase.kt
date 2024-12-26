package org.example.cleanarchitecture.book.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BookEntity::class],
    version = 1
)
@TypeConverters(
    StringListTypeConverter::class
)
abstract class FavoriteBookDataBase : RoomDatabase() {
    abstract val dao: FavoriteBookDao

    companion object {
        const val DB_NAME = "bookFavorite.db"
    }
}