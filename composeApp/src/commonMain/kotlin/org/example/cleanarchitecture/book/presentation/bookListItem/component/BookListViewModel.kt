package org.example.cleanarchitecture.book.presentation.bookListItem.component

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.cleanarchitecture.book.common.BookListAction
import org.example.cleanarchitecture.book.domain.Book
import org.example.cleanarchitecture.book.domain.BookRepository
import org.example.cleanarchitecture.book.presentation.bookDetail.bookListItem.components.BookListState
import org.example.cleanarchitecture.core.domain.onError
import org.example.cleanarchitecture.core.domain.onSuccess
import org.example.cleanarchitecture.core.domain.toUiText

@FlowPreview
class BookListViewModel(private val bookRepository: BookRepository): ViewModel() {
    private var searchJob: Job? = null
    private var cachedBooks = emptyList<Book>()
    private var observeFavoriteJob: Job? = null

    private val _state = MutableStateFlow(BookListState())
    val state = _state.onStart {
            if(cachedBooks.isEmpty()) observeSearchQuery()
            observeFavoriteBooks()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            _state.value
        )

    fun onAction(action: BookListAction) {
        when(action) {
            is BookListAction.OnBookClick -> {}
            is BookListAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(searchQuery = action.query)
                }
            }
            is BookListAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTabIndex = action.index)
                }
            }
        }
    }

    private fun observeFavoriteBooks() {
        observeFavoriteJob?.cancel()
        observeFavoriteJob = bookRepository.getFavoriteBooks().onEach { favoriteBooks ->
                _state.update { it.copy(
                    favoriteBooks = favoriteBooks
                ) }
            }.launchIn(viewModelScope)
    }

    private fun observeSearchQuery() {
        state.map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(timeoutMillis = 500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                errorMessage = null,
                                searchResults = cachedBooks
                            )
                        }
                    }
                    query.length >= 2 -> {
                        searchJob?.cancel()
                        searchJob = searchBooks(query)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun searchBooks(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        bookRepository.searchBooks(query)
            .onSuccess { newBookList ->
                _state.update {
                    it.copy(isLoading = false,
                            errorMessage = null,
                            searchResults = newBookList)
                }
            }
            .onError { error ->
                _state.update {
                    it.copy(isLoading = false,
                        errorMessage = error.toUiText(),
                        searchResults = emptyList()
                    )
                }
            }
    }

}