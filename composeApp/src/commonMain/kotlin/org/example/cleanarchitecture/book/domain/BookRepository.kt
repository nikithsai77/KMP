package org.example.cleanarchitecture.book.domain

import kotlinx.coroutines.flow.Flow
import org.example.cleanarchitecture.core.domain.DataError
import org.example.cleanarchitecture.core.domain.EmptyResult
import org.example.cleanarchitecture.core.domain.Result

interface BookRepository {
    fun getFavoriteBooks(): Flow<List<Book>>
    suspend fun deleteFromFavorites(id: String)
    fun isBookFavorite(id: String): Flow<Boolean>
    suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local>
    suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote>
    suspend fun getBookDescription(bookId: String): Result<String?, DataError>
}