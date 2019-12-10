package com.github.brymck.greetings.server

import com.github.brymck.greetings.v1.GreetRequest
import com.github.brymck.greetings.v1.GreetResponse
import com.github.brymck.greetings.v1.GreetingsApiGrpc
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GreetingsService : GreetingsApiGrpc.GreetingsApiImplBase() {
    override fun greet(request: GreetRequest, responseObserver: StreamObserver<GreetResponse>) {
        val name = request.name
        val response = GreetResponse.newBuilder().setMessage("Hello, $name!").build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    fun greet(name: String): String {
        return "Hello, $name!"
    }
}
