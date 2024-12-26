package org.example.cleanarchitecture

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.cleanarchitecture.app.App
import org.example.cleanarchitecture.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Clean Architecture",
        ) {
            App()
        }
    }
}