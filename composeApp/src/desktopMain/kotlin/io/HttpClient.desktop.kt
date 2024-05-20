package io

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache5.Apache5
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val Dispatchers.IO: CoroutineDispatcher
    get() = Dispatchers.IO

actual fun httpClientFactory(): HttpClient = HttpClient(Apache5) {
    engine {
        //...
    }
}