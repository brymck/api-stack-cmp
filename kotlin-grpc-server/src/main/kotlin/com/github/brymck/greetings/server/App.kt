package com.github.brymck.greetings.server

import io.grpc.ServerBuilder
import mu.KotlinLogging
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {}

/**
 * Server that manages startup/shutdown of a `Greeter` server.
 *
 * Note: this file was automatically converted from Java
 */
open class App {
    private val server = ServerBuilder.forPort(PORT)
        .addService(GreetingsApiImpl())
        .build()

    private fun start() {
        /* The port on which the server should run */
        server.start()
        logger.info("Server started, listening on $PORT")
        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down")
            this@App.stop()
            System.err.println("*** server shut down")
        })
    }

    private fun stop() {
        server.shutdown()
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private fun blockUntilShutdown() {
        server.awaitTermination()
    }

    companion object {
        private const val PORT = 9090

        /**
         * Main launches the server from the command line.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val app = App()
            app.start()
            app.blockUntilShutdown()
        }
    }
}
