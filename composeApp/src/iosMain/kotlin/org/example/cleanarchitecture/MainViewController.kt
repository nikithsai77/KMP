package org.example.cleanarchitecture

import androidx.compose.ui.window.ComposeUIViewController
import org.example.cleanarchitecture.app.App
import org.example.cleanarchitecture.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) {
    App()
}