package com.example

import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.PropertySource
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.rx3.rxSingle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.URL

@MicronautTest
class DemoTest {
    private var httpClient: HttpClient? = null

    @BeforeEach
    fun beforeEach() {
        val embeddedServer = ApplicationContext.run(
            EmbeddedServer::class.java, PropertySource.of(
                "test", emptyMap()
            )
        )
        httpClient = HttpClient.create(URL("http://${embeddedServer.host}:${embeddedServer.port}"))
    }

    @Test
    fun testItDoesntWork() {
        rxSingle {
            val req = HttpRequest.GET<Model>("/api/v1/getJava")
            val resp = assertThrows< HttpClientResponseException> { httpClient!!.exchange(req, Map::class.java).awaitSingle() }
            val body = resp.response.body.get() as Map<String,Map<String, List<Map<String, String>>>>
            assert(
                body["_embedded"]
                ?.get("errors")
                ?.get(0)?.get("message")
                ?.equals("More than 1 route matched the incoming request. The following routes matched /api/v1/getJava: GET - /api/v1/getJava, GET - /api/v1/getJava")!!
            )
        }.blockingGet()
    }


}
