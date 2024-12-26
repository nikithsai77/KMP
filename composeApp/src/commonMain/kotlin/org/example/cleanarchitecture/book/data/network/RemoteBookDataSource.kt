package org.example.cleanarchitecture.book.data.network

import org.example.cleanarchitecture.book.data.dto.BookWorkDto
import org.example.cleanarchitecture.book.data.dto.SearchResponseDto
import org.example.cleanarchitecture.core.domain.DataError
import org.example.cleanarchitecture.core.domain.Result

interface RemoteBookDataSource {
    suspend fun searchBooks(query: String, resultLimit: Int? = null): Result<SearchResponseDto, DataError.Remote>
    suspend fun getBookDetails(bookWorkId: String) : Result<BookWorkDto, DataError.Remote>
}