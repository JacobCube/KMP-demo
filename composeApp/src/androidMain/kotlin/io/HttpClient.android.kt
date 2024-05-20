package io

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val Dispatchers.IO: CoroutineDispatcher
    get() = Dispatchers.IO

actual fun httpClientFactory(): HttpClient  = HttpClient(Android){
    engine {
        connectTimeout = 100_000
        socketTimeout = 100_000
        //proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080))
    }
}