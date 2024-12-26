package org.example.cleanarchitecture.book.presentation.bookDetail

import org.example.cleanarchitecture.book.domain.Book

data class BookDetailState(
    val descriptionLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)
