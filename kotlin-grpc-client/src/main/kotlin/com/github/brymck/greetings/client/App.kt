package com.github.brymck.greetings.client

import com.github.brymck.greetings.apis.GreetingsApi
import com.github.brymck.greetings.models.Request
import com.github.brymck.greetings.v1.GreetRequest
import com.github.brymck.greetings.v1.GreetingsApiGrpc
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

private val logger = KotlinLogging.logger {}

/**
 * A simple client that requests a greeting from the [HelloWorldServer].
 */
class App(host: String, port: Int) {
    private val channel = ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext()
        .build()
    private val blockingStub = GreetingsApiGrpc.newBlockingStub(channel)

    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    /** Say hello to server.  */
    fun greet(name: String) {
        // logger.info("Will try to greet $name...")
        val request = GreetRequest.newBuilder().setName(name).build()
        val response =  try {
            blockingStub.greet(request)
        } catch (e: StatusRuntimeException) {
            logger.warn("RPC failed: $e")
            return
        }

        // logger.info("Greeting: ${response.message}")
    }

    companion object {
        private const val PORT = 9090
        /**
         * Greet server. If provided, the first element of `args` is the name to use in the
         * greeting.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            testGrpc()
            testOpenApi()
        }

        private fun testGrpc() {
            val client = App("localhost", PORT)
            try {
                val bestTime = timeIt(20, 10000) {
                    client.greet("world")
                }
                logger.info("Best gRPC time in milliseconds: $bestTime")
            } finally {
                client.shutdown()
            }
        }

        private fun testOpenApi() {
            val client = GreetingsApi()
            val request = Request("world")
            val bestTime = timeIt(20, 10000) {
                client.createGreeting(request)
            }
            logger.info("Best OpenAPI time in milliseconds: $bestTime")
        }

        private inline fun timeIt(times: Int, number: Int, crossinline block: () -> Unit): Double {
            val times = (0 until times).map {
                measureTimeMillis {
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            repeat(number) {
                                launch {
                                    block()
                                }
                            }
                        }
                    }
                }
            }
            logger.info("Times: $times")
            val bestTime = times.min()
                ?: throw RuntimeException("Could not calculate time it takes to run provided block")
            return bestTime.toDouble() / number
        }
    }
}
