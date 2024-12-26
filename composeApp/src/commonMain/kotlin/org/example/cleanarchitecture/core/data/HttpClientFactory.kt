package org.example.cleanarchitecture.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(engine: HttpClientEngine) : HttpClient {
        return HttpClient(engine) {
            //This is used for automatically parsing conversion from json to model class,
            //If any key is missing in model class then parsing causes crashes so
            //This block of code will ignore the missing keys in parsing.
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            //This is used for time out connection.
            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }
            //This is used for to print the response in log's.
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("kk response: $message")
                    }
                }
                level = LogLevel.ALL
            }
            //This is used for the all req must be send in json form.
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

}