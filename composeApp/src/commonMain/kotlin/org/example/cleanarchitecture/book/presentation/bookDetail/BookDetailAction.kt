package org.example.cleanarchitecture.book.presentation.bookDetail

import org.example.cleanarchitecture.book.domain.Book

sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavoriteClick: BookDetailAction
    data class OnSelectedBookChange(val book: Book): BookDetailAction
}