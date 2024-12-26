package org.example.cleanarchitecture.book.data.database

import androidx.room.Room
import androidx.room.RoomDatabase

class DatabaseFactoryImpl: DatabaseFactory {
    override var roomInstance: RoomDatabase.Builder<FavoriteBookDataBase> = create()

    private fun create() : RoomDatabase.Builder<FavoriteBookDataBase> {
        val dbFile = getDocPath()+FavoriteBookDataBase.DB_NAME
        return Room.databaseBuilder<FavoriteBookDataBase>(name = dbFile)
    }

    private fun getDocPath() : String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}