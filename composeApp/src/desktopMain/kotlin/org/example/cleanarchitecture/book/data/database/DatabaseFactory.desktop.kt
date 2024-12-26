package org.example.cleanarchitecture.book.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

class DatabaseFactoryImpl : DatabaseFactory {

    override var roomInstance: RoomDatabase.Builder<FavoriteBookDataBase> = create()

    private fun create(): RoomDatabase.Builder<FavoriteBookDataBase> {
        val os = System.getProperty("os.name").lowercase() //It is used to get the os name.
        val userHome = System.getProperty("user.home") //Gets the user's home directory.
        //Operating systems typically use subdirectories within the user's home folder for application data storage.
        val appDataDir = when {
            os.contains(other = "win") -> File(System.getenv("APPDATA"), "Book") // APPDATA is the home directory in windows.
            os.contains(other = "mac") -> File(userHome, "Library/Application Support/Book") // Library and Application Support are sub dir in mac os of user home dir
            else -> File(userHome, ".local/share/Book") //Linux
        }
        if (!appDataDir.exists()) appDataDir.mkdirs()
        //creates a file obj for the db path
        val dbFile = File(appDataDir, FavoriteBookDataBase.DB_NAME) //It creates the book.db file in provided path.
        return Room.databaseBuilder(dbFile.absolutePath) //returns the book.db path with home dir.
    }
}