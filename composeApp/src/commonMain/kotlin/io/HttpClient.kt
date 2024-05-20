package io

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

private val httpClientModule = DI.Module("httpClientModule") {
    bind<HttpClient>() with singleton { createHttpClient() }
}

private val settingsModule = DI.Module("settingsModule") {
    bind<Settings>() with singleton { createPreferenceSettings() }
}

val kodein = DI {
    import(httpClientModule)
    import(settingsModule)
}

expect fun httpClientFactory(): HttpClient

internal fun createHttpClient(enableLogging: Boolean = true): HttpClient {
    return httpClientFactory().config {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        if(enableLogging) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }
}

expect val Dispatchers.IO: CoroutineDispatcher

suspend inline fun <reified T> handleErrors(
    crossinline response: suspend () -> HttpResponse
): T = withContext(Dispatchers.Unconfined) {

    val result = try {
        response()
    } catch(e: IOException) {
        throw CustomException(CustomError.ServiceUnavailable)
    }

    when(result.status.value) {
        in 200..299 -> Unit
        in 400..499 -> throw CustomException(CustomError.ClientError)
        500 -> throw CustomException(CustomError.ServerError)
        else -> throw CustomException(CustomError.UnknownError)
    }

    return@withContext try {
        result.body<T>()
    } catch(e: Exception) {
        throw CustomException(CustomError.ServerError)
    }

}

enum class CustomError {
    ServiceUnavailable,
    ClientError,
    ServerError,
    UnknownError
}

class CustomException(error: CustomError): Exception(
    "Something went wrong: $error"
)