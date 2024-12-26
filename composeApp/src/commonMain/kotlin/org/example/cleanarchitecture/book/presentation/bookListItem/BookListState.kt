package org.example.cleanarchitecture.book.presentation.bookDetail.bookListItem.components

import org.example.cleanarchitecture.book.domain.Book
import org.example.cleanarchitecture.core.presentation.UIText

data class BookListState(
    val searchQuery: String = "harry",
    val searchResults: List<Book> = emptyList(),
    val favoriteBooks: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val selectedTabIndex: Int = 0,
    val errorMessage: UIText? = null
)