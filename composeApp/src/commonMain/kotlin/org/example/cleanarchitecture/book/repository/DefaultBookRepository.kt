package org.example.cleanarchitecture.book.repository

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.cleanarchitecture.book.data.database.FavoriteBookDao
import org.example.cleanarchitecture.book.data.mappers.toBook
import org.example.cleanarchitecture.book.data.mappers.toBookEntity
import org.example.cleanarchitecture.book.data.network.RemoteBookDataSource
import org.example.cleanarchitecture.book.domain.Book
import org.example.cleanarchitecture.book.domain.BookRepository
import org.example.cleanarchitecture.core.domain.DataError
import org.example.cleanarchitecture.core.domain.EmptyResult
import org.example.cleanarchitecture.core.domain.Result
import org.example.cleanarchitecture.core.domain.map

class DefaultBookRepository(private val remoteBookDataSource: RemoteBookDataSource, private val dao: FavoriteBookDao) : BookRepository
{
    override fun getFavoriteBooks(): Flow<List<Book>> {
        return dao.getFavoriteBooks().map {
            it.map { bookEntity ->
                bookEntity.toBook()
            }
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
       dao.deleteFavoriteBook(id)
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return dao.getFavoriteBooks().map {
            it.any { new ->
                new.id == id
            }
        }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            dao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch (e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
      return remoteBookDataSource.searchBooks(query).map { dto ->
            dto.results.map { it.toBook() }
        }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
       val localResult = dao.getFavoriteBook(id = bookId)
       return if (localResult != null) Result.Success(localResult.description)
       else remoteBookDataSource.getBookDetails(bookId).map { it.description }
    }

}