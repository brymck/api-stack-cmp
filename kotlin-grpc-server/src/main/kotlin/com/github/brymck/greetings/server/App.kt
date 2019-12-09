package com.github.brymck.greetings.server

import com.github.brymck.greetings.v1.GreetRequest
import com.github.brymck.greetings.v1.GreetResponse
import com.github.brymck.greetings.v1.GreetingsApiGrpc
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Server that manages startup/shutdown of a `Greeter` server.
 *
 * Note: this file was automatically converted from Java
 */
class App {
    private var server: Server? = null

    @Throws(IOException::class)
    private fun start() {
        /* The port on which the server should run */
        val port = 50051
        server = ServerBuilder.forPort(port)
            .addService(GreeterImpl())
            .build()
            .start()
        logger.log(Level.INFO, "Server started, listening on {0}", port)
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down")
                this@App.stop()
                System.err.println("*** server shut down")
            }
        })
    }

    private fun stop() {
        server?.shutdown()
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    @Throws(InterruptedException::class)
    private fun blockUntilShutdown() {
        server?.awaitTermination()
    }

    internal class GreeterImpl : GreetingsApiGrpc.GreetingsApiImplBase() {
        override fun greet(req: GreetRequest, responseObserver: StreamObserver<GreetResponse>) {
            val reply = GreetResponse.newBuilder().setMessage("Hello ${req.name}").build()
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
        }
    }

    companion object {
        private val logger = Logger.getLogger(App::class.java.name)

        /**
         * Main launches the server from the command line.
         */
        @Throws(IOException::class, InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val server = App()
            server.start()
            server.blockUntilShutdown()
        }
    }
}
