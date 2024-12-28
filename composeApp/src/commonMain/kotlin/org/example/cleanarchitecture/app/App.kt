package org.example.cleanarchitecture.app

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.FlowPreview
import org.example.cleanarchitecture.book.presentation.bookListItem.component.BookListViewModel
import org.example.cleanarchitecture.book.presentation.SelectedBookViewModel
import org.example.cleanarchitecture.book.presentation.bookDetail.BookDetailAction
import org.example.cleanarchitecture.book.presentation.bookDetail.component.BookDetailScreenRoot
import org.example.cleanarchitecture.book.presentation.bookDetail.component.BookDetailViewModel
import org.example.cleanarchitecture.book.presentation.bookListItem.component.BookListScreenRoot
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(FlowPreview::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val controller = rememberNavController()
        NavHost(navController = controller, startDestination = Route.BookGraph) {
            navigation<Route.BookGraph>(startDestination = Route.BookList) {
                composable<Route.BookList>(
                    //RTL
                    exitTransition = { slideOutHorizontally() },
                    //TLR
                    popEnterTransition = { slideInHorizontally() }
                ) {
                val viewModel = koinViewModel<BookListViewModel>()
                val selectedBookViewModel = it.sharedKoinViewModel<SelectedBookViewModel>(navController = controller)

                //This Effect will be called when the composable launched for the first time and
                //when the composable got active again when it came to this screen from another screen.
                LaunchedEffect(key1 = true) {
                    selectedBookViewModel.onSelectedBook(book = null)
                }

                BookListScreenRoot(viewModel = viewModel) { book ->
                    selectedBookViewModel.onSelectedBook(book)
                    controller.navigate(Route.BookDetail(id = book.id))
                }
            }
            composable<Route.BookDetail>(
                enterTransition = { slideInHorizontally { offset -> offset } },
                exitTransition = { slideOutHorizontally { offset -> offset } }
            ) { entry ->
                val selectedBookViewModel = entry.sharedKoinViewModel<SelectedBookViewModel>(navController = controller)
                val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()
                val viewModel = koinViewModel<BookDetailViewModel>()

                LaunchedEffect(key1 = true) {
                    selectedBook?.let {
                        viewModel.onAction(BookDetailAction.OnSelectedBookChange(book = it))
                    }
                }
                BookDetailScreenRoot(viewModel = viewModel) {
                    controller.navigateUp()
                }
              }
            }
        }
    }
}

@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(navController: NavController) : T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = navController.getBackStackEntry(navGraphRoute)
    return koinViewModel(viewModelStoreOwner = parentEntry)
}