package org.example.cleanarchitecture.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.example.cleanarchitecture.book.data.database.DatabaseFactory
import org.example.cleanarchitecture.book.data.database.FavoriteBookDataBase
import org.example.cleanarchitecture.book.data.network.KtorRemoteBookDataSource
import org.example.cleanarchitecture.book.data.network.RemoteBookDataSource
import org.example.cleanarchitecture.book.domain.BookRepository
import org.example.cleanarchitecture.book.presentation.bookListItem.component.BookListViewModel
import org.example.cleanarchitecture.book.presentation.SelectedBookViewModel
import org.example.cleanarchitecture.book.presentation.bookDetail.component.BookDetailViewModel
import org.example.cleanarchitecture.book.repository.DefaultBookRepository
import org.example.cleanarchitecture.core.data.HttpClientFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val PlatformModule: Module

val SharedModule = module {
    single {
        HttpClientFactory.create(get())
    }
    singleOf(::KtorRemoteBookDataSource).bind<RemoteBookDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()
    single {
        get<DatabaseFactory>().roomInstance
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDataBase>().dao }
    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
}