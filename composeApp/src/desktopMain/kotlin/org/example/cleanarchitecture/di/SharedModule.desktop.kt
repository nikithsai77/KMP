package org.example.cleanarchitecture.di

import io.ktor.client.engine.okhttp.OkHttp
import org.example.cleanarchitecture.book.data.database.DatabaseFactory
import org.example.cleanarchitecture.book.data.database.DatabaseFactoryImpl
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val PlatformModule: Module
    get() = module {
        single { OkHttp.create() }
        single { DatabaseFactoryImpl() }.bind<DatabaseFactory>()
    }