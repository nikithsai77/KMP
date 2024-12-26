package org.example.cleanarchitecture.book.presentation.bookDetail.component

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.cleanarchitecture.app.Route
import org.example.cleanarchitecture.book.domain.BookRepository
import org.example.cleanarchitecture.book.presentation.bookDetail.BookDetailAction
import org.example.cleanarchitecture.book.presentation.bookDetail.BookDetailState
import org.example.cleanarchitecture.core.domain.onSuccess

class BookDetailViewModel(private val bookRepository: BookRepository, savedStateHandle: SavedStateHandle): ViewModel() {
    private val bookId = savedStateHandle.toRoute<Route.BookDetail>().id
    private val _state = MutableStateFlow(BookDetailState())
    val state = _state
        .onStart {
            fetchBookDescription()
            observeFavoriteStatus()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L), _state.value)

    fun onAction(action: BookDetailAction) {
        when(action) {
            is BookDetailAction.OnSelectedBookChange -> {
                _state.update {
                    it.copy(book = action.book)
                }
            }
            is BookDetailAction.OnFavoriteClick -> {
                viewModelScope.launch {
                    if (state.value.isFavorite) bookRepository.deleteFromFavorites(bookId)
                    else {
                        state.value.book?.let { book ->
                            bookRepository.markAsFavorite(book)
                        }
                    }
                }
            }
            else -> Unit
        }
    }

    private fun observeFavoriteStatus() {
        bookRepository.isBookFavorite(bookId).onEach { isFavorite ->
            _state.update { it.copy(
                isFavorite = isFavorite
            ) }
        }.launchIn(viewModelScope)
    }

    private fun fetchBookDescription() {
        viewModelScope.launch {
            bookRepository.getBookDescription(bookId).onSuccess { description ->
                    _state.update {
                        it.copy(descriptionLoading = false, book = it.book?.copy(description = description))
                    }
                }
        }
    }

}